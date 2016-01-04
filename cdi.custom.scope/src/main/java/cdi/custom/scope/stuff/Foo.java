package cdi.custom.scope.stuff;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cdi.custom.scope.Child;

@FooScope
public class Foo {
	
	public boolean initialized=false;
	public boolean finalized=false;
	public String Id;
	
	@Inject
	EntityManager em;
	
	@PostConstruct
	void init(){
		System.out.println("Foo init "+em);
		initialized=true;
	}
	
	@PreDestroy
	public
	void destroy(){
		System.out.println("Foo destroy "+em);
		finalized=true;
	}
	
	public List<Child> queryAll() {
//		EntityManager entityManager3 = getEntityManager();
		System.out.println("Foo EM "+em);
		TypedQuery<Child> q = em.createQuery("select s from Child s", Child.class);
		List<Child> resultList = q.getResultList();
		resultList.stream().forEach(x -> System.out.println(String.format("Entity: %s", x.getType())));
		System.out.println(String.format("Foo -> List object DB size: %s ", resultList));
//		System.out.println(String.format("All entities in DB size: %s ", resultList.size()));
		return resultList;
	}

}
