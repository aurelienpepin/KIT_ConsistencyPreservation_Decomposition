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
import metamodels.vertices.ecore.EAttributeVertex;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import parsers.qvtr.QVTRelation;

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

        // Add predicate to the set of variables
        if (bindings.containsKey(variables)) {
            bindings.get(variables).add(predicate);
        } else {
            bindings.put(variables, new HashSet<>(Arrays.asList(predicate)));
        }
    }
    
    public void merge() {
        boolean merged = true;
        List<Entry<Set<Variable>, Set<Expr>>> sets = new ArrayList<>(bindings.entrySet());
        
        while (merged) {
            merged = false;
            List<Entry<Set<Variable>, Set<Expr>>> results = new ArrayList<>(); 
            
            while (!sets.isEmpty()) {
                Entry<Set<Variable>, Set<Expr>> common = sets.get(0);
                List<Entry<Set<Variable>, Set<Expr>>> rest = sets.subList(1, sets.size());
                sets = new ArrayList<>();
                
                for (Entry<Set<Variable>, Set<Expr>> s : rest) {
                    if (Collections.disjoint(s.getKey(), common.getKey())) {
                        sets.add(s);
                    } else {
                        merged = true;
                        common.getKey().addAll(s.getKey());
                        common.getValue().addAll(s.getValue());
                    }
                }
                results.add(common);
            }
                        
            sets = results;
        }
        
        bindings.clear();
        for (Entry<Set<Variable>, Set<Expr>> entry : sets) {
            bindings.put(entry.getKey(), entry.getValue());
        }
    }
    /* 
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
    */
    public Map<Set<Variable>, Set<Expr>> getBindings() {
        return bindings;
    }
}
