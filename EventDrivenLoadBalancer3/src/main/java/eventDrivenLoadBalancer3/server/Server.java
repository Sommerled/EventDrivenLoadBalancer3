package eventDrivenLoadBalancer3.server;

import java.util.List;

import eventDrivenLoadBalancer3.Balancer.AbstractBalancer;
import eventDrivenLoadBalancer3.eventHandler.EventHandler;
import eventDrivenLoadBalancer3.events.ConfigEvent;
import eventDrivenLoadBalancer3.events.EventType;

public class Server {
	private EventHandler eventHandler = null;
	private AbstractBalancer balancer = null;
	private Config config = null;
	
	public Server() {
		
	}
	
	public void startServices() throws InterruptedException {
		if(this.balancer == null) {
			throw new RuntimeException("The Server's balancer needs to be set");
		}else {
			if(this.balancer.getEventDispatcher() == null || this.balancer.getEventListener() == null) {
				throw new RuntimeException("The Server's balancer needs an Event Dispatcher and an Event Listener");
			}
		}
		
		if(this.config == null) {
			throw new RuntimeException("The Server's config needs to be set");
		}
		
		Thread balancerThread = new Thread(this.balancer);
		balancerThread.setName("Balancing Act");
		balancerThread.start();
		
		List<Config> children = this.config.getChildConfig();
		
		/*
		 * Tell the Balancing Act Thread about the socket on which
		 * to listen for new connections to the load balancer
		 */
		ConfigEvent listeningConfig = new ConfigEvent(this, EventType.NEW_CONNECTION, config);
		this.eventHandler.put(listeningConfig);
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public AbstractBalancer getBalancer() {
		return balancer;
	}

	public void setBalancer(AbstractBalancer balancer) {
		this.balancer = balancer;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
