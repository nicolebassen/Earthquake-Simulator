// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IndexPredicate.java

package electric1.xml;

import electric1.util.*;

// Referenced classes of package electric1.xml:
//            NameNode, XPathException, Parent, XPathNode, 
//            Elements, Element

public class IndexPredicate extends NameNode
{

    public IndexPredicate(String s)
        throws XPathException
    {
        int i = s.indexOf('[');
        if(i > 0)
            setName(s.substring(0, i));
        int j = s.indexOf(']');
        if(j == -1)
        {
            throw new XPathException("missing ] in " + s);
        } else
        {
            index = Integer.parseInt(s.substring(i + 1, j));
            return;
        }
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        if(super.name == null)
        {
            addNextNodes(((Parent)node).getElementAt(index), nodelist);
            return;
        }
        int i = 0;
        for(Elements elements = ((Parent)node).getElements(); elements.hasMoreElements();)
        {
            Element element = elements.next();
            if(element.hasName(super.namespace, super.name) && ++i == index)
            {
                addNextNodes(element, nodelist);
                return;
            }
        }

        throw new IndexOutOfBoundsException(index + " is an invalid index");
    }

    int index;
}
