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
        app.connect("localhost:3306");
    }

    @Test
    void testGetCityPop()
    {
        City city = app.getSingleCityPop("Edinburgh");
        //assertEquals(city.cityName, "Edinburgh");
        //assertEquals(city.population, 450180);
    }

    @Test
    void testGetCountryPop()
    {
        Country country = app.getSingleCountryPop("United Kingdom");
        //assertEquals(country.countryName, "United Kingdom");
        //assertEquals(country.population, 59623400);
    }

    @Test
    void testGetDistrictPop()
    {
        District district = app.getSingleDistrictPop("Scotland");
        //assertEquals(district.district, "Scotland");
        //assertEquals(district.population, 1429620);
    }
}