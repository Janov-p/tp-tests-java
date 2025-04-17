package com.example.rental;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.example.rental"},
        plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberTestRunner {
    // Cette classe est vide, elle sert uniquement à exécuter les tests Cucumber
}