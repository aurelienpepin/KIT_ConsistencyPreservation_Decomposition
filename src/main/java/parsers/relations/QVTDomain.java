package parsers.relations;

import org.eclipse.qvtd.pivot.qvtbase.Domain;
import org.eclipse.qvtd.pivot.qvtbase.Pattern;
import org.eclipse.qvtd.pivot.qvtrelation.DomainPattern;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.ObjectTemplateExp;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import org.eclipse.qvtd.pivot.qvttemplate.TemplateExp;

/**
 * 
 * @author Aurélien Pepin
 */
public class QVTDomain {
    
    /**
     * Corresponding QVT-R domain element.
     */
    public final RelationDomain domain;
    
    public QVTDomain(RelationDomain domain) {
        this.domain = domain;
//        System.out.println("domain: " + domain);
//        for (DomainPattern pattern : domain.getPattern()) {
//            System.out.println("pattern: " + pattern);
//            ObjectTemplateExp otexp = (ObjectTemplateExp) pattern.getTemplateExpression();
//            System.out.println("otexp: " + otexp);
//            for (PropertyTemplateItem pti : otexp.getPart()) {
//                System.out.println(pti);
//                System.out.println(pti.getResolvedProperty() + " " + pti.getReferredProperty() + " " + pti.getValue());
//            }
//        }
    }
}
