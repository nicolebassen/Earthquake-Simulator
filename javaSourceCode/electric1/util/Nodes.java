// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Nodes.java

package electric1.util;

import java.util.Enumeration;

// Referenced classes of package electric1.util:
//            NodeList, Node

public class Nodes
    implements Enumeration
{

    public Nodes()
    {
        list = new NodeList();
        reset();
    }

    public Nodes(Node node)
    {
        NodeList nodelist = new NodeList();
        nodelist.append(node);
        list = nodelist;
        reset();
    }

    public Nodes(NodeList nodelist)
    {
        list = nodelist;
        reset();
    }

    public Node currentNode()
    {
        return current != null ? current.getNode() : null;
    }

    public Node firstNode()
    {
        reset();
        return currentNode();
    }

    public NodeList getNodeList()
    {
        return list;
    }

    public boolean hasMoreElements()
    {
        return current != null;
    }

    public Object nextElement()
    {
        return nextNode();
    }

    public Node nextNode()
    {
        if(current == null)
        {
            return null;
        } else
        {
            Node node = current.getNode();
            current = current.next;
            return node;
        }
    }

    public void remove()
    {
        for(; hasMoreElements(); nextNode().remove());
    }

    public void reset()
    {
        current = list.first;
    }

    public int size()
    {
        return list.size();
    }

    protected NodeList list;
    protected Node current;
}
