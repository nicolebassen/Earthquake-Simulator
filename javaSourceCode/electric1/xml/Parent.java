// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Parent.java

package electric1.xml;

import electric1.util.*;
import java.util.Vector;

// Referenced classes of package electric1.xml:
//            Child, Children, Element, ElementsFilter, 
//            Selection, Elements, XPath

public abstract class Parent extends Child
{

    public Parent()
    {
        children = new NodeList();
    }

    public Parent(Parent parent)
    {
        children = new NodeList();
        for(Node node = parent.children.first; node != null; node = node.next)
            addChild((Child)((Child)node).clone());

    }

    public Children getChildren()
    {
        return new Children(children);
    }

    public Child addChild(Child child)
    {
        child.setParent(this);
        children.append(child);
        return child;
    }

    public Element addElement()
    {
        return addElement(new Element());
    }

    public Element addElement(Element element)
    {
        addChild(element);
        return element;
    }

    public Element addElement(String s)
    {
        return addElement().setName(s);
    }

    public Element addElement(String s, String s1)
    {
        return addElement().setName(s, s1);
    }

    protected void addNamespacePrefixes(String s, Vector vector, Vector vector1)
    {
    }

    public Element getElement(XPath xpath)
    {
        return xpath.getElement(this);
    }

    public Element getElement(String s)
    {
        for(Node node = children.first; node != null; node = node.next)
            if((node instanceof Element) && ((Element)node).name.equals(s))
                return (Element)node;

        return null;
    }

    public Element getElement(String s, String s1)
    {
        for(Node node = children.first; node != null; node = node.next)
            if((node instanceof Element) && ((Element)node).hasName(s, s1))
                return (Element)node;

        return null;
    }

    public Element getElementAt(int i)
        throws IndexOutOfBoundsException
    {
        int j = 0;
        for(Node node = children.first; node != null; node = node.next)
            if((node instanceof Element) && ++j == i)
                return (Element)node;

        throw new IndexOutOfBoundsException(i + " is an invalid index");
    }

    public Elements getElements()
    {
        return new ElementsFilter(children);
    }

    public Elements getElements(XPath xpath)
    {
        return xpath.getElements(this);
    }

    public Elements getElements(String s)
    {
        NodeList nodelist = new NodeList();
        for(Node node = children.first; node != null; node = node.next)
            if((node instanceof Element) && ((Element)node).name.equals(s))
                nodelist.append(new Selection(node));

        return new Elements(nodelist);
    }

    public Elements getElements(String s, String s1)
    {
        NodeList nodelist = new NodeList();
        for(Node node = children.first; node != null; node = node.next)
            if((node instanceof Element) && ((Element)node).hasName(s, s1))
                nodelist.append(new Selection(node));

        return new Elements(nodelist);
    }

    public synchronized Element getElementWithId(String s)
    {
        for(Node node = children.first; node != null; node = node.next)
        {
            if(!(node instanceof Element))
                continue;
            Element element = ((Element)node).getElementWithId(s);
            if(element != null)
                return element;
        }

        return null;
    }

    public String getNamespace(String s)
    {
        return null;
    }

    public boolean hasChildren()
    {
        return !children.isEmpty();
    }

    public boolean hasElements()
    {
        return getElements().current() != null;
    }

    public Element removeElement(XPath xpath)
    {
        Element element = getElement(xpath);
        if(element != null)
            element.remove();
        return element;
    }

    public Element removeElement(String s)
    {
        Element element = getElement(s);
        if(element != null)
            element.remove();
        return element;
    }

    public Element removeElement(String s, String s1)
    {
        Element element = getElement(s, s1);
        if(element != null)
            element.remove();
        return element;
    }

    public Element removeElementAt(int i)
    {
        Element element = getElementAt(i);
        element.remove();
        return element;
    }

    public Elements removeElements(XPath xpath)
    {
        Elements elements = getElements(xpath);
        elements.remove();
        elements.reset();
        return elements;
    }

    public Elements removeElements(String s)
    {
        Elements elements = getElements(s);
        elements.remove();
        elements.reset();
        return elements;
    }

    public Elements removeElements(String s, String s1)
    {
        Elements elements = getElements(s, s1);
        elements.remove();
        elements.reset();
        return elements;
    }

    void replaceChild(Child child, Child child1)
    {
        children.replace(child, child1);
        child1.parent = this;
    }

    public Element setElementAt(int i, Element element)
        throws IndexOutOfBoundsException
    {
        Element element1 = getElementAt(i);
        element1.replaceWith(element);
        return element1;
    }

    static final String EOL = "\r\n";
    NodeList children;
}
