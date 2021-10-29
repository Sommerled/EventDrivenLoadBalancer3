package eventDrivenLoadBalancer3.server;

import eventDrivenLoadBalancer3.eventHandler.EventDispatcher;
import eventDrivenLoadBalancer3.eventHandler.EventDispatcherAware;
import eventDrivenLoadBalancer3.eventHandler.EventListener;
import eventDrivenLoadBalancer3.eventHandler.EventListenerAware;

/**
 * Foundational class for building services
 * internal to the load balancer
 */
public abstract class Service implements EventDispatcherAware, EventListenerAware, Runnable {
	private EventListener listener = null;
	private EventDispatcher dispatcher = null;

	public Service(){
	}
	
	/**
	 * Retrieve the listener
	 */
	@Override
	public EventListener getEventListener() {
		return this.listener;
	}

	/**
	 * Set the listener
	 */
	@Override
	public void setEventListener(EventListener eventListener) {
		this.listener = eventListener;
	}

	/**
	 * Retrieve the dispatcher that sends
	 * events to the event queue
	 */
	@Override
	public EventDispatcher getEventDispatcher() {
		return this.dispatcher;
	}

	/**
	 * Sets the dispatcher
	 */
	@Override
	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.dispatcher = eventDispatcher;
	}
}
