// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Instruction.java

package electric1.xml;

import electric1.util.Lex;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Child, Parent

public class Instruction extends Child
{

    Instruction(Lex lex, Parent parent)
        throws IOException
    {
        super(parent);
        lex.skip("<?".length());
        target = lex.readToken();
        content = lex.readToPattern("?>", 3);
    }

    public Instruction(Instruction instruction)
    {
        target = instruction.target;
        content = instruction.content;
    }

    public Instruction(String s, String s1)
    {
        target = s;
        content = s1;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String s)
    {
        content = s;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String s)
    {
        target = s;
    }

    public Object clone()
    {
        return new Instruction(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<?");
        writer.write(target);
        writer.write(32);
        writer.write(content);
        writer.write("?>");
    }

    static final String START = "<?";
    static final String STOP = "?>";
    String target;
    String content;
}
