package cdi.custom.scope;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.TransactionScoped;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@TransactionScoped
public class EntityManagerWrapper implements Serializable {

	@Inject
	private transient EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
		throw new UnsupportedOperationException(String.format("%s does not support serialization", getClass()));
	}
}

