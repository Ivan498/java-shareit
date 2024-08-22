package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.AddItemRequestDto;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-id") long userId,
                                                 @Valid @RequestBody AddItemRequestDto addItemRequestDto) {
        return itemRequestClient.addItemRequest(userId, addItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getMyItemRequests(@RequestHeader("X-Sharer-User-id") long userId) {
        return itemRequestClient.getMyItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersItemRequests(@RequestHeader("X-Sharer-User-id") long userId,
                                                         @RequestParam(defaultValue = "0") Long from,
                                                         @RequestParam(defaultValue = "10")Integer size) {
        return itemRequestClient.getOtherUsersItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-id") long userId,
                                             @PathVariable Long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
