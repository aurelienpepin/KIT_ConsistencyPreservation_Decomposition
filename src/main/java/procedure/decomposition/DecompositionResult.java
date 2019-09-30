package procedure.decomposition;

import java.util.ArrayList;
import java.util.List;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import org.jgrapht.graph.AsSubgraph;

/**
 * Stores a component and edges that have been removed in this component.
 * The result of the decomposition procedure is a set of `DecompositionResult`.
 * 
 * @author Aurélien Pepin
 */
public class DecompositionResult {

    /**
     * Component (subgraph of the dual of the metagraph) in which constraints are simulated.
     */
    private final AsSubgraph<MetaEdge, DualEdge> component;

    /**
     * Constraints that are simulable through a combination of other remaining edges.
     */
    private final List<MetaEdge> removedEdges;

    
    public DecompositionResult(AsSubgraph<MetaEdge, DualEdge> component, List<MetaEdge> removedEdges) {
        this.component = component;
        this.removedEdges = new ArrayList<>(removedEdges);
    }

    public List<MetaEdge> getRemovedEdges() {
        return removedEdges;
    }
   
    /**
     * The result is considered positive if at least one constraint in the component was removed.
     * 
     * @return 
     */
    public boolean isPositive() {
        return !removedEdges.isEmpty();
    }

    @Override
    public String toString() {
        return "DecompositionResult{" + "component=" + component + ", removedEdges=" + removedEdges + '}';
    }
}
