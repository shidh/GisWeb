package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.powerTags.types.Operator;

@Entity
public class Substation extends PowerTag {

	public enum BooleanEnum {
		FALSE("False", "no"), NULL(" ", null), TRUE("True", "yes");

		public final String name;
		public final String osm;

		BooleanEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum LocationEnum {
		INDOOR("Indoor", "indoor"), KIOSK("Kiosk", "kiosk"), NULL(" ", null), OUTDOOR("Outdoor", "outdoor"), PLATFORM(
				"Platform", "platform"), ROOFTOP("Rooftop", "rooftop"), UNDERGROUND("Underground", "underground");

		public final String name;
		public final String osm;

		LocationEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum TypeEnum {
		COMPENSATION("Compensation", "compensation"), CONVERTER("Converter", "converter"), DISTRIBUTION(
				"Distribution", "distribution"), INDUSTRIAL("Industrial", "industrial"), MINOR_DISTRIBUTION(
				"Minor Distribution", "minor_distribution"), NULL(" ", null), TRACTION("Traction", "traction"), TRANSITION(
				"Transition", "transition"), TRANSMISSION("Transmission", "transmission");

		public final String name;
		public final String osm;

		TypeEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public BooleanEnum gas_insulated;
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