// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AttlistDecl.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Child, Parent

public final class AttlistDecl extends Child
{

    AttlistDecl(Lex lex, Parent parent)
        throws IOException
    {
        super(parent);
        lex.skip("<!ATTLIST".length());
        String s = lex.readToPattern(">", 22);
        content = s.substring(0, s.length() - ">".length());
    }

    public AttlistDecl(AttlistDecl attlistdecl)
    {
        content = attlistdecl.content;
    }

    public AttlistDecl(String s)
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
        return new AttlistDecl(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<!ATTLIST");
        writer.write(content);
        writer.write(">");
    }

    static final String START = "<!ATTLIST";
    static final String STOP = ">";
    String content;
}
