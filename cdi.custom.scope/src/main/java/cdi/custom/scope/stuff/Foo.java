package cdi.custom.scope.stuff;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cdi.custom.scope.Child;

@FooScope
public class Foo {
	
	public boolean initialized=false;
	public boolean finalized=false;
	public String Id;
	
	@Inject
	EntityManager em;
	
	private static final Logger L =
			LoggerFactory.getLogger("log");
	
	@PostConstruct
	void init(){
		L.info("Foo init "+em);
		initialized=true;
	}
	
	@PreDestroy
	public
	void destroy(){
		L.info("Foo destroy "+em);
		finalized=true;
	}
	
	public List<Child> queryAll() {
//		EntityManager entityManager3 = getEntityManager();
		L.info("Foo EM "+em);
		TypedQuery<Child> q = em.createQuery("select s from Child s", Child.class);
		List<Child> resultList = q.getResultList();
		resultList.stream().forEach(x -> L.info(String.format("Entity: %s", x.getType())));
		L.info(String.format("Foo -> List object DB size: %s ", resultList));
//		L.info(String.format("All entities in DB size: %s ", resultList.size()));
		return resultList;
	}

}
