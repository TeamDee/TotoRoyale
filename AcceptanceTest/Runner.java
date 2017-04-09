//package src;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"AcceptanceTest/Features/Tiger.feature"}
)

public class Runner{

        }