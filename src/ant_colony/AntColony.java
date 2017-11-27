package ant_colony;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

public class AntColony {
	
	private double c = 1.0;
    private double alpha = 1;
    private double beta = 5;
    private double evaporation = 0.5;
    private double Q = 500;
    private double antFactor = 0.8;
    private double randomFactor = 0.01;

    private int maxIterations = 1000;
    
    private int numberOfCities;
    private int numberOfAnts;
    private double trails[][];
    private double probabilities[];
    
    private Random random = new Random();

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;
    
    private List<Ant> ants = new ArrayList<>();
    
    private String[] bestTourSwitch = new String[10];
    private double[] bestTourCost;
	private ArrayList<Switch> switchs = new ArrayList<>();
//	private ArrayList<Link> links = new ArrayList<>();
	private ArrayList<Graph> graphs = new ArrayList<>();
    
    public AntColony(int numSwitch) {
    	// Add switch
    	AddSwichs(numSwitch);
    	// Add graph
    	AddGraph();
    	
    	numberOfCities = switchs.size();// = numSwitch;
    	numberOfAnts = (int) (numberOfCities * antFactor);
    	System.out.println("number: " + numberOfCities + "; ant: " + numberOfAnts);
    	
    	trails = new double[numberOfCities][numberOfCities];
    	probabilities = new double[numberOfCities];
    	
    	// Add Ant
    	switchs.forEach(sw -> ants.add(new Ant(numberOfCities)));
    }
    
    public void AddSwichs(int n) {
    	IntStream.range(0, n)
    		.forEach(i -> {
    			switchs.add(new Switch("s" + i, i));
    		});
    }
    
    public void AddGraph() {
    	switchs.forEach(src -> {
    		ArrayList<Link> links = new ArrayList<>();
    		
    		switchs.forEach(dst -> {
    			if (src.isEqualSw(dst)) {
    				links.add(new Link(src, dst, 0.0));
    			} else {
//    				if (src.getPos() == 2 && dst.getPos() == 5) {
//    					links.add(new Link(src, dst, -1));
//    				} else if (src.getPos() == 4 && dst.getPos() == 7) {
//    					links.add(new Link(src, dst, -1));
//    				} else if (src.getPos() == 5 && dst.getPos() == 2) {
//    					links.add(new Link(src, dst, -1));
//    				} else if (src.getPos() == 6 && dst.getPos() == 9) {
//    					links.add(new Link(src, dst, -1));
//    				} else if (src.getPos() == 7 && dst.getPos() == 4) {
//    					links.add(new Link(src, dst, -1));
//    				} else if (src.getPos() == 9 && dst.getPos() == 6) {
//    					links.add(new Link(src, dst, -1));
//    				} else {
//    					double cost = Math.abs(random.nextInt(100) + 1);
//            			links.add(new Link(src, dst, cost));
//    				}
    				double cost = Math.abs(random.nextInt(100) + 1);
        			links.add(new Link(src, dst, cost));
    			}	
    		});
//    		System.out.println("**********************************" + links.get(0).getCost());
    		graphs.add(new Graph(src, links));
    	});
    }
    
    public void startACO() {
    	graphs.forEach(graph -> {
//    		System.out.println("**********************************");
//    		System.out.println("switch: " + graph.getSw().getSw() + "; position: " + graph.getSw().getPos());
    		
    		graph.getLinks().forEach(link -> {
    			System.out.print(" | " + link.getSrc().getSw() + "->" + link.getDst().getSw() + ": " + link.getCost());
    		});
    		System.out.println();
    	});
    	
    	IntStream.rangeClosed(1, 10)
        .forEach(i -> {
            System.out.println("Attempt #" + i);
            solve();
        });
    }
    
    /**
     * Use this method to run the main logic
     */
    public int[] solve() {
        setupAnts();
        clearTrails();
        IntStream.range(0, maxIterations)
            .forEach(i -> {
                moveAnts();
                updateTrails();
                updateBest();
            });
        System.out.println("Best tour length: " + (bestTourLength - numberOfCities));
        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));
        System.out.println("Best tour Switch: " + Arrays.toString(bestTourSwitch));
        System.out.println("Best tour Cost: " + Arrays.toString(bestTourCost));
        return bestTourOrder.clone();
    }
    
    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        IntStream.range(0, numberOfAnts)
            .forEach(i -> {
                ants.forEach(ant -> {
                	ant.clear();
//                	ant.visitCity(-1, random.nextInt(numberOfCities));
                	int ranSw = random.nextInt(numberOfCities);
                	ant.visitCity(-1, ranSw, switchs.get(ranSw));
                });
            });
        currentIndex = 0;
    }
    
    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        IntStream.range(currentIndex, numberOfCities - 1)
            .forEach(i -> {
                ants.forEach(ant -> {
                	int nextSw = selectNextCity(ant);
                	ant.visitCity(currentIndex, nextSw, switchs.get(nextSw));
                });
                currentIndex++;
            });
    }
    
    /**
     * Select next city for each ant
     */
    private int selectNextCity(Ant ant) {
        int t = random.nextInt(numberOfCities - currentIndex);
        
        if (random.nextDouble() < randomFactor) {
            OptionalInt cityIndex = IntStream.range(0, numberOfCities)
                .filter(i -> i == t && !ant.visited(i))
                .findFirst();
            
            if (cityIndex.isPresent()) {
                return cityIndex.getAsInt();
            }
        }

        calculateProbabilities(ant);

        double r = random.nextDouble();
        double total = 0;
        
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }

        throw new RuntimeException("There are no other cities");
    }
    
//    /**
//     * Calculate the next city picks probabilites
//     */
//    public void calculateProbabilities(Ant ant) {
//        int i = ant.trail[currentIndex];
//        
//        double pheromone = 0.0;
////        for (int l = 0; l < numberOfCities; l++) {
////            if (!ant.visited(l)) {
////                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graphs.get(i).getLinks().get(l).getCost(), beta);
////            }
////        }
//        
////        for (int j = 0; j < numberOfCities; j++) {
////            if (ant.visited(j)) {
////                probabilities[j] = 0.0;
////            } else {
////                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graphs.get(i).getLinks().get(j).getCost(), beta);
////                probabilities[j] = numerator / pheromone;
////            }
////        }
//            
//        for (Link link : graphs.get(i).getLinks()) {
//        	if (!ant.visited(link.getDst().getPos())) {
//        		pheromone += Math.pow(trails[i][link.getDst().getPos()], alpha) * Math.pow(1.0 / link.getCost(), beta);
//        	}
//        }
//        for (Link link : graphs.get(i).getLinks()) {
//        	if (ant.visited(link.getDst().getPos())) {
//        		probabilities[link.getDst().getPos()] = 0.0;
//        	} else {
//        		double numerator = Math.pow(trails[i][link.getDst().getPos()], alpha) * Math.pow(1.0 / link.getCost(), beta);
//	            probabilities[link.getDst().getPos()] = numerator / pheromone;
//        	}
//        }
//    }
    
    /**
     * Calculate the next city picks probabilites
     */
    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
            	if (graphs.get(i).getLinks().get(l).hasCost()) {
            		pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graphs.get(i).getLinks().get(l).getCost(), beta);
            	}
            }
        }
        
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
            	if (graphs.get(i).getLinks().get(j).hasCost()) {
            		double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graphs.get(i).getLinks().get(j).getCost(), beta);
                    probabilities[j] = numerator / pheromone;
            	}
            }
        }
    }
    
    /**
     * Update trails that ants used
     */
    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                trails[i][j] *= evaporation;
            }
        }
        for (Ant a : ants) {
            double contribution = Q / a.trailLength(graphs);
            for (int i = 0; i < numberOfCities - 1; i++) {
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }
    
    /**
     * Update the best solution
     */
    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0)
                .trailLength(graphs);
            
            for (int i = 0; i < numberOfCities; i++) {
        		bestTourSwitch[i] = ants.get(0).switchs.get(i).getSw();
            }
            bestTourCost = ants.get(0).arrayCost(graphs);
        }
        
        for (Ant a : ants) {
            if (a.trailLength(graphs) < bestTourLength) {
                bestTourLength = a.trailLength(graphs);
                bestTourOrder = a.trail.clone();

                for (int i = 0; i < numberOfCities ; i++) {
                	bestTourSwitch[i] = a.switchs.get(i).getSw();
                }
                
                bestTourCost = a.arrayCost(graphs);
            }
        }
    }
    
    
    
    
    /**
     * Clear trails after simulation
     */
    private void clearTrails() {
        IntStream.range(0, numberOfCities)
            .forEach(i -> {
                IntStream.range(0, numberOfCities)
                    .forEach(j -> trails[i][j] = c);
            });
    }
}
