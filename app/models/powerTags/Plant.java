package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Plant extends PowerTag {

	public enum LanduseEnum {
		INDUSTRIAL("industrial"), WIND_FARM("wind farm");

		public final String name;

		LanduseEnum(String name) {
			this.name = name;
		}
	}

	public enum OutputEnum {
		COLD_AIR("cold air"), COLD_WATER("cold water"), COMPRESSED_AIR(
				"compressed air"), ELECTRICITY("electricity"), HOT_AIR(
				"hot air"), HOT_WATER("hot water"), STEAM("steam"), VACUUM(
				"vacuum");

		public final String name;

		OutputEnum(String name) {
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