package com.thoughtworks.services.railroad;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * Unit test for simple App.
 */
public class RouteTest 
{
    /**
     * Create the test case
     * @param args the command-line arguments
     * 			arg[0] path to a file that has rail routes
     * 			arg[1] test case 1 through 10
     */
    
	public static void main(String[] args) {
    	Scanner scanner = null;

    	try {
            // first try to read file from local file system
        	if (args.length < 2) {
        		throw new IllegalArgumentException("Usage Exception, arg[0] has to be the file with route inputs. arg[1] has to be the test case number, 1 through 10");
        	}
        	File file = new File(args[0]);
            if (file.exists()) {
                // for consistency with StdIn, wrap with BufferedInputStream instead of use
                // file as argument to Scanner
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis));
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + args[0], ioe);
        }
  
    	CitiesRouteGraph graph = new CitiesRouteGraph(scanner);
    	
    	if (args[1] == null) {
    		throw new IllegalArgumentException("Usage Exception, arg[1] has to be the test case number 1 through 10");
    	}
    	
    	int testCase = 0;
    	try {
    		testCase = Integer.parseInt(args[1]);
    	} catch (NumberFormatException nfe) {
    		throw new IllegalArgumentException("Usage Exception, arg[1] has to be the test case number 1 through 10");
    	}

    	switch(testCase) {
	    	case 1: {
	            //Test case 1
	            List<String> testCase1 = new LinkedList<String>(); 
	            testCase1.add("A");
	            testCase1.add("B");
	            testCase1.add("C");
	            
	            int distance = graph.findDistanceofRoute(testCase1);
	            if (distance < 0) {
	            	System.out.println("Output #1: " + "NO SUCH ROUTE");                    	
	            } else {
	            	System.out.println("Output #1: " + distance);
	            }
	             
	    		return;
	    	}
	    	case 2: {
	            //Test case 2
	            List<String> testCase2 = new LinkedList<String>(); 
	            testCase2.add("A");
	            testCase2.add("D");
	            
	            int distance = graph.findDistanceofRoute(testCase2);
	            if (distance < 0) {
	            	System.out.println("Output #2: " + "NO SUCH ROUTE");                    	
	            } else {
	            	System.out.println("Output #2: " + distance);
	            }
	    		return;
	    	}
	    	case 3: {
	            List<String> testCase3 = new LinkedList<String>(); 
	            testCase3.add("A");
	            testCase3.add("D");
	            testCase3.add("C");
	            
	            int distance = graph.findDistanceofRoute(testCase3);
	            if (distance < 0) {
	            	System.out.println("Output #3: " + "NO SUCH ROUTE");                    	
	            } else {
	            	System.out.println("Output #3: " + distance);
	            }

	    		return;
	    	}
	    	case 4: {
	            List<String> testCase4 = new LinkedList<String>(); 
	            testCase4.add("A");
	            testCase4.add("E");
	            testCase4.add("B");
	            testCase4.add("C");
	            testCase4.add("D");
	            int distance = graph.findDistanceofRoute(testCase4);
	            if (distance < 0) {
	            	System.out.println("Output #4: " + "NO SUCH ROUTE");                    	
	            } else {
	            	System.out.println("Output #4: " + distance);
	            }
	            
	    		return;
	    	}
	    	case 5: {
	            List<String> testCase5 = new LinkedList<String>(); 
	            testCase5.add("A");
	            testCase5.add("E");
	            testCase5.add("D");
	            int distance = graph.findDistanceofRoute(testCase5);
	            if (distance < 0) {
	            	System.out.println("Output #5: " + "NO SUCH ROUTE");                    	
	            } else {
	            	System.out.println("Output #5: " + distance);
	            }
	            return;
	    	}
    		case 6: {
				
		        // compute all paths
		        PathsFinder pf = new PathsFinder(graph);
		        
		        //Routes with max 3 Stops, start city C - end city C
		        pf.findAllPaths("C", "C", PathsFinder.CASETYPE_MAX, 3);
		        
		        List<ConnectedRoutesCollection<String>> possiblePaths =  pf.possiblePaths;
		        
		        System.out.println("Output #6:\nTotal # of routes with max 3 Stops, start city C - end city C --> " + possiblePaths.size() + ". \nList of path as below: ");  
		        
		        for(ConnectedRoutesCollection<String> possiblePath : possiblePaths) {
		        	for(String city : possiblePath)
		        		System.out.print(city);
		            System.out.println();  
		        }
		        return;
			}

    		case 7: {
	
		        // compute all paths
		        PathsFinder pf = new PathsFinder(graph);
		        //Routes with exactly 4 Stops, start city A - end city C
		        pf.findAllPaths("A", "C", PathsFinder.CASTTYPE_EXACT, 4);
		        
		        List<ConnectedRoutesCollection<String>> possiblePaths =  pf.possiblePaths;
		        
		        System.out.println("Output #7:\nTotal # of routes with exactly 4 Stops, start city A - end city C --> " + possiblePaths.size() + ". \nList of path as below: ");  

		        for(ConnectedRoutesCollection<String> possiblePath : possiblePaths) {
		        	for(String city : possiblePath)
		        		System.out.print(city);
		            System.out.println();  
		        }
		        return;
			}

    		case 8: {

    	        //Prepare additional inputs for test case 8 - shortest paths
    	        ShortestPath sp = new ShortestPath(graph);
    	        String fromCity = "A";
    	        String toCity = "C";
    	        
    	        sp.computeShortestPath(fromCity );
    	        if (sp.hasPathTo(toCity)) {
    	        	int distance = 0;
    	            for (Route route : sp.pathTo(toCity)) {
    	            	distance += route.getDistance();
    	            }
    	            System.out.println("Output #8: " + distance);
    	        } else {
    	          	System.out.print("There is no path from city" + fromCity + " to " + toCity);
    	        }
    	        return;
    		}
    		
    		case 9: {

    	        //Prepare additional inputs for test case 8 - shortest paths
    	        ShortestPath sp = new ShortestPath(graph);
    	        String fromCity = "B";
    	        String toCity = "B";
    	        
    	        sp.computeShortestPath(fromCity );
    	        if (sp.hasPathTo(toCity)) {
    	        	int distance = 0;
    	            for (Route route : sp.pathTo(toCity)) {
    	            	distance += route.getDistance();
    	            }
    	            
    	            System.out.println("Output #9: " + distance);
    	        } else {
    	          	System.out.print("Output #9: There is no path from city" + fromCity + " to " + toCity);
    	          	
    	        }
    	        return;
    		}
    		case 10: {

    	        // compute all paths
    	        PathsFinder pf = new PathsFinder(graph);

    	        //"Routes with max of 30 distance points, start city C - end city C;        
    	        pf.findAllPaths("C", "C", PathsFinder.CASETYPE_MAXDIST, 30);
    	        
		        List<ConnectedRoutesCollection<String>> possiblePaths =  pf.possiblePaths;
		        
		        System.out.println("Output #10:\nTotal # of routes with max of 30 distance points, start city C - end city C --> " + possiblePaths.size() + ". \nList of path as below: ");  

    	        for(ConnectedRoutesCollection<String> possiblePath : possiblePaths) {
    	        	for(String city : possiblePath)
    	        		System.out.print(city);
    	            System.out.println();  
    	        }
    	        return;
    		}
    	}
    }
}
