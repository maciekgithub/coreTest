package cdi.custom.scope;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class ProfileBuiler {
	
	@Inject
	private SimpleEntityFacade sef;
	
	public Profile buildProfile(){
		return new Profile(sef); 
	}

}
