package eventDrivenLoadBalancer3.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eventDrivenLoadBalancer3.Balancer.AbstractBalancer;
import eventDrivenLoadBalancer3.Balancer.Balancer_Algorithm;
import eventDrivenLoadBalancer3.Balancer.RoundRobinBalancer;
import eventDrivenLoadBalancer3.eventHandler.EventHandler;
import eventDrivenLoadBalancer3.events.AbstractEvent;
import eventDrivenLoadBalancer3.socketService.SocketService;

public class ServerMain {

	/**
	 * Starts the Load Balancer. This accepts two arguments:
	 * 1) <configFileName> (the initial configuration for the 
	 * 						Load Balancer - json format) and
	 * 2) <keyStore> (the path to the keystore for HTTPS connections)
	 * 
	 * Ex. Java -jar EventDrivenLoadBalancer3.jar config.json keyStore.key
	 * 
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException{
		String keystore = null;
		if(args.length >= 1) {
			
			File configFile = new File(args[0]);
			
			if(configFile.exists() && !configFile.isDirectory()) {
				//load the configs
				if(args.length >= 2) {
					keystore = args[1];
				}
				
				try {
					ConfigLoader.LoadConfigs(configFile, keystore);
				} catch (JsonParseException e) {
					throw new RuntimeException("Unable to parse as JSON: "+ e.getMessage(), e.getCause());
				} catch (JsonMappingException e) {
					throw new RuntimeException("Unable to map JSON file to config object: "+ e.getMessage(), e.getCause());
				} catch (IOException e) {
					throw new RuntimeException("Unable to read config file: "+ e.getMessage(), e.getCause());
				}
				configFile = null;
			}else {		
				//Create default configs and write them to the provided filename
				
				configFile = null;
				Config config = createDefaultConfig();
				ObjectMapper Obj = new ObjectMapper();
				
				try {
					String jsonStr = Obj.writeValueAsString(config);
					writeDefaultConfigToFile(jsonStr, args[0]);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Unable to parse default config object as json error: "+ e.getMessage(), e.getCause());
				} catch (IOException e) {
					throw new RuntimeException("Unable to write to file " + args[0] + ": " + e.getMessage(), e.getCause());
				}
				
				ConfigLoader.setConfig(config);
			}
			
			Server server = initializeServer(ConfigLoader.config());
			server.startServices();
			
		}else{
			throw new RuntimeException("Must include the path to an existing config file or config file that you want"
					+ " created as the first parameter.");
		}
	}
	
	private static Server initializeServer(Config config) {
		Server server = new Server();
		EventHandler eventHandler = new EventHandler();
		AbstractBalancer balancer = null;
		
		server.setConfig(config);
		
		BlockingQueue<AbstractEvent> eventQueue = new LinkedBlockingQueue<AbstractEvent>();
		eventHandler.setEventQueue(eventQueue);
		server.setEventHandler(eventHandler);
		
		switch(config.getAlgorithm()) {
		case ROUND_ROBIN:
			balancer= new RoundRobinBalancer();
			break;
		default:
			balancer= new RoundRobinBalancer();
			break;
		}
		
		balancer.setEventDispatcher(eventHandler);
		balancer.setEventListener(eventHandler);
		server.setBalancer(balancer);
		
		SocketService socketService = new SocketService();
		socketService.setSockets(new HashMap<Integer, Boolean>());
		socketService.setServerSocketTimeout(60000);
		server.setSocketService(socketService);
		
		return server;
	}
	
	private static Config createDefaultConfig(){
		Config config = new Config();
		config.setHost("127.0.0.1");
		config.setPort(8080);
		config.setProtocol("HTTP");
		config.setAlgorithm(Balancer_Algorithm.ROUND_ROBIN);
		config.setListening(true);
		
		return config;
	}
	
	private static void writeDefaultConfigToFile(String json, String fileName) throws IOException {
		BufferedWriter bw = null;
		File configFile = new File(fileName);
		
		if(configFile.isDirectory()) {
			configFile = null;
			String slash = "";
			
			if(fileName.endsWith("/")) {
				slash = "/";
			}
			
			configFile = new File(fileName + slash + "config.json");
		}
		
		FileWriter fw = new FileWriter(configFile);
		bw = new BufferedWriter (fw);
		
		try {
			bw.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			bw.close();
		}
	}

}
