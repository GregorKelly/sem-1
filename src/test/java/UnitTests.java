import com.napier.sem.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

    @Test
    void getDistrictTestNull()
    {
        District district = app.getSingleDistrictPop(null);
        app.displaySingleDistrictPop(district);
    }

    @Test
    void getDistrictTestEmpty()
    {
        District district = app.getSingleDistrictPop("");
        app.displaySingleDistrictPop(district);
    }

    @Test
    void getDistrictTest()
    {
        District district = app.getSingleDistrictPop("Scotland");
        app.displaySingleDistrictPop(district);
    }

    @Test
    void getRegionTestNull()
    {
        Region region = app.getSingleRegionPop(null);
        app.displaySingleRegionPop(region);
    }

    @Test
    void getRegionTestEmpty()
    {
        Region region = app.getSingleRegionPop("");
        app.displaySingleRegionPop(region);
    }

    @Test
    void getRegionTest()
    {
        Region region = app.getSingleRegionPop("Western Europe");
        app.displaySingleRegionPop(region);
    }

    @Test
    void getContinentTestNull()
    {
        Continent continent = app.getSingleContinentPop(null);
        app.displaySingleContinentPop(continent);
    }

    @Test
    void getContinentTestEmpty()
    {
        Continent continent = app.getSingleContinentPop("");
        app.displaySingleContinentPop(continent);
    }

    @Test
    void getContinentTest()
    {
        Continent continent = app.getSingleContinentPop("Europe");
        app.displaySingleContinentPop(continent);
    }

    @Test
    void getWorldTest()
    {
        World world = app.getWorldPopulation();
        app.displayWorldPop(world);
    }
}