// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Attributes.java

package electric1.xml;

import electric1.util.NodeList;
import electric1.util.Nodes;

// Referenced classes of package electric1.xml:
//            Attribute

public final class Attributes extends Nodes
{

    Attributes(NodeList nodelist)
    {
        super(nodelist);
    }

    public Attribute current()
    {
        return (Attribute)currentNode();
    }

    public Attribute first()
    {
        return (Attribute)firstNode();
    }

    public Attribute next()
    {
        return (Attribute)nextNode();
    }
}
