// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Children.java

package electric1.xml;

import electric1.util.NodeList;
import electric1.util.Nodes;

// Referenced classes of package electric1.xml:
//            Child

public final class Children extends Nodes
{

    Children(NodeList nodelist)
    {
        super(nodelist);
    }

    public Child current()
    {
        return (Child)currentNode();
    }

    public Child first()
    {
        return (Child)firstNode();
    }

    public Child next()
    {
        return (Child)nextNode();
    }
}
