// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AttributeNode.java

package electric1.xml;

import electric1.util.Node;
import electric1.util.NodeList;

// Referenced classes of package electric1.xml:
//            NameNode, Element, XPathNode

public class AttributeNode extends NameNode
{

    public AttributeNode(String s)
    {
        setName(s.substring(1));
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        addNextNodes(((Element)node).getAttribute(super.namespace, super.name), nodelist);
    }
}
