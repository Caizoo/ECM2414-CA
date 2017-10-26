 package main;

public enum BlackBagType {

	X(0),Y(1),Z(2);
	int index;
	BlackBagType(int index) {
		this.index = index;
	}
	
	public int getIndex() { return this.index; }
	
}
