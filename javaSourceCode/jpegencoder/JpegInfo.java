// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JpegInfo.java

package jpegencoder;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.image.PixelGrabber;

class JpegInfo
{

    public JpegInfo(Image image)
    {
        Ah = 0;
        Al = 0;
        NumberOfComponents = 3;
        Precision = 8;
        Se = 63;
        Ss = 0;
        Components = new Object[NumberOfComponents];
        compWidth = new int[NumberOfComponents];
        compHeight = new int[NumberOfComponents];
        BlockWidth = new int[NumberOfComponents];
        BlockHeight = new int[NumberOfComponents];
        imageobj = image;
        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
        Comment = "JPEG Encoder Copyright 1998, James R. Weeks and BioElectroMech.  ";
        getYCCArray();
    }

    public void setComment(String s)
    {
        Comment.concat(s);
    }

    public String getComment()
    {
        return Comment;
    }

    float[][] DownSample(float af[][], int i)
    {
        int j = 0;
        int k = 0;
        float af1[][] = new float[compHeight[i]][compWidth[i]];
        for(int l = 0; l < compHeight[i]; l++)
        {
            int j1 = 1;
            for(int i1 = 0; i1 < compWidth[i]; i1++)
            {
                af1[l][i1] = (af[j][k++] + af[j++][k--] + af[j][k++] + af[j--][k++] + (float)j1) / 4F;
                j1 ^= 3;
            }

            j += 2;
            k = 0;
        }

        return af1;
    }

    private void getYCCArray()
    {
        int ai[] = new int[imageWidth * imageHeight];
        PixelGrabber pixelgrabber = new PixelGrabber(imageobj.getSource(), 0, 0, imageWidth, imageHeight, ai, 0, imageWidth);
        MaxHsampFactor = 1;
        MaxVsampFactor = 1;
        for(int l = 0; l < NumberOfComponents; l++)
        {
            MaxHsampFactor = Math.max(MaxHsampFactor, HsampFactor[l]);
            MaxVsampFactor = Math.max(MaxVsampFactor, VsampFactor[l]);
        }

        for(int i1 = 0; i1 < NumberOfComponents; i1++)
        {
            compWidth[i1] = ((imageWidth % 8 == 0 ? imageWidth : (int)Math.ceil((double)imageWidth / 8D) * 8) / MaxHsampFactor) * HsampFactor[i1];
            if(compWidth[i1] != (imageWidth / MaxHsampFactor) * HsampFactor[i1])
                lastColumnIsDummy[i1] = true;
            BlockWidth[i1] = (int)Math.ceil((double)compWidth[i1] / 8D);
            compHeight[i1] = ((imageHeight % 8 == 0 ? imageHeight : (int)Math.ceil((double)imageHeight / 8D) * 8) / MaxVsampFactor) * VsampFactor[i1];
            if(compHeight[i1] != (imageHeight / MaxVsampFactor) * VsampFactor[i1])
                lastRowIsDummy[i1] = true;
            BlockHeight[i1] = (int)Math.ceil((double)compHeight[i1] / 8D);
        }

        try
        {
            if(!pixelgrabber.grabPixels())
                try
                {
                    throw new AWTException("Grabber returned false: " + pixelgrabber.status());
                }
                catch(Exception exception) { }
        }
        catch(InterruptedException interruptedexception) { }
        float af[][] = new float[compHeight[0]][compWidth[0]];
        float af1[][] = new float[compHeight[0]][compWidth[0]];
        float af2[][] = new float[compHeight[0]][compWidth[0]];
        float af3[][] = new float[compHeight[1]][compWidth[1]];
        float af4[][] = new float[compHeight[2]][compWidth[2]];
        int l1 = 0;
        for(int j1 = 0; j1 < imageHeight; j1++)
        {
            for(int k1 = 0; k1 < imageWidth; k1++)
            {
                int i = ai[l1] >> 16 & 0xff;
                int j = ai[l1] >> 8 & 0xff;
                int k = ai[l1] & 0xff;
                af[j1][k1] = (float)(0.29899999999999999D * (double)(float)i + 0.58699999999999997D * (double)(float)j + 0.114D * (double)(float)k);
                af2[j1][k1] = 128F + (float)((-0.16874D * (double)(float)i - 0.33126D * (double)(float)j) + 0.5D * (double)(float)k);
                af1[j1][k1] = 128F + (float)(0.5D * (double)(float)i - 0.41869000000000001D * (double)(float)j - 0.081309999999999993D * (double)(float)k);
                l1++;
            }

        }

        Components[0] = af;
        Components[1] = af2;
        Components[2] = af1;
    }

    public int ACtableNumber[] = {
        0, 1, 1
    };
    public int Ah;
    public int Al;
    public int BlockHeight[];
    public int BlockWidth[];
    public int CompID[] = {
        1, 2, 3
    };
    public Object Components[];
    public int DCtableNumber[] = {
        0, 1, 1
    };
    public int HsampFactor[] = {
        1, 1, 1
    };
    public int MaxHsampFactor;
    public int MaxVsampFactor;
    public int NumberOfComponents;
    public int Precision;
    public int QtableNumber[] = {
        0, 1, 1
    };
    public int Se;
    public int Ss;
    public int VsampFactor[] = {
        1, 1, 1
    };
    public int compHeight[];
    public int compWidth[];
    public int imageHeight;
    public int imageWidth;
    public Image imageobj;
    public boolean lastColumnIsDummy[] = {
        false, false, false
    };
    public boolean lastRowIsDummy[] = {
        false, false, false
    };
    String Comment;
}
