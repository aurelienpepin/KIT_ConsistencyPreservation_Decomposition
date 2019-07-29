package metamodels.vertices;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;

public class EPackageVertex extends ENamedElementVertex {

    public EPackageVertex(ENamedElement element) {
        super(element);
    }

    @Override
    public String getFullName() {
        EPackage packElem = (EPackage) element;
        StringBuilder sb = new StringBuilder();
        
        if (packElem.getESuperPackage() != null) {
            sb.append(packElem.getESuperPackage().getNsURI()).append("::");
        }
        
        sb.append(element.getName());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Package{" + this.getFullName() + "}";
    }
    
    @Override
    public EPackage getElement() {
        return (EPackage) element;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        
//        final EPackageVertex other = (EPackageVertex) obj;
//        return this.getElement().getNsURI().equals(other.getElement().getNsURI());
//    }
//
//    @Override
//    public int hashCode() {
//        return this.getElement().getNsURI().hashCode();
//    }
} 