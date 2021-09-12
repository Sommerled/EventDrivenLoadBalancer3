package eventDrivenLoadBalancer3.eventHandler;

import eventDrivenLoadBalancer3.events.AbstractEvent;

/**
 * The interface for creating dispatchers
 * for placing events on the event queue
 */
public interface EventDispatcher {
	/**
	 * Puts and event on the event queue
	 * @param e
	 * @throws InterruptedException
	 */
	public void put(AbstractEvent e) throws InterruptedException;
}
