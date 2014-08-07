package models;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Poi extends Model {

	@ElementCollection
	public List<Blob> photos;

	public Float accuracy;
	public Double altitude;
	public Float bearing;
	public Double latitude;
	public Double longitude;
	public String provider;
	public Long time;

	@OneToOne(mappedBy = "poi", cascade = CascadeType.ALL)
	public PowerTag powerTag;

	public Poi(String accuracy, String altitude, String bearing,
			String latitude, String longitude, String provider, String time) {
		this.accuracy = Float.parseFloat(accuracy);
		this.altitude = Double.parseDouble(altitude);
		this.bearing = Float.parseFloat(bearing);
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);
		this.provider = provider;
		this.time = Long.parseLong(time);
		this.photos = new ArrayList<Blob>();
	}

	/**
	 * Based on the work of Mike B.
	 * (http://mike.shannonandmike.net/2009/09/02/java
	 * -reflecting-to-get-all-classes-in-a-package/)
	 * 
	 * @return
	 */
	public static ArrayList<String> getPowerTagFields() {
		ArrayList<String> list = new ArrayList<String>();
		String packageName = "/models/powerTags";
		URL directoryURL = Thread.currentThread().getContextClassLoader()
				.getResource(packageName);
		String directoryString = directoryURL.getFile();
		File directory = new File(directoryString);
		String[] files = directory.list();

		for (String fileName : files) {
			list.add(fileName.replace(".java", "")
					.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2").toLowerCase());
		}
		Collections.sort(list);
		return list;
	}
}