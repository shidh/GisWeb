package models.powerTags.types;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.PowerTag;
import play.db.jpa.Model;

@Entity
public class Output extends Model {

	public enum OutputEnum {
		COLD_AIR("Cold Air"), COLD_WATER("Cold Water"), COMPRESSED_AIR(
				"Compressed Air"), ELECTRICITY("Electricity"), HOT_AIR(
				"Hot Air"), HOT_WATER("Hot Water"), NULL("null"), STEAM("Steam"), VACUUM(
				"Vacuum");

		public final String name;

		OutputEnum(String name) {
			this.name = name;
		}
	}

	@OneToOne
	public PowerTag powerTag;

	public OutputEnum output;

	public Output(PowerTag powerTag) {
		this.powerTag = powerTag;
	}
}