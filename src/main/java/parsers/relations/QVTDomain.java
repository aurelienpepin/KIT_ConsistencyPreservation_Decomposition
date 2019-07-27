package parsers.relations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.EvaluationVisitor;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.helper.Choice;
import org.eclipse.ocl.helper.ConstraintKind;
import org.eclipse.ocl.internal.helper.OCLSyntaxHelper;
import org.eclipse.ocl.parser.ValidationVisitor;
import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtbase.Pattern;
import org.eclipse.qvtd.pivot.qvtrelation.DomainPattern;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.ObjectTemplateExp;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import org.eclipse.qvtd.pivot.qvttemplate.TemplateExp;
import org.eclipse.ocl.pivot.*;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.utilities.ParserException;
import org.eclipse.qvtd.pivot.qvtrelation.RelationCallExp;
/**
 * 
 * @author Aurélien Pepin
 */
public class QVTDomain {
    
    /**
     * Corresponding QVT-R domain element.
     */
    private final RelationDomain domain;
    
    private final List<DomainPattern> patterns;
    
    private final List<PropertyTemplateItem> parts;
    
    public QVTDomain(RelationDomain domain) {
        this.domain = domain;
        this.patterns = new ArrayList<>();
        this.parts = new ArrayList<>();
        
        for (DomainPattern dp : domain.getPattern()) {
            this.patterns.add(dp);
            this.parts.addAll(((ObjectTemplateExp) dp.getTemplateExpression()).getPart());
        }
        
//        System.out.println("domain: " + domain);
//        for (DomainPattern pattern : domain.getPattern()) {
//            System.out.println("pattern: " + pattern);
//            ObjectTemplateExp otexp = (ObjectTemplateExp) pattern.getTemplateExpression();
//            System.out.println("otexp: " + otexp);
//            for (PropertyTemplateItem pti : otexp.getPart()) {
//                System.out.println(pti);
//                System.out.println(pti.getResolvedProperty() + " " + pti.getReferredProperty() + " " + pti.getValue());
//                System.out.println(pti.getValue().eResource());
//                
//                // ValidationVisitor vv = new ValidationVisitor(environment);
//                // pti.getValue().accept();
//                // OCL ocl = OCL.newInstance();
//                // System.out.println(pti.getValue() instanceof VariableExp); // TRUE
//                // System.out.println(pti.getValue() instanceof RelationCallExp); // FALSE
//                
//                // System.out.println(((VariableExp) pti.getValue()).getReferredElement());
//                
//                // EcoreEnvironmentFactory eef = EcoreEnvironmentFactory.INSTANCE;
//                // EvaluationVisitor ev = eef.createEvaluationVisitor(eef.createEvaluationEnvironment(), evalEnv, extentMap)
//                // pti.getValue().accept(vv);
//                
//                // EcoreEnvironmentFactory.INSTANCE.createEvaluationEnvironment();
//                // System.out.println(pti.getValue().getType());
//                // System.out.println(pti.getValue().getTypeId());
//                // System.out.println(pti.getValue().getTypeValue());
//                // pti.getValue().accept(new TransformationTranslator());
//                 
//                // TransformationTranslator tt = new TransformationTranslator();
//                // pti.getValue().accept(tt);
//                // try {
//                //     ExpressionInOCL eio = ocl.createQuery(pti.getReferredProperty(), "name > 0 and name <= 0");
//                //     eio.getOwnedBody().accept(tt);
//                // } catch (ParserException ex) {
//                //     Logger.getLogger(QVTDomain.class.getName()).log(Level.SEVERE, null, ex);
//                // }
//            }
//        }      
    }
    
    public List<DomainPattern> getPatterns() {
        return patterns;
    }

    public List<PropertyTemplateItem> getParts() {
        return parts;
    }
}
