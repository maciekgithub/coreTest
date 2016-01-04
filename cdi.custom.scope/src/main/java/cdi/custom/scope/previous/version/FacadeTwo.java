package cdi.custom.scope.previous.version;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cdi.custom.scope.Child;
import cdi.custom.scope.SimpleEntityFacade;
@RequestScoped
public class FacadeTwo {

	@Inject
	private EntityManager em;

//	@Inject
//	private SimpleEntityFacade sef;
	
	@Inject
	FacadeOne fo;
	
//	@Override
//	public String toString() {
//		return String.format("FacadeOne [em=%s,], %s", em, this);
//	}
//	org.apache.derby.jdbc.EmbeddedDataSource
//	org.apache.derby.jdbc.ClientDataSource
	public List<Child> queryAll() {
		System.out.println("FacadeOne injected into FacadeTwo "+fo);
		em.joinTransaction();
		TypedQuery<Child> q = em.createQuery("select s from Child s", Child.class);
		System.out.println("FacadeTwo IS JOINED IN QUERY "+em.isJoinedToTransaction());
		List<Child> resultList = q.getResultList();
		resultList.stream().forEach(x -> System.out.println(String.format("FacadeTwo Entity: %s", x.getType())));
		System.out.println(String.format("FacadeTwo - All entities in FacadeTwo DB size: %s ", resultList.size()));
		return resultList;
	}
	
	
//	public void write(String name){
//		sef.useFacadeAndPersistService(name);
//	}
	
	
}
