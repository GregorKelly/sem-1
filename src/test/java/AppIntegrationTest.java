import com.napier.sem.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060");
    }

    @Test
    void testGetCityPop()
    {
        ArrayList<City> city = app.getCityPop("Edinburgh");
        //assertEquals(city.city_name, "Edinburgh");
        //assertEquals(city.countryName, "United Kingdom");
    }

    @Test
    void testGetCountryPop()
    {
        ArrayList<CountryPop> country = app.getCountryPopulation("GBR");
        //assertEquals(country.country_name, "United Kingdom");
        //assertEquals(country.country_code, "GBR");
    }

    @Test
    void testGetDistrictPop()
    {
        ArrayList<District> district = app.getDistrictPop("Scotland");
    }
}