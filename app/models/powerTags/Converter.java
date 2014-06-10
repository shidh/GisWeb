package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Converter extends PowerTag {

	public ConverterEnum converter;
	public byte poles;
	public int rating;
	public int voltage;

	public Converter(Poi poi) {
		super(poi);
	}

	public enum ConverterEnum {
		BACK_TO_BACK("back-to-back"), LCC("lcc"), VSC("vsc");

		public final String name;

		ConverterEnum(String name) {
			this.name = name;
		}
	}
}