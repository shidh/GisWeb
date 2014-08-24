package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.types.Operator;
import models.types.Output.OutputEnum;

@Entity
public class Generator extends PowerTag {

	public enum MethodEnum {
		ANAEROBIC_DIGESTION("Anaerobic Digestion"), BARRAGE("Barrage"), COMBUSTION(
				"Combustion"), FISSION("Fission"), FUSION("Fusion"), GASIFICATION(
				"Gasification"), PHOTOVOLTAIC("Photovoltaic"), RUN_OF_THE_RIVER(
				"Run Of The River"), STREAM("Stream"), THERMAL("Thermal"), WATER_PUMPED_STORAGE(
				"Water Pumped Storage"), WATER_STORAGE("Water Storage"), NULL(
				"null");

		public final String name;

		MethodEnum(String name) {
			this.name = name;
		}
	}

	public enum PlantEnum {
		INTERMEDIATE("Intermediate"), OUTPUT("Output");

		public final String name;

		PlantEnum(String name) {
			this.name = name;
		}
	}

	public enum SourceEnum {
		BIOFUEL("Biofuel"), BIOGAS("Biogas"), BIOMASS("Biomass"), COAL("Coal"), DIESEL(
				"Diesel"), GAS("Gas"), GASOLINE("Gasoline"), GEOTHERMAL(
				"Geothermal"), HYDRO("Hydro"), NUCLEAR("Nuclear"), OIL("Oil"), SOLAR(
				"Solar"), TIDAL("Tidal"), WASTE("Waste"), WAVE("Wave"), WIND(
				"Wind");

		public final String name;

		SourceEnum(String name) {
			this.name = name;
		}
	}

	public enum TypeEnum {
		BIOREACTOR("Bioreactor"), BWR_1("BWR-1"), BWR_2("BWR-2"), BWR_3("BWR-3"), BWR_4(
				"BWR-4"), BWR_5("BWR-5"), BWR_6("BWR-6"), COLD_FUSION(
				"Cold Fusion"), COMBINED_CYCLE("Combined Cycle"), CANDU("CANDU"), CPR_1000(
				"CPR-1000"), EPR("EPR"), FRANCIS_TURBINE("Francis Turbine"), GAS_TURBINE(
				"Gas Turbine"), HEAT_PUMP("Heat Pump"), HORIZONTAL_AXIS(
				"Horizontal Axis"), ICF("ICF"), KAPLAN_TURBINE("Kaplan Turbine"), PELTON_TURBINE(
				"Pelton Turbine"), PWR("PWR"), RBMK_1000("RBMK-1000"), RBMK_1500(
				"RBMK-1500"), RECIPROCATING_ENGINE("Reciprocating Engine"), SOLAR_PHOTOVOLTAIC_PANEL(
				"Solar Photovoltaic Panel"), SOLAR_THERMAL_COLLECTOR(
				"Solar Thermal Collector"), STEAM_GENERATOR("Steam Generator"), STEAM_TURBINE(
				"Steam Turbine"), STELLARATOR("Stellarator"), TOKAMAK("Tokamak"), VERTICAL_AXIS(
				"Vertical Axis"), VVER("VVER"), NULL("null");

		public final String name;

		TypeEnum(String name) {
			this.name = name;
		}
	}

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
}