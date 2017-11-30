package com.thoughtworks.services.railroad;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;


/**
 *  The {@code ShortestPath} class computes the shortest path between 2 cities 
 *  represented in {@link CitiesRouteGraph} 
 *  The implementation uses Dijkstra algorithm to compute shortest path
 *  
 */

public class ShortestPath {
	//distTo - distance (represented as double) to a specify city (vertex)
    private Map<String, Double> distTo;          
    //edgeTo - last edge on shortest path
    private Map<String, Route> edgeTo;   
    //Priority queue of vertices
    private PriorityQueue<City> pq;    
    //Graph edge weighted directed graph
    private CitiesRouteGraph graph = null;

    /**
     * Constructor 
     * @param  graph edge weighted directed graph
     * @param  fromCity the source city/vertex
     * @throws IllegalArgumentException if an edge weight is negative
     */
    public ShortestPath(CitiesRouteGraph graph) {
    	//validate for negative distance
        this.graph = graph;
        for (Route route : graph.routes()) {
            if (route.getDistance() < 0)
                throw new IllegalArgumentException("route" + route + " has negative distance");
        }

        //Construct dissTo & edgeTo Hash Maps
        distTo = new HashMap<String, Double>();
        edgeTo = new HashMap<String, Route>();
    }

    /**
     * The method computes a shortest path tree from the source city/vertex {@code fromCity} 
     * to every other city/vertex in the 
     * edge weighted directed graph {@code graph}.
     *
     * @param  fromCity the source city/vertex
     * @throws IllegalArgumentException if an edge weight is negative
     */

    public void computeShortestPath(String fromCity) {

        //Initialize the distTo to infinite value for all the destinations, except for the source itself
        for (String city : graph.getCities())
            distTo.put(city, Double.POSITIVE_INFINITY);
        distTo.put(fromCity, 0.0);

        //Initialize the PQ with the custom comparator as we want min distance
        Comparator<City> comparator = new Comparator<City>() {

			public int compare(City city1, City city2) {				
				return 	(int) (city1.getDistance() - city2.getDistance());
			}        	
        };
        
        pq = new PriorityQueue<City>(graph.getCityCount(), comparator);
        pq.add(new City(fromCity, 0.0));
        
        //While the priority queue is not empty
        //relax vertices in order of distance from fromCity

        int iterationCount = 0;
        while (!pq.isEmpty()) {
        	//Get the head of the priority queue
            City city = pq.peek();
            
            //Remove the head of the priority queue
            pq.remove(city);
            for (Route route : graph.adj(city.getCityName())) {
            	//Relax all the routes adjacent to the city
            	relax(route);
            }
            if (iterationCount == 0) {
            	distTo.put(fromCity, Double.POSITIVE_INFINITY);
            	iterationCount++;
            }
            
        }

        //Check optimality conditions after determining the shortest route
        assert check(graph, fromCity);
    }

     
    /**
     * Relax Route 
     * @param  route the Route to relax 
     * @return none
     */
    private void relax(Route route) {
        String fromCity = route.getFromCity();
        String toCity = route.getToCity();

        //If the distTo the destination city in record is farther than the new route, 
        //update the distTo to new lower
        if (distTo(toCity) > distTo(fromCity) + route.getDistance()) {
            distTo.put(toCity, distTo(fromCity) + route.getDistance());
            edgeTo.put(toCity, route);
            City newCity = new City(toCity, route.getDistance());
            if (!pq.contains(newCity)) {
            	pq.add(newCity);
            }
        }
    }

    /**
     * Returns the length of a shortest path from the source city / vertex {@code city}.
     * @param  city the destination to the city
     * @return the length of a shortest path from the source city/vertex {@code fromCity} to city/vertex {@code city};
     *         if no such path, {@code Double.POSITIVE_INFINITY} 
     */
    public Double distTo(String city) {
    	return (distTo.get(city) == null ? Double.POSITIVE_INFINITY : distTo.get(city));
    }

    /**
     * Returns true if there is a path from the source vertex {@code fromCity} to vertex {@code city}.
     *
     * @param  city the destination city/vertex
     * @return {@code true} if there is a path from the source city/vertex
     *         {@code fromCity} to city/vertex {@code city}; {@code false} otherwise
     */
    public boolean hasPathTo(String city) {
        return distTo.get(city) != null;
    }

    /**
     * Returns a shortest path from the source city/vertex {@code fromCity} to city/vertex {@code city}.
     *
     * @param  city the destination city/vertex
     * @return a shortest path from the source vertex {@code fromCity} to vertex {@code city}
     *         as an iterable of routes, and {@code null} if there is no such path
     */
    public Iterable<Route> pathTo(String city) {
        if (!hasPathTo(city)) return null;
        Stack<Route> path = new Stack<Route>();
        for (Route route = edgeTo.get(city); route != null; route = edgeTo.get(route.getFromCity())) {
        	path.push(route);
            if (route.getFromCity().equals(city)) {
            	break;
            }
        }
        return path;
    }


    //Check optimality conditions
    private boolean check(CitiesRouteGraph g, String fromCity) {
    	// check that edge weights are nonnegative
        for (Route route : g.routes()) {
            if (route.getDistance() < 0) {
                System.err.println("negative route distance detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo.get(fromCity) != 0 || edgeTo.get(fromCity) != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        
        for (String city : g.getCities()) {
            if (city.equals(fromCity)) continue;
            if (edgeTo.get(city) == null && distTo.get(city) == null) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (String city : g.getCities()) {
            for (Route route : g.adj(city)) {
                String toCity = route.getToCity();
                if (distTo.get(toCity) + route.getDistance() < distTo.get(fromCity)) {
                    System.err.println("route " + route + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (String city : g.getCities()) {
            if (edgeTo.get(city) == null) continue;
            Route route = edgeTo.get(city);
            String fCity = route.getFromCity();
            if (!fCity.equals(route.getToCity())) return false;
            if (distTo.get(fCity) + route.getDistance() != distTo.get(city)) {
                System.err.println("route " + route + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }
}