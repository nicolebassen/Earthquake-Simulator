// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ILogger.java

package electric1.util.log;

import java.util.Date;

public interface ILogger
{

    public abstract void event(String s, Object obj, Date date);

    public abstract long getMask();

    public abstract void setMask(long l);

    public abstract void startLogging(String s);

    public abstract void stopLogging(String s);
}
