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
	 */
	public static void main(String[] args) {
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
		else System.exit(1);
		
		
		

		
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

}
