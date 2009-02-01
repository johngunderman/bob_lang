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
			if (((BobToken) expr).getValue().equals("current-time")) {
				return new BobNumber(System.nanoTime());
			}
			else return environment.find( expr.toString() );
		}
		else { 
			returned = expr;
		}
		return returned;
	}

	public static void repl() throws BobException {
		String message = "Welcome to Bob (v0.1)!\n" +
				"by John Gunderman (2009)" +
				" johngunderman@gmail.com\n" +
				"Please enjoy your stay and whatnot. Thanks!\n" +
				"Type 'about' for more info\n" +
				"Oh, and type 'help' for help. MEEP.";
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
			else if (expr.equals("help")) {
				System.out.println("Ha ha. Just kidding (sorry). I'm to lazy to make a\n" +
						"concise help file, so just read the tutorial. No laziness around\n" +
						"these parts, no sir. And STOP CRYING. Its not like I told you\n" +
						"that the apocalypse was drawing near. Oh, did I mention...\n" +
						"MEEP.");
				//System.exit(0);
			}
			if (expr.equals("about")) {
				System.out.println("All bow down and worship the greatness that is Bob.\n" +
						"The only thing greater than Bob is John (duh).\n" +
						"Oh, but as far as this whole 'about' thing goes...\n" +
						"Bob was started as a project for John (the Great)'s Comp-Sci\n" +
						"Class, under the wise mentorship of the one and only Mr. Lew.\n" +
						"And John spoketh, saying, \"Let there be light.\", and Bob\n" +
						"sprang forth into existance, fully formed, from John's mind.\n" +
						"And John saw what he had made, and saw that it was good.\n" +
						"And John said, \"Bob is like lisp, but cooler, because I made it.\"\n" +
						"But then John began to read SICP, and soon realized that Scheme was\n" +
						"almost more godly than he (and definitely more so than Bob). But that\n" +
						"did not bother John, for he knew that Bob was just a project. And John\n" +
						"saw the flaws in Bob, because Bob was written in Java, and Java was not\n" +
						"Scheme, or Python, or C. But he sighed and continued. And the wisdom and\n" +
						"might that is John, proudly presents to mortals, that which is Bob.\n" +
						"Enjoy. MEEP.");
				//System.exit(0);
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
