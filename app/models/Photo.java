package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Blob;

@Entity
public class Photo {

	@ManyToOne
	public Poi poi;
	
	@Id
	@GeneratedValue
	public Long id;
	
	public Float accuracy;
	public Double altitude;
	public Float bearing;
	public Double latitude;
	public Double longitude;
	public Blob photoBlob;
	public String provider;
	public Long time;
}
