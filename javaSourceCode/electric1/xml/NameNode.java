// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NameNode.java

package electric1.xml;

import electric1.util.Node;
import electric1.util.NodeList;

// Referenced classes of package electric1.xml:
//            XPathNode, XPathException, Parent

public class NameNode extends XPathNode
{

    public NameNode()
    {
    }

    public NameNode(String s)
        throws XPathException
    {
        setName(s);
    }

    public void setName(String s)
        throws XPathException
    {
        if(s.startsWith("'"))
        {
            if(!s.endsWith("'"))
                throw new XPathException("missing quote in name");
            s = s.substring(1, s.length() - 1);
        }
        int i = s.lastIndexOf(':');
        if(i != -1)
        {
            namespace = s.substring(0, i);
            name = s.substring(i + 1);
        } else
        {
            name = s;
        }
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        addNextNodes(((Parent)node).getElements(namespace, name), nodelist);
    }

    String namespace;
    String name;
}
