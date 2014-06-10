package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Cable extends PowerTag {

	public String cables;
	public byte circuits;
	public LocationEnum location;
	public String name;
	public Operator operator;
	public String ref;
	public int voltage;

	public Cable(Poi poi) {
		super(poi);
	}

	public enum LocationEnum {
		OVERGROUND("overground"), UNDERGROUND("underground"), UNDERWATER(
				"underwater");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}
}