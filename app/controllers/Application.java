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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import models.*;
import models.powerTags.*;
import models.powerTags.Generator.MethodEnum;
import models.powerTags.Generator.TypeEnum;
import models.powerTags.Generator.SourceEnum;
import models.types.Operator;

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

	private static String getHtmlCode(Field[] fields, Object objectWithValues,
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

				String select = "<label for='" + name + "'>" + name.replace("_", " ").replace("T", " t")
						+ "</label>";
				select += "<select name='" + name + "' id='" + name
						+ "' class='submitPoiDataField'>";
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

					if (objectWithValues != null
							&& field.get(objectWithValues) != null) {
						value = field.get(objectWithValues).toString();
					}

					if (type.equals("class models.types.Operator")) {
						Object operator = null;
						if (objectWithValues != null) {
							operator = field.get(objectWithValues);
						}
						output += "<p>Operator START</p>";
						output += getHtmlCode(Operator.class.getFields(),
								operator, source, method);
						output += "<p>Operator END</p>";
					} else {
						String lowerCaseType = type.toLowerCase();
						String parsleyValidator = "";
						if (lowerCaseType.equals("byte") || 
								lowerCaseType.equals("double") || 
								lowerCaseType.equals("class java.lang.float") || 
								lowerCaseType.equals("float") || 
								lowerCaseType.equals("int") || 
								lowerCaseType.equals("long")) {
							parsleyValidator="data-parsley-type='number'";
						}
						output += "<p>";
						output += "<label for='" + name + "'>" + name.replace("_", " ").replace("T", " t")
								+ "</label>";
						if (name.toLowerCase().contains("time") || name.toLowerCase().contains("date")) {
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							String date;
							try {
								date = dateFormat.format(new Date(Long.parseLong(value))).toString();
							} catch (Exception e) {
								date = dateFormat.format(new Date()).toString();
							}
							String id = "";
							if (objectWithValues != null && objectWithValues.getClass() == Poi.class) {
								id = "poiTime";
							} else {
								id = "powerTagTime";
							}
							output += "<input id='" + id + "' type='text' value='" + date + "'/>";
						} else {
						output += "<input type='text' " + parsleyValidator + " id='" + name + "' name='"
								+ name + "' value='" + value
								+ "' class='submitPoiDataField'/>";
						}
						output += "</p>";
					}
				}
			} else if (field.toString().equals(
					"public java.util.List models.Poi.photos")) {
				ArrayList list = new ArrayList(
						(List) field.get(objectWithValues));

				for (int i = 0; i < list.size(); i++) {
					output += "<img width='200' src='/application/getpicture?position="
							+ i
							+ "&id="
							+ ((Poi) objectWithValues).id.toString()
							+ "' alt='image of poi with id "
							+ ((Poi) objectWithValues).id.toString()
							+ " on position " + i + "'/>";
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