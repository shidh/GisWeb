package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class MinorLine extends PowerTag {

	public String cables;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public Float voltage;

	public MinorLine(Poi poi) {
		super(poi);
	}
}