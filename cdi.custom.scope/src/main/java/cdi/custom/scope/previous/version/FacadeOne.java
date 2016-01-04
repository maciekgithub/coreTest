package cdi.custom.scope.previous.version;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cdi.custom.scope.Child;
import cdi.custom.scope.TransactionContainer;

@RequestScoped
public class FacadeOne {
	
	@Inject
	private EntityManager em;

	@Inject
	private TransactionContainer tc;
	
//	@Override
//	public String toString() {
//		return String.format("FacadeOne [em=%s,], %s", getEm(),this);
//	}
	
	
	public Child useFacadeAndPersistService(String name) {

		Child s = null;
		try {
			s = new Child();
			s.setType(name);
			System.out.println("FacadeOne TRX STATUS "+tc.getTsr().getTransactionStatus());
			getEm().joinTransaction();
			System.out.println("FacadeOne IS JOINED IN PERSIST "+getEm().isJoinedToTransaction());
			getEm().persist(s);
		} catch (Exception e) {
			System.out.println(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
		}
		return s;
	}
	
	
	public List<Child> queryAll() {
		TypedQuery<Child> q = getEm().createQuery("select s from Child s", Child.class);
		System.out.println("FacadeOne IS JOINED IN QUERY "+getEm().isJoinedToTransaction());
		List<Child> resultList = q.getResultList();
		resultList.stream().forEach(x -> System.out.println(String.format("FacadeOne Entity: %s", x.getType())));
		System.out.println(String.format("FacadeOne All entities in FacadeOne DB size: %s ", resultList.size()));
		return resultList;
	}


	public EntityManager getEm() {
		return em;
	}


	public void setEm(EntityManager em) {
		this.em = em;
	}
}
