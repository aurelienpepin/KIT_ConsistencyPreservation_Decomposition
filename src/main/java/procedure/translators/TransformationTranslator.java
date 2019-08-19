package procedure.translators;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import metamodels.Metagraph;
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
    public static void translate(Set<QVTTransformation> transformations, Metagraph graph) {
        QVTSpecification spec = new QVTSpecification(transformations);
        graph.setSpecification(spec);
        
        for (QVTTransformation transformation : transformations) {
            transformation.translate(graph);
        }
    }
    
    /**
     * Merge sets as long as their intersections are non-empty.
     * Example: [{1}, {2, 3}, {1, 4}, {5}] -> [{1, 2, 3, 4}, {5}]
     * 
     * @param <T>
     * @param sets
     * @return 
     */
    public static<T> List<Set<T>> merge(List<Set<T>> sets) {
        boolean merged = true;
        
        while (merged) {
            merged = false;
            List<Set<T>> results = new ArrayList<>();
            
            while (!sets.isEmpty()) {
                Set<T> common = sets.get(0);
                List<Set<T>> rest = sets.subList(1, sets.size());
                sets = new ArrayList<>();
                
                for (Set s : rest) {
                    if (Collections.disjoint(s, common)) {
                        sets.add(s);
                    } else {
                        merged = true;
                        common.addAll(s);
                    }
                }                
                results.add(common);
            }            
            sets = results;
        }
        
        return sets;
    }
}
