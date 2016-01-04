package cdi.custom.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context {

	private Profile p;

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
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
