package ru.yandex.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.CompilationEntity;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {

    List<CompilationEntity> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
