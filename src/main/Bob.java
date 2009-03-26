package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import lang.*;

public class Bob {

	/**
	 * @param args
	 * @throws BobException 
	 */
	public static void main(String[] args) throws BobException {
		// TODO Auto-generated method stub
		
		Language.loadStdLib();
		
		
		if (args.length == 0) {
			startREPL();
		}
		else if (args[0].equals("--test")) {
			test();
		}
		else if (args [0].equals("-i")) {
			startREPL();
		}
		else if (args[0].equals("-f") || args [0].equals("--file")) {
			if (args.length == 2) {
				loadFile(args[1]);
			}
			else {
				System.out.println("ERROR: '" + args[0] + "' requires file path as argument.");
				System.exit(1);
			}
		}
		else if (args[0].equals("-h") || args[0].equals("--help")) {
			System.out.println("Thank you for accessing Bob-help.\n" +
					"A list of Bob command line arguments follows:\n\n" +
					"  --test\tRun a test Bob source file and exit.\n" +
					"\t\tPrimarily for debugging.\n" +
					"  -i\t\tRun Bob in interactive prompt mode.\n" +
					"\t\tThis is the defaoult mode if no arguments are given.\n" +
					"  -f, --file\tLoad a Bob source file from the system and exit.\n"
					);
		}
		else {
			System.out.println("ERROR: no option found for '" + args[0] + "'");
			System.exit(1);
		}
		
	}
	
	public static void startREPL() {
		try {
			Language.repl();
		} catch (BobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test() {
		//load test file src.bob
		InputStreamReader r = null;
		InputStream temp = Class.class.getResourceAsStream("/lib/src.bob");
		r = new InputStreamReader(temp);	
		
		parser.Parser p = new parser.Parser();
		BobList tree = p.parse( (Reader) r);
		
		
		try {
			Language.eval( tree, Language.replEnviron );
		} catch (BobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadFile(String filename) throws BobException {
		try {
			FileReader fr = new FileReader(new File( filename ));
			parser.Parser p = new parser.Parser();
			BobList tree = p.parse( fr );
			Language.eval( tree, Language.replEnviron);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File could not be found. Check to see that the file exists.");
			System.exit(1);
		}
	}

}
