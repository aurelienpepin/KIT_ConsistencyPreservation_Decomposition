package parsers;

import metamodels.nodes.VariableVertex;
import org.eclipse.ocl.pivot.Variable;
import parsers.relations.QVTRelation;

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
    
    public VariableVertexFactory(QVTRelation relation) {
        this.relation = relation;
        this.counter = 0;
    }
    
    public VariableVertex getNewVariableVertex(Variable variable) {
        return new VariableVertex(variable, relation, counter++);
    }
}
