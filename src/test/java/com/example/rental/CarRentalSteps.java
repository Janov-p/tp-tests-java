package com.example.rental;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarRentalSteps {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private Response response;
    private String testRegistrationNumber;
    private boolean rentalResult;

    // Setup
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        baseUrl = "/cars";
    }

    // Scénario 1: Lister toutes les voitures disponibles
    @Given("des voitures sont disponibles")
    public void des_voitures_sont_disponibles() {
        setup();
        // Ajouter quelques voitures pour le test
        Car car1 = new Car("ABC123", "Toyota", true);
        Car car2 = new Car("XYZ789", "Honda", true);

        given()
                .contentType(ContentType.JSON)
                .body(car1)
                .post(baseUrl + "/add");

        given()
                .contentType(ContentType.JSON)
                .body(car2)
                .post(baseUrl + "/add");
    }

    @When("je demande la liste des voitures")
    public void je_demande_la_liste_des_voitures() {
        response = given()
                .get(baseUrl);
    }

    @Then("toutes les voitures sont affichées")
    public void toutes_les_voitures_sont_affichées() {
        List<Map<String, Object>> cars = response.jsonPath().getList("$");
        assertTrue(cars.size() >= 2);
        assertEquals(200, response.getStatusCode());
    }

    // Scénario 2: Louer une voiture avec succès
    @Given("une voiture est disponible")
    public void une_voiture_est_disponible() {
        setup();
        testRegistrationNumber = "TEST001";
        Car car = new Car(testRegistrationNumber, "Tesla", true);

        given()
                .contentType(ContentType.JSON)
                .body(car)
                .post(baseUrl + "/add");
    }

    @When("je loue cette voiture")
    public void je_loue_cette_voiture() {
        response = given()
                .post(baseUrl + "/rent/" + testRegistrationNumber);

        rentalResult = response.as(Boolean.class);
    }

    @Then("la voiture n'est plus disponible")
    public void la_voiture_n_est_plus_disponible() {
        assertTrue(rentalResult);

        // Vérifier que la voiture n'est plus disponible
        response = given().get(baseUrl);
        List<Map<String, Object>> cars = response.jsonPath().getList("$");

        boolean found = false;
        for (Map<String, Object> car : cars) {
            if (car.get("registrationNumber").equals(testRegistrationNumber)) {
                found = true;
                assertFalse((Boolean) car.get("available"));
            }
        }
        assertTrue(found, "La voiture doit être trouvée dans la liste");
    }

    // Scénario 3: Retourner une voiture
    @Given("une voiture est louée")
    public void une_voiture_est_louée() {
        setup();
        testRegistrationNumber = "RENT001";

        // Ajouter une voiture disponible
        Car car = new Car(testRegistrationNumber, "BMW", true);
        given()
                .contentType(ContentType.JSON)
                .body(car)
                .post(baseUrl + "/add");

        // La louer
        given()
                .post(baseUrl + "/rent/" + testRegistrationNumber);
    }

    @When("je retourne cette voiture")
    public void je_retourne_cette_voiture() {
        given()
                .post(baseUrl + "/return/" + testRegistrationNumber);
    }

    @Then("la voiture est marquée comme disponible")
    public void la_voiture_est_marquée_comme_disponible() {
        response = given().get(baseUrl);
        List<Map<String, Object>> cars = response.jsonPath().getList("$");

        boolean found = false;
        for (Map<String, Object> car : cars) {
            if (car.get("registrationNumber").equals(testRegistrationNumber)) {
                found = true;
                assertTrue((Boolean) car.get("available"));
            }
        }
        assertTrue(found, "La voiture doit être trouvée dans la liste");
    }
}