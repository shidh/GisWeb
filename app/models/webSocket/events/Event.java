package models.webSocket.events;

public abstract class Event {

	public final String eventType;

	public Event(String eventType) {
		this.eventType = eventType;
	}

}
