package parsers;

import junit.framework.TestCase;
import org.eclipse.emf.ecore.EPackage;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Aurélien Pepin
 */
public class TestMetamodelParser extends TestCase {

    public TestMetamodelParser() {
        super("TestMetamodelParser");
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
    public void testNullRootPackage() {
        RuntimeException re = assertThrows(RuntimeException.class, () -> MetamodelParser.generateGraphFrom((EPackage) null));
        assertEquals("Root package cannot be null", re.getMessage());
    }
}
