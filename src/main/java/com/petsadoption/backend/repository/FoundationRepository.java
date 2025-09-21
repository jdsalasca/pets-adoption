package com.petsadoption.backend.repository;

import com.petsadoption.backend.entity.Foundation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoundationRepository extends JpaRepository<Foundation, UUID> {
}
