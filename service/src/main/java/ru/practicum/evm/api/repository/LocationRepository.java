package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.Location;

public interface LocationRepository extends JpaRepository<Location,Long> {
}
