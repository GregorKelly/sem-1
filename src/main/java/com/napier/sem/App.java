package com.napier.sem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

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
     * @param name CountryCode District Population of the city record to get.
     * @return The record of the city with CountryCode District Population or null if no city exists.
     */
    @RequestMapping("cityPop")
    public ArrayList<City> getCityPop(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT ID, Name, CountryCode, District, Population "
                            + "FROM city "
                            + "WHERE Name LIKE '" + name + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<City> cityArray = new ArrayList<>();

            // Extract city information
            while (rset.next())
            {
                City city = new City();
                city.cityID = rset.getInt("ID");
                city.cityName = rset.getString("Name");
                city.countryCode = rset.getString("CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
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

    public void displayCity(ArrayList<City> cityArray)
    {
        if (cityArray != null)
        {
            for(int i = 0; i < cityArray.size(); i++)
            {
                System.out.println(cityArray);
            }
        }
        else
        {
            System.out.println("No city found");
        }
    }

    /**
     * Get a single district record.
     * @param name District Population of the district record to get.
     * @return The record of the district with District Population or null if no district exists.
     */
    @RequestMapping("districtPop")
    public ArrayList<District> getDistrictPop(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT District, SUM(Population) "
                            + "FROM city "
                            + "WHERE District LIKE '" + name + "'"
                            + "GROUP BY District";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<District> districtArray = new ArrayList<>();

            // Extract city information
            while (rset.next())
            {
                District district = new District();
                district.district = rset.getString("District");
                district.population = rset.getInt("SUM(Population)");
                districtArray.add(district);
            }
            return districtArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get district details");
            return null;
        }
    }


    ////////////////////////////////////////////////////////

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
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");

                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;

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
                    country.countryCode + " "
                            + country.countryName + " "
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

    ////////////////////////////////////////////

    /**
     * Get a single capital city record.
     * @param name countryName Population of the city record to get.
     * @return The record of the city with countryName Population or null if no city exists.
     */
    @RequestMapping("capitalCity")
    public CapitalCity getCapitalCity(@RequestParam(value = "name") String name)
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
                CapitalCity capitalCity = new CapitalCity();
                capitalCity.cityName = rset.getString("city.Name");
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

    public void displayCapitalCity(CapitalCity capitalCity)
    {
        if (capitalCity != null)
        {
            System.out.println(
                    capitalCity.cityName + " "
                            + capitalCity.countryName + " "
                            + capitalCity.population);
        }
        else
        {
            System.out.println("No capital city found");
        }
    }

    /////////////////////////////////////////////////////


    /**
     * Get a single country population record.
     * @param name Name Continent Region Population Capital of the country record to get.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    @RequestMapping("countryPop")
    public ArrayList<CountryPop> getCountryPopulation(@RequestParam(value = "name") String name)
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

            ArrayList<CountryPop> countryPopArray = new ArrayList<>();

            if (rset.next())
            {
                CountryPop countryPop = new CountryPop();
                countryPop.countryName = rset.getString("country.Name");
                countryPop.population = rset.getInt("country.Population");
                countryPop.cityPopulation = rset.getInt("SUM(city.Population)");
                countryPop.cityPopulationPercentage = rset.getFloat("(SUM(city.Population)/country.Population)*100");
                countryPop.notCityPopulation = rset.getInt("country.Population-SUM(city.Population)");
                countryPop.notCityPopulationPercentage = rset.getFloat("((country.Population-SUM(city.Population))/country.Population)*100");
                countryPopArray.add(countryPop);
            }
            return countryPopArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    /*public void displayCountryPopulation(Country countryPop)
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
    }*/

    /**
     * Get a single region population record.
     * @param region Name Continent Region Population Capital of the region record to get.
     * @return The record of the region with Name Continent Region Population Capital or null if no region exists.
     */
    @RequestMapping("regionPop")
    public ArrayList<Region> getRegionPopulation(@RequestParam(value = "region") String region)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Region, SUM(country.Population), SUM(city.Population), (SUM(city.Population)/SUM(country.Population))*100, SUM(country.Population)-SUM(city.Population), ((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "WHERE country.Region LIKE '" + region + "' "
                            + "GROUP BY country.Region";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<Region> regionArray = new ArrayList<>();

            while (rset.next())
            {
                Region regionPop = new Region();
                regionPop.name = rset.getString("country.Region");
                regionPop.population = rset.getInt("SUM(country.Population)");
                regionPop.cityPopulation = rset.getInt("SUM(city.Population)");
                regionPop.cityPopulationPercentage = rset.getFloat("(SUM(city.Population)/SUM(country.Population))*100");
                regionPop.notCityPopulation = rset.getInt("SUM(country.Population)-SUM(city.Population)");
                regionPop.notCityPopulationPercentage = rset.getFloat("((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100");
                regionArray.add(regionPop);
            }
            return regionArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    /*public void displayRegionPopulation(Region regionPop)
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
    }*/

    /**
     * Get a single continent population record.
     * @param continent Name Continent Region Population Capital of the continent record to get.
     * @return The record of the continent with Name Continent Region Population Capital or null if no continent exists.
     */
    @RequestMapping("continentPop")
    public ArrayList<Continent> getContinentPopulation(@RequestParam(value = "continent") String continent)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Continent, SUM(country.Population), SUM(city.Population), (SUM(city.Population)/SUM(country.Population))*100, SUM(country.Population)-SUM(city.Population), ((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "WHERE country.Continent LIKE '" + continent + "' "
                            + "GROUP BY country.Continent";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<Continent> continentArray = new ArrayList<>();

            if (rset.next())
            {
                Continent continentPop = new Continent();
                continentPop.name = rset.getString("country.Continent");
                continentPop.population = rset.getInt("SUM(country.Population)");
                continentPop.cityPopulation = rset.getInt("SUM(city.Population)");
                continentPop.cityPopulationPercentage = rset.getFloat("(SUM(city.Population)/SUM(country.Population))*100");
                continentPop.notCityPopulation = rset.getInt("SUM(country.Population)-SUM(city.Population)");
                continentPop.notCityPopulationPercentage = rset.getFloat("((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100");
                continentArray.add(continentPop);
            }
            return continentArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

   /* public void displayContinentPopulation(Continent continentPop)
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
    }*/

    // Gets the name of a city for a country
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
                city.cityID = rset.getInt("ID");
                city.cityName = rset.getString("Name");

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

    // Gets the name of the country for the city
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
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");

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
     * @param name Population of the world.
     * @return The record of the world Population or null if no countries exists.
     */
    @RequestMapping("worldPop")
    public ArrayList<World> getWorldPopulation(@RequestParam(value = "name") String name)
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

            ArrayList<World> worldArray = new ArrayList<>();

            if (rset.next())
            {
                World worldPop = new World();
                worldPop.population = rset.getInt("SUM(country.Population)");
                worldArray.add(worldPop);
            }
            return worldArray;

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    /**
     * Gets all the countries
     * @param name countries in the world
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("worldCountries")
    public ArrayList<Country> getWorldCountries(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.continent, country.region, country.Population, country.capital "
                            + "FROM country "
                            + "ORDER BY country.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;
                countryArray.add(country);
            }
            return countryArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets all the countries
     * @param name countries in a continent
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("continentCountries")
    public ArrayList<Country> getContinentCountries(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.continent, country.region, country.Population, country.capital "
                            + "FROM country "
                            + "WHERE country.continent LIKE '" + name + "'"
                            + "ORDER BY country.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;
                countryArray.add(country);
            }
            return countryArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets all the countries
     * @param name countries in a region
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("regionCountries")
    public ArrayList<Country> getRegionCountries(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.continent, country.region, country.Population, country.capital "
                            + "FROM country "
                            + "WHERE country.region LIKE '" + name + "'"
                            + "ORDER BY country.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;
                countryArray.add(country);
            }
            return countryArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }


    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("worldCities")
    public ArrayList<City> getWorldCities()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next())
            {
                City city = new City();
                city.cityID = rset.getInt("city.ID");
                city.cityName = rset.getString("city.Name");
                city.countryCode = rset.getString("city.CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the cities
     * @param name cities in a continent
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("continentCities")
    public ArrayList<City> getContinentCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city JOIN country ON city.CountryCode = country.Code "
                            + "WHERE country.continent LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next())
            {
                City city = new City();
                city.cityID = rset.getInt("city.ID");
                city.cityName = rset.getString("city.Name");
                city.countryCode = rset.getString("city.CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the cities
     * @param name cities in a region
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("regionCities")
    public ArrayList<City> getRegionCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city JOIN country ON city.CountryCode = country.Code "
                            + "WHERE country.region LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next())
            {
                City city = new City();
                city.cityID = rset.getInt("city.ID");
                city.cityName = rset.getString("city.Name");
                city.countryCode = rset.getString("city.CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the cities
     * @param name cities in a country
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("countryCities")
    public ArrayList<City> getCountryCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city JOIN country ON city.CountryCode = country.Code "
                            + "WHERE country.name LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next())
            {
                City city = new City();
                city.cityID = rset.getInt("city.ID");
                city.cityName = rset.getString("city.Name");
                city.countryCode = rset.getString("city.CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the cities
     * @param name cities in a district
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("districtCities")
    public ArrayList<City> getDistrictCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city "
                            + "WHERE city.District LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next())
            {
                City city = new City();
                city.cityID = rset.getInt("city.ID");
                city.cityName = rset.getString("city.Name");
                city.countryCode = rset.getString("city.CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the capital cities
     * @param name capital cities in the world
     * @return A list of all capital cities, or null if there is an error.
     */
    @RequestMapping("worldCapitalCities")
    public ArrayList<CapitalCity> getWorldCapitalCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next())
            {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the capital cities
     * @param name capital cities in a continent
     * @return A list of all capital cities, or null if there is an error.
     */
    @RequestMapping("continentCapitalCities")
    public ArrayList<CapitalCity> getContinentCapitalCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.continent LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next())
            {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the capital cities
     * @param name capital cities in a region
     * @return A list of all capital cities, or null if there is an error.
     */
    @RequestMapping("regionCapitalCities")
    public ArrayList<CapitalCity> getRegionCapitalCities(@RequestParam(value = "name") String name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.region LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next())
            {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
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
     * Gets all the countries
     * @param name of continent
     * @param num of countries to be displayed
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("continentCountriesNum")
    public ArrayList<Country> getContinentCountriesNum(@RequestParam(value = "name") String name, @RequestParam(value = "num") String num)
    {
        try
        {
            int theNum = Integer.parseInt(num);

            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.continent, country.region, country.Population, country.capital "
                            + "FROM country "
                            + "WHERE country.continent LIKE '" + name + "'"
                            + "ORDER BY country.Population DESC"
                            + "FETCH FIRST" + theNum + "ROWS ONLY";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;
                countryArray.add(country);
            }
            return countryArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets all the countries
     * @param name of region
     * @param num of countries to be displayed
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("regionCountriesNum")
    public ArrayList<Country> getRegionCountriesNum(@RequestParam(value = "name") String name, @RequestParam(value = "num") String num)
    {
        try
        {
            int theNum = Integer.parseInt(num);

            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.continent, country.region, country.Population, country.capital "
                            + "FROM country "
                            + "WHERE country.region LIKE '" + name + "'"
                            + "ORDER BY country.Population DESC"
                            + "FETCH FIRST" + theNum + "ROWS ONLY";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<Country> countryArray = new ArrayList<>();

            while (rset.next())
            {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;
                countryArray.add(country);
            }
            return countryArray;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }
}












