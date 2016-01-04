package cdi.custom.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseValidator {
	
	protected Context ctx;

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	protected void init(Context ctx){
		L.info("Initializing Validator with ctx: "+ctx);
		this.ctx=ctx;
	}

}
