package com.example.rental.unit;

import com.example.rental.Car;
import com.example.rental.CarRentalService;
import com.example.rental.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

class CarRentalServiceSpyTest {

    private CarRepository repositorySpy;
    private CarRentalService service;
    private Car car;

    @BeforeEach
    void setUp() throws Exception {
        repositorySpy = Mockito.spy(new CarRepository());
        service = new CarRentalService();

        var field = CarRentalService.class.getDeclaredField("carRepository");
        field.setAccessible(true);
        field.set(service, repositorySpy);

        car = new Car("XYZ123", "Golf", true);
        repositorySpy.addCar(car);
    }

    @Test
    void testUpdateCarCalledOnRent() {
        service.rentCar("XYZ123");
        verify(repositorySpy).updateCar(any(Car.class));
    }

    @Test
    void testUpdateCarCalledOnReturn() {
        car.setAvailable(false);
        repositorySpy.updateCar(car);

        service.returnCar("XYZ123");
        verify(repositorySpy, times(2)).updateCar(any(Car.class)); // 1 for setup, 1 for return
    }
}
