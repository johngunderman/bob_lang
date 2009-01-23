package lang;

import java.util.Stack;

public class Language {

	public static Stack<String> traceback = new Stack<String>();
	
	public static BobObject eval( BobObject<?> expr, BobList environment ) throws BobException {
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
		
			
	
	public static BobList apply(BobList expr, BobList environment) throws BobException {
		BobList returned = new BobList(new Expression());
		
		for (BobObject elem : expr.getValue() ) {
			returned.getValue().add( eval(elem, environment) );
		}
		return returned;	
	}
	
	
	public static BobObject assoc( BobObject expr , BobList environment) throws BobException {
		BobObject returned = null;
		Boolean found = false;
		
		if (expr instanceof BobToken) {
			for (BobObject pair : environment.getValue() ) {
				//if expr = car, replace with cadr
				if ( ((BobList) pair).getValue().get(0).toString().equals(expr.getValue().toString()) ) {
					returned = ((BobList) pair).getValue().get(1);
					found = true;
					break;
				}
			}
			if (!found) throw new BobException("ERROR: UNDEFINED VAR (in assoc): " + expr, traceback);		
		}
		else { 
			returned = expr;
		}
		return returned;
	}


}
