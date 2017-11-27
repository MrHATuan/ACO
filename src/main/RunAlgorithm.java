package main;

//import ant_colony.AntColonyOptimization;
import ant_colony.AntColony;

public class RunAlgorithm {

	public static void main(String[] args) {
//		AntColonyOptimization antColony = new AntColonyOptimization(21);
//		antColony.startAntOptimization();
		
		AntColony antColony = new AntColony(10);
		antColony.startACO();
	}

}
