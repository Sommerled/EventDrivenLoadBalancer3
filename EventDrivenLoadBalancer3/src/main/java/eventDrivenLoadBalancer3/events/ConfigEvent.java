package eventDrivenLoadBalancer3.events;

import eventDrivenLoadBalancer3.server.Config;

public class ConfigEvent extends AbstractEvent {
	private final Config config;
	
	public ConfigEvent(Object originator, EventType et, Config config) {
		super(originator, et);
		
		this.config = config;	
	}

	public Config getConfig() {
		return config;
	}

}
