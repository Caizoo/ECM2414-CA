package exceptions;

public class BagUnderflowException extends Exception {
	
	public BagUnderflowException() { super("Trying to take from empty bag"); }

}
