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
		INDUSTRIAL("Industrial"), NULL("null"), WIND_FARM("Wind Farm");

		public final String name;

		LanduseEnum(String name) {
			this.name = name;
		}
	}

	public LanduseEnum landuse;
	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public OutputEnum output;
	public Long start_date;

	public Plant(Poi poi) {
		super(poi);
	}
}