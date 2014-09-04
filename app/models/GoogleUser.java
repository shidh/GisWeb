package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class GoogleUser extends Model {

	public enum AccountType {
		NULL, SUPERUSER, USER;
	}

	public AccountType accountType;
	public String googleId;
	public String googleMail;

	@OneToOne(mappedBy = "googleUser", cascade = CascadeType.ALL)
	public Poi poi;
}
