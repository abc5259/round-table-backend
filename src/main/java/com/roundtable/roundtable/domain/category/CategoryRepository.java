package com.roundtable.roundtable.domain.category;

import com.roundtable.roundtable.domain.house.House;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndHouse(String name, House house);

    Optional<Category> findByIdAndHouse(Long id, House house);
}