package com.thoughtworks.services.railroad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PathsFinder {
    // Mark all the vertices as not visited
    CitiesRouteGraph graph = null;
    // Create an array to store paths
    List<String> path = null;
    List<ConnectedRoutesCollection<String>> possiblePaths = null;

    static final int CASTTYPE_EXACT = 0;
    static final int CASETYPE_MAX = 1;
    static final int CASETYPE_MAXDIST = 2;

    PathsFinder(CitiesRouteGraph graph) {
        this.graph = graph;
        // Create an array to store paths
        path =  new LinkedList<String>(); // Initialize path[] as empty
    }
    
    void findAllPaths(String fromCity, String toCity, int caseType, int stopCount)
    {
   
        int stops = 0;  
        int distance = 0;
        // Call the recursive helper function to print all paths
        possiblePaths = new ArrayList<ConnectedRoutesCollection<String>>();
        findAllPaths(fromCity, toCity, stops, distance, caseType, stopCount);
    }
     
    // A recursive function to print all paths from 'fromCity' to 'toCity'.
    void findAllPaths(String fromCity, String toCity, int stops, int distance, int caseType, int stopCount)
    {
        // Mark the current node and store it in path[]
        //visited.put(fromCity, true);
        path.add(stops, fromCity);
     
        if (fromCity.equals(toCity)  && stops > 0) {
        	if ((stops <= stopCount && caseType == CASETYPE_MAX) || 
        			(stops == stopCount && caseType == CASTTYPE_EXACT) || caseType == CASETYPE_MAXDIST) {
	       		ConnectedRoutesCollection<String> possiblePath = new ConnectedRoutesCollection<String>();
	            possiblePaths.add(possiblePath);
	       	 
	        	for (int i = stops; i>= 0; i--) {
	                possiblePath.add(path.get(i));
	        	}
        	}
        }

        stops++;        
 
        // If current vertex is not same as destination, then recurse
        if (stops <= stopCount && (caseType == CASETYPE_MAX || caseType == CASTTYPE_EXACT) ||
        		distance < stopCount && caseType == CASETYPE_MAXDIST)
        {
            // Recur for all the vertices adjacent to current vertex
            for (Route route : graph.adj(fromCity)) {
        		distance += route.getDistance();
        		findAllPaths(route.getToCity(), toCity, stops, distance, caseType, stopCount);
        		distance -= route.getDistance();
            }
        } 

        stops--;
    }
}