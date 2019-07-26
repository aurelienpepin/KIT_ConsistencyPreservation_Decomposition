package procedure.translators;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.expressions.VariableExp;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.SendSignalAction;
import org.eclipse.ocl.pivot.Annotation;
import org.eclipse.ocl.pivot.AnyType;
import org.eclipse.ocl.pivot.AssociationClass;
import org.eclipse.ocl.pivot.AssociationClassCallExp;
import org.eclipse.ocl.pivot.BagType;
import org.eclipse.ocl.pivot.Behavior;
import org.eclipse.ocl.pivot.BooleanLiteralExp;
import org.eclipse.ocl.pivot.CallExp;
import org.eclipse.ocl.pivot.Class;
import org.eclipse.ocl.pivot.CollectionItem;
import org.eclipse.ocl.pivot.CollectionLiteralExp;
import org.eclipse.ocl.pivot.CollectionLiteralPart;
import org.eclipse.ocl.pivot.CollectionRange;
import org.eclipse.ocl.pivot.CollectionType;
import org.eclipse.ocl.pivot.Comment;
import org.eclipse.ocl.pivot.CompleteClass;
import org.eclipse.ocl.pivot.CompleteEnvironment;
import org.eclipse.ocl.pivot.CompleteModel;
import org.eclipse.ocl.pivot.CompletePackage;
import org.eclipse.ocl.pivot.ConnectionPointReference;
import org.eclipse.ocl.pivot.DataType;
import org.eclipse.ocl.pivot.Detail;
import org.eclipse.ocl.pivot.DynamicBehavior;
import org.eclipse.ocl.pivot.DynamicElement;
import org.eclipse.ocl.pivot.DynamicProperty;
import org.eclipse.ocl.pivot.DynamicType;
import org.eclipse.ocl.pivot.DynamicValueSpecification;
import org.eclipse.ocl.pivot.Element;
import org.eclipse.ocl.pivot.ElementExtension;
import org.eclipse.ocl.pivot.EnumLiteralExp;
import org.eclipse.ocl.pivot.Enumeration;
import org.eclipse.ocl.pivot.EnumerationLiteral;
import org.eclipse.ocl.pivot.ExpressionInOCL;
import org.eclipse.ocl.pivot.Feature;
import org.eclipse.ocl.pivot.FeatureCallExp;
import org.eclipse.ocl.pivot.FinalState;
import org.eclipse.ocl.pivot.IfExp;
import org.eclipse.ocl.pivot.Import;
import org.eclipse.ocl.pivot.InstanceSpecification;
import org.eclipse.ocl.pivot.IntegerLiteralExp;
import org.eclipse.ocl.pivot.InvalidLiteralExp;
import org.eclipse.ocl.pivot.InvalidType;
import org.eclipse.ocl.pivot.IterableType;
import org.eclipse.ocl.pivot.IterateExp;
import org.eclipse.ocl.pivot.Iteration;
import org.eclipse.ocl.pivot.IteratorExp;
import org.eclipse.ocl.pivot.IteratorVariable;
import org.eclipse.ocl.pivot.LambdaType;
import org.eclipse.ocl.pivot.LanguageExpression;
import org.eclipse.ocl.pivot.LetExp;
import org.eclipse.ocl.pivot.LetVariable;
import org.eclipse.ocl.pivot.Library;
import org.eclipse.ocl.pivot.LiteralExp;
import org.eclipse.ocl.pivot.LoopExp;
import org.eclipse.ocl.pivot.MapLiteralExp;
import org.eclipse.ocl.pivot.MapLiteralPart;
import org.eclipse.ocl.pivot.MapType;
import org.eclipse.ocl.pivot.MessageExp;
import org.eclipse.ocl.pivot.MessageType;
import org.eclipse.ocl.pivot.Model;
import org.eclipse.ocl.pivot.NamedElement;
import org.eclipse.ocl.pivot.Namespace;
import org.eclipse.ocl.pivot.NavigationCallExp;
import org.eclipse.ocl.pivot.NullLiteralExp;
import org.eclipse.ocl.pivot.NumericLiteralExp;
import org.eclipse.ocl.pivot.OCLExpression;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.ocl.pivot.OperationCallExp;
import org.eclipse.ocl.pivot.OppositePropertyCallExp;
import org.eclipse.ocl.pivot.OrderedSetType;
import org.eclipse.ocl.pivot.OrphanCompletePackage;
import org.eclipse.ocl.pivot.Package;
import org.eclipse.ocl.pivot.Parameter;
import org.eclipse.ocl.pivot.ParameterVariable;
import org.eclipse.ocl.pivot.Precedence;
import org.eclipse.ocl.pivot.PrimitiveCompletePackage;
import org.eclipse.ocl.pivot.PrimitiveLiteralExp;
import org.eclipse.ocl.pivot.PrimitiveType;
import org.eclipse.ocl.pivot.Profile;
import org.eclipse.ocl.pivot.ProfileApplication;
import org.eclipse.ocl.pivot.Property;
import org.eclipse.ocl.pivot.PropertyCallExp;
import org.eclipse.ocl.pivot.Pseudostate;
import org.eclipse.ocl.pivot.RealLiteralExp;
import org.eclipse.ocl.pivot.Region;
import org.eclipse.ocl.pivot.ResultVariable;
import org.eclipse.ocl.pivot.SelfType;
import org.eclipse.ocl.pivot.SequenceType;
import org.eclipse.ocl.pivot.SetType;
import org.eclipse.ocl.pivot.ShadowExp;
import org.eclipse.ocl.pivot.ShadowPart;
import org.eclipse.ocl.pivot.Signal;
import org.eclipse.ocl.pivot.Slot;
import org.eclipse.ocl.pivot.StandardLibrary;
import org.eclipse.ocl.pivot.State;
import org.eclipse.ocl.pivot.StateExp;
import org.eclipse.ocl.pivot.StateMachine;
import org.eclipse.ocl.pivot.Stereotype;
import org.eclipse.ocl.pivot.StereotypeExtender;
import org.eclipse.ocl.pivot.StringLiteralExp;
import org.eclipse.ocl.pivot.TemplateBinding;
import org.eclipse.ocl.pivot.TemplateParameter;
import org.eclipse.ocl.pivot.TemplateParameterSubstitution;
import org.eclipse.ocl.pivot.TemplateSignature;
import org.eclipse.ocl.pivot.TemplateableElement;
import org.eclipse.ocl.pivot.Transition;
import org.eclipse.ocl.pivot.Trigger;
import org.eclipse.ocl.pivot.TupleLiteralExp;
import org.eclipse.ocl.pivot.TupleLiteralPart;
import org.eclipse.ocl.pivot.TupleType;
import org.eclipse.ocl.pivot.Type;
import org.eclipse.ocl.pivot.TypeExp;
import org.eclipse.ocl.pivot.TypedElement;
import org.eclipse.ocl.pivot.UnlimitedNaturalLiteralExp;
import org.eclipse.ocl.pivot.UnspecifiedValueExp;
import org.eclipse.ocl.pivot.ValueSpecification;
import org.eclipse.ocl.pivot.VariableDeclaration;
import org.eclipse.ocl.pivot.Vertex;
import org.eclipse.ocl.pivot.VoidType;
import org.eclipse.ocl.pivot.WildcardType;
import org.eclipse.ocl.pivot.util.AbstractVisitor;
import org.eclipse.ocl.pivot.util.Visitable;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.utilities.OCLHelper;
import org.eclipse.ocl.pivot.utilities.ParserException;

/**
 *
 * @author Aurélien Pepin
 */
public class ToRemoveTransformationTranslator extends AbstractVisitor<String, Object> {

    public ToRemoveTransformationTranslator() {
        super(new Object());
    }
    
    @Override
    public String visiting(Visitable vstbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitAnnotation(Annotation antn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitAnyType(AnyType at) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitAssociationClass(AssociationClass ac) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitAssociationClassCallExp(AssociationClassCallExp acce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitBagType(BagType bt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitBehavior(Behavior bhvr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitBooleanLiteralExp(BooleanLiteralExp ble) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCallExp(CallExp ce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCallOperationAction(org.eclipse.ocl.pivot.CallOperationAction action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitClass(Class type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCollectionItem(CollectionItem ci) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCollectionLiteralExp(CollectionLiteralExp cle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCollectionLiteralPart(CollectionLiteralPart clp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCollectionRange(CollectionRange cr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCollectionType(CollectionType ct) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitComment(Comment cmnt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCompleteClass(CompleteClass cc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCompleteEnvironment(CompleteEnvironment ce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCompleteModel(CompleteModel cm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitCompletePackage(CompletePackage cp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitConnectionPointReference(ConnectionPointReference cpr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitConstraint(org.eclipse.ocl.pivot.Constraint c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDataType(DataType dt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDetail(Detail detail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDynamicBehavior(DynamicBehavior db) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDynamicElement(DynamicElement de) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDynamicProperty(DynamicProperty dp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDynamicType(DynamicType dt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitDynamicValueSpecification(DynamicValueSpecification dvs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitElement(Element elmnt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitElementExtension(ElementExtension ee) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitEnumLiteralExp(EnumLiteralExp ele) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitEnumeration(Enumeration enmrtn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitEnumerationLiteral(EnumerationLiteral el) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitExpressionInOCL(ExpressionInOCL eiocl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitFeature(Feature ftr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitFeatureCallExp(FeatureCallExp fce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitFinalState(FinalState fs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIfExp(IfExp ifexp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitImport(Import i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitInstanceSpecification(InstanceSpecification is) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIntegerLiteralExp(IntegerLiteralExp ile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitInvalidLiteralExp(InvalidLiteralExp ile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitInvalidType(InvalidType it) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIterableType(IterableType it) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIterateExp(IterateExp ie) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIteration(Iteration itrtn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIteratorExp(IteratorExp ie) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitIteratorVariable(IteratorVariable iv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLambdaType(LambdaType lt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLanguageExpression(LanguageExpression le) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLetExp(LetExp letexp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLetVariable(LetVariable lv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLibrary(Library lbr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLiteralExp(LiteralExp le) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitLoopExp(LoopExp le) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitMapLiteralExp(MapLiteralExp mle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitMapLiteralPart(MapLiteralPart mlp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitMapType(MapType mt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitMessageExp(MessageExp me) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitMessageType(MessageType mt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitModel(Model model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitNamedElement(NamedElement ne) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitNamespace(Namespace nmspc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitNavigationCallExp(NavigationCallExp nce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitNullLiteralExp(NullLiteralExp nle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitNumericLiteralExp(NumericLiteralExp nle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitOCLExpression(OCLExpression ocle) {
        System.out.println("lolilol");
        System.out.println("sera-ce imprimé un jour ??????");
        
        return "lolilol";
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitOperation(Operation oprtn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitOperationCallExp(OperationCallExp oce) {
        System.out.println("> " + oce.getOwnedArguments());
        System.out.println(">> " + oce.getReferredOperation());
        System.out.println(">>> " + oce.getOwnedSource());
        return "test";
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitOppositePropertyCallExp(OppositePropertyCallExp opce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitOrderedSetType(OrderedSetType ost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitOrphanCompletePackage(OrphanCompletePackage ocp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPackage(Package pckg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitParameter(Parameter prmtr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitParameterVariable(ParameterVariable pv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPrecedence(Precedence prcdnc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPrimitiveCompletePackage(PrimitiveCompletePackage pcp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPrimitiveLiteralExp(PrimitiveLiteralExp ple) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPrimitiveType(PrimitiveType pt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitProfile(Profile prfl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitProfileApplication(ProfileApplication pa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitProperty(Property prprt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPropertyCallExp(PropertyCallExp pce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitPseudostate(Pseudostate psdst) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitRealLiteralExp(RealLiteralExp rle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitRegion(Region region) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitResultVariable(ResultVariable rv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitSelfType(SelfType st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitSendSignalAction(org.eclipse.ocl.pivot.SendSignalAction action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitSequenceType(SequenceType st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitSetType(SetType st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitShadowExp(ShadowExp se) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitShadowPart(ShadowPart sp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitSignal(Signal signal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitSlot(Slot slot) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStandardLibrary(StandardLibrary sl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitState(State state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStateExp(StateExp se) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStateMachine(StateMachine sm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStereotype(Stereotype strtp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStereotypeExtender(StereotypeExtender se) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitStringLiteralExp(StringLiteralExp sle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTemplateBinding(TemplateBinding tb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTemplateParameter(TemplateParameter tp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTemplateParameterSubstitution(TemplateParameterSubstitution tps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTemplateSignature(TemplateSignature ts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTemplateableElement(TemplateableElement te) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTransition(Transition trnstn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTrigger(Trigger trgr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTupleLiteralExp(TupleLiteralExp tle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTupleLiteralPart(TupleLiteralPart tlp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTupleType(TupleType tt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitType(Type type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTypeExp(TypeExp te) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitTypedElement(TypedElement te) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitUnlimitedNaturalLiteralExp(UnlimitedNaturalLiteralExp unle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitUnspecifiedValueExp(UnspecifiedValueExp uve) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitValueSpecification(ValueSpecification vs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitVariable(org.eclipse.ocl.pivot.Variable vrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitVariableDeclaration(VariableDeclaration vd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitVariableExp(org.eclipse.ocl.pivot.VariableExp ve) {
        System.out.println("fergtrgtrrg " + ve);
        return "lol"; // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitVertex(Vertex vertex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitVoidType(VoidType vt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visitWildcardType(WildcardType wt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}

//public class TransformationTranslator<C, O, P, EL, PM, S, COA, SSA, CT> extends
//    AbstractVisitor<String, C, O, P, EL, PM, S, COA, SSA, CT> {
//
//    Environment<?, C, O, P, EL, PM, S, COA, SSA, CT, ?, ?> _env = null;
//    
//    public static TransformationTranslator<EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint> getEcoreVersion() {
//    Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> auxEnv = EcoreEnvironmentFactory.INSTANCE
//        .createEnvironment();
//    TransformationTranslator<EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint> res = new TransformationTranslator<EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint>(
//        auxEnv);
//    return res;
//  }
//    
//    protected TransformationTranslator(
//      Environment<?, C, O, P, EL, PM, S, COA, SSA, CT, ?, ?> env) {
//    _env = env;
//  }
//    
//    @Override
//    public String visitVariableExp(VariableExp<C, PM> v) {
//        System.out.println(">>>>>>>>>> VARIABLE: " + v);
//        return super.visitVariableExp(v); //To change body of generated methods, choose Tools | Templates.
//    }
//}
