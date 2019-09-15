package parsers.qvtr;

import com.microsoft.z3.BoolExpr;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import metamodels.MetaGraph;
import metamodels.edges.EdgeAssembler;
import metamodels.edges.MetaEdge;
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
 * Represents a relation, i.e. the basic unit of transformation behavior
 * specification in QVT-R. A relation is made of domains, preconditions
 * and postconditions.
 *
 * Edges of the metagraph are produced in the QVTRelation.
 * 
 * @author Aurélien Pepin
 */
public class QVTRelation implements QVTTranslatable {
    
    /**
     * Corresponding QVT-R relation element.
     */
    private final Relation relation;
    
    /**
     * List of domains in the transformation.
     */
    private final List<QVTDomain> domains;
    
    /**
     * A dependency visitor to provide the set of QVT-R variables
     * used in OCL conditions that appear in domains of the relation.
     */
    private final DependencyVisitor depV;
    
    /**
     * A (custom) constraint visitor to translate OCL conditions that appear in
     * domains of the relation to Z3 expressions.
     */
    private final ConstraintVisitor conV;
    
    /**
     * An intermediate structure to keep track of QVT-R variables provided
     * by the dependency visitor. Useful to build metagraph edges.
     */
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
    
    /**
     * Performs the translation of relations:
     * - translates preconditions (partial support at the moment);
     * - translates conditions into edges.
     * 
     * @see     VariableIndexer
     * @param   graph   The graph to fill with new edges
     */
    @Override
    public void translate(MetaGraph graph) {        
        for (QVTDomain domain : domains) {
            this.transformDomain(graph, domain);
        }
        
        // Add preconditions (When-clause) in the metagraph
        this.transformWhen(graph);
        
        // Group variables that have something to do together
        this.varIndexer.merge();
        
        // Build edges (MetaEdge) that represent constraints on set of metamodel elements
        for (Entry<Set<Variable>, EdgeAssembler> entry : varIndexer.getBindings().entrySet()) {
            EdgeAssembler assembler = entry.getValue();
            Set<QVTVariable> freeVariables = entry.getKey()
                    .stream()
                    .map(v -> new QVTVariable(v, this))
                    .collect(Collectors.toSet());
                        
            MetaEdge edge = new MetaEdge(assembler.getVertices(), assembler.getExpressions(), freeVariables);
            graph.addEdge(edge);
        }
    }
    
    /**
     * Retrieves the `when` clause of a relation and translate it.
     * The result is appended to the metagraph.
     * 
     * @param graph The graph to fill with preconditions
     */
    private void transformWhen(MetaGraph graph) {
        if (this.relation.getWhen() == null)
            return;
        
        // TODO: extend the support of calls of other relations!
        // If this occurs, the program raises an exception (~ unknown OCL op.) ATM
        for (Predicate pred : this.relation.getWhen().getPredicate()) {
            graph.addPrecondition((BoolExpr) pred.getConditionExpression().accept(conV));
        }
    }
    
    /**
     * Performs the translation of domains by first adding vertices to the graph.
     * The QVT-R variables in each domain are retrieved with the dependency
     * visitor to find interdependent PTIs.
     * 
     * @param graph  The graph to fill with vertices.
     * @param domain The QVTDomain to explore.
     */
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
