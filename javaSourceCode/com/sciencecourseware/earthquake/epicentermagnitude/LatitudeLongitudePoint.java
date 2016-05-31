// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LatitudeLongitudePoint.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import electric1.xml.Element;
import electric1.xml.Parent;
import java.io.*;

public class LatitudeLongitudePoint
{

    public LatitudeLongitudePoint()
    {
        latDegrees = 0x7fffffff;
        latMinutes = 0x7fffffff;
        latDirection = "";
        lonDegrees = 0x7fffffff;
        lonMinutes = 0x7fffffff;
        lonDirection = "";
    }

    public LatitudeLongitudePoint(double d, double d1)
    {
        String as[] = convertFromDouble(d, true);
        latDegrees = Integer.parseInt(as[0]);
        latMinutes = Integer.parseInt(as[1]);
        latDirection = as[2];
        as = convertFromDouble(d1, false);
        lonDegrees = Integer.parseInt(as[0]);
        lonMinutes = Integer.parseInt(as[1]);
        lonDirection = as[2];
    }

    public LatitudeLongitudePoint(int i, int j, String s, int k, int l, String s1)
    {
        latDegrees = i;
        latMinutes = j;
        latDirection = s;
        lonDegrees = k;
        lonMinutes = l;
        lonDirection = s1;
    }

    public LatitudeLongitudePoint(Element element)
    {
        this(element.getElement("latitude"), element.getElement("longitude"));
    }

    public LatitudeLongitudePoint(Element element, Element element1)
    {
        try
        {
            latDegrees = Integer.parseInt(element.getElement("degrees").getTextString());
            latMinutes = Integer.parseInt(element.getElement("minutes").getTextString());
            latDirection = element.getElement("direction").getTextString();
            lonDegrees = Integer.parseInt(element1.getElement("degrees").getTextString());
            lonMinutes = Integer.parseInt(element1.getElement("minutes").getTextString());
            lonDirection = element1.getElement("direction").getTextString();
        }
        catch(NumberFormatException numberformatexception)
        {
            latDegrees = 0;
            latMinutes = 0;
            latDirection = "N";
            lonDegrees = 0;
            lonMinutes = 0;
            lonDirection = WEST_ABBREV;
        }
    }

    public LatitudeLongitudePoint(DataInputStream datainputstream)
        throws IOException
    {
        latDegrees = datainputstream.readInt();
        latMinutes = datainputstream.readInt();
        char c = datainputstream.readChar();
        if(c == '?')
        {
            latDirection = "";
        } else
        {
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(c);
            latDirection = stringbuffer.toString();
        }
        lonDegrees = datainputstream.readInt();
        lonMinutes = datainputstream.readInt();
        c = datainputstream.readChar();
        if(c == '?')
        {
            lonDirection = "";
        } else
        {
            StringBuffer stringbuffer1 = new StringBuffer();
            stringbuffer1.append(c);
            lonDirection = stringbuffer1.toString();
        }
    }

    public static String[] convertFromDouble(double d, boolean flag)
    {
        String as[] = new String[3];
        if(d < 0.0D)
        {
            if(flag)
                as[2] = "S";
            else
                as[2] = WEST_ABBREV;
        } else
        if(flag)
            as[2] = "N";
        else
            as[2] = "E";
        d = Math.abs(d);
        int i = (int)d;
        as[0] = Integer.toString(i);
        d -= i;
        as[1] = Integer.toString((int)Math.round(d * 60D));
        return as;
    }

    public static double convertToDouble(int i, int j, String s)
    {
        double d = i;
        d += (double)j / 60D;
        if(s.equalsIgnoreCase("S") || s.equalsIgnoreCase("W"))
            d = -d;
        return d;
    }

    public void setLatitude(double d)
    {
        String as[] = convertFromDouble(d, true);
        latDegrees = Integer.parseInt(as[0]);
        latMinutes = Integer.parseInt(as[1]);
        latDirection = as[2];
    }

    public void setLatitude(int i, int j, String s)
    {
        latDegrees = i;
        latMinutes = j;
        latDirection = s;
    }

    public String[] getLatitude()
    {
        String as[] = new String[3];
        as[0] = Integer.toString(latDegrees);
        as[1] = Integer.toString(latMinutes);
        as[2] = latDirection;
        return as;
    }

    public int getLatitudeDegrees()
    {
        return latDegrees;
    }

    public String getLatitudeDirection()
    {
        return latDirection;
    }

    public double getLatitudeDouble()
    {
        return convertToDouble(latDegrees, latMinutes, latDirection);
    }

    public int getLatitudeMinutes()
    {
        return latMinutes;
    }

    public void setLongitude(double d)
    {
        String as[] = convertFromDouble(d, false);
        lonDegrees = Integer.parseInt(as[0]);
        lonMinutes = Integer.parseInt(as[1]);
        lonDirection = as[2];
    }

    public void setLongitude(int i, int j, String s)
    {
        lonDegrees = i;
        lonMinutes = j;
        lonDirection = s;
    }

    public String[] getLongitude()
    {
        String as[] = new String[3];
        as[0] = Integer.toString(lonDegrees);
        as[1] = Integer.toString(lonMinutes);
        as[2] = lonDirection;
        return as;
    }

    public int getLongitudeDegrees()
    {
        return lonDegrees;
    }

    public String getLongitudeDirection()
    {
        return lonDirection;
    }

    public double getLongitudeDouble()
    {
        return convertToDouble(lonDegrees, lonMinutes, lonDirection);
    }

    public int getLongitudeMinutes()
    {
        return lonMinutes;
    }

    public void addDataToXML(Element element)
    {
        Element element1 = new Element("latitude");
        element1.addElement(new Element("degrees")).setText(Integer.toString(latDegrees));
        element1.addElement(new Element("minutes")).setText(Integer.toString(latMinutes));
        Element element2 = new Element("direction");
        if(latDirection.length() == 0)
            element2.setText("?");
        else
            element2.setText("" + latDirection.charAt(0));
        element1.addElement(element2);
        Element element3 = new Element("longitude");
        element3.addElement(new Element("degrees")).setText(Integer.toString(lonDegrees));
        element3.addElement(new Element("minutes")).setText(Integer.toString(lonMinutes));
        element2 = new Element("direction");
        if(lonDirection.length() == 0)
            element2.setText("?");
        else
            element2.setText("" + lonDirection.charAt(0));
        element3.addElement(element2);
        element.addElement(element1);
        element.addElement(element3);
    }

    public String latitudeToString()
    {
        String s = latDegrees + "\260 " + latMinutes + "' " + latDirection;
        return s;
    }

    public String longitudeToString()
    {
        String s = lonDegrees + "  " + lonMinutes + "' " + lonDirection;
        return s;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("latitude ");
        stringbuffer.append(latitudeToString());
        stringbuffer.append(" longitude ");
        stringbuffer.append(longitudeToString());
        return stringbuffer.toString();
    }

    public void writeDataToStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(latDegrees);
        dataoutputstream.writeInt(latMinutes);
        if(latDirection.length() == 0)
            dataoutputstream.writeChar(63);
        else
            dataoutputstream.writeChar(latDirection.charAt(0));
        dataoutputstream.writeInt(lonDegrees);
        dataoutputstream.writeInt(lonMinutes);
        if(lonDirection.length() == 0)
            dataoutputstream.writeChar(63);
        else
            dataoutputstream.writeChar(lonDirection.charAt(0));
    }

    public static String WEST_ABBREV = "W";
    private String latDirection;
    private String lonDirection;
    private int latDegrees;
    private int latMinutes;
    private int lonDegrees;
    private int lonMinutes;

}
