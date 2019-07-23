package parsers.relations;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;

/**
 * 
 * @author Aurélien Pepin
 */
public class QVTRelation {
    
    /**
     * Corresponding QVT-R relation element.
     */
    private final Relation relation;
    
    private final List<QVTDomain> domains;
    
    public QVTRelation(Relation relation) {
        this.relation = relation;
        this.domains = new ArrayList<>();
        
        for (Domain rd : relation.getDomain()) {
            this.domains.add(new QVTDomain((RelationDomain) rd));
        }
    }
}
