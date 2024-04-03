package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
