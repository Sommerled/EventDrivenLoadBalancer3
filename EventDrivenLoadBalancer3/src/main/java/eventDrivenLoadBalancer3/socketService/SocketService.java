package eventDrivenLoadBalancer3.socketService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Map;

import eventDrivenLoadBalancer3.events.AbstractEvent;
import eventDrivenLoadBalancer3.events.ConfigEvent;
import eventDrivenLoadBalancer3.events.EventType;
import eventDrivenLoadBalancer3.events.ServerSocketDestroyEvent;
import eventDrivenLoadBalancer3.events.SocketConfigEvent;
import eventDrivenLoadBalancer3.server.Config;
import eventDrivenLoadBalancer3.server.Service;

public class SocketService extends Service {	
	private Map<Integer, Boolean> sockets;
	private Boolean shutdown;
	private Integer serverSocketTimeout;
	
	@Override
	public void run() {
		shutdown = false;
		
		if(this.sockets != null) {
			while(true) {
				try {
					AbstractEvent ae = this.getEventListener().peek();
					switch(ae.getEventType()){
					case NEW_SERVER_SOCKET_SERVICE:
						this.getEventListener().remove(ae);
						ConfigEvent sse = (ConfigEvent) ae;
						createServerSocket(sse);
						break;
					case NEW_CLIENT_SOCKET_SERVICE:
						this.getEventListener().remove(ae);
						SocketConfigEvent sce = (SocketConfigEvent) ae;
						
						//create client socket worker
						
						break;
					case SHUTDOWN:				
						shutdown = true;
						
						//TODO: shutdown all socket threads
 						
						break;
					case SERVER_SOCKET_DESTROYED:
						ServerSocketDestroyEvent ssde = (ServerSocketDestroyEvent) ae;
						serverSocketDestroyed(ssde);
						
						break;
					default:
						break;
					}
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}else {
			throw new RuntimeException("Socket map not set.");
		}
		
	}

	public void setSockets(Map<Integer, Boolean> sockets) {
		this.sockets = sockets;
	}
	
	public void setServerSocketTimeout(Integer serverSocketTimeout) {
		this.serverSocketTimeout = serverSocketTimeout;
	}
	
	private void createServerSocket(ConfigEvent sse) throws UnknownHostException, IOException, InterruptedException {
		Config serverSocketConfig = sse.getConfig();
		
		//there can be only one per port
		if(sockets.containsKey(serverSocketConfig.getPort()) && sockets.get(serverSocketConfig.getPort())) {
			ServerSocketDestroyEvent ssde = new ServerSocketDestroyEvent(this, EventType.SERVER_SOCKET_DESTROY, sse, serverSocketConfig.getPort());
			this.getEventDispatcher().put(ssde);
		} else {
			if(serverSocketTimeout != null) {
				ServerSocket serverSocket = SocketCreator.serverSocket(serverSocketConfig);
				serverSocket.setSoTimeout(serverSocketTimeout);
				
				ServerSocketWorker serverSocketWorker = new ServerSocketWorker(serverSocket, serverSocketConfig.getPort());
				serverSocketWorker.setEventDispatcher(this.getEventDispatcher());
				serverSocketWorker.setEventListener(this.getEventListener());
				
				Thread serverSocketWorkerThread = new Thread(serverSocketWorker);
				serverSocketWorkerThread.setName(Integer.toString(serverSocketConfig.getPort()));
				serverSocketWorkerThread.start();
				
				sockets.put(serverSocketConfig.getPort(), true);
			}else {
				throw new RuntimeException("Server Socket Accept timeout not set.");
			}
		}
	}
	
	private void serverSocketDestroyed(ServerSocketDestroyEvent ssde) throws InterruptedException {
		if(this.sockets.containsKey(ssde.getPort())) {
			this.sockets.get(ssde.getPort());
		}
		
		if(shutdown) {				
			//TODO: shutdown logic
		}
		
		if(ssde.getNextEvent() != null) {
			this.getEventDispatcher().put(ssde.getNextEvent());
		}
	}

}
