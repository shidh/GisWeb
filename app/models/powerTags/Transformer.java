package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Transformer extends PowerTag {

	public enum LocationEnum {
		NULL(" ", null), OVERGROUND("Overground", "overground"), ROOF("Roof", "roof"), UNDERGROUND(
				"Underground", "underground"), UNDERWATER("Underwater", "underwater");

		public final String name;
		public final String osm;

		LocationEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum TypeEnum {
		AUTO("Auto", "auto"), CONVERTER("Converter", "converter"), DISTRIBUTION("Distribution", "distribution"), GENERATOR(
				"Generator", "generator"), NULL(" ", null), PHASE_ANGLE_REGULATOR(
				"Phase Angle Regulator", "phase_angle_regulator"), TRACTION("Traction", "traction"), YES("Yes", "yes");

		public final String name;
		public final String osm;

		TypeEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public Float frequency;
	public LocationEnum location;
	public Integer phases;
	public Float rating;
	public TypeEnum type;
	public Float voltage;

	public Transformer(Poi poi) {
		super(poi);
	}
}