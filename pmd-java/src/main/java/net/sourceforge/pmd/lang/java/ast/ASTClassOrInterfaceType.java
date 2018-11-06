/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTClassOrInterfaceType.java */

package net.sourceforge.pmd.lang.java.ast;

import java.util.Optional;

// @formatter:off
/**
 * Represents a class or interface type, possibly parameterised with type arguments.
 * This node comes in two productions (see below):
 *
 * <p>The first is a left-recursive variant, allowing to parse references to type members
 * unambiguously. The resulting node's {@linkplain #getLeftHandSide() left-hand-side type}
 * addresses the type parent of the type. The position of type arguments and annotations are
 * preserved.
 *
 * <p>Parsing types left-recursively has a caveat though: fully-qualified type names. The
 * parser can't disambiguate between a reference to a type member (e.g. {@code Map.Entry}, where
 * both segments refer to a type, and which would ideally be parsed left-recursively), and a
 * qualified type name (e.g. {@code java.util.List}, where the full sequence refers to a unique
 * type, but individual segments don't).
 *
 * <p>We could remove that with a later AST visit, like type resolution though!
 *
 * <pre>
 *
 * ClassOrInterfaceType ::= ClassOrInterfaceType ( "." {@linkplain ASTAnnotation Annotation}* &lt;IDENTIFIER&gt; {@linkplain ASTTypeArguments TypeArguments}? )+
 *                        | &lt;IDENTIFIER&gt; ( "." &lt;IDENTIFIER&gt; ) *  {@linkplain ASTTypeArguments TypeArguments}?
 *
 * </pre>
 */
// @formatter:on
public class ASTClassOrInterfaceType extends AbstractJavaTypeNode implements ASTReferenceType {
    public ASTClassOrInterfaceType(String identifier) {
        super(JavaParserTreeConstants.JJTCLASSORINTERFACETYPE);
        setImage(identifier);
    }


    ASTClassOrInterfaceType(int id) {
        super(id);
    }


    ASTClassOrInterfaceType(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Gets the left-hand side type of this type. This is a type we know for sure
     * that this type is a member of.
     *
     * @return A type, or null if this is a base type
     */
    public Optional<ASTClassOrInterfaceType> getLeftHandSide() {
        return Optional.ofNullable(getFirstChildOfType(ASTClassOrInterfaceType.class));
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
     * Checks whether the type this node is referring to is declared within the
     * same compilation unit - either a class/interface or a enum type. You want
     * to check this, if {@link #getType()} is null.
     *
     * @return {@code true} if this node referencing a type in the same
     * compilation unit, {@code false} otherwise.
     */
    public boolean isReferenceToClassSameCompilationUnit() {
        ASTCompilationUnit root = getFirstParentOfType(ASTCompilationUnit.class);
        for (ASTClassOrInterfaceDeclaration c : root.findDescendantsOfType(ASTClassOrInterfaceDeclaration.class, true)) {
            if (c.hasImageEqualTo(getImage())) {
                return true;
            }
        }
        for (ASTEnumDeclaration e : root.findDescendantsOfType(ASTEnumDeclaration.class, true)) {
            if (e.hasImageEqualTo(getImage())) {
                return true;
            }
        }
        return false;
    }


    public boolean isAnonymousClass() {
        return jjtGetParent().getFirstChildOfType(ASTClassOrInterfaceBody.class) != null;
    }

}
