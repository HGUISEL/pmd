/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTPrimitiveType.java */

package net.sourceforge.pmd.lang.java.ast;

import java.util.Iterator;
import java.util.List;


/**
 * Represents an intersection type. Can only occur in the following contexts:
 * * Inside a {@linkplain ASTTypeParameter TypeParameter}
 * * As the target type of a {@linkplain ASTCastExpression CastExpression}, on Java 8 and above
 *
 * <pre>
 *
 * IntersectionType ::= {@link ASTType Type} ("&" {@link ASTType Type})+
 *
 * </pre>
 */
public class ASTIntersectionType extends AbstractJavaTypeNode implements ASTReferenceType, Iterable<ASTType> {

    ASTIntersectionType(int id) {
        super(id);
    }


    ASTIntersectionType(JavaParser p, int id) {
        super(p, id);
    }


    @Override
    public String getTypeImage() {
        return getBaseType().getImage();
    }

    /**
     * Returns the annotated type.
     */
    public ASTType getBaseType() {
        return (ASTType) jjtGetChild(jjtGetNumChildren() - 1);
    }

    /**
     * Returns the annotations contained within this node.
     */
    public List<ASTAnnotation> getAnnotations() {
        return findChildrenOfType(ASTAnnotation.class);
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
    public Iterator<ASTType> iterator() {
        return new NodeChildrenIterator<>(this, ASTType.class);
    }
}
