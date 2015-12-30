package cdi.custom.scope.previous.version;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class FacadeTwo {

	@Inject
	private EntityManager em;

	@Override
	public String toString() {
		return String.format("FacadeTwo [em=%s]", em);
	}
	
}
