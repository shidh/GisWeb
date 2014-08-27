package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class LocationTrace extends Model {

	@ManyToOne
	public Poi poi;

	public Float accuracy;
	public Double altitude;
	public Float bearing;
	public Double latitude;
	public Double longitude;
	public String provider;
	public Long time;

	public LocationTrace(Poi poi) {
		this.poi = poi;
	}
}