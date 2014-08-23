package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Substation extends PowerTag {

	public enum LocationEnum {
		INDOOR("Indoor"), KIOSK("Kiosk"), OUTDOOR("Outdoor"), PLATFORM(
				"Platform"), ROOFTOP("Rooftop"), UNDERGROUND("Underground");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}

	public enum TypeEnum {
		COMPENSATION("Compensation"), CONVERTER("Converter"), DISTRIBUTION(
				"Distribution"), INDUSTRIAL("Industrial"), MINOR_DISTRIBUTION(
				"Minor Distribution"), TRACTION("Traction"), TRANSITION(
				"Transition"), TRANSMISSION("Transmission");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
		}
	}

	public Boolean gas_insulated;
	public LocationEnum location;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public TypeEnum type;
	public Float voltage;

	public Substation(Poi poi) {
		super(poi);
	}
}