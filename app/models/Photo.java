package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import play.db.jpa.Blob;

@Entity
public class Photo {

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
	public Blob photoBlob;
	public String provider;
	public Long time;

	public Photo(Poi poi) {
		this.poi = poi;
	}
}
