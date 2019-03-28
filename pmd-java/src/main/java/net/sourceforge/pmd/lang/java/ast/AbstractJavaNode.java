/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import org.apache.commons.lang3.ArrayUtils;

import net.sourceforge.pmd.lang.ast.AbstractNode;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.symboltable.Scope;


public abstract class AbstractJavaNode extends AbstractNode implements JavaNode {

    protected JavaParser parser;
    private Scope scope;
    private Comment comment;


    public AbstractJavaNode(int id) {
        super(id);
    }


    public AbstractJavaNode(JavaParser parser, int id) {
        super(id);
        this.parser = parser;
    }


    @Override
    public void jjtOpen() {
        if (beginLine == -1 && parser.token.next != null) {
            beginLine = parser.token.next.beginLine;
            beginColumn = parser.token.next.beginColumn;
        }
    }


    @Override
    public void jjtClose() {
        if (beginLine == -1 && children.length == 0) {
            beginColumn = parser.token.beginColumn;
        }
        if (beginLine == -1) {
            beginLine = parser.token.beginLine;
        }
        if (this instanceof LeftRecursiveNode) {
            enlargeLeft();
        }
        endLine = parser.token.endLine;
        endColumn = parser.token.endColumn;
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


    /**
     * Accept the visitor. *
     */
    @Override
    public Object childrenAccept(JavaParserVisitor visitor, Object data) {
        for (Node child : children) {
            ((JavaNode) child).jjtAccept(visitor, data);
        }

        return data;
    }


    @Override
    public <T> void childrenAccept(SideEffectingVisitor<T> visitor, T data) {
        for (Node child : children) {
            ((JavaNode) child).jjtAccept(visitor, data);
        }

    }


    @Override
    public Scope getScope() {
        if (scope == null) {
            return ((JavaNode) parent).getScope();
        }
        return scope;
    }


    @Override
    public void setScope(Scope scope) {
        this.scope = scope;
    }


    public void comment(Comment theComment) {
        comment = theComment;
    }


    public Comment comment() {
        return comment;
    }


    void flatten(int idx) {

        AbstractJavaNode child = (AbstractJavaNode) jjtGetChild(idx);
        children = ArrayUtils.remove(children, idx);
        child.jjtSetParent(null);
        child.jjtSetChildIndex(-1);

        if (child.jjtGetNumChildren() > 0) {
            children = ArrayUtils.insert(idx, children, child.children);
        }
        updateChildrenIndices(idx);
    }

    /**
     * Inserts a child at the given index, shifting other children without overwriting
     * them. The implementation of jjtAddChild in AbstractNode overwrites nodes, and
     * doesn't shrink or grow the initial array. That's probably unexpected and this should
     * be the standard implementation.
     *
     * The text bounds of this node are enlarged to contain the new child if need be.
     * Text bounds of the child should hence have been set before calling this method.
     * The designer relies on this invariant to perform the overlay (in UniformStyleCollection).
     *
     * @param child Child to add
     * @param index Index the child should have in the parent
     */
    // visible to parser only
    void insertChild(AbstractJavaNode child, int index, boolean overwrite) {
        // Allow to insert a child without overwriting
        // If the child is null, it is replaced. If it is not null, children are shifted
        if (children == null) {
            assert index == 0;
            children = new Node[] {child};
        } else if (index <= children.length) {
            if (overwrite || children[index] == null) {
                children[index] = child;
            } else {
                children = ArrayUtils.insert(index, children, child);
            }
        }
        child.jjtSetChildIndex(index);
        child.jjtSetParent(this);

        updateChildrenIndices(index);

        // The text coordinates of this node will be enlarged with those of the child

        enlargeOnInsert(index, child);
    }

    private void enlargeOnInsert(int index, AbstractJavaNode child) {
        if (index == 0) {
            enlargeLeft(child);
        }
        if (index == children.length - 1) {
            enlargeRight(child);
        }
    }

    private void enlargeLeft() {
        if (jjtGetNumChildren() > 0) {
            enlargeLeft((AbstractJavaNode) jjtGetChild(0));
        }
    }

    private void enlargeLeft(AbstractJavaNode child) {
        if (this.beginLine > child.beginLine) {
            this.firstToken = child.firstToken;
            this.beginLine = child.beginLine;
            this.beginColumn = child.beginColumn;
        } else if (this.beginLine == child.beginLine
            && this.beginColumn > child.beginColumn) {
            this.firstToken = child.firstToken;
            this.beginColumn = child.beginColumn;
        }
    }

    private void enlargeRight(AbstractJavaNode child) {
        if (this.endLine < child.endLine) {
            this.lastToken = child.lastToken;
            this.endLine = child.endLine;
            this.endColumn = child.endColumn;
        } else if (this.endLine == child.endLine
            && this.endColumn < child.endColumn) {
            this.lastToken = child.lastToken;
            this.endColumn = child.endColumn;
        }
    }

    /**
     * Updates the {@link #jjtGetChildIndex()} of the children with their
     * real position, starting at [startIndex].
     */
    private void updateChildrenIndices(int startIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int j = startIndex; j < jjtGetNumChildren(); j++) {
            children[j].jjtSetChildIndex(j); // shift the children to the right
            children[j].jjtSetParent(this);
        }
    }


    /**
     * Shifts the begin and end columns of this node by the given offsets.
     */
    void shiftColumns(int beginShift, int endShift) {
        this.beginColumn += beginShift;
        this.endColumn += endShift;

        // TODO change the tokens. We need to link index
        //  the tokens probably...
    }

    void copyTextCoordinates(AbstractJavaNode copy) {
        this.beginLine = copy.getBeginLine();
        this.beginColumn = copy.getBeginColumn();
        this.endLine = copy.getEndLine();
        this.endColumn = copy.getEndColumn();
        this.firstToken = copy.jjtGetFirstToken();
        this.lastToken = copy.jjtGetLastToken();
    }


    // assumes that the child has the same text bounds
    // as the old one. Used to replace an ambiguous name
    // with an unambiguous representation
    void replaceChildAt(int idx, AbstractJavaNode newChild) {

        // parent of the old child must not be reset to null
        // as chances are we're reusing it as a child of the
        // new child

        newChild.jjtSetParent(this);
        newChild.jjtSetChildIndex(idx);

        children[idx] = newChild;
    }


    @Override
    public final String getXPathNodeName() {
        return JavaParserTreeConstants.jjtNodeName[id];
    }
}
