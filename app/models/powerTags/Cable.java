package models.powerTags;

import javax.persistence.Entity;

import models.powerTags.types.Operator;
import play.db.jpa.Model;

@Entity
public class Cable extends Model {

	public byte[] cables;
	public byte circuits;
	public LocationEnum location;
	public String name;
	public Operator operator;
	public String ref;
	public int voltage;

	public enum LocationEnum {
		OVERGROUND("overground"), UNDERGROUND("underground"), UNDERWATER(
				"underwater");

		public final String name;

		LocationEnum(String name) {
			this.name = name;
		}
	}
}