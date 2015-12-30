package cdi.custom.scope;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class RestApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {

		final Set<Class<?>> classes
			= new HashSet<Class<?>>();

		// resources from isep-core-web module
		classes.add(RestEndpoint.class);

		try {
			classes.add(Class.forName("org.glassfish.jersey.jackson.JacksonFeature"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// utility providers

		return classes;

	}

}


