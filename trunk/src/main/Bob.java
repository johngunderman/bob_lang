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
		
		
		//load standard library
		InputStreamReader stdLib = null;
		InputStream temp = Class.class.getResourceAsStream("/lib/standard-lib.bob");
		stdLib = new InputStreamReader( temp );
		
		
		//load test file src.bob
		InputStreamReader r = null;
		InputStream temp2 = Class.class.getResourceAsStream("/lib/src.bob");
		r = new InputStreamReader(temp2);
		
		//stdlib
		parser.Parser std = new parser.Parser();
		BobList preLoad = std.parse( (Reader) stdLib);		
		
		parser.Parser p = new parser.Parser();
		BobList tree = p.parse( (Reader) r);
		
		//debug
		//System.out.println(tree);
		
		Environment environ = new Environment();
		
		try {
			Language.eval( preLoad, environ );
		} catch (BobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(environ);
		try {
			Language.eval( tree, environ );
		} catch (BobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Language.repl();
		} catch (BobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
