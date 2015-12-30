package cdi.custom.scope;

import java.util.List;

import pl.orange.isep.model.service.Service;

public class Profile {

	SimpleEntityFacade sef;
	int a, b;

	public Profile(SimpleEntityFacade sef) {
		this.sef = sef;
		this.a = 10;
		this.b = 5;
	}

	@Override
	public String toString() {
		return String.format("Profile [sef=%s, a=%s, b=%s]", sef, a, b);
	}

	public void useFacade(long pk) {
		sef.getEntityManager().find(Service.class, pk);
	}

	public void useFacadeAndPersistService(String name) {
		sef.useFacadeAndPersistService(name);
	}

	public List<Service> getServices() {
		return sef.queryAll();
		
	}

}
