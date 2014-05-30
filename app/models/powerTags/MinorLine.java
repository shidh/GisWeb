package models.powerTags;

import javax.persistence.Entity;

import models.powerTags.types.Operator;
import play.db.jpa.Model;

@Entity
public class MinorLine extends Model {

	public byte[] cables;
	public String name;
	public Operator operator;
	public String ref;
	public int voltage;
}