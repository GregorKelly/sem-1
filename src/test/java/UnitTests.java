import com.napier.sem.CapitalCity;
import com.napier.sem.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;

//import static org.junit.jupiter.api.Assertions.*;

import com.napier.sem.App;
import com.napier.sem.City;

import java.util.ArrayList;

public class UnitTests
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:3306");
    }

    @Test
    void getCityPopTestNull()
    {
        City city = app.getSingleCityPop(null);
        app.displaySingleCityPop(city);
    }

    @Test
    void getCityPopTestEmpty()
    {
        City city = app.getSingleCityPop("");
        app.displaySingleCityPop(city);
    }

    @Test
    void getCityPopTest()
    {
        City city = app.getSingleCityPop("Edinburgh");
        app.displaySingleCityPop(city);
    }

    @Test
    void getCountryTestNull()
    {
        Country country = app.getSingleCountryPop(null);
        app.displaySingleCountryPop(country);
    }

    @Test
    void getCountryTestEmpty()
    {
        Country country = app.getSingleCountryPop("");
        app.displaySingleCountryPop(country);
    }

    @Test
    void getCountryTest()
    {
        Country country = app.getSingleCountryPop("United Kingdom");
        app.displaySingleCountryPop(country);
    }
    /*
    @Test
    void getCapitalCityTestNull()
    {
        City capCity = app.getCity(null);
        app.displayCity(capCity);
    }

    @Test
    void getCapitalCityTestEmpty()
    {
        City capCity = app.getCity("");
        app.displayCity(capCity);
    }

    @Test
    void getCapitalCityTest()
    {
        CapitalCity capCity = app.getCapitalCity("France");
        app.displayCapitalCity(capCity);
    }*/
}