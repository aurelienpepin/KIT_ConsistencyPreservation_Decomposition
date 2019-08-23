package integration;

import java.util.List;
import junit.framework.TestCase;
import metamodels.MetaGraph;
import org.junit.jupiter.api.Test;
import parsers.TransformationParser;
import procedure.decomposition.Decomposer;
import procedure.decomposition.DecompositionResult;

/**
 * 
 * @author Aurélien Pepin
 */
public class ITSimple extends TestCase implements IntegrationTest {
    
    private final String qvtrFile = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";
    
    public ITSimple() {
        super("Integration Simple");
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testIndependentSubgraphs() {
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtrFile);        
        List<DecompositionResult> results = Decomposer.decompose(graph);
        
        assertTrue("Bad number of independent subgraphs", results.size() == 2);
    }
    
    // @Test
    /* public void testResults() {
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtrFile);        
        List<DecompositionResult> results = Decomposer.decompose(graph);
        
        assertTrue("Bad result about decomposability", results.stream().allMatch(r -> r.isPositive()));
    } */
}
