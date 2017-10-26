package main;

public enum WhiteBagType {

	A(0),B(1),C(2);
	int index;
	WhiteBagType(int index) {
		this.index = index;
	}
	
	public int getIndex() { return this.index; }
	
}
