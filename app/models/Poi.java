package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import models.powerTags.*;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Poi extends Model {

	@ElementCollection
	public List<Blob> photos;

	public float accuracy;
	public double altitude;
	public float bearing;
	public double latitude;
	public double longitude;
	public String provider;
	public long time;

	public Cable powerTagCable;
	public CableDistributionCabinet powerTagCableDistributionCabinet;
	public Converter powerTagConverter;
	public Generator powerTagGenerator;
	public Line powerTagLine;
	public MinorLine powerTagMinorLine;
	public Plant powerTagPlant;
	public Pole powerTagPole;
	public Substation powerTagSubstation;
	public Switch powerTagSwitch;
	public Tower powerTagTower;
	public Transformer powerTagTransformer;

	public static ArrayList<String> getPowerTagFields() {
		Field[] fields = Poi.class.getFields();
		ArrayList<String> list = new ArrayList<String>();

		for (Field field : fields) {
			String fieldName = field.getName();

			if (fieldName.contains("powerTag")) {
				list.add(fieldName.replace("powerTag", "")
						.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2")
						.toLowerCase());
			}
		}

		return list;
	}

	public void setPoi(String accuracy, String altitude, String bearing,
			String latitude, String longitude, String provider, String time) {
		this.accuracy = Float.parseFloat(accuracy);
		this.altitude = Double.parseDouble(altitude);
		this.bearing = Float.parseFloat(bearing);
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);
		this.provider = provider;
		this.time = Long.parseLong(time);
	}
}