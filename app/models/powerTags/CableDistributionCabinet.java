package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class CableDistributionCabinet extends PowerTag {

	public Operator operator;
	public String ref;
	public int voltage;

	public CableDistributionCabinet(Poi poi) {
		super(poi);
	}
}