package models.types;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import play.db.jpa.Model;

@Entity
public class Operator extends Model {

	@OneToOne
	public PowerTag powerTag;
	
	public String name;
	public TypeEnum type;

	public enum TypeEnum {
		COMMUNITY("community"), GOVERNMENT("government"), NGO("ngo"), PRIVATE("private"), PUBLIC("public"), RELIGIOUS("religious");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
		}
	}
	
	public Operator(PowerTag powerTag) {
		this.powerTag = powerTag;
	}
}