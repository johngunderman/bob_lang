package lang;

public class BobList extends BobObject<Expression> {
	
	public BobList( Expression value ) {
		super("<list>", value );
	}
	
	public BobList(Expression value, boolean isEvaluated) {
		super("<list>", value, isEvaluated);
	}

	//returns atom of first element in expression
	public BobObject car() {
		if (this.getValue().size() > 0) 
			return this.getValue().get(0);
		else return new BobToken("f");
		
	}
	

	
	//returns remainder of expression as type 'list'
	public BobList cdr() {
		if (this.getValue().size() > 1) 
			return new BobList( Expression.listToExpression( this.getValue().subList(1, size() ) ) );
		else return new BobList(new Expression(), false );
	}
	
	public String toString() {
		String returned = new String("(");
		for (BobObject elem : this.getValue() ) {
			returned += " " + elem.toString();
		}
		return returned + " )";
	}
	
	public String prettyPrinter() {
		String returned = new String("");
		for (BobObject elem : this.getValue() ) {
			returned += " " + elem.getType();
		}
		return returned;
	}
	
	
	public int size() {
		return getValue().size();
	}
	
	//returns total # of tokens in this 'branch' / list
	//used in parser
	public int totalSize() {
		//the +1 accounts for parenthesis that are not included in the list itself.
		int returned = this.getValue().size() + 1;
		
		for (BobObject elem : this.getValue() ) {
			if (elem instanceof BobList ) {
				returned += ((BobList) elem).totalSize();
			}
		}
		return returned;
	}
	
}
