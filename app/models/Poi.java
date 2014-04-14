package models;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

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
}