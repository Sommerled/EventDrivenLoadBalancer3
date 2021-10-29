package eventDrivenLoadBalancer3.Balancer;

import eventDrivenLoadBalancer3.events.AbstractEvent;
import eventDrivenLoadBalancer3.events.ConfigEvent;
import eventDrivenLoadBalancer3.events.EventType;
import eventDrivenLoadBalancer3.events.SocketConfigEvent;
import eventDrivenLoadBalancer3.events.SocketEvent;
import eventDrivenLoadBalancer3.server.Config;
import eventDrivenLoadBalancer3.server.Service;

public abstract class AbstractBalancer extends Service implements Balancer {

	public AbstractBalancer() {
		super();
	}

	@Override
	public void run() {
		while(true) {
			try {
				AbstractEvent ae = this.getEventListener().peek();
				switch(ae.getEventType()){
					case NEW_CONNECTION:
						this.getEventListener().remove(ae);
						ConfigEvent ce =  (ConfigEvent) ae;
						
						//Add all child configs to load balance list
						if(ce.getConfig().getChildConfig() != null && 
								ce.getConfig().getChildConfig().size() > 0) {
							for(Config c : ce.getConfig().getChildConfig()) {
								this.registerConfig(c);
							}
						}
						
						/*
						 * If listening then start listening on the config's socket
						 * else add to list of configs that need to be load balanced
						 */
						if(ce.getConfig().isListening()) {
							ConfigEvent createWorker = new ConfigEvent(this, EventType.NEW_SERVER_SOCKET_SERVICE, ce.getConfig());
							this.getEventDispatcher().put(createWorker);
						}else {
							this.registerConfig(ce.getConfig());
						}
						
						break;
					case BALANCE_REQUEST:
						this.getEventListener().remove(ae);
						SocketEvent se = (SocketEvent) ae;
						Config next = this.nextConfig();
						
						SocketConfigEvent sce = new SocketConfigEvent(this, EventType.NEW_CLIENT_SOCKET_SERVICE, se.getSocket(), next);
						this.getEventDispatcher().put(sce);
						break;
					case SHUTDOWN:
						
						break;
					default:
						break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
