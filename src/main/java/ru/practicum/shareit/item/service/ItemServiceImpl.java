package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.StatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.AddCommentDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
        Item item = ItemMapper.INSTANCE.toModel(itemDto);
        item.setOwner(owner);
        Item addedItem = itemRepository.save(item);
        log.info("Пользователь с id '{}' добавил новую вещь: {}.", userId, addedItem);
        return ItemMapper.INSTANCE.toDto(addedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, long itemId, ItemUpdateDto itemUpdateDto) throws NotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id '" + itemId + "' не найдена."));
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("У пользователя с id '" + userId + "' не найдена вещь с id '" + itemId + "'.");
        }
        item.setName(Optional.ofNullable(itemUpdateDto.getName())
                .orElse(item.getName()));
        item.setDescription(Optional.ofNullable(itemUpdateDto.getDescription())
                .orElse(item.getDescription()));
        item.setAvailable(Optional.ofNullable(itemUpdateDto.getAvailable())
                .orElse(item.getAvailable()));

        return ItemMapper.INSTANCE.toDto(item);
    }

    @Override
    @Transactional
    public ItemDto findItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id '" + itemId + "' не найдена."));
        log.info("Получение вещи с id '{}': {}.", itemId, item);
        return ItemMapper.INSTANCE.toDto(item);
    }

    @Override
    @Transactional
    public List<ItemDto> findAllItemsByUserId(long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
        List<Item> items = itemRepository.findAllByOwner(owner);
        log.info("Получение всех вещей пользователя с id '{}'.", userId);
        return ItemMapper.INSTANCE.toDtoList(items);
    }

    @Override
    @Transactional
    public List<ItemDto> searchItems(String text) {
        List<Item> searchResult = itemRepository.findAllByNameIsLikeIgnoreCaseAndAvailableIsTrueOrDescriptionIsLikeIgnoreCaseAndAvailableIsTrue(
                text, text);
        log.info("Поиск вещей по запросу: {}.", text);
        return ItemMapper.INSTANCE.toDtoList(searchResult);
    }

    @Override
    @Transactional
    public CommentDto addCommentToItem(Long userId, Long itemId, AddCommentDto commentDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));;
        final Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id '" + itemId + "' не найдена."));;
        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerId(itemId, userId);
        checkIfUserCanAddComments(userId, itemId, bookings);
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        Comment savedComment = commentRepository.save(comment);
        log.info("Пользователь с id '{} добавил комментарий вещи с id '{}.", userId, itemId);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        return commentMapper.toDtoList(comments);
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId) {
        List<Long> itemId = userRepository.findItemIdsByUserId(userId);
        List<Comment> comments = commentRepository.findByItemId(itemId);
        return commentMapper.toDtoList(comments);
    }

    private void checkIfUserCanAddComments(Long userId, Long itemId, List<Booking> bookings) {
        boolean isAbleToAddComment = bookings.stream()
                .anyMatch(booking -> booking.getBooker().getId().equals(userId) && booking.getEnd().isBefore(LocalDateTime.now())
                        && booking.getStatus().equals(StatusEnum.APPROVED));
        if (!isAbleToAddComment) {
            throw new ItemUnavailableException("Пользователь с id '" + userId + "' не брал в аренду вещь с id '" +
                    itemId + "'.");
        }
    }
}
