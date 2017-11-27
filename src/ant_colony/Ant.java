package ant_colony;

import java.util.ArrayList;

public class Ant {

	protected int trailSize;
	protected int trail[];
	protected boolean visited[];
	protected ArrayList<Switch> switchs;

	public Ant(int tourSize) {
		this.trailSize = tourSize;
		this.trail = new int[tourSize];
		this.visited = new boolean[tourSize];
		this.switchs = new ArrayList<>();
	}

//	protected void visitCity(int currentIndex, int city) {
//		trail[currentIndex + 1] = city;
//		visited[city] = true;
//	}
	
	protected void visitCity(int currentIndex, int city, Switch sw) {
		trail[currentIndex + 1] = city;
		visited[city] = true;
		switchs.add(sw);
	}

	protected boolean visited(int i) {
		return visited[i];
	}

//	protected double trailLength(double graph[][]) {
//		double length = graph[trail[trailSize - 1]][trail[0]];
//		for (int i = 0; i < trailSize - 1; i++) {
//			length += graph[trail[i]][trail[i + 1]];
//		}
//		return length;
//	}
	
	protected double trailLength(ArrayList<Graph> graphs) {
		double length = graphs.get(trail[trailSize - 1]).getLinks().get(trail[0]).getCost();
		for (int i = 0; i < trailSize - 1; i++) {
			length += graphs.get(trail[i]).getLinks().get(trail[i + 1]).getCost();
		}
		return length;
	}

	protected void clear() {
		for (int i = 0; i < trailSize; i++){
			visited[i] = false;
			this.switchs = new ArrayList<>();
		}
	}

}