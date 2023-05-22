package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.CompilationEntity;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {
    @Query("SELECT e FROM CompilationEntity e " +
            "WHERE (:pinned is null or pinned is :pinned)")
    Page<CompilationEntity> findAllByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}