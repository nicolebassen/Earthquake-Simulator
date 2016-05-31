// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DocType.java

package electric1.xml;

import electric1.util.*;
import java.io.IOException;
import java.io.Writer;

// Referenced classes of package electric1.xml:
//            Parent, AttlistDecl, ElementDecl, EntityDecl, 
//            NotationDecl, Comment, Instruction, Child

public final class DocType extends Parent
{

    DocType(Lex lex, Parent parent)
        throws IOException
    {
        parent.addChild(this);
        lex.readChar(60);
        lex.readToken("<!DOCTYPE".substring(1));
        name = lex.readToDelimiter("[>");
        if(name.equals("[") || name.equals(">"))
            throw new IOException("DOCTYPE is missing a name");
        String s = lex.readToken();
        if(s.equals("SYSTEM"))
        {
            String s1 = lex.readToDelimiter("[>", 49);
            externalId = "SYSTEM '" + s1 + "'";
            s = lex.readToDelimiter("[>");
        } else
        if(s.equals("PUBLIC"))
        {
            String s2 = lex.readToDelimiter("[>", 49);
            String s3 = lex.readToDelimiter("[>", 49);
            externalId = "PUBLIC '" + s2 + "' '" + s3 + "'";
            s = lex.readToDelimiter("[>");
        }
        if(s.equals("["))
        {
            do
            {
                lex.skipWhitespace();
                int ai[] = new int[2];
                lex.peek(ai);
                int i = ai[0];
                int j = ai[1];
                if(i == 93)
                    break;
                if(i == -1)
                    throw new IOException("could not find matching ']' in DOCTYPE");
                if(i == 37)
                    lex.readToPattern(";", 6);
                else
                if(j == 33 && lex.peekString("<!ATTLIST"))
                    new AttlistDecl(lex, this);
                else
                if(j == 33 && lex.peekString("<!ELEMENT"))
                    new ElementDecl(lex, this);
                else
                if(j == 33 && lex.peekString("<!ENTITY"))
                    new EntityDecl(lex, this);
                else
                if(j == 33 && lex.peekString("<!NOTATION"))
                    new NotationDecl(lex, this);
                else
                if(j == 33 && lex.peekString("<!--"))
                    new Comment(lex, this);
                else
                if(j == 63)
                    new Instruction(lex, this);
                else
                    throw new IOException("illegal entry in DOCTYPE");
            } while(true);
            s = lex.readToken();
            s = lex.readToken();
        }
        if(!s.equals(">"))
            throw new IOException("could not find matching '>' in DOCTYPE");
        else
            return;
    }

    public DocType(DocType doctype)
    {
        super(doctype);
        name = doctype.name;
        externalId = doctype.externalId;
    }

    public DocType(String s)
    {
        name = s;
    }

    public String getExternalId()
    {
        return externalId;
    }

    public void setExternalId(String s)
    {
        externalId = s;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String s)
    {
        name = s;
    }

    public Object clone()
    {
        return new DocType(this);
    }

    public void write(Writer writer, int i)
        throws IOException
    {
        indent(writer, i);
        writer.write("<!DOCTYPE");
        writer.write(32);
        writer.write(name);
        if(externalId != null)
        {
            writer.write(32);
            writer.write(externalId);
        }
        if(!super.children.isEmpty())
        {
            writer.write("\r\n");
            int j = i + 2;
            indent(writer, j);
            writer.write(91);
            for(Node node = super.children.first; node != null; node = node.next)
            {
                writer.write("\r\n");
                ((Child)node).write(writer, j);
            }

            writer.write("\r\n");
            indent(writer, i + 2);
            writer.write(93);
            writer.write("\r\n");
        }
        writer.write(">");
    }

    static final String START = "<!DOCTYPE";
    static final String STOP = ">";
    static final String SYSTEM = "SYSTEM";
    static final String PUBLIC = "PUBLIC";
    String name;
    String externalId;
}
