package ru.yandex.practicum.service;

import ru.practicum.comment.CommentDto;

import java.util.List;


public interface CommentService {

    CommentDto addComment(Long eventId, CommentDto comment, Long userId);

    List<CommentDto> getAllUserComments(Long userId);

    CommentDto updateComment(Long comId, CommentDto comment);

    CommentDto getCommentById(Long comId);

    List<CommentDto> getAllComments();
}
