package ru.peter.rest.FirstRestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.peter.rest.FirstRestApp.dto.MeasurementDTO;
import ru.peter.rest.FirstRestApp.models.Measurements;
import ru.peter.rest.FirstRestApp.services.MeasurementsService;
import ru.peter.rest.FirstRestApp.util.MeasurementErrorResponse;
import ru.peter.rest.FirstRestApp.util.MeasurementException;
import ru.peter.rest.FirstRestApp.util.MeasurementValidator;

import java.util.List;

import static ru.peter.rest.FirstRestApp.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementsService measurementsService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }
    @GetMapping
    public List<Measurements> getMeasurements() {
        return measurementsService.findAll();
    }
    @GetMapping("/rainyDaysCount")
    public long countRainyDays() {
        return measurementsService.findAll().stream().filter(Measurements::getRaining).count();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurements(@RequestBody @Valid MeasurementDTO measurementsDTO, BindingResult bindingResult) {
        Measurements measurements = convertToMeasurements(measurementsDTO);
        measurementValidator.validate(measurements, bindingResult);
        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);
        measurementsService.add(measurements);
        return ResponseEntity.ok(HttpStatus.OK);
    }



    private MeasurementDTO convertToDTO(Measurements measurements){
        return modelMapper.map(measurements, MeasurementDTO.class);
    }

    private Measurements convertToMeasurements(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurements.class);
    }
    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
