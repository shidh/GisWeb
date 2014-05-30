package models.powerTags;

import javax.persistence.Entity;

import models.powerTags.types.Operator;
import play.db.jpa.Model;

@Entity
public class CableDistributionCabinet extends Model {

	public Operator operator;
	public String ref;
	public int voltage;
}