package models.powerTags.types;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.PowerTag;
import play.db.jpa.Model;

@Entity
public class Operator extends Model {

	public enum TypeEnum {
		COMMUNITY("Community", "community"), GOVERNMENT("Government", "government"), NGO("NGO", "ngo"), NULL(
				" ", null), PRIVATE("Private", "private"), PUBLIC("Public", "public"), RELIGIOUS(
				"Religious", "religious");

		public final String name;
		public final String osm;

		TypeEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	@OneToOne
	public PowerTag powerTag;

	public String name;
	public TypeEnum type;

	public Operator(PowerTag powerTag) {
		this.powerTag = powerTag;
	}
}