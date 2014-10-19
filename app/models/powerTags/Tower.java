package models.powerTags;

import java.awt.Color;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Tower extends PowerTag {

	public enum BooleanEnum {
		FALSE("False", "no"), NULL(" ", null), TRUE("True", "yes");

		public final String name;
		public final String osm;

		BooleanEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum DesignEnum {
		ASYMMETRIC("Asymmetric", "asymmetric"), BARREL("Barrel", "barrel"), BIPOLE("Bipole", "bipole"), DELTA(
				"Delta", "delta"), DELTA_TWO_LEVEL("Delta And Two-Level", "delta_two-level"), DONAU("Donau", "donau"), DONAU_ONE_LEVEL(
				"Donau And One-Level", "donau;one-level"), FLAG("Flag", "flag"), FOUR_LEVEL("Four-Level", "four-level"), GUYED_H_FRAME(
				"Guyed H-Frame", "guyed_h-frame"), GUYED_V_FRAME("Guyed V-Frame", "guyed_v-frame"), H_FRAME(
				"H-Frame", "h-frame"), MONOPOLAR("Monopolar", "monopolar"), NINE_LEVEL("Nine-Level", "nine-level"), NULL(" ", null), ONE_LEVEL(
				"One-Level", "one-level"), PORTAL("Portal", "portal"), PORTAL_THREE_LEVEL(
				"Portal And Three-Level", "portal_three-level"), PORTAL_TWO_LEVEL(
				"Portal And Two-Level", "portal_two-level"), SIX_LEVEL("Six-Level", "six-level"), THREE_LEVEL(
				"Three-Level", "three-level"), TRIANGLE("Triangle", "triangle"), TWO_LEVEL("Two-Level", "two-level"), X_FRAME(
				"X-Frame", "x-frame"), Y_FRAME("Y-Frame", "y-frame");

		public final String name;
		public final String osm;

		DesignEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum MaterialEnum {
		ALUMINUM("Aluminum", "aluminum"), COMPOSITE("Composite", "composite"), CONCRETE("Concrete", "concrete"), NULL(" ", null), STEEL("Steel", "steel"), WOOD("Wood", "wood");

		public final String name;
		public final String osm;

		MaterialEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum StructureEnum {
		LATTICE("Lattice", "lattice"), NULL(" ", null), SOLID("Solid", "solid"), TUBULAR("Tubular", "tubular");

		public final String name;
		public final String osm;

		StructureEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum TypeEnum {
		ANCHOR("Anchor", "anchor"), BRANCH("Branch", "branch"), CROSSING("Crossing", "crossing"), NULL(" ", null), SUSPENSION(
				"Suspension", "suspension"), TERMINATION("Termination", "termination"), TRANSITION(
				"Transition", "transition"), TRANSPOSING("Transposing", "transposing");

		public final String name;
		public final String osm;

		TypeEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public Color color;
	public DesignEnum design;
	public BooleanEnum design_incomplete;
	public String design_name;
	public Float height;
	public MaterialEnum material;
	public String ref;
	public StructureEnum structure;
	public BooleanEnum transition;
	public TypeEnum type;

	public Tower(Poi poi) {
		super(poi);
	}
}
