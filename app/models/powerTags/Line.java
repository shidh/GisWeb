package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.powerTags.types.Operator;

@Entity
public class Line extends PowerTag {

	public enum WiresEnum {
		NULL(" ", null), SINGLE("Single", "single"), DOUBLE("Double", "double"), TRIPLE("Triple", "triple"), QUAD(
				"Quad", "quad"), FIVEFOLD("Fivefold", "fivefold"), SIXFOLD("Sixfold", "sixfold"), EIGHTFOLD(
				"Eightfold", "eightfold");

		public final String name;
		public final String osm;

		WiresEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public String cables;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public String ref;
	public Float voltage;
	public WiresEnum wires;

	public Line(Poi poi) {
		super(poi);
	}
}