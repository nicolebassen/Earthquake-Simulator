// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XPathNode.java

package electric1.xml;

import electric1.util.*;

// Referenced classes of package electric1.xml:
//            Selection

public abstract class XPathNode extends Node
{

    public XPathNode()
    {
    }

    void addNextNodes(Node node, NodeList nodelist)
    {
        if(node == null)
            return;
        XPathNode xpathnode = next();
        if(xpathnode == null)
            nodelist.append(new Selection(node));
        else
            xpathnode.addNodes(node, nodelist);
    }

    void addNextNodes(Nodes nodes, NodeList nodelist)
    {
        XPathNode xpathnode = next();
        if(xpathnode == null)
            for(; nodes.hasMoreElements(); nodelist.append(new Selection(nodes.nextNode())));
        else
            for(; nodes.hasMoreElements(); xpathnode.addNodes(nodes.nextNode(), nodelist));
    }

    public abstract void addNodes(Node node, NodeList nodelist);

    XPathNode next()
    {
        return (XPathNode)getNextSiblingNode();
    }
}
