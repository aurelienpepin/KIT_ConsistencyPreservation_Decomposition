package metamodels.vertices;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;

public class EPackageVertex extends ENamedElementVertex {

    public EPackageVertex(ENamedElement element) {
        super(element);
    }

//    @Override
//    public String getFullName() {
//        EPackage packElem = (EPackage) element;
//        StringBuilder sb = new StringBuilder();
//        
//        if (packElem.getESuperPackage() != null) {
//            sb.append(packElem.getESuperPackage().getName()).append("::");
//        }
//        
//        sb.append(element.getName());
//        return sb.toString();
//    }

    @Override
    public String toString() {
        return "Package{" + element.getName() + "}";
    }
    
    @Override
    public EPackage getElement() {
        return (EPackage) element;
    }
} 