package com.example.rental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;
    private Car sampleCar;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
        sampleCar = new Car("ABC123", "Toyota", true);
        carRepository.addCar(sampleCar);
    }

    @Test
    void testGetAllCars() {
        List<Car> cars = carRepository.getAllCars();
        assertEquals(1, cars.size());
        assertEquals("ABC123", cars.get(0).getRegistrationNumber());
    }

    @Test
    void testFindByRegistrationNumber() {
        Optional<Car> result = carRepository.findByRegistrationNumber("ABC123");
        assertTrue(result.isPresent());
        assertEquals(sampleCar, result.get());
    }

    @Test
    void testAddCar() {
        Car newCar = new Car("XYZ789", "Honda", false);
        carRepository.addCar(newCar);

        Optional<Car> found = carRepository.findByRegistrationNumber("XYZ789");
        assertTrue(found.isPresent());
        assertEquals("Honda", found.get().getModel());
    }

    @Test
    void testUpdateCar() {
        Car updatedCar = new Car("ABC123", "Peugeot", true); // same reg number, new model
        carRepository.updateCar(updatedCar);

        Optional<Car> found = carRepository.findByRegistrationNumber("ABC123");
        assertTrue(found.isPresent());
        assertEquals("Peugeot", found.get().getModel());
    }
}
