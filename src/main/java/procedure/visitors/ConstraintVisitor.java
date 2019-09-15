package procedure.visitors;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.ocl.pivot.Annotation;
import org.eclipse.ocl.pivot.AnyType;
import org.eclipse.ocl.pivot.AssociationClass;
import org.eclipse.ocl.pivot.AssociationClassCallExp;
import org.eclipse.ocl.pivot.BagType;
import org.eclipse.ocl.pivot.Behavior;
import org.eclipse.ocl.pivot.BooleanLiteralExp;
import org.eclipse.ocl.pivot.CallExp;
import org.eclipse.ocl.pivot.CallOperationAction;
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
import org.eclipse.ocl.pivot.Constraint;
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
import org.eclipse.ocl.pivot.SendSignalAction;
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
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.ocl.pivot.VariableDeclaration;
import org.eclipse.ocl.pivot.VariableExp;
import org.eclipse.ocl.pivot.Vertex;
import org.eclipse.ocl.pivot.VoidType;
import org.eclipse.ocl.pivot.WildcardType;
import org.eclipse.ocl.pivot.util.AbstractVisitor;
import org.eclipse.ocl.pivot.util.Visitable;
import parsers.qvtr.QVTRelation;
import procedure.translators.TranslatorContext;

/**
 * Traverse the abstract syntax tree of an OCL expression and recursively
 * returns the translation into Z3 expressions of OCL expressions.
 * 
 * @author Aurélien Pepin
 */
public class ConstraintVisitor extends AbstractVisitor<Expr, TranslatorContext> {
    
    /**
     * The relation in which the constraint visitor is created.
     */
    private final QVTRelation relation;
    
    /**
     * An access to the constraint factory to handle complex translations.
     */
    private final ConstraintFactory factory;
    
    
    public ConstraintVisitor(QVTRelation relation, TranslatorContext context) {
        super(context);
        this.relation = relation;
        this.factory = new ConstraintFactory(context);
    }

    @Override
    public Expr visitAssociationClassCallExp(AssociationClassCallExp acce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK
    public Expr visitBooleanLiteralExp(BooleanLiteralExp ble) {
        return context.getZ3Ctx().mkBool(ble.isBooleanSymbol());
    }

    @Override
    public Expr visitCallExp(CallExp ce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK // Example: Sequence{5, 3, 5}
    public Expr visitCollectionLiteralExp(CollectionLiteralExp cle) {
        return factory.fromCollectionLiteral(cle);
    }

    @Override
    public Expr visitEnumLiteralExp(EnumLiteralExp ele) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitFeatureCallExp(FeatureCallExp fce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK
    public Expr visitIfExp(IfExp ifexp) {
        BoolExpr condition = (BoolExpr) ifexp.getOwnedCondition().accept(this);
        Expr thenExpr = ifexp.getOwnedThen().accept(this);
        Expr elseExpr = ifexp.getOwnedElse().accept(this);
        
        return context.getZ3Ctx().mkITE(condition, thenExpr, elseExpr);
    }

    @Override // OK
    public Expr visitIntegerLiteralExp(IntegerLiteralExp ile) {
        return context.getZ3Ctx().mkInt(ile.getIntegerSymbol().longValue());
    }

    @Override
    public Expr visitInvalidLiteralExp(InvalidLiteralExp ile) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitIterateExp(IterateExp ie) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitIteratorExp(IteratorExp ie) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLetExp(LetExp letexp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLiteralExp(LiteralExp le) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLoopExp(LoopExp le) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitMapLiteralExp(MapLiteralExp mle) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitMessageExp(MessageExp me) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitNavigationCallExp(NavigationCallExp nce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitNullLiteralExp(NullLiteralExp nle) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitNumericLiteralExp(NumericLiteralExp nle) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK
    public Expr visitOperationCallExp(OperationCallExp oce) {
        List<Expr> operands = new ArrayList<>();
        operands.add(oce.getOwnedSource().accept(this));
        
        for (OCLExpression operand : oce.getOwnedArguments()) {
            operands.add(operand.accept(this));
        }
        
        return factory.fromOperationCall(oce, operands);
    }

    @Override
    public Expr visitOppositePropertyCallExp(OppositePropertyCallExp opce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPrimitiveLiteralExp(PrimitiveLiteralExp ple) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPropertyCallExp(PropertyCallExp pce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK
    public Expr visitRealLiteralExp(RealLiteralExp rle) {
        return context.getZ3Ctx().mkReal(rle.getRealSymbol().toString());
    }

    @Override
    public Expr visitShadowExp(ShadowExp se) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitStateExp(StateExp se) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK
    public Expr visitStringLiteralExp(StringLiteralExp sle) {
        return context.getZ3Ctx().mkString(sle.getStringSymbol());
    }

    @Override
    public Expr visitTupleLiteralExp(TupleLiteralExp tle) {
        // To use: tle.getOwnedParts()
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTypeExp(TypeExp te) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitUnlimitedNaturalLiteralExp(UnlimitedNaturalLiteralExp unle) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitUnspecifiedValueExp(UnspecifiedValueExp uve) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override // OK
    public Expr visitVariableExp(VariableExp ve) {
        return factory.fromVariableExp(ve, relation);
    }
    
    /* **********************************************************
     * BELOW, VISITOR'S METHODS THAT DON'T MATCH OCL EXPRESSIONS.
     * **********************************************************
     */

    @Override
    public Expr visiting(Visitable vstbl) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitAnnotation(Annotation antn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitAnyType(AnyType at) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitAssociationClass(AssociationClass ac) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitBagType(BagType bt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitBehavior(Behavior bhvr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCallOperationAction(CallOperationAction action) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitClass(Class type) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCollectionItem(CollectionItem ci) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCollectionLiteralPart(CollectionLiteralPart clp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCollectionRange(CollectionRange cr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCollectionType(CollectionType ct) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitComment(Comment cmnt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCompleteClass(CompleteClass cc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCompleteEnvironment(CompleteEnvironment ce) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCompleteModel(CompleteModel cm) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitCompletePackage(CompletePackage cp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitConnectionPointReference(ConnectionPointReference cpr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitConstraint(Constraint c) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDataType(DataType dt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDetail(Detail detail) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDynamicBehavior(DynamicBehavior db) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDynamicElement(DynamicElement de) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDynamicProperty(DynamicProperty dp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDynamicType(DynamicType dt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitDynamicValueSpecification(DynamicValueSpecification dvs) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitElement(Element elmnt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitElementExtension(ElementExtension ee) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitEnumeration(Enumeration enmrtn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitEnumerationLiteral(EnumerationLiteral el) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitExpressionInOCL(ExpressionInOCL eiocl) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitFeature(Feature ftr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitFinalState(FinalState fs) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitImport(Import i) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitInstanceSpecification(InstanceSpecification is) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitInvalidType(InvalidType it) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitIterableType(IterableType it) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitIteration(Iteration itrtn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitIteratorVariable(IteratorVariable iv) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLambdaType(LambdaType lt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLanguageExpression(LanguageExpression le) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLetVariable(LetVariable lv) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitLibrary(Library lbr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitMapLiteralPart(MapLiteralPart mlp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitMapType(MapType mt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitMessageType(MessageType mt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitModel(Model model) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitNamedElement(NamedElement ne) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitNamespace(Namespace nmspc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitOCLExpression(OCLExpression ocle) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitOperation(Operation oprtn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitOrderedSetType(OrderedSetType ost) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitOrphanCompletePackage(OrphanCompletePackage ocp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPackage(Package pckg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitParameter(Parameter prmtr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitParameterVariable(ParameterVariable pv) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPrecedence(Precedence prcdnc) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPrimitiveCompletePackage(PrimitiveCompletePackage pcp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPrimitiveType(PrimitiveType pt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitProfile(Profile prfl) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitProfileApplication(ProfileApplication pa) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitProperty(Property prprt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitPseudostate(Pseudostate psdst) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitRegion(Region region) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitResultVariable(ResultVariable rv) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitSelfType(SelfType st) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitSendSignalAction(SendSignalAction action) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitSequenceType(SequenceType st) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitSetType(SetType st) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitShadowPart(ShadowPart sp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitSignal(Signal signal) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitSlot(Slot slot) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitStandardLibrary(StandardLibrary sl) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitState(State state) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitStateMachine(StateMachine sm) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitStereotype(Stereotype strtp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitStereotypeExtender(StereotypeExtender se) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTemplateBinding(TemplateBinding tb) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTemplateParameter(TemplateParameter tp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTemplateParameterSubstitution(TemplateParameterSubstitution tps) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTemplateSignature(TemplateSignature ts) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTemplateableElement(TemplateableElement te) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTransition(Transition trnstn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTrigger(Trigger trgr) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTupleLiteralPart(TupleLiteralPart tlp) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTupleType(TupleType tt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitType(Type type) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitTypedElement(TypedElement te) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitValueSpecification(ValueSpecification vs) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitVariable(Variable vrbl) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitVariableDeclaration(VariableDeclaration vd) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitVertex(Vertex vertex) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitVoidType(VoidType vt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Expr visitWildcardType(WildcardType wt) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public ConstraintFactory getFactory() {
        return factory;
    }
}
