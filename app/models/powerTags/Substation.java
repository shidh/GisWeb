package models.powerTags;

import javax.persistence.Entity;

import models.powerTags.types.Operator;
import play.db.jpa.Model;

@Entity
public class Substation extends Model {

	public Boolean gas_insulated;
	public LocationEnum location;
	public String name;
	public Operator operator;
	public String ref;
	public SubstationTypeEnum substationType;
	public int voltage;

	public enum LocationEnum {
		INDOOR("indoor"), KIOSK("kiosk"), OUTDOOR("outdoor"), PLATFORM(
				"platform"), ROOFTOP("rooftop"), UNDERGROUND("underground");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}

	public enum SubstationTypeEnum {
		COMPENSATION("compensation"), CONVERTER("converter"), DISTRIBUTION(
				"distribution"), INDUSTRIAL("industrial"), MINOR_DISTRIBUTION(
				"minor distribution"), TRACTION("traction"), TRANSITION(
				"transition"), TRANSMISSION("transmission");

		public final String name;

		SubstationTypeEnum(String name) {
			this.name = name;
		}
	}
}