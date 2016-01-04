package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import cdi.custom.scope.stuff.Foo;
import cdi.custom.scope.stuff.FooInstance;

@RequestScoped
public class ProfileBuiler {
	
	@Inject
	private SimpleEntityFacade sef;
	
	@Inject
//	@FooInstance
	Foo foo;
	
	public Profile buildProfile(){
		return new Profile(sef, foo); 
	}
	
	@PostConstruct
	public void info(){
		System.out.println("ProfileBuiler constructed "+this.foo);
	}
	
	@PreDestroy
	public void outfo(){
		System.out.println("ProfileBuiler destructed "+this.foo);
	}

}
