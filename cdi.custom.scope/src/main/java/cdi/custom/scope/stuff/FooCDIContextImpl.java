package cdi.custom.scope.stuff;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.stuff.FooScopeContext.ScopedInstance;
import cdi.custom.scope.stuff.FooScopeContext.ThreadLocalState;


public class FooCDIContextImpl implements Context {

	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	public Class<? extends Annotation> getScope() {
		L.info("getScope()");
		return FooScope.class;
	}

	public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
		
		L.info("get(Contextual<T> contextual, CreationalContext<T> creationalContext");
		
		Bean bean = (Bean) contextual;
		ScopedInstance si = null;
		ThreadLocalState tscope = FooScopeContext.state.get();
		if (Foo.class.isAssignableFrom(bean.getBeanClass())) {
			L.info("isAssignableFrom(Foo)");
			//Check if qualifier is present
//			String id = FooCDIInstanceProducer.fooInstanceId.get();
//			if (id == null) {//no scope present, get scope singleton
//				id = "";
//			}
//			si = tscope.fooInstances.get(id);
			if (si == null) {
				
				si = new ScopedInstance();
				si.bean = bean;
				si.ctx = creationalContext;
				si.instance = bean.create(creationalContext);
				tscope.fooInstances.add(si);
				tscope.allInstances.add(si);
				L.info("fooInstances and allInstances have added ScopedInstance "+si);
			}

			return (T) si.instance;
		} else {
			L.info("isAssignableFrom(OTHER)");
			si = new ScopedInstance();
			si.bean = bean;
			si.ctx = creationalContext;
			si.instance = bean.create(creationalContext);
			tscope.allInstances.add(si);
		}
		return (T)si.instance;
	}

	public <T> T get(Contextual<T> contextual) {
		ThreadLocalState tscope = FooScopeContext.state.get();
		Foo instance = tscope.fooInstances.iterator().next().instance;
		L.info("get(Contextual<T> contextual) -> returning INSTANCE "+ instance);
		return (T) instance;
//		tscope.fooInstances.add(si);
//		throw new IllegalArgumentException();
	}

	public boolean isActive() {
		boolean active = FooScopeContext.state != null ? true : false;
		L.info("isActive "+active);
		L.info("reference to this "+ this);
		return active;
	}

}
