package procedure.translators;

import java.util.Set;
import metamodels.MetaGraph;
import parsers.qvtr.QVTSpecification;
import parsers.qvtr.QVTTransformation;

/**
 * Starting point of the translation of QVT-R transformations.
 * Starts the translation and gives an access of models to translate to the graph.
 * 
 * @author Aurélien Pepin
 */
public class TransformationTranslator {
    
    /**
     * Fill a graph with a set of QVT-R transformations.
     * 
     * @param transformations
     * @param graph 
     */
    public static void translate(Set<QVTTransformation> transformations, MetaGraph graph) {
        QVTSpecification spec = new QVTSpecification(transformations);
        graph.setSpecification(spec);
        
        for (QVTTransformation transformation : transformations) {
            transformation.translate(graph);
        }
    }
}
