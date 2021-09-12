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

	public AbstractEvent(Object originator, EventType et) {
		this.originator = originator;
		this.et = et;
	}
	
	public Object getOriginator() {
		return this.originator;
	}
	
	public EventType getEventType() {
		return this.et;
	}
}
