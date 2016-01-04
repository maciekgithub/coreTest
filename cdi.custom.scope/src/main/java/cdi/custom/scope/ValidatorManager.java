package cdi.custom.scope;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorManager {

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@Inject
	private ValidatorFactory vf;

	public Validator validate(Context ctx) {

		Validator validator = vf.createValidator(ctx);
		L.info(String.format("validator created - %s ", validator));
		
		validator.validate(ctx);
		return validator;
	}

}
