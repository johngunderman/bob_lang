package lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

import parser.Parser;

public class Language {

	public static Stack<String> traceback = new Stack<String>();
	public static Environment replEnviron = new Environment();
	
	public static BobObject eval( BobObject<?> expr, Environment environment ) throws BobException {
		traceback.push(expr.toString());
		BobObject<?> returned = null;
		
		if (expr instanceof Atom ) {
			returned = assoc( expr, environment );
		}
		else if (expr instanceof BobList) {
			
			
			if ( ((BobList) expr).car() instanceof BobToken ) {

				returned = Builtin.invoke(  ((BobToken) ((BobList) expr).car()).getValue(), //car string value
						((BobList) expr).cdr(),  //cdr
						environment //pass the environment
				);
			}
			//if we have nested expression, grab it.
			else if ( ((BobList) expr).car() instanceof BobList ) {
				returned = apply( (BobList) expr, environment );
			}
			
			
			else throw new BobException("ERROR: BAD EXPRESSION: " + ((BobList) expr).car(), traceback);
		}
		else throw new BobException("ERROR: WTF WTF WTF?????? THE SQUIRRELS MUST HAVE BEEN AT IT AGAIN...",
					traceback);
		traceback.pop();
		return returned;
	}
		
			
	
	public static BobList apply(BobList expr, Environment environment) throws BobException {
		BobList returned = new BobList(new Expression());
		
		for (BobObject elem : expr.getValue() ) {
			returned.getValue().add( eval(elem, environment) );
		}
		return returned;	
	}
	
	
	public static BobObject assoc( BobObject expr , Environment environment) throws BobException {
		BobObject returned = null;
		
		if (expr instanceof BobToken) {
			return environment.find( expr.toString() );
		}
		else { 
			returned = expr;
		}
		return returned;
	}

	public static void repl() throws BobException {
		String message = "\nWelcome to Bob (v0.1)! Please enjoy your stay and whatnot. Thanks!";
		String expr = new String ("");
		String line = new String("");
		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Parser parser = new Parser();
		int parenCount = 0;
		
		System.out.println(message);
		//  read the username from the command-line; need to use try/catch with the
		//  readLine() method
		while (true) {
			try {
				System.out.print("> ");
				line = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error in REPL. Uh oh!");
				System.exit(1);
			}
			expr += line;
			parenCount =  parenCount + getParenOpenCount(line) - getParenCloseCount(line);
			if (expr.equals("exit") || expr.equals("(exit)")) {
				System.out.println("Goodbye!");
				System.exit(0);
			}
			else if (parenCount == 0) {
				BobObject value = new BobToken("null");
				value = eval(parser.parse(new StringReader(expr)), replEnviron);
				//System.out.println("=> " + value.toString());
				expr = new String("");
			}
		}
	}
	
	private static int getParenOpenCount(String line) {
		line = "blob" + line + "blob";
		int count = line.split("[(]").length;
		return count;
	}
	
	private static int getParenCloseCount(String line) {
		line = "blob" + line + "blob";
		int count = line.split("[)]").length;
		return count;
	}
	
	public static void loadStdLib() {
		//load standard library
		InputStreamReader stdLib = null;
		InputStream temp = Class.class.getResourceAsStream("/lib/standard-lib.bob");
		stdLib = new InputStreamReader( temp );
		
		//stdlib
		parser.Parser std = new parser.Parser();
		BobList preLoad = std.parse( (Reader) stdLib);	
		
		try {
			Language.eval( preLoad, Language.replEnviron );
		} catch (BobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
