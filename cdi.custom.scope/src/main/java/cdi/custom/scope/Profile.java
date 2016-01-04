package cdi.custom.scope;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.stuff.Foo;


public class Profile {

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	SimpleEntityFacade sef;
	Foo f;
	int a, b;

	public Profile(SimpleEntityFacade sef,Foo f) {
		this.sef = sef;
		this.a = 10;
		this.b = 5;
		this.f = f;
	}

	@Override
	public String toString() {
		return String.format("Profile [sef=%s, a=%s, b=%s]", sef, a, b);
	}

	public void useFacade(long pk) {
		sef.entityManager.find(Child.class, pk);
	}

	public void useFacadeAndPersistService(String name) {
		sef.useFacadeAndPersistService(name);
	}

	public List<Child> getServices() {
		return sef.queryAll();
		
	}

}
