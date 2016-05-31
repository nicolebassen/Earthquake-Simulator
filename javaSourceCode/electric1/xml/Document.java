// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Document.java

package electric1.xml;

import electric1.util.*;
import electric1.util.io.*;
import java.io.*;
import java.util.*;

// Referenced classes of package electric1.xml:
//            Parent, ParseException, DocType, Element, 
//            XMLDecl, Comment, Instruction, Child, 
//            NamespaceException

public class Document extends Parent
{

    public Document()
    {
    }

    public Document(byte abyte0[])
        throws ParseException
    {
        this(abyte0, null);
    }

    public Document(byte abyte0[], Hashtable hashtable)
        throws ParseException
    {
        try
        {
            parse(new FastReader(Strings.toString(abyte0)), hashtable);
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            throw new ParseException(unsupportedencodingexception.toString());
        }
    }

    public Document(Document document)
    {
        super(document);
        context = document.context;
    }

    public Document(File file)
        throws ParseException
    {
        try
        {
            parse(new FastBufferedReader(Streams.getReader(file)), null);
        }
        catch(IOException ioexception)
        {
            throw new ParseException(ioexception.toString());
        }
    }

    public Document(InputStream inputstream)
        throws ParseException
    {
        try
        {
            parse(new FastBufferedReader(Streams.getReader(inputstream)), null);
        }
        catch(IOException ioexception)
        {
            throw new ParseException(ioexception.toString());
        }
    }

    public Document(Reader reader)
        throws ParseException
    {
        parse(reader, null);
    }

    public Document(Reader reader, Hashtable hashtable)
        throws ParseException
    {
        parse(reader, hashtable);
    }

    public Document(String s)
        throws ParseException
    {
        parse(new FastReader(s), null);
    }

    public Hashtable getContext()
    {
        return context;
    }

    public void setContext(Hashtable hashtable)
    {
        context = hashtable;
    }

    protected void addNamespacePrefixes(String s, Vector vector, Vector vector1)
    {
        if(context == null)
            return;
        Enumeration enumeration = context.keys();
        do
        {
            if(!enumeration.hasMoreElements())
                break;
            String s1 = (String)enumeration.nextElement();
            if(!vector.contains(s1) && context.get(s1).equals(s))
                vector1.addElement(s1);
        } while(true);
    }

    public Object clone()
    {
        return new Document(this);
    }

    public DocType getDocType()
    {
        for(Node node = super.children.first; node != null; node = node.next)
            if(node instanceof DocType)
                return (DocType)node;

        return null;
    }

    public Document getDocument()
    {
        return this;
    }

    public String getEncoding()
    {
        XMLDecl xmldecl = getXMLDecl();
        return xmldecl == null ? "UTF-8" : xmldecl.getEncoding();
    }

    public String getNamespace(String s)
    {
        return context != null ? (String)context.get(s) : null;
    }

    public Element getRoot()
    {
        for(Node node = super.children.first; node != null; node = node.next)
            if(node instanceof Element)
                return (Element)node;

        return null;
    }

    public boolean getStandalone()
    {
        XMLDecl xmldecl = getXMLDecl();
        return xmldecl == null ? false : xmldecl.getStandalone();
    }

    public String getVersion()
    {
        XMLDecl xmldecl = getXMLDecl();
        return xmldecl == null ? "1.0" : xmldecl.getVersion();
    }

    public XMLDecl getXMLDecl()
    {
        for(Node node = super.children.first; node != null; node = node.next)
            if(node instanceof XMLDecl)
                return (XMLDecl)node;

        return null;
    }

    public Element newRoot()
    {
        return setRoot(new Element());
    }

    void parse(Lex lex, Hashtable hashtable)
        throws IOException, NamespaceException
    {
        context = hashtable;
        int i = 0;
        do
        {
            lex.skipWhitespace();
            lex.mark(2);
            int j = lex.peekRead();
            int k = lex.peekRead();
            lex.reset();
            if(j == -1)
                break;
            if(k == 33 && lex.peekString("<!--"))
                new Comment(lex, this);
            else
            if(k == 33 && lex.peekString("<!DOCTYPE"))
                new DocType(lex, this);
            else
            if(k == 63 && lex.peekString("<?xml "))
                new XMLDecl(lex, this);
            else
            if(k == 63)
            {
                new Instruction(lex, this);
            } else
            {
                new Element(lex, this);
                i++;
            }
        } while(true);
        if(i != 1)
            throw new IOException("the document does not have exactly one root");
        lex.skipWhitespace();
        if(lex.read() != -1)
            throw new IOException("extra stuff at the end");
        else
            return;
    }

    void parse(Reader reader, Hashtable hashtable)
        throws ParseException
    {
        Lex lex = new Lex(reader, "<>=/:", 1);
        try
        {
            parse(lex, hashtable);
        }
        catch(Throwable throwable)
        {
            throw new ParseException(throwable.getMessage() + "\n" + lex.getLocation());
        }
        try
        {
            reader.close();
        }
        catch(IOException ioexception) { }
        break MISSING_BLOCK_LABEL_84;
        Exception exception;
        exception;
        try
        {
            reader.close();
        }
        catch(IOException ioexception1) { }
        throw exception;
    }

    public Element setRoot(Element element)
    {
        Element element1 = getRoot();
        if(element1 != null)
            element1.replaceWith(element);
        else
            addChild(element);
        return element;
    }

    public Element setRoot(String s)
    {
        Element element = newRoot();
        element.setName(s);
        return element;
    }

    public Element setRoot(String s, String s1)
    {
        Element element = newRoot();
        element.setName(s, s1);
        return element;
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        for(Node node = super.children.first; node != null; node = node.next)
        {
            ((Child)node).write(writer, i);
            if(node.next != null)
                writer.write("\r\n");
        }

    }

    public static final int MAJOR_VERSION = 3;
    public static final int MINOR_VERSION = 0;
    public static final XMLDecl DECLARATION = new XMLDecl("1.0", "UTF-8");
    static final String NO_STRINGS[] = new String[0];
    Hashtable context;

}
