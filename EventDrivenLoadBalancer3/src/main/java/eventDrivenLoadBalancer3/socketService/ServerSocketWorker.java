package eventDrivenLoadBalancer3.socketService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import eventDrivenLoadBalancer3.events.EventType;
import eventDrivenLoadBalancer3.events.SocketEvent;
import eventDrivenLoadBalancer3.server.Service;

public class ServerSocketWorker extends Service {
	private final ServerSocket serverSocket;
	
	public ServerSocketWorker(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
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
				SocketEvent se = new SocketEvent(this, EventType.BALANCE_REQUEST, socket);
				this.getEventDispatcher().put(se);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	}

}
