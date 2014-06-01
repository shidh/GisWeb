package controllers;

import play.db.jpa.Blob;
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

	public static String poi(String poiId) throws IllegalArgumentException,
			IllegalAccessException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Field[] fields = Poi.class.getFields();

		String output = poiHtml(fields, poi);

		return output;
	}

	public static String poiDetails(String powerTag, String poiId)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object object = Poi.class.getField("powerTag" + powerTag).get(poi);
		Field[] fields = Class.forName("models.powerTags." + powerTag)
				.getFields();

		String output = poiHtml(fields, object);

		return output;
	}

	private static String poiHtml(Field[] fields, Object object)
			throws IllegalArgumentException, IllegalAccessException {
		String output = "";

		for (Field field : fields) {
			String name = field.getName();
			String type = field.getType().toString();

			if (!type.equals("interface java.util.List") && !name.equals("id")
					&& !name.equals("willBeSaved")
					&& !name.contains("powerTag")) {
				String value = "";
				if (object != null && field.get(object) != null) {
					value = field.get(object).toString();
				}
				output += "<p>";
				output += "<label>" + name + "</label>";
				output += "<input type='text' id='" + name + "' name='" + name
						+ "' value='" + value
						+ "' class='submitPoiDataField' readonly='true'>";
				output += "</p>";
			} else if (field.toString().equals(
					"public java.util.List models.Poi.photos")) {
				ArrayList list = new ArrayList((List) field.get(object));

				for (int i = 0; i < list.size(); i++) {
					output += "<img width='200px' src='/application/getpicture?position="
							+ i + "&id=" + ((Poi) object).id.toString() + "'/>";
				}
			}
		}

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