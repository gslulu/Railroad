package com.thoughtworks.services.railroad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CitiesRouteGraph {

	private static final String NEWLINE = System.getProperty("line.separator");

	
    private final int cityCount;                // number of cities / vertices in this digraph
    private int routeCount;                      // number of routes in this digraph
    private Map<String, ConnectedRoutesCollection<Route>> adj;    // adj = adjacency map for vertex v
    private Set<String> cities;
    private Map<String, Integer> indegree;             
    
    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public CitiesRouteGraph(int cityCount) {
        if (cityCount < 0) {
        	throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        
        this.cityCount = cityCount;
        this.routeCount = 0;
        
        this.indegree = new HashMap<String, Integer>(cityCount);
        
        this.cities = new HashSet<String>(cityCount);
        adj = new HashMap<String, ConnectedRoutesCollection<Route>>();
    }


    /**  
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public CitiesRouteGraph(Scanner scanner) {
        this(scanner.nextInt());
        int cityCount = scanner.nextInt();
        if (cityCount < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < cityCount; i++) {
            String fromCity  = scanner.next();
            String toCity = scanner.next();
            //Validate City
            validateCityName(fromCity);
            validateCityName(toCity);
            
            int distance = scanner.nextInt();
            
            if(!cities.contains(toCity)) {
            	cities.add(toCity);
            }
            if(!cities.contains(fromCity)) {
            	cities.add(fromCity);
            }
            
            addRoute(new Route(fromCity, toCity, distance));
        }
    }


    /**
     * Returns the cities of all routes in this edge weighted digraph.
     *
     * @return the cities of all routes in this edge weighted digraph.
     */
    public Set<String> getCities() {
        return cities;
    }

    /**
     * Returns the number of routes in this edge weighted digraph.
     *
     * @return the number of routes in this edge weighted digraph.
     */
    public int getCityCount() {
        return cityCount;
    }

    /**
     * Returns the number of edges/routes in this edge weighted digraph.
     *
     * @return the number of edges/routes in this edge weighted digraph
     */
    public int getRouteCount() {
        return routeCount;
    }

    // throw an IllegalArgumentException if City Name is incomplete
    private void validateCityName(String city) {
        if (city == null || city.trim().isEmpty())
            throw new IllegalArgumentException("City" + city + " is incorrect");
    }

    //Add route to the graph
    public void addRoute(Route route) {
        String fromCity = route.getFromCity();
        String toCity = route.getToCity();
        
        //Validate city names
        validateCityName(fromCity);
        validateCityName(toCity);
        
        ConnectedRoutesCollection<Route> collection = adj.getOrDefault(fromCity, new ConnectedRoutesCollection<Route>());
        collection.add(route);
        adj.put(fromCity, collection);
        Integer countOfIndegree = indegree.getOrDefault(toCity, 0);
       	countOfIndegree++;
        indegree.put(toCity, countOfIndegree);
        routeCount++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Route> adj(String fromCity) {
        validateCityName(fromCity);
        return adj.get(fromCity);
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(String fromCity) {
        validateCityName(fromCity);
        return adj.get(fromCity).size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(String toCity) {
        validateCityName(toCity);
        return indegree.get(toCity);
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (Route e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<Route> routes() {
        ConnectedRoutesCollection<Route> list = new ConnectedRoutesCollection<Route>();
   	
        Set<String> keySet = adj.keySet();
        for (String key : keySet) {
            ConnectedRoutesCollection<Route> routes = adj.get(key);
            for(Route route : routes) {
            	list.add(route);
            }
        }
        
        return list;
    } 

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices/cities <em>cityCount</em>, followed by the number of edges/routes <em>routeCount</em>,
     *         followed by the <em>routeCount</em> adjacency lists of edges/routes
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int i = 0;
        s.append(cityCount + " " + routeCount + NEWLINE);
            Set<String> keySet = adj.keySet();
            for (String key: keySet) {
                s.append(i++ + ": ");
                ConnectedRoutesCollection<Route> routes = adj.get(key);
                for(Route route : routes) {
                	s.append(route + "  ");
                }
                s.append(NEWLINE);
            }
        return s.toString();
    }

    //Find the distance of a given route
	int findDistanceofRoute(List<String> cities) {
		int distance = 0;
		
		if (cities == null || cities.isEmpty()) {
			return 0;
		}

		ConnectedRoutesCollection<Route> routeFromCity = null;
		for(String city : cities) {
			if (city != null && city.trim().isEmpty()) return -1;
			if (routeFromCity == null) {
				routeFromCity = adj.get(city);
			} else {
				Iterator<Route> iterator = routeFromCity.iterator();
				boolean routeFound = false;
				while(iterator.hasNext()) {
					Route route = iterator.next();
					if (route.getToCity().equals(city)) {
						routeFromCity = adj.get(city);
						distance += route.getDistance();
						routeFound = true;
						break;
					}
				}
				if (!routeFound) {
					return -1;
				}
			}
		}
		return distance;
	}
}