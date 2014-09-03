package controllers;

import play.mvc.*;
import play.libs.F.*;
import play.mvc.Http.*;

import static play.libs.F.Matcher.*;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import models.GoogleUser;
import models.Poi;
import models.webSocket.events.AddMarker;
import models.webSocket.events.DeleteMarker;
import models.webSocket.events.Event;

public class WebSocket extends WebSocketController {

	private final static ArchivedEventStream<Event> events = new ArchivedEventStream<Event>(
			100);

	public static void join(String gToken) {

		// Socket connected, join the event stream
		EventStream<Event> eventStream = events.eventStream();

		GoogleUser googleUser = Application.getGoogleUser(gToken);

		// Loop while the socket is open
		while (inbound.isOpen()) {

			// Wait for an event (either something coming on the inbound socket
			// channel, or eventStream messages)
			Either<WebSocketEvent, Event> e = await(Promise.waitEither(
					inbound.nextEvent(), eventStream.nextEvent()));

			// Case: Adding marker
			for (AddMarker event : ClassOf(AddMarker.class).match(e._2)) {
				outbound.send("%s:%s:%s:%s:%s:%s", event.eventType,
						event.googleId, event.latitude, event.longitude,
						event.poiId, event.taskCompleted);
			}

			// Case: Deleting marker
			for (DeleteMarker event : ClassOf(DeleteMarker.class).match(e._2)) {
				outbound.send("%s:null:null:null:%s:null", event.eventType,
						event.poiId);
			}

			// Case: The socket has been closed
			for (WebSocketClose closed : SocketClosed.match(e._1)) {

				if (googleUser != null) {
					long googleUserId = googleUser.id;
					googleUser = GoogleUser.findById(googleUserId);
					Poi poi = googleUser.poi;

					if (googleUser.poi != null) {
						WebSocket.publishAddMarkerEvent("null", poi.latitude,
								poi.longitude, poi.id, poi.taskCompleted);
						googleUser.poi = null;
						googleUser.save();
					}
				}
				disconnect();
			}
		}
	}

	static void publishAddMarkerEvent(String googleId, double latitude,
			double longitude, long poiId, boolean taskCompleted) {
		events.publish(new AddMarker(googleId, latitude, longitude, poiId,
				taskCompleted));
	}

	static void publishDeleteMarkerEvent(long poiId) {
		events.publish(new DeleteMarker(poiId));
	}
}
