package parsers;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import metamodels.vertices.ecore.EAttributeVertex;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import parsers.qvtr.QVTRelation;
import procedure.translators.ConstraintVisitor;
import procedure.translators.TranslatorContext;

/**
 * 
 * @author Aurélien Pepin
 */
public class VariableIndexer {
    
    private final QVTRelation relation;
    
    private final Map<Set<Variable>, Set<Expr>> bindings;
    
    private final TranslatorContext tContext;
    
    public VariableIndexer(QVTRelation relation) {
        this.relation = relation;
        this.bindings = new HashMap<>();
        this.tContext = TranslatorContext.getInstance();
    }
    
    public void addBinding(Set<Variable> variables, PropertyTemplateItem pti) {
        if (variables.isEmpty())
            return; // Useless for multi-model consistency
        
        // Transform the PropertyTemplateItem into a Z3 constraint
        EAttributeVertex eav1 = new EAttributeVertex((EAttribute) pti.getResolvedProperty().getESObject());
        Expr eq1 = tContext.getZ3Ctx().mkConst(eav1.getFullName(), tContext.getZ3Ctx().mkStringSort());
        BoolExpr predicate = tContext.getZ3Ctx().mkEq( // (= eq1 translate(pti))
                eq1,
                pti.getValue().accept(relation.getConstraintVisitor())
        );

        if (bindings.containsKey(variables)) {
            bindings.get(variables).add(predicate);
        } else {
            bindings.put(variables, new HashSet<>(Arrays.asList(predicate)));
        }
    }
    
    public void merge() {
        throw new UnsupportedOperationException("TODO merge of variables");
    }

    public Map<Set<Variable>, Set<Expr>> getBindings() {
        return bindings;
    }
    
    /**
     * Merge sets as long as their intersections are non-empty.
     * Example: [{1}, {2, 3}, {1, 4}, {5}] -> [{1, 2, 3, 4}, {5}]
     * 
     * @param <T>
     * @param sets
     * @return 
     */
    public static<T> List<Set<T>> merge(List<Set<T>> sets) {
        boolean merged = true;
        
        while (merged) {
            merged = false;
            List<Set<T>> results = new ArrayList<>();
            
            while (!sets.isEmpty()) {
                Set<T> common = sets.get(0);
                List<Set<T>> rest = sets.subList(1, sets.size());
                sets = new ArrayList<>();
                
                for (Set s : rest) {
                    if (Collections.disjoint(s, common)) {
                        sets.add(s);
                    } else {
                        merged = true;
                        common.addAll(s);
                    }
                }                
                results.add(common);
            }            
            sets = results;
        }
        
        return sets;
    }
}
