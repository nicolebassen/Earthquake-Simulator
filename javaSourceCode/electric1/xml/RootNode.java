// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RootNode.java

package electric1.xml;

import electric1.util.Node;
import electric1.util.NodeList;

// Referenced classes of package electric1.xml:
//            XPathNode, Child

public class RootNode extends XPathNode
{

    public RootNode()
    {
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        addNextNodes(((Child)node).getDocument(), nodelist);
    }
}
