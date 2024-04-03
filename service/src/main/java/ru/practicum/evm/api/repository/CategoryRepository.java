package ru.practicum.evm.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
