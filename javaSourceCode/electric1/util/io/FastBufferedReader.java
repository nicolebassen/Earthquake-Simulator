// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FastBufferedReader.java

package electric1.util.io;

import java.io.IOException;
import java.io.Reader;

public final class FastBufferedReader extends Reader
{

    public FastBufferedReader(Reader reader1)
    {
        this(reader1, defaultCharBufferSize);
    }

    public FastBufferedReader(Reader reader1, int i)
    {
        super(reader1);
        markedChar = -1;
        readAheadLimit = 0;
        skipLF = false;
        markedSkipLF = false;
        if(i <= 0)
        {
            throw new IllegalArgumentException("buffer size <= 0");
        } else
        {
            reader = reader1;
            buffer = new char[i];
            nextChar = nChars = 0;
            return;
        }
    }

    public void close()
        throws IOException
    {
        if(reader == null)
        {
            return;
        } else
        {
            reader.close();
            reader = null;
            buffer = null;
            return;
        }
    }

    void fill()
        throws IOException
    {
        int i;
        if(markedChar <= -1)
        {
            i = 0;
        } else
        {
            int j = nextChar - markedChar;
            if(j >= readAheadLimit)
            {
                markedChar = -2;
                readAheadLimit = 0;
                i = 0;
            } else
            {
                if(readAheadLimit <= buffer.length)
                {
                    System.arraycopy(buffer, markedChar, buffer, 0, j);
                    markedChar = 0;
                    i = j;
                } else
                {
                    char ac[] = new char[readAheadLimit];
                    System.arraycopy(buffer, markedChar, ac, 0, j);
                    buffer = ac;
                    markedChar = 0;
                    i = j;
                }
                nextChar = nChars = j;
            }
        }
        int k;
        do
            k = reader.read(buffer, i, buffer.length - i);
        while(k == 0);
        if(k > 0)
        {
            nChars = i + k;
            nextChar = i;
        }
    }

    public void mark(int i)
    {
        readAheadLimit = i;
        markedChar = nextChar;
        markedSkipLF = skipLF;
    }

    public boolean markSupported()
    {
        return true;
    }

    public int read()
        throws IOException
    {
        do
        {
            if(nextChar >= nChars)
            {
                fill();
                if(nextChar >= nChars)
                    return -1;
            }
            if(!skipLF)
                break;
            skipLF = false;
            if(buffer[nextChar] != '\n')
                break;
            nextChar++;
        } while(true);
        return buffer[nextChar++];
    }

    public int read(char ac[], int i, int j)
        throws IOException
    {
        if(j == 0)
            return 0;
        int k = read1(ac, i, j);
        if(k <= 0)
            return k;
        do
        {
            if(k >= j || !reader.ready())
                break;
            int l = read1(ac, i + k, j - k);
            if(l <= 0)
                break;
            k += l;
        } while(true);
        return k;
    }

    int read1(char ac[], int i, int j)
        throws IOException
    {
        if(nextChar >= nChars)
        {
            if(j >= buffer.length && markedChar <= -1 && !skipLF)
                return reader.read(ac, i, j);
            fill();
        }
        if(nextChar >= nChars)
            return -1;
        if(skipLF)
        {
            skipLF = false;
            if(buffer[nextChar] == '\n')
            {
                nextChar++;
                if(nextChar >= nChars)
                    fill();
                if(nextChar >= nChars)
                    return -1;
            }
        }
        int k = Math.min(j, nChars - nextChar);
        System.arraycopy(buffer, nextChar, ac, i, k);
        nextChar += k;
        return k;
    }

    public boolean ready()
        throws IOException
    {
        return nextChar < nChars || reader.ready();
    }

    public void reset()
        throws IOException
    {
        if(markedChar < 0)
        {
            throw new IOException(markedChar != -2 ? "Stream not marked" : "Mark invalid");
        } else
        {
            nextChar = markedChar;
            skipLF = markedSkipLF;
            return;
        }
    }

    public long skip(long l)
        throws IOException
    {
        long l1 = l;
        do
        {
            if(l1 <= 0L)
                break;
            if(nextChar >= nChars)
                fill();
            if(nextChar >= nChars)
                break;
            if(skipLF)
            {
                skipLF = false;
                if(buffer[nextChar] == '\n')
                    nextChar++;
            }
            long l2 = nChars - nextChar;
            if(l1 <= l2)
            {
                nextChar += l1;
                l1 = 0L;
                break;
            }
            l1 -= l2;
            nextChar = nChars;
        } while(true);
        return l - l1;
    }

    static final int INVALIDATED = -2;
    static final int UNMARKED = -1;
    static int defaultCharBufferSize = 8192;
    static int defaultExpectedLineLength = 80;
    Reader reader;
    char buffer[];
    int nChars;
    int nextChar;
    int markedChar;
    int readAheadLimit;
    boolean skipLF;
    boolean markedSkipLF;

}
