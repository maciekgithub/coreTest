package cdi.custom.scope;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdi.custom.scope.stuff.Bar;
import cdi.custom.scope.stuff.Foo;
import cdi.custom.scope.stuff.FooInstance;
import cdi.custom.scope.stuff.FooScopeContext;

/**
 * Event route for the Core REST webservice.
 */
@Path("/v1")
@RequestScoped
public class RestEndpoint {

	@Resource
	private UserTransaction utx;

	@Inject
	private TransactionContainer txcontainer;

	@Any
	@Inject
	private Instance<TransactionContainer> txcontainerSrc;

	// @Inject
	// private SimpleEntityFacade sef;

	// @Inject
	// private EntityManager readOnlyEntityManager;
	//
	// @Inject
	// private FacadeOne fo;
	//
	// @Inject
	// private FacadeTwo ft;

	@Inject
	private ExecutorFactory executorFactory;

	@Inject
	private TransactionContainer tc;

	@Inject
	private ContextBuilder ctxBuilder;

	@Inject
	private BeanManager bm;

	@Inject
//	@FooInstance
	@Any
	Instance<Foo> fooSrc;
	
	@Inject
//	@FooInstance
	@Any
	Instance<Bar> barSrc;

	@GET
	@Path("/event")
	// @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String readEN() {

		System.out.println(String.format(
				"#############################################################################################"));

		// System.out.println(String.format("readOnlyEntityManager %s",
		// readOnlyEntityManager));

		// System.out.println(String.format("Obtaining transactional EM..."));

		TransactionContainer transactionContainer = txcontainerSrc.get();

		// System.out.println(String.format("txcontainer via source %s",
		// transactionContainer));

		// System.out.println(String.format("SEF logging %s ", sef));
		// System.out.println(String.format("EM injected logging %s ",
		// readOnlyEntityManager));

		long sleepPeriod = 2000;

		try {
			Thread.sleep(sleepPeriod);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FooScopeContext fooScope = null;
		CDIBean<FooScopeContext> fooScopeBean = null;

		try {
			transactionContainer.getUtx().begin();
			System.out.println(String.format("Analyzing EM objects.."));
			System.out.println(String.format("BM in REST %s", bm));

			
			fooScopeBean = newInstance(FooScopeContext.class);
			fooScope = fooScopeBean.instance;

			System.out.println(String.format("FooScopeContext %s", fooScopeBean.instance));

			fooScope.create();
			Foo foo = null, foo2 = null;
			Bar bar;

//			try {
				fooScope.begin();
				foo = fooSrc.get();
				foo2 = fooSrc.get();
				bar =  barSrc.get();
//				 CDIBean<Foo> bar1Bean = newInstance(Foo.class);
//				 foo = bar1Bean.instance;
//				 CDIBean<Foo> bar2Bean = newInstance(Foo.class);
//				 foo2 = bar2Bean.instance;
				System.out.println(String.format("foo  %s", foo));
				System.out.println(String.format("foo %s", foo2));
//				foo.destroy();
//				foo2.destroy();
//			} catch (Exception e) {
//				System.out.println(String.format("EXCEPTION %s", e.getMessage()));
//			} finally {
//				fooScope.end();
//			}

			System.out.println(String.format("foo finalized %s", foo.finalized));
			System.out.println(String.format("foo2 finalized %s", foo2.finalized));
		
			System.out.println(String.format("foo finalized %s", foo.finalized));
			System.out.println(String.format("foo2 finalized %s", foo2.finalized));

			// System.out.println(String.format("Facade one EM %s", fo));
			// System.out.println(String.format("Facade two EM %s", ft));
			// fo.useFacadeAndPersistService("CHILD_IN_REST FACADE ONE");
			// fo.queryAll();
			// ft.queryAll();
			// ft.write("CHILD_IN_REST FACADE TWO USING SEF");
			transactionContainer.getUtx().commit();
			//
		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// String srvName = "SrvInEndpoint"+UUID.randomUUID();
		// System.out.println(String.format("Will persist service %s.",
		// srvName));
		// transactionContainer.getUtx().begin();
		// Service persistedService = sef.useFacadeAndPersistService(srvName);
		// transactionContainer.getUtx().commit();
		// System.out.println(String.format("COMMITED !!! using srv id: %s.",
		// persistedService.getId()));
		//// tc.getUtx().commit();
		// } catch (Exception e) {
		// System.out.println(String.format("Exception came when persisting %s,
		// %s", e.getMessage(), e));
		// }
		// ###
		System.out.println(String.format("Submitting task to executor"));

		Executor asyncExecutor = null;
		try {
			Context context = ctxBuilder.getContext();
			System.out.println(String.format("Context created by ContextBuilder %s", context));
			List<Child> queryAll = context.getP().sef.queryAll();

			asyncExecutor = executorFactory.create(context, fooScopeBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = asyncExecutor.execute();

		System.out.println(String.format("Sync execution result %s ", result));
		System.out.println(String.format(
				"#############################################################################################"));
		// ###
		return String.format("Reponse after %s millis: %s. Simple-Entity-Facade with EM toString() %s ", sleepPeriod,
				"tmp res", "tmp sef");
	}

	@PostConstruct
	public void info() {
		System.out.println("RestEndpoint constructed");
	}

	@PreDestroy
	public void outfo() {
		System.out.println("RestEndpoint destructed");
	}

	public static class CDIBean<T> {
		public Bean<T> bean;
		public CreationalContext<T> cCtx;
		public T instance;

		public void destroy() {
			bean.destroy(instance, cCtx);
		}

	}

	public <T> CDIBean<T> newInstance(Class<T> clazz) throws Exception {
		CDIBean cdiBean = new CDIBean();
		Set<Bean<?>> beans = bm.getBeans(clazz, new AnnotationLiteral<Any>() {
		});
		if (beans.size() > 0) {
			cdiBean.bean = beans.iterator().next();
			cdiBean.cCtx = bm.createCreationalContext(cdiBean.bean);
			System.out.println("Producing new instance of CDIBean of type " + clazz);
			cdiBean.instance = bm.getReference(cdiBean.bean, clazz, cdiBean.cCtx);
			return cdiBean;
		} else {
			throw new Exception(String.format("Can't find class %s", FooScopeContext.class));
		}
	}

}
