package main;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import metamodels.DualGraph;
import metamodels.MetaGraph;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import org.jgrapht.ext.JGraphXAdapter;
import parsers.TransformationParser;

import procedure.decomposition.Decomposer;

public class Main {
    
    public static void main(String[] args) throws IOException {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");
        // testZ3();
        
        // 1. CREATE GRAPH
        // TODO: read the filename(s) from `args`
        String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\films\\films.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\simple\\simple.qvtr";
        
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtr);
        showGraph(graph.toDual());
        
        // 2. PERFORM DECOMPOSITION
        System.out.println(Decomposer.decompose(graph));
    }
    
    /**
     * Exports a picture to visualize a graph of constraints.
     * Offers a convenient way to check the procedure with complex consistency relations.
     * (Temporary, for debugging purpose.)
     * 
     * @param g             The graph to visualize
     * @throws IOException  The image cannot be created
     */
    public static void showGraph(DualGraph g) throws IOException {
        JGraphXAdapter<MetaEdge, DualEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxCircleLayout circleLayout = new mxCircleLayout(graphAdapter);
        
        mxIGraphLayout layout = circleLayout;
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 5, Color.WHITE, true, null);
        File imgFile = new File("src/test/resources/graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }
    
    public static void testZ3() {
        Context ctx = new Context();
        Solver s = ctx.mkSolver();
        
        //  Three sets with unspecified content
        ArrayExpr set1 = (ArrayExpr) ctx.mkConst("a", ctx.mkSetSort(ctx.mkIntSort()));
        ArrayExpr set2 = (ArrayExpr) ctx.mkConst("b", ctx.mkSetSort(ctx.mkIntSort()));
        ArrayExpr set3 = (ArrayExpr) ctx.mkConst("c", ctx.mkSetSort(ctx.mkIntSort()));
        
        s.add(ctx.mkNot(
            ctx.mkImplies(
                ctx.mkAnd(ctx.mkSetSubset(set2, set1), ctx.mkSetSubset(set3, set2)),
                ctx.mkSetSubset(set3, set1)
            )
        ));
        
        // Result: UNSATISFIABLE
        System.out.println("S: " + s.check());
    }
}
