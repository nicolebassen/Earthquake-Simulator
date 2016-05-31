// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Node.java

package electric1.util;

import java.io.Serializable;

// Referenced classes of package electric1.util:
//            NodeList

public class Node
    implements Serializable
{

    public Node()
    {
    }

    public Node getNextSiblingNode()
    {
        return next != null ? next.getNode() : null;
    }

    public Node getNode()
    {
        return this;
    }

    public Node getPreviousSiblingNode()
    {
        return prev != null ? prev.getNode() : prev;
    }

    public boolean remove()
    {
        if(list != null)
        {
            list.remove(this);
            return true;
        } else
        {
            return false;
        }
    }

    public void setNextSiblingNode(Node node)
    {
        list.addSiblingNode(this, node);
    }

    public void setPreviousSiblingNode(Node node)
    {
        list.insertSiblingNode(this, node);
    }

    public NodeList list;
    public Node prev;
    public Node next;
}
