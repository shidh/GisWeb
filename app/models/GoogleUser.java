package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class GoogleUser extends Model {

	public enum AccountType {
		NULL, SUPERUSER, USER;
	}

	public AccountType accountType;
	public String googleId;
	public String googleMail;
}
