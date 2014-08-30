package controllers;

import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.*;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.*;
import models.GoogleUser.AccountType;
import models.powerTags.Cable;
import models.powerTags.Converter.ConverterEnum;
import models.powerTags.Generator.MethodEnum;
import models.powerTags.Generator.PlantEnum;
import models.powerTags.Generator.SourceEnum;
import models.powerTags.CableDistributionCabinet;
import models.powerTags.Converter;
import models.powerTags.Generator;
import models.powerTags.Line;
import models.powerTags.Line.WiresEnum;
import models.powerTags.Plant.LanduseEnum;
import models.powerTags.MinorLine;
import models.powerTags.Plant;
import models.powerTags.Pole;
import models.powerTags.Substation;
import models.powerTags.Substation.BooleanEnum;
import models.powerTags.Tower.DesignEnum;
import models.powerTags.Tower.MaterialEnum;
import models.powerTags.Switch;
import models.powerTags.Tower;
import models.powerTags.Tower.StructureEnum;
import models.powerTags.Transformer;
import models.types.Operator;
import models.types.Operator.TypeEnum;
import models.types.Output.OutputEnum;

public class Application extends Controller {

	private static final String audience = "889611969164-ujvohn299csu833avfmcsun3k6fna30s.apps.googleusercontent.com";
	private static final String[] android_clientId = new String[] { "889611969164-hhapbnd498ntbuulf3u7m2prba7cpu29.apps.googleusercontent.com" };
	private static final String[] web_clientId = new String[] { audience };

	public static void createPoi(String token) throws FileNotFoundException {

		Checker checker = new Checker(android_clientId, audience);
		Payload payload = checker.check(token);

		if (payload != null) {
			Poi poi = new Poi();
			poi.latLngIsDerived = true;
			poi.locationTrace = new ArrayList<LocationTrace>();
			poi.photos = new ArrayList<Photo>();
			int index = 0;
			double latitude = 0;
			double longitude = 0;
			while (true) {
				File photoFile = params.get("photo_" + index + "_file",
						File.class);
				if (photoFile == null) {
					break;
				} else {
					Photo photo = new Photo(poi);
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
					poi.photos.add(photo);
					latitude += photo.latitude;
					longitude += photo.longitude;
					index++;
				}
			}
			poi.latitude = latitude / poi.photos.size();
			poi.longitude = longitude / poi.photos.size();
			if (!poi.photos.isEmpty()) {
				index = 0;
				while (true) {
					LocationTrace trace = new LocationTrace(poi);
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
					poi.locationTrace.add(trace);
					index++;
				}
				poi.save();
				WebSocket.publishAddMarkerEvent(poi.latitude, poi.longitude,
						poi.id);
				ok();
			} else {
				badRequest();
			}
		} else {
			unauthorized();
		}
	}

	public static void getPhoto() {
		String gToken = params.get("gToken");
		Long photoId = params.get("photoId", Long.class);
		Photo photo = Photo.findById(photoId);
		renderArgs.put("photo", photo);
		render("app/views/tags/photo.html");
	}

	public static void getPoi() {
		String gToken = params.get("gToken");
		Long poiId = params.get("poiId", Long.class);
		Poi poi = Poi.findById(poiId);
		renderArgs.put("poi", poi);
		render("app/views/tags/poi.html");
	}

	public static void getPoiPowerTag(long poiId, String powerTag) {

		if (powerTag != null && !powerTag.isEmpty() && !powerTag.equals("null")) {
			Poi poi = Poi.findById(poiId);
			String powerTagFileName = powerTag.replaceAll(" ", "_")
					.toLowerCase();

			if (poi.powerTag != null) {
				String poiPowerTagClassName = poi.powerTag.getClass()
						.getSimpleName();
				String powerTagInput = powerTag.replaceAll(" ", "");
				if (poiPowerTagClassName.equals(powerTagInput)) {
					renderArgs.put("powerTag", poi.powerTag);
				}
			}
			render("app/views/tags/power/" + powerTagFileName + ".html");
		}
	}

	public static void getPoiPowerTagGeneratorMethod(long poiId, String source) {

		if (source != null && !source.isEmpty() && !source.equals("null")) {
			Poi poi = Poi.findById(poiId);
			Generator generator = (Generator) poi.powerTag;
			if (generator != null && generator.source != null
					&& generator.source.name.equals(source)
					&& generator.method != null) {
				renderArgs.put("method", generator.method);
			}
			render("app/views/tags/power/fields/generator/method/"
					+ source.replaceAll(" ", "_").toLowerCase() + ".html");
		}
	}

	public static void getPoiPowerTagGeneratorType(long poiId,
			String sourceMethod) {

		if (sourceMethod != null && !sourceMethod.isEmpty()
				&& !sourceMethod.equals("null")) {
			Poi poi = Poi.findById(poiId);
			Generator generator = (Generator) poi.powerTag;
			String source = "";
			String method = "";
			if (generator != null) {
				if (generator.source != null && generator.source.name != "null") {
					source = generator.source.name;
				}
				if (generator.method != null && generator.method.name != "null") {
					method = generator.method.name;
				}
			}
			String poiSourceMethod;
			if (source.isEmpty() || method.isEmpty()) {
				poiSourceMethod = source + method;
			} else {
				poiSourceMethod = source + "_" + method;
			}
			if (poiSourceMethod != null && poiSourceMethod.equals(sourceMethod)
					&& generator.type != null) {
				renderArgs.put("type", generator.type);
			}
			render("app/views/tags/power/fields/generator/type/"
					+ sourceMethod.replaceAll(" ", "_").toLowerCase() + ".html");
		}
	}

	public static void index() {
		List<Poi> pois = Poi.findAll();
		renderArgs.put("pois", pois);
		render();
	}

	public static void registerUser() throws Exception {

		String gToken = params.get("gToken");

		if (gToken != null) {
			Checker checker = new Checker(web_clientId, audience);
			Payload payload = checker.check(gToken);

			if (payload != null) {
				String googleId = payload.getUserId();
				String googleMail = payload.getEmail();

				if (googleId != null && googleMail != null) {
					List<GoogleUser> users = GoogleUser.find("byGoogleId",
							googleId).fetch();
					GoogleUser user;
					if (users.size() == 1) {
						user = users.get(0);
						if (!googleMail.equals(user.googleMail)) {
							user.googleMail = googleMail;
							user.save();
						}
					} else if (users.isEmpty()) {
						user = new GoogleUser();
						user.accountType = AccountType.NULL;
						user.googleId = googleId;
						user.googleMail = googleMail;
						user.save();
					} else {
						throw new Exception();
					}
				}
			}
		}
	}

	public static String spaceBeforeUpperCase(String str) {
		return str.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
	}

	public static void updatePoi() throws ParseException,
			ClassNotFoundException {
		Poi poi = Poi.findById(params.get("poi_id", Long.class));
		poi.accuracy = params.get("poi_accuracy", Float.class);
		poi.altitude = params.get("poi_altitude", Double.class);
		poi.bearing = params.get("poi_bearing", Float.class);
		poi.latitude = params.get("poi_latitude", Double.class);
		poi.longitude = params.get("poi_longitude", Double.class);
		poi.provider = params.get("poi_provider", String.class);
		String time = params.get("poi_time", String.class);
		if (!time.isEmpty()) {
			poi.time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(
					time.replace("T", " ")).getTime();
		}
		String powerTagParam = params.get("poi_power_tag", String.class);
		if (poi.powerTag != null) {
			poi.powerTag.delete();
			poi.powerTag = null;
		}
		if (powerTagParam != null && !powerTagParam.equals("null")) {
			Class powerTagClass = Class.forName("models.powerTags."
					+ powerTagParam.replaceAll(" ", ""));
			if (powerTagClass.equals(Cable.class)) {
				poi.powerTag = new Cable(poi);
				Cable cable = (Cable) poi.powerTag;
				cable.cables = params.get("cables", String.class);
				cable.circuits = params.get("circuits", Byte.class);
				cable.location = Cable.LocationEnum.valueOf(params
						.get("location", String.class).replaceAll(" ", "_")
						.toUpperCase());
				cable.name = params.get("name", String.class);
				cable.operator = new Operator(cable);
				cable.operator.name = params.get("operator_name", String.class);
				cable.operator.type = TypeEnum.valueOf(params
						.get("operator_type", String.class)
						.replaceAll(" ", "_").toUpperCase());
				cable.ref = params.get("ref", String.class);
				cable.voltage = params.get("voltage", Float.class);
			} else if (powerTagClass.equals(CableDistributionCabinet.class)) {
				poi.powerTag = new CableDistributionCabinet(poi);
				CableDistributionCabinet cableDistributionCabinet = (CableDistributionCabinet) poi.powerTag;
				cableDistributionCabinet.operator = new Operator(
						cableDistributionCabinet);
				cableDistributionCabinet.operator.name = params.get(
						"operator_name", String.class);
				cableDistributionCabinet.operator.type = TypeEnum
						.valueOf(params.get("operator_type", String.class)
								.replaceAll(" ", "_").toUpperCase());
				cableDistributionCabinet.ref = params.get("ref", String.class);
				cableDistributionCabinet.voltage = params.get("voltage",
						Float.class);
			} else if (powerTagClass.equals(Converter.class)) {
				poi.powerTag = new Converter(poi);
				Converter converter = (Converter) poi.powerTag;
				converter.converter = ConverterEnum.valueOf(params
						.get("converter", String.class).replaceAll(" ", "_")
						.toUpperCase());
				converter.poles = params.get("poles", Byte.class);
				converter.rating = params.get("rating", Long.class);
				converter.voltage = params.get("voltage", Float.class);
			} else if (powerTagClass.equals(Generator.class)) {
				poi.powerTag = new Generator(poi);
				Generator generator = (Generator) poi.powerTag;
				generator.name = params.get("name", String.class);
				generator.operator = new Operator(generator);
				generator.operator.name = params.get("operator_name",
						String.class);
				generator.operator.type = TypeEnum.valueOf(params
						.get("operator_type", String.class)
						.replaceAll(" ", "_").toUpperCase());
				generator.output = OutputEnum.valueOf(params
						.get("output", String.class).replaceAll(" ", "_")
						.toUpperCase());
				generator.plant = PlantEnum.valueOf(params
						.get("plant", String.class).replaceAll(" ", "_")
						.toUpperCase());
				String source = params.get("source");
				String method = params.get("method");
				String type = params.get("type");
				if (source != null) {
					generator.source = SourceEnum.valueOf(source.replaceAll(
							" ", "_").toUpperCase());
				}
				if (method != null) {
					generator.method = MethodEnum.valueOf(method.replaceAll(
							" ", "_").toUpperCase());
				}
				if (type != null) {
					generator.type = Generator.TypeEnum.valueOf(type
							.replaceAll(" ", "_").toUpperCase());
				}
			} else if (powerTagClass.equals(Line.class)) {
				poi.powerTag = new Line(poi);
				Line line = (Line) poi.powerTag;
				line.cables = params.get("cables", String.class);
				line.operator = new Operator(line);
				line.operator.name = params.get("operator_name", String.class);
				line.operator.type = TypeEnum.valueOf(params
						.get("operator_type", String.class)
						.replaceAll(" ", "_").toUpperCase());
				line.ref = params.get("ref", String.class);
				line.voltage = params.get("voltage", Float.class);
				line.wires = WiresEnum.valueOf(params
						.get("wires", String.class).replaceAll(" ", "_")
						.toUpperCase());
			} else if (powerTagClass.equals(MinorLine.class)) {
				poi.powerTag = new MinorLine(poi);
				MinorLine minorLine = (MinorLine) poi.powerTag;
				minorLine.cables = params.get("cables", String.class);
				minorLine.name = params.get("name", String.class);
				minorLine.operator = new Operator(minorLine);
				minorLine.operator.name = params.get("operator_name",
						String.class);
				minorLine.operator.type = TypeEnum.valueOf(params
						.get("operator_type", String.class)
						.replaceAll(" ", "_").toUpperCase());
				minorLine.ref = params.get("ref", String.class);
				minorLine.voltage = params.get("voltage", Float.class);
			} else if (powerTagClass.equals(Plant.class)) {
				poi.powerTag = new Plant(poi);
				Plant plant = (Plant) poi.powerTag;
				plant.landuse = LanduseEnum.valueOf(params
						.get("landuse", String.class).replaceAll(" ", "_")
						.toUpperCase());
				plant.name = params.get("name", String.class);
				plant.operator = new Operator(plant);
				plant.operator.name = params.get("operator_name", String.class);
				plant.operator.type = TypeEnum.valueOf(params
						.get("operator_type", String.class)
						.replaceAll(" ", "_").toUpperCase());
				plant.output = OutputEnum.valueOf(params
						.get("output", String.class).replaceAll(" ", "_")
						.toUpperCase());
				String start_date = params.get("start_date", String.class);
				if (!time.isEmpty()) {
					plant.start_date = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse(start_date.replace("T", " ")).getTime();
				}
			} else if (powerTagClass.equals(Pole.class)) {
				poi.powerTag = new Pole(poi);
				Pole pole = (Pole) poi.powerTag;
				pole.ref = params.get("ref", String.class);
			} else if (powerTagClass.equals(Substation.class)) {
				poi.powerTag = new Substation(poi);
				Substation substation = (Substation) poi.powerTag;
				substation.gas_insulated = BooleanEnum.valueOf(params
						.get("gas_insulated", String.class)
						.replaceAll(" ", "_").toUpperCase());
				substation.location = Substation.LocationEnum.valueOf(params
						.get("location", String.class).replaceAll(" ", "_")
						.toUpperCase());
				substation.name = params.get("name", String.class);
				substation.operator = new Operator(substation);
				substation.operator.name = params.get("operator_name",
						String.class);
				substation.operator.type = TypeEnum.valueOf(params
						.get("operator_type", String.class)
						.replaceAll(" ", "_").toUpperCase());
				substation.ref = params.get("ref", String.class);
				substation.type = Substation.TypeEnum.valueOf(params
						.get("type", String.class).replaceAll(" ", "_")
						.toUpperCase());
				substation.voltage = params.get("voltage", Float.class);
			} else if (powerTagClass.equals(Switch.class)) {
				poi.powerTag = new Switch(poi);
			} else if (powerTagClass.equals(Tower.class)) {
				poi.powerTag = new Tower(poi);
				Tower tower = (Tower) poi.powerTag;
				tower.color = Color.decode(params.get("color", String.class));
				tower.design = DesignEnum.valueOf(params
						.get("design", String.class).replaceAll(" ", "_")
						.toUpperCase());
				tower.height = params.get("height", Float.class);
				tower.material = MaterialEnum.valueOf(params
						.get("material", String.class).replaceAll(" ", "_")
						.toUpperCase());
				tower.ref = params.get("ref", String.class);
				tower.structure = StructureEnum.valueOf(params
						.get("structure", String.class).replaceAll(" ", "_")
						.toUpperCase());
				tower.type = Tower.TypeEnum.valueOf(params
						.get("type", String.class).replaceAll(" ", "_")
						.toUpperCase());
			} else if (powerTagClass.equals(Transformer.class)) {
				poi.powerTag = new Transformer(poi);
				Transformer transformer = (Transformer) poi.powerTag;
				transformer.frequency = params.get("frequency", Float.class);
				transformer.location = Transformer.LocationEnum.valueOf(params
						.get("location", String.class).replaceAll(" ", "_")
						.toUpperCase());
				transformer.phases = params.get("phases", Integer.class);
				transformer.rating = params.get("rating", Long.class);
				transformer.type = Transformer.TypeEnum.valueOf(params
						.get("type", String.class).replaceAll(" ", "_")
						.toUpperCase());
				transformer.voltage = params.get("voltage", Float.class);
			}
		}
		poi.save();
	}
}