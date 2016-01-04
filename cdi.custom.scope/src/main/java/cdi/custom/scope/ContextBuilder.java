package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class ContextBuilder {
	
	@Inject
	private ProfileBuiler pb;
	
	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	private Profile p;
	
	public Context getContext() {
		return new Context(pb.buildProfile());
	}

	public Profile getP() {
		return p;
	}

	public void setP(Profile p) {
		this.p = p;
	}

	@Override
	public String toString() {
		return String.format("ContextBuilder [pb=%s, p=%s]", pb, p);
	}
	
	@PostConstruct
	public void info(){
		L.info("ContextBuilder constructed");
	}
	
	@PreDestroy
	public void outfo(){
		L.info("ContextBuilder destructed");
	}

}
