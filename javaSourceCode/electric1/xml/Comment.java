// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Comment.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Child, Parent

public final class Comment extends Child
{

    Comment(Lex lex, Parent parent)
        throws IOException
    {
        super(parent);
        lex.skip("<!--".length());
        string = lex.readToPattern("-->", 2);
    }

    public Comment(Comment comment)
    {
        string = comment.string;
    }

    public Comment(String s)
    {
        string = s;
    }

    public String getString()
    {
        return string;
    }

    public void setString(String s)
    {
        string = s;
    }

    public Object clone()
    {
        return new Comment(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<!--");
        writer.write(string);
        writer.write("-->");
    }

    static final String START = "<!--";
    static final String STOP = "-->";
    String string;
}
