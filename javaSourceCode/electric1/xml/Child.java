// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Child.java

package electric1.xml;

import electric1.util.Node;
import electric1.util.Strings;
import electric1.util.io.Streams;
import java.io.*;

// Referenced classes of package electric1.xml:
//            Parent, Document, Element

public abstract class Child extends Node
    implements Cloneable
{

    Child()
    {
    }

    Child(Parent parent1)
    {
        parent1.addChild(this);
    }

    public Parent getParent()
    {
        return parent;
    }

    void setParent(Parent parent1)
    {
        parent = parent1;
    }

    public abstract Object clone();

    public byte[] getBytes()
        throws UnsupportedEncodingException
    {
        String s = Strings.normalizeEncoding(getDocument().getEncoding());
        return toString(-1).getBytes(s);
    }

    public Document getDocument()
    {
        return parent != null ? parent.getDocument() : null;
    }

    public Child getNextSibling()
    {
        return (Child)getNextSiblingNode();
    }

    public Child getPreviousSibling()
    {
        return (Child)getPreviousSiblingNode();
    }

    public Element getRoot()
    {
        return parent != null ? parent.getRoot() : null;
    }

    void indent(Writer writer, int i)
        throws IOException
    {
        for(int j = 0; j < i; j++)
            writer.write(32);

    }

    public void replaceWith(Child child)
    {
        parent.replaceChild(this, child);
    }

    public Child setNextSibling(Child child)
    {
        child.setParent(parent);
        setNextSiblingNode(child);
        return this;
    }

    public Child setPreviousSibling(Child child)
    {
        child.setParent(parent);
        setPreviousSiblingNode(child);
        return this;
    }

    public String toString()
    {
        return toString(0);
    }

    public String toString(int i)
    {
        StringWriter stringwriter = new StringWriter();
        try
        {
            write(stringwriter, i);
        }
        catch(IOException ioexception) { }
        return stringwriter.toString();
    }

    public void write(File file)
        throws IOException
    {
        BufferedWriter bufferedwriter = new BufferedWriter(Streams.getWriter(file, getDocument().getEncoding()));
        write(((Writer) (bufferedwriter)), 0);
        bufferedwriter.close();
        break MISSING_BLOCK_LABEL_39;
        Exception exception;
        exception;
        bufferedwriter.close();
        throw exception;
    }

    public void write(OutputStream outputstream)
        throws IOException
    {
        Writer writer = Streams.getWriter(outputstream, getDocument().getEncoding());
        write(writer, 0);
        writer.flush();
    }

    public void write(Writer writer)
        throws IOException
    {
        write(writer, 0);
    }

    public abstract void write(Writer writer, int i)
        throws IOException;

    Parent parent;
}
