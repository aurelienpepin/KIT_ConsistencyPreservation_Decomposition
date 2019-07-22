package parsers.relations;

import org.eclipse.qvtd.pivot.qvtrelation.Relation;

/**
 * 
 * @author Aurélien Pepin
 */
public class QVTRelation {
    
    /**
     * Corresponding QVT-R relation element.
     */
    private final Relation relation;
    
    public QVTRelation(Relation relation) {
        this.relation = relation;
    }
}
