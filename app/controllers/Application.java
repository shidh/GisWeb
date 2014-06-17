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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import models.*;
import models.powerTags.*;
import models.types.Operator;

public class Application extends Controller {

	public static void createPoi(String accuracy, String altitude, String bearing, String latitude, String longitude, String provider, String time) {
		Poi poi = new Poi(accuracy, altitude, bearing, latitude, longitude, provider, time);
		poi.save();
		renderText(String.valueOf(poi.id));
		index();
	}

	public static void createPowerTagCable(String poiId, String powerTags_Cable_cables, String powerTags_Cable_circuits, String powerTags_Cable_location, String powerTags_Cable_name,
			String types_Operator_name, String types_Operator_type, String powerTags_Cable_ref, String powerTags_Cable_voltage) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Cable(poi);
		} else if (poi.powerTag.getClass() != Cable.class) {
			poi.powerTag.delete();
			poi.powerTag = new Cable(poi);
		}
		Cable cable = (Cable) poi.powerTag;
		cable.cables = powerTags_Cable_cables;
		cable.circuits = Byte.parseByte(powerTags_Cable_circuits);
		cable.location = Cable.LocationEnum.valueOf(powerTags_Cable_location);
		cable.name = powerTags_Cable_name;

		if (cable.operator == null) {
			cable.operator = new Operator();
		}
		cable.operator.name = types_Operator_name;
		cable.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		cable.operator.save();
		cable.ref = powerTags_Cable_ref;
		cable.voltage = Integer.parseInt(powerTags_Cable_voltage);
		poi.powerTag.save();
	}

	public static void createPowerTagCableDistributionCabinet(String poiId, String types_Operator_name, String types_Operator_type, String powerTags_CableDistributionCabinet_ref,
			String powerTags_CableDistributionCabinet_voltage) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new CableDistributionCabinet(poi);
		} else if (poi.powerTag.getClass() != CableDistributionCabinet.class) {
			poi.powerTag.delete();
			poi.powerTag = new CableDistributionCabinet(poi);
		}
		CableDistributionCabinet cableDistributionCabinet = (CableDistributionCabinet) poi.powerTag;

		if (cableDistributionCabinet.operator == null) {
			cableDistributionCabinet.operator = new Operator();
		}
		cableDistributionCabinet.operator.name = types_Operator_name;
		cableDistributionCabinet.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		cableDistributionCabinet.operator.save();
		cableDistributionCabinet.ref = powerTags_CableDistributionCabinet_ref;
		cableDistributionCabinet.voltage = Integer.parseInt(powerTags_CableDistributionCabinet_voltage);
		poi.powerTag.save();
	}

	public static void createPowerTagConverter(String poiId, String powerTags_Converter_converter, String powerTags_Converter_poles, String powerTags_Converter_rating,
			String powerTags_Converter_voltage) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Converter(poi);
		} else if (poi.powerTag.getClass() != Converter.class) {
			poi.powerTag.delete();
			poi.powerTag = new Converter(poi);
		}
		Converter converterObject = (Converter) poi.powerTag;
		converterObject.converter = Converter.ConverterEnum.valueOf(powerTags_Converter_converter);
		converterObject.poles = Byte.parseByte(powerTags_Converter_poles);
		converterObject.rating = Integer.parseInt(powerTags_Converter_rating);
		converterObject.voltage = Integer.parseInt(powerTags_Converter_voltage);
		poi.powerTag.save();
	}

	public static void createPowerTagGenerator(String poiId, String powerTags_Generator_name, String types_Operator_name, String types_Operator_type, String powerTags_Generator_output,
			String powerTags_Generator_plant, String powerTags_Generator_source, String powerTags_Generator_method, String powerTags_Generator_type) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Generator(poi);
		} else if (poi.powerTag.getClass() != Generator.class) {
			poi.powerTag.delete();
			poi.powerTag = new Generator(poi);
		}
		Generator generator = (Generator) poi.powerTag;
		generator.name = powerTags_Generator_name;

		if (generator.operator == null) {
			generator.operator = new Operator();
		}
		generator.operator.name = types_Operator_name;
		generator.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		generator.operator.save();
		generator.output = Generator.OutputEnum.valueOf(powerTags_Generator_output);
		generator.plant = Generator.PlantEnum.valueOf(powerTags_Generator_plant);
		generator.source = Generator.SourceEnum.valueOf(powerTags_Generator_source);
		poi.powerTag.save();
		generator.setMethod(Generator.MethodEnum.valueOf(powerTags_Generator_method));
		poi.powerTag.save();
		generator.setType(Generator.TypeEnum.valueOf(powerTags_Generator_type));
		poi.powerTag.save();
	}

	public static void createPowerTagLine(String poiId, String powerTags_Line_cables, String types_Operator_name, String types_Operator_type, String powerTags_Line_ref, String powerTags_Line_voltage,
			String powerTags_Line_wires) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Line(poi);
		} else if (poi.powerTag.getClass() != Line.class) {
			poi.powerTag.delete();
			poi.powerTag = new Line(poi);
		}
		Line line = (Line) poi.powerTag;
		line.cables = Byte.parseByte(powerTags_Line_cables);

		if (line.operator == null) {
			line.operator = new Operator();
		}
		line.operator.name = types_Operator_name;
		line.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		line.operator.save();
		line.ref = powerTags_Line_ref;
		line.voltage = Integer.parseInt(powerTags_Line_voltage);
		line.wires = Line.WiresEnum.valueOf(powerTags_Line_wires);
		poi.powerTag.save();
	}

	public static void createPowerTagMinorLine(String poiId, String powerTags_MinorLine_cables, String powerTags_MinorLine_name, String types_Operator_name, String types_Operator_type,
			String powerTags_MinorLine_ref, String powerTags_MinorLine_voltage) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new MinorLine(poi);
		} else if (poi.powerTag.getClass() != MinorLine.class) {
			poi.powerTag.delete();
			poi.powerTag = new MinorLine(poi);
		}
		MinorLine minorLine = (MinorLine) poi.powerTag;
		minorLine.cables = Byte.parseByte(powerTags_MinorLine_cables);
		minorLine.name = powerTags_MinorLine_name;

		if (minorLine.operator == null) {
			minorLine.operator = new Operator();
		}
		minorLine.operator.name = types_Operator_name;
		minorLine.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		minorLine.operator.save();
		minorLine.ref = powerTags_MinorLine_ref;
		minorLine.voltage = Integer.parseInt(powerTags_MinorLine_voltage);
		poi.powerTag.save();
	}

	public static void createPowerTagPlant(String poiId, String powerTags_Plant_landuse, String powerTags_Plant_name, String types_Operator_name, String types_Operator_type,
			String powerTags_Plant_output, String powerTags_Plant_start_date) throws ParseException {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Plant(poi);
		} else if (poi.powerTag.getClass() != Plant.class) {
			poi.powerTag.delete();
			poi.powerTag = new Plant(poi);
		}
		Plant plant = (Plant) poi.powerTag;
		plant.landuse = Plant.LanduseEnum.valueOf(powerTags_Plant_landuse);
		plant.name = powerTags_Plant_name;

		if (plant.operator == null) {
			plant.operator = new Operator();
		}
		plant.operator.name = types_Operator_name;
		plant.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		plant.operator.save();
		plant.output = Plant.OutputEnum.valueOf(powerTags_Plant_output);
		plant.start_date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(powerTags_Plant_start_date).getTime();
		poi.powerTag.save();
	}

	public static void createPowerTagPole(String poiId, String powerTags_Pole_ref) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Pole(poi);
		} else if (poi.powerTag.getClass() != Pole.class) {
			poi.powerTag.delete();
			poi.powerTag = new Pole(poi);
		}
		Pole pole = (Pole) poi.powerTag;
		pole.ref = powerTags_Pole_ref;
		poi.powerTag.save();
	}

	public static void createPowerTagSubstation(String poiId, String powerTags_Substation_gas_insulated, String powerTags_Substation_location, String powerTags_Substation_name,
			String types_Operator_name, String types_Operator_type, String powerTags_Substation_ref, String powerTags_Substation_substationType, String powerTags_Substation_voltage) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Substation(poi);
		} else if (poi.powerTag.getClass() != Substation.class) {
			poi.powerTag.delete();
			poi.powerTag = new Substation(poi);
		}
		Substation substation = (Substation) poi.powerTag;
		substation.gas_insulated = Boolean.valueOf(powerTags_Substation_gas_insulated);
		substation.location = Substation.LocationEnum.valueOf(powerTags_Substation_location);
		substation.name = powerTags_Substation_name;

		if (substation.operator == null) {
			substation.operator = new Operator();
		}
		substation.operator.name = types_Operator_name;
		substation.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		substation.operator.save();
		substation.ref = powerTags_Substation_ref;
		substation.substationType = Substation.SubstationTypeEnum.valueOf(powerTags_Substation_substationType);
		substation.voltage = Integer.parseInt(powerTags_Substation_voltage);
		poi.powerTag.save();
	}

	public static void createPowerTagSwitch(String poiId) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Switch(poi);
		} else if (poi.powerTag.getClass() != Switch.class) {
			poi.powerTag.delete();
			poi.powerTag = new Switch(poi);
		}
		poi.powerTag.save();
	}

	public static void createPowerTagTower(String poiId, String powerTags_Tower_color, String powerTags_Tower_design, String powerTags_Tower_height, String powerTags_Tower_material,
			String powerTags_Tower_ref, String powerTags_Tower_structure, String powerTags_Tower_type) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Tower(poi);
		} else if (poi.powerTag.getClass() != Tower.class) {
			poi.powerTag.delete();
			poi.powerTag = new Tower(poi);
		}
		Tower tower = (Tower) poi.powerTag;
		System.out.println(powerTags_Tower_color);
		tower.design = Tower.DesignEnum.valueOf(powerTags_Tower_design);
		tower.height = Integer.parseInt(powerTags_Tower_height);
		tower.material = Tower.MaterialEnum.valueOf(powerTags_Tower_material);
		tower.ref = powerTags_Tower_ref;
		tower.structure = Tower.StructureEnum.valueOf(powerTags_Tower_structure);
		tower.type = Tower.TypeEnum.valueOf(powerTags_Tower_type);
		poi.powerTag.save();
	}

	public static void createPowerTagTransformer(String poiId, String powerTags_Transformer_frequency, String powerTags_Transformer_location, String powerTags_Transformer_phases,
			String powerTags_Transformer_rating, String powerTags_Transformer_transformerType, String powerTags_Transformer_voltage) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag == null) {
			poi.powerTag = new Transformer(poi);
		} else if (poi.powerTag.getClass() != Transformer.class) {
			poi.powerTag.delete();
			poi.powerTag = new Transformer(poi);
		}
		Transformer transformer = (Transformer) poi.powerTag;
		transformer.frequency = Float.parseFloat(powerTags_Transformer_frequency);
		transformer.location = Transformer.LocationEnum.valueOf(powerTags_Transformer_location);
		transformer.phases = Integer.parseInt(powerTags_Transformer_phases);
		transformer.rating = Integer.parseInt(powerTags_Transformer_rating);
		transformer.transformerType = Transformer.TransformerEnum.valueOf(powerTags_Transformer_transformerType);
		transformer.voltage = Integer.parseInt(powerTags_Transformer_voltage);
		poi.powerTag.save();
	}

	public static void editPoi(String poiId) {
		Poi poi = Poi.findById(Long.parseLong(poiId));
		renderTemplate("/Application/poi.html", poi);
	}

	private static String getHtmlCode(Field[] objectFields, Object objectWithValues, String source, String method) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException, InvocationTargetException, NoSuchFieldException {

		StringBuffer output = new StringBuffer();
		String newLine = System.getProperty("line.separator");

		for (Field field : objectFields) {

			/*
			 * Example: fieldName="photos","accuracy","altitude","latitude",...;
			 * fieldId
			 * ="Poi_photos","Poi_accuracy","Poi_altitude","Poi_latitude",...;
			 * fieldLabel:"start_date" => "start date", "substationType" =>
			 * "substation type",... fieldDeclaringClass="models.Poi";
			 * fieldAbsoluteName="models.Poi.photos","models.Poi.accuracy",...;
			 * fieldType="java.util.List","float","double","float",...
			 */

			String fieldName = field.getName();
			String fieldDeclaringClass = field.getDeclaringClass().getName();
			String fieldType = field.getType().getName();
			String fieldId = fieldDeclaringClass.replace("models.", "").replace(".", "_") + "_" + fieldName;
			String fieldLabel = fieldName.replace("_", " ").replace("T", " t");
			String fieldAbsoluteName = fieldDeclaringClass + "." + fieldName;

			StringBuffer select = new StringBuffer();
			select.append("<label for='" + fieldId + "'>" + fieldLabel + "</label>" + newLine);
			select.append("<select name='" + fieldId + "' id='" + fieldId + "' class='submitPoiDataField'>" + newLine);
			select.append("<option value=''> </option>" + newLine);

			if (fieldName.equals("id") || fieldName.equals("willBeSaved") || fieldAbsoluteName.equals("models.Poi.powerTag") || fieldAbsoluteName.equals("models.PowerTag.poi")) {
				continue;
			}

			if (fieldAbsoluteName.equals("models.Poi.photos")) {
				ArrayList list = new ArrayList((List) field.get(objectWithValues));
				String poiId = ((Poi) objectWithValues).id.toString();

				for (int i = 0; i < list.size(); i++) {
					output.append("<img width='200' src='/application/getpicture?position=" + i + "&id=" + poiId + "' alt='image of poi with id " + poiId + " on position " + i + "'/>" + newLine);
				}
			} else if (fieldType.equals("models.powerTags.Generator$MethodEnum") || fieldType.equals("models.powerTags.Generator$TypeEnum")) {
				if (objectFields.length == 1) {
					Object[] enums;

					if (fieldType.equals("models.powerTags.Generator$MethodEnum")) {
						enums = Generator.getMethods(Generator.SourceEnum.valueOf(source));
					} else {
						enums = Generator.getTypes(Generator.SourceEnum.valueOf(source), Generator.MethodEnum.valueOf(method));
					}

					if (enums.length >= 1) {
						Method getName = enums[0].getClass().getMethod("getName");
						Method getType = enums[0].getClass().getMethod("name");
						String enumArrayFieldName;
						String enumArrayFieldType;

						if (enums.length > 1) {
							output.append(select + newLine);
							for (int i = 0; i < enums.length; i++) {
								enumArrayFieldName = (String) getName.invoke(enums[i]);
								enumArrayFieldType = (String) getType.invoke(enums[i]);
								output.append("<option value='" + enumArrayFieldType + "'>" + enumArrayFieldName + "</option>" + newLine);
							}
							output.append("</select>" + newLine);
						} else {
							enumArrayFieldName = (String) getName.invoke(enums[0]);
							enumArrayFieldType = (String) getType.invoke(enums[0]);
							output.append("<p>" + fieldLabel + ": " + enumArrayFieldName + "</p>" + newLine);
						}
					}
				}
			} else if (fieldType.contains("Enum")) {
				Class enumClass = Class.forName(fieldType);
				Method getName = enumClass.getMethod("getName");
				Method getType = enumClass.getMethod("name");
				output.append(select + newLine);

				for (Object enumConstant : enumClass.getEnumConstants()) {
					String enumName = (String) getName.invoke(enumConstant);
					String enumType = (String) getType.invoke(enumConstant);
					output.append("<option value='" + enumType + "'>" + enumName + "</option>" + newLine);
				}
				output.append("</select>" + newLine);

				if (fieldType.equals("models.powerTags.Generator$SourceEnum")) {
					output.append("<div id='generatorMethod'></div>" + newLine);
					output.append("<div id='generatorType'></div>" + newLine);
				}
			} else {
				String fieldValue = "";

				if (objectWithValues != null && field.get(objectWithValues) != null) {
					fieldValue = field.get(objectWithValues).toString();
				}

				if (fieldType.equals("models.types.Operator")) {
					Object operator = null;

					if (objectWithValues != null) {
						operator = field.get(objectWithValues);
					}
					output.append("<p>Operator START</p>" + newLine);
					output.append(getHtmlCode(Operator.class.getFields(), operator, source, method) + newLine);
					output.append("<p>Operator END</p>" + newLine);
				} else {
					String lowerCaseFieldName = fieldName.toLowerCase();
					String lowerCaseFieldType = fieldType.toLowerCase();
					String parsleyValidator = "";

					if (lowerCaseFieldType.equals("byte") || lowerCaseFieldType.equals("double") || lowerCaseFieldType.equals("class java.lang.float") || lowerCaseFieldType.equals("float")
							|| lowerCaseFieldType.equals("int") || lowerCaseFieldType.equals("long")) {
						parsleyValidator = "data-parsley-type='number'";
					}
					output.append("<p>" + newLine);
					output.append("<label for='" + fieldId + "'>" + fieldLabel + "</label>" + newLine);

					if (lowerCaseFieldName.contains("time") || lowerCaseFieldName.contains("date")) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String date;

						try {
							date = dateFormat.format(new Date(Long.parseLong(fieldValue))).toString();
						} catch (Exception e) {
							date = "";
						}
						output.append("<input id='" + fieldId + "' class='datetimepicker' type='text' value='" + date + "'/>" + newLine);
					} else if (lowerCaseFieldType.equals("java.awt.color")) {
						output.append("<p>" + newLine);
						output.append("<span>" + fieldLabel + "</span>" + newLine);
						output.append("<span id='" + fieldId + "' class='color-box' style='background-color: #ff8800;'></span>" + newLine);
					} else {
						output.append("<input type='text' " + parsleyValidator + " id='" + fieldId + "' name='" + fieldId + "' value='" + fieldValue + "' class='submitPoiDataField'/>" + newLine);
					}
					output.append("</p>" + newLine);
				}
			}
		}

		return output.toString();
	}

	public static String getHtmlMethods(String source, String poiId) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag.getClass() != Generator.class) {
			poiPowerTag = null;
		}

		Field[] objectFields = { Generator.class.getField("method") };
		return getHtmlCode(objectFields, poiPowerTag, source, "");
	}

	public static String getHtmlPoi(String poiId) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, InvocationTargetException,
			NoSuchFieldException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Field[] objectFields = Poi.class.getFields();
		return getHtmlCode(objectFields, poi, "", "");
	}

	public static String getHtmlPowerTagDetails(String powerTag, String poiId) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException,
			ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		Class powerTagClass = Class.forName("models.powerTags." + powerTag);
		if (poiPowerTag.getClass() != powerTagClass) {
			poiPowerTag = null;
		}

		Field[] objectFields = Class.forName("models.powerTags." + powerTag).getFields();
		return getHtmlCode(objectFields, poiPowerTag, "", "");
	}

	public static String getHtmlTypes(String source, String method, String poiId) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException,
			ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag.getClass() != Generator.class) {
			poiPowerTag = null;
		}

		Field[] objectFields = { Generator.class.getField("type") };
		return getHtmlCode(objectFields, poiPowerTag, source, method);
	}

	public static void getPicture(long id, int position) {
		Poi poi = Poi.findById(id);
		response.setContentTypeIfNotSet(poi.photos.get(position).type());
		renderBinary(poi.photos.get(position).get());
	}

	public static void index() {
		render();
	}

	public static void savePicture(String id, File image) throws FileNotFoundException {
		Poi poi = Poi.findById(Long.valueOf(id));
		Blob blob = new Blob();
		blob.set(new FileInputStream(image), MimeTypes.getContentType(image.getName()));
		poi.photos.add(blob);
		poi.save();
	}

	public static void updatePoi(String Poi_accuracy, String Poi_altitude, String Poi_bearing, String poiId, String Poi_latitude, String Poi_longitude, String Poi_provider, String Poi_time)
			throws ParseException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		poi.accuracy = Float.parseFloat(Poi_accuracy);
		poi.altitude = Double.parseDouble(Poi_altitude);
		poi.bearing = Float.parseFloat(Poi_bearing);
		poi.latitude = Double.parseDouble(Poi_latitude);
		poi.longitude = Double.parseDouble(Poi_longitude);
		poi.provider = Poi_provider;
		poi.time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(Poi_time).getTime();
		poi.save();
		index();
	}

}