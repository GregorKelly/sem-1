package com.napier.sem;

//import com.sun.org.apache.xpath.internal.operations.Variable;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Get City By City Name
        City city = a.getCity("Edinburgh");
        a.displayCity(city);

        // Get Country By Country Code
        Country country = a.getCountry("GBR");
        a.displayCountry(country);

        // Get Capital City By Country Name
        City capitalCity = a.getCapitalCity("France");
        a.displayCapitalCity(capitalCity);


        // Not giving correct answers

        // Get Country Populations By Country Name
        Country countryPop = a.getCountryPopulation("United Kingdom");
        a.displayCountryPopulation(countryPop);

       // Get Region Populations By Region Name
        Region regionPop = a.getRegionPopulation("British Islands");
        a.displayRegionPopulation(regionPop);

        // Get Continent Populations By Continent Name
        Continent continentPop = a.getContinentPopulation("Asia");
        a.displayContinentPopulation(continentPop);



        // Extract city information
        //ArrayList<City> cities = a.getAllCities();
        //a.printCities(cities);

        // Test the size of the returned data
        //System.out.println(cities.size());

        // Disconnect from database
        a.disconnect();
    }

        /**
         * Connection to MySQL database.
         */
        private Connection con = null;

        /**
         * Connect to the MySQL database.
         */
        public void connect()
        {
            try
            {
                // Load Database driver
                Class.forName("com.mysql.jdbc.Driver");
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
                    con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
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
        public void disconnect()
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

        public City getCity(String name)
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
            // Return new city if valid.
            // Check one is returned
            if (rset.next())
            {
                City city = new City();
                city.city_ID = rset.getInt("ID");
                city.city_name = rset.getString("Name");

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

    public Country getCountry(String code)
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

    public City getCapitalCity(String name)
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



    public Country getCountryPopulation(String name)
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

    public Region getRegionPopulation(String region)
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

    public Continent getContinentPopulation(String continent)
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