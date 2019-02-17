/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTAllocationExpression.java */

package net.sourceforge.pmd.lang.java.ast;

/**
 * An array creation expression.
 *
 * <pre>
 *
 * ArrayCreationExpression ::= "new" {@link ASTAnnotation TypeAnnotation}*
 *                             ({@link ASTPrimitiveType PrimitiveType} | {@link ASTClassOrInterfaceType ClassOrInterfaceType})
 *                             {@link ASTArrayDimsAndInits ArrayDimsAndInits}
 *
 * </pre>
 */
public final class ASTArrayAllocation extends AbstractJavaTypeNode implements ASTPrimaryExpression {


    ASTArrayAllocation(int id) {
        super(id);
    }


    ASTArrayAllocation(JavaParser p, int id) {
        super(p, id);
    }


    @Override
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    @Override
    public <T> void jjtAccept(SideEffectingVisitor<T> visitor, T data) {
        visitor.visit(this, data);
    }


    /**
     * Returns the node representing the element type of the array. This
     * is never an {@link ASTArrayType array type}. The dimensions of the
     * instantiated array are carried by {@link #getArrayDims() another node}.
     */
    public ASTType getElementTypeNode() {
        return getFirstChildOfType(ASTType.class);
    }


    /**
     * Returns the dimensions of the array if this is an array creation expression.
     */
    public ASTArrayDimsAndInits getArrayDims() {
        return getFirstChildOfType(ASTArrayDimsAndInits.class);
    }


}