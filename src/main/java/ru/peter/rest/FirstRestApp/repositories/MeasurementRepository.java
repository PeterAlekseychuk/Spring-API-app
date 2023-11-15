package ru.peter.rest.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.peter.rest.FirstRestApp.models.Measurements;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurements, Integer> {
}
