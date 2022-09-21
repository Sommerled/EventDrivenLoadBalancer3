package eventDrivenLoadBalancer3.events;

import java.net.Socket;

import eventDrivenLoadBalancer3.server.Config;

public class SocketConfigEvent  extends AbstractEvent{
	private final Socket socket;
	private final Config config;

	public SocketConfigEvent(Object originator, EventType et, AbstractEvent nextEvent, Socket socket, Config config) {
		super(originator, et, nextEvent);
		this.socket = socket;
		this.config = config;
	}

	public Socket getSocket() {
		return socket;
	}

	public Config getConfig() {
		return config;
	}

}
