package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LocationTrace {

	@Id
	@GeneratedValue
	public Long id;

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