package parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.ocl.pivot.Variable;
import parsers.qvtr.QVTRelation;

/**
 * 
 * @author Aurélien Pepin
 */
public class VariableIndexer {
    
    /**
     * A counter starting from 0 to give an unique identifier to
     * occurrences of a same QVT-R variable.
     */
    private int counter;
    
    private final QVTRelation relation;
    
    private HashMap<String, List<Variable>> classes;
    
    public VariableIndexer(QVTRelation relation) {
        this.relation = relation;
        this.counter = 0;
        this.classes = new HashMap<>();
    }
    
    public VariableVertex getNewVariableVertex(Variable variable) {
        if (variable == null)
            throw new NullPointerException("No vertex can be instantiated for a null variable.");
        
        VariableVertex newVertex = new VariableVertex(variable, relation, counter++);
        this.addOccurrenceOfVariable(newVertex);
        return newVertex;
    }
    
    private void addOccurrenceOfVariable(Variable var) {
        String varKey = var.getName();
        
        if (!classes.containsKey(varKey))
            classes.put(varKey, new ArrayList<>());
        
        classes.get(varKey).add(var);
    }

    public HashMap<String, List<VariableVertex>> getClasses() {
        return classes;
    }
}
