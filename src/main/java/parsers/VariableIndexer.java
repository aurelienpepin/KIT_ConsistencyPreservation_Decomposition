package parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.ocl.pivot.Variable;
import parsers.qvtr.QVTRelation;
import parsers.qvtr.QVTVariable;

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
    
    private final HashMap<String, List<QVTVariable>> classes;
    
    public VariableIndexer(QVTRelation relation) {
        this.relation = relation;
        this.counter = 0;
        this.classes = new HashMap<>();
    }
    
    public QVTVariable getNewQVTVariable(Variable variable) {
        if (variable == null)
            throw new NullPointerException("No vertex can be instantiated for a null variable.");
        
        QVTVariable newVertex = new QVTVariable(variable, relation, counter++);
        this.addOccurrenceOfVariable(newVertex);
        return newVertex;
    }
    
    private void addOccurrenceOfVariable(QVTVariable var) {
        String varKey = var.getVariable().getName();
        
        if (!classes.containsKey(varKey))
            classes.put(varKey, new ArrayList<>());
        
        classes.get(varKey).add(var);
    }

    public HashMap<String, List<QVTVariable>> getClasses() {
        return classes;
    }
}
