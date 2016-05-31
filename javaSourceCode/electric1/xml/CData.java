// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CData.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Text, Child, Parent

public final class CData extends Text
{

    CData(Lex lex, StringBuffer stringbuffer, Parent parent)
        throws IOException
    {
        super(parent);
        lex.skip("<![CDATA[".length());
        String s = lex.readToPattern("]]>", 6);
        super.string = s.substring(0, s.length() - "]]>".length());
    }

    public CData(CData cdata)
    {
        super(cdata);
    }

    public CData(String s)
    {
        super(s);
    }

    public Object clone()
    {
        return new CData(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<![CDATA[");
        writer.write(super.string);
        writer.write("]]>");
    }

    static final String START = "<![CDATA[";
    static final String STOP = "]]>";
}
