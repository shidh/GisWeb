package controllers;

import play.*;
import play.db.jpa.Blob;
import play.db.jpa.GenericModel;
import play.libs.MimeTypes;
import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;

import models.*;

public class Application extends Controller {

	public static void createPOI(String accuracy, String altitude,
			String bearing, String latitude, String longitude, String provider,
			String time) {
		Poi poi = new Poi();
		poi.photos = new ArrayList<Blob>();
		poi.setPoi(accuracy, altitude, bearing, latitude, longitude, provider,
				time);
		poi.save();

		renderText(String.valueOf(poi.id));
		index();
	}

	public static void editPoi(String poiId) {
		renderTemplate("/Application/poiDetails.html", poiId);
	}

	public static void getPicture(long id, int position) {
		Poi poi = Poi.findById(id);
		response.setContentTypeIfNotSet(poi.photos.get(position).type());
		renderBinary(poi.photos.get(position).get());
	}

	public static void index() {
		render();
	}

	public static String poi(String poiId) {
		return poiHtml("models.Poi", poiId);
	}

	public static String poiDetails(String powerTag, String poiId) {
		return poiHtml("models.powerTags." + powerTag, poiId);
	}

	public static String poiHtml(String className, String poiId) {
		String output = "";

		try {
			Field[] fields = Class.forName(className).getFields();
			Poi poi = Poi.findById(Long.valueOf(poiId));

			for (Field field : fields) {
				String type = field.getType().toString();

				if (!type.equals("interface java.util.List")) {
					String name = field.getName();
					String value = "";

					if (className.equals("models.Poi")) {
						value = field.get(poi).toString();
					}

					output += "<p>";
					output += "<label>" + name + "</label>";
					output += "<input type='text' id='" + name + "' name='"
							+ name + "' value='" + value
							+ "' class='submitPoiDataField' readonly='true'>";
					output += "</p>";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(output);
		return output;
	}

	public static void savePicture(String id, File image) {
		Poi poi = Poi.findById(Long.valueOf(id));
		try {
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

	public static void updatePoi(String accuracy, String altitude,
			String bearing, String id, String latitude, String longitude,
			String provider, String time) {
		Poi poi = Poi.findById(Long.valueOf(id));
		poi.setPoi(accuracy, altitude, bearing, latitude, longitude, provider,
				time);
		poi.save();
		index();
	}

}