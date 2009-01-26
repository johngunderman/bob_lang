package lang;

import java.util.Stack;

public class Language {

	public static Stack<String> traceback = new Stack<String>();
	
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


}
