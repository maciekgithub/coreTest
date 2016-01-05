package cdi.custom.scope.stuff;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;

//@ConversationScoped
public class TestConversational {
	
//	@Inject 
//	Conversation conversation;
	
	@PostConstruct
	public void startup(){
//		conversation.begin();
	}

}
