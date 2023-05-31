package ru.yandex.practicum.util;

import ru.practicum.comment.CommentDto;
import ru.yandex.practicum.model.CommentEntity;

public class CommentMapper {

    private final EventMapper eventMapper = new EventMapper();
    private final UserMapper userMapper = new UserMapper();

    public CommentEntity toEntity(CommentDto commentDto) {
        CommentEntity entity = new CommentEntity();
        entity.setText(commentDto.getText());
        return entity;
    }

    public CommentDto toDto(CommentEntity entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setEvent(eventMapper.toShortDto(entity.getEvent()));
        dto.setAuthor(userMapper.toShortDto(entity.getAuthor()));
        dto.setCreatedOn(entity.getCreatedOn());
        return dto;
    }
}
