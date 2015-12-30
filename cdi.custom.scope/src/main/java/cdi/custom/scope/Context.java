package cdi.custom.scope;

public class Context {

	private Profile p;

	public Context(Profile p) {
		this.setP(p);
	}

	@Override
	public String toString() {
		return String.format("Context [p=%s]", getP());
	}

	public Profile getP() {
		return p;
	}

	public void setP(Profile p) {
		this.p = p;
	}

}
