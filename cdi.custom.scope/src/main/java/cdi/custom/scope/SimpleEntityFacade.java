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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
	
	private static final Logger L =
			LoggerFactory.getLogger("log");

	public EntityManager getEntityManager() {

		try {
			entityManagerWrapper = entityManagerWrapperSource.get();
			entityManager = entityManagerWrapper.getEntityManager();
			L.info(String.format("Obtained sucessfully transactional EM %s ", entityManager));
		} catch (ContextNotActiveException e) {
			entityManager =
				readOnlyEntityManager;
			L.info(String.format("Obtained RO EM because exception came out %s", readOnlyEntityManager));
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
			L.info("Will read all entities");
			L.info(String.format("Transaction status %s", tc.getTsr().getTransactionStatus()));

//			EntityManager entityManager3 = getEntityManager();
//			EntityManager entityManager4 = getEntityManager();
			s = new Child();
			s.setType(name);
			getEntityManager().persist(s);

			queryAll();
			
			//			tc.getUtx().commit();

		} catch (Exception e) {
			L.info(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
		}
		//
		return s;
	}

	public List<Child> queryAll() {
//		EntityManager entityManager3 = getEntityManager();
		TypedQuery<Child> q = getEntityManager().createQuery("select s from Child s", Child.class);
		List<Child> resultList = q.getResultList();
//		resultList.stream().forEach(x -> L.info(String.format("Entity: %s", x.getType())));
//		L.info(String.format("List object DB size: %s ", resultList));
//		L.info(String.format("All entities in DB size: %s ", resultList.size()));
		return resultList;
	}
	
	@PostConstruct
	public void info(){
		L.info("SimpleEntityFacade constructed "+this);
	}
	
	@PreDestroy
	public void outfo(){
		L.info("SimpleEntityFacade destructed "+this);
	}
}
