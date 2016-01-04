package cdi.custom.scope;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Validator extends BaseValidator {

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	public boolean validate(Context ctx){
		List<Child> services = ctx.getP().getServices();
		services.stream().forEach(x -> L.info("services name in validator:"+x.getType()));
		return true;
	}

	@Override
	public String toString() {
		return String.format("Validator []");
	}
	
}
