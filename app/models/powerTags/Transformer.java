package models.powerTags;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Transformer extends Model {

	public Float frequency;
	public LocationEnum location;
	public int phases;
	public int rating;
	public TransformerEnum transformerType;
	public int voltage;

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
}