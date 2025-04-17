package com.example.rental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

public class CarControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private CarRentalService carRentalService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }


    @Test
    public void testGetAllCars() throws Exception {
        // Création des objets Car avec l'état disponible ou non
        Car car1 = new Car("ABC123", "Toyota Camry", true);
        Car car2 = new Car("XYZ456", "Honda Civic", false);
        List<Car> cars = Arrays.asList(car1, car2);

        // Simulation du comportement du service
        when(carRentalService.getAllCars()).thenReturn(cars);

        // Test du endpoint GET /cars
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registrationNumber").value("ABC123"))
                .andExpect(jsonPath("$[1].registrationNumber").value("XYZ456"))
                .andExpect(jsonPath("$[0].model").value("Toyota Camry"))
                .andExpect(jsonPath("$[1].model").value("Honda Civic"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].available").value(false));
    }

    @Test
    public void testRentCar() throws Exception {
        String registrationNumber = "ABC123";

        // Simulation du comportement du service pour louer une voiture
        when(carRentalService.rentCar(registrationNumber)).thenReturn(true);

        // Test du endpoint POST /cars/rent/{registrationNumber}
        mockMvc.perform(post("/cars/rent/{registrationNumber}", registrationNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Vérification que la méthode rentCar a bien été appelée
        verify(carRentalService, times(1)).rentCar(registrationNumber);
    }

    @Test
    public void testReturnCar() throws Exception {
        String registrationNumber = "ABC123";

        // Test du endpoint POST /cars/return/{registrationNumber}
        mockMvc.perform(post("/cars/return/{registrationNumber}", registrationNumber))
                .andExpect(status().isOk());

        // Vérification que la méthode returnCar a bien été appelée
        verify(carRentalService, times(1)).returnCar(registrationNumber);
    }
}
