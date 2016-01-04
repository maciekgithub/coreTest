package cdi.custom.scope;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.stuff.Foo;
import cdi.custom.scope.stuff.FooCDIContextImpl;
import cdi.custom.scope.stuff.FooCDIInstanceProducer;
import cdi.custom.scope.stuff.FooInstance;
import cdi.custom.scope.stuff.FooScope;
import cdi.custom.scope.stuff.FooScopeContext;

public class CDIScopeTestExtension implements Extension {
	// We will bootstrap all CDI beans rather than using beans.xml for
	// autodiscovery
	
	private static final Logger L =
			LoggerFactory.getLogger("log");

	public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
		L.info(" beforeBeanDiscovery EXTENSION BM" + bm);
//		bbd.addAnnotatedType(bm.createAnnotatedType(FooCDIInstanceProducer.class));
//		bbd.addAnnotatedType(bm.createAnnotatedType(FooScopeContext.class));
//		bbd.addAnnotatedType(bm.createAnnotatedType(Foo.class));
		// bbd.addAnnotatedType(bm.createAnnotatedType(Bar.class));
//		 bbd.addQualifier(FooInstance.class);
//		bbd.addScope(FooScope.class, false, false);	

		// bbd.addAnnotatedType(bm.createAnnotatedType(FooCDIInstanceProducer.class));
		// bbd.addAnnotatedType(bm.createAnnotatedType(FooScopeContext.class));
		// bbd.addAnnotatedType(bm.createAnnotatedType(Foo.class));
		// bbd.addAnnotatedType(bm.createAnnotatedType(Bar.class));
		// bbd.addQualifier(FooInstance.class);
		// bbd.addScope(FooScope.class, false, false);
	}

	public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		FooCDIContextImpl fooCDIContextImpl = new FooCDIContextImpl();
		L.info("afterBeanDiscovery EXTENSION fooCDIContextImpl" + fooCDIContextImpl);
		// L.info("EXTENSION BM"+bm);
		abd.addContext(fooCDIContextImpl);
		// abd.addContext(new FooCDIContextImpl());
	}

	public void afterDeployment(@Observes AfterDeploymentValidation adv, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

	public void beforeShutdown(@Observes BeforeShutdown bfs, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

	public void processAnnotatedType(@Observes ProcessAnnotatedType<?> pa, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

	public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

	public void processProducer(@Observes ProcessProducer<?, ?> ppd, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

	public void processBean(@Observes ProcessBean<?> pb, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

	public void processObserverMethod(@Observes ProcessObserverMethod<?, ?> pom, BeanManager bm) {
		// L.info("EXTENSION BM"+bm);
	}

}
