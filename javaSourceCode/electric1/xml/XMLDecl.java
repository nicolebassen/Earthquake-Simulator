// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLDecl.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Child, Attribute, Parent

public final class XMLDecl extends Child
{

    XMLDecl(Lex lex, Parent parent)
        throws IOException
    {
        super(parent);
        version = "1.0";
        encoding = "UTF-8";
        standalone = false;
        lex.skip("<?xml ".length());
        content = lex.readToPattern("?>", 3);
        Lex lex1 = new Lex(content, "=", 1);
        do
        {
            if(lex1.eof())
                break;
            Attribute attribute = new Attribute(lex1, null);
            String s = attribute.getName();
            String s1 = attribute.getValue();
            if(s.equals("version"))
                version = s1;
            else
            if(s.equals("encoding"))
                encoding = s1;
            else
            if(s.equals("standalone"))
                standalone = s1.equals("yes");
        } while(true);
    }

    public XMLDecl(XMLDecl xmldecl)
    {
        version = "1.0";
        encoding = "UTF-8";
        standalone = false;
        content = xmldecl.content;
        version = xmldecl.version;
        encoding = xmldecl.encoding;
        standalone = xmldecl.standalone;
    }

    public XMLDecl(String s, String s1)
    {
        version = "1.0";
        encoding = "UTF-8";
        standalone = false;
        content = "version='" + s + "' encoding='" + s1 + "'";
        version = s;
        encoding = s1;
    }

    public XMLDecl(String s, String s1, boolean flag)
    {
        version = "1.0";
        encoding = "UTF-8";
        standalone = false;
        content = "version='" + s + "' encoding='" + s1 + "' standalone='" + (flag ? "yes" : "no") + "'";
        version = s;
        encoding = s1;
        standalone = flag;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String s)
    {
        encoding = s;
    }

    public boolean getStandalone()
    {
        return standalone;
    }

    public void setStandalone(boolean flag)
    {
        standalone = flag;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String s)
    {
        version = s;
    }

    public Object clone()
    {
        return new XMLDecl(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<?xml ");
        writer.write(content);
        writer.write("?>");
    }

    static final String DEFAULT_VERSION = "1.0";
    static final String DEFAULT_ENCODING = "UTF-8";
    static final boolean DEFAULT_STANDALONE = false;
    static final String START = "<?xml ";
    static final String STOP = "?>";
    String content;
    String version;
    String encoding;
    boolean standalone;
}
