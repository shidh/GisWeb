package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Line extends PowerTag {

	public enum WiresEnum {
		NULL("null"), SINGLE("Single"), DOUBLE("Double"), TRIPLE("Triple"), QUAD("Quad"), FIVEFOLD(
				"Fivefold"), SIXFOLD("Sixfold"), EIGHTFOLD("Eightfold");

		public final String name;

		WiresEnum(String name) {
			this.name = name;
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