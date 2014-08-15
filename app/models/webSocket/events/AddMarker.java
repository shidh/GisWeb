package models.webSocket.events;

public class AddMarker extends Event {

	public final double latitude;
	public final double longitude;
	public final long poiId;

	public AddMarker(double latitude, double longitude, long poiId) {
		super("AddMarker");
		this.latitude = latitude;
		this.longitude = longitude;
		this.poiId = poiId;
	}

}
