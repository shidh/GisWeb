package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class OsmChangeset extends Model {

	public long osmChangesetId;
	
	@OneToMany(mappedBy = "osmChangeset", cascade = CascadeType.ALL)
	public List<OsmNode> osmNodes;
}
