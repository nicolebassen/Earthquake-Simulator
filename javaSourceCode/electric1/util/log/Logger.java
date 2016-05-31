// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Logger.java

package electric1.util.log;

import java.util.Date;

// Referenced classes of package electric1.util.log:
//            ILogger, Log

public abstract class Logger
    implements ILogger
{

    public Logger()
    {
        this(0L);
    }

    public Logger(long l)
    {
        mask = l;
    }

    public long getMask()
    {
        return mask;
    }

    public void setMask(long l)
    {
        mask = l;
        Log.recalcMasks();
    }

    public void addMask(long l)
    {
        mask |= l;
        Log.recalcMasks();
    }

    public abstract void event(String s, Object obj, Date date);

    public void removeMask(long l)
    {
        mask &= ~l;
        Log.recalcMasks();
    }

    public void startLogging(String s)
    {
        addMask(Log.getCode(s));
    }

    public void stopLogging(String s)
    {
        removeMask(Log.getCode(s));
    }

    long mask;
}
