package parsers.qvtr;

import com.microsoft.z3.BoolExpr;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import metamodels.MetaGraph;
import metamodels.edges.EdgeAssembler;
import metamodels.edges.MetaEdge;
import metamodels.hypergraphs.HyperEdge;
import metamodels.vertices.ecore.EAttributeVertex;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtbase.Predicate;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import procedure.translators.VariableIndexer;
import procedure.visitors.ConstraintVisitor;
import procedure.visitors.DependencyVisitor;
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
        this.conV = new ConstraintVisitor(this, TranslatorContext.getInstance());
        
        for (Domain rd : relation.getDomain()) {
            this.domains.add(new QVTDomain((RelationDomain) rd));
        }
    }
    
    @Override
    public void translate(MetaGraph graph) {        
        for (QVTDomain domain : domains) {
            this.transformDomain(graph, domain);
        }
        
        // Add preconditions (When-clause) in the metagraph
        this.transformWhen(graph);
        
        // Group variables that have something to do together
        this.varIndexer.merge();
        
        for (EdgeAssembler assembler : varIndexer.getBindings().values()) {
            MetaEdge edge = new MetaEdge(assembler.getVertices(), assembler.getExpressions());
            graph.addEdge(edge);            
        }
    }
    
    private void transformWhen(MetaGraph graph) {
        if (this.relation.getWhen() == null)
            return;
        
        // TODO: extend the support of calls of other relations!
        // If this occurs, the program raises an exception (~ unknown OCL op.) ATM
        for (Predicate pred : this.relation.getWhen().getPredicate()) {
            // System.out.println("PRED: " + pred.getConditionExpression().accept(conV));
            graph.addPrecondition((BoolExpr) pred.getConditionExpression().accept(conV));
        }
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
