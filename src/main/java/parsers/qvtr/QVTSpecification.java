package parsers.qvtr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of QVT transformations.
 * @author Aurélien Pepin
 */
public class QVTSpecification {
    
    private final List<QVTTransformation> transformations;
    
    public QVTSpecification(Collection<QVTTransformation> transformations) {
        this.transformations = new ArrayList<>(transformations);
    }
    
    public QVTSpecification() {
        this.transformations = new ArrayList<>();
    }

    public List<QVTTransformation> getTransformations() {
        return transformations;
    }
    
    public void addTransformations(Collection<QVTTransformation> transformations) {
        this.transformations.addAll(transformations);
    }
}
