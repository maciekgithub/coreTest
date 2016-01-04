package cdi.custom.scope;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class SimpleEntityFacade {

	@Any
	@Inject
	private Instance<EntityManagerWrapper> entityManagerWrapperSource;

	private EntityManagerWrapper entityManagerWrapper;

	@Inject
	private EntityManager readOnlyEntityManager;

	EntityManager entityManager;

	@Inject
	private TransactionContainer tc;

	public EntityManager getEntityManager() {

		try {
			entityManagerWrapper = entityManagerWrapperSource.get();
			entityManager = entityManagerWrapper.getEntityManager();
			System.out.println(String.format("Obtained sucessfully transactional EM %s ", entityManager));
		} catch (ContextNotActiveException e) {
			entityManager =
				readOnlyEntityManager;
			System.out.println(String.format("Obtained RO EM because exception came out %s", readOnlyEntityManager));
		}
		return entityManager;
	}

	@Override
	public String toString() {
		return String.format("SimpleEntityFacade [entityManagerWrapperSource=%s, readOnlyEntityManager=%s, entityManager=%s]",
			entityManagerWrapperSource, readOnlyEntityManager, entityManager);
	}

	public Child useFacadeAndPersistService(String name) {

		Child s = null;
		try {
			//			tc.getUtx().begin();
			System.out.println("Will read all entities");
			System.out.println(String.format("Transaction status %s", tc.getTsr().getTransactionStatus()));

//			EntityManager entityManager3 = getEntityManager();
//			EntityManager entityManager4 = getEntityManager();
			s = new Child();
			s.setType(name);
			getEntityManager().persist(s);

			queryAll();
			
			//			tc.getUtx().commit();

		} catch (Exception e) {
			System.out.println(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
		}
		//
		return s;
	}

	public List<Child> queryAll() {
//		EntityManager entityManager3 = getEntityManager();
		TypedQuery<Child> q = getEntityManager().createQuery("select s from Child s", Child.class);
		List<Child> resultList = q.getResultList();
//		resultList.stream().forEach(x -> System.out.println(String.format("Entity: %s", x.getType())));
//		System.out.println(String.format("List object DB size: %s ", resultList));
//		System.out.println(String.format("All entities in DB size: %s ", resultList.size()));
		return resultList;
	}
	
	@PostConstruct
	public void info(){
		System.out.println("SimpleEntityFacade constructed "+this);
	}
	
	@PreDestroy
	public void outfo(){
		System.out.println("SimpleEntityFacade destructed "+this);
	}
}
