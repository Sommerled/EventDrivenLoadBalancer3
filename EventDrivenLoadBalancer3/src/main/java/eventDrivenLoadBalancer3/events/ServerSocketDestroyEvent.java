package eventDrivenLoadBalancer3.events;

public class ServerSocketDestroyEvent extends AbstractEvent {
	private final Integer port;

	public ServerSocketDestroyEvent(Object originator, EventType et, AbstractEvent nextEvent,  Integer port) {
		super(originator, et, nextEvent);		
		this.port = port;
	}

	public Integer getPort() {
		return port;
	}

}
