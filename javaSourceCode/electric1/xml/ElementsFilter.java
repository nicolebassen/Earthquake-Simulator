// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ElementsFilter.java

package electric1.xml;

import electric1.util.*;

// Referenced classes of package electric1.xml:
//            Elements, Element

final class ElementsFilter extends Elements
{

    ElementsFilter(NodeList nodelist)
    {
        super(nodelist);
    }

    public Node nextNode()
    {
        if(super.current == null)
            return null;
        Node node = super.current.getNode();
        do
            super.current = super.current.next;
        while(super.current != null && !(super.current.getNode() instanceof Element));
        return node;
    }

    public void reset()
    {
        super.reset();
        for(; super.current != null && !(super.current.getNode() instanceof Element); super.current = super.current.next);
    }

    public int size()
    {
        int i = 0;
        for(Node node = super.list.first; node != null; node = node.next)
            if(node.getNode() instanceof Element)
                i++;

        return i;
    }
}
