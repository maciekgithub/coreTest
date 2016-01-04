package cdi.custom.scope;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.RestEndpoint.CDIBean;
import cdi.custom.scope.stuff.FooScopeContext;

@ApplicationScoped
public class ExecutorFactory {

	@Any
	@Inject
	private Instance<Executor> operationExecutorSource;
	
	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	Executor e;

	public Executor create(Context ctx) throws Exception {
		try {
			Executor executor =
				operationExecutorSource.get();
			try {
				executor.init(ctx, this);
				e=executor;
			} catch (Exception e) {
				operationExecutorSource.destroy(executor);
				throw e;
			}
			return executor;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Executor create(Context ctx,CDIBean<FooScopeContext> fsc) throws Exception {
		try {
			Executor executor =
				operationExecutorSource.get();
			try {
				executor.init(ctx, this,fsc);
				e=executor;
			} catch (Exception e) {
				operationExecutorSource.destroy(executor);
				throw e;
			}
			return executor;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void destroy() {
		operationExecutorSource.destroy(e);
	}
}
