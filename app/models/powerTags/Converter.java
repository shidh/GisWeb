package models.powerTags;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Converter extends Model {

	public ConverterEnum converter;
	public byte poles;
	public int rating;
	public int voltage;

	public enum ConverterEnum {
		BACK_TO_BACK("back-to-back"), LCC("lcc"), VSC("vsc");

		public final String name;

		ConverterEnum(String name) {
			this.name = name;
		}
	}
}