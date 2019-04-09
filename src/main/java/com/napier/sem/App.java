package com.napier.sem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
//import java.util.ArrayList;

@SpringBootApplication
@RestController
public class App
{
    public static void main(String[] args)
    {

        // Connect to database
        if (args.length < 1)
        {
            connect("localhost:33060");
        }
        else
        {
            connect(args[0]);
        }

        SpringApplication.run(App.class, args);

        // Extract city information
        //ArrayList<City> cities = a.getAllCities();
        //a.printCities(cities);

        // Test the size of the returned data
        //System.out.println(cities.size());
    }

        /**
         * Connection to MySQL database.
         */
        private static Connection con = null;

        /**
         * Connect to the MySQL database.
         */
        public static void connect(String location)
        {
            try
            {
                // Load Database driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Could not load SQL driver");
                System.exit(-1);
            }

            int retries = 10;
            for (int i = 0; i < retries; ++i)
            {
                System.out.println("Connecting to database...");
                try
                {
                    // Wait a bit for db to start
                    Thread.sleep(30000);
                    // Connect to database
                    con = DriverManager.getConnection("jdbc:mysql://"+location+"/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                    System.out.println("Successfully connected");
                    break;
                }
                catch (SQLException sqle)
                {
                    System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                    System.out.println(sqle.getMessage());
                }
                catch (InterruptedException ie)
                {
                    System.out.println("Thread interrupted? Should not happen.");
                }
            }
        }

        /**
         * Disconnect from the MySQL database.
         */
        public static void disconnect()
        {
            if (con != null)
            {
                try
                {
                    // Close connection
                    con.close();
                }
                catch (Exception e)
                {
                    System.out.println("Error closing connection to database");
                }
            }
        }

    /**
     * Get a single city record.
     * CountryCode District Population of the city record to get.
     * @return The record of the city with CountryCode District Population or null if no city exists.
     */
    @RequestMapping("city")
    public City getCity()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT ID, Name, CountryCode, District, Population "
                            + "FROM city"
                            + "GROUP BY Population";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new city if valid.
            // Check one is returned
            if (rset.next())
            {
                City city = new City();
                city.city_ID = rset.getInt("ID");
                city.city_name = rset.getString("Name");
                city.country_code = rset.getString("CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.country_name;
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");

                return city;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayCity(City city)
    {
        if (city != null)
        {
            System.out.println(
                    city.city_name + " "
                            + city.countryName + " "
                                + city.district + " "
                                    + city.population);
        }
        else
        {
            System.out.println("No city found");
        }
    }

    /**
     * Get a single country record.
     * @param code Name Continent Region Population Capital of the country record to get.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    @RequestMapping("country")
    public Country getCountry(@RequestParam(value = "code") String code)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "WHERE Code LIKE '" + code + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Check one is returned
            if (rset.next())
            {
                Country country = new Country();
                country.country_code = rset.getString("Code");
                country.country_name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");

                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.city_name;

                return country;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public void displayCountry(Country country)
    {
        if (country != null)
        {
            System.out.println(
                    country.country_code + " "
                            + country.country_name + " "
                            + country.continent + " "
                            + country.region + " "
                            + country.population + " "
                            + country.capitalName);
        }
        else
        {
            System.out.println("No country found");
        }
    }

    /**
     * Get a single capital city record.
     * @param name countryName Population of the city record to get.
     * @return The record of the city with countryName Population or null if no city exists.
     */
    @RequestMapping("capitalCity")
    public City getCapitalCity(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.name LIKE '" + name + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new city if valid.
            // Check one is returned
            if (rset.next())
            {
                City capitalCity = new City();
                capitalCity.city_name = rset.getString("city.Name");
                capitalCity.countryName = rset.getString("country.Name");
                capitalCity.population = rset.getInt("city.Population");

                return capitalCity;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }
    }

    public void displayCapitalCity(City capitalCity)
    {
        if (capitalCity != null)
        {
            System.out.println(
                    capitalCity.city_name + " "
                            + capitalCity.countryName + " "
                            + capitalCity.population);
        }
        else
        {
            System.out.println("No capital city found");
        }
    }

    /**
     * Get a single country population record.
     * @param name Name Continent Region Population Capital of the country record to get.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    @RequestMapping("countryPop")
    public Country getCountryPopulation(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Name, country.Population, SUM(city.Population), (SUM(city.Population)/country.Population)*100, country.Population-SUM(city.Population), ((country.Population-SUM(city.Population))/country.Population)*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "WHERE country.Name LIKE '" + name + "' "
                            + "GROUP BY country.Name, country.Population";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country for population if valid.
            // Check one is returned
            if (rset.next())
            {
                Country countryPop = new Country();
                countryPop.country_name = rset.getString("country.Name");
                countryPop.population = rset.getInt("country.Population");
                countryPop.allCityPopulation = rset.getInt("SUM(city.Population)");
                countryPop.allCityPopulationPercentage = rset.getFloat("(SUM(city.Population)/country.Population)*100");
                countryPop.notCityPopulation = rset.getInt("country.Population-SUM(city.Population)");
                countryPop.notCityPopulationPercentage = rset.getFloat("((country.Population-SUM(city.Population))/country.Population)*100");

                return countryPop;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    public void displayCountryPopulation(Country countryPop)
    {
        if (countryPop != null)
        {
            System.out.println(
                    countryPop.country_name + " "
                            + countryPop.population + " "
                            + countryPop.allCityPopulation + " "
                            + countryPop.allCityPopulationPercentage + "% "
                            + countryPop.notCityPopulation + " "
                            + countryPop.notCityPopulationPercentage + "%");
        }
        else
        {
            System.out.println("No country population found");
        }
    }

    /**
     * Get a single region population record.
     * @param region Name Continent Region Population Capital of the region record to get.
     * @return The record of the region with Name Continent Region Population Capital or null if no region exists.
     */
    @RequestMapping("regionPop")
    public Region getRegionPopulation(@RequestParam(value = "region") String region)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Region, SUM(country.Population), city.Population, (SUM(city.Population)/SUM(country.Population))*100, SUM(country.Population)-SUM(city.Population), ((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "WHERE country.Region LIKE '" + region + "' "
                            + "GROUP BY country.Region, city.Population";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new region for population if valid.
            // Check one is returned
            if (rset.next())
            {
                Region regionPop = new Region();
                regionPop.name = rset.getString("country.Region");
                regionPop.population = rset.getInt("SUM(country.Population)");
                regionPop.allCityPopulation = rset.getInt("city.Population");
                regionPop.allCityPopulationPercentage = rset.getFloat("(SUM(city.Population)/SUM(country.Population))*100");
                regionPop.notCityPopulation = rset.getInt("SUM(country.Population)-SUM(city.Population)");
                regionPop.notCityPopulationPercentage = rset.getFloat("((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100");

                return regionPop;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    public void displayRegionPopulation(Region regionPop)
    {
        if (regionPop != null)
        {
            System.out.println(
                    regionPop.name + " "
                            + regionPop.population + " "
                            + regionPop.allCityPopulation + " "
                            + regionPop.allCityPopulationPercentage + "% "
                            + regionPop.notCityPopulation + " "
                            + regionPop.notCityPopulationPercentage + "%");
        }
        else
        {
            System.out.println("No region population found");
        }
    }

    /**
     * Get a single continent population record.
     * @param continent Name Continent Region Population Capital of the continent record to get.
     * @return The record of the continent with Name Continent Region Population Capital or null if no continent exists.
     */
    @RequestMapping("continentPop")
    public Continent getContinentPopulation(@RequestParam(value = "continent") String continent)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Continent, SUM(country.Population), city.Population, (SUM(city.Population)/SUM(country.Population))*100, SUM(country.Population)-SUM(city.Population), ((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "WHERE country.Continent LIKE '" + continent + "' "
                            + "GROUP BY country.Continent, city.Population";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new continent for population if valid.
            // Check one is returned
            if (rset.next())
            {
                Continent continentPop = new Continent();
                continentPop.name = rset.getString("country.Continent");
                continentPop.population = rset.getInt("SUM(country.Population)");
                //continentPop.population = rset.getInt("country.Population");
                continentPop.allCityPopulation = rset.getInt("city.Population");
                //continentPop.allCityPopulation = rset.getInt("SUM(city.Population)");
                continentPop.allCityPopulationPercentage = rset.getFloat("(SUM(city.Population)/SUM(country.Population))*100");
                continentPop.notCityPopulation = rset.getInt("SUM(country.Population)-SUM(city.Population)");
                continentPop.notCityPopulationPercentage = rset.getFloat("((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100");

                return continentPop;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    public void displayContinentPopulation(Continent continentPop)
    {
        if (continentPop != null)
        {
            System.out.println(
                    continentPop.name + " "
                            + continentPop.population + " "
                            + continentPop.allCityPopulation + " "
                            + continentPop.allCityPopulationPercentage + "% "
                            + continentPop.notCityPopulation + " "
                            + continentPop.notCityPopulationPercentage + "%");
        }
        else
        {
            System.out.println("No continent population found");
        }
    }


    public City getCityForCountry(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT ID, Name "
                            + "FROM city "
                            + "WHERE ID = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new city if valid.
            // Check one is returned
            if (rset.next())
            {
                City city = new City();
                city.city_ID = rset.getInt("ID");
                city.city_name = rset.getString("Name");

                return city;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public Country getCountryForCity(String code)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name "
                            + "FROM country "
                            + "WHERE Code LIKE '" + code + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Check one is returned
            if (rset.next())
            {
                Country country = new Country();
                country.country_code = rset.getString("Code");
                country.country_name = rset.getString("Name");

                return country;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get the world population record.
     * Population of the world.
     * @return The record of the world Population or null if no countries exists.
     */
    @RequestMapping("worldPop")
    public Country getWorldPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(country.Population)"
                            + "FROM country";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country for population if valid.
            // Check one is returned
            if (rset.next())
            {
                Country worldPop = new Country();
                worldPop.population = rset.getInt("SUM(country.Population)");

                return worldPop;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }









    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     *
    public ArrayList<City> getAllCities()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city JOIN country ON city.CountryCode = country.Code "
                            + "WHERE country.Name LIKE 'United Kingdom' "
                            + "ORDER BY city.Name";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();
                city.city_ID = rset.getInt("city.ID");
                city.city_name = rset.getString("city.Name");
                city.country_code = rset.getString("city.CountryCode");
                city.district = rset.getString("city.District");
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Prints a list of cities.
     * @param cityArray The list of cities to print.
     *
    public void printCities(ArrayList<City> cityArray)
    {
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-25s %-30s", "City ID", "City Name", "Country Code", "District", "City Population"));
        // Loop over all cities in the list
        for (City city : cityArray)
        {
            String city_string =
                    String.format("%-10s %-15s %-20s %-25s %-30s",
                            city.city_ID, city.city_name, city.country_code, city.district, city.population);
            System.out.println(city_string);
        }
    } */

}