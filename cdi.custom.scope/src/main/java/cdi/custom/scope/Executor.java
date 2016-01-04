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
	private SimpleEntityFacade sef2; 
	
	CDIBean<FooScopeContext> fsc;
	
	@Any
	@Inject
	private Instance<Command> commandSrc;

	@Inject
	private ValidatorManager vm;

	//	@Inject
	//	@TestCommand(name="TestCommandName")
	//	private Command c;

	//	public Executor(Profile p) {
	//		this.p = p;
	//	}

	public void init(Context ctx, ExecutorFactory exe) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.ctx = ctx;
		this.exe = exe;

//		L.info(String.format("Executor - initialized  with Profile %s", ctx.getP()));
	}

	public String execute() {
//		L.info(String.format("Executor - Submitting task to Managed Executor"));

		List<Child> queryAll = ctx.getP().sef.queryAll();
//		L.info(String.format("2 - READING ALL PROM PROFILE FACADE %s", queryAll));

		tc.getExecutorService().submit(this);
		return "Processed";
	}

	public String call() {
		L.info(String.format("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));
		
		List<Child> queryAll2 = sef2.queryAll();;
//		L.info(String.format("3a - READING ALL PROM PROFILE FACADE %s", queryAll2));
		
		
		L.info(String.format("Ending foo scope in CALL %s ",fsc.instance));
		ctx.getP().f.queryAll();
		L.info(String.format("Ending foo scope in CALL %s ","end()"));
		fsc.instance.end();
		L.info(String.format("Ending foo scope in CALL %s ","destroy()"));
		fsc.instance.destroy();
		fsc.bean.destroy(fsc.instance, fsc.cCtx);
//		L.info(String.format("Executor - Async task started - will process request for 5 sec."));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//		p.useFacade(1L);

		Instance<Command> select = commandSrc.select(new TestCommand() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return TestCommand.class;
			}

			@Override
			public String name() {
				return "TestCommandName";
			}
		});

		Command c = select.get();

//		L.info(String.format("Before beginning transaction.."));
		try {
			String srvName = "Srv_" + UUID.randomUUID();

//			L.info(String.format("Will persist service %s.", srvName));

						tc.getUtx().begin();
						List<Child> queryAll = ctx.getP().sef.queryAll();
						tc.getUtx().commit();
//						L.info(String.format("3 - READING ALL PROM PROFILE FACADE %s", queryAll));
			//			//			ctx.getP().useFacadeAndPersistService(srvName);
			//			L.info(String.format("Command created succesfully %s. Will invoke execute()", c));
			//			c.execute(ctx);

//			vm.validate(ctx);
//
		} catch (Exception e) {
			L.info(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
			e.printStackTrace();
		}

//		L.info(String.format("After commiting transaction - ok"));
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
//			L.info(String.format("Executor - initialized  with Profile %s", ctx.getP()));
	}

}
