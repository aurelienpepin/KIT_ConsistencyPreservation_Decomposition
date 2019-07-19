package parsers;

import metamodels.Metagraph;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import org.eclipse.qvtd.xtext.qvtrelation.QVTrelationStandaloneSetup;
import org.eclipse.qvtd.pivot.qvtrelation.RelationModel;
import org.eclipse.qvtd.xtext.qvtrelationcs.TopLevelCS;

public class TransformationParser {
    
    public static void fillGraphWith(Metagraph graph, String qvtrFilePath) {
        if (graph == null)
            throw new RuntimeException("The metagraph cannot be null");

/* Resource resource;
//        ResourceSet resourceSet = new XtextResourceSet();
//        Resource pivotResource;
//        try{
//                System.err.println("avant le getResource");
//                URI inputURI = URI.createPlatformResourceURI(qvtrFilePath,true);
//                System.err.println("au milieu là");
//                // Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("qvtr", QVTrelationPivotStandaloneSetup.getInjector().getInstance(QVTrelationPivotStandaloneSetup));
//                
//                System.err.println(Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap());
//                BaseCSResource xtextResource = (BaseCSResource) resourceSet.getResource(inputURI, true);
//                System.err.println("après le getResource");
//                
//                xtextResource.unload();
//                xtextResource.load(resourceSet.getLoadOptions());
//
//                // adapter = xtextResource.getCS2ASAdapter(null);
//                pivotResource = xtextResource.getASResource();
//                //adapter = BaseCSResource.getCS2ASAdapter(xtextResource, null);
//                // pivotResource = adapter.getASResource(xtextResource);
//                pivotResource.setURI(URI.createURI(qvtrFilePath));
//                String message = PivotUtil.formatResourceDiagnostics(pivotResource.getErrors(), "Error parsing QVT.", "\n\t");
//                if (message != null)
//                    throw new RuntimeException(message + "QVT Parser");
//
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage() + "QVT Parser");
//        } */
        ResourceSet resourceSet = new ResourceSetImpl();
        
        OCLstdlib.install();
        QVTrelationStandaloneSetup.doSetup();
        URI qvtrURI = URI.createFileURI(qvtrFilePath);
        // EcoreUtil.getPlatformResourceMap().put("project", URI.createURI("file:///C:/"));
        
        // System.err.println(EcorePlugin.getWorkspaceRoot());
        // System.err.println(qvtrURI.toPlatformString(true));
        // System.err.println(EcorePlugin.resolvePlatformResourcePath(qvtrURI.toPlatformString(false)));
        
        // System.err.println("ici");
        // System.err.println("act: " + resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap());
        // System.err.println("ideal: " + Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap());
        // System.err.println("ideal (c): " + Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap());
        // System.err.println("ideal (p): " + Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap());
        // resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().putAll(Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap());

        // System.err.println("act<-ideal: " + resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap());
        Resource metamodelResource = resourceSet.getResource(qvtrURI, true);
        // System.err.println("et là");
        
        System.err.println(metamodelResource.getWarnings());
        
        TopLevelCS root = (TopLevelCS) metamodelResource.getContents().get(0);
        RelationModel rm = (RelationModel) root.getPivot();
        // RelationalTransformation rt = (RelationalTransformation) rm.eContents().get(3);
        
        System.out.println(rm.getName());
        System.out.println(rm.allOwnedElements());
        // EList<TransformationCS> test = ((TopLevelCSImpl) metamodelResource.getContents().get(0)).getOwnedTransformations();
        //System.err.println(test.get(0).getOwnedRelations().get(0).getOwnedDomains());
        
        // DomainCS dom = test.get(0).getOwnedRelations().get(0).
        
        
        // ESSAI CONV
        
        
        // System.err.println(((TopLevelCSImpl) metamodelResource.getContents().get(0)).getOwnedTransformations());
        // RelationModel rm = (RelationModel) metamodelResource.getContents().get(0);
        // RelationalTransformation transformation = (RelationalTransformation) rm.eContents().get(0); 
        
        // System.out.println(transformation.getName());
    }
}

// CF. https://www.eclipse.org/forums/index.php?t=msg&th=491160&goto=1110495&
// CF. https://github.com/haslab/echo/blob/6c35e510204fc71dcd2996fe1e084ed049408bb3/plugins/pt.uminho.haslab.echo/src/pt/uminho/haslab/mde/EMFParser.java