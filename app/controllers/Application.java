package controllers;

import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import models.*;

public class Application extends Controller {

	private static final String audience = "889611969164-ujvohn299csu833avfmcsun3k6fna30s.apps.googleusercontent.com";
	private static final String[] clientId = new String[] { "889611969164-hhapbnd498ntbuulf3u7m2prba7cpu29.apps.googleusercontent.com" };

	public static void createPoi(String token) throws FileNotFoundException {

		Checker checker = new Checker(clientId, audience);
		Payload payload = checker.check(token);

		if (payload != null) {
			Poi poi = new Poi();
			poi.locationTrace = new ArrayList<LocationTrace>();
			poi.photos = new ArrayList<Photo>();
			int index = 0;
			while (true) {
				File photoFile = params.get("photo_" + index + "_file",
						File.class);
				if (photoFile == null) {
					break;
				} else {
					Photo photo = new Photo();
					photo.accuracy = params.get("photo_" + index + "_accuracy",
							Float.class);
					photo.altitude = params.get("photo_" + index + "_altitude",
							Double.class);
					photo.bearing = params.get("photo_" + index + "_bearing",
							Float.class);
					photo.latitude = params.get("photo_" + index + "_latitude",
							Double.class);
					photo.longitude = params.get("photo_" + index
							+ "_longitude", Double.class);
					photo.provider = params.get("photo_" + index + "_provider",
							String.class);
					photo.time = params.get("photo_" + index + "_time",
							Long.class);
					Blob photoBlob = new Blob();
					String photoName = photoFile.getName();
					photoBlob.set(new FileInputStream(photoFile),
							MimeTypes.getContentType(photoName));
					photo.photoBlob = photoBlob;
					photo.poi = poi;
					poi.photos.add(photo);
					index++;
				}
			}
			if (!poi.photos.isEmpty()) {
				index = 0;
				while (true) {
					LocationTrace trace = new LocationTrace();
					trace.latitude = params.get("trace_" + index + "_latitude",
							Double.class);
					if (trace.latitude == null) {
						break;
					}

					trace.accuracy = params.get("trace_" + index + "_accuracy",
							Float.class);
					trace.altitude = params.get("trace_" + index + "_altitude",
							Double.class);
					trace.bearing = params.get("trace_" + index + "_bearing",
							Float.class);
					trace.longitude = params.get("trace_" + index
							+ "_longitude", Double.class);
					trace.provider = params.get("trace_" + index + "_provider",
							String.class);
					trace.time = params.get("trace_" + index + "_time",
							Long.class);
					trace.poi = poi;
					poi.locationTrace.add(trace);
					index++;
				}

				poi.save();
				WebSocket.publishAddMarkerEvent(poi.derivePoiLatitude(),
						poi.derivePoiLongitude(), poi.id);
				ok();
			} else {
				badRequest();
			}
		} else {
			unauthorized();
		}
	}

	public static void index() {
		render();
	}

}