// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WildNode.java

package electric1.xml;

import electric1.util.*;

// Referenced classes of package electric1.xml:
//            XPathNode, XPathException, Parent, Elements

public class WildNode extends XPathNode
{

    public WildNode()
    {
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        XPathNode xpathnode = next();
        if(xpathnode == null)
            throw new XPathException("// cannot be at the end of an XPath expression");
        xpathnode.addNodes(node, nodelist);
        for(Elements elements = ((Parent)node).getElements(); elements.hasMoreElements(); addNodes(((Node) (elements.next())), nodelist));
    }
}
