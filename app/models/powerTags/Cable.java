package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Cable extends PowerTag {

	public String cables;
	public Byte circuits;
	public LocationEnum location;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public Integer voltage;

	public Cable(Poi poi) {
		super(poi);
	}

	public enum LocationEnum {
		OVERGROUND("overground"), UNDERGROUND("underground"), UNDERWATER("underwater");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}
}