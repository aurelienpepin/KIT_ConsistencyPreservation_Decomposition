package parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

public class TransformationParser {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Metagraph generateGraphFrom(String... qvtrFilePaths) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Set<EPackage> rootPackages = new HashSet<>();
        
        // Install QVT-R and OCL components
        OCLstdlib.install();
        QVTrelationStandaloneSetup.doSetup();
        
        for (String qvtrFilePath : qvtrFilePaths) {
            URI qvtrURI = URI.createFileURI(qvtrFilePath);
            Resource transfoResource = resourceSet.getResource(qvtrURI, true);
            
            TransformationParser.manageErrors(transfoResource.getErrors(), qvtrURI);
            rootPackages.addAll(TransformationParser.findMetamodelsInTransfoFile(transfoResource));
        }
        
        System.out.println(rootPackages);
        
        Metagraph graph = MetamodelParser.generateGraphFrom(rootPackages.toArray(new EPackage[0]));
        
        return graph;
    }
    
    private static Set<EPackage> findMetamodelsInTransfoFile(Resource transfoResource) {
        Set<EPackage> rootPackages = new HashSet<>();
        
        TopLevelCS root = (TopLevelCS) transfoResource.getContents().get(0);
        RelationModel rm = (RelationModel) root.getPivot();
                
        for (Import packageImport : rm.getOwnedImports()) {
            rootPackages.add((EPackage) packageImport.getImportedNamespace().getESObject());
        }
        
        return rootPackages;
    }
    
    public static void fillGraphWith(Metagraph graph, String qvtrFilePath) {
        if (graph == null)
            throw new RuntimeException("The metagraph object cannot be null");
        
        ResourceSet resourceSet = new ResourceSetImpl();
        
        // Install QVT-R and OCL components
        OCLstdlib.install();
        QVTrelationStandaloneSetup.doSetup();
        
        URI qvtrURI = URI.createFileURI(qvtrFilePath);
        Resource metamodelResource = resourceSet.getResource(qvtrURI, true);
        System.out.println("DIAGNOSTIC (ERRORS): " + metamodelResource.getErrors());

        // From concrete syntax to model
        TopLevelCS root = (TopLevelCS) metamodelResource.getContents().get(0);
        RelationModel rm = (RelationModel) root.getPivot();
        
        Metagraph gr = new Metagraph();
        gr.addVertex(new EPackageVertex((EPackage) rm.getOwnedImports().get(0).getImportedNamespace().getESObject()));
        // MetamodelParser.parseFromPackage((EPackage) rm.getOwnedImports().get(0).getImportedNamespace().getESObject(), gr);
        System.out.println(gr);
        
        for (TransformationCS transfCS : root.getOwnedTransformations()) {
            TransformationParser.parseTransformation(graph, (RelationalTransformation) transfCS.getPivot());
        }
        
        // TransformationCS tr = (TransformationCS) root.eContents().get(2);
        // RelationalTransformation rt = (RelationalTransformation) tr.getPivot();
        
        // System.out.println(rt.getOwnedSignature());
    }
    
    private static void parseTransformation(Metagraph graph, RelationalTransformation rt) {
        if (graph == null || rt == null)
            throw new RuntimeException("The RelationalTransformation cannot be null");
        
        QVTTransformation transf = new QVTTransformation(rt);
        System.out.println(graph.getVertices());
        System.out.println(transf);
        
        System.out.println(": " + transf.getRelations().get(0).getDomains().get(0).domain);
        System.out.println(": " + transf.getRelations().get(0).getDomains().get(0).domain.getRootVariable().get(0).getType().getESObject());
        
//        System.out.println("Name: " + rt.getName());
//        System.out.println("ModelParameter: " + rt.getModelParameter());
//        
//        System.out.println("Extends: " + rt.getExtends());
//        System.out.println("ICS: " + rt.getInstanceClassName());
//        System.out.println("MTN: " + rt.getMetaTypeName());
//        
//        System.out.println("Constraints: " + rt.getOwnedConstraints());
//        System.out.println("Context: " + rt.getOwnedContext());
//        System.out.println("Invariants: " + rt.getOwnedInvariants());
//        System.out.println("Key: " + rt.getOwnedKey());
//        System.out.println("Properties: " + rt.getOwnedProperties());
//        System.out.println("OP: " + rt.getOwningPackage());
//        
//        System.out.println("Behaviours: " + rt.getOwnedBehaviors());
//        System.out.println("Rule: " + rt.getRule());
//        
//        Relation r = (Relation) rt.getRule().get(0);
//        
//        System.out.println("R/Variable: " + r.getVariable());
//        System.out.println("R/Domain: " + r.getDomain());
//        
//        RelationDomain rd = (RelationDomain) r.getDomain().get(0);
//        System.out.println("R/RelationDomain: " + rd.getPattern().get(0).getTemplateExpression());
//        System.out.println("R/RelationDomain: " + rd.getOwnedComments());

        // ObjectTemplateExp otexp = (ObjectTemplateExp) rd.getPattern().get(0).getTemplateExpression();
        // System.out.println("otexp inside: " + otexp.getPart().get(0).getResolvedProperty());
        // System.out.println("otexp inside: " + otexp.getPart().get(0));
        
        // System.out.println("T, TI, TV: " + otexp.getType() + " " + otexp.getTypeId() + " " + otexp.getTypeValue());
        
        // def otexp: pattern associé à un domaine
        // obtenir le chemin complet via otexp.getType()
        // savoir que CollectionTemplateExp existe
        
        // System.out.println("-------------");
        // System.out.println("R/OCLInvalid?: " + r.getVariable().get(2));
        // System.out.println("R/OCLInvalid?: " + r.getDomain().get(0).getPattern());
        
        // System.out.println(r.allOwnedElements());
        // System.out.println(r.getDomain().get(1).allOwnedElements());
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

//        System.out.println(rm.getOwnedConstraints());
//        System.out.println(rm.getOwnedPackages());
//        System.out.println(rm.getESObject());
//        
//        System.out.println(rm.getExternalURI());
//        System.out.println(rm.getName());
//        System.out.println(rm.eResource());
//        
//        System.out.println(rm.getOwnedExtensions());
//        System.out.println(rm.eContents());
//        System.out.println(rm.eAllContents());

// CF. https://www.eclipse.org/forums/index.php?t=msg&th=491160&goto=1110495&
// CF. https://github.com/haslab/echo/blob/6c35e510204fc71dcd2996fe1e084ed049408bb3/plugins/pt.uminho.haslab.echo/src/pt/uminho/haslab/mde/EMFParser.java