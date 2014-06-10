package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Line extends PowerTag {

	public byte cables;
	public Operator operator;
	public String ref;
	public int voltage;
	public WiresEnum wires;

	public Line(Poi poi) {
		super(poi);
	}

	public enum WiresEnum {
		SINGLE("single"), DOUBLE("double"), TRIPLE("triple"), QUAD("quad"), FIVEFOLD(
				"fivefold"), SIXFOLD("sixfold"), EIGHTFOLD("eightfold");

		public final String name;

		WiresEnum(String name) {
			this.name = name;
		}
	}
}