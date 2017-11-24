package ant_colony;

public class Switch {
	protected String sw;
	protected int pos;
	
	public Switch(String sw, int pos) {
		this.sw = sw;
		this.pos = pos;
	}
	
	public String getSw() {
		return sw;
	}
	
	public int getPos() {
		return pos;
	}
	
	public boolean isEqualSw(Switch swich) {
		if (sw.equals(swich.getSw()) && pos == swich.getPos()) {
			return true;
		} else {
			return false;
		}
	}
}
