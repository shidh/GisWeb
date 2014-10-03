package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.powerTags.types.Operator;

@Entity
public class CableDistributionCabinet extends PowerTag {

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public Float voltage;

	public CableDistributionCabinet(Poi poi) {
		super(poi);
	}
}