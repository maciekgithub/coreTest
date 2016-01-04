package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.stuff.Foo;
import cdi.custom.scope.stuff.FooInstance;

@RequestScoped
public class ProfileBuiler {
	
	@Inject
	private SimpleEntityFacade sef;
	
	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@Inject
//	@FooInstance
	Foo foo;
	
	public Profile buildProfile(){
		return new Profile(sef, foo); 
	}
	
	@PostConstruct
	public void info(){
		L.info("ProfileBuiler constructed "+this.foo);
	}
	
	@PreDestroy
	public void outfo(){
		L.info("ProfileBuiler destructed "+this.foo);
	}

}
