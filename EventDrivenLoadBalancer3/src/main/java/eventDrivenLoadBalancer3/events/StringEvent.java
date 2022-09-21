package eventDrivenLoadBalancer3.events;

/**
 * Intended as an ubiquitous String event
 * for any type of string message
 * 
 * @author User
 *
 */
public class StringEvent extends AbstractEvent{
	private final String msg;
	
	public StringEvent(Object originator, EventType et, AbstractEvent nextEvent, String msg) {
		super(originator, et, nextEvent);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
