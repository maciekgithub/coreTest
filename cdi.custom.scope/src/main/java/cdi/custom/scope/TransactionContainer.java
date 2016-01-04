package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.Dependent;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

@Dependent
public class TransactionContainer {

	public TransactionContainer() {
	}

	@Resource
	private UserTransaction utx;

	@Resource(name = "concurrent/__defaultManagedExecutorService")
	private ManagedExecutorService executorService;

	@Resource
	private TransactionSynchronizationRegistry tsr;

	@PostConstruct
	public void postConstruct() {

		InitialContext ctx = null;

		if (getExecutorService() == null) {
			

			try {
				ctx = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}
			try {
				executorService = (ManagedExecutorService) ctx.lookup("java:comp/DefaultManagedExecutorService");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else {
		}
		
		if (getUtx() == null) {
			
			
			try {
				ctx = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}
			try {
				utx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else {
		}
		
		if (getTsr() == null) {
			
			
			try {
				ctx = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}
			try {
				setTsr((TransactionSynchronizationRegistry) ctx.lookup("java:comp/TransactionSynchronizationRegistry"));
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

	public ManagedExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ManagedExecutorService executorService) {
		this.executorService = executorService;
	}

	public UserTransaction getUtx() {
		return utx;
	}

	public void setUtx(UserTransaction utx) {
		this.utx = utx;
	}

	public TransactionSynchronizationRegistry getTsr() {
		return tsr;
	}

	public void setTsr(TransactionSynchronizationRegistry tsr) {
		this.tsr = tsr;
	}

}
