package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Pole extends PowerTag {

	public String ref;

	public Pole(Poi poi) {
		super(poi);
	}
}