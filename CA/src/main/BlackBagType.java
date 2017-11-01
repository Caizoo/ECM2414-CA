 package main;

public enum BlackBagType {

	X(0),Y(1),Z(2);
	int index;
	BlackBagType(int index) {
		this.index = index;
	}
	
	public int getIndex() { return this.index; }
	public static BlackBagType getType(int i) {
		if(i==0) {
			return BlackBagType.X;
		}else if(i==1) {
			return BlackBagType.Y;
		}else{
			return BlackBagType.Z;
		}
	}
	
}
