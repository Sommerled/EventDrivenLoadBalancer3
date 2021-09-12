package eventDrivenLoadBalancer3.server;

import eventDrivenLoadBalancer3.eventHandler.EventHandler;

public class Server {
	private EventHandler eventHandler = null;
	
	public Server() {
		
	}
	
	public void startServices() {
		
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}
}
