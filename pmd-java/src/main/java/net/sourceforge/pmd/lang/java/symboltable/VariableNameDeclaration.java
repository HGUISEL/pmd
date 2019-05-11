/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.symboltable;

import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTLambdaExpression;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.AccessNode;
import net.sourceforge.pmd.lang.java.ast.TypeNode;
import net.sourceforge.pmd.lang.symboltable.AbstractNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.Scope;

public class VariableNameDeclaration extends AbstractNameDeclaration implements TypedNameDeclaration {

    public VariableNameDeclaration(ASTVariableDeclaratorId node) {
        super(node);
    }

    @Override
    public Scope getScope() {
        return node.getScope().getEnclosingScope(ClassScope.class);
    }

    public boolean isArray() {
        return getDeclaratorId().hasArrayType();
    }

    public int getArrayDepth() {
        return getTypeNode().getArrayDepth();
    }

    public boolean isVarargs() {
        ASTFormalParameter parameter = node.getFirstParentOfType(ASTFormalParameter.class);
        return parameter != null && parameter.isVarargs();
    }

    public boolean isExceptionBlockParameter() {
        return getDeclaratorId().isExceptionBlockParameter();
    }

    /**
     * @deprecated use {@link #isTypeInferred()}
     */
    @Deprecated
    public boolean isLambdaTypelessParameter() {
        return isTypeInferred();
    }

    public boolean isTypeInferred() {
        return getDeclaratorId().isTypeInferred();
    }

    public boolean isPrimitiveType() {
        return getTypeNode().isPrimitiveType();
    }

    @Override
    public String getTypeImage() {
        TypeNode typeNode = getTypeNode();
        if (typeNode != null) {
            return typeNode.getImage();
        }
        return null;
    }

    /**
     * Note that an array of primitive types (int[]) is a reference type.
     */
    public boolean isReferenceType() {
        return getTypeNode() instanceof ASTReferenceType;
    }

    public AccessNode getAccessNodeParent() {
        if (node.jjtGetParent() instanceof ASTFormalParameter || node.jjtGetParent() instanceof ASTLambdaExpression) {
            return (AccessNode) node.jjtGetParent();
        }
        return (AccessNode) node.jjtGetParent().jjtGetParent();
    }

    public ASTVariableDeclaratorId getDeclaratorId() {
        return (ASTVariableDeclaratorId) node;
    }

    private ASTType getTypeNode() {
        return getDeclaratorId().getTypeNode();
    }

    @Override
    public Class<?> getType() {
        TypeNode typeNode = getTypeNode();
        if (typeNode != null) {
            return typeNode.getType();
        }
        // if there is no type node, then return the type of the declarator id.
        // this might be a inferred type
        return getDeclaratorId().getType();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VariableNameDeclaration)) {
            return false;
        }
        VariableNameDeclaration n = (VariableNameDeclaration) o;
        return n.node.getImage().equals(node.getImage());
    }

    @Override
    public int hashCode() {
        return node.getImage().hashCode();
    }

    @Override
    public String toString() {
        return "Variable: image = '" + node.getImage() + "', line = " + node.getBeginLine();
    }
}
