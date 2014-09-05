package controllers;

import java.util.List;

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
				if (outbound.isOpen()) {
					outbound.send("%s:%s:%s:%s:%s:%s:%s", event.eventType,
							event.googleId, event.latitude, event.longitude,
							event.poiId, event.taskCompleted, event.timeStamp);
				}
			}

			// Case: Deleting marker
			for (DeleteMarker event : ClassOf(DeleteMarker.class).match(e._2)) {
				if (outbound.isOpen()) {
					outbound.send("%s:null:null:null:%s:null:null", event.eventType,
							event.poiId);
				}
			}

			// Case: The socket has been closed
			for (WebSocketClose closed : SocketClosed.match(e._1)) {

				if (googleUser != null) {
					List<Poi> pois = Poi.find("byGoogleUser",
							GoogleUser.findById(googleUser.id)).fetch();

					for (Poi poi : pois) {

						if (poi != null) {
							WebSocket.publishAddMarkerEvent(null, poi);
							poi.googleUser = null;
							poi.store();
						}
					}
				}
				disconnect();
			}
		}
	}

	static void publishAddMarkerEvent(GoogleUser googleUser, Poi poi) {
		String googleId = "null";
		if (googleUser != null) {
			googleId = googleUser.googleId;
		}
		events.publish(new AddMarker(googleId, poi.latitude, poi.longitude,
				poi.id, poi.taskCompleted, poi.timeStamp));
	}

	static void publishDeleteMarkerEvent(long poiId) {
		events.publish(new DeleteMarker(poiId));
	}
}
