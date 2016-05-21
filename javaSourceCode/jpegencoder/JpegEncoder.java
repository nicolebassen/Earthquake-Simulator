// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JpegEncoder.java

package jpegencoder;

import java.awt.*;
import java.io.*;
import java.util.Vector;

// Referenced classes of package jpegencoder:
//            JpegInfo, DCT, Huffman

public class JpegEncoder extends Frame
{

    public JpegEncoder(Image image1, int i, OutputStream outputstream)
    {
        MediaTracker mediatracker = new MediaTracker(this);
        mediatracker.addImage(image1, 0);
        try
        {
            mediatracker.waitForID(0);
        }
        catch(InterruptedException interruptedexception) { }
        Quality = i;
        JpegObj = new JpegInfo(image1);
        imageHeight = JpegObj.imageHeight;
        imageWidth = JpegObj.imageWidth;
        outStream = new BufferedOutputStream(outputstream);
        dct = new DCT(Quality);
        Huf = new Huffman(imageWidth, imageHeight);
    }

    public void setQuality(int i)
    {
        dct = new DCT(i);
    }

    public int getQuality()
    {
        return Quality;
    }

    public void Compress()
    {
        WriteHeaders(outStream);
        WriteCompressedData(outStream);
        WriteEOI(outStream);
        try
        {
            outStream.flush();
        }
        catch(IOException ioexception)
        {
            System.out.println("IO Error: " + ioexception.getMessage());
        }
    }

    public void WriteCompressedData(BufferedOutputStream bufferedoutputstream)
    {
        boolean flag = false;
        float af1[][] = new float[8][8];
        double ad[][] = new double[8][8];
        int ai[] = new int[64];
        int ai2[] = new int[JpegObj.NumberOfComponents];
        int ai3[] = new int[64];
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        int k3 = imageWidth % 8 == 0 ? imageWidth : (int)(Math.floor((double)imageWidth / 8D) + 1.0D) * 8;
        int l3 = imageHeight % 8 == 0 ? imageHeight : (int)(Math.floor((double)imageHeight / 8D) + 1.0D) * 8;
        for(int k1 = 0; k1 < JpegObj.NumberOfComponents; k1++)
        {
            k3 = Math.min(k3, JpegObj.BlockWidth[k1]);
            l3 = Math.min(l3, JpegObj.BlockHeight[k1]);
        }

        boolean flag1 = false;
        for(int k = 0; k < l3; k++)
        {
            for(int l = 0; l < k3; l++)
            {
                int i2 = l * 8;
                int j2 = k * 8;
                for(int l1 = 0; l1 < JpegObj.NumberOfComponents; l1++)
                {
                    int i3 = JpegObj.BlockWidth[l1];
                    int j3 = JpegObj.BlockHeight[l1];
                    float af[][] = (float[][])JpegObj.Components[l1];
                    for(int i = 0; i < JpegObj.VsampFactor[l1]; i++)
                    {
                        for(int j = 0; j < JpegObj.HsampFactor[l1]; j++)
                        {
                            int k2 = j * 8;
                            int l2 = i * 8;
                            for(int i1 = 0; i1 < 8; i1++)
                            {
                                for(int j1 = 0; j1 < 8; j1++)
                                    af1[i1][j1] = af[j2 + l2 + i1][i2 + k2 + j1];

                            }

                            double ad1[][] = dct.forwardDCT(af1);
                            int ai1[] = dct.quantizeBlock(ad1, JpegObj.QtableNumber[l1]);
                            Huf.HuffmanBlockEncoder(bufferedoutputstream, ai1, ai2[l1], JpegObj.DCtableNumber[l1], JpegObj.ACtableNumber[l1]);
                            ai2[l1] = ai1[0];
                        }

                    }

                }

            }

        }

        Huf.flushBuffer(bufferedoutputstream);
    }

    public void WriteEOI(BufferedOutputStream bufferedoutputstream)
    {
        byte abyte0[] = {
            -1, -39
        };
        WriteMarker(abyte0, bufferedoutputstream);
    }

    public void WriteHeaders(BufferedOutputStream bufferedoutputstream)
    {
        byte abyte0[] = {
            -1, -40
        };
        WriteMarker(abyte0, bufferedoutputstream);
        byte abyte1[] = new byte[18];
        abyte1[0] = -1;
        abyte1[1] = -32;
        abyte1[2] = 0;
        abyte1[3] = 16;
        abyte1[4] = 74;
        abyte1[5] = 70;
        abyte1[6] = 73;
        abyte1[7] = 70;
        abyte1[8] = 0;
        abyte1[9] = 1;
        abyte1[10] = 0;
        abyte1[11] = 0;
        abyte1[12] = 0;
        abyte1[13] = 1;
        abyte1[14] = 0;
        abyte1[15] = 1;
        abyte1[16] = 0;
        abyte1[17] = 0;
        WriteArray(abyte1, bufferedoutputstream);
        String s = new String();
        s = JpegObj.getComment();
        int j2 = s.length();
        byte abyte2[] = new byte[j2 + 4];
        abyte2[0] = -1;
        abyte2[1] = -2;
        abyte2[2] = (byte)(j2 >> 8 & 0xff);
        abyte2[3] = (byte)(j2 & 0xff);
        System.arraycopy(JpegObj.Comment.getBytes(), 0, abyte2, 4, JpegObj.Comment.length());
        WriteArray(abyte2, bufferedoutputstream);
        byte abyte3[] = new byte[134];
        abyte3[0] = -1;
        abyte3[1] = -37;
        abyte3[2] = 0;
        abyte3[3] = -124;
        int i2 = 4;
        for(int i = 0; i < 2; i++)
        {
            abyte3[i2++] = (byte)(0 + i);
            int ai[] = (int[])dct.quantum[i];
            for(int i1 = 0; i1 < 64; i1++)
                abyte3[i2++] = (byte)ai[jpegNaturalOrder[i1]];

        }

        WriteArray(abyte3, bufferedoutputstream);
        byte abyte4[] = new byte[19];
        abyte4[0] = -1;
        abyte4[1] = -64;
        abyte4[2] = 0;
        abyte4[3] = 17;
        abyte4[4] = (byte)JpegObj.Precision;
        abyte4[5] = (byte)(JpegObj.imageHeight >> 8 & 0xff);
        abyte4[6] = (byte)(JpegObj.imageHeight & 0xff);
        abyte4[7] = (byte)(JpegObj.imageWidth >> 8 & 0xff);
        abyte4[8] = (byte)(JpegObj.imageWidth & 0xff);
        abyte4[9] = (byte)JpegObj.NumberOfComponents;
        int l1 = 10;
        for(int j = 0; j < abyte4[9]; j++)
        {
            abyte4[l1++] = (byte)JpegObj.CompID[j];
            abyte4[l1++] = (byte)((JpegObj.HsampFactor[j] << 4) + JpegObj.VsampFactor[j]);
            abyte4[l1++] = (byte)JpegObj.QtableNumber[j];
        }

        WriteArray(abyte4, bufferedoutputstream);
        j2 = 2;
        l1 = 4;
        int i3 = 4;
        byte abyte5[] = new byte[17];
        byte abyte8[] = new byte[4];
        abyte8[0] = -1;
        abyte8[1] = -60;
        for(int k = 0; k < 4; k++)
        {
            int k2 = 0;
            abyte5[l1++ - i3] = (byte)((int[])Huf.bits.elementAt(k))[0];
            for(int j1 = 1; j1 < 17; j1++)
            {
                int l2 = ((int[])Huf.bits.elementAt(k))[j1];
                abyte5[l1++ - i3] = (byte)l2;
                k2 += l2;
            }

            int j3 = l1;
            byte abyte6[] = new byte[k2];
            for(int k1 = 0; k1 < k2; k1++)
                abyte6[l1++ - j3] = (byte)((int[])Huf.val.elementAt(k))[k1];

            byte abyte7[] = new byte[l1];
            System.arraycopy(abyte8, 0, abyte7, 0, i3);
            System.arraycopy(abyte5, 0, abyte7, i3, 17);
            System.arraycopy(abyte6, 0, abyte7, i3 + 17, k2);
            abyte8 = abyte7;
            i3 = l1;
        }

        abyte8[2] = (byte)(l1 - 2 >> 8 & 0xff);
        abyte8[3] = (byte)(l1 - 2 & 0xff);
        WriteArray(abyte8, bufferedoutputstream);
        byte abyte9[] = new byte[14];
        abyte9[0] = -1;
        abyte9[1] = -38;
        abyte9[2] = 0;
        abyte9[3] = 12;
        abyte9[4] = (byte)JpegObj.NumberOfComponents;
        l1 = 5;
        for(int l = 0; l < abyte9[4]; l++)
        {
            abyte9[l1++] = (byte)JpegObj.CompID[l];
            abyte9[l1++] = (byte)((JpegObj.DCtableNumber[l] << 4) + JpegObj.ACtableNumber[l]);
        }

        abyte9[l1++] = (byte)JpegObj.Ss;
        abyte9[l1++] = (byte)JpegObj.Se;
        abyte9[l1++] = (byte)((JpegObj.Ah << 4) + JpegObj.Al);
        WriteArray(abyte9, bufferedoutputstream);
    }

    void WriteArray(byte abyte0[], BufferedOutputStream bufferedoutputstream)
    {
        try
        {
            int i = ((abyte0[2] & 0xff) << 8) + (abyte0[3] & 0xff) + 2;
            bufferedoutputstream.write(abyte0, 0, i);
        }
        catch(IOException ioexception)
        {
            System.out.println("IO Error: " + ioexception.getMessage());
        }
    }

    void WriteMarker(byte abyte0[], BufferedOutputStream bufferedoutputstream)
    {
        try
        {
            bufferedoutputstream.write(abyte0, 0, 2);
        }
        catch(IOException ioexception)
        {
            System.out.println("IO Error: " + ioexception.getMessage());
        }
    }

    public static int jpegNaturalOrder[] = {
        0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 
        32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 
        40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 
        21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 
        29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 
        52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 
        47, 55, 62, 63
    };
    Huffman Huf;
    JpegInfo JpegObj;
    int Quality;
    int code;
    DCT dct;
    Image image;
    int imageHeight;
    int imageWidth;
    BufferedOutputStream outStream;
    Thread runner;

}
