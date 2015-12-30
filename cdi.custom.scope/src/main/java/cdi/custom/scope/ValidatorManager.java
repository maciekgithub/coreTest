package cdi.custom.scope;

import javax.inject.Inject;

public class ValidatorManager {

	@Inject
	private ValidatorFactory vf;

	public Validator validate(Context ctx) {

		Validator validator = vf.createValidator(ctx);
		System.out.println(String.format("validator created - %s ", validator));
		
		validator.validate(ctx);
		return validator;
	}

}
