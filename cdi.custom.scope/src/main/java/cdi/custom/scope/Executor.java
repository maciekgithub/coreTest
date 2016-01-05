package cdi.custom.scope;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;
import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.RestEndpoint.CDIBean;
import cdi.custom.scope.stuff.FooScopeContext;



/**
 * 
 * @author mkamin
 * 
 * https://developer.jboss.org/thread/186700?start=0&tstart=0
 * SEAM
 * The entityManager is Conversation scoped, while your bean is session scoped. 
 * This means that after the conversation that created the bean is over, the entityManager will be closed.
 * You should not store references to conversation scoped components inside a session scoped component
 * (actually you should not store references to components at all). 
 * Instead they should be injection each time using @In.
 * 
 */

public class Executor implements Callable<String>, ManagedTask {

	public Executor() {
	}

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@Inject
	private TransactionContainer tc;

	private ExecutorFactory exe;

	private Context ctx;
	
	@Inject
	private Conversation conversation;

	@Inject
	private SimpleEntityFacade sef2; 
	
	CDIBean<FooScopeContext> fsc;
	
	@Any
	@Inject
	private Instance<Command> commandSrc;

	@Inject
	private ValidatorManager vm;


	public void init(Context ctx, ExecutorFactory exe) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.ctx = ctx;
		this.exe = exe;

	}

	public String execute() {
//		L.info(String.format("Executor - Submitting task to Managed Executor"));

		List<Child> queryAll = ctx.getP().sef.queryAll();
		
		L.info(String.format("Querying in execute()"));
		
//		L.info(String.format("2 - READING ALL PROM PROFILE FACADE %s", queryAll));

		tc.getExecutorService().submit(this);
		return "Processed";
	}

	public String call() {
		L.info(String.format("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
		try {
			L.info(String.format("Will sleep for 5 seconds"));
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		L.info(String.format("CONVERSATION %s",conversation));
//
		try {
			String srvName = "Srv_" + UUID.randomUUID();

						tc.getUtx().begin();
						List<Child> queryAll = ctx.getP().sef.queryAll();
						tc.getUtx().commit();
//
		} catch (Exception e) {
			L.info(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
			e.printStackTrace();
		}
		L.info(String.format("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
		return "Processed";
	}

	public ManagedTaskListener getManagedTaskListener() {
		return new AsyncManagedTaskListener();
	}

	public Map<String, String> getExecutionProperties() {
		return null;
	}

	class AsyncManagedTaskListener implements ManagedTaskListener {

		public void taskSubmitted(Future<?> future, ManagedExecutorService executor, Object task) {
			L.info(String.format("Executor - Async task submitted."));
		}

		public void taskAborted(Future<?> future, ManagedExecutorService executor, Object task, Throwable exception) {
			L.info(String.format("Executor - Async task Aborted."));
		}

		public void taskDone(Future<?> future, ManagedExecutorService executor, Object task, Throwable exception) {
			L.info(String.format("Executor - Async task Done with exception "+exception));
			if (null != exception) {
				exception.printStackTrace();
			}
//			exe.destroy((Executor) task);
			exe.destroy();
		}

		public void taskStarting(Future<?> future, ManagedExecutorService executor, Object task) {
			L.info(String.format("Executor - Async task Starting."));
		}
	}
	
	@PostConstruct
	public void info(){
		L.info("Executor constructed "+this);
	}
	
	@PreDestroy
	public void outfo(){
		L.info("Executor destructed "+this);
	}

	public void init(Context ctx2, ExecutorFactory executorFactory, CDIBean<FooScopeContext> fsc2) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.ctx = ctx2;
			this.exe = executorFactory;
			this.fsc =fsc2;
			L.info(String.format("Executor - initialized  with Profile %s", ctx.getP()));
	}

}
