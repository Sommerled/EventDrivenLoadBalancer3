package eventDrivenLoadBalancer3.events;

import java.net.Socket;

public class SocketEvent extends AbstractEvent{
	private final Socket socket;
	
	public SocketEvent(Object originator, EventType et, AbstractEvent nextEvent, Socket socket) {
		super(originator, et, nextEvent);
		
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}
}
