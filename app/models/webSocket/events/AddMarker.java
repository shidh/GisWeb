package models.webSocket.events;

import models.Poi.TaskStatus;

public class AddMarker extends Event {

	public final String googleId;
	public final double latitude;
	public final double longitude;
	public final long poiId;
	public final TaskStatus taskStatus;
	public final long timeStamp;

	public AddMarker(String googleId, double latitude, double longitude, long poiId, TaskStatus taskStatus, long timeStamp) {
		super("AddMarker");
		this.googleId = googleId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.poiId = poiId;
		this.taskStatus = taskStatus;
		this.timeStamp = timeStamp;
	}

}
