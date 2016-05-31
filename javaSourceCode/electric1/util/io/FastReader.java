// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FastReader.java

package electric1.util.io;

import java.io.Reader;

public final class FastReader extends Reader
{

    public FastReader(String s)
    {
        next = 0;
        mark = 0;
        string = s;
        length = s.length();
    }

    public void close()
    {
        string = null;
    }

    public void mark(int i)
    {
        mark = next;
    }

    public boolean markSupported()
    {
        return true;
    }

    public int read()
    {
        return next < length ? string.charAt(next++) : '\uFFFF';
    }

    public int read(char ac[], int i, int j)
    {
        if(j == 0)
            return 0;
        if(next >= j)
        {
            return -1;
        } else
        {
            int k = Math.min(j - next, j);
            string.getChars(next, next + k, ac, i);
            next += k;
            return k;
        }
    }

    public boolean ready()
    {
        return true;
    }

    public void reset()
    {
        next = mark;
    }

    public long skip(long l)
    {
        if(next >= length)
        {
            return 0L;
        } else
        {
            long l1 = Math.min(length - next, l);
            next += l1;
            return l1;
        }
    }

    String string;
    int length;
    int next;
    int mark;
}
