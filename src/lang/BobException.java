package lang;

import java.util.Stack;

public class BobException extends Exception {
	
	public BobException( String message, Stack<String> traceback) {
		System.out.println(message);
		System.out.println("Traceback:");
		for (int x = 0; x < traceback.size() - 1; x++) {
				System.out.println( "at:  " + traceback.pop());
		}
		if (Language.exitOnError) {
			System.exit(1);
		}
	}
	
}
