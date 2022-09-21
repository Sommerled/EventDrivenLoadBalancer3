package eventDrivenLoadBalancer3.Balancer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eventDrivenLoadBalancer3.server.Config;

public class RoundRobinBalancer extends AbstractBalancer {
	private List<Config> configList;
	private Integer currentIndex;
	
	public RoundRobinBalancer() {
		super();
		this.configList = new ArrayList<Config>();
		this.currentIndex = 0;
	}

	@Override
	public Config nextConfig() {
		Config next = null;
		
		if(this.currentIndex.equals(this.configList.size())) {
			this.currentIndex = 0;
		}
		
		if(this.configList.size() > 0) {
			next = this.configList.get(this.currentIndex);
			this.currentIndex++;
		}
		
		return next;
	}

	@Override
	public void registerConfig(Config c) {
		Iterator<Config> iter = this.configList.iterator();
		boolean exists = false;
		
		while(iter.hasNext()) {
			Config config = iter.next();
			
			if(config.sameConnection(c)) {
				
				/*
				 * configs are the same, but may have different 
				 * encryption algorithms or something else.
				 * If so remove existing config so that it
				 * can be replaced. 
				 */
				if(!config.equals(c)) {
					this.configList.remove(config);
				}else {
					exists = true;
				}
				
				break;
			}
		}
		
		if(!exists) {
			this.configList.add(c);
		}
		
	}

}
