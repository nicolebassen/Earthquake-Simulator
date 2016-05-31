// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextPredicate.java

package electric1.xml;

import electric1.util.*;
import java.io.IOException;

// Referenced classes of package electric1.xml:
//            NameNode, Attribute, XPathException, Parent, 
//            Selection, Elements, Element, XPathNode

public class TextPredicate extends NameNode
{

    public TextPredicate(String s)
        throws XPathException
    {
        try
        {
            int i = s.indexOf('[');
            if(i > 0)
                setName(s.substring(0, i));
            Lex lex = new Lex(s.substring(i + 1), "=", 1);
            Attribute attribute = new Attribute(lex, null);
            lex.readChar(93);
            text = attribute.getValue();
        }
        catch(IOException ioexception)
        {
            throw new XPathException(ioexception.toString());
        }
    }

    public void addNodes(Node node, NodeList nodelist)
    {
        NodeList nodelist1 = new NodeList();
        Elements elements = ((Parent)node).getElements();
        do
        {
            if(!elements.hasMoreElements())
                break;
            Element element = elements.next();
            String s = element.getTextString();
            if(s != null && text.equals(s.trim()) && (super.name == null || element.hasName(super.namespace, super.name)))
                nodelist1.append(new Selection(element));
        } while(true);
        addNextNodes(new Nodes(nodelist1), nodelist);
    }

    String text;
}
