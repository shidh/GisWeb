package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class GoogleUser extends Model {

	public String googleMail;
	public String googleId;
}
