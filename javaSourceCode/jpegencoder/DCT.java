// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DCT.java

package jpegencoder;


class DCT
{

    public DCT(int i)
    {
        Divisors = new Object[2];
        N = 8;
        QUALITY = 80;
        quantum = new Object[2];
        quantum_luminance = new int[N * N];
        DivisorsLuminance = new double[N * N];
        quantum_chrominance = new int[N * N];
        DivisorsChrominance = new double[N * N];
        initMatrix(i);
    }

    public double[][] forwardDCT(float af[][])
    {
        double ad[][] = new double[N][N];
        for(int i = 0; i < 8; i++)
        {
            for(int l = 0; l < 8; l++)
                ad[i][l] = (double)af[i][l] - 128D;

        }

        for(int j = 0; j < 8; j++)
        {
            double d = ad[j][0] + ad[j][7];
            double d14 = ad[j][0] - ad[j][7];
            double d2 = ad[j][1] + ad[j][6];
            double d12 = ad[j][1] - ad[j][6];
            double d4 = ad[j][2] + ad[j][5];
            double d10 = ad[j][2] - ad[j][5];
            double d6 = ad[j][3] + ad[j][4];
            double d8 = ad[j][3] - ad[j][4];
            double d16 = d + d6;
            double d22 = d - d6;
            double d18 = d2 + d4;
            double d20 = d2 - d4;
            ad[j][0] = d16 + d18;
            ad[j][4] = d16 - d18;
            double d24 = (d20 + d22) * 0.70710678100000002D;
            ad[j][2] = d22 + d24;
            ad[j][6] = d22 - d24;
            d16 = d8 + d10;
            d18 = d10 + d12;
            d20 = d12 + d14;
            double d32 = (d16 - d20) * 0.38268343300000002D;
            double d26 = 0.54119609999999996D * d16 + d32;
            double d30 = 1.3065629649999999D * d20 + d32;
            double d28 = d18 * 0.70710678100000002D;
            double d34 = d14 + d28;
            double d36 = d14 - d28;
            ad[j][5] = d36 + d26;
            ad[j][3] = d36 - d26;
            ad[j][1] = d34 + d30;
            ad[j][7] = d34 - d30;
        }

        for(int k = 0; k < 8; k++)
        {
            double d1 = ad[0][k] + ad[7][k];
            double d15 = ad[0][k] - ad[7][k];
            double d3 = ad[1][k] + ad[6][k];
            double d13 = ad[1][k] - ad[6][k];
            double d5 = ad[2][k] + ad[5][k];
            double d11 = ad[2][k] - ad[5][k];
            double d7 = ad[3][k] + ad[4][k];
            double d9 = ad[3][k] - ad[4][k];
            double d17 = d1 + d7;
            double d23 = d1 - d7;
            double d19 = d3 + d5;
            double d21 = d3 - d5;
            ad[0][k] = d17 + d19;
            ad[4][k] = d17 - d19;
            double d25 = (d21 + d23) * 0.70710678100000002D;
            ad[2][k] = d23 + d25;
            ad[6][k] = d23 - d25;
            d17 = d9 + d11;
            d19 = d11 + d13;
            d21 = d13 + d15;
            double d33 = (d17 - d21) * 0.38268343300000002D;
            double d27 = 0.54119609999999996D * d17 + d33;
            double d31 = 1.3065629649999999D * d21 + d33;
            double d29 = d19 * 0.70710678100000002D;
            double d35 = d15 + d29;
            double d37 = d15 - d29;
            ad[5][k] = d37 + d27;
            ad[3][k] = d37 - d27;
            ad[1][k] = d35 + d31;
            ad[7][k] = d35 - d31;
        }

        return ad;
    }

    public double[][] forwardDCTExtreme(float af[][])
    {
        double ad[][] = new double[N][N];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                for(int k = 0; k < 8; k++)
                {
                    for(int l = 0; l < 8; l++)
                        ad[i][j] += (double)af[k][l] * Math.cos(((double)(2 * k + 1) * (double)j * 3.1415926535897931D) / 16D) * Math.cos(((double)(2 * l + 1) * (double)i * 3.1415926535897931D) / 16D);

                }

                ad[i][j] *= 0.25D * (j != 0 ? 1.0D : 1.0D / Math.sqrt(2D)) * (i != 0 ? 1.0D : 1.0D / Math.sqrt(2D));
            }

        }

        return ad;
    }

    public int[] quantizeBlock(double ad[][], int i)
    {
        int ai[] = new int[N * N];
        int l = 0;
        for(int j = 0; j < 8; j++)
        {
            for(int k = 0; k < 8; k++)
            {
                ai[l] = (int)Math.round(ad[j][k] * ((double[])Divisors[i])[l]);
                l++;
            }

        }

        return ai;
    }

    public int[] quantizeBlockExtreme(double ad[][], int i)
    {
        int ai[] = new int[N * N];
        int l = 0;
        for(int j = 0; j < 8; j++)
        {
            for(int k = 0; k < 8; k++)
            {
                ai[l] = (int)Math.round(ad[j][k] / (double)((int[])quantum[i])[l]);
                l++;
            }

        }

        return ai;
    }

    private void initMatrix(int i)
    {
        double ad[] = {
            1.0D, 1.3870398450000001D, 1.3065629649999999D, 1.1758756020000001D, 1.0D, 0.785694958D, 0.54119609999999996D, 0.275899379D
        };
        int i2 = i;
        if(i2 <= 0)
            i2 = 1;
        if(i2 > 100)
            i2 = 100;
        if(i2 < 50)
            i2 = 5000 / i2;
        else
            i2 = 200 - i2 * 2;
        quantum_luminance[0] = 16;
        quantum_luminance[1] = 11;
        quantum_luminance[2] = 10;
        quantum_luminance[3] = 16;
        quantum_luminance[4] = 24;
        quantum_luminance[5] = 40;
        quantum_luminance[6] = 51;
        quantum_luminance[7] = 61;
        quantum_luminance[8] = 12;
        quantum_luminance[9] = 12;
        quantum_luminance[10] = 14;
        quantum_luminance[11] = 19;
        quantum_luminance[12] = 26;
        quantum_luminance[13] = 58;
        quantum_luminance[14] = 60;
        quantum_luminance[15] = 55;
        quantum_luminance[16] = 14;
        quantum_luminance[17] = 13;
        quantum_luminance[18] = 16;
        quantum_luminance[19] = 24;
        quantum_luminance[20] = 40;
        quantum_luminance[21] = 57;
        quantum_luminance[22] = 69;
        quantum_luminance[23] = 56;
        quantum_luminance[24] = 14;
        quantum_luminance[25] = 17;
        quantum_luminance[26] = 22;
        quantum_luminance[27] = 29;
        quantum_luminance[28] = 51;
        quantum_luminance[29] = 87;
        quantum_luminance[30] = 80;
        quantum_luminance[31] = 62;
        quantum_luminance[32] = 18;
        quantum_luminance[33] = 22;
        quantum_luminance[34] = 37;
        quantum_luminance[35] = 56;
        quantum_luminance[36] = 68;
        quantum_luminance[37] = 109;
        quantum_luminance[38] = 103;
        quantum_luminance[39] = 77;
        quantum_luminance[40] = 24;
        quantum_luminance[41] = 35;
        quantum_luminance[42] = 55;
        quantum_luminance[43] = 64;
        quantum_luminance[44] = 81;
        quantum_luminance[45] = 104;
        quantum_luminance[46] = 113;
        quantum_luminance[47] = 92;
        quantum_luminance[48] = 49;
        quantum_luminance[49] = 64;
        quantum_luminance[50] = 78;
        quantum_luminance[51] = 87;
        quantum_luminance[52] = 103;
        quantum_luminance[53] = 121;
        quantum_luminance[54] = 120;
        quantum_luminance[55] = 101;
        quantum_luminance[56] = 72;
        quantum_luminance[57] = 92;
        quantum_luminance[58] = 95;
        quantum_luminance[59] = 98;
        quantum_luminance[60] = 112;
        quantum_luminance[61] = 100;
        quantum_luminance[62] = 103;
        quantum_luminance[63] = 99;
        for(int l = 0; l < 64; l++)
        {
            int j2 = (quantum_luminance[l] * i2 + 50) / 100;
            if(j2 <= 0)
                j2 = 1;
            if(j2 > 255)
                j2 = 255;
            quantum_luminance[l] = j2;
        }

        int l1 = 0;
        for(int j = 0; j < 8; j++)
        {
            for(int i1 = 0; i1 < 8; i1++)
            {
                DivisorsLuminance[l1] = 1.0D / ((double)quantum_luminance[l1] * ad[j] * ad[i1] * 8D);
                l1++;
            }

        }

        quantum_chrominance[0] = 17;
        quantum_chrominance[1] = 18;
        quantum_chrominance[2] = 24;
        quantum_chrominance[3] = 47;
        quantum_chrominance[4] = 99;
        quantum_chrominance[5] = 99;
        quantum_chrominance[6] = 99;
        quantum_chrominance[7] = 99;
        quantum_chrominance[8] = 18;
        quantum_chrominance[9] = 21;
        quantum_chrominance[10] = 26;
        quantum_chrominance[11] = 66;
        quantum_chrominance[12] = 99;
        quantum_chrominance[13] = 99;
        quantum_chrominance[14] = 99;
        quantum_chrominance[15] = 99;
        quantum_chrominance[16] = 24;
        quantum_chrominance[17] = 26;
        quantum_chrominance[18] = 56;
        quantum_chrominance[19] = 99;
        quantum_chrominance[20] = 99;
        quantum_chrominance[21] = 99;
        quantum_chrominance[22] = 99;
        quantum_chrominance[23] = 99;
        quantum_chrominance[24] = 47;
        quantum_chrominance[25] = 66;
        quantum_chrominance[26] = 99;
        quantum_chrominance[27] = 99;
        quantum_chrominance[28] = 99;
        quantum_chrominance[29] = 99;
        quantum_chrominance[30] = 99;
        quantum_chrominance[31] = 99;
        quantum_chrominance[32] = 99;
        quantum_chrominance[33] = 99;
        quantum_chrominance[34] = 99;
        quantum_chrominance[35] = 99;
        quantum_chrominance[36] = 99;
        quantum_chrominance[37] = 99;
        quantum_chrominance[38] = 99;
        quantum_chrominance[39] = 99;
        quantum_chrominance[40] = 99;
        quantum_chrominance[41] = 99;
        quantum_chrominance[42] = 99;
        quantum_chrominance[43] = 99;
        quantum_chrominance[44] = 99;
        quantum_chrominance[45] = 99;
        quantum_chrominance[46] = 99;
        quantum_chrominance[47] = 99;
        quantum_chrominance[48] = 99;
        quantum_chrominance[49] = 99;
        quantum_chrominance[50] = 99;
        quantum_chrominance[51] = 99;
        quantum_chrominance[52] = 99;
        quantum_chrominance[53] = 99;
        quantum_chrominance[54] = 99;
        quantum_chrominance[55] = 99;
        quantum_chrominance[56] = 99;
        quantum_chrominance[57] = 99;
        quantum_chrominance[58] = 99;
        quantum_chrominance[59] = 99;
        quantum_chrominance[60] = 99;
        quantum_chrominance[61] = 99;
        quantum_chrominance[62] = 99;
        quantum_chrominance[63] = 99;
        for(int j1 = 0; j1 < 64; j1++)
        {
            int k2 = (quantum_chrominance[j1] * i2 + 50) / 100;
            if(k2 <= 0)
                k2 = 1;
            if(k2 >= 255)
                k2 = 255;
            quantum_chrominance[j1] = k2;
        }

        l1 = 0;
        for(int k = 0; k < 8; k++)
        {
            for(int k1 = 0; k1 < 8; k1++)
            {
                DivisorsChrominance[l1] = 1.0D / ((double)quantum_chrominance[l1] * ad[k] * ad[k1] * 8D);
                l1++;
            }

        }

        quantum[0] = quantum_luminance;
        Divisors[0] = DivisorsLuminance;
        quantum[1] = quantum_chrominance;
        Divisors[1] = DivisorsChrominance;
    }

    public Object Divisors[];
    public int N;
    public int QUALITY;
    public Object quantum[];
    public int quantum_luminance[];
    public double DivisorsLuminance[];
    public int quantum_chrominance[];
    public double DivisorsChrominance[];
}
