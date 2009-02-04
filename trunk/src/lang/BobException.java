package lang;

import java.util.Stack;

public class BobException extends Exception {

	
	public BobException( String message, Stack<String> traceback) {
		this(message, traceback, true);
	}
	
	public BobException( String message, Stack<String> traceback, boolean exit ) {
		System.out.println(message);
		System.out.println("Traceback:");
		for (int x = 0; x < traceback.size(); x++) {
				System.out.println( "at:  " + traceback.pop());
		}
		if (exit) {
			System.exit(1);
		}
	}
	
}
