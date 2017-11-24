package ant_colony;

import java.util.ArrayList;

public class Graph {
	protected Switch sw;
	protected ArrayList<Link> links;
	
	public Graph(Switch sw, ArrayList<Link> links) {
		this.sw = sw;
		this.links = links;
	}
	
	public Switch getSw() {
		return sw;
	}
	
	public ArrayList<Link> getLinks() {
		return links;
	}
}
