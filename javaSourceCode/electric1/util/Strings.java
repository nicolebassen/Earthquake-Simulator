// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Strings.java

package electric1.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

public final class Strings
{

    public Strings()
    {
    }

    public static String arrayToString(Object obj)
    {
        int i = Array.getLength(obj);
        if(i == 0)
            return "()";
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("( ");
        for(int j = 0; j < i; j++)
        {
            if(j > 0)
                stringbuffer.append(", ");
            stringbuffer.append(Array.get(obj, j));
        }

        return stringbuffer.append(" )").toString();
    }

    public static String asJavaName(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(i == 0 && Character.isJavaIdentifierStart(c) || Character.isJavaIdentifierPart(c))
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    public static String extension(String s)
    {
        int i = s.lastIndexOf('/');
        int j = s.lastIndexOf('.');
        return j <= i ? null : s.substring(j + 1);
    }

    public static String fromFilename(String s)
    {
        int i = s.indexOf('_');
        if(i == -1)
        {
            return s;
        } else
        {
            int j = s.indexOf('_', i + 1);
            char c = (char)Integer.parseInt(s.substring(i + 1, j));
            return s.substring(0, i) + c + fromFilename(s.substring(j + 1));
        }
    }

    public static String getCapitalized(String s)
    {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String getEndpoint(String s)
    {
        int i = s.lastIndexOf("/");
        return i != -1 ? s.substring(0, i) : "";
    }

    public static String getJavaPackage(String s)
    {
        int i = s.lastIndexOf('.');
        return i == -1 ? null : s.substring(0, i);
    }

    public static String getLocalJavaName(String s)
    {
        return tail(s, '.');
    }

    public static String getString(String s, String as[], int i)
    {
        if(i >= as.length)
            throw new IllegalArgumentException("missing argument to -" + s);
        else
            return as[i];
    }

    public static String getURN(String s)
    {
        int i = s.lastIndexOf("/");
        return i != -1 ? s.substring(i + 1) : s;
    }

    public static String head(String s, char c)
    {
        int i = s.lastIndexOf(c);
        return i != -1 ? s.substring(0, i) : s;
    }

    public static boolean isUTF16(byte abyte0[])
    {
        if(abyte0.length < 2)
            return false;
        else
            return abyte0[0] == -1 && abyte0[1] == -2 || abyte0[0] == -2 && abyte0[1] == -1;
    }

    public static boolean isUTF8(String s)
    {
        return s == null || s.equalsIgnoreCase("UTF-8") || s.equalsIgnoreCase("UTF8");
    }

    public static String normalizeEncoding(String s)
    {
        return s != null && !s.equalsIgnoreCase("UTF-8") ? s : "UTF8";
    }

    public static String replace(String s, String s1, String s2)
    {
        if(s.indexOf(s1.charAt(0)) == -1)
            return s;
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        do
        {
            int j = s.indexOf(s1, i);
            if(j == -1)
            {
                stringbuffer.append(s.substring(i, s.length()));
                return stringbuffer.toString();
            }
            stringbuffer.append(s.substring(i, j)).append(s2);
            i = j + s1.length();
        } while(true);
    }

    public static String splice(String s, String s1)
    {
        if(s1.length() == 0)
            return s;
        if(s.endsWith("/") && s1.startsWith("/"))
            return s + s1.substring(1);
        if(s.endsWith("/") || s1.startsWith("/"))
            return s + s1;
        if(s.length() > 0)
            return s + "/" + s1;
        else
            return s1;
    }

    public static void substitute(String as[][], String as1[][])
        throws IOException
    {
label0:
        for(int i = 0; i < as.length; i++)
        {
            String s = as[i][1];
            if(s.length() <= 0 || s.charAt(0) != '$')
                continue;
            String s1 = s.substring(1, s.length());
            int j = 0;
            do
            {
                if(j >= as1.length)
                    continue label0;
                if(as1[j][0].equals(s1))
                {
                    as[i][1] = as1[j][1];
                    continue label0;
                }
                j++;
            } while(true);
        }

    }

    public static String tail(String s, char c)
    {
        int i = s.lastIndexOf(c);
        return i != -1 ? s.substring(i + 1) : s;
    }

    public static String toFilename(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(Character.isLetterOrDigit(c) || c == '.')
            {
                stringbuffer.append(c);
            } else
            {
                stringbuffer.append('_');
                stringbuffer.append(Integer.toString(c));
                stringbuffer.append('_');
            }
        }

        return stringbuffer.toString();
    }

    public static String toString(byte abyte0[])
        throws UnsupportedEncodingException
    {
        return isUTF16(abyte0) ? new String(abyte0, "UTF-16") : new String(abyte0, "UTF8");
    }
}
