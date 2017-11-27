package ant_colony;

public class Link {
	protected Switch src;
	protected Switch dst;
	protected double cost;
	
	public Link(Switch src, Switch dst, double cost) {
		this.src = src;
		this.dst = dst;
		this.cost = cost;
	}
//	public Link(Switch src, Switch dst) {
//		this.src = src;
//		this.dst = dst;
//	}
	
	public Switch getSrc() {
		return src;
	}
	
	public Switch getDst() {
		return dst;
	}
	
	public double getCost() {
		return cost;
	}
	
	public boolean hasCost() {
		if (cost >= 0) {
			return true;
		} else {
			return false;
		}
	}
}
