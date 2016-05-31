// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Text.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Child, Element, Parent

public class Text extends Child
{

    Text(Lex lex, StringBuffer stringbuffer, Element element)
        throws IOException
    {
        super(element);
        String s = lex.readToPattern("<", 72);
        string = stringbuffer != null ? stringbuffer.append(s).toString() : s;
    }

    Text(Parent parent)
    {
        super(parent);
    }

    public Text(Text text)
    {
        string = text.string;
    }

    public Text(String s)
    {
        string = s;
    }

    private static int getSpecialIndex(char c)
    {
        for(int i = 0; i < specials.length; i++)
            if(specials[i] == c)
                return i;

        return -1;
    }

    static void writeWithSubstitution(Writer writer, String s)
        throws IOException
    {
        char ac[] = s.toCharArray();
        int i = 0;
        for(int j = 0; j < ac.length; j++)
        {
            int k = getSpecialIndex(ac[j]);
            if(k < 0)
                continue;
            if(i < j)
                writer.write(ac, i, j - i);
            writer.write(substitutes[k]);
            i = j + 1;
        }

        if(i < ac.length)
            writer.write(ac, i, ac.length - i);
    }

    public boolean getRaw()
    {
        return raw;
    }

    public void setRaw(boolean flag)
    {
        raw = flag;
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
        return new Text(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        if(raw)
            writer.write(string);
        else
            writeWithSubstitution(writer, string);
    }

    static final char specials[] = {
        '&', '<', '>', '\'', '"'
    };
    static final String substitutes[] = {
        "&amp;", "&lt;", "&gt;", "&apos;", "&quot;"
    };
    String string;
    boolean raw;

}
