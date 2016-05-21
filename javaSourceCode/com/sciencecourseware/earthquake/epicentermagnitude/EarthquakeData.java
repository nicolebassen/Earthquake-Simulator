// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EarthquakeData.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import electric1.util.Nodes;
import electric1.xml.*;
import java.io.PrintStream;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            LatitudeLongitudePoint

public class EarthquakeData
{

    public EarthquakeData()
    {
        this("", "", 0.0D, 0, 0, "N", 0, 0, "W");
    }

    public EarthquakeData(Element element)
    {
        stationLocations = null;
        stationNames = null;
        earthquakeName = element.getElement("name").getTextString();
        try
        {
            Element element1 = element.getElement("magnitude");
            if(element1 != null)
            {
                String s = element1.getTextString();
                magnitude = Double.valueOf(s).doubleValue();
            }
        }
        catch(NumberFormatException numberformatexception) { }
        Element element2 = element.getElement("location");
        if(element2 != null)
        {
            locationName = element2.getElement("name").getTextString();
            location = new LatitudeLongitudePoint(element2.getElement("latitude"), element2.getElement("longitude"));
        } else
        {
            location = null;
        }
        Element element3 = element.getElement("map");
        mapName = element3.getElement("name").getTextString();
        buttonBackgroundName = element3.getElement("buttonbackgroundname").getTextString();
        upperRight = new LatitudeLongitudePoint(element3.getElement("upperlatitude"), element3.getElement("rightlongitude"));
        lowerLeft = new LatitudeLongitudePoint(element3.getElement("lowerlatitude"), element3.getElement("leftlongitude"));
        try
        {
            String s1 = element3.getElement("scale").getTextString();
            mapScale = Double.valueOf(s1).doubleValue();
        }
        catch(NumberFormatException numberformatexception1)
        {
            mapScale = 1.0D;
        }
        loadStations(element.getElements("station"));
    }

    public EarthquakeData(String s, String s1, double d, int i, int j, String s2, 
            int k, int l, String s3)
    {
        stationLocations = null;
        stationNames = null;
        earthquakeName = s;
        locationName = s1;
        magnitude = d;
        location = new LatitudeLongitudePoint(i, j, s2, k, l, s3);
    }

    public String getButtonBackgroundName()
    {
        return buttonBackgroundName;
    }

    public String getEarthquakeName()
    {
        return earthquakeName;
    }

    public LatitudeLongitudePoint getLocation()
    {
        return location;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public LatitudeLongitudePoint getMapLowerLeft()
    {
        return lowerLeft;
    }

    public String getMapName()
    {
        return mapName;
    }

    public double getMapScale()
    {
        return mapScale;
    }

    public LatitudeLongitudePoint getMapUpperRight()
    {
        return upperRight;
    }

    public void setStationLocations(LatitudeLongitudePoint alatitudelongitudepoint[])
    {
        stationLocations = alatitudelongitudepoint;
    }

    public LatitudeLongitudePoint[] getStationLocations()
    {
        return stationLocations;
    }

    public void setStationNames(String as[])
    {
        stationNames = as;
    }

    public String[] getStationNames()
    {
        return stationNames;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("name=");
        stringbuffer.append(earthquakeName);
        stringbuffer.append(";location name=");
        stringbuffer.append(locationName);
        stringbuffer.append(";location=");
        stringbuffer.append(location.toString());
        stringbuffer.append(";map name=");
        stringbuffer.append(mapName);
        stringbuffer.append(";map scale=");
        stringbuffer.append(mapScale);
        stringbuffer.append(";upperRight=");
        stringbuffer.append(upperRight.toString());
        stringbuffer.append(";lowerLeft=");
        stringbuffer.append(lowerLeft.toString());
        return stringbuffer.toString();
    }

    protected void loadStations(Elements elements)
    {
        int i = elements.size();
        System.out.println("There are " + i + " stations.");
        stationNames = new String[i];
        stationLocations = new LatitudeLongitudePoint[i];
        elements.reset();
        for(int j = 0; j < i; j++)
        {
            Element element = elements.next();
            if(element != null)
            {
                stationNames[j] = element.getAttributeValue("name");
                stationLocations[j] = new LatitudeLongitudePoint(element.getElement("latitude"), element.getElement("longitude"));
            }
        }

    }

    public LatitudeLongitudePoint stationLocations[];
    private LatitudeLongitudePoint location;
    private LatitudeLongitudePoint lowerLeft;
    private LatitudeLongitudePoint upperRight;
    private String buttonBackgroundName;
    private String earthquakeName;
    private String locationName;
    private String mapName;
    private String stationNames[];
    private double magnitude;
    private double mapScale;
}
