package parsers.qvtr;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import metamodels.Metagraph;
import metamodels.edges.LockedEdge;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.Metavertex;
import metamodels.vertices.VariableVertex;
import metamodels.vertices.ecore.EAttributeVertex;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.ocl.pivot.VariableExp;
import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import parsers.VariableVertexFactory;
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
    
    // private final Map<Variable, List<PropertyTemplateItem>> classes;
    
    public QVTRelation(Relation relation) {
        this.relation = relation;
        this.domains = new ArrayList<>();
        
        // Each QVTrelation has its own factory so the scope of a VariableVertice is the relation.
        VariableVertexFactory varVertexFactory = new VariableVertexFactory(this);
        this.depV = new DependencyVisitor(varVertexFactory, TranslatorContext.getInstance());
        
        // this.classes = new HashMap<>();
        
        for (Domain rd : relation.getDomain()) {
            this.domains.add(new QVTDomain((RelationDomain) rd));
        }
        
//        for (Variable v : relation.getVariable()) {
//            this.classes.put(v, new ArrayList<>()); // !!! it adds useless variables
//        }
    }

    @Override
    public void translate(Metagraph graph) {
        for (QVTDomain domain : domains) {
            this.transformDomain(graph, domain);
        }
        
        TranslatorContext tc = TranslatorContext.getInstance();
        HashMap<String, List<VariableVertex>> classes = depV.getVariableVertexFactory().getClasses();
        
        for (String v : classes.keySet()) {
            for (int i = 0; i < classes.get(v).size(); ++i) {
                for (int j = i + 1; j < classes.get(v).size(); ++j) {
                    VariableVertex v1 = classes.get(v).get(i);
                    VariableVertex v2 = classes.get(v).get(j);
                    
                    Expr eq1 = tc.getZ3Ctx().mkConst(v1.getFullName(), tc.getZ3Ctx().mkStringSort());
                    Expr eq2 = tc.getZ3Ctx().mkConst(v2.getFullName(), tc.getZ3Ctx().mkStringSort());
                    
                    BoolExpr predicate = tc.getZ3Ctx().mkEq(eq1, eq2);
                    PredicateEdge pEdge = new PredicateEdge(predicate);
                    graph.addEdge(v1, v2, pEdge);
                }
            }
        }
    }
    
    private void transformDomain(Metagraph graph, QVTDomain domain) {
        for (PropertyTemplateItem pti : domain.getParts()) {
            Metavertex valueVertex = pti.getValue().accept(depV);
            graph.addVertex(valueVertex);
            
            // TODO: write something more general
            EAttributeVertex eav1 = new EAttributeVertex((EAttribute) pti.getResolvedProperty().getESObject());
            graph.addVertex(eav1);
            
            // AJOUTER L'EDGE ENTRE LES DEUX
            TranslatorContext tc = TranslatorContext.getInstance();
            
            Expr eq1 = tc.getZ3Ctx().mkConst(eav1.getFullName(), tc.getZ3Ctx().mkStringSort());
            Expr eq2 = tc.getZ3Ctx().mkConst(valueVertex.getFullName(), tc.getZ3Ctx().mkStringSort());

            BoolExpr predicate = tc.getZ3Ctx().mkEq(eq1, eq2);
            LockedEdge lEdge = new LockedEdge(predicate);
            
            graph.addEdge(eav1, valueVertex, lEdge);
        }
    }
    
    /*
    private void computeDependencyClasses(Metagraph graph) {
        DependencyVisitor depV = new DependencyVisitor(TranslatorContext.getInstance());
        
        for (QVTDomain domain : domains) {
            Set<Variable> dependencies;
            
            for (PropertyTemplateItem pti : domain.getParts()) {
                dependencies = pti.getValue().accept(depV); // returns liste de dependances
                
                for (Variable dep : dependencies) {  // pour chaque elem de dependance  //      classes.get(elem).add(pti.getReferredProp);
                    classes.get(dep).add(pti);
                }
            }
        }
        
        // TEMP. Predicates via subsets (all distinct pairs)
        TranslatorContext tc = TranslatorContext.getInstance();
        
        for (Variable dep : classes.keySet()) {
            for (int i = 0; i < classes.get(dep).size(); ++i) {
                for (int j = i + 1; j < classes.get(dep).size(); ++j) {
                    ENamedElement elem1 = (ENamedElement) classes.get(dep).get(i).getResolvedProperty().getESObject();
                    ENamedElement elem2 = (ENamedElement) classes.get(dep).get(j).getResolvedProperty().getESObject();
                    
                    EAttributeVertex eav1 = new EAttributeVertex((EAttribute) elem1);
                    EAttributeVertex eav2 = new EAttributeVertex((EAttribute) elem2);
                    
                    // Only works for equality relations for now                    
                    Expr eq1 = tc.getZ3Ctx().mkConst(eav1.getFullName(), tc.getZ3Ctx().mkStringSort());
                    Expr eq2 = tc.getZ3Ctx().mkConst(eav2.getFullName(), tc.getZ3Ctx().mkStringSort());
                    
                    BoolExpr predicate = tc.getZ3Ctx().mkEq(eq1, eq2);
                    PredicateEdge pEdge = new PredicateEdge(predicate);
                    
                    pEdge.addDependentPTI(classes.get(dep).get(i));
                    pEdge.addDependentPTI(classes.get(dep).get(j));
                    graph.addEdge(eav1, eav2, pEdge);                   
                }
            }
        }
        
        // Q: deux visiteurs ou un seul?
        // ici on a les classes de dépendances
        // pour chaque elem de dépendance
        //      pour chaque combi a-b                            v--- ici il faut aussi parser (visitor)
        //          graph.addEdge(a, b, Equality(a, partA) AND Equality(b, partB))
    }
    */
    
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
}
