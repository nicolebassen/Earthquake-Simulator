// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PointData.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import java.io.*;

public class PointData
{

    public PointData()
    {
        preset = false;
        distance = 0.0D;
        spInterval = 0.0D;
        tableX = -1;
        tableY = -1;
        x = -1;
        y = -1;
    }

    public PointData(double d, double d1)
    {
        this(d, d1, false);
    }

    public PointData(double d, double d1, boolean flag)
    {
        preset = false;
        distance = 0.0D;
        spInterval = 0.0D;
        tableX = -1;
        tableY = -1;
        x = -1;
        y = -1;
        setPreset(flag);
        setDistance(d);
        setSPInterval(d1);
    }

    public PointData(DataInputStream datainputstream)
        throws IOException
    {
        preset = false;
        distance = 0.0D;
        spInterval = 0.0D;
        tableX = -1;
        tableY = -1;
        x = -1;
        y = -1;
        System.out.println("-------------- Start of PointData.PointData (read data) ----------------");
        System.out.println("distance = dis.readDouble();");
        distance = datainputstream.readDouble();
        System.out.println("spInterval = dis.readDouble();");
        spInterval = datainputstream.readDouble();
        System.out.println("x = dis.readInt();");
        x = datainputstream.readInt();
        System.out.println("y = dis.readInt();");
        y = datainputstream.readInt();
        System.out.println("tableX = dis.readInt();");
        tableX = datainputstream.readInt();
        System.out.println("tableY = dis.readInt();");
        tableY = datainputstream.readInt();
        System.out.println("preset = dis.readBoolean();");
        preset = datainputstream.readBoolean();
        System.out.println("-------------- End of PointData.PointData (read data) ----------------");
    }

    public void setDistance(double d)
    {
        distance = d;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setPreset(boolean flag)
    {
        preset = flag;
    }

    public boolean isPreset()
    {
        return preset;
    }

    public void setSPInterval(double d)
    {
        spInterval = d;
    }

    public double getSPInterval()
    {
        return spInterval;
    }

    public void setTableXY(int i, int j)
    {
        tableX = i;
        tableY = j;
    }

    public int getX()
    {
        if(x < 0)
            return tableX;
        else
            return x;
    }

    public void setXY(int i, int j)
    {
        x = i;
        y = j;
    }

    public int getY()
    {
        if(y < 0)
            return tableY;
        else
            return y;
    }

    public void writeDataToStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeDouble(distance);
        dataoutputstream.writeDouble(spInterval);
        dataoutputstream.writeInt(x);
        dataoutputstream.writeInt(y);
        dataoutputstream.writeInt(tableX);
        dataoutputstream.writeInt(tableY);
        dataoutputstream.writeBoolean(preset);
    }

    private boolean preset;
    private double distance;
    private double spInterval;
    private int tableX;
    private int tableY;
    private int x;
    private int y;
}
