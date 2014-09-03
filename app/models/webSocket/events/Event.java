package models.webSocket.events;

public abstract class Event {

	public final String eventType;
	public final Long timestamp;

	public Event(String eventType) {
		this.eventType = eventType;
		this.timestamp = System.currentTimeMillis();
	}

}
