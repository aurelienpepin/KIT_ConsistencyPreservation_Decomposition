package integration;

import com.microsoft.z3.BoolExpr;
import java.util.List;
import java.util.Set;
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
public class ITPrimLiterals extends TestCase implements IntegrationTest {
    
    private final String qvtrFile = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\primLiterals\\primLiterals.qvtr";
    
    public ITPrimLiterals() {
        super("Integration Primitive (test of literals, no metagraph)");
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
    public void testLiteralsInWhen() {
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtrFile);
        Set<BoolExpr> preconds = graph.getPreconditions();
        
        assertTrue("Bad number of parsed preconditions: " + preconds, preconds.size() == 1);
    }
    
    @Test
    public void testIndependentSubgraphs() {
        MetaGraph graph = TransformationParser.generateGraphFrom(qvtrFile);        
        List<DecompositionResult> results = Decomposer.decompose(graph);
        
        assertTrue("Bad number of independent subgraphs: " + results, results.isEmpty());
    }
}
