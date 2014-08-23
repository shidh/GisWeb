package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Cable extends PowerTag {

	public enum LocationEnum {
		OVERGROUND("Overground"), UNDERGROUND("Underground"), UNDERWATER(
				"Underwater");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}

	public String cables;
	public Byte circuits;
	public LocationEnum location;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public Float voltage;

	public Cable(Poi poi) {
		super(poi);
	}
}