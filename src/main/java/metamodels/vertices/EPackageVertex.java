package metamodels.vertices;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;

public class EPackageVertex extends ENamedElementVertex {

    public EPackageVertex(ENamedElement element) {
        super(element);
    }

    @Override
    public String toString() {
        return "Package{" + element.getName() + "}";
    }
    
    @Override
    public EPackage getElement() {
        return (EPackage) element;
    }
} 