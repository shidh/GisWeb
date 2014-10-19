package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.powerTags.types.Operator;
import models.powerTags.types.Output.OutputEnum;

@Entity
public class Plant extends PowerTag {

	public enum LanduseEnum {
		INDUSTRIAL("Industrial", "industrial"), NULL(" ", null), WIND_FARM("Wind Farm", "wind_farm");

		public final String name;
		public final String osm;

		LanduseEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public LanduseEnum landuse;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public OutputEnum output;
	public String output_value;
	public Long start_date;

	public Plant(Poi poi) {
		super(poi);
	}
}