package ru.peter.rest.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peter.rest.FirstRestApp.models.Measurements;
import ru.peter.rest.FirstRestApp.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {
    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementsService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    public List<Measurements> findAll() {
        return measurementRepository.findAll();
    }
    @Transactional
    public void add(Measurements measurements) {
        enrich(measurements);
        measurementRepository.save(measurements);
    }
    public void enrich(Measurements measurements) {
        measurements.setSensor(sensorService.findByName(measurements.getSensor().getName()).get());
        measurements.setMeasurementDateTime(LocalDateTime.now());
    }
}
