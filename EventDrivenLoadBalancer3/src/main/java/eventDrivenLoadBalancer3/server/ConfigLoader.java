package eventDrivenLoadBalancer3.server;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author User
 *
 */
public class ConfigLoader {
	private static Config config;
	
	public ConfigLoader() {
		
	}
	
	public static void LoadConfigs(String fileName, String keystore) throws JsonParseException, JsonMappingException, IOException {
		File configFile = new File(fileName);
		LoadConfigs(configFile, keystore);
	}
	
	public static void LoadConfigs(File config, String keystore) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ConfigLoader.config = mapper.readValue(config, Config.class);
	}

	public static Config config() {
		return ConfigLoader.config;
	}

	public static void setConfig(Config config) {
		ConfigLoader.config = config;
	}
	
}
