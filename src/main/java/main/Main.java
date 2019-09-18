package main;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import metamodels.DualGraph;
import metamodels.MetaGraph;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import org.jgrapht.ext.JGraphXAdapter;
import parsers.TransformationParser;

import procedure.decomposition.Decomposer;

public class Main {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static void main(String[] args) throws IOException {
        LOGGER.info(">> DECOMPOSITION PROCEDURE <<");
        
        // 1. CREATE GRAPH
        // TODO: read the filename(s) from `args`
        String qvtr = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";

        MetaGraph graph = TransformationParser.generateGraphFrom(qvtr);
        showGraph(graph.toDual());
        
        // 2. PERFORM DECOMPOSITION
        LOGGER.info(Decomposer.decompose(graph).toString());
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
}
