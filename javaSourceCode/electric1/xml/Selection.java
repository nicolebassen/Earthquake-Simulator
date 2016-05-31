// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Selection.java

package electric1.xml;

import electric1.util.Node;

final class Selection extends Node
{

    Selection(Node node1)
    {
        node = node1;
    }

    Selection(Selection selection)
    {
        node = selection.node;
    }

    public Node getNode()
    {
        return node;
    }

    public Object clone()
    {
        return new Selection(this);
    }

    Node node;
}
