package eventDrivenLoadBalancer3.Balancer;

import eventDrivenLoadBalancer3.server.Config;

public interface Balancer {
	
	/**
	 * Retrieves the next config to be used
	 * 
	 * @return
	 */
	public Config nextConfig();
	
	/**
	 * Registers a configuration with the balancer
	 * 
	 * @param c
	 */
	public void registerConfig(Config c);
}
