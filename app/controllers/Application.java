package controllers;

import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import models.*;
import models.powerTags.*;
import models.powerTags.Generator.MethodEnum;
import models.powerTags.Generator.TypeEnum;
import models.powerTags.Generator.SourceEnum;

public class Application extends Controller {

	public static void createPOI(String accuracy, String altitude,
			String bearing, String latitude, String longitude, String provider,
			String time) {
		Poi poi = new Poi(accuracy, altitude, bearing, latitude, longitude,
				provider, time);
		poi.save();
		renderText(String.valueOf(poi.id));
		index();
	}

	public static void editPoi(String poiId) {
		renderTemplate("/Application/poi.html", poiId);
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
			IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException,
			InvocationTargetException, NoSuchFieldException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Field[] fields = Poi.class.getFields();
		return getHtmlCode(fields, poi, "", "");
	}

	public static String powerTagDetails(String powerTag, String poiId)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag.getClass() != Class.forName("models.powerTags."
				+ powerTag)) {
			poiPowerTag = null;
		}

		Field[] fields = Class.forName("models.powerTags." + powerTag)
				.getFields();
		return getHtmlCode(fields, poiPowerTag, "", "");
	}

	public static String getMethodHtmlCode(String source, String poiId)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag.getClass() != Generator.class) {
			poiPowerTag = null;
		}

		Field[] fields = { Generator.class.getField("method") };
		return getHtmlCode(fields, poiPowerTag, source, "");
	}

	public static String getTypeHtmlCode(String source, String method,
			String poiId) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException,
			ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag.getClass() != Generator.class) {
			poiPowerTag = null;
		}

		Field[] fields = { Generator.class.getField("type") };
		return getHtmlCode(fields, poiPowerTag, source, method);
	}

	private static String getHtmlCode(Field[] fields, Object poiPowerTag,
			String source, String method) throws IllegalArgumentException,
			IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException,
			InvocationTargetException, NoSuchFieldException {
		String output = "";

		for (Field field : fields) {
			String name = field.getName();
			String type = field.getType().toString();

			if (!type.equals("interface java.util.List") && !name.equals("id")
					&& !name.equals("willBeSaved")
					&& !name.contains("powerTag")) {

				String select = "<label for='" + name + "'>" + name
						+ "</label>";
				select += "<select name='" + name + "' id='" + name + "' class='submitPoiDataField'>";
				select += "<option value=''> </option>";

				if (type.contains("Generator$MethodEnum") && fields.length == 1) {
					MethodEnum[] methods = Generator.getMethods(SourceEnum
							.valueOf(source));
					output += select;

					if (methods.length > 1) {
						for (MethodEnum methodEnum : methods) {
							output += "<option value='" + methodEnum.name()
									+ "'>" + methodEnum.name + "</option>";
						}
						output += "</select>";
					} else if (methods.length == 1) {
						output = "<p>" + name + ": " + methods[0].name + "</p>";
					} else {
						output = "";
					}
				} else if (type.contains("Generator$TypeEnum")
						&& fields.length == 1) {
					TypeEnum[] types = Generator.getTypes(
							SourceEnum.valueOf(source),
							MethodEnum.valueOf(method));
					output += select;

					if (types.length > 1) {
						for (TypeEnum typeEnum : types) {
							output += "<option value='" + typeEnum.name()
									+ "'>" + typeEnum.name + "</option>";
						}
						output += "</select>";
					} else if (types.length == 1) {
						output = "<p>" + name + ": " + types[0].name + "</p>";
					} else {
						output = "";
					}
				} else if (type.contains("Enum")
						&& !type.contains("Generator$MethodEnum")
						&& !type.contains("Generator$TypeEnum")) {
					Class enumClass = Class.forName(type.replace("class ", ""));
					Object[] enumConstants = enumClass.getEnumConstants();
					Method getName = enumClass.getMethod("getName");
					Method getType = enumClass.getMethod("name");
					output += select;

					for (Object enumConstant : enumConstants) {
						String enumName = (String) getName.invoke(enumConstant);
						String enumType = (String) getType.invoke(enumConstant);
						output += "<option value='" + enumType + "'>"
								+ enumName + "</option>";
					}
					output += "</select>";

					if (type.contains("Generator$SourceEnum")) {
						output += "<div id='generatorMethod'></div>";
						output += "<div id='generatorType'></div>";
					}
				} else if (!type.contains("Generator$MethodEnum")
						&& !type.contains("Generator$TypeEnum")
						&& !type.contains("class models.Poi")) {
					String value = "";

					if (poiPowerTag != null && field.get(poiPowerTag) != null) {
						value = field.get(poiPowerTag).toString();
					}

					output += "<p>";
					output += "<label for='" + name + "'>" + name + "</label>";
					output += "<input type='text' id='" + name + "' name='"
							+ name + "' value='" + value
							+ "' class='submitPoiDataField' readonly>";
					output += "</p>";
				}
			} else if (field.toString().equals(
					"public java.util.List models.Poi.photos")) {
				ArrayList list = new ArrayList((List) field.get(poiPowerTag));

				for (int i = 0; i < list.size(); i++) {
					output += "<img width='200' src='/application/getpicture?position="
							+ i
							+ "&id="
							+ ((Poi) poiPowerTag).id.toString()
							+ "' alt='image of poi with id " + ((Poi) poiPowerTag).id.toString() + " on position " + i + "'/>";
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
		poi.accuracy = Float.parseFloat(accuracy);
		poi.altitude = Double.parseDouble(altitude);
		poi.bearing = Float.parseFloat(bearing);
		poi.latitude = Double.parseDouble(latitude);
		poi.longitude = Double.parseDouble(longitude);
		poi.provider = provider;
		poi.time = Long.parseLong(time);
		poi.save();
		index();
	}

}