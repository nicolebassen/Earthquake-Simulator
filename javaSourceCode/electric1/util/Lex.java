// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Lex.java

package electric1.util;

import electric1.util.io.FastReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

public final class Lex
{

    public Lex(Reader reader1)
    {
        defaultDelimiters = "";
        defaultFlags = 1;
        index = 0;
        lastChar = 0;
        lineNumber = 1;
        charNumber = 0;
        window = new int[60];
        comment = new StringBuffer();
        reader = reader1;
    }

    public Lex(Reader reader1, String s, int i)
    {
        defaultDelimiters = "";
        defaultFlags = 1;
        index = 0;
        lastChar = 0;
        lineNumber = 1;
        charNumber = 0;
        window = new int[60];
        comment = new StringBuffer();
        reader = reader1;
        defaultDelimiters = s;
        defaultFlags = i;
    }

    public Lex(String s)
    {
        defaultDelimiters = "";
        defaultFlags = 1;
        index = 0;
        lastChar = 0;
        lineNumber = 1;
        charNumber = 0;
        window = new int[60];
        comment = new StringBuffer();
        reader = new FastReader(s);
    }

    public Lex(String s, String s1, int i)
    {
        defaultDelimiters = "";
        defaultFlags = 1;
        index = 0;
        lastChar = 0;
        lineNumber = 1;
        charNumber = 0;
        window = new int[60];
        comment = new StringBuffer();
        reader = new FastReader(s);
        defaultDelimiters = s1;
        defaultFlags = i;
    }

    public String getComment()
    {
        return comment.toString();
    }

    public void clearComment()
    {
        comment = new StringBuffer();
    }

    public boolean eof()
        throws IOException
    {
        skipWhitespace();
        return peek() == -1;
    }

    public String getLocation()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("line ").append(lineNumber).append(", char ").append(charNumber);
        stringbuffer.append(": ...");
        StringBuffer stringbuffer1 = new StringBuffer();
        int i = 0;
        int j = index;
        do
        {
            if(i++ >= charNumber)
                break;
            if(j-- == 0)
                j = 59;
            if(window[j] == 10 || j == index)
                break;
            stringbuffer1.append((char)window[j]);
        } while(true);
        return stringbuffer.append(stringbuffer1.reverse()).toString();
    }

    public void mark(int i)
        throws IOException
    {
        reader.mark(i);
    }

    public int peek()
        throws IOException
    {
        reader.mark(1);
        int i = reader.read();
        reader.reset();
        return i;
    }

    public void peek(int ai[])
        throws IOException
    {
        reader.mark(ai.length);
        for(int i = 0; i < ai.length; i++)
            ai[i] = reader.read();

        reader.reset();
    }

    public int peekRead()
        throws IOException
    {
        return reader.read();
    }

    public String peekString(int i)
        throws IOException
    {
        int ai[] = new int[i];
        peek(ai);
        StringBuffer stringbuffer = new StringBuffer();
        for(int j = 0; j < i && ai[j] != -1; j++)
            stringbuffer.append((char)ai[j]);

        return stringbuffer.toString();
    }

    public boolean peekString(String s)
        throws IOException
    {
        return s.equals(peekString(s.length()));
    }

    public int read()
        throws IOException
    {
        if(lastChar == 10)
        {
            lineNumber++;
            charNumber = 0;
        }
        int i = reader.read();
        charNumber++;
        lastChar = i;
        window[index] = i;
        if(++index == window.length)
            index = 0;
        return i;
    }

    public int readChar()
        throws IOException
    {
        skipWhitespace();
        int i = read();
        if(i == -1)
            throw new IOException("unexpected EOF");
        else
            return i;
    }

    public void readChar(int i)
        throws IOException
    {
        int j = readChar();
        if(j != i)
            throw new IOException("expected '" + (char)i + "', got '" + (char)j + "'");
        else
            return;
    }

    public void readHTML(StringBuffer stringbuffer)
        throws IOException
    {
        read();
        String s = readTo(59);
        try
        {
            if(s.startsWith("#x"))
                stringbuffer.append((char)Integer.parseInt(s.substring(2), 16));
            else
            if(s.startsWith("#"))
            {
                stringbuffer.append((char)Integer.parseInt(s.substring(1)));
            } else
            {
                String s1 = (String)substitutions.get(s);
                if(s1 != null)
                {
                    stringbuffer.append(s1);
                } else
                {
                    stringbuffer.append('&');
                    stringbuffer.append(s);
                    stringbuffer.append(';');
                }
            }
        }
        catch(NumberFormatException numberformatexception)
        {
            throw new IOException("number format exception while parsing &" + s + ";");
        }
    }

    void readMultiLineComment()
        throws IOException
    {
        int i;
        do
        {
            i = read();
            if(i == -1)
                throw new IOException("missing */ on comment");
            comment.append((char)i);
        } while(i != 42 || peek() != 47);
        i = read();
        comment.append((char)i);
    }

    void readOneLineComment()
        throws IOException
    {
        comment.append(readToPattern("\n", 10));
    }

    void readQuoted(StringBuffer stringbuffer, int i, int j)
        throws IOException
    {
        int k = read();
        if((j & 0x20) == 0)
            stringbuffer.append((char)k);
        do
        {
            int l = read();
            if(l == -1)
                break;
            if(l == i)
            {
                if((j & 0x20) == 0)
                    stringbuffer.append((char)l);
                break;
            }
            stringbuffer.append((char)l);
        } while(true);
    }

    public String readTo(int i)
        throws IOException
    {
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            int j = read();
            if(j == -1)
                throw new IOException("could not find " + i);
            if(j != i)
                stringbuffer.append((char)j);
            else
                return stringbuffer.toString();
        } while(true);
    }

    public String readToDelimiter(String s)
        throws IOException
    {
        return readToDelimiter(s, defaultFlags);
    }

    public String readToDelimiter(String s, int i)
        throws IOException
    {
        if((i & 1) != 0)
            skipWhitespace(i);
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            int j = peek();
            if(j == -1)
                if(stringbuffer.length() <= 0)
                {
                    throw new IOException("unexpected EOF");
                } else
                {
                    read();
                    return stringbuffer.toString();
                }
            if(s.indexOf(j) != -1)
            {
                if(stringbuffer.length() == 0)
                    stringbuffer.append((char)read());
                return stringbuffer.toString();
            }
            if(Character.isWhitespace((char)j))
                return stringbuffer.toString();
            if(j == 38 && (i & 0x40) != 0)
                readHTML(stringbuffer);
            else
            if((j == 39 || j == 34) && (i & 0x10) != 0)
                readQuoted(stringbuffer, j, i);
            else
                stringbuffer.append((char)read());
        } while(true);
    }

    public String readToken()
        throws IOException
    {
        return readToDelimiter(defaultDelimiters, defaultFlags);
    }

    public void readToken(String s)
        throws IOException
    {
        String s1 = readToken();
        if(!s.equals(s1))
            throw new IOException("expected \"" + s + "\", got \"" + s1 + "\"");
        else
            return;
    }

    public String readToPattern(String s, int i)
        throws IOException
    {
        StringBuffer stringbuffer = new StringBuffer();
        if((i & 1) != 0)
            skipWhitespace(i);
        char c = s.charAt(0);
        int j = s.length();
        do
        {
            int k = peek();
            if(k == -1)
                if((i & 8) == 0)
                {
                    throw new IOException("unexpected EOF while searching for '" + s + "'");
                } else
                {
                    read();
                    return stringbuffer.toString();
                }
            if(k == c)
            {
                if(j == 1)
                {
                    if((i & 4) != 0)
                        stringbuffer.append((char)k);
                    if((i & 2) != 0)
                        read();
                    return stringbuffer.toString();
                }
                int ai[] = new int[j];
                peek(ai);
                boolean flag = true;
                int l = 1;
                do
                {
                    if(l >= j)
                        break;
                    if(ai[l] != s.charAt(l))
                    {
                        flag = false;
                        break;
                    }
                    l++;
                } while(true);
                if(flag)
                {
                    if((i & 2) != 0)
                    {
                        for(int i1 = 0; i1 < j; i1++)
                            read();

                    }
                    if((i & 4) != 0)
                    {
                        for(int j1 = 0; j1 < j; j1++)
                            stringbuffer.append((char)ai[j1]);

                    }
                    return stringbuffer.toString();
                }
            }
            if(k == 38 && (i & 0x40) != 0)
                readHTML(stringbuffer);
            else
            if((k == 39 || k == 34) && (i & 0x10) != 0)
                readQuoted(stringbuffer, k, i);
            else
                stringbuffer.append((char)read());
        } while(true);
    }

    public StringBuffer readWhitespace()
        throws IOException
    {
        return readWhitespace(defaultFlags);
    }

    public StringBuffer readWhitespace(int i)
        throws IOException
    {
        StringBuffer stringbuffer = null;
        do
        {
            reader.mark(2);
            int j = reader.read();
            if(Character.isWhitespace((char)j))
            {
                if(stringbuffer == null)
                    stringbuffer = new StringBuffer();
                reader.reset();
                stringbuffer.append((char)read());
            } else
            if(j == 47 && (i & 0x80) != 0)
            {
                int k = reader.read();
                reader.reset();
                if(k == 47)
                    readOneLineComment();
                else
                if(k == 42)
                    readMultiLineComment();
                else
                    return stringbuffer;
            } else
            {
                reader.reset();
                return stringbuffer;
            }
        } while(true);
    }

    public void reset()
        throws IOException
    {
        reader.reset();
    }

    public void skip(int i)
        throws IOException
    {
        for(int j = 0; j < i; j++)
            read();

    }

    public void skipWhitespace()
        throws IOException
    {
        skipWhitespace(defaultFlags);
    }

    public void skipWhitespace(int i)
        throws IOException
    {
        do
        {
            reader.mark(2);
            int j = reader.read();
            if(Character.isWhitespace((char)j))
            {
                reader.reset();
                read();
            } else
            if(j == 47 && (i & 0x80) != 0)
            {
                int k = reader.read();
                reader.reset();
                if(k == 47)
                    readOneLineComment();
                else
                if(k == 42)
                    readMultiLineComment();
                else
                    return;
            } else
            {
                reader.reset();
                return;
            }
        } while(true);
    }

    public static final int SKIP_WS = 1;
    public static final int CONSUME = 2;
    public static final int INCLUDE = 4;
    public static final int EOF_OK = 8;
    public static final int QUOTES = 16;
    public static final int STRIP = 32;
    public static final int HTML = 64;
    public static final int COMMENTS = 128;
    static final int BUFFER_SIZE = 60;
    static final Hashtable substitutions;
    Reader reader;
    String defaultDelimiters;
    int defaultFlags;
    int index;
    int lastChar;
    int lineNumber;
    int charNumber;
    int window[];
    StringBuffer comment;

    static 
    {
        substitutions = new Hashtable();
        substitutions.put("apos", "'");
        substitutions.put("lt", "<");
        substitutions.put("gt", ">");
        substitutions.put("quot", "\"");
        substitutions.put("amp", "&");
    }
}
