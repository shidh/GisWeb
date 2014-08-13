package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Switch extends PowerTag {

	public Switch(Poi poi) {
		super(poi);
	}
}