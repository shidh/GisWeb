package models.types;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.PowerTag;
import play.db.jpa.Model;

@Entity
public class Operator extends Model {

	public enum TypeEnum {
		COMMUNITY("Community"), GOVERNMENT("Government"), NGO("NGO"), NULL("null"), PRIVATE(
				"Private"), PUBLIC("Public"), RELIGIOUS("Religious");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
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