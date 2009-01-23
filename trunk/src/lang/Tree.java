package lang;

import java.util.ArrayList;

public class Tree {

	private ArrayList<Expression> tree = new ArrayList<Expression>();
	private int index;
	
	public Tree(ArrayList<Expression> t ) {
		tree = t;
		index = 0;
	}
	
	public Expression getNextExpression() {
		Expression returned = tree.get(index);
		index++;
		return returned;
		
	}
	
	//public Expression getNested() {
	//	
	//}
	
}
