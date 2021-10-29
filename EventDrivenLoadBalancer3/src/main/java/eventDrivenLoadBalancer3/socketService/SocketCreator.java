package eventDrivenLoadBalancer3.socketService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import eventDrivenLoadBalancer3.server.Config;

public class SocketCreator {
	public static Socket clientSocket(Config config) throws UnknownHostException, IOException {
		Socket client = null;
		
		if(config.getProtocol() == null || !config.getProtocol().equals("HTTPS")) {
			client = new Socket(config.getHost(), config.getPort());
		}else {
			
		}
		
		return client;
	}
	
	public static ServerSocket serverSocket(Config config) throws UnknownHostException, IOException {
		ServerSocket client = null;
		
		if(config.getProtocol() == null || !config.getProtocol().equals("HTTPS")) {
			client = new ServerSocket(config.getPort());
		}else {
			
		}
		
		return client;
	}
}
