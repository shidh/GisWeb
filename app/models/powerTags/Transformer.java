package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Transformer extends PowerTag {

	public enum LocationEnum {
		INDOOR("indoor"), OVERGROUND("overground"), UNDERGROUND("underground"), UNDERWATER(
				"underwater");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}

	public enum TransformerEnum {
		AUTO("auto"), CONVERTER("converter"), DISTRIBUTION("distribution"), GENERATOR(
				"generator"), PHASE_ANGLE_REGULATOR("phase angle regulator"), TRACTION(
				"traction"), YES("yes");

		public final String name;

		TransformerEnum(String name) {
			this.name = name;
		}
	}

	public Float frequency;
	public LocationEnum location;
	public Integer phases;
	public Long rating;
	public TransformerEnum transformerType;
	public Float voltage;

	public Transformer(Poi poi) {
		super(poi);
	}
}