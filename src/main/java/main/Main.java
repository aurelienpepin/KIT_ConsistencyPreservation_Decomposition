package main;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import metamodels.MetaGraph;
import metamodels.hypergraphs.HyperGraph;
import metamodels.hypergraphs.HyperVertex;
import metamodels.vertices.MetaVertex;
import org.jgrapht.ext.JGraphXAdapter;
import parsers.TransformationParser;

import procedure.decomposition.Decomposer;

public class Main {
    
    public static void main(String[] args) throws IOException {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");
        
        // 1. CREATE GRAPH
        // String qvtr = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\simpleConcat\\simpleConcat.qvtr";
        String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\simpleDoubleConcat\\simpleDoubleConcat.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\uml2rdbms_simple\\uml2rdbms_simple.qvtr";
        
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtr);
        // showGraph(graph);
        
        // 2. PERFORM DECOMPOSITION
        Decomposer.decompose(graph);
        
        System.out.println(graph);
    }
    
//    public static void showGraph(Metagraph g) throws IOException {
//        JGraphXAdapter<Metavertex, PredicateEdge> graphAdapter = new JGraphXAdapter<>(g);
//        mxCircleLayout circleLayout = new mxCircleLayout(graphAdapter);
//        
//        mxIGraphLayout layout = circleLayout;
//        layout.execute(graphAdapter.getDefaultParent());
//
//        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 5, Color.WHITE, true, null);
//        File imgFile = new File("src/test/resources/graph.png");
//        ImageIO.write(image, "PNG", imgFile);
//    }
}
