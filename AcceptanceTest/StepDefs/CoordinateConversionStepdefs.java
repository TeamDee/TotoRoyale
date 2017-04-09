package StepDefs;

import GameModel.Map.Coordinates.CubicCoordinate;
import GameModel.Map.Coordinates.OffsetCoordinate;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * Created by Z_K on 4/8/2017.
 */
public class CoordinateConversionStepdefs {
    private OffsetCoordinate offsetCoordinate;
    private CubicCoordinate cubicCoordinate;

    @Given("^I need to convert one of our coordinate objects with coordinates: ([-\\d]+) ([-\\d]+)$")
    public void offsetToBeConverted(String row, String col){
        offsetCoordinate = new OffsetCoordinate(Integer.parseInt(row), Integer.parseInt(col));
    }

    @When("^I convert it into cubical coordinate object$")
    public void doConvertion(){
        cubicCoordinate = offsetCoordinate.getCubicCoordinate();
    }

    @Then("^I have cubical coordinates: ([-\\d]+) ([-\\d]+) ([-\\d]+)$")
    public void checkAfterConversion(String xExpected, String yExpected, String zExpected){
        Assert.assertEquals(Integer.parseInt(xExpected), cubicCoordinate.getX());
        Assert.assertEquals(Integer.parseInt(yExpected), cubicCoordinate.getY());
        Assert.assertEquals(Integer.parseInt(zExpected), cubicCoordinate.getZ());
    }

    @Given("^I need to convert cubic object with: ([-\\d]+) ([-\\d]+) ([-\\d]+)$")
    public void cubicToBeConverted(String x, String y, String z){
        cubicCoordinate = new CubicCoordinate(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
    }

    @When("^I convert it into offset coordinate object$")
    public void doConvertionToOffset(){
        offsetCoordinate = cubicCoordinate.getOffsetCoordinate();
    }

    @Then("^I have offset coordinates: ([-\\d]+) ([-\\d]+)$")
    public void checkAfterConversionOffset(String rowExpected, String colExpected){
        Assert.assertEquals(Integer.parseInt(rowExpected), offsetCoordinate.x);
        Assert.assertEquals(Integer.parseInt(colExpected), offsetCoordinate.y);
    }
}
