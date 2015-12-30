package cdi.custom.scope;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import pl.orange.isep.model.service.Service;

@TestCommand(name="TestCommandName")
public class Command {
	
	@Inject
	private SimpleEntityFacade sef;
	
	public void execute(Context ctx){
		
		List<Service> queryAll = ctx.getP().sef.queryAll();
		
		sef.useFacadeAndPersistService("Command|"+UUID.randomUUID());
	}
	
	

}
