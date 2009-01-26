package lang;

//builtin methods for Bob
public class Builtin {
		
	public static BobObject invoke(String funcName, BobList args, Environment environ) throws BobException {
		BobObject returned = null;
		if (funcName.equals("print")) print(args, environ);
		else if (funcName.equals("puts")) puts(args, environ);
		else if (funcName.equals("quote")) returned = quote(args);
		else if (funcName.equals("car")) returned = car(args, environ);
		else if (funcName.equals("cdr")) returned = cdr(args, environ);
		else if (funcName.equals("list")) returned = list(args, environ);
		else if (funcName.equals("atom")) returned = atom(args, environ);
		else if (funcName.equals("eq")) returned = eq(args, environ);
		else if (funcName.equals("define")) define(args, environ);
		else if (funcName.equals("cons")) returned = cons(args, environ);
		else if (funcName.equals("cond")) returned = cond(args, environ);
		else if (funcName.equals("eval")) returned = eval(args, environ);
		
		//TODO: NEEDS DEBUG BAD
		else {
			BobObject func = Language.assoc(new BobToken(funcName), environ);
			if ( func instanceof BobFunction) {
				if ( args.size() == ((BobFunction) func).getArgs().size() ) {
					//important to enter new scope
					environ.enterNewScope();
					for (int x = 0; x < args.size(); x++) {
						//assoc local vars in new environ
						environ.define( ((BobFunction) func).getArgs().getValue().get(x).toString(),
								args.getValue().get(x));
					}
					//weird hack to make eval work, wants expression nested.
					BobList body = new BobList(new Expression());
					body.getValue().add((BobList) func.getValue());
					returned = eval( body, environ );
					//important to exit current scope
					environ.exitCurrentScope();
				}
				else throw new BobException("EVAL: WRONG NUMBER OF ARGS", Language.traceback);
			}
		}
		//else throw new RuntimeException("EVAL: Undefined function: " + funcName);
		return returned;
	}
	
	
	//checks if the correct amount of parameters have been passed.
	public static boolean paramsGood(int expected, int given) {
		if (expected == given) return true; 
		else{
			//TODO: change to something besides RuntimeException
			throw new RuntimeException("EVAL: bad params. Expected:" + expected + " Given:" + given);
		}
	}
	
	public static void print( BobList list, Environment environ ) throws BobException {
		for (BobObject elem : list.getValue() ) {
			System.out.print( Language.eval( elem, environ ) + " " );
		}	
	}
	
	public static void puts( BobList list, Environment environ ) throws BobException {
		print( list, environ );
		System.out.println();
	}
	
	public static void define( BobList toMake, Environment environ) throws BobException {
		//we don't want to throw an error if wrong args.
		if ( toMake.getValue().size() == 2 ) {
			if ( toMake.car() instanceof BobToken ) {
				environ.define( toMake.car().toString(), toMake.cdr().car() );
			}
		}
		//functions
		else if (paramsGood(3, toMake.getValue().size()) ) {
			if ( toMake.car() instanceof BobToken 
					&& toMake.cdr().car() instanceof BobList
					&& toMake.cdr().cdr().car() instanceof BobList ) {
				environ.define(toMake.car().toString(),
						new BobFunction(
								//args
								(BobList) toMake.cdr().car(),
								//body
								(BobList) toMake.cdr().cdr().car()
							)
					);
			}
			else throw new BobException("ERROR: MAKE: MALFORMED FUNCTION FORM", Language.traceback);
		}
		else throw new BobException("ERROR: MAKE: MALFORMED VARIABLE NAME", Language.traceback);
	}
	

	//SEVEN PRIMITIVE OPERATORS
	
	public static BobObject quote( BobList toQuote ) {
		if (paramsGood(1, toQuote.getValue().size() ) )
			return toQuote.car(); 
		else return null;
	}
	
	public static BobObject car( BobList toCar, Environment environ) throws BobException {
		BobObject returned = null;
		//take the car of it because apply returns results in list, so we want the list inside the list.
		BobObject evaled = ((BobList) Language.eval( toCar, environ)).car();
		if (evaled instanceof BobList ) {
			returned = ((BobList) evaled).car();
		}
		else throw new RuntimeException("EVAL: ERROR: PARAM FOR CAR IS NOT LIST");
		return returned; 
	}
	
	public static BobObject cdr( BobList toCdr, Environment environ ) throws BobException {
		BobObject returned = null;
		//take the car of it because apply returns results in list, so we want the list inside the list.
		BobObject evaled = ( (BobList) Language.eval( toCdr, environ)).car();
		if (evaled instanceof BobList ) {
			returned = ((BobList) evaled).cdr();
		}
		else throw new RuntimeException("EVAL: ERROR: PARAM FOR CDR IS NOT LIST");
		return returned; 
	}
	
	public static BobObject list( BobList toList, Environment environ ) {
		//toList.setEvaluated(false);
		return toList;
	}
	
	public static BobObject atom( BobList isAtom, Environment environ ) throws BobException {
		if ( Language.eval( isAtom.car(), environ) instanceof Atom) {
			return Constant.TRUE;
		}
		else return Constant.FALSE;
	}
	
	public static BobObject eq(BobList areEqual, Environment environ ) throws BobException {
		if (paramsGood(2, areEqual.getValue().size() ))
			if ( Language.eval( areEqual.car(), environ )
					.getValue().equals( 
							Language.eval( areEqual.cdr().car(), environ ).getValue() 
							) ) {
				return Constant.TRUE;
			}
			else return Constant.FALSE;
		return null;
	}
	
	public static BobObject cons(BobList toCons, Environment environ) throws BobException {
		Expression newList = new Expression();
		if (paramsGood(2, toCons.getValue().size() ) ) {
			if (toCons.cdr().car() instanceof BobList ) {
				newList.add(Language.eval(toCons.car(), environ));
				newList.addAll((Expression) ((BobList) Language.eval(toCons, environ))
						.cdr().car().getValue());
			}
			else throw new BobException("ERROR: BAD ARG. SECOND ARG MUST OF TYPE 'LIST'", Language.traceback);
		}
		return new BobList(newList);
	}
	
	public static BobObject cond(BobList args, Environment environ ) throws BobException {
		BobObject returned = null;
		//TODO: NEEDS DEBUGGING BADLY
		for (BobObject elem : args.getValue()) {
			if (elem instanceof BobList && paramsGood(2, ((BobList) elem).getValue().size())) {
				//if 0th elem evals to true...
				if (Language.eval(((BobList)elem).car(), environ)
						.toString().equals(Constant.TRUE.toString())) {
					//...return the eval of the 1st elem.
					returned = Language.eval(((BobList)elem).cdr().car(), environ);	
					break;
				}
			}
			else throw new RuntimeException("ERROR: COND: MALFORMED EXPRESSION");
		}
		return returned;
	}
	
	public static BobObject eval(BobList args, Environment environ ) throws BobException {
		BobObject returned = null;
		System.out.println(args);
		if (paramsGood(1, args.getValue().size() ) ) {
			if (args.car() instanceof BobToken) {
				//once for grabbing the var value, once for evaling it 
				returned = Language.eval( Language.eval(args.car(), environ), environ );
			}
			else returned = Language.eval(args.car(), environ);
		}
		return returned;
	}
}
