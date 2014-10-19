package controllers;

import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.*;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import models.*;
import models.GoogleUser.AccountType;
import models.Poi.TaskStatus;
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
import models.powerTags.types.Operator;
import models.powerTags.types.Operator.TypeEnum;
import models.powerTags.types.Output.OutputEnum;
import models.powerTags.Transformer;

public class Application extends Controller {

	private static final String audience = "484348018096-2vlhog84fcgd06nalbe16i446veti18c.apps.googleusercontent.com";
	private static final String[] android_clientId = new String[] { "484348018096-8eupo737lepmjjrkni5su91ddtddkbda.apps.googleusercontent.com" };
	private static final String[] web_clientId = new String[] { audience };

	public static void broadcastClosePoi(String gToken, Long poiId) {

		if (isAuthorized(gToken)) {
			GoogleUser googleUser = getGoogleUser(gToken);
			Poi poi = Poi.findById(poiId);

			if (googleUser != null && poi != null && poi.googleUser != null
					&& poi.googleUser.equals(googleUser)) {
				poi.googleUser = null;
				if (poi.store()) {
					WebSocket.publishAddMarkerEvent(null, poi);
					ok();
				} else {
					forbidden();
				}
			}
		}
	}

	public static void broadcastOpenPoi(String gToken, Long poiId) {

		if (isAuthorized(gToken)) {
			GoogleUser googleUser = getGoogleUser(gToken);
			Poi poi = Poi.findById(poiId);

			if (googleUser != null
					&& poi != null
					&& (poi.googleUser != null && poi.googleUser
							.equals(googleUser))) {
				WebSocket.publishAddMarkerEvent(googleUser, poi);
			}
		}
	}

	public static String colorToHex(Color color) {
		String rbg = Integer.toHexString(color.getRGB());
		return rbg.substring(2, rbg.length());
	}

	public static void createPoi(String token) throws FileNotFoundException {

		Checker checker = new Checker(android_clientId, audience);
		Payload payload = checker.check(token);

		if (payload != null) {
			Poi poi = new Poi();
			poi.latLngIsDerived = true;
			poi.locationTrace = new ArrayList<LocationTrace>();
			poi.photos = new ArrayList<Photo>();
			poi.taskStatus = TaskStatus.TODO;
			int index = 0;
			Float accuracy = null;
			Double altitude = null;
			Double latitude = null;
			Double longitude = null;

			while (true) {
				File photoFile = params.get("photo_" + index + "_file",
						File.class);

				if (photoFile == null) {
					break;
				}
				Photo photo = new Photo(poi);
				photo.accuracy = params.get("photo_" + index + "_accuracy",
						Float.class);
				photo.altitude = params.get("photo_" + index + "_altitude",
						Double.class);
				photo.bearing = params.get("photo_" + index + "_bearing",
						Float.class);
				photo.latitude = params.get("photo_" + index + "_latitude",
						Double.class);
				photo.longitude = params.get("photo_" + index + "_longitude",
						Double.class);
				photo.provider = params.get("photo_" + index + "_provider");
				photo.time = params.get("photo_" + index + "_time", Long.class);
				Blob photoBlob = new Blob();
				String photoName = photoFile.getName();
				photoBlob.set(new FileInputStream(photoFile),
						MimeTypes.getContentType(photoName));
				photo.photoBlob = photoBlob;
				poi.photos.add(photo);
				if (photo.accuracy != null) {
					if (accuracy == null) {
						accuracy = photo.accuracy;
					} else {
						accuracy += photo.accuracy;
					}
				}
				if (photo.altitude != null) {
					if (altitude == null) {
						altitude = photo.altitude;
					} else {
						altitude += photo.altitude;
					}
				}
				if (photo.latitude != null) {
					if (latitude == null) {
						latitude = photo.latitude;
					} else {
						latitude += photo.latitude;
					}
				}
				if (photo.longitude != null) {
					if (longitude == null) {
						longitude = photo.longitude;
					} else {
						longitude += photo.longitude;
					}
				}
				index++;
			}
			if (!poi.photos.isEmpty()) {
				int numberOfPhotos = poi.photos.size();
				if (accuracy != null) {
					poi.accuracy = accuracy / numberOfPhotos;
				}
				if (altitude != null) {
					poi.altitude = altitude / numberOfPhotos;
				}
				if (latitude != null) {
					poi.latitude = latitude / numberOfPhotos;
				}
				if (longitude != null) {
					poi.longitude = longitude / numberOfPhotos;
				}
				poi.provider = poi.photos.get(0).provider;
				poi.start_time = poi.photos.get(0).time;
				poi.end_time = poi.photos.get(numberOfPhotos - 1).time;
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
					trace.provider = params.get("trace_" + index + "_provider");
					trace.time = params.get("trace_" + index + "_time",
							Long.class);
					poi.locationTrace.add(trace);
					index++;
				}
				if (poi.store()) {
					WebSocket.publishAddMarkerEvent(null, poi);
					ok();
				} else {
					forbidden();
				}
			} else {
				badRequest();
			}
		} else {
			unauthorized();
		}
	}

	private static String getGoogleId(String gToken) {

		if (gToken != null) {
			Checker checker = new Checker(web_clientId, audience);
			Payload payload = checker.check(gToken);

			if (payload != null) {
				return payload.getUserId();
			}
		}
		return null;
	}

	private static String getGoogleMail(String gToken) {

		if (gToken != null) {
			Checker checker = new Checker(web_clientId, audience);
			Payload payload = checker.check(gToken);

			if (payload != null) {
				return payload.getEmail();
			}
		}
		return null;
	}

	static GoogleUser getGoogleUser(String gToken) {
		String googleId = getGoogleId(gToken);

		if (googleId != null) {
			List<GoogleUser> users = GoogleUser.find("byGoogleId", googleId)
					.fetch();

			if (users.size() == 1) {
				return users.get(0);
			}
		}
		return null;
	}

	public static String getHtmlInputStep(String step, String value) {
		BigDecimal stepDecimal = BigDecimal
				.valueOf(Math.pow(10, -new BigDecimal(step).scale()))
				.stripTrailingZeros().abs();
		BigDecimal valueDecimal = BigDecimal
				.valueOf(Math.pow(10, -new BigDecimal(value).scale()))
				.stripTrailingZeros().abs();

		if (stepDecimal.compareTo(valueDecimal) >= 0) {
			return valueDecimal.toPlainString();
		} else {
			return stepDecimal.toPlainString();
		}
	}

	public static void index() {
		List<Poi> pois = Poi.findAll();
		renderArgs.put("pois", pois);
		render();
	}

	private static boolean isAuthorized(String gToken) {
		GoogleUser googleUser = getGoogleUser(gToken);

		if (googleUser != null && googleUser.accountType != null
				&& !googleUser.accountType.equals(AccountType.NULL)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isSuperUser(String gToken) {
		GoogleUser googleUser = getGoogleUser(gToken);

		if (googleUser != null && googleUser.accountType != null
				&& googleUser.accountType.equals(AccountType.SUPERUSER)) {
			return true;
		} else {
			return false;
		}
	}

	public static void jsonLocationTrace(Long poiId) {
		Poi poi = Poi.findById(poiId);

		if (poi != null) {
			ArrayList<Double[]> coordinates = new ArrayList<Double[]>();

			for (LocationTrace location : poi.locationTrace) {
				Double[] coordinate = { location.latitude, location.longitude };
				coordinates.add(coordinate);
			}
			renderJSON(coordinates);
		}
	}

	public static void jsonPhoto(Long poiId) {
		Poi poi = Poi.findById(poiId);

		if (poi != null) {
			JsonArray jsonArray = new JsonArray();

			for (Photo photo : poi.photos) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("latitude", photo.latitude);
				jsonObject.addProperty("longitude", photo.longitude);
				jsonObject.addProperty("marker_id", photo.id);
				jsonObject.addProperty("marker_type", "photo");
				jsonArray.add(jsonObject);
			}
			renderJSON(jsonArray);
		}
	}

	public static void jsonPoi() {
		List<Poi> pois = Poi.findAll();
		JsonArray jsonArray = new JsonArray();
		for (Poi poi : pois) {
			JsonObject jsonObject = new JsonObject();
			String googleId = null;
			if (poi.googleUser != null) {
				googleId = poi.googleUser.googleId;
			}
			jsonObject.addProperty("google_id", googleId);
			jsonObject.addProperty("latitude", poi.latitude);
			jsonObject.addProperty("longitude", poi.longitude);
			jsonObject.addProperty("marker_id", poi.id);
			jsonObject.addProperty("marker_type", "poi");
			jsonObject.addProperty("task_status", poi.taskStatus.status);
			jsonObject.addProperty("time_stamp", poi.timeStamp);
			jsonArray.add(jsonObject);
		}
		renderJSON(jsonArray);
	}

	public static void registerUser(String gToken) throws Exception {
		String googleId = getGoogleId(gToken);
		String googleMail = getGoogleMail(gToken);
		GoogleUser googleUser = getGoogleUser(gToken);

		if (googleUser == null) {

			if (googleId != null && googleMail != null) {
				googleUser = new GoogleUser();
				googleUser.accountType = AccountType.NULL;
				googleUser.googleId = googleId;
				googleUser.googleMail = googleMail;
				googleUser.save();
			}
		} else if (googleId != null && googleMail != null
				&& googleUser.googleId.equals(googleId)
				&& !googleUser.googleMail.equals(googleMail)) {
			googleUser.googleMail = googleMail;
			googleUser.save();
		}
		if (googleUser == null) {
			badRequest();
		}
	}

	public static void renderPhoto(String gToken, Long photoId) {
		Photo photo = Photo.findById(photoId);

		if (photo != null) {
			renderArgs.put("photo", photo);
			renderArgs.put("isAuthorized", false);
			render("app/views/tags/photo.html");
		} else {
			badRequest();
		}
	}

	public static void renderPhotoFile(Long photoId) {
		Photo photo = Photo.findById(photoId);
		
		if (photo != null && photo.photoBlob != null) {
			response.setContentTypeIfNotSet(photo.photoBlob
					.type());
			renderBinary(photo.photoBlob.get());
		}
	}

	public static void renderPoi(String gToken, Long poiId) {
		Poi poi = Poi.findById(poiId);

		if (poi != null) {
			renderArgs.put("poi", poi);
			boolean isAuthorized = false;

			if (isAuthorized(gToken)) {
				GoogleUser googleUser = getGoogleUser(gToken);

				if (googleUser != null && poi.googleUser != null
						&& poi.googleUser.equals(googleUser) && (poi.taskStatus.equals(TaskStatus.TODO) || (!poi.taskStatus.equals(TaskStatus.SUBMITTED_TO_OSM) && isSuperUser(gToken)))) {
					isAuthorized = true;
				}
			}
			renderArgs.put("isAuthorized", isAuthorized);
			render("app/views/tags/poi.html");
		} else {
			badRequest();
		}
	}

	public static void renderPoiStatus(String gToken, Long poiId) {
		Poi poi = Poi.findById(poiId);

		if (poi != null) {
			renderArgs.put("poi", poi);
			boolean isAuthorized = false;

			if (isAuthorized(gToken)) {
				GoogleUser googleUser = getGoogleUser(gToken);

				if (googleUser != null && poi.googleUser != null
						&& poi.googleUser.equals(googleUser) && (poi.taskStatus.equals(TaskStatus.TODO) || (!poi.taskStatus.equals(TaskStatus.SUBMITTED_TO_OSM) && isSuperUser(gToken)))) {
					isAuthorized = true;
				}
			}
			renderArgs.put("isAuthorized", isAuthorized);
			render("app/views/tags/poi_task_status.html");
		} else {
			badRequest();
		}
	}

	public static void renderPoiPowerTag(String gToken, Long poiId,
			String powerTag) {

		if (powerTag != null && !powerTag.isEmpty() && !powerTag.equals("NULL")) {
			Poi poi = Poi.findById(poiId);

			if (poi != null) {
				GoogleUser googleUser = getGoogleUser(gToken);
				boolean isAuthorized = false;

				if (googleUser != null && poi.googleUser != null
						&& poi.googleUser.equals(googleUser) && (poi.taskStatus.equals(TaskStatus.TODO) || (!poi.taskStatus.equals(TaskStatus.SUBMITTED_TO_OSM) && isSuperUser(gToken)))) {
					isAuthorized = true;
				}
				renderArgs.put("isAuthorized", isAuthorized);

				if (poi.powerTag != null) {
					String poiPowerTagClassName = poi.powerTag.getClass()
							.getSimpleName();

					if (poiPowerTagClassName.equals(powerTag)) {
						renderArgs.put("powerTag", poi.powerTag);
					}
				}
				render("app/views/tags/powerTags/"
						+ powerTag.replaceAll("(\\p{Ll})(\\p{Lu})", "$1_$2").toLowerCase() + ".html");
			} else {
				badRequest();
			}
		}
	}

	public static void renderPoiPowerTagGeneratorMethod(String gToken,
			Long poiId, String source) {

		if (source != null && !source.isEmpty() && !source.equals("NULL")) {
			Poi poi = Poi.findById(poiId);

			if (poi != null) {
				GoogleUser googleUser = getGoogleUser(gToken);
				boolean isAuthorized = false;

				if (googleUser != null && poi.googleUser != null
						&& poi.googleUser.equals(googleUser) && (poi.taskStatus.equals(TaskStatus.TODO) || (!poi.taskStatus.equals(TaskStatus.SUBMITTED_TO_OSM) && isSuperUser(gToken)))) {
					isAuthorized = true;
				}
				renderArgs.put("isAuthorized", isAuthorized);

				if (poi.powerTag != null
						&& poi.powerTag.getClass().equals(Generator.class)) {
					Generator generator = (Generator) poi.powerTag;

					if (generator.source != null
							&& generator.source.name().equals(source)
							&& generator.method != null) {
						renderArgs.put("method", generator.method);
					}
				}
				render("app/views/tags/powerTags/fields/generator/method/"
						+ source.replaceAll(" ", "_").toLowerCase() + ".html");
			} else {
				badRequest();
			}
		}
	}

	public static void renderPoiPowerTagGeneratorType(String gToken,
			Long poiId, String sourceMethod) {

		if (sourceMethod != null && !sourceMethod.isEmpty()
				&& !sourceMethod.equals("NULL")) {
			Poi poi = Poi.findById(poiId);

			if (poi != null) {
				GoogleUser googleUser = getGoogleUser(gToken);
				boolean isAuthorized = false;

				if (googleUser != null && poi.googleUser != null
						&& poi.googleUser.equals(googleUser) && (poi.taskStatus.equals(TaskStatus.TODO) || (!poi.taskStatus.equals(TaskStatus.SUBMITTED_TO_OSM) && isSuperUser(gToken)))) {
					isAuthorized = true;
				}
				renderArgs.put("isAuthorized", isAuthorized);

				if (poi.powerTag != null
						&& poi.powerTag.getClass().equals(Generator.class)) {
					Generator generator = (Generator) poi.powerTag;

					if (generator.source != null && generator.method != null) {
						String poiSourceMethod = generator.source.name() + "_"
								+ generator.method.name();

						if (poiSourceMethod.toLowerCase().equals(sourceMethod.toLowerCase())
								&& generator.type != null) {
							renderArgs.put("type", generator.type);
						}
					}
				}
				try {
					render("app/views/tags/powerTags/fields/generator/type/"
							+ sourceMethod.replaceAll(" ", "_").toLowerCase()
							+ ".html");
				} catch (Exception e) {
					render("app/views/Application/about_blank.html");
				}
			} else {
				badRequest();
			}
		}
	}

	public static void renderSettingsModal(String gToken) {

		if (isSuperUser(gToken)) {
			render("app/views/tags/settings_modal.html");
		}
	}

	public static void renderSettingsUserAdministration(String gToken) {

		if (isSuperUser(gToken)) {
			List<GoogleUser> allUsers = GoogleUser.findAll();
			ArrayList<GoogleUser> users = new ArrayList<GoogleUser>();

			for (GoogleUser user : allUsers) {
				if (!user.accountType.equals(AccountType.SUPERUSER)) {
					users.add(user);
				}
			}
			renderArgs.put("users", users);
			render("app/views/tags/settings_user_administration.html");
		} else {
			unauthorized();
		}
	}

	public static void reservePoi(String gToken, Long poiId) {

		if (isAuthorized(gToken)) {
			GoogleUser googleUser = getGoogleUser(gToken);
			Poi poi = Poi.findById(poiId);

			if (googleUser != null && poi != null) {

				if (poi.googleUser == null) {
					List<Poi> pois = Poi.find("byGoogleUser", googleUser)
							.fetch();
					for (Poi currentPoi : pois) {
						currentPoi.googleUser = null;
						currentPoi.store();
					}
					poi.googleUser = googleUser;

					if (poi.store()) {
						ok();
					} else {
						forbidden();
					}
				}
			} else {
				badRequest();
			}
		}
	}

	public static String toLowerString(String str) {
		
		try {
			if (Character.isUpperCase(str.codePointAt(0)) && Character.isLowerCase(str.codePointAt(1))) {
				return str.toLowerCase();
			}
		} catch (Exception e) {
		}
		return str;
	}

	public static void updatePoi() throws ParseException,
			ClassNotFoundException {
		String gToken = params.get("gToken");

		if (isAuthorized(gToken)) {
			GoogleUser googleUser = getGoogleUser(gToken);
			Poi poi = Poi.findById(params.get("poi_id", Long.class));

			if (googleUser != null && poi != null && poi.googleUser != null
					&& poi.googleUser.equals(googleUser)) {
				TaskStatus previousTaskStatus = poi.taskStatus;
				TaskStatus currentTaskStatus;
				poi.street = params.get("poi_street");
				poi.housenumber = params.get("poi_housenumber");
				poi.postcode = params.get("poi_postcode", Integer.class);
				poi.city = params.get("poi_city");
				poi.state = params.get("poi_state");
				poi.country = params.get("poi_country");
				poi.latitude = params.get("poi_latitude", Double.class);
				poi.longitude = params.get("poi_longitude", Double.class);
				
				Boolean poi_task_done = params.get("poi_task_done", Boolean.class);
				Boolean poi_task_ready = params.get("poi_task_ready", Boolean.class);
				Boolean poi_task_submitted = params.get("poi_task_submitted", Boolean.class);
				if (poi_task_done != null && poi_task_done) {
					if (poi_task_ready != null && poi_task_ready) {
						if (poi_task_submitted != null && poi_task_submitted) {
							poi.taskStatus = TaskStatus.SUBMITTED_TO_OSM;
							currentTaskStatus = TaskStatus.SUBMITTED_TO_OSM;
						} else {
							poi.taskStatus = TaskStatus.READY_TO_SUBMIT_TO_OSM;
							currentTaskStatus = TaskStatus.READY_TO_SUBMIT_TO_OSM;
						}
					} else {
						poi.taskStatus = TaskStatus.DONE;
						currentTaskStatus = TaskStatus.DONE;
					}
				} else {
					poi.taskStatus = TaskStatus.TODO;
					currentTaskStatus = TaskStatus.TODO;
				}
				
				String powerTagParam = params
						.get("poi_power_tag");
				if (poi.powerTag != null) {
					poi.powerTag.delete();
					poi.powerTag = null;
				}
				if (powerTagParam != null && !powerTagParam.equals("NULL")) {
					Class powerTagClass = Class.forName("models.powerTags."
							+ powerTagParam);
					if (powerTagClass.equals(Cable.class)) {
						poi.powerTag = new Cable(poi);
						Cable cable = (Cable) poi.powerTag;
						cable.cables = params.get("cables");
						cable.circuits = params.get("circuits", Byte.class);
						cable.frequency = params.get("frequency",
								Float.class);
						cable.location = Cable.LocationEnum.valueOf(params
								.get("location"));
						cable.name = params.get("name");
						cable.operator = new Operator(cable);
						cable.operator.name = params.get("operator_name");
						cable.operator.type = TypeEnum.valueOf(params
								.get("operator_type"));
						cable.ref = params.get("ref");
						cable.voltage = params.get("voltage", Float.class);
					} else if (powerTagClass
							.equals(CableDistributionCabinet.class)) {
						poi.powerTag = new CableDistributionCabinet(poi);
						CableDistributionCabinet cableDistributionCabinet = (CableDistributionCabinet) poi.powerTag;
						cableDistributionCabinet.operator = new Operator(
								cableDistributionCabinet);
						cableDistributionCabinet.operator.name = params.get(
								"operator_name");
						cableDistributionCabinet.operator.type = TypeEnum
								.valueOf(params
										.get("operator_type"));
						cableDistributionCabinet.ref = params.get("ref");
						cableDistributionCabinet.voltage = params.get(
								"voltage", Float.class);
					} else if (powerTagClass.equals(Converter.class)) {
						poi.powerTag = new Converter(poi);
						Converter converter = (Converter) poi.powerTag;
						converter.converter = ConverterEnum.valueOf(params
								.get("converter"));
						converter.poles = params.get("poles", Byte.class);
						converter.rating = params.get("rating", Float.class);
						converter.voltage = params.get("voltage", Float.class);
					} else if (powerTagClass.equals(Generator.class)) {
						poi.powerTag = new Generator(poi);
						Generator generator = (Generator) poi.powerTag;
						generator.name = params.get("name");
						generator.operator = new Operator(generator);
						generator.operator.name = params.get("operator_name");
						generator.operator.type = TypeEnum.valueOf(params
								.get("operator_type"));
						generator.output = OutputEnum.valueOf(params
								.get("output"));
						generator.output_value = params.get("output_value");
						generator.plant = PlantEnum.valueOf(params
								.get("plant"));
						String source = params.get("source");
						String method = params.get("method");
						String type = params.get("type");
						if (source != null) {
							generator.source = SourceEnum.valueOf(source);
						}
						if (method != null) {
							generator.method = MethodEnum.valueOf(method);
						}
						if (type != null) {
							generator.type = Generator.TypeEnum.valueOf(type);
						}
					} else if (powerTagClass.equals(Line.class)) {
						poi.powerTag = new Line(poi);
						Line line = (Line) poi.powerTag;
						line.cables = params.get("cables");
						line.operator = new Operator(line);
						line.operator.name = params.get("operator_name");
						line.operator.type = TypeEnum.valueOf(params
								.get("operator_type"));
						line.ref = params.get("ref");
						line.voltage = params.get("voltage", Float.class);
						line.wires = WiresEnum.valueOf(params
								.get("wires"));
					} else if (powerTagClass.equals(MinorLine.class)) {
						poi.powerTag = new MinorLine(poi);
						MinorLine minorLine = (MinorLine) poi.powerTag;
						minorLine.cables = params.get("cables");
						minorLine.name = params.get("name");
						minorLine.operator = new Operator(minorLine);
						minorLine.operator.name = params.get("operator_name");
						minorLine.operator.type = TypeEnum.valueOf(params
								.get("operator_type"));
						minorLine.ref = params.get("ref");
						minorLine.voltage = params.get("voltage", Float.class);
					} else if (powerTagClass.equals(Plant.class)) {
						poi.powerTag = new Plant(poi);
						Plant plant = (Plant) poi.powerTag;
						plant.landuse = LanduseEnum.valueOf(params
								.get("landuse"));
						plant.name = params.get("name");
						plant.operator = new Operator(plant);
						plant.operator.name = params.get("operator_name");
						plant.operator.type = TypeEnum.valueOf(params
								.get("operator_type"));
						plant.output = OutputEnum.valueOf(params
								.get("output"));
						plant.output_value = params.get("output_value");
						String start_date = params.get("start_date");
						if (!start_date.isEmpty()) {
							plant.start_date = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm").parse(
									start_date.replace("T", " ")).getTime();
						}
					} else if (powerTagClass.equals(Pole.class)) {
						poi.powerTag = new Pole(poi);
						Pole pole = (Pole) poi.powerTag;
						pole.ref = params.get("ref");
					} else if (powerTagClass.equals(Substation.class)) {
						poi.powerTag = new Substation(poi);
						Substation substation = (Substation) poi.powerTag;
						substation.gas_insulated = BooleanEnum.valueOf(params
								.get("gas_insulated"));
						substation.location = Substation.LocationEnum
								.valueOf(params.get("location"));
						substation.name = params.get("name");
						substation.operator = new Operator(substation);
						substation.operator.name = params.get("operator_name");
						substation.operator.type = TypeEnum.valueOf(params
								.get("operator_type"));
						substation.ref = params.get("ref");
						substation.type = Substation.TypeEnum.valueOf(params
								.get("type"));
						substation.voltage = params.get("voltage", Float.class);
					} else if (powerTagClass.equals(Switch.class)) {
						poi.powerTag = new Switch(poi);
					} else if (powerTagClass.equals(Tower.class)) {
						poi.powerTag = new Tower(poi);
						Tower tower = (Tower) poi.powerTag;
						tower.color = Color.decode(params.get("color",
								String.class));
						tower.design = DesignEnum.valueOf(params
								.get("design"));
						tower.design_incomplete = models.powerTags.Tower.BooleanEnum.valueOf(params
								.get("design_incomplete"));
						tower.design_name = params.get("design_name");
						tower.height = params.get("height", Float.class);
						tower.material = MaterialEnum.valueOf(params
								.get("material"));
						tower.ref = params.get("ref");
						tower.structure = StructureEnum.valueOf(params
								.get("structure"));
						tower.transition = models.powerTags.Tower.BooleanEnum.valueOf(params
								.get("transition"));
						tower.type = Tower.TypeEnum.valueOf(params
								.get("type"));
					} else if (powerTagClass.equals(Transformer.class)) {
						poi.powerTag = new Transformer(poi);
						Transformer transformer = (Transformer) poi.powerTag;
						transformer.frequency = params.get("frequency",
								Float.class);
						transformer.location = Transformer.LocationEnum
								.valueOf(params.get("location"));
						transformer.phases = params
								.get("phases", Integer.class);
						transformer.rating = params.get("rating", Float.class);
						transformer.type = Transformer.TypeEnum.valueOf(params
								.get("type"));
						transformer.voltage = params
								.get("voltage", Float.class);
					}
				}
				if (previousTaskStatus.equals(TaskStatus.READY_TO_SUBMIT_TO_OSM) && currentTaskStatus.equals(TaskStatus.SUBMITTED_TO_OSM)) {
					OsmNode osmNode = OpenStreetMap.createNode(poi);
					
					if (osmNode != null) {
						poi.osmNode = osmNode;
					} else {
						badRequest();
						return;
					}
				}
				if (poi.store()) {
					WebSocket.publishAddMarkerEvent(googleUser, poi);
					ok();
				} else {
					forbidden();
				}
			} else {
				badRequest();
			}
		} else {
			unauthorized();
		}
	}

	public static void updateSettings() {

		if (isSuperUser(params.get("gToken"))) {
			List<GoogleUser> users = GoogleUser.findAll();

			for (GoogleUser user : users) {

				if (!user.accountType.equals(AccountType.SUPERUSER)) {
					String googleId = user.googleId;
					Boolean isUser = params.get(googleId, Boolean.class);

					if (isUser != null && isUser) {
						user.accountType = AccountType.USER;
					} else {
						user.accountType = AccountType.NULL;
					}
					user.save();
				}
			}
		} else {
			unauthorized();
		}
	}
}