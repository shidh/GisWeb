package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class MinorLine extends PowerTag {

	public byte cables;
	public String name;
	public Operator operator;
	public String ref;
	public int voltage;

	public MinorLine(Poi poi) {
		super(poi);
	}
}