package controllers;

import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.*;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.*;
import models.powerTags.*;
import models.powerTags.Generator.SourceEnum;
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

		if (powerTags_Cable_circuits.equals("")) {
			cable.circuits = null;
		} else {
			cable.circuits = Byte.parseByte(powerTags_Cable_circuits);
		}

		if (powerTags_Cable_location.equals("")) {
			cable.location = null;
		} else {
			cable.location = Cable.LocationEnum.valueOf(powerTags_Cable_location);
		}
		cable.name = powerTags_Cable_name;

		if (cable.operator == null) {
			cable.operator = new Operator(poi.powerTag);
		}
		cable.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			cable.operator.type = null;
		} else {
			cable.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}
		cable.ref = powerTags_Cable_ref;

		if (powerTags_Cable_voltage.equals("")) {
			cable.voltage = null;
		} else {
			cable.voltage = Float.parseFloat(powerTags_Cable_voltage);
		}
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
			cableDistributionCabinet.operator = new Operator(poi.powerTag);
		}
		cableDistributionCabinet.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			cableDistributionCabinet.operator.type = null;
		} else {
			cableDistributionCabinet.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}
		cableDistributionCabinet.ref = powerTags_CableDistributionCabinet_ref;

		if (powerTags_CableDistributionCabinet_voltage.equals("")) {
			cableDistributionCabinet.voltage = null;
		} else {
			cableDistributionCabinet.voltage = Float.parseFloat(powerTags_CableDistributionCabinet_voltage);
		}
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

		if (powerTags_Converter_converter.equals("")) {
			converterObject.converter = null;
		} else {
			converterObject.converter = Converter.ConverterEnum.valueOf(powerTags_Converter_converter);
		}

		if (powerTags_Converter_poles.equals("")) {
			converterObject.poles = null;
		} else {
			converterObject.poles = Byte.parseByte(powerTags_Converter_poles);
		}

		if (powerTags_Converter_rating.equals("")) {
			converterObject.rating = null;
		} else {
			converterObject.rating = Long.parseLong(powerTags_Converter_rating);
		}

		if (powerTags_Converter_voltage.equals("")) {
			converterObject.voltage = null;
		} else {
			converterObject.voltage = Float.parseFloat(powerTags_Converter_voltage);
		}
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
			generator.operator = new Operator(poi.powerTag);
		}
		generator.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			generator.operator.type = null;
		} else {
			generator.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}

		if (powerTags_Generator_output.equals("")) {
			generator.output = null;
		} else {
			generator.output = Generator.OutputEnum.valueOf(powerTags_Generator_output);
		}

		if (powerTags_Generator_plant.equals("")) {
			generator.plant = null;
		} else {
			generator.plant = Generator.PlantEnum.valueOf(powerTags_Generator_plant);
		}

		if (powerTags_Generator_source.equals("")) {
			generator.source = null;
		} else {
			generator.source = Generator.SourceEnum.valueOf(powerTags_Generator_source);
		}

		if (powerTags_Generator_method.equals("")) {
			generator.method = Generator.MethodEnum.NULL;
		} else {
			generator.setMethodEnum(Generator.MethodEnum.valueOf(powerTags_Generator_method));
		}

		if (powerTags_Generator_type.equals("")) {
			generator.type = Generator.TypeEnum.NULL;
		} else {
			generator.setTypeEnum(Generator.TypeEnum.valueOf(powerTags_Generator_type));
		}
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
		line.cables = powerTags_Line_cables;

		if (line.operator == null) {
			line.operator = new Operator(poi.powerTag);
		}
		line.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			line.operator.type = null;
		} else {
			line.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}
		line.ref = powerTags_Line_ref;

		if (powerTags_Line_voltage.equals("")) {
			line.voltage = null;
		} else {
			line.voltage = Float.parseFloat(powerTags_Line_voltage);
		}

		if (powerTags_Line_wires.equals("")) {
			line.wires = null;
		} else {
			line.wires = Line.WiresEnum.valueOf(powerTags_Line_wires);
		}
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
		minorLine.cables = powerTags_MinorLine_cables;
		minorLine.name = powerTags_MinorLine_name;

		if (minorLine.operator == null) {
			minorLine.operator = new Operator(poi.powerTag);
		}
		minorLine.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			minorLine.operator.type = null;
		} else {
			minorLine.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}
		minorLine.ref = powerTags_MinorLine_ref;

		if (powerTags_MinorLine_voltage.equals("")) {
			minorLine.voltage = null;
		} else {
			minorLine.voltage = Float.parseFloat(powerTags_MinorLine_voltage);
		}
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

		if (powerTags_Plant_landuse.equals("")) {
			plant.landuse = null;
		} else {
			plant.landuse = Plant.LanduseEnum.valueOf(powerTags_Plant_landuse);
		}
		plant.name = powerTags_Plant_name;

		if (plant.operator == null) {
			plant.operator = new Operator(poi.powerTag);
		}
		plant.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			plant.operator.type = null;
		} else {
			plant.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}

		if (powerTags_Plant_output.equals("")) {
			plant.output = null;
		} else {
			plant.output = Plant.OutputEnum.valueOf(powerTags_Plant_output);
		}
		
		if (powerTags_Plant_start_date.equals("")) {
			plant.start_date = null;
		} else {
			plant.start_date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(powerTags_Plant_start_date).getTime();
		}
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

		if (powerTags_Substation_gas_insulated.equals("")) {
			substation.gas_insulated = null;
		} else {
			substation.gas_insulated = Boolean.valueOf(powerTags_Substation_gas_insulated);
		}

		if (powerTags_Substation_location.equals("")) {
			substation.location = null;
		} else {
			substation.location = Substation.LocationEnum.valueOf(powerTags_Substation_location);
		}
		substation.name = powerTags_Substation_name;

		if (substation.operator == null) {
			substation.operator = new Operator(poi.powerTag);
		}
		substation.operator.name = types_Operator_name;

		if (types_Operator_type.equals("")) {
			substation.operator.type = null;
		} else {
			substation.operator.type = Operator.TypeEnum.valueOf(types_Operator_type);
		}
		substation.ref = powerTags_Substation_ref;

		if (powerTags_Substation_substationType.equals("")) {
			substation.substationType = null;
		} else {
			substation.substationType = Substation.SubstationTypeEnum.valueOf(powerTags_Substation_substationType);
		}

		if (powerTags_Substation_voltage.equals("")) {
			substation.voltage = null;
		} else {
			substation.voltage = Float.parseFloat(powerTags_Substation_voltage);
		}
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
		Pattern pattern = Pattern.compile(" *([0-9]+), *([0-9]+), *([0-9]+), *([0-9]+) *");
		Matcher matcher = pattern.matcher(powerTags_Tower_color);
		if (matcher.matches()) {
			tower.color = new Color(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3)));
		} else {
			tower.color = null;
		}

		if (powerTags_Tower_design.equals("")) {
			tower.design = null;
		} else {
			tower.design = Tower.DesignEnum.valueOf(powerTags_Tower_design);
		}

		if (powerTags_Tower_height.equals("")) {
			tower.height = null;
		} else {
			tower.height = Float.parseFloat(powerTags_Tower_height);
		}

		if (powerTags_Tower_material.equals("")) {
			tower.material = null;
		} else {
			tower.material = Tower.MaterialEnum.valueOf(powerTags_Tower_material);
		}
		tower.ref = powerTags_Tower_ref;

		if (powerTags_Tower_structure.equals("")) {
			tower.structure = null;
		} else {
			tower.structure = Tower.StructureEnum.valueOf(powerTags_Tower_structure);
		}

		if (powerTags_Tower_type.equals("")) {
			tower.type = null;
		} else {
			tower.type = Tower.TypeEnum.valueOf(powerTags_Tower_type);
		}
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

		if (powerTags_Transformer_frequency.equals("")) {
			transformer.frequency = null;
		} else {
			transformer.frequency = Float.parseFloat(powerTags_Transformer_frequency);
		}

		if (powerTags_Transformer_location.equals("")) {
			transformer.location = null;
		} else {
			transformer.location = Transformer.LocationEnum.valueOf(powerTags_Transformer_location);
		}

		if (powerTags_Transformer_phases.equals("")) {
			transformer.phases = null;
		} else {
			transformer.phases = Integer.parseInt(powerTags_Transformer_phases);
		}

		if (powerTags_Transformer_rating.equals("")) {
			transformer.rating = null;
		} else {
			transformer.rating = Long.parseLong(powerTags_Transformer_rating);
		}

		if (powerTags_Transformer_transformerType.equals("")) {
			transformer.transformerType = null;
		} else {
			transformer.transformerType = Transformer.TransformerEnum.valueOf(powerTags_Transformer_transformerType);
		}

		if (powerTags_Transformer_voltage.equals("")) {
			transformer.voltage = null;
		} else {
			transformer.voltage = Float.parseFloat(powerTags_Transformer_voltage);
		}
		poi.powerTag.save();
	}

	public static void deletePowerTag(String poiId) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag != null) {
			poi.powerTag.delete();
		}
	}

	public static void editPoi(String poiId) {
		Poi poi = Poi.findById(Long.parseLong(poiId));
		renderTemplate("/Application/editPoi.html", poi);
	}

	public static String getColor(String poiId) {
		Poi poi = Poi.findById(Long.parseLong(poiId));

		if (poi.powerTag != null && poi.powerTag.getClass() == Tower.class && ((Tower) poi.powerTag).color != null) {
			String hexColor = Integer.toHexString(((Tower) poi.powerTag).color.getRGB());
			return hexColor.substring(2, hexColor.length());
		} else {
			return "ffffff";
		}
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
			select.append("   <label for='" + fieldId + "'>" + fieldLabel + "</label>" + newLine);
			select.append("   <select name='" + fieldId + "' id='" + fieldId + "' class='submitPoiDataField'>" + newLine);
			select.append("      <option value=''> </option>" + newLine);

			if (fieldName.equals("id") || fieldName.equals("willBeSaved") || fieldAbsoluteName.equals("models.Poi.powerTag") || fieldAbsoluteName.equals("models.types.Operator.powerTag")
					|| fieldAbsoluteName.equals("models.PowerTag.poi")) {
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
								String selected = "";

								if (objectWithValues != null && field.get(objectWithValues) != null && field.get(objectWithValues).toString().equals(enumArrayFieldType)) {
									if (((Generator) objectWithValues).source.name().equals(source) && fieldType.equals("models.powerTags.Generator$MethodEnum")) {
										selected = "selected=''";
									} else if (((Generator) objectWithValues).source.name().equals(source) && ((Generator) objectWithValues).method.name().equals(method) && fieldType.equals("models.powerTags.Generator$TypeEnum")) {
										selected = "selected=''";
									}
								}
								output.append("      <option " + selected + " value='" + enumArrayFieldType + "'>" + enumArrayFieldName + "</option>" + newLine);
							}
							output.append("</select>" + newLine);
						} else {
							enumArrayFieldName = (String) getName.invoke(enums[0]);
							enumArrayFieldType = (String) getType.invoke(enums[0]);
							output.append("<p>" + newLine);
							output.append("   <span>" + fieldLabel + ":</span>" + newLine);
							output.append("   <span id='" + fieldId + "' style='display:none;'>" + enumArrayFieldType + "</span>" + newLine);
							output.append("   <span                                           >" + enumArrayFieldName + "</span>" + newLine);
							output.append("</p>");
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
					String selected = "";
					if (objectWithValues != null && field.get(objectWithValues) != null && field.get(objectWithValues).equals(enumConstant)) {
						selected = "selected=''";
					}

					output.append("      <option value='" + enumType + "' " + selected + ">" + enumName + "</option>" + newLine);
				}
				output.append("</select>" + newLine);

				if (fieldType.equals("models.powerTags.Generator$SourceEnum")) {
					String htmlMethods = "";
					String htmlTypes = "";
					
					if (objectWithValues != null && ((Generator) objectWithValues).source != null) {
						String poiId = ((Generator) objectWithValues).poi.id.toString();
						String sourceName = ((Generator) objectWithValues).source.name();
						htmlMethods = getHtmlMethods(sourceName, poiId);
					
						if (((Generator) objectWithValues).method != null) {
							String methodName = ((Generator) objectWithValues).method.name();
							htmlTypes = getHtmlTypes(sourceName, methodName, poiId);
						}
					}
					
					output.append("<div id='generatorMethod'>" + htmlMethods + "</div>" + newLine);
					output.append("<div id='generatorType'>" + htmlTypes + "</div>" + newLine);
				}
			} else if (fieldType.equals("models.types.Operator")) {
				Object operator = null;

				if (objectWithValues != null) {
					operator = field.get(objectWithValues);
				}
				output.append("<p>Operator START</p>" + newLine);
				output.append(getHtmlCode(Operator.class.getFields(), operator, source, method) + newLine);
				output.append("<p>Operator END</p>" + newLine);
			} else if (fieldType.equals("java.awt.Color")) {
				String fieldValue;

				if (objectWithValues != null && field.get(objectWithValues) != null) {
					String hexColor = Integer.toHexString(((Color) field.get(objectWithValues)).getRGB());
					fieldValue = hexColor.substring(2, hexColor.length());
				} else {
					fieldValue = "ffffff";
				}

				output.append("<p>" + newLine);
				output.append("   <span>" + fieldLabel + "</span>" + newLine);
				output.append("   <span class='color-box submitPoiDataField' id='" + fieldId + "' style='background-color:#" + fieldValue + ";'></span>" + newLine);
				output.append("</p>" + newLine);
			} else if (fieldName.toLowerCase().contains("time") || fieldName.toLowerCase().contains("date")) {
				String fieldValue = "";

				if (objectWithValues != null && field.get(objectWithValues) != null && fieldType.equals("java.lang.Long")) {
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					fieldValue = dateFormat.format(new Date(Long.parseLong(field.get(objectWithValues).toString()))).toString();
				}
				output.append("<p>" + newLine);
				output.append("   <label for='" + fieldId + "'>" + fieldLabel + "</label>" + newLine);
				output.append("   <input class='datetimepicker submitPoiDataField' id='" + fieldId + "' name='" + fieldId + "' type='text' value='" + fieldValue + "'/>" + newLine);
				output.append("</p>" + newLine);
			} else if (fieldType.equals("java.lang.Boolean")) {
				Boolean fieldValue;
				output.append(select + newLine);

				if (objectWithValues != null && field.get(objectWithValues) != null) {
					fieldValue = (Boolean) field.get(objectWithValues);
					
					if (fieldValue) {
						output.append("      <option value='false'>false</option>" + newLine);
						output.append("      <option selected='' value='true'>true</option>" + newLine);
					} else {
						output.append("      <option selected='' value='false'>false</option>" + newLine);
						output.append("      <option value='true'>true</option>" + newLine);	
					}
				} else {
					output.append("      <option value='false'>false</option>" + newLine);
					output.append("      <option value='true'>true</option>" + newLine);	
				}
				output.append("</select>" + newLine);
			} else if (fieldType.equals("java.lang.Byte") || fieldType.equals("java.lang.Double") || fieldType.equals("java.lang.Float") || fieldType.equals("java.lang.Integer") || fieldType.equals("java.lang.Long") || (fieldType.equals("java.lang.String") && fieldName.equals("cables"))) {
				String additionalClass = "";
				String fieldValue = "";
				List<String> restrictedFields = Arrays.asList("accuracy", "bearing", "cables", "circuits", "frequency", "height", "latitude", "longitude", "phases", "poles", "rating");
				String step = "";
				String type = "number";

				if (objectWithValues != null && field.get(objectWithValues) != null) {
					fieldValue = field.get(objectWithValues).toString();
				}

				if (restrictedFields.contains(fieldName)) {
					additionalClass = fieldName;
				} else if (fieldType.equals("java.lang.Byte")) {
					additionalClass = "byte";
				} else if (fieldType.equals("java.lang.Double")) {
					additionalClass = "double";
				} else if (fieldType.equals("java.lang.Float")) {
					additionalClass = "float";
				} else if (fieldType.equals("java.lang.Integer")) {
					additionalClass = "integer";
				} else if (fieldType.equals("java.lang.Long")) {
					additionalClass = "long";
				}
				
				if (fieldType.equals("java.lang.Double") || fieldType.equals("java.lang.Float")) {
					if (!fieldValue.equals("")) {
						step = "step='" + BigDecimal.valueOf(Math.pow(10, -new BigDecimal(fieldValue).scale())).stripTrailingZeros().toPlainString() + "'";
					}
				}
				
				if (fieldType.equals("java.lang.String")) {
					type = "text";
				}

				output.append("<p>" + newLine);
				output.append("   <label for='" + fieldId + "'>" + fieldLabel + "</label>" + newLine);
				output.append("   <input class='" + additionalClass + " submitPoiDataField' id='" + fieldId + "' name='" + fieldId + "' " + step + " type='" + type + "' value='" + fieldValue + "'/>" + newLine);
				output.append("</p>" + newLine);
			} else {
				String fieldValue = "";

				if (objectWithValues != null && field.get(objectWithValues) != null) {
					fieldValue = field.get(objectWithValues).toString();
				}
				output.append("<p>" + newLine);
				output.append("   <label for='" + fieldId + "'>" + fieldLabel + "</label>" + newLine);
				output.append("   <input class='submitPoiDataField' id='" + fieldId + "' name='" + fieldId + "' type='text' value='" + fieldValue + "'/>" + newLine);
				output.append("</p>" + newLine);
			}
		}

		return output.toString();
	}

	public static String getHtmlMethods(String source, String poiId) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag != null && poiPowerTag.getClass() != Generator.class) {
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
		if (poiPowerTag != null && poiPowerTag.getClass() != powerTagClass) {
			poiPowerTag = null;
		}

		Field[] objectFields = Class.forName("models.powerTags." + powerTag).getFields();
		return getHtmlCode(objectFields, poiPowerTag, "", "");
	}

	public static String getHtmlTypes(String source, String method, String poiId) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException,
			ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		Poi poi = Poi.findById(Long.valueOf(poiId));
		Object poiPowerTag = Poi.class.getField("powerTag").get(poi);

		if (poiPowerTag != null && poiPowerTag.getClass() != Generator.class) {
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