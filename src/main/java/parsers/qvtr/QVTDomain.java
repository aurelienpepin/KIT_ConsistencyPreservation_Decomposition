package parsers.qvtr;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.ocl.pivot.Element;
import org.eclipse.qvtd.pivot.qvtrelation.DomainPattern;
import org.eclipse.qvtd.pivot.qvtrelation.RelationDomain;
import org.eclipse.qvtd.pivot.qvttemplate.ObjectTemplateExp;
import org.eclipse.qvtd.pivot.qvttemplate.PropertyTemplateItem;
import procedure.translators.TranslatorContext;
import procedure.visitors.DependencyVisitor;

/**
 * Represents a domain in QVT-R, i.e. a typed variable that can be matched
 * in a model of a given model type. A domain is part of a relation.
 * 
 * @author Aurélien Pepin
 */
public class QVTDomain {
    
    /**
     * Corresponding QVT-R domain element.
     */
    private final RelationDomain domain;
    
    /**
     * List of patterns in the domain.
     * A pattern is made of a set of variables and a set of constraints that
     * model elements bound to those variables must satisfy.
     */
    private final List<DomainPattern> patterns;
    
    /**
     * (Shortcut) List of property template items (PTIs).
     * A PTI is where the condition occurs in a pattern.
     */
    private final List<PropertyTemplateItem> parts;
    
    
    public QVTDomain(RelationDomain domain) {
        this.domain = domain;
        this.patterns = new ArrayList<>();
        this.parts = new ArrayList<>();
        
        // System.out.println("VISITONS LE DOMAIN: ");
        // domain.accept(new DependencyVisitor(TranslatorContext.getInstance()));
        
        // System.out.println(domain.eAllContents());
        System.out.println("aaaaaaaaaaaaaaaah: " + domain.getPattern().get(0));
        System.out.println("aaaaaaaaaaaaaaaah: " + domain.getPattern().get(0).getTemplateExpression().getWhere());
        for (Element el : domain.allOwnedElements()) {
            System.out.println(el + "\t\t||| " + el.eContainer().eClass() + " ? " + el.eContainmentFeature());
        }
        
        
        
        for (DomainPattern dp : domain.getPattern()) {
            this.patterns.add(dp);
            this.parts.addAll(((ObjectTemplateExp) dp.getTemplateExpression()).getPart());
        }   
    }
    
    public List<DomainPattern> getPatterns() {
        return patterns;
    }

    public List<PropertyTemplateItem> getParts() {
        return parts;
    }

    public RelationDomain getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return domain.toString() + ":" + parts;
    }
}
