// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Huffman.java

package jpegencoder;

import java.io.*;
import java.util.Vector;

class Huffman
{

    public Huffman(int i, int j)
    {
        bits = new Vector();
        bits.addElement(bitsDCluminance);
        bits.addElement(bitsACluminance);
        bits.addElement(bitsDCchrominance);
        bits.addElement(bitsACchrominance);
        val = new Vector();
        val.addElement(valDCluminance);
        val.addElement(valACluminance);
        val.addElement(valDCchrominance);
        val.addElement(valACchrominance);
        initHuf();
        code = code;
        ImageWidth = i;
        ImageHeight = j;
    }

    public void HuffmanBlockEncoder(BufferedOutputStream bufferedoutputstream, int ai[], int i, int j, int k)
    {
        NumOfDCTables = 2;
        NumOfACTables = 2;
        int j1;
        int l = j1 = ai[0] - i;
        if(l < 0)
        {
            l = -l;
            j1--;
        }
        int l1 = 0;
        for(; l != 0; l >>= 1)
            l1++;

        bufferIt(bufferedoutputstream, ((int[][])DC_matrix[j])[l1][0], ((int[][])DC_matrix[j])[l1][1]);
        if(l1 != 0)
            bufferIt(bufferedoutputstream, j1, l1);
        int k2 = 0;
        for(int j2 = 1; j2 < 64; j2++)
        {
            int i1;
            if((i1 = ai[jpegNaturalOrder[j2]]) == 0)
            {
                k2++;
                continue;
            }
            for(; k2 > 15; k2 -= 16)
                bufferIt(bufferedoutputstream, ((int[][])AC_matrix[k])[240][0], ((int[][])AC_matrix[k])[240][1]);

            int k1 = i1;
            if(i1 < 0)
            {
                i1 = -i1;
                k1--;
            }
            int i2;
            for(i2 = 1; (i1 >>= 1) != 0; i2++);
            int l2 = (k2 << 4) + i2;
            bufferIt(bufferedoutputstream, ((int[][])AC_matrix[k])[l2][0], ((int[][])AC_matrix[k])[l2][1]);
            bufferIt(bufferedoutputstream, k1, i2);
            k2 = 0;
        }

        if(k2 > 0)
            bufferIt(bufferedoutputstream, ((int[][])AC_matrix[k])[0][0], ((int[][])AC_matrix[k])[0][1]);
    }

    public void initHuf()
    {
        DC_matrix0 = new int[12][2];
        DC_matrix1 = new int[12][2];
        AC_matrix0 = new int[255][2];
        AC_matrix1 = new int[255][2];
        DC_matrix = new Object[2];
        AC_matrix = new Object[2];
        int ai[] = new int[257];
        int ai1[] = new int[257];
        int i = 0;
        for(int k = 1; k <= 16; k++)
        {
            for(int k1 = 1; k1 <= bitsDCchrominance[k]; k1++)
                ai[i++] = k;

        }

        ai[i] = 0;
        int k2 = i;
        int i3 = 0;
        int l2 = ai[0];
        for(i = 0; ai[i] != 0;)
        {
            while(ai[i] == l2) 
            {
                ai1[i++] = i3;
                i3++;
            }
            i3 <<= 1;
            l2++;
        }

        for(i = 0; i < k2; i++)
        {
            DC_matrix1[valDCchrominance[i]][0] = ai1[i];
            DC_matrix1[valDCchrominance[i]][1] = ai[i];
        }

        i = 0;
        for(int l = 1; l <= 16; l++)
        {
            for(int l1 = 1; l1 <= bitsACchrominance[l]; l1++)
                ai[i++] = l;

        }

        ai[i] = 0;
        k2 = i;
        i3 = 0;
        l2 = ai[0];
        for(i = 0; ai[i] != 0;)
        {
            while(ai[i] == l2) 
            {
                ai1[i++] = i3;
                i3++;
            }
            i3 <<= 1;
            l2++;
        }

        for(i = 0; i < k2; i++)
        {
            AC_matrix1[valACchrominance[i]][0] = ai1[i];
            AC_matrix1[valACchrominance[i]][1] = ai[i];
        }

        i = 0;
        for(int i1 = 1; i1 <= 16; i1++)
        {
            for(int i2 = 1; i2 <= bitsDCluminance[i1]; i2++)
                ai[i++] = i1;

        }

        ai[i] = 0;
        k2 = i;
        i3 = 0;
        l2 = ai[0];
        for(i = 0; ai[i] != 0;)
        {
            while(ai[i] == l2) 
            {
                ai1[i++] = i3;
                i3++;
            }
            i3 <<= 1;
            l2++;
        }

        for(i = 0; i < k2; i++)
        {
            DC_matrix0[valDCluminance[i]][0] = ai1[i];
            DC_matrix0[valDCluminance[i]][1] = ai[i];
        }

        i = 0;
        for(int j1 = 1; j1 <= 16; j1++)
        {
            for(int j2 = 1; j2 <= bitsACluminance[j1]; j2++)
                ai[i++] = j1;

        }

        ai[i] = 0;
        k2 = i;
        i3 = 0;
        l2 = ai[0];
        for(int j = 0; ai[j] != 0;)
        {
            while(ai[j] == l2) 
            {
                ai1[j++] = i3;
                i3++;
            }
            i3 <<= 1;
            l2++;
        }

        for(int j3 = 0; j3 < k2; j3++)
        {
            AC_matrix0[valACluminance[j3]][0] = ai1[j3];
            AC_matrix0[valACluminance[j3]][1] = ai[j3];
        }

        DC_matrix[0] = DC_matrix0;
        DC_matrix[1] = DC_matrix1;
        AC_matrix[0] = AC_matrix0;
        AC_matrix[1] = AC_matrix1;
    }

    void bufferIt(BufferedOutputStream bufferedoutputstream, int i, int j)
    {
        int k = i;
        int l = bufferPutBits;
        k &= (1 << j) - 1;
        l += j;
        k <<= 24 - l;
        k |= bufferPutBuffer;
        for(; l >= 8; l -= 8)
        {
            int i1 = k >> 16 & 0xff;
            try
            {
                bufferedoutputstream.write(i1);
            }
            catch(IOException ioexception)
            {
                System.out.println("IO Error: " + ioexception.getMessage());
            }
            if(i1 == 255)
                try
                {
                    bufferedoutputstream.write(0);
                }
                catch(IOException ioexception1)
                {
                    System.out.println("IO Error: " + ioexception1.getMessage());
                }
            k <<= 8;
        }

        bufferPutBuffer = k;
        bufferPutBits = l;
    }

    void flushBuffer(BufferedOutputStream bufferedoutputstream)
    {
        int i = bufferPutBuffer;
        int j;
        for(j = bufferPutBits; j >= 8; j -= 8)
        {
            int k = i >> 16 & 0xff;
            try
            {
                bufferedoutputstream.write(k);
            }
            catch(IOException ioexception)
            {
                System.out.println("IO Error: " + ioexception.getMessage());
            }
            if(k == 255)
                try
                {
                    bufferedoutputstream.write(0);
                }
                catch(IOException ioexception1)
                {
                    System.out.println("IO Error: " + ioexception1.getMessage());
                }
            i <<= 8;
        }

        if(j > 0)
        {
            int l = i >> 16 & 0xff;
            try
            {
                bufferedoutputstream.write(l);
            }
            catch(IOException ioexception2)
            {
                System.out.println("IO Error: " + ioexception2.getMessage());
            }
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
    public Object AC_matrix[];
    public int AC_matrix0[][];
    public int AC_matrix1[][];
    public Object DC_matrix[];
    public int DC_matrix0[][];
    public int DC_matrix1[][];
    public int ImageHeight;
    public int ImageWidth;
    public int NumOfACTables;
    public int NumOfDCTables;
    public Vector bits;
    public int bitsACchrominance[] = {
        17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 
        5, 4, 4, 0, 1, 2, 119
    };
    public int bitsACluminance[] = {
        16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 
        5, 4, 4, 0, 0, 1, 125
    };
    public int bitsDCchrominance[] = {
        1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 
        1, 1, 0, 0, 0, 0, 0
    };
    public int bitsDCluminance[] = {
        0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 
        0, 0, 0, 0, 0, 0, 0
    };
    public int code;
    public Vector val;
    public int valACchrominance[] = {
        0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 
        18, 65, 81, 7, 97, 113, 19, 34, 50, 129, 
        8, 20, 66, 145, 161, 177, 193, 9, 35, 51, 
        82, 240, 21, 98, 114, 209, 10, 22, 36, 52, 
        225, 37, 241, 23, 24, 25, 26, 38, 39, 40, 
        41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 
        69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 
        87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 
        105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 
        130, 131, 132, 133, 134, 135, 136, 137, 138, 146, 
        147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 
        164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 
        181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 
        198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 
        215, 216, 217, 218, 226, 227, 228, 229, 230, 231, 
        232, 233, 234, 242, 243, 244, 245, 246, 247, 248, 
        249, 250
    };
    public int valACluminance[] = {
        1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 
        65, 6, 19, 81, 97, 7, 34, 113, 20, 50, 
        129, 145, 161, 8, 35, 66, 177, 193, 21, 82, 
        209, 240, 36, 51, 98, 114, 130, 9, 10, 22, 
        23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 
        52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 
        70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 
        88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 
        106, 115, 116, 117, 118, 119, 120, 121, 122, 131, 
        132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 
        149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 
        166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 
        183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 
        200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 
        217, 218, 225, 226, 227, 228, 229, 230, 231, 232, 
        233, 234, 241, 242, 243, 244, 245, 246, 247, 248, 
        249, 250
    };
    public int valDCchrominance[] = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
        10, 11
    };
    public int valDCluminance[] = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
        10, 11
    };
    int bufferPutBits;
    int bufferPutBuffer;

}
