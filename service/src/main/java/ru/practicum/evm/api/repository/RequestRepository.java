package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.Request;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findAllByEventId(Long eventId);

    Collection<Object> findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);
}
