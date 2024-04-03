package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {
}
