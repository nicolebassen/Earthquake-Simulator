// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XPath.java

package electric1.xml;

import electric1.util.*;
import java.io.IOException;

// Referenced classes of package electric1.xml:
//            RootNode, XPathException, ParentNode, CurrentNode, 
//            WildNode, AllNode, AllAttributesNode, AttributeNode, 
//            AttributePredicate, TextPredicate, IndexPredicate, NameNode, 
//            XPathNode, Attributes, Elements, Element, 
//            Attribute, Parent

public final class XPath
{

    public XPath(String s)
        throws XPathException
    {
        nodes = new NodeList();
        try
        {
            Lex lex = new Lex(s.trim(), "/", 19);
            if(lex.peek() == 47)
            {
                addNode(new RootNode());
                lex.read();
            }
            if(!lex.eof())
            {
                String s1 = lex.readToken();
                addNode(s1);
                for(; !lex.eof(); addNode(s1))
                {
                    if(!s1.equals("/"))
                        lex.readChar(47);
                    s1 = lex.readToken();
                }

            }
        }
        catch(IOException ioexception)
        {
            throw new XPathException(ioexception.toString());
        }
    }

    void addNode(Node node)
    {
        nodes.append(node);
    }

    void addNode(String s)
        throws XPathException
    {
        if(s.equals(".."))
            addNode(((Node) (new ParentNode())));
        else
        if(s.equals("."))
            addNode(((Node) (new CurrentNode())));
        else
        if(s.equals("/"))
            addNode(((Node) (new WildNode())));
        else
        if(s.equals("*"))
            addNode(((Node) (new AllNode())));
        else
        if(s.equals("@*"))
            addNode(((Node) (new AllAttributesNode())));
        else
        if(s.startsWith("@"))
            addNode(((Node) (new AttributeNode(s))));
        else
        if(s.indexOf("[@") != -1)
            addNode(((Node) (new AttributePredicate(s))));
        else
        if(s.indexOf("[text") != -1)
            addNode(((Node) (new TextPredicate(s))));
        else
        if(s.indexOf("[") != -1)
            addNode(((Node) (new IndexPredicate(s))));
        else
            addNode(((Node) (new NameNode(s))));
    }

    Attribute getAttribute(Element element)
    {
        return getAttributes(element).first();
    }

    Attributes getAttributes(Element element)
    {
        NodeList nodelist = new NodeList();
        ((XPathNode)nodes.first).addNodes(element, nodelist);
        return new Attributes(nodelist);
    }

    Element getElement(Parent parent)
    {
        return getElements(parent).first();
    }

    Elements getElements(Parent parent)
    {
        NodeList nodelist = new NodeList();
        ((XPathNode)nodes.first).addNodes(parent, nodelist);
        return new Elements(nodelist);
    }

    NodeList nodes;
}
