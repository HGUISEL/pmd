/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTUnaryExpression.java */

package net.sourceforge.pmd.lang.java.ast;


/**
 * Represents a unary prefix operation on a value.
 * This has a precedence greater than {@link ASTMultiplicativeExpression}.
 *
 * <p>UnaryExpression has the same precedence as {@linkplain ASTPreIncrementExpression PreIncrementExpression},
 * {@linkplain ASTPreDecrementExpression PreDecrementExpression}, and {@linkplain ASTCastExpression CastExpression}.
 *
 * <p>Note that the child of this node is not necessarily a UnaryExpression,
 * rather, it can be an expression with an operator precedence greater or equal
 * to a UnaryExpression.
 *
 * <p>TODO it would be sensible to make {@link ASTPreDecrementExpression} and {@link ASTPreIncrementExpression} extend this node
 *
 * <pre>
 *
 * UnaryExpression ::= ( "+" | "-" | "~" | "!" ) UnaryExpression
 *
 * </pre>
 */
public class ASTUnaryExpression extends AbstractJavaTypeNode implements ASTExpression {

    private UnaryOp operator;

    public ASTUnaryExpression(int id) {
        super(id);
    }

    public ASTUnaryExpression(JavaParser p, int id) {
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


    @Override
    public void setImage(String image) {
        super.setImage(image);
        this.operator = UnaryOp.fromImage(image);
    }


    /**
     * Returns the image of this unary operator, i.e. "+" or "-".
     * @deprecated use {@link #getOp()}
     */
    @Deprecated
    public String getOperator() {
        return getImage();
    }


    /**
     * Returns the constant representing the operator of this expression.
     */
    public UnaryOp getOp() {
        return operator;
    }

}
