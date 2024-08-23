package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.page.OffsetPageRequest;
import ru.practicum.shareit.request.dto.AddItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper = ItemRequestMapper.INSTANCE;

    @Override
    @Transactional
    public ItemRequestDto addNewItemRequest(Long userId, AddItemRequestDto addItemRequestDto) {
        final User requester = findUser(userId);
        final ItemRequest itemRequest = itemRequestMapper.toModel(addItemRequestDto);
        itemRequest.setRequester(requester);
        final ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        log.info("Добавлен новый запрос с id '{}'.", savedRequest.getId());
        return itemRequestMapper.toDto(savedRequest);
    }

    @Transactional
    @Override
    public List<ItemRequestDto> getAllItemRequestsFromUser(final Long userId) {
        findUser(userId);
        final List<ItemRequest> requests = itemRequestRepository.findRequestsFromUser(userId);
        log.info("Получение всех запросов для пользователя с id '" + userId + "'.");
        return itemRequestMapper.toDtoList(requests);
    }

    @Transactional
    @Override
    public List<ItemRequestDto> getAvailableItemRequests(final Long userId, final Long from, final Integer size) {
        findUser(userId);
        final OffsetPageRequest pageRequest = OffsetPageRequest.of(from, size);
        final Page<ItemRequest> requests = itemRequestRepository.findAvailableRequests(userId, pageRequest);
        log.info("Получение списка запросов, начиная с '{}', по '{}' элемента на странице.", from, size);
        return itemRequestMapper.toDtoList(requests.getContent());
    }

    @Transactional
    @Override
    public ItemRequestDto getItemRequestById(final Long userId, final Long requestId) {
        findUser(userId);
        final ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id '" + requestId + "' не найден."));
        log.info("Получение запроса с id '{}'.", requestId);
        return itemRequestMapper.toDto(itemRequest);
    }


    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
    }
}
