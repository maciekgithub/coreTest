package cdi.custom.scope;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import pl.orange.isep.model.service.Service;


/**
 * 
 * @author mkamin
 * 
 * https://developer.jboss.org/thread/186700?start=0&tstart=0
 * 
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

	@Inject
	private TransactionContainer tc;

	private ExecutorFactory exe;

	private Context ctx;

	@Inject
	private SimpleEntityFacade sef2; 
	
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

		System.out.println(String.format("Executor - initialized  with Profile %s", ctx.getP()));
	}

	public String execute() {
		System.out.println(String.format("Executor - Submitting task to Managed Executor"));

		List<Service> queryAll = ctx.getP().sef.queryAll();
		System.out.println(String.format("2 - READING ALL PROM PROFILE FACADE %s", queryAll));

		tc.getExecutorService().submit(this);
		return "Processed";
	}

	public String call() {

		
		List<Service> queryAll2 = sef2.queryAll();;
		System.out.println(String.format("3a - READING ALL PROM PROFILE FACADE %s", queryAll2));
		

		System.out.println(String.format("Executor - Async task started - will process request for 5 sec."));
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

		System.out.println(String.format("Before beginning transaction.."));
		try {
			String srvName = "Srv_" + UUID.randomUUID();

			System.out.println(String.format("Will persist service %s.", srvName));

						tc.getUtx().begin();
						tc.getUtx().commit();
						List<Service> queryAll = ctx.getP().sef.queryAll();
						System.out.println(String.format("3 - READING ALL PROM PROFILE FACADE %s", queryAll));
			//			//			ctx.getP().useFacadeAndPersistService(srvName);
			//			System.out.println(String.format("Command created succesfully %s. Will invoke execute()", c));
			//			c.execute(ctx);

//			vm.validate(ctx);
//
		} catch (Exception e) {
			System.out.println(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
			e.printStackTrace();
		}

		System.out.println(String.format("After commiting transaction - ok"));

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
			System.out.println(String.format("Executor - Async task submitted."));
		}

		public void taskAborted(Future<?> future, ManagedExecutorService executor, Object task, Throwable exception) {
			System.out.println(String.format("Executor - Async task Aborted."));
		}

		public void taskDone(Future<?> future, ManagedExecutorService executor, Object task, Throwable exception) {
			System.out.println(String.format("Executor - Async task Done."));
			if (null != exception) {
				exception.printStackTrace();
			}
		}

		public void taskStarting(Future<?> future, ManagedExecutorService executor, Object task) {
			System.out.println(String.format("Executor - Async task Starting."));
		}
	}

}
