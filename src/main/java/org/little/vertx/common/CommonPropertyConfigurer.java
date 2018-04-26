package org.little.vertx.common;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;

public class CommonPropertyConfigurer extends PreferencesPlaceholderConfigurer {

	private static Properties constants = new Properties();
	
	public static Properties getInstance() {
		return constants;
	}

	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
		constants = props;
	}

}
