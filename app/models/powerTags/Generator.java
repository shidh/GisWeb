package models.powerTags;

import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;

@Entity
public class Generator extends PowerTag {

	public String name;
	
	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public OutputEnum output;
	public PlantEnum plant;
	public SourceEnum source;
	public MethodEnum method;
	public TypeEnum type;

	public Generator(Poi poi) {
		super(poi);
	}

	public static MethodEnum[] getMethods(SourceEnum source) {

		MethodEnum[] methods = new MethodEnum[] {};

		switch (source) {
		case BIOMASS:
			methods = new MethodEnum[] { MethodEnum.COMBUSTION, MethodEnum.GASIFICATION };
			break;
		case BIOFUEL:
		case BIOGAS:
		case COAL:
		case DIESEL:
		case GAS:
		case GASOLINE:
		case OIL:
			methods = new MethodEnum[] { MethodEnum.COMBUSTION };
			break;
		case GEOTHERMAL:
		case WAVE:
		case WIND:
			methods = new MethodEnum[] {};
			break;
		case HYDRO:
			methods = new MethodEnum[] { MethodEnum.RUN_OF_THE_RIVER, MethodEnum.WATER_PUMPED_STORAGE, MethodEnum.WATER_STORAGE };
			break;
		case NUCLEAR:
			methods = new MethodEnum[] { MethodEnum.FISSION, MethodEnum.FUSION };
			break;
		case SOLAR:
			methods = new MethodEnum[] { MethodEnum.PHOTOVOLTAIC, MethodEnum.THERMAL };
			break;
		case TIDAL:
			methods = new MethodEnum[] { MethodEnum.BARRAGE, MethodEnum.STREAM };
			break;
		case WASTE:
			methods = new MethodEnum[] { MethodEnum.COMBUSTION, MethodEnum.GASIFICATION };
			break;
		}

		return methods;
	}

	public static TypeEnum[] getTypes(SourceEnum source, MethodEnum method) {

		TypeEnum[] types = new TypeEnum[] {};

		switch (source) {
		case BIOFUEL:
		case DIESEL:
		case GASOLINE:
			types = new TypeEnum[] { TypeEnum.RECIPROCATING_ENGINE, TypeEnum.STEAM_GENERATOR };
			break;
		case BIOGAS:
			types = new TypeEnum[] { TypeEnum.GAS_TURBINE, TypeEnum.RECIPROCATING_ENGINE, TypeEnum.STEAM_GENERATOR };
			break;
		case BIOMASS:
			if (method == MethodEnum.COMBUSTION) {
				types = new TypeEnum[] { TypeEnum.STEAM_GENERATOR, TypeEnum.STEAM_TURBINE };
			} else if (method == MethodEnum.GASIFICATION) {
				types = new TypeEnum[] { TypeEnum.GAS_TURBINE };
			}
			break;
		case COAL:
		case OIL:
			types = new TypeEnum[] { TypeEnum.STEAM_GENERATOR, TypeEnum.STEAM_TURBINE };
			break;
		case GAS:
			types = new TypeEnum[] { TypeEnum.COMBINED_CYCLE, TypeEnum.GAS_TURBINE, TypeEnum.RECIPROCATING_ENGINE, TypeEnum.STEAM_GENERATOR, TypeEnum.STEAM_TURBINE };
			break;
		case GEOTHERMAL:
			types = new TypeEnum[] { TypeEnum.HEAT_PUMP, TypeEnum.STEAM_TURBINE };
			break;
		case HYDRO:
			if (method == MethodEnum.RUN_OF_THE_RIVER || method == MethodEnum.WATER_STORAGE) {
				types = new TypeEnum[] { TypeEnum.FRANCIS_TURBINE, TypeEnum.KAPLAN_TURBINE, TypeEnum.PELTON_TURBINE };
			} else if (method == MethodEnum.WATER_PUMPED_STORAGE) {
				types = new TypeEnum[] { TypeEnum.FRANCIS_TURBINE };
			}
			break;
		case NUCLEAR:
			if (method == MethodEnum.FISSION) {
				types = new TypeEnum[] { TypeEnum.BWR_1, TypeEnum.BWR_2, TypeEnum.BWR_3, TypeEnum.BWR_4, TypeEnum.BWR_5, TypeEnum.BWR_6, TypeEnum.CANDU, TypeEnum.CPR_1000, TypeEnum.EPR, TypeEnum.PWR,
						TypeEnum.RBMK_1000, TypeEnum.RBMK_1500, TypeEnum.VVER };
			} else if (method == MethodEnum.FUSION) {
				types = new TypeEnum[] { TypeEnum.COLD_FUSION, TypeEnum.ICF, TypeEnum.STELLARATOR, TypeEnum.TOKAMAK };
			}
			break;
		case SOLAR:
			if (method == MethodEnum.PHOTOVOLTAIC) {
				types = new TypeEnum[] { TypeEnum.SOLAR_PHOTOVOLTAIC_PANEL };
			} else if (method == MethodEnum.THERMAL) {
				types = new TypeEnum[] { TypeEnum.SOLAR_THERMAL_COLLECTOR, TypeEnum.STEAM_TURBINE };
			}
			break;
		case TIDAL:
			if (method == MethodEnum.BARRAGE) {
				types = new TypeEnum[] { TypeEnum.KAPLAN_TURBINE };
			} else if (method == MethodEnum.STREAM) {
				types = new TypeEnum[] { TypeEnum.HORIZONTAL_AXIS, TypeEnum.VERTICAL_AXIS };
			}
			break;
		case WASTE:
			if (method == MethodEnum.COMBUSTION) {
				types = new TypeEnum[] { TypeEnum.STEAM_GENERATOR, TypeEnum.STEAM_TURBINE };
			} else if (method == MethodEnum.GASIFICATION) {
				types = new TypeEnum[] { TypeEnum.GAS_TURBINE };
			}
			break;
		case WAVE:
			types = new TypeEnum[] {};
			break;
		case WIND:
			types = new TypeEnum[] { TypeEnum.HORIZONTAL_AXIS, TypeEnum.VERTICAL_AXIS };
			break;
		}

		return types;
	}

	public void setMethodEnum(MethodEnum method) {
		MethodEnum[] methods = getMethods(this.source);

		if (Arrays.asList(methods).contains(method)) {
			this.method = method;
		}
	}

	public void setTypeEnum(TypeEnum type) {
		TypeEnum[] types = getTypes(this.source, this.method);

		if (Arrays.asList(types).contains(type)) {
			this.type = type;
		}
	}

	public enum SourceEnum {
		BIOFUEL("biofuel"), BIOGAS("biogas"), BIOMASS("biomass"), COAL("coal"), DIESEL("diesel"), GAS("gas"), GASOLINE("gasoline"), GEOTHERMAL("geothermal"), HYDRO("hydro"), NUCLEAR("nuclear"), OIL(
				"oil"), SOLAR("solar"), TIDAL("tidal"), WASTE("waste"), WAVE("wave"), WIND("wind");

		public final String name;

		SourceEnum(String name) {
			this.name = name;
		}
	}

	public enum MethodEnum {
		BARRAGE("barrage"), COMBUSTION("combustion"), FISSION("fission"), FUSION("fusion"), GASIFICATION("gasification"), PHOTOVOLTAIC("photovoltaic"), RUN_OF_THE_RIVER("run of the river"), STREAM(
				"stream"), THERMAL("thermal"), WATER_PUMPED_STORAGE("water pumped storage"), WATER_STORAGE("water storage"), NULL("null");
		;

		public final String name;

		MethodEnum(String name) {
			this.name = name;
		}
	}

	public enum TypeEnum {
		BWR_1("BWR-1"), BWR_2("BWR-2"), BWR_3("BWR-3"), BWR_4("BWR-4"), BWR_5("BWR-5"), BWR_6("BWR-6"), COLD_FUSION("cold fusion"), COMBINED_CYCLE("combined cycle"), CANDU("CANDU"), CPR_1000(
				"CPR-1000"), EPR("EPR"), FRANCIS_TURBINE("francis turbine"), GAS_TURBINE("gas turbine"), HEAT_PUMP("heat pump"), HORIZONTAL_AXIS("horizontal axis"), ICF("ICF"), KAPLAN_TURBINE(
				"kaplan turbine"), PELTON_TURBINE("pelton turbine"), PWR("PWR"), RBMK_1000("RBMK-1000"), RBMK_1500("RBMK-1500"), RECIPROCATING_ENGINE("reciprocating engine"), SOLAR_PHOTOVOLTAIC_PANEL(
				"solar photovoltaic panel"), SOLAR_THERMAL_COLLECTOR("solar thermal collector"), STEAM_GENERATOR("steam generator"), STEAM_TURBINE("steam turbine"), STELLARATOR("stellarator"), TOKAMAK(
				"tokamak"), VERTICAL_AXIS("vertical axis"), VVER("VVER"), NULL("null");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
		}
	}

	public enum OutputEnum {
		COLD_AIR("cold air"), COLD_WATER("cold water"), COMPRESSED_AIR("compressed air"), ELECTRICITY("electricity"), HOT_AIR("hot air"), HOT_WATER("hot water"), STEAM("steam"), VACUUM("vacuum");

		public final String name;

		OutputEnum(String name) {
			this.name = name;
		}
	}

	public enum PlantEnum {
		INTERMEDIATE("intermediate"), OUTPUT("output");

		public final String name;

		PlantEnum(String name) {
			this.name = name;
		}
	}
}