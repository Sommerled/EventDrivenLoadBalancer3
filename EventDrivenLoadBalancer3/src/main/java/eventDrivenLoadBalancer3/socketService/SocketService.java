package eventDrivenLoadBalancer3.socketService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import eventDrivenLoadBalancer3.events.AbstractEvent;
import eventDrivenLoadBalancer3.events.ConfigEvent;
import eventDrivenLoadBalancer3.events.SocketConfigEvent;
import eventDrivenLoadBalancer3.server.Service;

public class SocketService extends Service {

	@Override
	public void run() {
		List<Integer> portList = new ArrayList<Integer>();
		while(true) {
			try {
				AbstractEvent ae = this.getEventListener().peek();
				switch(ae.getEventType()){
				case NEW_SERVER_SOCKET_SERVICE:
					this.getEventListener().remove(ae);
					ConfigEvent sse =  (ConfigEvent) ae;
					Boolean exists = false;
					
					for(Integer p : portList) {
						if(p.equals(sse.getConfig().getPort())) {
							exists = true;
							break;
						}
					}
					
					//there can be only one 
					if(exists) {
						//do something here
					} else {
						portList.add(sse.getConfig().getPort());
						ServerSocket serverSocket = SocketCreator.serverSocket(sse.getConfig());
						
						ServerSocketWorker serverSocketWorker = new ServerSocketWorker(serverSocket);
						serverSocketWorker.setEventDispatcher(this.getEventDispatcher());
						serverSocketWorker.setEventListener(this.getEventListener());
						
						Thread serverSocketWorkerThread = new Thread(serverSocketWorker);
						serverSocketWorkerThread.setName(Integer.toString(sse.getConfig().getPort()));
						serverSocketWorkerThread.start();
					}
					break;
				case NEW_CLIENT_SOCKET_SERVICE:
					this.getEventListener().remove(ae);
					SocketConfigEvent sce = (SocketConfigEvent) ae;
					
					//create client socket worker
					
					break;
				default:
					break;
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	}

}
