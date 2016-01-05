package cdi.custom.scope.stuff;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class FooScopeContext implements Serializable /*implements some ScopeContext interface*/{

	private static final Logger L =
			LoggerFactory.getLogger("log");

	ThreadLocalState scope = null;

	public static ThreadLocal<ThreadLocalState> state = new ThreadLocal<ThreadLocalState>();

	static class ThreadLocalState {
		//These should be converted to thread safe collections
		Set<ScopedInstance<Foo>> allInstances = new HashSet<ScopedInstance<Foo>>();
		Set<ScopedInstance<Foo>> fooInstances = new HashSet<ScopedInstance<Foo>>();
	}

	@Inject
	BeanManager bm;

	public void create() {
		L.info("FooScope will be created via "+this);
		scope = new ThreadLocalState();
	}

	public void begin() {
		if (state.get() != null) {
			throw new IllegalAccessError("Already in FooScope");
		}
		state.set(scope);
		L.info("FooScope will begin due to "+this);
	}

	public void end() {
		L.info("IMORTANT!! FooScope will end due to "+this+" SCOPE STATE "+state.get());
		if (state.get() == null) {
			throw new IllegalAccessError("Not in FooScope");
		}
		state.remove();
		L.info("FooScope will end sucessfully due to "+this);
	}

	public <T> T newInstance(Class<T> clazz) {
		Set<Bean<?>> beans = bm.getBeans(clazz, new AnnotationLiteral<Any>() {
		});
		if (beans.size() > 0) {
			Bean bean = beans.iterator().next();
			CreationalContext cc = bm.createCreationalContext(bean);
			return (T) bm.getReference(bean, clazz, cc);
		}
		return null;
	}

	public void destroy() {
		//Since this is not a CDI NormalScope we are responsible for managing the entire lifecycle, including
		//destroying the beans
		for (ScopedInstance entry : state.get().allInstances) {
			L.info("DESTROYING "+entry);
			entry.bean.destroy(entry.instance, entry.ctx);
		}
		state = null;
	}

	public static class ScopedInstance<T> {
		Bean<T> bean;
		CreationalContext<T> ctx;
		T instance;
		
		@Override
		public String toString() {
			L.info("ScopedInstance of type " + instance + ", with bean "+ bean +", creationalCtx "+ctx);
			return super.toString();
		}
	}
	
	@PostConstruct
	public void init() {
		L.info("INIT"); 
	}
	
	@PreDestroy
	public void destroyPost() {
		L.info("DESTROY"); 
	}

}
