package eventDrivenLoadBalancer3.eventHandler;

import java.util.concurrent.BlockingQueue;

import eventDrivenLoadBalancer3.events.AbstractEvent;

/**
 * This object stores events in a <LinkedBlockingQueue> 
 * until they are removed by services that are listening 
 * for events. I used an IOC philosophy on this to make
 * it easier to test.
 *  
 * @author User
 *
 */
public class EventHandler implements EventDispatcher, EventListener {
	private BlockingQueue<AbstractEvent> eventQueue = null;
	
	public EventHandler() {
		
	}

	@Override
	public AbstractEvent peek() throws InterruptedException {
		if(this.eventQueue == null) {
			throw new RuntimeException("eventQueue not set");
		}
		
		AbstractEvent e = null;
		
		while(e == null){
			while(size() == 0){
				synchronized(this){
					wait();
				}
			}
			e = this.eventQueue.peek();
		}
		
		return e;
	}

	@Override
	public boolean remove(AbstractEvent e) {
		if(this.eventQueue == null) {
			throw new RuntimeException("eventQueue not set");
		}

		return this.eventQueue.remove(e);
	}

	@Override
	public void put(AbstractEvent e) throws InterruptedException {
		if(this.eventQueue == null) {
			throw new RuntimeException("eventQueue not set");
		}
		
		this.eventQueue.put(e);
		synchronized(this){
			this.notifyAll();
		}
	}

	public BlockingQueue<AbstractEvent> getEventQueue() {
		return eventQueue;
	}

	public void setEventQueue(BlockingQueue<AbstractEvent> eventQueue) {
		this.eventQueue = eventQueue;
	}
	
	/**
	 * Returns the number of events on the
	 * <LinkedBlockingQueue>
	 * @return
	 */
	public synchronized int size(){
		if(this.eventQueue == null) {
			throw new RuntimeException("eventQueue not set");
		}
		
		return this.eventQueue.size();
	}

}
