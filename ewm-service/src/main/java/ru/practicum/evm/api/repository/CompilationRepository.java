package ru.practicum.evm.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evm.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {
    @Query("select c from Compilation as c where (:pinned is null or c.pinned = :pinned)")
    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}