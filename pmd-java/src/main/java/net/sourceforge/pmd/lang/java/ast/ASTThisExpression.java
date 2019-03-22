/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTNullLiteral.java */

package net.sourceforge.pmd.lang.java.ast;

import javax.annotation.Nullable;


/**
 * The "this" expression. Related to the {@link ASTSuperExpression "super"} pseudo-expression.
 *
 * <pre class="grammar">
 *
 * ThisExpression ::= "this"
 *                  | {@link ASTClassOrInterfaceType TypeName} "." "this"
 *
 * </pre>
 */
public final class ASTThisExpression extends AbstractLateInitNode implements ASTPrimaryExpression {
    ASTThisExpression(int id) {
        super(id);
    }


    ASTThisExpression(JavaParser p, int id) {
        super(p, id);
    }


    @Nullable
    public ASTClassOrInterfaceType getQualifier() {
        return jjtGetNumChildren() > 0 ? (ASTClassOrInterfaceType) jjtGetChild(0) : null;
    }


    /**
     * Accept the visitor. *
     */
    @Override
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    @Override
    public <T> void jjtAccept(SideEffectingVisitor<T> visitor, T data) {
        visitor.visit(this, data);
    }


    @Override
    void onInjectFinished() {
        // If this method is called, then a qualifier was injected
        ASTAmbiguousName name = (ASTAmbiguousName) jjtGetChild(0);
        this.replaceChildAt(0, name.forceTypeContext());
    }
}
