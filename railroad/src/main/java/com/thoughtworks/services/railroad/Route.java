package com.thoughtworks.services.railroad;


public class Route { 
    private final String fromCity;
    private final String toCity;
    private final Double distance;

    public Route(String fromCity, String toCity, double distance) {
    	//Validate Arguments for the Route construction
        if (fromCity == null || fromCity.trim().isEmpty() ) {
        	throw new IllegalArgumentException("Please enter a valid from City Name for the route");
        }
        if (toCity == null || toCity.trim().isEmpty() ) {
        	throw new IllegalArgumentException("Please enter a valid to City Name for the route");
        }
        if (distance <= 0 ) {
        	throw new IllegalArgumentException("Please enter a value greater than 0 for Route distance");
        }

        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    /**
     * Returns the to city of the route.
     * @return the to city of the route
     */
    public String getToCity() {
        return toCity;
    }

    /**
     * Returns the from city of the route.
     * @return the from city of the route
     */
    public String getFromCity() {
        return fromCity;
    }

    /**
     * Returns the distance of the route.
     * @return the distance of the route
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns a string representation of the route.
     * @return a string representation of the route
     */
    public String toString() {
        return fromCity + "->" + toCity + " " + distance;
    }
 }
