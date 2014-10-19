package models.powerTags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import models.Poi;
import models.PowerTag;
import models.powerTags.types.Operator;
import models.powerTags.types.Output.OutputEnum;

@Entity
public class Generator extends PowerTag {

	public enum MethodEnum {
		ANAEROBIC_DIGESTION("Anaerobic Digestion", "anaerobic_digestion"), BARRAGE("Barrage", "barrage"), COMBUSTION(
				"Combustion", "combustion"), FISSION("Fission", "fission"), FUSION("Fusion", "fusion"), GASIFICATION(
				"Gasification", "gasification"), NULL(" ", null), PHOTOVOLTAIC("Photovoltaic", "photovoltaic"), RUN_OF_THE_RIVER(
				"Run Of The River", "run-of-the-river"), STREAM("Stream", "stream"), THERMAL("Thermal", "thermal"), WATER_PUMPED_STORAGE(
				"Water Pumped Storage", "water-pumped-storage"), WATER_STORAGE("Water Storage", "water-storage");

		public final String name;
		public final String osm;

		MethodEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum PlantEnum {
		INTERMEDIATE("Intermediate", "intermediate"), NULL(" ", null), OUTPUT("Output", "output");

		public final String name;
		public final String osm;

		PlantEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum SourceEnum {
		BIOFUEL("Biofuel", "biofuel"), BIOGAS("Biogas", "biogas"), BIOMASS("Biomass", "biomass"), COAL("Coal", "coal"), DIESEL(
				"Diesel", "diesel"), GAS("Gas", "gas"), GASOLINE("Gasoline", "gasoline"), GEOTHERMAL(
				"Geothermal", "geothermal"), HYDRO("Hydro", "hydro"), NUCLEAR("Nuclear", "nuclear"), NULL(" ", null), OIL(
				"Oil", "oil"), SOLAR("Solar", "solar"), TIDAL("Tidal", "tidal"), WASTE("Waste", "waste"), WAVE(
				"Wave", "wave"), WIND("Wind", "wind");

		public final String name;
		public final String osm;

		SourceEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public enum TypeEnum {
		BIOREACTOR("Bioreactor", "bioreactor"), BWR_1("BWR-1", "BWR-1"), BWR_2("BWR-2", "BWR-2"), BWR_3("BWR-3", "BWR-3"), BWR_4(
				"BWR-4", "BWR-4"), BWR_5("BWR-5", "BWR-5"), BWR_6("BWR-6", "BWR-6"), CANDU("CANDU", "CANDU"), COLD_FUSION(
				"Cold Fusion", "cold-fusion"), COMBINED_CYCLE("Combined Cycle", "combined_cycle"), CPR_1000(
				"CPR-1000", "CPR-1000"), EPR("EPR", "EPR"), FRANCIS_TURBINE("Francis Turbine", "francis_turbine"), GAS_TURBINE(
				"Gas Turbine", "gas_turbine"), HEAT_PUMP("Heat Pump", "heat_pump"), HORIZONTAL_AXIS(
				"Horizontal Axis", "horizontal_axis"), HYDRODYNAMIC_SCREW("Hydrodynamic Screw", "hydrodynamic_screw"), ICF("ICF", "ICF"), KAPLAN_TURBINE("Kaplan Turbine", "kaplan_turbine"), NULL(
				" ", null), PELTON_TURBINE("Pelton Turbine", "pelton_turbine"), PWR("PWR", "PWR"), RBMK_1000(
				"RBMK-1000", "RBMK-1000"), RBMK_1500("RBMK-1500", "RBMK-1500"), RECIPROCATING_ENGINE(
				"Reciprocating Engine", "reciprocating_engine"), SOLAR_PHOTOVOLTAIC_PANEL(
				"Solar Photovoltaic Panel", "solar_photovoltaic_panel"), SOLAR_THERMAL_COLLECTOR(
				"Solar Thermal Collector", "solar_thermal_collector"), STEAM_GENERATOR("Steam Generator", "steam_generator"), STEAM_TURBINE(
				"Steam Turbine", "steam_turbine"), STELLARATOR("Stellarator", "stellarator"), TOKAMAK("Tokamak", "tokamak"), VERTICAL_AXIS(
				"Vertical Axis", "vertical_axis"), VVER("VVER", "VVER");

		public final String name;
		public final String osm;

		TypeEnum(String name, String osm) {
			this.name = name;
			this.osm = osm;
		}
	}

	public String name;

	@OneToOne(mappedBy = "powerTag", cascade = CascadeType.ALL)
	public Operator operator;

	public OutputEnum output;
	public String output_value;
	public PlantEnum plant;
	public SourceEnum source;
	public MethodEnum method;
	public TypeEnum type;

	public Generator(Poi poi) {
		super(poi);
	}
}