package procedure.decomposition;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Quantifier;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import parsers.qvtr.QVTVariable;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurélien Pepin
 */
public class Decomposer {

    public static List<DecompositionResult> decompose(MetaGraph graph) {
        List<DecompositionResult> results = new ArrayList<>();
        Set<BoolExpr> preconditions = graph.getPreconditions();
        DualGraph dual = graph.toDual();
        
        // Get connected components
        BiconnectivityInspector inspector = new BiconnectivityInspector(dual);
        Set<AsSubgraph<MetaEdge, DualEdge>> connectedComponents = inspector.getConnectedComponents();

        // For each component, see what you can remove thanks to simulation
        for (AsSubgraph<MetaEdge, DualEdge> component : connectedComponents) {
            DecompositionResult result = checkCycles(component, preconditions);
            results.add(result);
        }
        
        return results;
    }
    
    public static DecompositionResult checkCycles(AsSubgraph<MetaEdge, DualEdge> component, Set<BoolExpr> preconditions) {
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
                
                if (pathToHornClause(p, constraint, preconditions)) {
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
    
    private static boolean pathToHornClause(GraphPath<MetaEdge, DualEdge> path, MetaEdge constraint, Set<BoolExpr> preconditions) {
        Context ctx = TranslatorContext.getInstance().getZ3Ctx();
        Solver s = ctx.mkSolver();
        // Solver fakeS = ctx.mkSolver();
        
        if (!preconditions.isEmpty()) {
            s.add(ctx.mkAnd(preconditions.toArray(new BoolExpr[preconditions.size()])));
            // fakeS.add(ctx.mkAnd(preconditions.toArray(new BoolExpr[preconditions.size()])));
        }
        
        // Temporary: remove constraint from path
        Set<MetaEdge> otherConstraints = new HashSet<>(path.getVertexList());
        otherConstraints.remove(constraint);
        
        otherConstraints.forEach((pathEdge) -> {
            s.add((BoolExpr) pathEdge.getPredicate());
            // fakeS.add((BoolExpr) pathEdge.getPredicate());
        });
        
        Quantifier qt2 = ctx.mkForall(
                freeVariablesToExprs(constraint.getFreeVariables()), // new Expr[]{ctx.mkConst(constraint.getFreeVariables().iterator().next().getFullName(), 
                // ctx.mkStringSort())},
                ctx.mkNot((BoolExpr) constraint.getPredicate()), 0, null, null, null, null);
        
        s.add(qt2);

        System.out.println("-----------------------------------");
        System.out.println("assertions_size: " + s.getAssertions().length);
        System.out.println("assertions:\n" + Arrays.toString(s.getAssertions()));
        
        // (!) System.out.println(ctx.SimplifyHelp());
        // System.out.println(s.check());
        // System.out.println("UnsatCore: " + Arrays.toString(s.getUnsatCore()));
        // System.out.println("Statistics: " + s.getStatistics());
        // System.out.println("Parameters: " + s.getParameterDescriptions());
        // System.out.println("Ctx Params: " + ctx.mkParams().toString());
        // System.out.println(s.getReasonUnknown());
        // System.out.println(s.getModel());
        // System.out.println("Check: " + fakeS.check());
        // System.out.println("UnsatCore: " + Arrays.toString(fakeS.getUnsatCore()));
        // System.out.println(s.check());
        // System.out.println(s.getModel());
        
        // (!!) already unsatisfiable in left hand >> check before? TODO
        return Status.UNSATISFIABLE.equals(s.check());
    }
    
    private static Expr[] freeVariablesToExprs(Set<QVTVariable> variables) {
        return variables.stream().map(v -> v.getExpr()).toArray(Expr[]::new);
    }
}