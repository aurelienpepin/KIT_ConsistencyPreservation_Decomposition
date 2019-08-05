package parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import metamodels.vertices.VariableVertex;
import org.eclipse.ocl.pivot.Variable;
import parsers.qvtr.QVTRelation;

/**
 * 
 * @author Aurélien Pepin
 */
public class VariableVertexFactory {
    
    /**
     * A counter starting from 0 to give an unique identifier to
     * occurrences of a same QVT-R variable.
     */
    private int counter;
    
    private final QVTRelation relation;
    
    private HashMap<String, List<VariableVertex>> classes;
    
    public VariableVertexFactory(QVTRelation relation) {
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
    
    private void addOccurrenceOfVariable(VariableVertex varVertex) {
        String varKey = varVertex.getVariable().getName();
        
        if (!classes.containsKey(varKey))
            classes.put(varKey, new ArrayList<>());
        
        classes.get(varKey).add(varVertex);
    }

    public HashMap<String, List<VariableVertex>> getClasses() {
        return classes;
    }
}
