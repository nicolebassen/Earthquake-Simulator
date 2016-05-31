// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NotationDecl.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Child, Parent

public final class NotationDecl extends Child
{

    NotationDecl(Lex lex, Parent parent)
        throws IOException
    {
        super(parent);
        lex.skip("<!NOTATION".length());
        String s = lex.readToPattern(">", 22);
        content = s.substring(0, s.length() - ">".length());
    }

    public NotationDecl(NotationDecl notationdecl)
    {
        content = notationdecl.content;
    }

    public NotationDecl(String s)
    {
        content = s;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String s)
    {
        content = s;
    }

    public Object clone()
    {
        return new NotationDecl(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<!NOTATION");
        writer.write(content);
        writer.write(">");
    }

    static final String START = "<!NOTATION";
    static final String STOP = ">";
    String content;
}
