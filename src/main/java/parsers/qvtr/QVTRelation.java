package parsers.qvtr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import metamodels.MetaGraph;
import metamodels.vertices.ecore.EAttributeVertex;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import procedure.translators.VariableIndexer;
import procedure.translators.ConstraintVisitor;
import procedure.translators.DependencyVisitor;
import procedure.translators.TranslatorContext;

/**
 * 
 * @author Aurélien Pepin
 */
public class QVTRelation implements QVTTranslatable {
    
    /**
     * Corresponding QVT-R relation element.
     */
    private final Relation relation;
    
    private final List<QVTDomain> domains;
    
    private final DependencyVisitor depV;
    
    private final ConstraintVisitor conV;
    
    private final VariableIndexer varIndexer;
    
    public QVTRelation(Relation relation) {
        this.relation = relation;
        this.domains = new ArrayList<>();
        
        this.varIndexer = new VariableIndexer(this);
        this.depV = new DependencyVisitor(TranslatorContext.getInstance());
        this.conV = new ConstraintVisitor(TranslatorContext.getInstance());
        
        for (Domain rd : relation.getDomain()) {
            this.domains.add(new QVTDomain((RelationDomain) rd));
        }
    }
    
    @Override
    public void translate(MetaGraph graph) {        
        for (QVTDomain domain : domains) {
            this.transformDomain(graph, domain);
        }
        
        // Group variables that have something to do together
        System.out.println("AVANT MERGE: " + this.varIndexer.getBindings());
        this.varIndexer.merge();
        System.out.println("APRES MERGE: " + this.varIndexer.getBindings());
        
        // POUR CHAQUE MERGE:
        //      - nouvelle edge dans le graph
        //      - graph.addEdge()
    }
    
    private void transformDomain(MetaGraph graph, QVTDomain domain) {        
        for (PropertyTemplateItem pti : domain.getParts()) {
            // System.out.println(pti + " " + pti.getValue().accept(depV));
            Set<Variable> variables = pti.getValue().accept(depV);
            varIndexer.addBinding(variables, pti);
            
            graph.addVertex(new EAttributeVertex((EAttribute) pti.getResolvedProperty().getESObject()));
        }
    }
    
    @Override
    public String toString() {
        return relation.toString();
    }

    public List<QVTDomain> getDomains() {
        return domains;
    }
    
    public String getName() {
        return relation.getName();
    }

    public ConstraintVisitor getConstraintVisitor() {
        return conV;
    }
}
