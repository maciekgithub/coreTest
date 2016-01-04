package cdi.custom.scope.stuff;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FooScope
public class Bar {

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@PostConstruct
	public void init() {
		L.info("Bar INIT"); 
	}
	
	@PreDestroy
	public void destroyPost() {
		L.info("Bar DESTROY"); 
	}
}
