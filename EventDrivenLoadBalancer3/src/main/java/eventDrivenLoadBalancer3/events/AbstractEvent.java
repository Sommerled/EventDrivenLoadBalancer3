package eventDrivenLoadBalancer3.events;

/**
 * All events need to know who sent it and the 
 * type. Adding a type gets around using 
 * new extensions of <AbstractEvent> to describe
 * what the event is intended to convey in addition
 * to it's message.
 * 
 * @author User
 *
 */
public abstract class AbstractEvent {
	private final Object originator;
	private final EventType et;
	private final AbstractEvent nextEvent;

	public AbstractEvent(Object originator, EventType et, AbstractEvent nextEvent) {
		this.originator = originator;
		this.et = et;
		this.nextEvent = nextEvent;
	}
	
	public Object getOriginator() {
		return this.originator;
	}
	
	public EventType getEventType() {
		return this.et;
	}

	public AbstractEvent getNextEvent() {
		return nextEvent;
	}
}
