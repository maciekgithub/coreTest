package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains CDI producer/disposer methods for {@link javax.persistence.EntityManager}.
 *
 * @author jigga
 */
@ApplicationScoped
public class EntityManagerFactory {

	private static final String PU_NAME_ISEP_MODEL = "isep-modelx";

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@PersistenceUnit(unitName = PU_NAME_ISEP_MODEL)
	private javax.persistence.EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		L.info("Creating producer EntityManagerFactory");
		if (null == entityManagerFactory) {
			entityManagerFactory = Persistence.createEntityManagerFactory(PU_NAME_ISEP_MODEL);
		} else {
		}
	}

	@Produces 
	public EntityManager createEntityManager() {
		EntityManager entityManager =
			entityManagerFactory.createEntityManager();
		L.info("CREATING EM DEPENDENT !!!! "+entityManager);
		return entityManager;
	}
	
//	@Produces 
//	public EntityManager createEntityManagerRS() {
//		EntityManager entityManager =
//			entityManagerFactory.createEntityManager();
//		L.info("CREATING EM REQEST SCOPED !!!! "+entityManager);
//		return entityManager;
//	}

	/**
	 * Disposer method for {@link javax.persistence.EntityManager} produced by {@link #createEntityManager()} method.
	 *
	 * @param entityManager {@link javax.persistence.EntityManager} instance produced by {@link #createEntityManager()}.
	 */
	public void destroyEntityManager(@Disposes EntityManager entityManager) {
		if (entityManager.isOpen()) {
			L.info("DISPOSESING EM !!!! "+entityManager);
			entityManager.close();
		}
	}
}


