package models.powerTags;

import java.awt.Color;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Tower extends PowerTag {

	public Color color;
	public DesignEnum design;
	public int height;
	public MaterialEnum material;
	public String ref;
	public StructureEnum structure;
	public TypeEnum type;

	public Tower(Poi poi) {
		super(poi);
	}

	public enum DesignEnum {
		ASYMMETRIC("asymmetric"), BARREL("barrel"), BIPOLE("bipole"), DELTA("delta"), DELTA_TWO_LEVEL("delta and two-level"), DONAU("donau"), DONAU_ONE_LEVEL("donau and one-level"), FLAG("flag"), FOUR_LEVEL(
				"four-level"), GUYED_H_FRAME("guyed h-frame"), GUYED_V_FRAME("guyed v-frame"), H_FRAME("h-frame"), INCOMPLETE("incomplete"), MONOPOLAR("monopolar"), NAME("name"), NINE_LEVEL(
				"nine-level"), ONE_LEVEL("one-level"), PORTAL("portal"), PORTAL_THREE_LEVEL("portal and three-level"), PORTAL_TWO_LEVEL("portal and two-level"), SIX_LEVEL("six-level"), THREE_LEVEL(
				"three-level"), TRIANGLE("triangle"), TWO_LEVEL("two-level"), X_FRAME("x-frame"), Y_FRAME("y-frame");

		public final String name;

		DesignEnum(String name) {
			this.name = name;
		}
	}

	public enum MaterialEnum {
		CONCRETE("concrete"), STEEL("steel"), WOOD("wood");

		public final String name;

		MaterialEnum(String name) {
			this.name = name;
		}
	}

	public enum StructureEnum {
		LATTICE("lattice"), SOLID("solid"), TUBULAR("tubular");

		public final String name;

		StructureEnum(String name) {
			this.name = name;
		}
	}

	public enum TypeEnum {
		ANCHOR("anchor"), BRANCH("branch"), CROSSING("crossing"), SUSPENSION("suspension"), TERMINATION("termination"), TRANSITION("transition"), TRANSPOSING("transposing");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
		}
	}
}
