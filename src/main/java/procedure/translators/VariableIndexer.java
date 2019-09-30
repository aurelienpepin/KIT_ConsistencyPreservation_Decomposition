package procedure.translators;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import metamodels.edges.EdgeAssembler;
import metamodels.vertices.ecore.EAttributeVertex;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import parsers.qvtr.QVTRelation;
import procedure.visitors.ConstraintFactory;

/**
 * A tool to gather groups of QVT-R variables in a relation and to merge them
 * if their intersections are not empty. 
 * 
 * The VariableIndexer organizes the consistency patterns in QVT-R in such a
 * way that the constraints of the metagraph can be created.
 * 
 * @author Aurélien Pepin
 */
public class VariableIndexer {
    
    /**
     * The explored relation.
     */
    private final QVTRelation relation;

    /**
     * A map to store dependencies between sets of variables and conditions in
     * which these sets appear. The underlying idea is that two OCL conditions (PTIs)
     * that share the same variables are dependent and will belong to the
     * same constraint in the metagraph.
     */
    private final Map<Set<Variable>, EdgeAssembler> bindings;
    
    /**
     * An access to the translator context to translate OCL conditions (PTIs)
     * into Z3 expressions.
     */
    private final TranslatorContext tContext;
    
    
    public VariableIndexer(QVTRelation relation) {
        this.relation = relation;
        this.bindings = new HashMap<>();
        this.tContext = TranslatorContext.getInstance();
    }
    
    /**
     * Store the dependency (binding) between a PTI (in a domain pattern) and the set of
     * QVT-R patterns it contains. Two things are retrieved from PTIs (and then
     * associated in an `EdgeAssembler` with a set of variables:
     * 
     * - The metamodel element (left part of the PTI)
     * - The translated condition with QVT-R variables (right part of the PTI)
     * 
     * @param variables A set of variables found in the PTI
     * @param pti       The PTI to explore
     */
    public void addBinding(Set<Variable> variables, PropertyTemplateItem pti) {
        if (variables.isEmpty())
            return; // Useless for multi-model consistency
        
        // Transform the PropertyTemplateItem into a Z3 constraint
        ConstraintFactory factory = new ConstraintFactory(tContext);
        
        EAttributeVertex eav1 = new EAttributeVertex((EAttribute) pti.getResolvedProperty().getESObject());
        Expr eq1 = tContext.getZ3Ctx().mkConst(eav1.getFullName(), factory.fromEcoreTypedElement(eav1.getElement()));
        BoolExpr predicate = tContext.getZ3Ctx().mkEq(eq1, pti.getValue().accept(relation.getConstraintVisitor()));
        
        // Add predicate to the set of variables
        if (!bindings.containsKey(variables)) {
            bindings.put(variables, new EdgeAssembler());
        }
        
        bindings.get(variables).addExpression(predicate);
        bindings.get(variables).addVertex(eav1);
    }
    
    /**
     * Merge sets of variables if their intersections are non-empty. A non-empty
     * intersection means that respective `EdgeAssembler` are bound to the same
     * variable(s) and are interdependent > merge them.
     * 
     * Example of merge:
     * INPUT:   [({1, 2, 3}:{a, b}), ({2, 4}:{c}), ({5}:{d})]
     * OUTPUT:  [({1, 2, 3, 4}:{a, b, c}), ({5}:{d})]
     */
    public void joinConstraints() {
        boolean merged = false;
        List<Entry<Set<Variable>, EdgeAssembler>> entries = new ArrayList<>(bindings.entrySet());
        
        do {
            List<Entry<Set<Variable>, EdgeAssembler>> results = new ArrayList<>(); 
            
            while (!entries.isEmpty()) {
                Entry<Set<Variable>, EdgeAssembler> common = entries.get(0);
                List<Entry<Set<Variable>, EdgeAssembler>> rest = entries.subList(1, entries.size());
                entries = new ArrayList<>();
                
                for (Entry<Set<Variable>, EdgeAssembler> s : rest) {
                    if (Collections.disjoint(s.getKey(), common.getKey())) {
                        entries.add(s);
                    } else {
                        merged = true;
                        common.getKey().addAll(s.getKey());
                        common.getValue().add(s.getValue());
                    }
                }
                results.add(common);
            }
                        
            entries = results;
        } while (merged);
        
        // Replace the old (separate) bindings with the merged ones
        bindings.clear();
        for (Entry<Set<Variable>, EdgeAssembler> entry : entries) {
            bindings.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<Set<Variable>, EdgeAssembler> getBindings() {
        return bindings;
    }
}
