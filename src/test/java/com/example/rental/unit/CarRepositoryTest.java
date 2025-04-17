package com.example.rental.unit;

import com.example.rental.Car;
import com.example.rental.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository repository;
    private Car car;

    @BeforeEach
    void setUp() {
        repository = new CarRepository();
        car = new Car("ABC123", "Model S", true);
        repository.addCar(car);
    }

    @Test
    void testGetAllCars() {
        assertEquals(1, repository.getAllCars().size());
    }

    @Test
    void testFindByRegistrationNumber() {
        Optional<Car> found = repository.findByRegistrationNumber("ABC123");
        assertTrue(found.isPresent());
        assertEquals("Model S", found.get().getModel());
    }

    @Test
    void testAddCar() {
        Car newCar = new Car("DEF456", "Yaris", true);
        repository.addCar(newCar);
        assertEquals(2, repository.getAllCars().size());
    }

    @Test
    void testUpdateCar() {
        car.setAvailable(false);
        repository.updateCar(car);
        Optional<Car> updated = repository.findByRegistrationNumber("ABC123");
        assertTrue(updated.isPresent());
        assertFalse(updated.get().isAvailable());
    }
}
