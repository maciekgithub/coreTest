package cdi.custom.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class ContextBuilder {
	
	@Inject
	private ProfileBuiler pb;
	
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
		System.out.println("ContextBuilder constructed");
	}
	
	@PreDestroy
	public void outfo(){
		System.out.println("ContextBuilder destructed");
	}

}
