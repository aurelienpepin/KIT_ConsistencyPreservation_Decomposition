package parsers.relations;

import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;

/**
 * 
 * @author Aur�lien Pepin
 */
public class QVTDomain {
    
    /**
     * Corresponding QVT-R domain element.
     */
    private final RelationDomain domain;
    
    public QVTDomain(RelationDomain domain) {
        this.domain = domain;
    }
}
