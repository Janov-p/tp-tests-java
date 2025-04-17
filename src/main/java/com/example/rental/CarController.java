package com.example.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRentalService carRentalService;

    @GetMapping
    public List<Car> getAllCars() {
        return carRentalService.getAllCars();
    }

    @PostMapping("/rent/{registrationNumber}")
    public boolean rentCar(@PathVariable String registrationNumber) {
        return carRentalService.rentCar(registrationNumber);
    }

    @PostMapping("/return/{registrationNumber}")
    public void returnCar(@PathVariable String registrationNumber) {
        carRentalService.returnCar(registrationNumber);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        boolean success = carRentalService.addCar(car);
        if (success) {
            return ResponseEntity.ok("Car added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Registration number already exists");
        }
    }

    @GetMapping("/search")
    public List<Car> searchCarsByModel(@RequestParam String model) {
        return carRentalService.searchCarsByModel(model);
    }

}