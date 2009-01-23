package lang;

public class BobNumber extends BobObject<Double> implements Atom {
	
	public BobNumber( double value ) {
		super("<num>", value );
	}
	
}
