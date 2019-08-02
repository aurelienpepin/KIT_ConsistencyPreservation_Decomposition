package metamodels.edges;

import com.microsoft.z3.BoolExpr;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author Aurélien Pepin
 */
public class PredicateEdge extends DefaultEdge {
    
    private final BoolExpr predicate;
    
    private final List<PropertyTemplateItem> ptis;
    
    public PredicateEdge(BoolExpr predicate) {
        this.predicate = predicate;
        this.ptis = new ArrayList<>();
    }

    @Override
    public String toString() {
        // return super.toString() + "!!" + predicate + "!!"; //To change body of generated methods, choose Tools | Templates.
        return "!!" + predicate + "!!";
    }
    
    public BoolExpr getPredicate() {
        return predicate;
    }
    
    public void addDependentPTI(PropertyTemplateItem pti) {
        this.ptis.add(pti);
    }

    public List<PropertyTemplateItem> getDependentPTIS() {
        return ptis;
    }
}
