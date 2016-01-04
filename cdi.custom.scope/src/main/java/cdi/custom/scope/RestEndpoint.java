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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	
	private static final Logger L =
	LoggerFactory.getLogger("log");
	
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
		L.info("Starting");
		L.info(String.format(
				"#############################################################################################"));

		// L.info(String.format("readOnlyEntityManager %s",
		// readOnlyEntityManager));

		// L.info(String.format("Obtaining transactional EM..."));

		TransactionContainer transactionContainer = txcontainerSrc.get();

		// L.info(String.format("txcontainer via source %s",
		// transactionContainer));

		// L.info(String.format("SEF logging %s ", sef));
		// L.info(String.format("EM injected logging %s ",
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
			L.info(String.format("Analyzing EM objects.."));
			L.info(String.format("BM in REST %s", bm));

			
			fooScopeBean = newInstance(FooScopeContext.class);
			fooScope = fooScopeBean.instance;

			L.info(String.format("FooScopeContext %s", fooScopeBean.instance));

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
				L.info(String.format("foo  %s", foo));
				L.info(String.format("foo %s", foo2));
//				foo.destroy();
//				foo2.destroy();
//			} catch (Exception e) {
//				L.info(String.format("EXCEPTION %s", e.getMessage()));
//			} finally {
//				fooScope.end();
//			}

			L.info(String.format("foo finalized %s", foo.finalized));
			L.info(String.format("foo2 finalized %s", foo2.finalized));
		
			L.info(String.format("foo finalized %s", foo.finalized));
			L.info(String.format("foo2 finalized %s", foo2.finalized));

			// L.info(String.format("Facade one EM %s", fo));
			// L.info(String.format("Facade two EM %s", ft));
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
		// L.info(String.format("Will persist service %s.",
		// srvName));
		// transactionContainer.getUtx().begin();
		// Service persistedService = sef.useFacadeAndPersistService(srvName);
		// transactionContainer.getUtx().commit();
		// L.info(String.format("COMMITED !!! using srv id: %s.",
		// persistedService.getId()));
		//// tc.getUtx().commit();
		// } catch (Exception e) {
		// L.info(String.format("Exception came when persisting %s,
		// %s", e.getMessage(), e));
		// }
		// ###
		L.info(String.format("Submitting task to executor"));

		Executor asyncExecutor = null;
		try {
			Context context = ctxBuilder.getContext();
			L.info(String.format("Context created by ContextBuilder %s", context));
			List<Child> queryAll = context.getP().sef.queryAll();

			asyncExecutor = executorFactory.create(context, fooScopeBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = asyncExecutor.execute();

		L.info(String.format("Sync execution result %s ", result));
		L.info(String.format(
				"#############################################################################################"));
		// ###
		return String.format("Reponse after %s millis: %s. Simple-Entity-Facade with EM toString() %s ", sleepPeriod,
				"tmp res", "tmp sef");
	}

	@PostConstruct
	public void info() {
		L.info("RestEndpoint constructed");
	}

	@PreDestroy
	public void outfo() {
		L.info("RestEndpoint destructed");
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
			L.info("Producing new instance of CDIBean of type " + clazz);
			cdiBean.instance = bm.getReference(cdiBean.bean, clazz, cdiBean.cCtx);
			return cdiBean;
		} else {
			throw new Exception(String.format("Can't find class %s", FooScopeContext.class));
		}
	}

}
