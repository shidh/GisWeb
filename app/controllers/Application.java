package controllers;

import play.*;
import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import models.*;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void savePOI(String accuracy, String altitude,
			String bearing, String latitude, String longitude, String provider,
			String time) {
		Logger.info("content type: %s", request.contentType);
		Logger.info("json accuracy: %s", accuracy);
		Logger.info("json altitude: %s", altitude);
		Logger.info("json bearing: %s", bearing);
		Logger.info("json latitude: %s", latitude);
		Logger.info("json longitude: %s", longitude);
		Logger.info("json provider: %s", provider);
		Logger.info("json time: %s", time);

		Poi poi = new Poi();
		poi.photos = new ArrayList<Blob>();

		poi.accuracy = Float.parseFloat(accuracy);
		poi.altitude = Double.parseDouble(altitude);
		poi.bearing = Float.parseFloat(bearing);
		poi.latitude = Double.parseDouble(latitude);
		poi.longitude = Double.parseDouble(longitude);
		poi.provider = provider;
		poi.time = Long.parseLong(time);
		poi.save();

		renderText(String.valueOf(poi.id));
		index();
	}

	public static void savePicture(String id, File image) {
		Poi poi = Poi.findById(Long.valueOf(id));
		try {
			Logger.info("content type: %s", request.contentType);
			Blob blob = new Blob();
			blob.set(new FileInputStream(image),
					MimeTypes.getContentType(image.getName()));
			poi.photos.add(blob);
			poi.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getPicture(long id, int position) {
		Logger.info("asfd content type: %s", request.contentType);
		Poi poi = Poi.findById(id);
		response.setContentTypeIfNotSet(poi.photos.get(position).type());
		renderBinary(poi.photos.get(position).get());
	}
}