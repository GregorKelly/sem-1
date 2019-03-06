

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import com.napier.sem.App;
import com.napier.sem.City;

public class Tests
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
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
}