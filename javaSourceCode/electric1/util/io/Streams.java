// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Streams.java

package electric1.util.io;

import electric1.util.Strings;
import electric1.util.log.Log;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public final class Streams
{

    public Streams()
    {
    }

    public static void copy(InputStream inputstream, OutputStream outputstream, int i, int j)
        throws IOException
    {
        copy(inputstream, outputstream, i, j, -1L);
    }

    public static void copy(InputStream inputstream, OutputStream outputstream, int i, int j, long l)
        throws IOException
    {
        byte abyte0[] = new byte[i != -1 ? Math.min(j, i) : j];
        int k = 0;
        do
        {
            int i1 = i != -1 ? Math.min(j, i - k) : j;
            int j1 = readUpTo(inputstream, abyte0, 0, i1);
            if(j1 == 0)
                break;
            if(l > -1L && Log.isLogging(l))
                Log.log(l, new String(abyte0, 0, j1));
            outputstream.write(abyte0, 0, j1);
            k += j1;
        } while(i == -1 || k < i);
    }

    public static String getEncoding(byte abyte0[])
    {
        if(Strings.isUTF16(abyte0))
            return "UTF-16";
        String s = new String(abyte0);
        int i = s.indexOf("encoding=");
        if(i == -1)
        {
            return null;
        } else
        {
            int j = i + "encoding=".length() + 1;
            char c = s.charAt(j - 1);
            int k = s.indexOf(c, j);
            return s.substring(j, k);
        }
    }

    public static Reader getReader(File file)
        throws IOException
    {
        FileInputStream fileinputstream = new FileInputStream(file);
        byte abyte0[] = readUpTo(fileinputstream, 100);
        fileinputstream.close();
        String s = Strings.normalizeEncoding(getEncoding(abyte0));
        return new InputStreamReader(new FileInputStream(file), s);
    }

    public static Reader getReader(InputStream inputstream)
        throws IOException
    {
        if(!(inputstream instanceof BufferedInputStream))
            inputstream = new BufferedInputStream(inputstream);
        inputstream.mark(100);
        byte abyte0[] = readUpTo(inputstream, 100);
        inputstream.reset();
        String s = Strings.normalizeEncoding(getEncoding(abyte0));
        return new InputStreamReader(inputstream, s);
    }

    public static URL getURL(File file)
        throws MalformedURLException
    {
        String s = file.getAbsolutePath();
        if(File.separatorChar != '/')
            s = s.replace(File.separatorChar, '/');
        if(!s.startsWith("/"))
            s = "/" + s;
        if(!s.endsWith("/") && file.isDirectory())
            s = s + "/";
        return new URL("file", "", s);
    }

    public static Writer getWriter(File file, String s)
        throws IOException, UnsupportedEncodingException
    {
        return getWriter(((OutputStream) (new FileOutputStream(file))), s);
    }

    public static Writer getWriter(OutputStream outputstream, String s)
        throws UnsupportedEncodingException
    {
        s = Strings.normalizeEncoding(s);
        return new OutputStreamWriter(outputstream, s);
    }

    public static File[] listFiles(File file)
    {
        String as[] = file.list();
        if(as == null)
            return null;
        int i = as.length;
        File afile[] = new File[i];
        for(int j = 0; j < i; j++)
            afile[j] = new File(file.getPath(), as[j]);

        return afile;
    }

    public static byte[] loadResource(String s)
        throws IOException
    {
        URL url = new URL(s);
        InputStream inputstream = url.openStream();
        byte abyte0[] = readFully(inputstream);
        inputstream.close();
        return abyte0;
    }

    public static byte[] readFully(File file)
        throws IOException
    {
        RandomAccessFile randomaccessfile = new RandomAccessFile(file, "r");
        byte abyte0[] = new byte[(int)randomaccessfile.length()];
        randomaccessfile.readFully(abyte0);
        randomaccessfile.close();
        return abyte0;
    }

    public static byte[] readFully(InputStream inputstream)
        throws IOException
    {
        byte abyte0[] = new byte[10000];
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        do
        {
            int i = inputstream.read(abyte0, 0, abyte0.length);
            if(i != -1)
                bytearrayoutputstream.write(abyte0, 0, i);
            else
                return bytearrayoutputstream.toByteArray();
        } while(true);
    }

    public static void readFully(InputStream inputstream, byte abyte0[], int i, int j)
        throws IOException
    {
        int l;
        for(int k = 0; k < j; k += l)
        {
            l = inputstream.read(abyte0, i + k, j - k);
            if(l < 0)
                throw new EOFException("expected " + j + " bytes of content, got " + k);
        }

    }

    public static byte[] readFully(InputStream inputstream, int i)
        throws IOException
    {
        if(i <= 0)
        {
            return NO_BYTES;
        } else
        {
            byte abyte0[] = new byte[i];
            readFully(inputstream, abyte0, 0, i);
            return abyte0;
        }
    }

    public static String readLine(InputStream inputstream)
        throws IOException
    {
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            int i = inputstream.read();
            if(i == -1)
                return stringbuffer.length() != 0 ? stringbuffer.toString() : null;
            if(i == 13)
            {
                if((i = inputstream.read()) == 10)
                    return stringbuffer.toString();
                stringbuffer.append('\r');
                stringbuffer.append((char)i);
            } else
            {
                if(i == 10)
                    return stringbuffer.toString();
                stringbuffer.append((char)i);
            }
        } while(true);
    }

    public static int readUpTo(InputStream inputstream, byte abyte0[], int i, int j)
        throws IOException
    {
        int k = 0;
        do
        {
            if(k >= j)
                break;
            int l = inputstream.read(abyte0, i + k, j - k);
            if(l < 0)
                break;
            k += l;
        } while(true);
        return k;
    }

    public static byte[] readUpTo(InputStream inputstream, int i)
        throws IOException
    {
        if(i <= 0)
            return NO_BYTES;
        byte abyte0[] = new byte[i];
        int j = readUpTo(inputstream, abyte0, 0, i);
        if(j < i)
        {
            byte abyte1[] = abyte0;
            abyte0 = new byte[j];
            System.arraycopy(abyte1, 0, abyte0, 0, j);
        }
        return abyte0;
    }

    public static void saveFile(String s, String s1, String s2, String s3)
        throws IOException
    {
        if(!s.equals(""))
        {
            File file = new File(s);
            file.mkdirs();
        }
        File file1 = new File(s + s1 + s3);
        System.out.println("write file " + file1);
        if(file1.exists())
            file1.delete();
        FileOutputStream fileoutputstream = new FileOutputStream(file1);
        fileoutputstream.write(s2.getBytes());
        fileoutputstream.close();
    }

    static final String ENCODING = "encoding=";
    static final int CHUNK_SIZE = 10000;
    static final byte NO_BYTES[] = new byte[0];

}
