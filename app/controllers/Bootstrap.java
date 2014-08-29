package controllers;

import models.GoogleUser;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() throws Exception {

		// Check if the database is empty
		if (GoogleUser.count() == 0) {
			Fixtures.loadModels("initial-data.yml");
		}
	}
}
