package models.powerTags;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Converter extends PowerTag {

	public enum ConverterEnum {
		BACK_TO_BACK("Back-To-Back", "back-to-back"), LCC("LCC", "lcc"), NULL(" ", null), VSC("VSC", "vsc");

		public final String name;
		public final String osm;

		ConverterEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public ConverterEnum converter;
	public Byte poles;
	public Float rating;
	public Float voltage;

	public Converter(Poi poi) {
		super(poi);
	}
}