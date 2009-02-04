package lang;

import java.util.ArrayList;
import java.util.List;

public class Expression extends ArrayList<BobObject> {
	
	public Expression(Expression value) {
		super(value);
	}
	
	public Expression() {
		super();
	}
	
	//converts from List to Expression.
	//needed because .subList() returns list, and java no like casting for it.
	public static Expression listToExpression( List<BobObject> stuff ) {
		Expression returned = new Expression();
		
		for ( int x = 0; x < stuff.size(); x++ ) {
			returned.add( stuff.get(x) );
		}
		
		return returned;
	}
	
	public boolean equals(Object toCompare) {
		boolean returned = false;
		Expression other;
		if (toCompare instanceof Expression) {
			other = (Expression) toCompare;
			if (this.size() == other.size()){
				for (int x = 0; x < this.size(); x++){
					if (this.get(x).getValue().equals(other.get(x).getValue()) ) {
						returned = true;
					}
					else {
						returned = false;
						break;
					}
				}
			}
		}
		return returned;
	}
	
	
}
