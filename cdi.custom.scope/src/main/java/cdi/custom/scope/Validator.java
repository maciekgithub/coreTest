package cdi.custom.scope;

import java.util.List;

import pl.orange.isep.model.service.Service;


public class Validator extends BaseValidator {

	public boolean validate(Context ctx){
		List<Service> services = ctx.getP().getServices();
		services.stream().forEach(x -> System.out.println("services name in validator:"+x.getName()));
		return true;
	}

	@Override
	public String toString() {
		return String.format("Validator []");
	}
	
}
