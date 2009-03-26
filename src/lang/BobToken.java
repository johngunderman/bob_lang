package lang;

public class BobToken extends BobObject<String> implements Atom {
	
	public BobToken( String value ) {
		super("<token>", value );
	}
	
	public BobToken(String value, boolean isEvaled) {
		// TODO Auto-generated constructor stub
		super("<token>", value, isEvaled);
	}

	public String toString() {
		return getValue();
	}
}
