package models.webSocket.events;

public class UpdateMarker extends Event {

	public final double latitude;
	public final double longitude;
	public final long poiId;

	public UpdateMarker(double latitude, double longitude, long poiId) {
		super("UpdateMarker");
		this.latitude = latitude;
		this.longitude = longitude;
		this.poiId = poiId;
	}

}
