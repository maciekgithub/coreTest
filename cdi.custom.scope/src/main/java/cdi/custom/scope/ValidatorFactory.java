package cdi.custom.scope;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;


public class ValidatorFactory {

	@Any
	@Inject
	Instance<Validator> validatorSource;
	
	public Validator createValidator(Context ctx){
		Validator validator = validatorSource.get();
		System.out.println(String.format("Validator source: %s - creating validator with reference %s and instantly initializing it.", validatorSource,validator));
		validator.init(ctx);
		return validator;
	}
	
}
