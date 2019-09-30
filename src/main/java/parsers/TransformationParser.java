package parsers;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import metamodels.MetaGraph;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import org.eclipse.qvtd.xtext.qvtrelation.QVTrelationStandaloneSetup;
import org.eclipse.qvtd.pivot.qvtrelation.RelationalTransformation;
import org.eclipse.qvtd.xtext.qvtrelationcs.TopLevelCS;
import org.eclipse.qvtd.xtext.qvtrelationcs.TransformationCS;
import parsers.qvtr.QVTTransformation;
import java.util.logging.Logger;
import parsers.qvtr.QVTSpecification;
import procedure.translators.TransformationTranslator;

/**
 * Parses QVT-R files to generate an intermediate representation of the
 * consistency specification (QVTSpecification, QVTTransformation, etc.).
 * 
 * Note: the TransformationParser calls the MetamodelParser because metamodels
 * are always imported at the beginning at a QVT-R file.
 * 
 * @see     MetamodelParser
 * @see     QVTSpecification
 * @author  Aurelien
 */
public final class TransformationParser {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private TransformationParser() {}
    
    /**
     * Generate graph from an arbitrary number of QVT-R transformation files.
     * @param qvtrFilePaths
     * @return 
     */
    public static MetaGraph generateGraphFrom(String... qvtrFilePaths) {
        OCLstdlib.install();
        QVTrelationStandaloneSetup.doSetup();
        
        MetaGraph graph = new MetaGraph();
        TransformationParser.fillGraph(graph, qvtrFilePaths); // Predicates
        
        return graph;
    }
    
    /**
     * Fill the graph with the content of given QVT-R files.
     * 
     * @param graph
     * @param qvtrFilePaths
     * @return 
     */
    private static MetaGraph fillGraph(MetaGraph graph, String... qvtrFilePaths) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Set<QVTTransformation> transformations = new HashSet<>();
        
        for (String qvtrFilePath : qvtrFilePaths) {
            URI qvtrURI = URI.createFileURI(qvtrFilePath);
            Resource transfoResource = resourceSet.getResource(qvtrURI, true);
            
            TransformationParser.manageErrors(transfoResource.getErrors(), qvtrURI);            
            transformations.addAll(TransformationParser.findTransformationsInFile(transfoResource));
        }
        
        // (!) Here, transform transformations into graph edges and vertices.
        TransformationTranslator.translate(transformations, graph);
        return graph;
    }
    
    private static Set<QVTTransformation> findTransformationsInFile(Resource transfoResource) {
        Set<QVTTransformation> transformations = new HashSet<>();
        TopLevelCS root = (TopLevelCS) transfoResource.getContents().get(0);
        
        for (TransformationCS transfCS : root.getOwnedTransformations()) {
            // System.out.println(transfCS.toString());
            transformations.add(new QVTTransformation((RelationalTransformation) transfCS.getPivot()));
        }
        
        return transformations;
    }

    /**
     * If there are errors in the QVT-R files, list them here.
     * 
     * @param errors
     * @param qvtrURI 
     */
    private static void manageErrors(EList<Resource.Diagnostic> errors, URI qvtrURI) {
        if (errors != null && !errors.isEmpty()) {
            LOGGER.log(Level.SEVERE, "[PARSER] The following errors were detected while parsing {0}:", qvtrURI.path());
            
            for (Resource.Diagnostic error : errors) {
                LOGGER.log(Level.SEVERE, "> {0}", error);
            }
        }
    }
}

// CF. https://www.eclipse.org/forums/index.php?t=msg&th=491160&goto=1110495&
// CF. https://github.com/haslab/echo/blob/6c35e510204fc71dcd2996fe1e084ed049408bb3/plugins/pt.uminho.haslab.echo/src/pt/uminho/haslab/mde/EMFParser.java