package parsers.relations;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.qvtd.pivot.qvtrelation.RelationalTransformation;

/**
 * 
 * @author Aurélien Pepin
 */
public class QVTTransformation {
    
    /**
     * Corresponding QVT-R transformation element.
     */
    private final RelationalTransformation transformation;
    
    /**
     * Relations contained in the transformation.
     */
    private final List<QVTRelation> relations;
    
    public QVTTransformation(RelationalTransformation transformation) {
        this.transformation = transformation;
        this.relations = new ArrayList<>();
    }
}
