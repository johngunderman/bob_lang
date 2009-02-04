package lang;

public abstract class BobObject<T> {

	protected String type;
	private T value;
	private boolean isEvaluated;
	

	public BobObject(String type, T value) {
		this(type, value, true);
	}
	
	public BobObject(String type, T value, boolean isEvaluated) {
		this.type = type;
		this.value = value;
		this.isEvaluated = isEvaluated;
	}
	
	
	public String getType() {
		return type;
	}
	
	public T getValue() { return value; }
	
	public String toString() {
		return value.toString();
	}
	
	public boolean isEvaluated() {
		return isEvaluated;
	}

	public void setEvaluated(boolean isEvaluated) {
		this.isEvaluated = isEvaluated;
	}
	
}
