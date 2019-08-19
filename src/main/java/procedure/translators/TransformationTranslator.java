package procedure.translators;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import metamodels.MetaGraph;
import parsers.qvtr.QVTSpecification;
import parsers.qvtr.QVTTransformation;

/**
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
