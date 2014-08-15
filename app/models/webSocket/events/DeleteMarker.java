package models.webSocket.events;

public class DeleteMarker extends Event {

	public final long poiId;

	public DeleteMarker(long poiId) {
		super("DeleteMarker");
		this.poiId = poiId;
	}

}
