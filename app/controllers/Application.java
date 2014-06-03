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
import models.powerTags.Generator;
import models.powerTags.Generator.MethodEnum;
import models.powerTags.Generator.TypeEnum;
import models.powerTags.Generator.MethodEnum.*;
import models.powerTags.Generator.SourceEnum;
import models.powerTags.Generator.SourceEnum.*;

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
			IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Field[] fields = Poi.class.getFields();
		return getHtmlCode(fields, poi, "", "");
	}

	public static String poiDetails(String powerTag, String poiId)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object object = Poi.class.getField("powerTag" + powerTag).get(poi);
		Field[] fields = Class.forName("models.powerTags." + powerTag)
				.getFields();
		return getHtmlCode(fields, object, "", "");
	}

	public static String generatorMethod(String source, String poiId)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object object = Poi.class.getField("powerTagGenerator").get(poi);
		Field[] fields = {Class.forName("models.powerTags.Generator")
				.getField("method")};
		return getHtmlCode(fields, object, source, "");
	}

	public static String generatorType(String source, String method, String poiId)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object object = Poi.class.getField("powerTagGenerator").get(poi);
		Field[] fields = {Class.forName("models.powerTags.Generator")
				.getField("type")};
		return getHtmlCode(fields, object, source, method);
	}

	private static String getHtmlCode(Field[] fields, Object object, String source, String method)
			throws IllegalArgumentException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException,
			InvocationTargetException {
		String output = "";

		for (Field field : fields) {
			String name = field.getName();
			String type = field.getType().toString();

			if (!type.equals("interface java.util.List") && !name.equals("id")
					&& !name.equals("willBeSaved")
					&& !name.contains("powerTag")) {
				
				String select = "<label for='" + name + "'>" + name + "</label>";
				select += "<select name='" + name + "' id='" + name + "'>";
				select += "<option value=''></option>";

				if (type.contains("Generator$MethodEnum") && fields.length == 1) {
					MethodEnum[] methods = Generator.getMethods(SourceEnum.valueOf(source));
					output += select;

					for (MethodEnum methodEnum: methods) {
						output += "<option value='" + methodEnum.name() + "'>"
								+ methodEnum.name + "</option>";
					}
					output += "</select>";
				} else if (type.contains("Generator$TypeEnum") && fields.length == 1) {
					TypeEnum[] types = Generator.getTypes(SourceEnum.valueOf(source), MethodEnum.valueOf(method));
					output += select;

					for (TypeEnum typeEnum: types) {
						output += "<option value='" + typeEnum.name() + "'>"
								+ typeEnum.name + "</option>";
					}
					output += "</select>";
				} else if (type.contains("Enum") && !type.contains("Generator$MethodEnum") && !type.contains("Generator$TypeEnum")) {
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
				} else if (!type.contains("Generator$MethodEnum") && !type.contains("Generator$TypeEnum")) {
					String value = "";

					if (object != null && field.get(object) != null) {
						value = field.get(object).toString();
					}
					output += "<p>";
					output += "<label for='" + name + "'>" + name + "</label>";
					output += "<input type='text' id='" + name + "' name='"
							+ name + "' value='" + value
							+ "' class='submitPoiDataField' readonly='true'>";
					output += "</p>";
				}
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