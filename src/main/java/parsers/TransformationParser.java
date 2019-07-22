package parsers;

import metamodels.Metagraph;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import org.eclipse.qvtd.pivot.qvtrelation.Relation;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.xtext.qvtrelation.QVTrelationStandaloneSetup;
import org.eclipse.qvtd.pivot.qvtrelation.RelationModel;
import org.eclipse.qvtd.pivot.qvtrelation.RelationalTransformation;
import org.eclipse.qvtd.xtext.qvtrelationcs.TopLevelCS;
import org.eclipse.qvtd.xtext.qvtrelationcs.TransformationCS;

public class TransformationParser {
    
    public static void fillGraphWith(Metagraph graph, String qvtrFilePath) {
        if (graph == null)
            throw new RuntimeException("The metagraph object cannot be null");
        
        ResourceSet resourceSet = new ResourceSetImpl();
        
        // Install QVT-R and OCL components
        OCLstdlib.install();
        QVTrelationStandaloneSetup.doSetup();
        
        URI qvtrURI = URI.createFileURI(qvtrFilePath);
        Resource metamodelResource = resourceSet.getResource(qvtrURI, true);
        
        // From concrete syntax to model
        TopLevelCS root = (TopLevelCS) metamodelResource.getContents().get(0);
        RelationModel rm = (RelationModel) root.getPivot();
        
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
        
        System.out.println("Name: " + rt.getName());
        System.out.println("ModelParameter: " + rt.getModelParameter());
        
        System.out.println("Extends: " + rt.getExtends());
        System.out.println("ICS: " + rt.getInstanceClassName());
        System.out.println("MTN: " + rt.getMetaTypeName());
        
        System.out.println("Constraints: " + rt.getOwnedConstraints());
        System.out.println("Context: " + rt.getOwnedContext());
        System.out.println("Invariants: " + rt.getOwnedInvariants());
        System.out.println("Key: " + rt.getOwnedKey());
        System.out.println("Properties: " + rt.getOwnedProperties());
        System.out.println("OP: " + rt.getOwningPackage());
        
        System.out.println("Behaviours: " + rt.getOwnedBehaviors());
        System.out.println("Rule: " + rt.getRule());
        
        Relation r = (Relation) rt.getRule().get(0);
        
        System.out.println("R/Variable: " + r.getVariable());
        System.out.println("R/Domain: " + r.getDomain());
        
        RelationDomain rd = (RelationDomain) r.getDomain().get(0);
        System.out.println("R/RelationDomain: " + rd.getPattern());
        
        // System.out.println("-------------");
        // System.out.println("R/OCLInvalid?: " + r.getVariable().get(2));
        // System.out.println("R/OCLInvalid?: " + r.getDomain().get(0).getPattern());
        
        // System.out.println(r.allOwnedElements());
        // System.out.println(r.getDomain().get(1).allOwnedElements());
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