package cdi.custom.scope.stuff;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@FooScope
public class Bar {
	
	@PostConstruct
	public void init() {
		System.out.println("Bar INIT"); 
	}
	
	@PreDestroy
	public void destroyPost() {
		System.out.println("Bar DESTROY"); 
	}
}
