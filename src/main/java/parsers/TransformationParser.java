package parsers;

import java.util.HashSet;
import java.util.Set;
import metamodels.Metagraph;
import metamodels.vertices.EPackageVertex;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import org.eclipse.qvtd.xtext.qvtrelation.QVTrelationStandaloneSetup;
import org.eclipse.qvtd.pivot.qvtrelation.RelationModel;
import org.eclipse.qvtd.pivot.qvtrelation.RelationalTransformation;
import org.eclipse.qvtd.xtext.qvtrelationcs.TopLevelCS;
import org.eclipse.qvtd.xtext.qvtrelationcs.TransformationCS;
import parsers.relations.QVTTransformation;
import java.util.logging.Logger;
import org.eclipse.ocl.pivot.Import;
import procedure.translators.TransformationTranslator;

public class TransformationParser {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    /**
     * Generate graph from an arbitrary number of QVT-R transformation files.
     * @param qvtrFilePaths
     * @return 
     */
    public static Metagraph generateGraphFrom(String... qvtrFilePaths) {
        OCLstdlib.install();
        QVTrelationStandaloneSetup.doSetup();
        
        Metagraph graph = generateGraphVertices(qvtrFilePaths); // Metamodel elements
        TransformationParser.generateGraphEdges(graph, qvtrFilePaths); // Predicates
        
        return graph;
    }
    
    private static Metagraph generateGraphVertices(String... qvtrFilePaths) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Set<EPackage> rootPackages = new HashSet<>();
        
        for (String qvtrFilePath : qvtrFilePaths) {
            URI qvtrURI = URI.createFileURI(qvtrFilePath);
            Resource transfoResource = resourceSet.getResource(qvtrURI, true);
            
            TransformationParser.manageErrors(transfoResource.getErrors(), qvtrURI);
            rootPackages.addAll(TransformationParser.findMetamodelsInFile(transfoResource));
        }
        
        return MetamodelParser.generateGraphFrom(rootPackages.toArray(new EPackage[0]));
    }
    
    private static Metagraph generateGraphEdges(Metagraph graph, String... qvtrFilePaths) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Set<QVTTransformation> transformations = new HashSet<>();
        
        for (String qvtrFilePath : qvtrFilePaths) {
            URI qvtrURI = URI.createFileURI(qvtrFilePath);
            Resource transfoResource = resourceSet.getResource(qvtrURI, true);
            
            TransformationParser.manageErrors(transfoResource.getErrors(), qvtrURI);            
            transformations.addAll(TransformationParser.findTransformationsInFile(transfoResource));
        }
        
        // EQUAL PACKAGES TEST
//        System.out.println("--- BEGIN TEST");
//        for (ENamedElementVertex v : graph.vertexSet()) {
//            for (QVTTransformation transfo : transformations) {
//                for (QVTModelParam mp : transfo.getModelParams().values()) {
//                    for (org.eclipse.ocl.pivot.Package p : mp.getModel().getUsedPackage()) {
//                        // System.out.println(p.getEPackage());
//                        // System.out.println(p.getImportedPackages());
//                        System.out.println(new EPackageVertex(v.getElement()).equals(new EPackageVertex(p.getEPackage())));
//                    }
//                }
//            }
//        }
//        
//        System.out.println("--- END TEST");
        
        TransformationTranslator.translate(transformations, graph);
        
        // HERE, DO THE TRANSFORMATION !!! OCL > LOGIC
//        for (QVTTransformation transformation : transformations) {
//            System.out.println("MODEL PARAMETERS: " + transformation.getModelParams());
//            for (QVTRelation relation : transformation.getRelations()) {
//                for (QVTDomain domain : relation.getDomains()) {
//                    for (PropertyTemplateItem pti : domain.getParts()) {
//                        System.out.println(pti.getObjContainer());
//                        
//                        TransformationVisitor tv = new TransformationVisitor(new TranslatorContext(graph));
//                        pti.getValue().accept(tv);
//                        
//                        // ALORS LA REGARDER TTES LES VARIABLES QUI REMONTENT
//                        // SI VAR EN COMMUN: faire un arc avec les conditions des deux domaines
//                        // (Faire remonter les variables)
//                        
//                        // Exemple:
//                        // d1 {propA = 'a' + var}
//                        // d2 {propB = 'b' + var}
//                        // RESULTAT CONDITION propA <-> propB : (propA = 'a' + var) AND (propB = 'b' + var)
//                    }
//                }
//            }
//        }
        
        return graph;
    }
    
    private static Set<EPackage> findMetamodelsInFile(Resource transfoResource) {
        Set<EPackage> rootPackages = new HashSet<>();
        
        TopLevelCS root = (TopLevelCS) transfoResource.getContents().get(0);
        RelationModel rm = (RelationModel) root.getPivot();
        
        // System.out.println("PACKAGES: " + rm.getOwnedImports().get(0).getgetESObject());
        
        for (Import packageImport : rm.getOwnedImports()) {
            rootPackages.add((EPackage) packageImport.getImportedNamespace().getESObject());
        }
        
        return rootPackages;
    }
    
    private static Set<QVTTransformation> findTransformationsInFile(Resource transfoResource) {
        Set<QVTTransformation> transformations = new HashSet<>();
        TopLevelCS root = (TopLevelCS) transfoResource.getContents().get(0);
        
        for (TransformationCS transfCS : root.getOwnedTransformations()) {
            System.out.println(transfCS.toString());
            transformations.add(new QVTTransformation((RelationalTransformation) transfCS.getPivot()));
        }
        
        return transformations;
    }

    private static void manageErrors(EList<Resource.Diagnostic> errors, URI qvtrURI) {
        if (errors != null && !errors.isEmpty()) {
            System.out.println("[Parser] The following errors were detected while parsing " + qvtrURI.path());
            
            for (Resource.Diagnostic error : errors) {
                System.out.println("> " + error);
            }
        }
    }
}

// CF. https://www.eclipse.org/forums/index.php?t=msg&th=491160&goto=1110495&
// CF. https://github.com/haslab/echo/blob/6c35e510204fc71dcd2996fe1e084ed049408bb3/plugins/pt.uminho.haslab.echo/src/pt/uminho/haslab/mde/EMFParser.java