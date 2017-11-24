package ant_colony;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

public class TestArray {
	private ArrayList<Switch> switchs = new ArrayList<>();
//	private ArrayList<Link> links = new ArrayList<>();
	private ArrayList<Graph> graphs = new ArrayList<>();
    private Random random = new Random();
    
    public TestArray(int noOfCities) {
    	AddSwichs(noOfCities);
    	AddGraph(noOfCities);
    }
    
    public void AddSwichs(int n) {
    	IntStream.range(0, n)
    		.forEach(i -> {
    			switchs.add(new Switch("s" + i, i));
    		});
    }
    
    public void AddGraph(int n) {
    	switchs.forEach(src -> {
    		ArrayList<Link> links = new ArrayList<>();
    		System.out.println("**********************************" + links);
    		switchs.forEach(dst -> {
    			if (src.isEqualSw(dst)) {
    				links.add(new Link(src, dst, 0.0));
    			} else {
    				double cost = Math.abs(random.nextInt(100) + 1);
        			links.add(new Link(src, dst, cost));
    			}	
    		});
    		System.out.println("**********************************" + links.get(0).getCost());
    		graphs.add(new Graph(src, links));
    	});
    }
    
    public void startTest() {
    	graphs.forEach(graph -> {
    		System.out.println("**********************************");
    		System.out.println("switch: " + graph.getSw().getSw() + "; position: " + graph.getSw().getPos());
    		
    		graph.getLinks().forEach(link -> {
    			System.out.print(" | " + link.getSrc().getSw() + ", " + link.getDst().getSw() + ", " + link.getCost());
    		});
    		System.out.println();
    	});
    }
}
