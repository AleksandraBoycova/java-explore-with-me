package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.UserDto;
import ru.yandex.practicum.exception.AlreadyExistsException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.UserEntity;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.util.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto addUser(UserDto user) {
        if (userRepository.existsByName(user.getName())) {
            throw new AlreadyExistsException("Пользователь с таким именем уже существует: " + user.getName());
        }
        UserEntity savedUser = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDto(savedUser);
    }


    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        if (size != 0) {
            return getUsersWithPage(userIds, from, size);
        }
        List<UserEntity> users = userRepository.findAllByUserIdIn(userIds);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersWithPage(List<Long> userIds, Integer from, Integer size) {
        final PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<UserEntity> users = userRepository.findAllByUserIdIn(pageRequest, userIds);
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Такого пользователя нет " + id);
        }
        userRepository.deleteById(id);
    }
}
