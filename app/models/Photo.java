package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Photo extends Model {

	@ManyToOne
	public Poi poi;

	public Float accuracy;
	public Double altitude;
	public Float bearing;
	public Double latitude;
	public Double longitude;
	public Blob photoBlob;
	public String provider;
	public Long time;

	public Photo(Poi poi) {
		this.poi = poi;
	}
}
