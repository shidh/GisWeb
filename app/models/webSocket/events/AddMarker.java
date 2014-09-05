package models.webSocket.events;

public class AddMarker extends Event {

	public final String googleId;
	public final double latitude;
	public final double longitude;
	public final long poiId;
	public final boolean taskCompleted;
	public final long timeStamp;

	public AddMarker(String googleId, double latitude, double longitude, long poiId, boolean taskCompleted, long timeStamp) {
		super("AddMarker");
		this.googleId = googleId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.poiId = poiId;
		this.taskCompleted = taskCompleted;
		this.timeStamp = timeStamp;
	}

}
