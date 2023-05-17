package ru.yandex.practicum.service;

import ru.practicum.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user);

    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);

    List<UserDto> getUsersWithPage(List<Long> userIds, Integer from, Integer size);

    void deleteUser(Long id);
}
