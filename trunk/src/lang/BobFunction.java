package lang;

public class BobFunction extends BobObject<BobList> {

	private String type;
	private BobList args;
	private boolean hasArgs;
	
	//if no args
	public BobFunction( BobList body ) {
		this(null, body);
	}
	
	//with args
	public BobFunction( BobList args, BobList body ) {
		super("<func>", body );
		this.args = args;
	}
	
	public BobList getArgs() {
		return args;
	}
	
	
}
