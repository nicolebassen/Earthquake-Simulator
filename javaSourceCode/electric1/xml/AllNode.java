// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AllNode.java

package electric1.xml;

import electric1.util.Node;
import electric1.util.NodeList;

// Referenced classes of package electric1.xml:
//            XPathNode, Parent

public class AllNode extends XPathNode
{

    public AllNode()
    {
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        addNextNodes(((Parent)node).getElements(), nodelist);
    }
}
