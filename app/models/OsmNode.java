package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class OsmNode extends Model {

	public long osmNodeId;

	@ManyToOne
	public OsmChangeset osmChangeset;
	
	@OneToOne(mappedBy = "osmNode", cascade = CascadeType.ALL)
	public Poi poi;
}
