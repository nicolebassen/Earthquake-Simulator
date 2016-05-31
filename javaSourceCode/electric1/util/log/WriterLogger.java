// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WriterLogger.java

package electric1.util.log;

import java.io.*;
import java.util.Date;

// Referenced classes of package electric1.util.log:
//            Logger

public class WriterLogger extends Logger
{

    public WriterLogger(long l)
    {
        this(((Writer) (new OutputStreamWriter(System.out))), l);
    }

    public WriterLogger(Writer writer1)
    {
        this(writer1, 0L);
    }

    public WriterLogger(Writer writer1, long l)
    {
        super(l);
        writer = writer1;
    }

    public Writer getWriter()
    {
        return writer;
    }

    public void setWriter(Writer writer1)
    {
        writer = writer1;
    }

    public void event(String s, Object obj, Date date)
    {
        try
        {
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("LOG.").append(s).append(": ");
            stringbuffer.append(obj.toString()).append('\n');
            writer.write(stringbuffer.toString());
            writer.flush();
        }
        catch(IOException ioexception)
        {
            System.out.println("WriterLogger error: " + ioexception);
        }
    }

    Writer writer;
}
