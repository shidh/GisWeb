package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Substation extends PowerTag {

	public Boolean gas_insulated;
	public LocationEnum location;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public SubstationTypeEnum substationType;
	public Float voltage;

	public Substation(Poi poi) {
		super(poi);
	}

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