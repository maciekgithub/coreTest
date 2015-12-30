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
			
			System.out.println(String.format("executorService is null - why ? Must obtain via lookup."));

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
			System.out.println(String.format("executorService not null - ok"));
		}
		
		if (getUtx() == null) {
			
			System.out.println(String.format("utx is null - why ? Must obtain via lookup."));
			
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
			System.out.println(String.format("utx not null - ok"));
		}
		
		if (getTsr() == null) {
			
			System.out.println(String.format("tsr is null - why ? Must obtain via lookup."));
			
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
			System.out.println(String.format("java:comp/TransactionSynchronizationRegistry not null - ok"));
		}

		
		
		
		System.out.println(String.format("TransactionContainer - constructed executorService: %s", getExecutorService()));
		System.out.println(String.format("TransactionContainer - constructed utx: %s", getUtx()));
		
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
