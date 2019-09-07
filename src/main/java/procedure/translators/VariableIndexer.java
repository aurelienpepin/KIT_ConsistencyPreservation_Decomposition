package procedure.translators;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
 * 
 * @author Aurélien Pepin
 */
public class VariableIndexer {
    
    private final QVTRelation relation;
    
    private final Map<Set<Variable>, EdgeAssembler> bindings;
    
    private final TranslatorContext tContext;
    
    public VariableIndexer(QVTRelation relation) {
        this.relation = relation;
        this.bindings = new HashMap<>();
        this.tContext = TranslatorContext.getInstance();
    }
    
    public void addBinding(Set<Variable> variables, PropertyTemplateItem pti) {
        if (variables.isEmpty())
            return; // Useless for multi-model consistency
        
        System.out.println(variables);
        
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
    
    public void merge() {
        boolean merged = true;
        List<Entry<Set<Variable>, EdgeAssembler>> entries = new ArrayList<>(bindings.entrySet());
        
        while (merged) {
            merged = false;
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
        }
        
        bindings.clear();
        for (Entry<Set<Variable>, EdgeAssembler> entry : entries) {
            bindings.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<Set<Variable>, EdgeAssembler> getBindings() {
        return bindings;
    }
    
    public static String uniqueNameGenerator(QVTRelation relation, Variable var) {
        return relation.getName() + "@" + var.getName();
    }
}
