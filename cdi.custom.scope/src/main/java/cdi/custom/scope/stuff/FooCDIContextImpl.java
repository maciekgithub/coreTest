package cdi.custom.scope.stuff;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import cdi.custom.scope.stuff.FooScopeContext.ScopedInstance;
import cdi.custom.scope.stuff.FooScopeContext.ThreadLocalState;


public class FooCDIContextImpl implements Context {

	public Class<? extends Annotation> getScope() {
		System.out.println("FooCDIContextImpl  get Scope ");
		return FooScope.class;
	}

	public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
		
		System.out.println("FooCDIContextImpl get invoked");
		
		Bean bean = (Bean) contextual;
		ScopedInstance si = null;
		ThreadLocalState tscope = FooScopeContext.state.get();
		if (Foo.class.isAssignableFrom(bean.getBeanClass())) {
			System.out.println("FooCDIContextImpl Foo");
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
				System.out.println("FooCDIContextImpl Added Si "+si);
			}

			return (T) si.instance;
		} else {
			System.out.println("FooCDIContextImpl OTHER "+bean.getBeanClass());
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
		System.out.println("FooCDIContextImpl Returning INSTANCE"+ instance);
		return (T) instance;
//		tscope.fooInstances.add(si);
//		throw new IllegalArgumentException();
	}

	public boolean isActive() {
		System.out.println("XXX FooCDIContextImpl ID"+ this);
		boolean active = FooScopeContext.state != null ? true : false;
		System.out.println("XXX FooCDIContextImpl isActive "+active);
		return active;
	}

}
