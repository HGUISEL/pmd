/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTFormalParameter.java */

package net.sourceforge.pmd.lang.java.ast;

import org.checkerframework.checker.nullness.qual.Nullable;

import net.sourceforge.pmd.lang.java.typeresolution.typedefinition.JavaTypeDefinition;


/**
 * Formal parameter of a lambda expression. Child of {@link ASTLambdaParameterList}.
 *
 * <pre class="grammar">
 *
 * LambdaParameter ::= ( "final" | {@link ASTAnnotation Annotation} )* ( "var" | {@link ASTType Type}) [ "..." ] {@link ASTVariableDeclaratorId VariableDeclaratorId}
 *                   | {@link ASTVariableDeclaratorId VariableDeclaratorId}
 *
 * </pre>
 */
public final class ASTLambdaParameter extends AbstractJavaTypeNode {

    private boolean isVarargs;
    private boolean isFinal;
    private boolean isVarType;

    public ASTLambdaParameter(int id) {
        super(id);
    }

    public ASTLambdaParameter(JavaParser p, int id) {
        super(p, id);
    }


    void setVarargs() {
        isVarargs = true;
    }

    void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isFinal() {
        return isFinal;
    }

    void setVarType() {
        isVarType = true;
    }

    public boolean isVarType() {
        return isVarType;
    }

    /**
     * Returns true if this node is a varargs parameter.
     */
    public boolean isVarargs() {
        return isVarargs;
    }


    /**
     * If true, this formal parameter represents one without explicit types.
     * This can appear as part of a lambda expression with java11 using "var".
     *
     * @see ASTVariableDeclaratorId#isTypeInferred()
     */
    public boolean isTypeInferred() {
        return getTypeNode() == null;
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
     * Returns the declarator ID of this formal parameter.
     */
    public ASTVariableDeclaratorId getVariableDeclaratorId() {
        return getFirstChildOfType(ASTVariableDeclaratorId.class);
    }

    /**
     * Returns the type node of this formal parameter.
     * The type of that node is not necessarily the type
     * of the parameter itself, see {@link ASTVariableDeclaratorId#getType()}.
     *
     * <p>In particular, the type of the returned node
     * doesn't take into account whether this formal
     * parameter is varargs or not.
     */
    @Nullable
    public ASTType getTypeNode() {
        return getFirstChildOfType(ASTType.class);
    }


    /**
     * Returns the type of this formal parameter. That type
     * is exactly that of the variable declarator id,
     * which means that the declarator id's type takes into
     * account whether this parameter is varargs or not.
     */
    @Override
    public Class<?> getType() {
        return getVariableDeclaratorId().getType();
    }


    @Override
    public JavaTypeDefinition getTypeDefinition() {
        return getVariableDeclaratorId().getTypeDefinition();
    }


    /**
     * Noop, the type of this node is defined by the type
     * of the declarator id.
     */
    @Override
    public void setTypeDefinition(JavaTypeDefinition type) {
        // see javadoc
    }

    /**
     * Noop, the type of this node is defined by the type
     * of the declarator id.
     */
    @Override
    public void setType(Class<?> type) {
        // see javadoc
    }
}
