package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Transformer extends PowerTag {

	public enum LocationEnum {
		INDOOR("Indoor"), OVERGROUND("Overground"), UNDERGROUND("Underground"), UNDERWATER(
				"Underwater");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}

	public enum TypeEnum {
		AUTO("Auto"), CONVERTER("Converter"), DISTRIBUTION("Distribution"), GENERATOR(
				"Generator"), PHASE_ANGLE_REGULATOR("Phase Angle Regulator"), TRACTION(
				"Traction"), YES("Yes");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
		}
	}

	public Float frequency;
	public LocationEnum location;
	public Integer phases;
	public Long rating;
	public TypeEnum type;
	public Float voltage;

	public Transformer(Poi poi) {
		super(poi);
	}
}