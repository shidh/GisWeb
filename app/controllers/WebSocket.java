package controllers;

import play.mvc.*;
import play.libs.F.*;
import play.mvc.Http.*;

import static play.libs.F.Matcher.*;
import models.webSocket.events.AddMarker;
import models.webSocket.events.DeleteMarker;
import models.webSocket.events.Event;

public class WebSocket extends WebSocketController {

	private final static ArchivedEventStream<Event> events = new ArchivedEventStream<Event>(
			100);

	public static void join() {

		// Socket connected, join the event stream
		EventStream<Event> eventStream = events.eventStream();

		// Loop while the socket is open
		while (inbound.isOpen()) {

			// Wait for an event (either something coming on the inbound socket
			// channel, or eventStream messages)
			Either<WebSocketEvent, Event> e = await(Promise.waitEither(
					inbound.nextEvent(), eventStream.nextEvent()));

			// Case: Adding marker
			for (AddMarker event : ClassOf(AddMarker.class).match(e._2)) {
				outbound.send("%s:%s:%s:%s", event.type, event.poiId,
						event.latitude, event.longitude);
			}

			// Case: Deleting marker
			for (DeleteMarker event : ClassOf(DeleteMarker.class).match(e._2)) {
				outbound.send("%s:%s:null:null", event.type, event.poiId);
			}
		}
	}

	static void publishAddMarkerEvent(double latitude, double longitude,
			long poiId) {
		events.publish(new AddMarker(latitude, longitude, poiId));
	}

	static void publishDeleteMarkerEvent(long poiId) {
		events.publish(new DeleteMarker(poiId));
	}
}
