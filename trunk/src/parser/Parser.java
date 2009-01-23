package parser;

import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.StringReader;
import lang.*;
import java.util.HashMap;


public class Parser {

	//escaped tokens.
	private HashMap<String,String> escTokens = new HashMap<String,String>(); 

	public Parser() {
		escTokens.put("+", "sum");
		escTokens.put("-", "subtract");
		escTokens.put("/", "divide");
		escTokens.put("*", "multiply");
		escTokens.put("(", "$BEGIN");
		escTokens.put(")", "$END");
		escTokens.put("=", "equal");
	}
	
	public BobList parse( Reader sr ) {
		Expression tokens = escape( tokenize(sr) );
		//System.out.println( tokens );
		BobList tree = makeTree( tokens );
		//System.out.println( tree.prettyPrinter() );
		tree = expandQuotes( tree );
		//System.out.println( tree.prettyPrinter() );
		return tree;
	}
	
	//turns String into array of tokens.
	private Expression tokenize(Reader sr) {
		StreamTokenizer st = new StreamTokenizer(sr);
		Expression exp = new Expression();
		//set ; (semicolon) as the character to begin one line comments
		st.commentChar(59);
		
		//tell it to parse numbers
		st.parseNumbers();
		
		//ignore end of lines
		st.eolIsSignificant(false);
		
		//make / an ordinary char
		st.ordinaryChar(47);
		//make ' an ordinary char
		st.ordinaryChar(39);
			
	    try {
			while(st.nextToken() != StreamTokenizer.TT_EOF) {
				//34 is the ascii code for quotation marks. 
				//it will be the type if the token is a quoted string
				if ( st.ttype == 34 ) {
					exp.add(new BobString( st.sval ) );	
				}
				//if type is token, not quoted.
				else if (st.ttype == st.TT_WORD ) {
					exp.add(new BobToken( st.sval ) );
				}
				else if (st.ttype == st.TT_NUMBER ) {
					exp.add(new BobNumber( st.nval ) );
				}
				//grab single char tokens
				else { 
					exp.add( new BobToken( String.valueOf( (char) st.ttype ) ) ); 
				}//end else
				
			}//end while
	    }
		catch (IOException e) { System.out.println("Next token could not be read."); }

		return exp;
	} // end method tokenize
	
	

	//replaces syntax tokens (eg. '+') with lang accepted tokens: 'sum'  
	private Expression escape( Expression exp ) {
		for ( int x = 0; x < exp.size(); x++ ) {
			if ( exp.get(x) instanceof BobToken ) {
				if (escTokens.containsKey( exp.get(x).toString() ) ){
					exp.set( x, new BobToken( escTokens.get( exp.get(x).toString() ) ) );
				}
			}
		} // end for
		return exp;
	}

	private BobList expandQuotes( BobList tree ) {
		//only works when passed built trees, not just a token expression
		Expression tokens = tree.getValue();
		for (int x = 0; x < tokens.size(); x++) {
			if ( tokens.get(x) instanceof BobToken) {
				if ( tokens.get(x).toString().equals("'") ) {
					//create another nesting inside a quote expression
					Expression quoted = new Expression();
					quoted.add( new BobToken("quote") );
					quoted.add( tokens.get(x+1) );
					tokens.remove(x+1);
					tokens.set(x, new BobList(quoted) );
				}
			}
			if (tokens.get(x) instanceof BobList) {
				BobList nextBranch =  (BobList) tokens.get(x);
				//recurse
				tokens.set(x, expandQuotes( nextBranch ) );
			}
		}
		return new BobList( tokens );
	}
	
	private BobList makeTree( Expression tokens ) {
		//TODO checks to make sure we're not skipping anything
		
		//hack to get makeBranch to not exit early because of separate complete
		//statements which would otherwise drop the second tree. basically merges
		//both trees.
		tokens.add(0, new BobToken("$BEGIN") );
		tokens.add(new BobToken( "$END") );
		//debug
		//System.out.println( tokens );
		//do the heavy lifting:
		return makeBranch( tokens );
	} // end method makeTree
	
	//recursively creates branches
	private BobList makeBranch( Expression tokens ) {
		//TODO: raise an error if not enough parenthesis
		Expression tree = new Expression();
		int x = 0;
		//while we are still in this nest, and while there are still tokens left.
		while ( ( x < tokens.size() ) && !( tokens.get(x).toString().equals("$END") && tokens.get(x) instanceof BobToken ) && x < tokens.size() ) {
			//if a syntax token...
			if ( tokens.get(x) instanceof BobToken ) {
				
				//are we beginning a new nested nest?
				if (tokens.get(x).toString().equals("$BEGIN") ) {
					//recursively call the function
					BobList branch = makeBranch( Expression.listToExpression( tokens.subList(x + 1, tokens.size() ) ) );
					//make sure we compensate for the already parsed tokens
					x += branch.totalSize();
					tree.add( branch );
				}		
				else {
					tree.add( tokens.get(x) );
				}				
			}
			//if regular token
			else {
				tree.add( tokens.get(x) );
			}
			x++;
		}
		return new BobList( tree );
	}// end method makeBranch
}