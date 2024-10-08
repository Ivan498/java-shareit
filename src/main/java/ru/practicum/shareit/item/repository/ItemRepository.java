package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User owner);

    List<Item> findAllByNameIsLikeIgnoreCaseAndAvailableIsTrueOrDescriptionIsLikeIgnoreCaseAndAvailableIsTrue(String name, String description);
}
