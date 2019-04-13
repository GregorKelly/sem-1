import com.napier.sem.CapitalCity;
import com.napier.sem.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;

//import static org.junit.jupiter.api.Assertions.*;

import com.napier.sem.App;
import com.napier.sem.City;

public class UnitTests
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060");
    }

    /*@Test
    void getCityTestNull()
    {
        City city = app.getCity(null);
        app.displayCity(city);
    }

    @Test
    void getCityTestEmpty()
    {
        City city = app.getCity("");
        app.displayCity(city);
    }

    @Test
    void getCityTest()
    {
        City city = app.getCity("Edinburgh");
        app.displayCity(city);
    }

    @Test
    void getCountryTestNull()
    {
        Country country = app.getCountry(null);
        app.displayCountry(country);
    }

    @Test
    void getCountryTestEmpty()
    {
        Country country = app.getCountry("");
        app.displayCountry(country);
    }

    @Test
    void getCountryTest()
    {
        Country country = app.getCountry("GBR");
        app.displayCountry(country);
    }

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