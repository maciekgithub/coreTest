package cdi.custom.scope;

import java.util.List;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import pl.orange.isep.model.service.Service;

public class SimpleEntityFacade {

	@Any
	@Inject
	private Instance<EntityManagerWrapper> entityManagerWrapperSource;

	private EntityManagerWrapper entityManagerWrapper;

	@Inject
	private EntityManager readOnlyEntityManager;

	private EntityManager entityManager;

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
			System.out.println(String.format("Obtained RO EM because exception came out %s", e.getMessage()));
		}
		return entityManager;
	}

	@Override
	public String toString() {
		return String.format("SimpleEntityFacade [entityManagerWrapperSource=%s, readOnlyEntityManager=%s, entityManager=%s]",
			entityManagerWrapperSource, readOnlyEntityManager, entityManager);
	}

	public Service useFacadeAndPersistService(String name) {

		Service s = null;
		try {
			//			tc.getUtx().begin();
			System.out.println("Will read all entities");
			System.out.println(String.format("Transaction status %s", tc.getTsr().getTransactionStatus()));

			EntityManager entityManager3 = getEntityManager();
			EntityManager entityManager4 = getEntityManager();

			System.out.println(String.format("Entity managers %s, %s", entityManager3, entityManager4));

			s = new Service();
			s.setName(name);
			entityManager3.persist(s);

			queryAll();
			
			//			tc.getUtx().commit();

		} catch (Exception e) {
			System.out.println(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
		}
		//
		return s;
	}

	public List<Service> queryAll() {
		EntityManager entityManager3 = getEntityManager();
		TypedQuery<Service> q = entityManager3.createQuery("select s from Service s", Service.class);
		List<Service> resultList = q.getResultList();
		resultList.stream().forEach(x -> System.out.println(String.format("Entity: %s", x.getName())));
		System.out.println(String.format("List object DB size: %s ", resultList));
		System.out.println(String.format("All entities in DB size: %s ", resultList.size()));
		return resultList;
	}

}
