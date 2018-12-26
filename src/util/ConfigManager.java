package util;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigManager {

	private static ConfigManager instance = null;
	
	Configuration config;

	protected ConfigManager() {
		Configurations configs = new Configurations();
		try {
			config = configs.properties(new File("config.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	public String getProperty(String property) {
		return config.getString(property);
	}

}