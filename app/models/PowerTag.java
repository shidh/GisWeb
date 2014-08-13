package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class PowerTag extends Model {

	@OneToOne
	public Poi poi;

	public PowerTag(Poi poi) {
		this.poi = poi;
	}
}
