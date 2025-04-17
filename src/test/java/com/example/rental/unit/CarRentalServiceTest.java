package com.example.rental.unit;

import com.example.rental.Car;
import com.example.rental.CarRentalService;
import com.example.rental.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CarRentalServiceTest {

    private final CarRepository carRepository = mock(CarRepository.class);
    private CarRentalService service;

    private Car availableCar;
    private Car unavailableCar;

    @BeforeEach
    void setUp() throws Exception {
        service = new CarRentalService();

        var field = CarRentalService.class.getDeclaredField("carRepository");
        field.setAccessible(true);
        field.set(service, carRepository);

        availableCar = new Car("CAR001", "Model 3", true);
        unavailableCar = new Car("CAR002", "X5", false);
    }

    @Test
    void testGetAllCars() {
        when(carRepository.getAllCars()).thenReturn(List.of(availableCar, unavailableCar));
        List<Car> cars = service.getAllCars();
        assertEquals(2, cars.size());
    }

    @Test
    void testRentAvailableCar() {
        when(carRepository.findByRegistrationNumber("CAR001")).thenReturn(Optional.of(availableCar));
        boolean result = service.rentCar("CAR001");
        assertTrue(result);
        assertFalse(availableCar.isAvailable());
        verify(carRepository).updateCar(availableCar);
    }

    @Test
    void testRentUnavailableCar() {
        when(carRepository.findByRegistrationNumber("CAR002")).thenReturn(Optional.of(unavailableCar));
        assertFalse(service.rentCar("CAR002"));
        verify(carRepository, never()).updateCar(any());
    }

    @Test
    void testRentNonexistentCar() {
        when(carRepository.findByRegistrationNumber("XXX")).thenReturn(Optional.empty());
        assertFalse(service.rentCar("XXX"));
    }

    @Test
    void testReturnCar() {
        unavailableCar.setAvailable(false);
        when(carRepository.findByRegistrationNumber("CAR002")).thenReturn(Optional.of(unavailableCar));
        service.returnCar("CAR002");
        assertTrue(unavailableCar.isAvailable());
        verify(carRepository).updateCar(unavailableCar);
    }

    @Test
    void testAddCar_Success() {
        Car newCar = new Car("NEW123", "Civic", true);

        when(carRepository.findByRegistrationNumber("NEW123")).thenReturn(Optional.empty());

        boolean result = service.addCar(newCar);

        assertTrue(result);
        verify(carRepository).addCar(newCar);
    }

    @Test
    void testAddCar_AlreadyExists() {
        Car existingCar = new Car("EXIST123", "Yaris", true);

        when(carRepository.findByRegistrationNumber("EXIST123")).thenReturn(Optional.of(existingCar));

        boolean result = service.addCar(existingCar);

        assertFalse(result);
        verify(carRepository, never()).addCar(any());
    }

    @Test
    void testSearchCarsByModel() {
        Car car1 = new Car("A1", "Megane", true);
        Car car2 = new Car("A2", "Megane", false);

        when(carRepository.findByModel("Megane")).thenReturn(List.of(car1, car2));

        List<Car> results = service.searchCarsByModel("Megane");

        assertEquals(2, results.size());
        assertTrue(results.contains(car1));
        assertTrue(results.contains(car2));
    }
}
