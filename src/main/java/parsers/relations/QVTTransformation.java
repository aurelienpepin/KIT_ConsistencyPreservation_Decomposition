package parsers.relations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metamodels.Metagraph;
import org.eclipse.qvtd.pivot.qvtbase.Rule;
import org.eclipse.qvtd.pivot.qvtbase.TypedModel;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationalTransformation;

/**
 * 
 * @author Aurélien Pepin
 */
public class QVTTransformation implements QVTTranslatable {
    
    /**
     * Corresponding QVT-R transformation element.
     */
    private final RelationalTransformation transformation;
    
    /**
     * Parameters contained in the transformation.
     */
    private final Map<String, QVTModelParam> modelParams;
    
    /**
     * Relations contained in the transformation.
     */
    private final List<QVTRelation> relations;
    
    
    public QVTTransformation(RelationalTransformation transformation) {
        if (transformation == null)
            throw new RuntimeException("Transformation cannot be null");
        
        this.transformation = transformation;
        this.relations = new ArrayList<>();
        this.modelParams = new HashMap<>();
        
        for (Rule rule : transformation.getRule()) {
            relations.add(new QVTRelation((Relation) rule));
        }
        
        for (TypedModel tm : transformation.getModelParameter()) {
            modelParams.put(tm.getName(), new QVTModelParam(tm));
        }
        
        // TODO: handle keys
        if (!transformation.getOwnedKey().isEmpty())
            throw new RuntimeException("TODO: support the use of keys in transformations");
    }
    @Override
    public void translate(Metagraph graph) {
        for (QVTRelation relation : relations) {
            relation.translate(graph);
        }
    }

    @Override
    public String toString() {
        return transformation.getName() + ": " + relations;
    }

    public List<QVTRelation> getRelations() {
        return relations;
    }

    public Map<String, QVTModelParam> getModelParams() {
        return modelParams;
    }
}
