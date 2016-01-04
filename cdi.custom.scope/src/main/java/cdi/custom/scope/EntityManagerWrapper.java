package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.TransactionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@TransactionScoped
public class EntityManagerWrapper implements Serializable {
	
	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	private static final long serialVersionUID = 1L;
	@Inject
	private transient EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
		throw new UnsupportedOperationException(String.format("%s does not support serialization", getClass()));
	}
	
	@PostConstruct
	public void init(){
		L.info("EntityManagerWrapper constructed");
	}
}

