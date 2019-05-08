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
public class App {
    public static void main(String[] args) {

        // Create new Application
        App a = new App();

        // Connect to database
        if (args.length < 1) {
            a.connect2("localhost:3306");
            connect("localhost:33060");
        } else {
            a.connect2(args[0]);
            connect(args[0]);
        }

        City cityPop = a.getSingleCityPop("Edinburgh");
        // Print single city population
        a.displaySingleCityPop(cityPop);

        District districtPop = a.getSingleDistrictPop("Scotland");
        // Print single district population
        a.displaySingleDistrictPop(districtPop);

        Country countryPop = a.getSingleCountryPop("United Kingdom");
        // Print single country population
        a.displaySingleCountryPop(countryPop);

        Region regionPop = a.getSingleRegionPop("Western Europe");
        // Print single regions population
        a.displaySingleRegionPop(regionPop);

        Continent continentPop = a.getSingleContinentPop("Europe");
        // Print single continents population
        a.displaySingleContinentPop(continentPop);

        World worldPop = a.getWorldPopulation();
        // Print world population;
        a.displayWorldPop(worldPop);

        ArrayList<Country> worldCountries = a.getTheWorldCountries();
        // Display all countries in the world
        a.displayWorldCountries(worldCountries);

        ArrayList<Country> continentCountries = a.getTheContinentCountries("Europe");
        // Display all countries in a continent
        //a.displayContinentCountries(continentCountries);

        ArrayList<Country> regionCountries = a.getTheRegionCountries("Western Europe");
        // Display all countries in a region
        //a.displayRegionCountries(regionCountries);

        ArrayList<City> worldCities = a.getTheWorldCities();
        // Display all cities in the world
        //a.displayWorldCities(worldCities);

        ArrayList<City> continentCities = a.getTheContinentCities("Europe");
        // Display all cities in a continent
        //a.displayContinentCities(continentCities);

        ArrayList<City> regionCities = a.getTheRegionCities("Western Europe");
        // Display all cities in a region
        //a.displayRegionCities(regionCities);

        ArrayList<City> countryCities = a.getTheCountryCities("United Kingdom");
        // Display all cities in a country
        //a.displayCountryCities(countryCities);

        ArrayList<City> districtCities = a.getTheDistrictCities("Scotland");
        // Display all cities in a district
        //a.displayDistrictCities(districtCities);

        ArrayList<CapitalCity> worldCapitalCities = a.getTheWorldCapitalCities();
        // Display all cities in the world
        //a.displayWorldCapitalCities(worldCapitalCities);

        ArrayList<CapitalCity> continentCapitalCities = a.getTheContinentCapitalCities("Europe");
        // Display all cities in a continent
        //a.displayContinentCapitalCities(continentCapitalCities);

        ArrayList<CapitalCity> regionCapitalCities = a.getTheRegionCapitalCities("Western Europe");
        // Display all cities in a region
        //a.displayRegionCapitalCities(regionCapitalCities);

        ArrayList<Continent> continentPercentage = a.getContinentPopPercent();
        // Display all continents cities percentage
        a.displayContinentPopPercent(continentPercentage);

        ArrayList<Region> regionPercentage = a.getRegionPopPercent();
        // Display all regions cities percentage
        a.displayRegionPopPercent(regionPercentage);

        ArrayList<CountryPop> countryPercentage = a.getCountryPopPercent();
        // Display all countries cities percentage
        //a.displayCountryPopPercent(countryPercentage);

        // Disconnect from database
        a.disconnect2();

        SpringApplication.run(App.class, args);
    }

    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    /**
     * Connection to MySQL database.
     */
    private Connection con2 = null;

    /**
     * Connect to the MySQL database.
     */
    public static void connect(String location) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect2(String location) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public static void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect2() {
        if (con2 != null) {
            try {
                // Close connection
                con2.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Get a single city record.
     *
     * @param name CountryCode District Population of the city record to get.
     * @return The record of the city with CountryCode District Population or null if no city exists.
     */
    @RequestMapping("cityPop")
    public ArrayList<City> getCityPop(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    // Get a single city population
    public City getSingleCityPop(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Name, CountryCode, District, Population "
                            + "FROM city "
                            + "WHERE Name LIKE '" + name + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Extract city information
            if (rset.next()) {
                City city = new City();
                city.cityName = rset.getString("Name");
                city.countryCode = rset.getString("CountryCode");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.district = rset.getString("District");
                city.population = rset.getInt("Population");
                return city;
            } else
                return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    // Displays a single cities population
    public void displaySingleCityPop(City city) {
        if (city != null) {
            System.out.println(
                    city.cityName + "  " + city.population);
        } else {
            System.out.println("No city found");
        }
    }

    /**
     * Get a single district record.
     *
     * @param name District Population of the district record to get.
     * @return The record of the district with District Population or null if no district exists.
     */
    @RequestMapping("districtPop")
    public ArrayList<District> getDistrictPop(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
                District district = new District();
                district.district = rset.getString("District");
                district.population = rset.getInt("SUM(Population)");
                districtArray.add(district);
            }
            return districtArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get district details");
            return null;
        }
    }

    // Get a single districts population
    public District getSingleDistrictPop(String name) {
        try {
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

            // Extract city information
            if (rset.next()) {
                District district = new District();
                district.district = rset.getString("District");
                district.population = rset.getInt("SUM(Population)");
                return district;
            } else
                return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get district details");
            return null;
        }
    }

    // Displays a single districts population
    public void displaySingleDistrictPop(District district) {
        if (district != null) {
            System.out.println(
                    district.district + "  " + district.population);
        } else {
            System.out.println("No district found");
        }
    }

    /**
     * Get a single country record.
     *
     * @param code Name Continent Region Population Capital of the country record to get.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    @RequestMapping("country")
    public Country getCountry(@RequestParam(value = "code") String code) {
        try {
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
            if (rset.next()) {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");

                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;

                return country;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    // Get single country population
    public Country getSingleCountryPop(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "WHERE Name LIKE '" + name + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Check one is returned
            if (rset.next()) {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");

                City city = getCityForCountry(rset.getInt("Capital"));
                country.capitalName = city.cityName;

                return country;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    // Dsiplays a single countries population
    public void displaySingleCountryPop(Country country) {
        if (country != null) {
            System.out.println(
                    country.countryName + "  " + country.population);
        } else {
            System.out.println("No country found");
        }
    }

    /**
     * Get a single capital city record.
     *
     * @param name countryName Population of the city record to get.
     * @return The record of the city with countryName Population or null if no city exists.
     */
    @RequestMapping("capitalCity")
    public CapitalCity getCapitalCity(@RequestParam(value = "name") String name) {
        try {
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
            if (rset.next()) {
                CapitalCity capitalCity = new CapitalCity();
                capitalCity.cityName = rset.getString("city.Name");
                capitalCity.countryName = rset.getString("country.Name");
                capitalCity.population = rset.getInt("city.Population");

                return capitalCity;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }
    }

    // Displays a capital city
    public void displayCapitalCity(CapitalCity capitalCity) {
        if (capitalCity != null) {
            System.out.println(
                    capitalCity.cityName + " "
                            + capitalCity.countryName + " "
                            + capitalCity.population);
        } else {
            System.out.println("No capital city found");
        }
    }

    /**
     * Get a single country population record.
     *
     * @param name Name Continent Region Population Capital of the country record to get.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    @RequestMapping("countryPop")
    public ArrayList<CountryPop> getCountryPopulation(@RequestParam(value = "name") String name) {
        try {
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

            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    /**
     * Get a single country population record.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    public ArrayList<CountryPop> getCountryPopPercent() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Name, country.Population, SUM(city.Population), (SUM(city.Population)/country.Population)*100, country.Population-SUM(city.Population), ((country.Population-SUM(city.Population))/country.Population)*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "GROUP BY country.Name, country.Population";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<CountryPop> countryPopArray = new ArrayList<>();

            while (rset.next()) {
                CountryPop countryPop = new CountryPop();
                countryPop.countryName = rset.getString("country.Name");
                countryPop.population = rset.getLong("country.Population");
                countryPop.cityPopulation = rset.getLong("SUM(city.Population)");
                countryPop.cityPopulationPercentage = rset.getFloat("(SUM(city.Population)/country.Population)*100");
                countryPop.notCityPopulation = rset.getLong("country.Population-SUM(city.Population)");
                countryPop.notCityPopulationPercentage = rset.getFloat("((country.Population-SUM(city.Population))/country.Population)*100");
                countryPopArray.add(countryPop);
            }
            return countryPopArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    // Displays Countries Cities Percentage
    public void displayCountryPopPercent(ArrayList<CountryPop> countryPopArray) {
        // Check the country arraylist is not null
        if (countryPopArray == null) {
            System.out.println("No countries");
            return;
        }
        // Print header
        System.out.println(String.format("%-20s %-15s %-15s %-15s %-15s %-15s", "Name", "Population", "City Population", "City Population %", "Rural Population", "Rural Population %"));
        // Loop over all countries in the list
        for (CountryPop country : countryPopArray) {
            String country_string =
                    String.format("%-20s %-15s %-15s %-15s %-15s %-15s",
                            country.countryName, country.population, country.cityPopulation, country.cityPopulationPercentage, country.notCityPopulation, country.notCityPopulationPercentage);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Get a single region population record.
     *
     * @param region Name Continent Region Population Capital of the region record to get.
     * @return The record of the region with Name Continent Region Population Capital or null if no region exists.
     */
    @RequestMapping("regionPop")
    public ArrayList<Region> getRegionPopulation(@RequestParam(value = "region") String region) {
        try {
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

            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    /**
     * Get a single country population record.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    public ArrayList<Region> getRegionPopPercent() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Region, SUM(country.Population), SUM(city.Population), (SUM(city.Population)/SUM(country.Population))*100, SUM(country.Population)-SUM(city.Population), ((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "GROUP BY country.Region";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<Region> regionArray = new ArrayList<>();

            while (rset.next()) {
                Region regionPop = new Region();
                regionPop.name = rset.getString("country.Region");
                regionPop.population = rset.getLong("SUM(country.Population)");
                regionPop.cityPopulation = rset.getLong("SUM(city.Population)");
                regionPop.cityPopulationPercentage = rset.getFloat("(SUM(city.Population)/SUM(country.Population))*100");
                regionPop.notCityPopulation = rset.getLong("SUM(country.Population)-SUM(city.Population)");
                regionPop.notCityPopulationPercentage = rset.getFloat("((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100");
                regionArray.add(regionPop);
            }
            return regionArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    // Displays Region Cities Percentage
    public void displayRegionPopPercent(ArrayList<Region> regionArray) {
        // Check the country arraylist is not null
        if (regionArray == null) {
            System.out.println("No countries");
            return;
        }
        // Print header
        System.out.println(String.format("%-20s %-15s %-15s %-15s %-15s %-15s", "Name", "Population", "City Population", "City Population %", "Rural Population", "Rural Population %"));
        // Loop over all countries in the list
        for (Region region : regionArray) {
            String country_string =
                    String.format("%-20s %-15s %-15s %-15s %-15s %-15s",
                            region.name, region.population, region.cityPopulation, region.cityPopulationPercentage, region.notCityPopulation, region.notCityPopulationPercentage);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    // Get single region population
    public Region getSingleRegionPop(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Region, SUM(Population)"
                            + "FROM country "
                            + "WHERE country.Region LIKE '" + name + "' "
                            + "GROUP BY country.Region";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Region region = new Region();
                region.name = rset.getString("country.Region");
                region.population = rset.getInt("SUM(Population)");
                return region;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    // Displays a regions population
    public void displaySingleRegionPop(Region region) {
        if (region != null) {
            System.out.println(
                    region.name + " " + region.population + " ");
        } else {
            System.out.println("No region population found");
        }
    }

    /**
     * Get a single continent population record.
     *
     * @param continent Name Continent Region Population Capital of the continent record to get.
     * @return The record of the continent with Name Continent Region Population Capital or null if no continent exists.
     */
    @RequestMapping("continentPop")
    public ArrayList<Continent> getContinentPopulation(@RequestParam(value = "continent") String continent) {
        try {
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

            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    /**
     * Get a single country population record.
     * @return The record of the country with Name Continent Region Population Capital or null if no country exists.
     */
    public ArrayList<Continent> getContinentPopPercent() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Continent, SUM(country.Population), SUM(city.Population), (SUM(city.Population)/SUM(country.Population))*100, SUM(country.Population)-SUM(city.Population), ((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100 "
                            + "FROM country JOIN city ON country.Code = city.CountryCode "
                            + "GROUP BY country.Continent";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<Continent> continentArray = new ArrayList<>();

            while (rset.next()) {
                Continent continentPop = new Continent();
                continentPop.name = rset.getString("country.Continent");
                continentPop.population = rset.getLong("SUM(country.Population)");
                continentPop.cityPopulation = rset.getLong("SUM(city.Population)");
                continentPop.cityPopulationPercentage = rset.getFloat("(SUM(city.Population)/SUM(country.Population))*100");
                continentPop.notCityPopulation = rset.getLong("SUM(country.Population)-SUM(city.Population)");
                continentPop.notCityPopulationPercentage = rset.getFloat("((SUM(country.Population)-SUM(city.Population))/SUM(country.Population))*100");
                continentArray.add(continentPop);
            }
            return continentArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    // Display Continent City Percentage
    public void displayContinentPopPercent(ArrayList<Continent> continentArray) {
        // Check the country arraylist is not null
        if (continentArray == null) {
            System.out.println("No countries");
            return;
        }
        // Print header
        System.out.println(String.format("%-20s %-20s %-20s %-20s %-20s %-20s", "Name", "Population", "City Population", "City Population %", "Rural Population", "Rural Population %"));
        // Loop over all countries in the list
        for (Continent continent : continentArray) {
            String country_string =
                    String.format("%-20s %-20s %-20s %-20s %-20s %-20s",
                            continent.name, continent.population, continent.cityPopulation, continent.cityPopulationPercentage, continent.notCityPopulation, continent.notCityPopulationPercentage);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    // Get single continent population
    public Continent getSingleContinentPop(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Continent, SUM(Population) "
                            + "FROM country "
                            + "WHERE country.Continent LIKE '" + name + "' "
                            + "GROUP BY country.Continent";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Continent continent = new Continent();
                continent.name = rset.getString("country.Continent");
                continent.population = rset.getInt("SUM(Population)");
                return continent;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    // Displays a continents population
    public void displaySingleContinentPop(Continent continent) {
        if (continent != null) {
            System.out.println(
                    continent.name + " " + continent.population);
        } else {
            System.out.println("No continent population found");
        }
    }

    // Gets the name of a city for a country
    public City getCityForCountry(int ID) {
        try {
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
            if (rset.next()) {
                City city = new City();
                city.cityID = rset.getInt("ID");
                city.cityName = rset.getString("Name");

                return city;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    // Gets the name of the country for the city
    public Country getCountryForCity(String code) {
        try {
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
            if (rset.next()) {
                Country country = new Country();
                country.countryCode = rset.getString("Code");
                country.countryName = rset.getString("Name");

                return country;
            } else
                return null;
        } catch (Exception e) {
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
    public ArrayList<World> getWorldPopulation(@RequestParam(value = "name") String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(country.Population)"
                            + "FROM country";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<World> worldArray = new ArrayList<>();

            while (rset.next()) {
                World worldPop = new World();
                worldPop.population = rset.getLong("SUM(country.Population)");
                worldArray.add(worldPop);
            }
            return worldArray;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population details");
            return null;
        }
    }

    // Get the world population
    public World getWorldPopulation() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(country.Population)"
                            + "FROM country";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                World world = new World();
                world.population = rset.getLong("SUM(country.Population)");
                return world;
            } else
                return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get world population details");
            return null;
        }
    }

    // Displays the worlds population
    public void displayWorldPop(World world) {
        if (world != null) {
            System.out.println(
                    "World  " + world.population);
        } else {
            System.out.println("No world population found");
        }
    }

    /**
     * Gets all the countries
     * @param name countries in the world
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("worldCountries")
    public ArrayList<Country> getWorldCountries(@RequestParam(value = "name") String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get all the countries in the world
     * @return A list of all countries in the world or null if there is an error
     */
    public ArrayList<Country> getTheWorldCountries() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, country.Capital "
                            + "FROM country "
                            + "ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next()) {
                Country country = new Country();
                country.countryCode = rset.getString("country.Code");
                country.countryName = rset.getString("country.Name");
                country.continent = rset.getString("country.Continent");
                country.region = rset.getString("country.Region");
                country.population = rset.getInt("country.Population");
                City city = getCityForCountry(rset.getInt("country.Capital"));
                country.capitalName = city.cityName;
                countryArray.add(country);
            }
            return countryArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public void displayWorldCountries(ArrayList<Country> countryArray) {
        // Check the country arraylist is not null
        if (countryArray == null) {
            System.out.println("No countries");
            return;
        }

        // Print header
        System.out.println(String.format("%-15s %-20s %-15s %-15s %-15s %-15s", "Country Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Loop over all countries in the list
        for (Country country : countryArray) {
            String country_string =
                    String.format("%-15s %-20s %-15s %-15s %-15s %-15s",
                            country.countryCode, country.countryName, country.continent, country.region, country.population, country.capitalName);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the countries
     *
     * @param name countries in a continent
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("continentCountries")
    public ArrayList<Country> getContinentCountries(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get all the countries in a continent
     * @return A list of all countries in a continent or null if there is an error
     */
    public ArrayList<Country> getTheContinentCountries(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "WHERE continent LIKE'" + name + "'"
                            + "ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public void displayContinentCountries(ArrayList<Country> countryArray) {
        // Check the country arraylist is not null
        if (countryArray == null) {
            System.out.println("No countries");
            return;
        }

        // Print header
        System.out.println(String.format("%-15s %-20s %-15s %-15s %-15s %-15s", "Country Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Loop over all countries in the list
        for (Country country : countryArray) {
            String country_string =
                    String.format("%-15s %-20s %-15s %-15s %-15s %-15s",
                            country.countryCode, country.countryName, country.continent, country.region, country.population, country.capitalName);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the countries
     *
     * @param name countries in a region
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("regionCountries")
    public ArrayList<Country> getRegionCountries(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Get all the countries in a region
     * @return A list of all countries in a region or null if there is an error
     */
    public ArrayList<Country> getTheRegionCountries(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "WHERE Region LIKE'" + name + "'"
                            + "ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information
            ArrayList<Country> countryArray = new ArrayList<>();
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public void displayRegionCountries(ArrayList<Country> countryArray) {
        // Check the country arraylist is not null
        if (countryArray == null) {
            System.out.println("No countries");
            return;
        }

        // Print header
        System.out.println(String.format("%-15s %-20s %-15s %-15s %-15s %-15s", "Country Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Loop over all countries in the list
        for (Country country : countryArray) {
            String country_string =
                    String.format("%-15s %-20s %-15s %-15s %-15s %-15s",
                            country.countryCode, country.countryName, country.continent, country.region, country.population, country.capitalName);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the cities
     *
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("worldCities")
    public ArrayList<City> getWorldCities() {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     */
    public ArrayList<City> getTheWorldCities() {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayWorldCities(ArrayList<City> cityArray) {
        // Check the country arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s %-15s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s %-15s",
                            city.cityName, city.countryName, city.district, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the cities
     *
     * @param name cities in a continent
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("continentCities")
    public ArrayList<City> getContinentCities(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     */
    public ArrayList<City> getTheContinentCities(String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayContinentCities(ArrayList<City> cityArray) {
        // Check the city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s %-15s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s %-15s",
                            city.cityName, city.countryName, city.district, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the cities
     *
     * @param name cities in a region
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("regionCities")
    public ArrayList<City> getRegionCities(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     */
    public ArrayList<City> getTheRegionCities(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city JOIN country ON city.CountryCode = country.Code "
                            + "WHERE country.Region LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayRegionCities(ArrayList<City> cityArray) {
        // Check the city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s %-15s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s %-15s",
                            city.cityName, city.countryName, city.district, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the cities
     *
     * @param name cities in a country
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("countryCities")
    public ArrayList<City> getCountryCities(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     */
    public ArrayList<City> getTheCountryCities(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city JOIN country ON city.CountryCode = country.Code "
                            + "WHERE country.Name LIKE '" + name + "'"
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<City> cityArray = new ArrayList<>();
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayCountryCities(ArrayList<City> cityArray) {
        // Check the city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s %-15s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s %-15s",
                            city.cityName, city.countryName, city.district, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the cities
     *
     * @param name cities in a district
     * @return A list of all cities, or null if there is an error.
     */
    @RequestMapping("districtCities")
    public ArrayList<City> getDistrictCities(@RequestParam(value = "name") String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the cities
     * @return A list of all cities, or null if there is an error.
     */
    public ArrayList<City> getTheDistrictCities(String name) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayDistrictCities(ArrayList<City> cityArray) {
        // Check the city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s %-15s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s %-15s",
                            city.cityName, city.countryName, city.district, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the capital cities
     *
     * @param name capital cities in the world
     * @return A list of all capital cities, or null if there is an error.
     */
    @RequestMapping("worldCapitalCities")
    public ArrayList<CapitalCity> getWorldCapitalCities(@RequestParam(value = "name") String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next()) {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the capital cities
     * @return A list of all capital cities, or null if there is an error.
     */
    public ArrayList<CapitalCity> getTheWorldCapitalCities() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next()) {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayWorldCapitalCities(ArrayList<CapitalCity> cityArray) {
        // Check the capital city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s", "Name", "Country", "Population"));
        // Loop over all countries in the list
        for (CapitalCity city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s",
                            city.cityName, city.countryName, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the capital cities
     *
     * @param name capital cities in a continent
     * @return A list of all capital cities, or null if there is an error.
     */
    @RequestMapping("continentCapitalCities")
    public ArrayList<CapitalCity> getContinentCapitalCities(@RequestParam(value = "name") String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.continent LIKE '" + name + "' "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next()) {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the capital cities
     * @return A list of all capital cities, or null if there is an error.
     */
    public ArrayList<CapitalCity> getTheContinentCapitalCities(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.continent LIKE '" + name + "' "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next()) {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayContinentCapitalCities(ArrayList<CapitalCity> cityArray) {
        // Check the capital city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s", "Name", "Country", "Population"));
        // Loop over all countries in the list
        for (CapitalCity city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s",
                            city.cityName, city.countryName, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }

    /**
     * Gets all the capital cities
     *
     * @param name capital cities in a region
     * @return A list of all capital cities, or null if there is an error.
     */
    @RequestMapping("regionCapitalCities")
    public ArrayList<CapitalCity> getRegionCapitalCities(@RequestParam(value = "name") String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.region LIKE '" + name + "' "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next()) {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    /**
     * Gets all the capital cities
     * @return A list of all capital cities, or null if there is an error.
     */
    public ArrayList<CapitalCity> getTheRegionCapitalCities(String name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city JOIN country ON city.ID = country.Capital "
                            + "WHERE country.Region LIKE '" + name + "' "
                            + "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information
            ArrayList<CapitalCity> cityArray = new ArrayList<>();
            while (rset.next()) {
                CapitalCity city = new CapitalCity();
                city.cityName = rset.getString("city.Name");
                Country country = getCountryForCity(rset.getString("CountryCode"));
                city.countryName = country.countryName;
                city.population = rset.getInt("city.Population");
                cityArray.add(city);
            }
            return cityArray;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public void displayRegionCapitalCities(ArrayList<CapitalCity> cityArray) {
        // Check the capital city arraylist is not null
        if (cityArray == null) {
            System.out.println("No cities");
            return;
        }

        // Print header
        System.out.println(String.format("%-20s %-20s %-15s", "Name", "Country", "Population"));
        // Loop over all countries in the list
        for (CapitalCity city : cityArray) {
            String country_string =
                    String.format("%-20s %-20s %-15s",
                            city.cityName, city.countryName, city.population);
            System.out.println(country_string);
        }
        System.out.println("\n");
    }





    /**
     * Gets all the countries
     *
     * @param name of continent
     * @param num  of countries to be displayed
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("continentCountriesNum")
    public ArrayList<Country> getContinentCountriesNum(@RequestParam(value = "name") String name, @RequestParam(value = "num") String num) {
        try {
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
            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * Gets all the countries
     *
     * @param name of region
     * @param num  of countries to be displayed
     * @return A list of all countries, or null if there is an error.
     */
    @RequestMapping("regionCountriesNum")
    public ArrayList<Country> getRegionCountriesNum(@RequestParam(value = "name") String name, @RequestParam(value = "num") String num) {
        try {
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

            while (rset.next()) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }
}












