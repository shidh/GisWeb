package models.powerTags;

import java.awt.Color;

import javax.persistence.Entity;

import models.Poi;
import models.PowerTag;

@Entity
public class Tower extends PowerTag {

	public enum BooleanEnum {
		FALSE("False"), NULL("null"), TRUE("True");

		public final String name;

		BooleanEnum(String name) {
			this.name = name;
		}
	}

	public enum DesignEnum {
		ASYMMETRIC("Asymmetric"), BARREL("Barrel"), BIPOLE("Bipole"), DELTA(
				"Delta"), DELTA_TWO_LEVEL("Delta And Two-Level"), DONAU("Donau"), DONAU_ONE_LEVEL(
				"Donau And One-Level"), FLAG("Flag"), FOUR_LEVEL("Four-Level"), GUYED_H_FRAME(
				"Guyed H-Frame"), GUYED_V_FRAME("Guyed V-Frame"), H_FRAME(
				"H-Frame"), INCOMPLETE("Incomplete"), MONOPOLAR("Monopolar"), NAME(
				"Name"), NINE_LEVEL("Nine-Level"), NULL("null"), ONE_LEVEL(
				"One-Level"), PORTAL("Portal"), PORTAL_THREE_LEVEL(
				"Portal And Three-Level"), PORTAL_TWO_LEVEL(
				"Portal And Two-Level"), SIX_LEVEL("Six-Level"), THREE_LEVEL(
				"Three-Level"), TRIANGLE("Triangle"), TWO_LEVEL("Two-Level"), X_FRAME(
				"X-Frame"), Y_FRAME("Y-Frame");

		public final String name;

		DesignEnum(String name) {
			this.name = name;
		}
	}

	public enum MaterialEnum {
		CONCRETE("Concrete"), NULL("null"), STEEL("Steel"), WOOD("Wood");

		public final String name;

		MaterialEnum(String name) {
			this.name = name;
		}
	}

	public enum StructureEnum {
		LATTICE("Lattice"), NULL("null"), SOLID("Solid"), TUBULAR("Tubular");

		public final String name;

		StructureEnum(String name) {
			this.name = name;
		}
	}

	public enum TypeEnum {
		ANCHOR("Anchor"), BRANCH("Branch"), CROSSING("Crossing"), NULL("null"), SUSPENSION(
				"Suspension"), TERMINATION("Termination"), TRANSITION(
				"Transition"), TRANSPOSING("Transposing");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
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
