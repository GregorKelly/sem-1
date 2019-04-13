import com.napier.sem.App;
import com.napier.sem.City;
import com.napier.sem.Country;
import com.napier.sem.CapitalCity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

    /*@Test
    void testGetCity()
    {
        City city = app.getCity("Edinburgh");
        assertEquals(city.city_name, "Edinburgh");
        assertEquals(city.countryName, "United Kingdom");
    }

    @Test
    void testGetCountry()
    {
        Country country = app.getCountry("GBR");
        assertEquals(country.country_name, "United Kingdom");
        assertEquals(country.country_code, "GBR");
    }

    @Test
    void testGetCapitalCity()
    {
        CapitalCity capitalCity = app.getCapitalCity("France");
        assertEquals(capitalCity.city_name, "Paris");
        assertEquals(capitalCity.countryName, "France");
    }*/
}