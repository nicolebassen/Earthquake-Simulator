// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Log.java

package electric1.util.log;

import java.util.*;

// Referenced classes of package electric1.util.log:
//            ILogger, WriterLogger

public final class Log
{

    public Log()
    {
    }

    public static synchronized void addLogger(String s, ILogger ilogger)
    {
        nameToLogger.put(s, ilogger);
        recalcMasks();
    }

    public static synchronized String getCategory(long l)
    {
        return (String)codeToString.get(new Long(l));
    }

    public static synchronized long getCode(String s)
    {
        Long long1 = (Long)stringToCode.get(s);
        if(long1 == null)
        {
            long1 = new Long(1 << stringToCode.size());
            stringToCode.put(s, long1);
            codeToString.put(long1, s);
        }
        return long1.longValue();
    }

    public static synchronized ILogger getLogger(String s)
    {
        return (ILogger)nameToLogger.get(s);
    }

    public static synchronized Enumeration getLoggerNames()
    {
        Vector vector = new Vector();
        for(Enumeration enumeration = nameToLogger.keys(); enumeration.hasMoreElements(); vector.addElement(enumeration.nextElement()));
        return vector.elements();
    }

    public static synchronized Enumeration getLoggers()
    {
        Vector vector = new Vector();
        for(Enumeration enumeration = nameToLogger.elements(); enumeration.hasMoreElements(); vector.addElement(enumeration.nextElement()));
        return vector.elements();
    }

    public static boolean isLogging(long l)
    {
        return (masks & l) != 0L;
    }

    public static boolean isLogging(String s)
    {
        return isLogging(getCode(s));
    }

    public static synchronized void log(long l, Object obj)
    {
        if((masks & l) != 0L)
            log(l, getCategory(l), obj);
    }

    static synchronized void log(long l, String s, Object obj)
    {
        Date date = new Date();
        Enumeration enumeration = nameToLogger.elements();
        do
        {
            if(!enumeration.hasMoreElements())
                break;
            ILogger ilogger = (ILogger)enumeration.nextElement();
            if((ilogger.getMask() & l) != 0L)
                ilogger.event(s, obj, date);
        } while(true);
    }

    public static void log(Object obj)
    {
        log(COMMENT_EVENT, obj);
    }

    public static synchronized void log(String s, Object obj)
    {
        long l = getCode(s);
        if((masks & l) != 0L)
            log(l, s, obj);
    }

    static synchronized void recalcMasks()
    {
        masks = 0L;
        for(Enumeration enumeration = nameToLogger.elements(); enumeration.hasMoreElements();)
            masks |= ((ILogger)enumeration.nextElement()).getMask();

    }

    public static synchronized void removeLogger(String s)
    {
        nameToLogger.remove(s);
        recalcMasks();
    }

    public static synchronized void setMask(long l)
    {
        for(Enumeration enumeration = nameToLogger.elements(); enumeration.hasMoreElements(); ((ILogger)enumeration.nextElement()).setMask(l));
    }

    public static synchronized void startLogging(String s)
    {
        for(Enumeration enumeration = nameToLogger.elements(); enumeration.hasMoreElements(); ((ILogger)enumeration.nextElement()).startLogging(s));
    }

    public static void startup()
    {
        String s = System.getProperty("electric.logging");
        if(s == null)
            return;
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, ", \t"); stringtokenizer.hasMoreTokens(); startLogging(stringtokenizer.nextToken()));
    }

    public static synchronized void stopLogging(String s)
    {
        for(Enumeration enumeration = nameToLogger.elements(); enumeration.hasMoreElements(); ((ILogger)enumeration.nextElement()).stopLogging(s));
    }

    static final Hashtable stringToCode = new Hashtable();
    static final Hashtable codeToString = new Hashtable();
    static final Hashtable nameToLogger = new Hashtable();
    static final long ERROR_EVENT;
    static final long COMMENT_EVENT;
    static long masks;

    static 
    {
        ERROR_EVENT = getCode("ERROR");
        COMMENT_EVENT = getCode("COMMENT");
        addLogger("default", new WriterLogger(ERROR_EVENT | COMMENT_EVENT));
    }
}
