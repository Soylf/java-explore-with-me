package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.Request;

public interface RequestRepository extends JpaRepository<Request,Long> {
}
