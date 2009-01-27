package lang;

import java.util.HashMap;
import java.util.Stack;

public class Environment {

	//holds the hashmap scopes in an array list.
	private Stack<HashMap<String, BobObject<?>>> scopeHolder; 
	//the default scope
	private HashMap<String, BobObject<?>> globals; 
	
	public Environment() {
		scopeHolder = new Stack<HashMap<String, BobObject<?>>>();
		globals = new HashMap<String, BobObject<?>>();
		
		//push our global scope onto the stack.
		scopeHolder.push(globals);
	}
	
	public void enterNewScope() {
		scopeHolder.push( new HashMap<String, BobObject<?>>() );
	}
	
	public void exitCurrentScope() {
		scopeHolder.pop();
	}
	
	public void define(String key, BobObject<?> value) {
		scopeHolder.peek().put(key, value);
	}
	
	public BobObject<?> find(String key) throws BobException {
		BobObject<?> var = null;
		for (int x = scopeHolder.size() - 1; x >= 0; x--) {
			var = scopeHolder.get(x).get(key);
			if (var != null) {
				return var;
			}
		}
		throw new BobException("ERROR: no value for token: " + key, Language.traceback);
	}
	
	public String toString() {
		String returned = "";
		for (HashMap<String, BobObject<?>> scope : scopeHolder) {
			returned += scope.toString();
		}
		return returned;
	}
	
}
