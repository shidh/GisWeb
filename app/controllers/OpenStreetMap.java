package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.OsmChangeset;
import models.OsmNode;
import models.Poi;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.templates.TemplateLoader;

public class OpenStreetMap extends Controller {

	private static final String osmUser = "";
	private static final String osmPassword = "";

	private static OsmChangeset createChangeset() {
		OsmChangeset osmChangeset = null;
		HttpResponse response = WS
				.url("http://api06.dev.openstreetmap.org/api/0.6/changeset/create")
				.authenticate(osmUser, osmPassword)
				.body(TemplateLoader.load("/app/views/tags/osm/changeset.xml")
						.render()).put();

		if (response.success()) {
			osmChangeset = new OsmChangeset();
			osmChangeset.osmChangesetId = Long.valueOf(response.getString());
			osmChangeset.save();
		}
		return osmChangeset;
	}

	public static OsmNode createNode(Poi poi) {
		OsmChangeset osmChangeset = getChangeset();
		OsmNode osmNode = null;

		if (osmChangeset != null) {
			Map args = new HashMap();
			args.put("changeset", osmChangeset);
			args.put("poi", poi);
			HttpResponse response = WS
					.url("http://api06.dev.openstreetmap.org/api/0.6/node/create")
					.authenticate(osmUser, osmPassword)
					.body(TemplateLoader.load("/app/views/tags/osm/poi.xml")
							.render(args)).put();

			if (response.success()) {
				osmNode = new OsmNode();
				osmNode.osmChangeset = osmChangeset;
				osmNode.osmNodeId = Long.valueOf(response.getString());
				osmNode.save();
			}
		}
		return osmNode;
	}

	private static OsmChangeset getChangeset() {
		OsmChangeset osmChangeset = null;

		if (OsmChangeset.findAll().isEmpty()) {
			osmChangeset = createChangeset();
		} else {
			List<OsmChangeset> osmChangesets = OsmChangeset.findAll();
			osmChangeset = osmChangesets.get(osmChangesets.size() - 1);
			HttpResponse response = WS.url(
					"http://api06.dev.openstreetmap.org/api/0.6/changeset/"
							+ osmChangeset.osmChangesetId).get();

			if (response.success()) {
				Boolean isOpen = Boolean.valueOf(response.getXml()
						.getElementsByTagName("changeset").item(0)
						.getAttributes().getNamedItem("open").getTextContent());

				if (!isOpen) {
					osmChangeset = createChangeset();
				}
			} else {
				osmChangeset = createChangeset();
			}
		}
		return osmChangeset;
	}
}
