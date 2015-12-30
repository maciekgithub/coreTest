package cdi.custom.scope;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class ExecutorFactory {

	@Any
	@Inject
	private Instance<Executor> operationExecutorSource;

	public Executor create(Context ctx) throws Exception {
		try {
			Executor executor =
				operationExecutorSource.get();
			try {
				executor.init(ctx, this);
			} catch (Exception e) {
				operationExecutorSource.destroy(executor);
				throw e;
			}
			return executor;
		} catch (Exception e) {
			throw e;
		}
	}
}
