package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.CategoryDto;
import ru.practicum.comment.CommentDto;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.event.EventDto;
import ru.yandex.practicum.service.CategoriesService;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.CompilationService;
import ru.yandex.practicum.service.EventService;
import ru.yandex.practicum.util.EventsSortedBy;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicApiController {

    private final CompilationService compilationService;
    private final CategoriesService categoriesService;
    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Public API: Вызван метод getCategories с параметрами from {}, size {}", from, size);
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.debug("Public API: Вызван метод getCategoryById, catId {}", catId);
        return categoriesService.getCategoryById(catId);
    }

    @GetMapping("events")
    public List<EventDto> getEventsFiltered(@RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<Long> categoriesIds,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(name = "sort", required = false) EventsSortedBy sorted,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        log.debug("Public API: Вызван метод getEventsFiltered");
        return eventService.getEvents(text, categoriesIds, paid, rangeStart, rangeEnd,
                onlyAvailable, sorted, from, size, request);
    }

    @GetMapping("events/{id}")
    public EventDto getFullEventById(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Public API: Вызван метод getCategoryById, id {}", id);
        return eventService.getEventById(id, request);
    }

    @GetMapping("compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.debug("Public API: Вызван метод getCompilationById for id {}", compId);
        return compilationService.getCompilationById(compId);
    }


    @GetMapping("compilations")
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Public API: Вызван метод getCompilations with parameters: pinned {} from {} size {} ", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/comments")
    public List<CommentDto> getAllComments() {
        return commentService.getAllComments();
    }

    @PostMapping("/events/{eventId}/comment/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long eventId, @Valid @RequestBody CommentDto comment,
                                 @PathVariable Long userId) {
        log.info("Private: Вызван метод addComment, userId eventId {} {}", userId, eventId);
        return commentService.addComment(eventId, comment, eventId);
    }


    @GetMapping("/comments/all/{userId}")
    public List<CommentDto> getAllUserComments(@PathVariable Long userId) {
        log.info("Private: Вызван метод getAllUserComments, userId {}", userId);
        return commentService.getAllUserComments(userId);
    }


    @GetMapping("/comments/{comId}")
    public CommentDto getCommentById(@PathVariable Long comId) {
        return commentService.getCommentById(comId);
    }
}
