package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User owner = userStorage.findById(userId);
        Item item = ItemMapper.INSTANCE.toModel(itemDto);
        item.setOwnerId(owner.getId());
        userStorage.addItemToUser(userId, item);
        Item addedItem = itemStorage.save(userId, item);
        log.info("Пользователь с id '{}' добавил новую вещь: {}.", userId, addedItem);
        return ItemMapper.INSTANCE.toDto(addedItem);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemUpdateDto itemUpdateDto) throws NotFoundException {
        userStorage.findById(userId);
        Item item = itemStorage.findById(itemId);
        if (item.getOwnerId() != userId) {
            throw new NotFoundException("У пользователя с id '" + userId + "' не найдена вещь с id '" + itemId + "'.");
        }

        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }

        Item updatedItem = itemStorage.update(itemId, itemUpdateDto);
        return ItemMapper.INSTANCE.toDto(updatedItem);
    }

    @Override
    public ItemDto findItemById(long itemId) {
        Item item = itemStorage.findById(itemId);
        log.info("Получение вещи с id '{}': {}.", itemId, item);
        return ItemMapper.INSTANCE.toDto(item);
    }

    @Override
    public List<ItemDto> findAllItemsByUserId(long userId) {
        userStorage.findById(userId);
        List<Item> items = itemStorage.findAllByUserId(userId);
        log.info("Получение всех вещей пользователя с id '{}'.", userId);
        return ItemMapper.INSTANCE.toDtoList(items);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> searchResult = itemStorage.search(text);
        log.info("Поиск вещей по запросу: {}.", text);
        return ItemMapper.INSTANCE.toDtoList(searchResult);
    }
}
