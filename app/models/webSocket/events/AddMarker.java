package models.webSocket.events;

public class AddMarker extends Event {

	public final String googleId;
	public final double latitude;
	public final double longitude;
	public final long poiId;
	public final boolean taskCompleted;

	public AddMarker(String googleId, double latitude, double longitude, long poiId, boolean taskCompleted) {
		super("AddMarker");
		this.googleId = googleId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.poiId = poiId;
		this.taskCompleted = taskCompleted;
	}

}
