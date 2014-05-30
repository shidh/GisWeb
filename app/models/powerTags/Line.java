package models.powerTags;

import javax.persistence.Entity;

import models.powerTags.types.Operator;
import play.db.jpa.Model;

@Entity
public class Line extends Model {

	public byte[] cables;
	public Operator operator;
	public String ref;
	public int voltage;
	public WiresEnum wires;

	public enum WiresEnum {
		SINGLE("single"), DOUBLE("double"), TRIPLE("triple"), QUAD("quad"), FIVEFOLD(
				"fivefold"), SIXFOLD("sixfold"), EIGHTFOLD("eightfold");

		public final String name;

		WiresEnum(String name) {
			this.name = name;
		}
	}
}