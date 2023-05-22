package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.compilation.NewCompilationDto;
import ru.practicum.compilation.UpdateCompilationRequest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.CompilationEntity;
import ru.yandex.practicum.model.EventEntity;
import ru.yandex.practicum.repository.CompilationRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.service.CompilationService;
import ru.yandex.practicum.util.CompilationMapper;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Page<CompilationEntity> compilationEntities = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size, Sort.by("id")));
        return compilationEntities.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long id) {
        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой подборки нет " + id));
        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto compilation) {
        if (compilation.getEvents() != null && compilation.getEvents().size() != 0) {
            Set<Long> eventIds = compilation.getEvents();
            Set<EventEntity> events = new HashSet<>(eventRepository.findAllByIdIn(eventIds));
            CompilationEntity compilationEntity = CompilationMapper.toEntity(compilation);
            compilationEntity.setEvents(events);
            CompilationEntity savedCompil = compilationRepository.save(compilationEntity);
            return CompilationMapper.toDto(savedCompil);
        }
        CompilationEntity fromDto = CompilationMapper.toEntity(compilation);
        CompilationEntity savedCompil = compilationRepository.save(fromDto);
        return CompilationMapper.toDto(savedCompil);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        compilationRepository.deleteById(compId);
    }


    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) {
        CompilationEntity compilationFromDb = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        if (compilation.getEvents().size() != 0) {
            Set<Long> eventIds = compilation.getEvents();
            Set<EventEntity> events = new HashSet<>(eventRepository.findAllByIdIn(eventIds));
            compilationFromDb.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            compilationFromDb.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            compilationFromDb.setTitle(compilation.getTitle());
        }
        CompilationEntity updated = compilationRepository.save(compilationFromDb);
        return CompilationMapper.toDto(updated);
    }
}
