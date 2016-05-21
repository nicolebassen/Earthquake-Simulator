// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Element.java

package electric1.xml;

import electric1.util.*;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

// Referenced classes of package electric1.xml:
//            Parent, NamespaceException, Attribute, Attributes, 
//            Text, CData, Comment, Instruction, 
//            Child, XPath

public class Element extends Parent
{

    public Element()
    {
        attributes = new NodeList();
        name = "UNDEFINED";
    }

    Element(Lex lex, Parent parent)
        throws IOException, NamespaceException
    {
        attributes = new NodeList();
        parent.addChild(this);
        lex.read();
        name = lex.readToken();
        if(lex.peek() == 58)
        {
            prefix = name;
            lex.read();
            name = lex.readToken();
        }
        lex.skipWhitespace();
        int i = lex.peek();
        if(i != 62 && i != 47)
            parseAttributes(lex);
        if(prefix == null)
        {
            namespace = getNamespace("");
        } else
        {
            namespace = getNamespace(prefix);
            if(namespace == null)
                throw new NamespaceException("could not find namespace with prefix " + prefix);
        }
        int j = lex.read();
        if(j == 47)
        {
            lex.readChar(62);
        } else
        {
            if(j != 62)
                throw new IOException("expected > or /");
            parseChildren(lex);
            lex.readChar(60);
            lex.readChar(47);
            if(prefix != null)
            {
                String s = lex.readToken();
                if(!prefix.equals(s))
                    throw new IOException("<" + prefix + ":...> does not match </" + s + ":...>");
                lex.readChar(58);
            }
            String s1 = lex.readToken();
            lex.readChar(62);
            if(!name.equals(s1))
                throw new IOException("<..." + name + "> does not match </..." + s1 + ">");
        }
    }

    public Element(Element element)
    {
        super(element);
        attributes = new NodeList();
        prefix = element.prefix;
        namespace = element.namespace;
        name = element.name;
        for(Attribute attribute = (Attribute)element.attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            attributes.append(new Attribute(attribute));

    }

    public Element(String s)
    {
        attributes = new NodeList();
        name = s;
    }

    public Attributes getAttributes()
    {
        return new Attributes(attributes);
    }

    public Attributes getAttributes(XPath xpath)
    {
        return xpath.getAttributes(this);
    }

    public String getName()
    {
        return name;
    }

    public Element setName(String s)
    {
        name = s;
        namespace = getNamespace("");
        return this;
    }

    public Element setName(String s, String s1)
        throws NamespaceException
    {
        prefix = s;
        name = s1;
        if(s == null)
        {
            namespace = getNamespace("");
        } else
        {
            namespace = getNamespace(s);
            if(namespace == null)
                throw new NamespaceException("could not find namespace with prefix " + s);
        }
        return this;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public String getNamespace(String s)
    {
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            if(attribute.isNamespace && attribute.name.equals(s))
            {
                String s1 = attribute.value;
                return s1.length() <= 0 ? null : s1;
            }

        return super.parent != null ? super.parent.getNamespace(s) : null;
    }

    public void setNamespace(String s, String s1)
        throws NamespaceException
    {
        setAttribute(new Attribute(s, s1, true));
    }

    public String getPrefix()
    {
        return prefix;
    }

    protected void addNamespacePrefixes(String s, Vector vector, Vector vector1)
    {
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
        {
            if(!attribute.isNamespace)
                continue;
            if(attribute.value.equals(s) && !vector.contains(attribute.name))
                vector1.addElement(attribute.name);
            vector.addElement(attribute.name);
        }

        if(super.parent != null)
            super.parent.addNamespacePrefixes(s, vector, vector1);
    }

    public Element addText(Text text)
    {
        addChild(text);
        return this;
    }

    public Element addText(String s)
    {
        return addText(new Text(s));
    }

    public Object clone()
    {
        return new Element(this);
    }

    public Attribute getAttribute(XPath xpath)
    {
        return xpath.getAttribute(this);
    }

    public Attribute getAttribute(String s)
    {
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            if(attribute.name.equals(s))
                return attribute;

        return null;
    }

    public Attribute getAttribute(String s, String s1)
    {
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            if(attribute.hasName(s, s1))
                return attribute;

        return null;
    }

    public String getAttributeValue(XPath xpath)
    {
        Attribute attribute = getAttribute(xpath);
        return attribute != null ? attribute.value : null;
    }

    public String getAttributeValue(String s)
    {
        Attribute attribute = getAttribute(s);
        return attribute != null ? attribute.value : null;
    }

    public String getAttributeValue(String s, String s1)
    {
        Attribute attribute = getAttribute(s, s1);
        return attribute != null ? attribute.value : null;
    }

    public synchronized Element getElementWithId(String s)
    {
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            if(attribute.isId && attribute.value.equals(s))
                return this;

        return super.getElementWithId(s);
    }

    public String[] getNamespacePrefixes(String s)
    {
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        addNamespacePrefixes(s, vector, vector1);
        String as[] = new String[vector1.size()];
        vector1.copyInto(as);
        return as;
    }

    public Dictionary getNamespaces()
    {
        Hashtable hashtable = new Hashtable();
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            if(attribute.isNamespace)
                hashtable.put(attribute.name, attribute.value);

        return hashtable;
    }

    public String getQName()
    {
        if(namespace == null)
            return name;
        if(namespace.endsWith(":"))
            return namespace + name;
        else
            return namespace + ":" + name;
    }

    public String getQName(String s)
        throws NamespaceException
    {
        int i = s.indexOf(':');
        String s1 = null;
        if(i != -1)
        {
            String s2 = s.substring(0, i);
            s = s.substring(i + 1);
            s1 = getNamespace(s2);
            if(s1 == null)
                throw new NamespaceException("could not resolve " + s2 + ":" + s);
        } else
        {
            s1 = getNamespace("");
        }
        if(s1 == null)
            return name;
        if(s1.endsWith(":"))
            return s1 + s;
        else
            return s1 + ":" + s;
    }

    public Element getRoot()
    {
        return super.parent != null ? super.parent.getRoot() : this;
    }

    public Text getText()
    {
        for(Node node = super.children.first; node != null; node = node.next)
            if(node instanceof Text)
                return (Text)node;

        return null;
    }

    public Text getText(XPath xpath)
    {
        Element element = getElement(xpath);
        return element != null ? element.getText() : null;
    }

    public Text getText(String s)
    {
        Element element = getElement(s);
        return element != null ? element.getText() : null;
    }

    public Text getText(String s, String s1)
    {
        Element element = getElement(s, s1);
        return element != null ? element.getText() : null;
    }

    public String getTextString()
    {
        Text text = getText();
        return text != null ? text.string : null;
    }

    public String getTextString(XPath xpath)
    {
        Text text = getText(xpath);
        return text != null ? text.string : null;
    }

    public String getTextString(String s)
    {
        Text text = getText(s);
        return text != null ? text.string : null;
    }

    public String getTextString(String s, String s1)
    {
        Text text = getText(s, s1);
        return text != null ? text.string : null;
    }

    public boolean hasAttributes()
    {
        return !attributes.isEmpty();
    }

    public boolean hasName(String s, String s1)
    {
        if(!name.equals(s1))
            return false;
        if(s == null)
            return true;
        if(namespace == null)
            return false;
        else
            return namespace.equals(s);
    }

    public boolean hasText()
    {
        return getText() != null;
    }

    void parseAttributes(Lex lex)
        throws IOException, NamespaceException
    {
        int i = 0;
        do
        {
            attributes.append(new Attribute(lex, this));
            lex.skipWhitespace();
            i = lex.peek();
        } while(i != 62 && i != 47);
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            attribute.resolve(this);

    }

    void parseChildren(Lex lex)
        throws IOException, NamespaceException
    {
        do
        {
            StringBuffer stringbuffer = lex.readWhitespace();
            lex.mark(2);
            int i = lex.peekRead();
            int j = lex.peekRead();
            lex.reset();
            if(i == -1)
                break;
            if(i != 60)
            {
                new Text(lex, stringbuffer, this);
                continue;
            }
            if(j == 47)
            {
                if(super.children.isEmpty() && stringbuffer != null)
                    new Text(lex, stringbuffer, this);
                break;
            }
            if(j == 33 && lex.peekString("<![CDATA["))
                new CData(lex, stringbuffer, this);
            else
            if(j == 33 && lex.peekString("<!--"))
                new Comment(lex, this);
            else
            if(j == 63)
                new Instruction(lex, this);
            else
                new Element(lex, this);
        } while(true);
    }

    public Attribute removeAttribute(XPath xpath)
    {
        Attribute attribute = getAttribute(xpath);
        if(attribute != null)
            attribute.remove();
        return attribute;
    }

    public Attribute removeAttribute(String s)
    {
        Attribute attribute = getAttribute(s);
        if(attribute != null)
            attribute.remove();
        return attribute;
    }

    public Attribute removeAttribute(String s, String s1)
    {
        Attribute attribute = getAttribute(s, s1);
        if(attribute != null)
            attribute.remove();
        return attribute;
    }

    public Attributes removeAttributes(XPath xpath)
    {
        Attributes attributes1 = getAttributes(xpath);
        attributes1.remove();
        attributes1.reset();
        return attributes1;
    }

    public void removeNamespace(String s)
    {
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
            if(attribute.isNamespace && attribute.name.equals(s))
            {
                attribute.remove();
                return;
            }

    }

    public Element setAttribute(Attribute attribute)
        throws NamespaceException
    {
        attribute.resolve(this);
        for(Attribute attribute1 = (Attribute)attributes.first; attribute1 != null; attribute1 = (Attribute)((Node) (attribute1)).next)
            if(attribute1.hasName(attribute.namespace, attribute.name))
            {
                attributes.replace(attribute1, attribute);
                return this;
            }

        attributes.append(attribute);
        return this;
    }

    public Element setAttribute(String s, String s1)
    {
        setAttribute(new Attribute(s, s1));
        return this;
    }

    public Element setAttribute(String s, String s1, String s2)
    {
        setAttribute(new Attribute(s, s1, s2));
        return this;
    }

    public Element setNextSibling(String s)
    {
        Element element = new Element();
        element.setParent(super.parent);
        element.setName(s);
        setNextSibling(element);
        return element;
    }

    public Element setNextSibling(String s, String s1)
    {
        Element element = new Element();
        element.setParent(super.parent);
        element.setName(s, s1);
        setNextSibling(element);
        return element;
    }

    public Element setPreviousSibling(String s)
    {
        Element element = new Element();
        element.setParent(super.parent);
        element.setName(s);
        setPreviousSibling(element);
        return element;
    }

    public Element setPreviousSibling(String s, String s1)
    {
        Element element = new Element();
        element.setParent(super.parent);
        element.setName(s, s1);
        setPreviousSibling(element);
        return element;
    }

    public Element setText(Text text)
    {
        Text text1 = getText();
        if(text == null)
        {
            if(text1 != null)
                text1.remove();
        } else
        if(text1 == null)
            addChild(text);
        else
            text1.replaceWith(text);
        return this;
    }

    public Element setText(String s)
    {
        return setText(s != null ? new Text(s) : null);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write(60);
        if(prefix != null)
        {
            writer.write(prefix);
            writer.write(58);
        }
        writer.write(name);
        for(Attribute attribute = (Attribute)attributes.first; attribute != null; attribute = (Attribute)((Node) (attribute)).next)
        {
            writer.write(32);
            attribute.write(writer, i);
        }

        if(super.children.isEmpty())
        {
            writer.write(47);
        } else
        {
            writer.write(62);
            writeChildren(writer, i);
            writer.write("</");
            if(prefix != null)
            {
                writer.write(prefix);
                writer.write(58);
            }
            writer.write(name);
        }
        writer.write(62);
    }

    void writeChildren(Writer writer, int i)
        throws IOException
    {
        if(super.children.first == super.children.last && (super.children.first instanceof Text))
            ((Text)super.children.first).write(writer, -1);
        else
        if(i == -1)
        {
            for(Node node = super.children.first; node != null; node = node.next)
                ((Child)node).write(writer, i);

        } else
        {
            int j = i + 2;
            for(Node node1 = super.children.first; node1 != null; node1 = node1.next)
            {
                writer.write("\r\n");
                ((Child)node1).write(writer, j);
            }

            writer.write("\r\n");
            indent(writer, i);
        }
    }

    String prefix;
    String namespace;
    String name;
    NodeList attributes;
}
