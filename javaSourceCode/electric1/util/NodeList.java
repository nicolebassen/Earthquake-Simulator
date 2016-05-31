// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NodeList.java

package electric1.util;

import java.io.Serializable;

// Referenced classes of package electric1.util:
//            Node

public final class NodeList
    implements Serializable
{

    public NodeList()
    {
    }

    public void addSiblingNode(Node node, Node node1)
    {
        node1.remove();
        node1.list = this;
        node1.next = node.next;
        node1.prev = node;
        if(node.next == null)
            last = node1;
        else
            node.next.prev = node1;
        node.next = node1;
    }

    public void append(Node node)
    {
        node.remove();
        node.list = this;
        node.prev = last;
        if(first == null)
            first = node;
        else
            last.next = node;
        last = node;
    }

    public void insertSiblingNode(Node node, Node node1)
    {
        node1.remove();
        node1.list = this;
        node1.prev = node.prev;
        node1.next = node;
        if(node.prev == null)
            first = node1;
        else
            node.prev.next = node1;
        node.prev = node1;
    }

    public boolean isEmpty()
    {
        return first == null;
    }

    public void remove(Node node)
    {
        if(node.prev == null)
            first = node.next;
        else
            node.prev.next = node.next;
        if(node.next == null)
            last = node.prev;
        else
            node.next.prev = node.prev;
    }

    public void replace(Node node, Node node1)
    {
        node1.remove();
        node1.list = this;
        if(node.prev == null)
            first = node1;
        else
            node.prev.next = node1;
        if(node.next == null)
            last = node1;
        else
            node.next.prev = node1;
        node1.prev = node.prev;
        node1.next = node.next;
    }

    public int size()
    {
        int i = 0;
        for(Node node = first; node != null; node = node.next)
            i++;

        return i;
    }

    public Node first;
    public Node last;
}
