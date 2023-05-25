package ru.yandex.practicum.util;

import ru.practicum.user.UserDto;
import ru.yandex.practicum.model.UserEntity;

public class UserMapper {

    public UserEntity toEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setName(userDto.getName());
        return userEntity;
    }

    public UserDto toDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setName(userEntity.getName());
        return userDto;
    }

    public UserDto toShortDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setName(userEntity.getName());
        return userDto;
    }
}
