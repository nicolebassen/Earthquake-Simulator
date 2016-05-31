// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CurrentNode.java

package electric1.xml;

import electric1.util.Node;
import electric1.util.NodeList;

// Referenced classes of package electric1.xml:
//            XPathNode

public class CurrentNode extends XPathNode
{

    public CurrentNode()
    {
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        addNextNodes(node, nodelist);
    }
}
