package integration;

import java.util.List;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import metamodels.MetaGraph;
import org.junit.jupiter.api.Test;
import parsers.TransformationParser;
import procedure.decomposition.Decomposer;
import procedure.decomposition.DecompositionResult;

/**
 *
 * @author Aurelien
 */
public class ITAbcPlusNo extends TestCase implements IntegrationTest {
    
    private final String qvtrFile = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\abcPlusNo\\abcPlusNo.qvtr";
    
    public ITAbcPlusNo() {
        super("Integration ABC (using + operation) (IRREDUCIBLE)");
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
        
        assertTrue("Bad number of independent subgraphs", results.size() == 1);
    }
    
    @Test
    public void testResults() {
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtrFile);        
        List<DecompositionResult> results = Decomposer.decompose(graph);
        
        assertTrue("Bad result about decomposability", results.stream().allMatch(r -> !r.isPositive()));
    }    
}
