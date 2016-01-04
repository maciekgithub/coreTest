package cdi.custom.scope;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidatorFactory {

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@Any
	@Inject
	Instance<Validator> validatorSource;
	
	public Validator createValidator(Context ctx){
		Validator validator = validatorSource.get();
		L.info(String.format("Validator source: %s - creating validator with reference %s and instantly initializing it.", validatorSource,validator));
		validator.init(ctx);
		return validator;
	}
	
}
