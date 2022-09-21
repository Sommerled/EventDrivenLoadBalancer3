package eventDrivenLoadBalancer3.socketService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import eventDrivenLoadBalancer3.events.AbstractEvent;
import eventDrivenLoadBalancer3.events.EventType;
import eventDrivenLoadBalancer3.events.ServerSocketDestroyEvent;
import eventDrivenLoadBalancer3.events.SocketEvent;
import eventDrivenLoadBalancer3.server.Service;

public class ServerSocketWorker extends Service {
	private final ServerSocket serverSocket;
	private final Integer port;
	
	public ServerSocketWorker(ServerSocket serverSocket, Integer port) {
		this.serverSocket = serverSocket;
		this.port = port;
	}

	@Override
	public void run() {
		if(this.serverSocket == null) {
			throw new RuntimeException("Unable to start ServerSocketWorker. Null Server Socket");
		}
		
		if(this.getEventDispatcher() == null) {
			throw new RuntimeException("Unable to start ServerSocketWorker. Null dispatcher");
		}
		
		if(this.getEventListener() == null) {
			throw new RuntimeException("Unable to start ServerSocketWorker. Null Listener");
		}
		
		while(true) {
			try {
				Socket socket = this.serverSocket.accept();
				SocketEvent se = new SocketEvent(this, EventType.BALANCE_REQUEST, null, socket);
				this.getEventDispatcher().put(se);
			} catch(SocketTimeoutException e) {
				//check for a socket destroy event
				try {
					AbstractEvent ae = this.getEventListener().peek();
					
					if(ae.getEventType() == EventType.SERVER_SOCKET_DESTROY) {
						
						ServerSocketDestroyEvent ssde = (ServerSocketDestroyEvent)ae;
						if(ssde.getPort().equals(this.port)) {
							this.getEventListener().remove(ssde);
							
							if(ssde.getNextEvent() != null) {
								this.getEventDispatcher().put(ssde.getNextEvent());
							}
							
							this.setEventDispatcher(null);
							this.setEventListener(null);
							
							this.serverSocket.close();
						}
					}
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				} catch (IOException e1) {
					throw new RuntimeException("Server Socet error while closing", e1);
				}
			}catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	}

}
