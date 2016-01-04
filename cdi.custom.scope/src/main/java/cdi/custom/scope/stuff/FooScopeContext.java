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
	
//	ThreadLocalState scope = null;

	public static ThreadLocalState state;
//	= new ThreadLocalState();

	 static class ThreadLocalState {
		//These should be converted to thread safe collections
		Set<ScopedInstance<Foo>> allInstances = new HashSet<ScopedInstance<Foo>>();
		Set<ScopedInstance<Foo>> fooInstances = new HashSet<ScopedInstance<Foo>>();
	}

	@Inject
	BeanManager bm;

	public void create() {
		L.info("FooScope create");
		state = new ThreadLocalState();
	}

	public void begin() {
//		if (state != null) {
//			throw new IllegalAccessError("Already in FooScope");
//		}
//		state.set(scope);
		L.info("FooScope begin"+this);
	}

	public void end() {
		if (state == null) {
			throw new IllegalAccessError("Not in FooScope");
		}
//		state.remove();
		L.info("FooScope end "+this);
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
		for (ScopedInstance entry2 : state.allInstances) {
			L.info("FooScopeContext destroying "+entry2);
			entry2.bean.destroy(entry2.instance, entry2.ctx);
		}
		state = null;
	}

	public static class ScopedInstance<T> {
		Bean<T> bean;
		CreationalContext<T> ctx;
		T instance;
	}
	
	@PostConstruct
	public void init() {
		L.info("FooScopeContext INIT"); 
	}
	
	@PreDestroy
	public void destroyPost() {
		L.info("FooScopeContext DESTROY"); 
	}

}
