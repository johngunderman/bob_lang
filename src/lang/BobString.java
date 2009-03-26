package lang;

public class BobString extends BobObject<String> implements Atom {
	
	public BobString( String value ) {
		super("<str>", value );
	}
	
	public String toString() {
		return "\"" + getValue() + "\"";
		//return getValue();
	}
	
}
