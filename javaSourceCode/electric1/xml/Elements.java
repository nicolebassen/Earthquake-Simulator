// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Elements.java

package electric1.xml;

import electric1.util.NodeList;
import electric1.util.Nodes;

// Referenced classes of package electric1.xml:
//            Element

public class Elements extends Nodes
{

    Elements(NodeList nodelist)
    {
        super(nodelist);
    }

    Elements(Element element)
    {
        super(element);
    }

    public Element current()
    {
        return (Element)currentNode();
    }

    public Element first()
    {
        return (Element)firstNode();
    }

    public Element next()
    {
        return (Element)nextNode();
    }
}
