package procedure.decomposition;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import metamodels.Metagraph;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.Metavertex;
import metamodels.vertices.ecore.ENamedElementVertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.AsSubgraph;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurélien Pepin
 */
public class Decomposer {
    
    public static List<DecompositionResult> decompose(Metagraph graph) {
        List<DecompositionResult> results = new ArrayList<>();
        
        // Get connected components
        BiconnectivityInspector inspector = new BiconnectivityInspector(graph);
        Set<AsSubgraph<Metavertex, PredicateEdge>> connectedComponents = inspector.getConnectedComponents();
        
        // For each component, see what you can remove thanks to simulation
        for (AsSubgraph<Metavertex, PredicateEdge> component : connectedComponents) {
            if (!component.edgeSet().isEmpty()) {
                DecompositionResult result = Decomposer.reverseDelete(component);
                List<PredicateEdge> edges = result.getRemovedEdges();
                
                results.add(result);
                System.out.println("Result: " + edges);
            }
        }
        
        return results;
    }
    
    // TODO: Use the strategy pattern to give the choice of the algorithm
    private static DecompositionResult reverseDelete(AsSubgraph<Metavertex, PredicateEdge> component) {
        List<PredicateEdge> edges = new ArrayList<>(component.edgeSet());
        List<PredicateEdge> removedEdges = new ArrayList<>();
        int i = 0;
        
        while (i < edges.size()) {           
            PredicateEdge edge = edges.get(i);
            Metavertex s = component.getEdgeSource(edge);
            Metavertex t = component.getEdgeTarget(edge);
            
            // Delete the edge
            component.removeEdge(edge);
            edges.set(i, null);
            
            ConnectivityInspector inspector = new ConnectivityInspector(component);
            if (edge.isLocked() || !inspector.isConnected() || !simulableThroughCombination(component, edge, s, t)) {	// Cancel deletion
                edges.set(i, edge);
                component.addEdge(s, t, edge);
            } else {
                removedEdges.add(edge);
            }

            i++;
        }
        
        edges.removeIf(e -> Objects.isNull(e));
        return new DecompositionResult(component, removedEdges, edges);
    }
    
    private static boolean simulableThroughCombination(AsSubgraph<Metavertex, PredicateEdge> graph, PredicateEdge edge, Metavertex source, Metavertex target) {
        KShortestPathAlgorithm algo = new YenKShortestPath(graph);
        // System.out.println("ARB: " + edge + " " + algo.getPaths(source, target, 100));	// arbitrary, for experimentation

        List<GraphPath> paths = algo.getPaths(source, target, Integer.MAX_VALUE);
        if (paths.stream().anyMatch((path) -> (pathToHornClause(path, edge)))) {
            return true;
        }

        return false;
    }
    
    private static boolean pathToHornClause(GraphPath<ENamedElementVertex, PredicateEdge> path, PredicateEdge edge) {
        Context ctx = TranslatorContext.getInstance().getZ3Ctx();
        Solver s = ctx.mkSolver();

        path.getEdgeList().forEach((pathEdge) -> {
            s.add(pathEdge.getPredicate());
        });

        s.add(ctx.mkNot(edge.getPredicate()));
        // System.out.println("assertions: " + Arrays.toString(s.getAssertions()));

        return Status.UNSATISFIABLE.equals(s.check());
    }
}