package cdi.custom.scope;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.orange.isep.model.service.Service;
import cdi.custom.scope.previous.version.FacadeOne;
import cdi.custom.scope.previous.version.FacadeTwo;

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

	@Inject
	private SimpleEntityFacade sef;

	@Inject
	private EntityManager readOnlyEntityManager;

	@Inject
	private FacadeOne fo;

	@Inject
	private FacadeTwo ft;

	@Inject
	private ExecutorFactory executorFactory;

	@Inject
	private TransactionContainer tc;

	@Inject
	private ContextBuilder ctxBuilder; 
	
	
	@GET
	@Path("/event")
	//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String readEN() {

//		System.out.println(String.format("readOnlyEntityManager %s", readOnlyEntityManager));

//		System.out.println(String.format("Obtaining transactional EM..."));

		TransactionContainer transactionContainer = txcontainerSrc.get();

//		System.out.println(String.format("txcontainer via source %s", transactionContainer));

		System.out.println(String.format("SEF logging %s ", sef));
		
		long sleepPeriod = 2000;

		try {
			Thread.sleep(sleepPeriod);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		try {
//			transactionContainer.getUtx().begin();
//			System.out.println(String.format("Analyzing EM objects.."));
//			System.out.println(String.format("Facade one EM %s", fo));
//			System.out.println(String.format("Facade two EM %s", ft));
//			transactionContainer.getUtx().commit();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		try {
//			String srvName = "SrvInEndpoint"+UUID.randomUUID();
//			System.out.println(String.format("Will persist service %s.", srvName));
//			transactionContainer.getUtx().begin();
//			Service persistedService = sef.useFacadeAndPersistService(srvName);
//			transactionContainer.getUtx().commit();
//			System.out.println(String.format("COMMITED !!! using srv id: %s.", persistedService.getId()));
////			tc.getUtx().commit();
//		} catch (Exception e) {
//			System.out.println(String.format("Exception came when persisting %s, %s", e.getMessage(), e));
//		}
//
		System.out.println(String.format("Submitting task to executor"));

		Executor asyncExecutor = null;
		try {
			
			Context context = ctxBuilder.getContext();
			
			List<Service> queryAll = context.getP().sef.queryAll();
			System.out.println(String.format("1 READING ALL PROM PROFILE FACADE %s",queryAll));
			
			System.out.println(String.format("Context created by ContextBuilder %s",context));
			
			System.out.println(String.format("Transaction STATUS IN REST %s", tc.getTsr().getTransactionStatus()));
			
			asyncExecutor = executorFactory.create(context);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = asyncExecutor.execute();

		System.out.println(String.format("Sync execution result %s ", result));

		return String.format("Reponse after %s millis: %s. Simple-Entity-Facade with EM toString() %s ", sleepPeriod,result, sef);
	}

}
