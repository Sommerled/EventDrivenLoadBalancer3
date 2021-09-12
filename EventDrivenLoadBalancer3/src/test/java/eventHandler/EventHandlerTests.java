package eventHandler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;

import eventDrivenLoadBalancer3.eventHandler.EventHandler;
import eventDrivenLoadBalancer3.events.AbstractEvent;
import eventDrivenLoadBalancer3.events.StringEvent;

class EventHandlerTests {

	@Test
	public void Size3() {
		BlockingQueue<AbstractEvent> eventQueue = new LinkedBlockingQueue<AbstractEvent>();
		EventHandler eh = new EventHandler();
		
		eventQueue.add(new StringEvent(this, null, null));
		eventQueue.add(new StringEvent(this, null, null));
		eventQueue.add(new StringEvent(this, null, null));
		
		eh.setEventQueue(eventQueue);
		
		assertEquals(eh.size(), 3);
	}

	
	@Test
	public void SizeException() {
		EventHandler eh = new EventHandler();
		RuntimeException e = assertThrows(RuntimeException.class, ()->{
			eh.size();
		});
		
		assertEquals(e.getMessage(), "eventQueue not set");
	}
	
	@Test
	public void put1() throws InterruptedException {
		BlockingQueue<AbstractEvent> eventQueue = new LinkedBlockingQueue<AbstractEvent>();
		EventHandler eh = new EventHandler();

		eh.setEventQueue(eventQueue);
		
		eh.put(new StringEvent(this, null, "hello world"));
		assertEquals(eh.size(), 1);
		
		AbstractEvent ae = eh.peek();
		assertTrue(ae instanceof StringEvent);
		
		StringEvent se = (StringEvent) ae;
		assertEquals(se.getMsg(), "hello world");
	}
	
	@Test
	public void putException() throws InterruptedException {
		EventHandler eh = new EventHandler();
		
		RuntimeException e = assertThrows(RuntimeException.class, ()->{
			eh.put(new StringEvent(this, null, "hello world"));
		});
	}
	
	@Test
	public void remove1Pass() throws InterruptedException {
		BlockingQueue<AbstractEvent> eventQueue = new LinkedBlockingQueue<AbstractEvent>();
		EventHandler eh = new EventHandler();

		eh.setEventQueue(eventQueue);
		StringEvent se = new StringEvent(this, null, "hello world");
		eh.put(se);
		
		assertTrue(eh.remove(se));
	}
	
	@Test
	public void remove1FailEventNotAdded() {
		BlockingQueue<AbstractEvent> eventQueue = new LinkedBlockingQueue<AbstractEvent>();
		EventHandler eh = new EventHandler();
		
		eh.setEventQueue(eventQueue);
		StringEvent se = new StringEvent(this, null, "hello world");
		
		assertFalse(eh.remove(se));
	}
	
	@Test
	public void remove1FailException() {
		EventHandler eh = new EventHandler();
		StringEvent se = new StringEvent(this, null, "hello world");
		
		RuntimeException e = assertThrows(RuntimeException.class, ()->{
			eh.remove(se);
		});
	}
}
