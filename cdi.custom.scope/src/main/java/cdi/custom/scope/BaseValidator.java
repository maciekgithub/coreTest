package cdi.custom.scope;


public abstract class BaseValidator {
	
	protected Context ctx;

	protected void init(Context ctx){
		System.out.println("Initializing Validator with ctx: "+ctx);
		this.ctx=ctx;
	}

}
