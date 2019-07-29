package parsers.relations;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import metamodels.Metagraph;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.EAttributeVertex;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import procedure.translators.DependencyVisitor;
import procedure.translators.TransformationVisitor;
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
    
    private final Map<Variable, List<PropertyTemplateItem>> classes;
    
    public QVTRelation(Relation relation) {
        this.relation = relation;
        this.domains = new ArrayList<>();
        this.classes = new HashMap<>();
        
        for (Domain rd : relation.getDomain()) {
            this.domains.add(new QVTDomain((RelationDomain) rd));
        }
        
        for (Variable v : relation.getVariable()) {
            this.classes.put(v, new ArrayList<>()); // !!! it adds useless variables
        }
    }

    @Override
    public void translate(Metagraph graph) {
        this.computeDependencyClasses(graph);
        // System.out.println("I'm about to translate " + this);
        // System.out.println("TODO: Later, when and where support in QVTRelation");
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    
    }
    
    private void computeDependencyClasses(Metagraph graph) {
        DependencyVisitor tv = new DependencyVisitor(new TranslatorContext(graph));
        
        for (QVTDomain domain : domains) {
            System.out.println("ROOTVAR DU DOMAIN: " + domain.getDomain().getRootVariable());
            Set<Variable> dependencies;
            
            for (PropertyTemplateItem pti : domain.getParts()) {
                dependencies = pti.getValue().accept(tv); // returns liste de dependances
                
                for (Variable dep : dependencies) {  // pour chaque elem de dependance  //      classes.get(elem).add(pti.getReferredProp);
                    classes.get(dep).add(pti);
                }
            }
        }
        
        System.out.println("Classes: " + classes);
        
        // TEMP. Predicates via subsets (all distinct pairs)
        TranslatorContext tc = new TranslatorContext(graph);
        
        for (Variable dep : classes.keySet()) {
            for (int i = 0; i < classes.get(dep).size(); ++i) {
                for (int j = i + 1; j < classes.get(dep).size(); ++j) {
                    ENamedElement elem1 = (ENamedElement) classes.get(dep).get(i).getResolvedProperty().getESObject();
                    ENamedElement elem2 = (ENamedElement) classes.get(dep).get(j).getResolvedProperty().getESObject();
                    
                    EAttributeVertex eav1 = new EAttributeVertex(elem1);
                    EAttributeVertex eav2 = new EAttributeVertex(elem2);
                    
                    // WE ONLY WORK WITH EQUALITY HERE.......................
                    
                    Expr eq1 = tc.getZ3Ctx().mkConst(eav1.getFullName(), tc.getZ3Ctx().mkStringSort());
                    Expr eq2 = tc.getZ3Ctx().mkConst(eav2.getFullName(), tc.getZ3Ctx().mkStringSort());
                    BoolExpr predicate = tc.getZ3Ctx().mkEq(eq1, eq2);
                    graph.addEdge(eav1, eav2, new PredicateEdge(predicate));                   
                }
            }
        }
        
        // Q: deux visiteurs ou un seul?
        // ici on a les classes de dépendances
        // pour chaque elem de dépendance
        //      pour chaque combi a-b                            v--- ici il faut aussi parser (visitor)
        //          graph.addEdge(a, b, Equality(a, partA) AND Equality(b, partB))
    }

    @Override
    public String toString() {
        return relation.toString();
    }

    public List<QVTDomain> getDomains() {
        return domains;
    }
}
