package main;

public enum WhiteBagType {

	A(0),B(1),C(2);
	int index;
	WhiteBagType(int index) {
		this.index = index;
	}
	
	public int getIndex() { return this.index; }
	public static WhiteBagType getType(int i) {
		if(i==0) {
			return WhiteBagType.A;
		}else if(i==1) {
			return WhiteBagType.B;
		}else{
			return WhiteBagType.C;
		}
	}
	
}
