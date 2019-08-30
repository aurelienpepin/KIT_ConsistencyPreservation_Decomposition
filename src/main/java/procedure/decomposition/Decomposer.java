package procedure.decomposition;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import metamodels.DualGraph;
import metamodels.MetaGraph;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.graph.AsSubgraph;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurélien Pepin
 */
public class Decomposer {

    public static List<DecompositionResult> decompose(MetaGraph graph) {
        List<DecompositionResult> results = new ArrayList<>();
        
        // Get the dual graph
        DualGraph dual = graph.toDual();
        
        // Get connected components
        BiconnectivityInspector inspector = new BiconnectivityInspector(dual);
        Set<AsSubgraph<MetaEdge, DualEdge>> connectedComponents = inspector.getConnectedComponents();
        
        // For each component, see what you can remove thanks to simulation
        for (AsSubgraph<MetaEdge, DualEdge> component : connectedComponents) {
            DecompositionResult result = checkCycles(component);
            results.add(result);
        }
        
        return results;
        // throw new UnsupportedOperationException("Not finished yet.");
    }
    
    public static DecompositionResult checkCycles(AsSubgraph<MetaEdge, DualEdge> component) {
        List<MetaEdge> dualVertices = new ArrayList<>(component.vertexSet());
        List<MetaEdge> removedDualVertices = new ArrayList<>();
        
        Set<GraphPath<MetaEdge, DualEdge>> simpleCycles;
        PatonCycleBase paton;
        int i = 0;
        
        while (i < dualVertices.size()) {
            MetaEdge constraint = dualVertices.get(i);
            paton = new PatonCycleBase(component);
            simpleCycles = paton.getCycleBasis().getCyclesAsGraphPaths();
            
            for (GraphPath p : simpleCycles) {
                if (!p.getVertexList().contains(constraint))
                    continue;
                
                if (pathToHornClause(p, constraint)) {
                    component.removeVertex(constraint);
                    removedDualVertices.add(constraint);
                    
                    System.out.println("REPLACED: " + constraint);
                    break;
                }
            }
            
            // Shorten the reduction procedure if there are no more cycles
            if (paton.getCycleBasis().getCycles().isEmpty()) {
                break;
            }
            
            i++;
        }
        
        return new DecompositionResult(component, removedDualVertices);
    }
    
    private static boolean pathToHornClause(GraphPath<MetaEdge, DualEdge> path, MetaEdge constraint) {
        Context ctx = TranslatorContext.getInstance().getZ3Ctx();
        Solver s = ctx.mkSolver();

        // Temporary: remove constraint from path
        // List<MetaEdge> otherConstraints = new ArrayList<>(path.getVertexList());
        // otherConstraints.removeAll(Collections.singleton(constraint));
        Set<MetaEdge> otherConstraints = new HashSet<>(path.getVertexList());
        otherConstraints.removeAll(Collections.singleton(constraint));
        
        otherConstraints.forEach((pathEdge) -> {
            s.add((BoolExpr) pathEdge.getPredicate());
        });
        
        // System.out.println(constraint.getPredicateParts());
        s.add((BoolExpr) constraint.getPredicateParts().iterator().next());
        s.add(ctx.mkNot((BoolExpr) constraint.getPredicate()));
        // System.out.println("assertions: " + Arrays.toString(s.getAssertions()));
        // System.out.println(s.check());
        // System.out.println(s.getModel());
        return Status.UNSATISFIABLE.equals(s.check());
    }
}