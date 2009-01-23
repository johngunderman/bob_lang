package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import lang.*;

public class Bob {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//load standard library
		FileReader stdLib = null;
		try {
			stdLib = new FileReader( new File("./standard-lib.bob"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//load test file src.bob
		FileReader r = null;
		try {
			r = new FileReader( new File("./src.bob") );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//stdlib
		parser.Parser std = new parser.Parser();
		BobList preLoad = std.parse( (Reader) stdLib);

		//TODO START HERE NEXT TIME
		
		
		parser.Parser p = new parser.Parser();
		BobList tree = p.parse( (Reader) r);
		
		//debug
		//System.out.println(tree);
		
		BobList environ = new BobList(new Expression());
		
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
		
	}

}
