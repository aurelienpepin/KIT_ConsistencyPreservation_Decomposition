package main;

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
        
        // 1. CREATE GRAPH
        // String qvtr = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\simpleConcat\\simpleConcat.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\simpleDoubleConcat\\simpleDoubleConcat.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\uml2rdbms_simple\\uml2rdbms_simple.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\abc\\abc.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\abcNo\\abcNo.qvtr";
        String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\primAbs\\primAbs.qvtr";
        
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtr);
        showGraph(graph.toDual());
        
        // 2. PERFORM DECOMPOSITION
        System.out.println(Decomposer.decompose(graph));
        
        // System.out.println(graph);
    }
    
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
