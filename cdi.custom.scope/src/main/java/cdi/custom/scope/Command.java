package cdi.custom.scope;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;


@TestCommand(name="TestCommandName")
public class Command {
	
	@Inject
	private SimpleEntityFacade sef;
	
	public void execute(Context ctx){
		
		List<Child> queryAll = ctx.getP().sef.queryAll();
		
		sef.useFacadeAndPersistService("Command|"+UUID.randomUUID());
	}
	
	

}
