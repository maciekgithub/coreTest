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

@Dependent
public class FooScopeContext implements Serializable /*implements some ScopeContext interface*/{

//	ThreadLocalState scope = null;

	ThreadLocalState scope = null;

	public static ThreadLocal<ThreadLocalState> state = new ThreadLocal<ThreadLocalState>();

	static class ThreadLocalState {
		//These should be converted to thread safe collections
		Set<ScopedInstance<Foo>> allInstances = new HashSet<ScopedInstance<Foo>>();
		Set<ScopedInstance<Foo>> fooInstances = new HashSet<ScopedInstance<Foo>>();
		
		@Override
		public String toString() {
			System.out.println("ThreadLocalState toString");
			System.out.println("allInstances "+allInstances);
			System.out.println("fooInstances "+fooInstances);
			return super.toString();
		}
	}
	


	@Inject
	BeanManager bm;

	public void create() {
		System.out.println("FooScope create ");
		scope = new ThreadLocalState();
	}

	public void begin() {
		if (state.get() != null) {
			throw new IllegalAccessError("Already in FooScope");
		}
		state.set(scope);
		System.out.println("FooScope begin "+this);
	}

	public void end() {
		if (state == null) {
			throw new IllegalAccessError("Not in FooScope ");
		}
		state.remove();
		System.out.println("FooScope end "+this);
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
		System.out.println("state in  destroy() "+state.get());
		for (ScopedInstance entry2 : state.get().allInstances) {
			System.out.println("FooScopeContext destroying "+entry2);
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
		System.out.println("FooScopeContext INIT"); 
	}
	
	@PreDestroy
	public void destroyPost() {
		System.out.println("FooScopeContext DESTROY"); 
	}

}
