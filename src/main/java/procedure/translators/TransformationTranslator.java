package procedure.translators;

import java.util.Set;
import metamodels.Metagraph;
import parsers.relations.QVTSpecification;
import parsers.relations.QVTTransformation;

/**
 *
 * @author Aurélien Pepin
 */
public class TransformationTranslator {
    
    public static void translate(Set<QVTTransformation> transformations, Metagraph graph) {
        QVTSpecification spec = new QVTSpecification(transformations);
        graph.setSpecification(spec);
        
        for (QVTTransformation transformation : transformations) {
            transformation.translate(graph);
        }
    }
}
