package cdi.custom.scope;

import java.util.List;



public class Validator extends BaseValidator {

	public boolean validate(Context ctx){
		List<Child> services = ctx.getP().getServices();
		services.stream().forEach(x -> System.out.println("services name in validator:"+x.getType()));
		return true;
	}

	@Override
	public String toString() {
		return String.format("Validator []");
	}
	
}
