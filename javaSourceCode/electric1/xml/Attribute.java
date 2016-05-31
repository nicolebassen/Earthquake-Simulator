// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Attribute.java

package electric1.xml;

import electric1.util.*;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            NamespaceException, Element, Text

public final class Attribute extends Node
{

    Attribute(Lex lex, Element element1)
        throws IOException
    {
        name = lex.readToken();
        if(lex.peek() == 58)
        {
            prefix = name;
            lex.read();
            name = lex.readToken();
        }
        lex.readChar(61);
        lex.skipWhitespace();
        int i = lex.read();
        if(i == 34)
            value = lex.readToPattern("\"", 66);
        else
        if(i == 39)
            value = lex.readToPattern("'", 66);
        else
            throw new IOException("missing quote at start of attribute");
        initialize();
    }

    public Attribute(Attribute attribute)
    {
        prefix = attribute.prefix;
        namespace = attribute.namespace;
        name = attribute.name;
        value = attribute.value;
        isNamespace = attribute.isNamespace;
        raw = attribute.raw;
    }

    public Attribute(String s, String s1)
    {
        name = s;
        value = s1;
        initialize();
    }

    public Attribute(String s, String s1, String s2)
    {
        prefix = s;
        name = s1;
        value = s2;
        initialize();
    }

    Attribute(String s, String s1, boolean flag)
    {
        name = s;
        value = s1;
        isNamespace = flag;
    }

    public Element getElement()
    {
        return element;
    }

    public String getName()
    {
        return name;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public boolean isNamespace()
    {
        return isNamespace;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public boolean getRaw()
    {
        return raw;
    }

    public void setRaw(boolean flag)
    {
        raw = flag;
    }

    public String getValue()
    {
        return value;
    }

    public Object clone()
    {
        return new Attribute(this);
    }

    public boolean equals(Object obj)
    {
        return (obj instanceof Attribute) && ((Attribute)obj).name.equals(name) && ((Attribute)obj).value.equals(value) && ArrayUtil.equals(((Attribute)obj).namespace, namespace);
    }

    public String getQName()
    {
        return namespace != null ? namespace + ":" + name : name;
    }

    public int hashCode()
    {
        return name.hashCode() + value.hashCode();
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

    void initialize()
    {
        if("xmlns".equals(name))
        {
            isNamespace = true;
            name = "";
        } else
        if("xmlns".equals(prefix))
        {
            isNamespace = true;
            prefix = null;
        } else
        if("id".equals(name))
            isId = true;
    }

    void resolve(Element element1)
        throws NamespaceException
    {
        element = element1;
        if(prefix != null && !prefix.equals("xml"))
        {
            namespace = element1.getNamespace(prefix);
            if(namespace == null)
                throw new NamespaceException("could not find namespace with prefix " + prefix);
        }
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        if(isNamespace)
        {
            stringbuffer.append("xmlns");
            if(name.length() > 0)
                stringbuffer.append(':').append(name);
        } else
        {
            if(prefix != null)
                stringbuffer.append(prefix).append(':');
            stringbuffer.append(name);
        }
        stringbuffer.append("='").append(value).append('\'');
        return stringbuffer.toString();
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        if(isNamespace)
        {
            writer.write("xmlns");
            if(name.length() > 0)
            {
                writer.write(58);
                writer.write(name);
            }
        } else
        {
            if(prefix != null)
            {
                writer.write(prefix);
                writer.write(58);
            }
            writer.write(name);
        }
        writer.write("='");
        if(value == null)
            writer.write("null");
        else
        if(raw)
            writer.write(value);
        else
            Text.writeWithSubstitution(writer, value);
        writer.write(39);
    }

    Element element;
    String prefix;
    String namespace;
    String name;
    String value;
    boolean isNamespace;
    boolean isId;
    boolean raw;
}
