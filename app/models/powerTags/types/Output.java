package models.powerTags.types;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.PowerTag;
import play.db.jpa.Model;

@Entity
public class Output extends Model {

	public enum OutputEnum {
		BIOGAS("Biogas", "biogas"), COLD_AIR("Cold Air", "cold_air"), COLD_WATER("Cold Water", "cold_water"), COMPRESSED_AIR(
				"Compressed Air", "compressed_air"), ELECTRICITY("Electricity", "electricity"), HEAT("Heat", "heat"), HOT_AIR(
				"Hot Air", "hot_air"), HOT_WATER("Hot Water", "hot_water"), NULL(" ", null), STEAM("Steam", "steam"), VACUUM(
				"Vacuum", "vacuum");

		public final String name;
		public final String osm;

		OutputEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	@OneToOne
	public PowerTag powerTag;

	public OutputEnum output;

	public Output(PowerTag powerTag) {
		this.powerTag = powerTag;
	}
}