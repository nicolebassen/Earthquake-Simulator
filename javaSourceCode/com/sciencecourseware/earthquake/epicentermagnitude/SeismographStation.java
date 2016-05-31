// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeismographStation.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import com.sciencecourseware.components.StringServer;
import electric1.xml.Element;
import electric1.xml.Parent;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Random;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            EpicenterMagnitude

public class SeismographStation
{

    public SeismographStation(int i, int j, int k, NumberFormat numberformat, double d, StringServer stringserver)
    {
        magnitude = 5D;
        scaleFactor = 1.0D;
        bColor = Color.white;
        distanceTextColor = Color.white;
        normalColor = Color.red;
        seismogramBackgroundColor = bColor;
        selectedColor = null;
        spMeasuredColor = Color.blue;
        distanceTextFont = null;
        fm = null;
        chartImage = null;
        measuredDistanceRadiusString = null;
        stationName = "Station";
        mouseOver = false;
        selected = false;
        showingDistance = false;
        a = 1.0D;
        b = 1.0D;
        distance = 0.0D;
        duration = 1000D;
        heightFactor = 50D;
        lastTime = 0.0D;
        lastY = 0.0D;
        maxDistance = 0.0D;
        pAmplitude = 0.0D;
        sAmplitude = 0.0D;
        scaleX = 1.0D;
        spInterval = 0.0D;
        xOn = 0.0D;
        lastCosSign = 1;
        lastYSign = 1;
        measuredDistanceRadius = 0x7fffffff;
        myHeight = 10;
        scale = 100;
        randomSeed = 100L;
        stringServer = null;
        number = i;
        stringServer = stringserver;
        spDraggingBegin = 0x7fffffff;
        spDraggingEnd = 0x7fffffff;
        ampDraggingX = 0x7fffffff;
        ampDraggingY = 0x7fffffff;
        distanceFormat = numberformat;
        initialX = j;
        initialY = k;
        setXY(j, k);
        magnitude = d;
    }

    public SeismographStation(Element element, NumberFormat numberformat, Component component, StringServer stringserver)
    {
        magnitude = 5D;
        scaleFactor = 1.0D;
        bColor = Color.white;
        distanceTextColor = Color.white;
        normalColor = Color.red;
        seismogramBackgroundColor = bColor;
        selectedColor = null;
        spMeasuredColor = Color.blue;
        distanceTextFont = null;
        fm = null;
        chartImage = null;
        measuredDistanceRadiusString = null;
        stationName = "Station";
        mouseOver = false;
        selected = false;
        showingDistance = false;
        a = 1.0D;
        b = 1.0D;
        distance = 0.0D;
        duration = 1000D;
        heightFactor = 50D;
        lastTime = 0.0D;
        lastY = 0.0D;
        maxDistance = 0.0D;
        pAmplitude = 0.0D;
        sAmplitude = 0.0D;
        scaleX = 1.0D;
        spInterval = 0.0D;
        xOn = 0.0D;
        lastCosSign = 1;
        lastYSign = 1;
        measuredDistanceRadius = 0x7fffffff;
        myHeight = 10;
        scale = 100;
        randomSeed = 100L;
        stringServer = null;
        stringServer = stringserver;
        Element element1 = element.getElement("name");
        if(element1 != null)
            stationName = element1.getTextString();
        distanceFormat = numberformat;
        element1 = element.getElement("x");
        if(element1 != null)
            try
            {
                x = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception) { }
        element1 = element.getElement("y");
        if(element1 != null)
            try
            {
                y = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception1) { }
        element1 = element.getElement("initialx");
        if(element1 != null)
            try
            {
                initialX = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception2) { }
        element1 = element.getElement("initialy");
        if(element1 != null)
            try
            {
                initialY = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception3) { }
        element1 = element.getElement("number");
        if(element1 != null)
            try
            {
                number = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception4) { }
        element1 = element.getElement("selected");
        if(element1 != null)
            selected = Boolean.valueOf(element1.getTextString()).booleanValue();
        element1 = element.getElement("showingdistance");
        if(element1 != null)
            showingDistance = Boolean.valueOf(element1.getTextString()).booleanValue();
        element1 = element.getElement("distance");
        if(element1 != null)
            try
            {
                distance = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception5) { }
        element1 = element.getElement("spinterval");
        if(element1 != null)
            try
            {
                spInterval = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception6) { }
        element1 = element.getElement("maxdistance");
        if(element1 != null)
            try
            {
                maxDistance = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception7) { }
        element1 = element.getElement("duration");
        if(element1 != null)
            try
            {
                duration = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception8) { }
        element1 = element.getElement("pamplitude");
        if(element1 != null)
            try
            {
                pAmplitude = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception9) { }
        element1 = element.getElement("samplitude");
        if(element1 != null)
            try
            {
                sAmplitude = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception10) { }
        element1 = element.getElement("scale");
        if(element1 != null)
            try
            {
                scale = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception11) { }
        element1 = element.getElement("randomseed");
        if(element1 != null)
            try
            {
                randomSeed = Long.parseLong(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception12) { }
        element1 = element.getElement("spdraggingbegin");
        if(element1 != null)
            try
            {
                spDraggingBegin = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception13) { }
        element1 = element.getElement("spdraggingend");
        if(element1 != null)
            try
            {
                spDraggingEnd = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception14) { }
        element1 = element.getElement("ampdraggingx");
        if(element1 != null)
            try
            {
                ampDraggingX = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception15) { }
        element1 = element.getElement("ampdraggingy");
        if(element1 != null)
            try
            {
                ampDraggingY = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception16) { }
        element1 = element.getElement("scalefactor");
        if(element1 != null)
            try
            {
                scaleFactor = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception17) { }
        element1 = element.getElement("scalex");
        if(element1 != null)
            try
            {
                scaleX = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception18) { }
        element1 = element.getElement("chart");
        boolean flag;
        int i;
        int j;
        if(element1 != null)
        {
            flag = true;
            try
            {
                i = Integer.parseInt(element1.getAttributeValue("width"));
                j = Integer.parseInt(element1.getAttributeValue("height"));
            }
            catch(NumberFormatException numberformatexception19)
            {
                flag = false;
                i = 0;
                j = 0;
            }
        } else
        {
            flag = false;
            i = 0;
            j = 0;
        }
        element1 = element.getElement("measureddistanceradius");
        if(element1 != null)
            try
            {
                measuredDistanceRadius = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception20) { }
        element1 = element.getElement("measureddistanceradiusstring");
        if(element1 != null)
            measuredDistanceRadiusString = element1.getTextString();
        element1 = element.getElement("magnitude");
        if(element1 != null)
            try
            {
                magnitude = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception21) { }
        element1 = element.getElement("myheight");
        if(element1 != null)
            try
            {
                myHeight = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception22) { }
        element1 = element.getElement("heightfactor");
        if(element1 != null)
            try
            {
                heightFactor = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception23) { }
        if(flag)
            setupChartImage(i, j, distance, maxDistance, component, scale, randomSeed, false);
    }

    public SeismographStation(DataInputStream datainputstream, NumberFormat numberformat, Component component, StringServer stringserver)
        throws IOException
    {
        magnitude = 5D;
        scaleFactor = 1.0D;
        bColor = Color.white;
        distanceTextColor = Color.white;
        normalColor = Color.red;
        seismogramBackgroundColor = bColor;
        selectedColor = null;
        spMeasuredColor = Color.blue;
        distanceTextFont = null;
        fm = null;
        chartImage = null;
        measuredDistanceRadiusString = null;
        stationName = "Station";
        mouseOver = false;
        selected = false;
        showingDistance = false;
        a = 1.0D;
        b = 1.0D;
        distance = 0.0D;
        duration = 1000D;
        heightFactor = 50D;
        lastTime = 0.0D;
        lastY = 0.0D;
        maxDistance = 0.0D;
        pAmplitude = 0.0D;
        sAmplitude = 0.0D;
        scaleX = 1.0D;
        spInterval = 0.0D;
        xOn = 0.0D;
        lastCosSign = 1;
        lastYSign = 1;
        measuredDistanceRadius = 0x7fffffff;
        myHeight = 10;
        scale = 100;
        randomSeed = 100L;
        stringServer = null;
        stringServer = stringserver;
        stationName = datainputstream.readUTF();
        distanceFormat = numberformat;
        x = datainputstream.readInt();
        y = datainputstream.readInt();
        initialX = datainputstream.readInt();
        initialY = datainputstream.readInt();
        number = datainputstream.readInt();
        selected = datainputstream.readBoolean();
        showingDistance = datainputstream.readBoolean();
        distance = datainputstream.readDouble();
        spInterval = datainputstream.readDouble();
        maxDistance = datainputstream.readDouble();
        duration = datainputstream.readDouble();
        pAmplitude = datainputstream.readDouble();
        sAmplitude = datainputstream.readDouble();
        scale = datainputstream.readInt();
        randomSeed = datainputstream.readLong();
        spDraggingBegin = datainputstream.readInt();
        spDraggingEnd = datainputstream.readInt();
        ampDraggingX = datainputstream.readInt();
        ampDraggingY = datainputstream.readInt();
        scaleFactor = datainputstream.readDouble();
        scaleX = datainputstream.readDouble();
        boolean flag = datainputstream.readBoolean();
        int i = datainputstream.readInt();
        int j = datainputstream.readInt();
        measuredDistanceRadius = datainputstream.readInt();
        measuredDistanceRadiusString = datainputstream.readUTF();
        magnitude = datainputstream.readDouble();
        myHeight = datainputstream.readInt();
        heightFactor = datainputstream.readDouble();
        if(flag)
            setupChartImage(i, j, distance, maxDistance, component, scale, randomSeed, false);
    }

    public double getActualSPInterval()
    {
        return spInterval;
    }

    public Image getChartImage()
    {
        if(chartImage == null)
            System.out.println("chartImage was null.");
        return chartImage;
    }

    public void setDistance(double d, double d1)
    {
        distance = d;
        maxDistance = d1;
        spInterval = 0.23200000000000001D * d - 3.0109799999999999E-06D * d * d - (0.12889999999999999D * d - 1.44672E-06D * d * d);
        pAmplitude = computePAmplitude(d, magnitude);
        sAmplitude = pAmplitude / 5D;
        duration = spInterval * ((d1 * 3D) / d);
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistanceTextColor(Color color)
    {
        if(color != null)
            distanceTextColor = color;
    }

    public void setDistanceTextFont(Font font1)
    {
        distanceTextFont = font1;
    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public void setMeasuredDistanceRadius(int i, String s)
    {
        measuredDistanceRadius = i;
        measuredDistanceRadiusString = s;
    }

    public int getMeasuredDistanceRadius()
    {
        return measuredDistanceRadius;
    }

    public String getMeasuredDistanceRadiusString()
    {
        return measuredDistanceRadiusString;
    }

    public void setMouseOver(boolean flag)
    {
        mouseOver = flag;
    }

    public boolean isMouseOver()
    {
        return mouseOver;
    }

    public boolean isMovedFromInitialPoint()
    {
        return true;
    }

    public void setNormalColor(Color color)
    {
        normalColor = color;
        if(selectedColor == null)
            selectedColor = normalColor.brighter();
    }

    public Color getNormalColor()
    {
        return normalColor;
    }

    public int getNumber()
    {
        return number;
    }

    public double getPAmplitude()
    {
        return pAmplitude;
    }

    public int getPWaveMax()
    {
        return (int)Math.round(((spInterval + distance / 10D + 3D) * (double)getChartImage().getWidth(c)) / duration);
    }

    public double getSPInterval()
    {
        double d = (double)Math.abs(spDraggingBegin - spDraggingEnd) / scaleX;
        return d;
    }

    public int getSWaveStart()
    {
        return (int)Math.round(((distance / 10D) * (double)getChartImage().getWidth(c)) / duration);
    }

    public int getScale()
    {
        return scale;
    }

    public void setSelected(boolean flag)
    {
        selected = flag;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelectedColor(Color color)
    {
        selectedColor = color;
    }

    public Color getSelectedColor()
    {
        return selectedColor;
    }

    public void setShowingDistance(boolean flag)
    {
        setShowingDistance(flag, 1.7976931348623157E+308D, 1.7976931348623157E+308D);
    }

    public void setShowingDistance(boolean flag, double d, double d1)
    {
        showingDistance = flag;
        if(d != 1.7976931348623157E+308D)
            setDistance(d, d1);
    }

    public boolean isShowingDistance()
    {
        return showingDistance;
    }

    public void setStationName(String s)
    {
        stationName = s;
    }

    public String getStationName()
    {
        return stationName;
    }

    public int getX()
    {
        return x;
    }

    public void setXY(int i, int j)
    {
        x = i;
        y = j;
    }

    public int getY()
    {
        return y;
    }

    public void addDataToXML(Element element)
    {
        String s;
        if(stationName == null)
            s = "";
        else
            s = stationName;
        element.addElement(new Element("x")).setText(Integer.toString(x));
        element.addElement(new Element("y")).setText(Integer.toString(y));
        element.addElement(new Element("initialx")).setText(Integer.toString(initialX));
        element.addElement(new Element("initialy")).setText(Integer.toString(initialY));
        element.addElement(new Element("number")).setText(Integer.toString(number));
        element.addElement(new Element("selected")).setText(EpicenterMagnitude.booleanToString(selected));
        s = EpicenterMagnitude.booleanToString(showingDistance);
        element.addElement(new Element("showingdistance")).setText(s);
        element.addElement(new Element("distance")).setText(Double.toString(distance));
        element.addElement(new Element("spinterval")).setText(Double.toString(spInterval));
        element.addElement(new Element("maxdistance")).setText(Double.toString(maxDistance));
        element.addElement(new Element("duration")).setText(Double.toString(duration));
        element.addElement(new Element("pamplitude")).setText(Double.toString(pAmplitude));
        element.addElement(new Element("samplitude")).setText(Double.toString(sAmplitude));
        element.addElement(new Element("scale")).setText(Integer.toString(scale));
        element.addElement(new Element("randomseed")).setText(Long.toString(randomSeed));
        element.addElement(new Element("spdraggingbegin")).setText(Integer.toString(spDraggingBegin));
        element.addElement(new Element("spdraggingend")).setText(Integer.toString(spDraggingEnd));
        element.addElement(new Element("ampdraggingx")).setText(Integer.toString(ampDraggingX));
        element.addElement(new Element("ampdraggingy")).setText(Integer.toString(ampDraggingY));
        element.addElement(new Element("scalefactor")).setText(Double.toString(scaleFactor));
        element.addElement(new Element("scalex")).setText(Double.toString(scaleX));
        if(chartImage != null)
        {
            Element element1 = new Element("chart");
            element1.setAttribute("width", Integer.toString(chartImage.getWidth(c)));
            element1.setAttribute("height", Integer.toString(chartImage.getHeight(c)));
            element.addElement(element1);
        }
        s = Integer.toString(measuredDistanceRadius);
        element.addElement(new Element("measureddistanceradius")).setText(s);
        if(measuredDistanceRadiusString == null)
            measuredDistanceRadiusString = Integer.toString(measuredDistanceRadius);
        element.addElement(new Element("measureddistanceradiusstring")).setText(measuredDistanceRadiusString);
        element.addElement(new Element("magnitude")).setText(Double.toString(magnitude));
        element.addElement(new Element("myheight")).setText(Integer.toString(myHeight));
        element.addElement(new Element("heightfactor")).setText(Double.toString(heightFactor));
    }

    public boolean checkAmplitudeSnapTo()
    {
        int i = (int)Math.round(((pAmplitude * (double)myHeight) / heightFactor) * scaleFactor) + myHeight / 2;
        int j = -(int)Math.round(((pAmplitude * (double)myHeight) / heightFactor) * scaleFactor) + myHeight / 2;
        if(Math.abs(ampDraggingY - i) < 3 || Math.abs(ampDraggingY - j) < 3)
        {
            if(ampDraggingY > myHeight / 2)
                ampDraggingY = i;
            else
                ampDraggingY = j;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean checkSPSnapTo()
    {
        int i = (int)Math.round((distance / 10D) * scaleX);
        int j = (int)Math.round((spInterval + distance / 10D) * scaleX);
        if((Math.abs(i - spDraggingBegin) < 5 || Math.abs(j - spDraggingBegin) < 5) && Math.abs(Math.abs(j - i) - Math.abs(spDraggingEnd - spDraggingBegin)) < 5)
        {
            spDraggingEnd = j;
            spDraggingBegin = i;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean contains(int i, int j)
    {
        return i >= x - 8 && i < x + 8 && j >= y - 8 && j < y + 8;
    }

    public void paintSeismogram(Graphics g, int i, int j)
    {
    }

    public void paintStation(Graphics g, int i, int j, int k, int l)
    {
        Color color;
        if(selected)
        {
            if(selectedColor == null)
                color = normalColor.brighter();
            else
                color = selectedColor;
        } else
        if(spDraggingBegin != 0x7fffffff && spDraggingEnd != 0x7fffffff && ampDraggingY != 0x7fffffff)
            color = spMeasuredColor;
        else
            color = normalColor;
        g.setColor(color);
        g.fillRect((x + i) - 8, (y + j) - 8, 16, 16);
        g.setFont(font);
        Color color1 = new Color(255 - distanceTextColor.getRed(), 255 - distanceTextColor.getGreen(), 255 - distanceTextColor.getBlue());
        if(color == spMeasuredColor)
            g.setColor(distanceTextColor);
        else
            g.setColor(color1);
        if(fm == null)
            fm = g.getFontMetrics();
        String s = Integer.toString(number);
        int i1 = fm.stringWidth(s);
        g.drawString(s, (x + i + (16 - i1) / 2) - 8, (y + j + (16 + fm.getHeight()) / 2) - 2 - 8);
        if(mouseOver || measuredDistanceRadius != 0x7fffffff && (l == 10 || l == 11))
        {
            String s1 = "";
            g.setFont(distanceTextFont);
            FontMetrics fontmetrics = g.getFontMetrics();
            if(mouseOver)
            {
                s1 = s1 + stringServer.getString("seismograph_station_num") + getNumber();
                if(measuredDistanceRadiusString != null && (l == 10 || l == 11))
                    s1 = s1 + " (";
            }
            if(measuredDistanceRadiusString != null && (l == 10 || l == 11))
            {
                s1 = s1 + measuredDistanceRadiusString + " km";
                if(mouseOver)
                    s1 = s1 + ")";
            }
            int j1 = fontmetrics.stringWidth(s1);
            if(j1 + x + 16 + 2 <= k)
            {
                g.setColor(color1);
                g.fillRect(x + i + 16, (y + j + (16 + fontmetrics.getHeight()) / 2) - 4 - 8 - fontmetrics.getMaxAscent(), j1 + 4, fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + 4);
                g.setColor(distanceTextColor);
                g.drawString(s1, x + i + 16 + 2, (y + j + (16 + fontmetrics.getHeight()) / 2) - 2 - 8);
            } else
            {
                g.setColor(color1);
                g.fillRect((x + i) - 16 - 6 - j1, (y + j + (16 + fontmetrics.getHeight()) / 2) - 4 - 8 - fontmetrics.getMaxAscent(), j1 + 4, fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + 4);
                g.setColor(distanceTextColor);
                g.drawString(s1, (x + i) - 16 - 4 - j1, (y + j + (16 + fontmetrics.getHeight()) / 2) - 2 - 8);
            }
        }
    }

    public void resetToInitialPoint()
    {
        x = initialX;
        y = initialY;
    }

    public boolean setupChartImage(int i, int j, double d, double d1, Component component, 
            int k)
    {
        return setupChartImage(i, j, d, d1, component, k, 0xffffffffL);
    }

    public boolean setupChartImage(int i, int j, double d, double d1, Component component, 
            int k, long l)
    {
        return setupChartImage(i, j, d, d1, component, k, l, true);
    }

    public boolean setupChartImage(int i, int j, double d, double d1, Component component, 
            int k, long l, boolean flag)
    {
        c = component;
        myHeight = j;
        boolean flag1 = false;
        if(l != 0xffffffffL)
            randomSeed = l;
        r = new Random(randomSeed);
        scale = k;
        double d2 = 0.0D;
        if(flag && ampDraggingY != 0x7fffffff)
            d2 = (double)(ampDraggingY - j / 2) / scaleFactor;
        scaleFactor = (double)k / 100D;
        if(flag && ampDraggingY != 0x7fffffff)
            ampDraggingY = (int)Math.round(d2 * scaleFactor) + j / 2;
        setDistance(d, d1);
        if(chartImage == null || chartImage.getWidth(component) != i || chartImage.getHeight(component) != j)
            chartImage = component.createImage(i, j);
        if(chartImage != null)
        {
            Graphics g = chartImage.getGraphics();
            if(g != null)
            {
                g.setColor(seismogramBackgroundColor);
                g.fillRect(0, 0, i, j);
                g.setColor(Color.black);
                scaleX = (double)i / duration;
                int i1 = 0x7fffffff;
                int j1 = 0x7fffffff;
                for(double d3 = 0.0D; d3 <= duration * 1.0D; d3 += 0.050000000000000003D)
                {
                    int k1 = j / 2 - (int)Math.round(computeNextStep(d3, j) * scaleFactor);
                    int i2 = (int)Math.round(d3 * scaleX);
                    if(j1 != 0x7fffffff)
                    {
                        g.setColor(Color.black);
                        g.drawLine(j1, i1, i2, k1);
                    }
                    j1 = i2;
                    i1 = k1;
                    if(k1 < 0 || k1 > j - 1)
                        flag1 = true;
                }

                g.setFont(bigFont);
                FontMetrics fontmetrics = g.getFontMetrics();
                String s = stringServer.getString("station_number") + getNumber();
                int l1 = fontmetrics.stringWidth(s);
                g.setColor(bColor);
                g.drawRect(2, 2, l1 + 8, fontmetrics.getHeight() + 4);
                g.setColor(Color.black);
                g.drawString(s, 4, 2 + fontmetrics.getAscent());
                g.dispose();
            } else
            {
                System.out.println("g was null.");
            }
        } else
        {
            System.out.println("chartImage was still null.");
        }
        return flag1;
    }

    public boolean setupChartImage(int i, int j, Component component)
    {
        return setupChartImage(i, j, distance, maxDistance, component, scale);
    }

    public void writeDataToStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        if(stationName == null)
            dataoutputstream.writeUTF("");
        else
            dataoutputstream.writeUTF(stationName);
        dataoutputstream.writeInt(x);
        dataoutputstream.writeInt(y);
        dataoutputstream.writeInt(initialX);
        dataoutputstream.writeInt(initialY);
        dataoutputstream.writeInt(number);
        dataoutputstream.writeBoolean(selected);
        dataoutputstream.writeBoolean(showingDistance);
        dataoutputstream.writeDouble(distance);
        dataoutputstream.writeDouble(spInterval);
        dataoutputstream.writeDouble(maxDistance);
        dataoutputstream.writeDouble(duration);
        dataoutputstream.writeDouble(pAmplitude);
        dataoutputstream.writeDouble(sAmplitude);
        dataoutputstream.writeInt(scale);
        dataoutputstream.writeLong(randomSeed);
        dataoutputstream.writeInt(spDraggingBegin);
        dataoutputstream.writeInt(spDraggingEnd);
        dataoutputstream.writeInt(ampDraggingX);
        dataoutputstream.writeInt(ampDraggingY);
        dataoutputstream.writeDouble(scaleFactor);
        dataoutputstream.writeDouble(scaleX);
        if(chartImage == null)
        {
            dataoutputstream.writeBoolean(false);
            dataoutputstream.writeInt(0);
            dataoutputstream.writeInt(0);
        } else
        {
            dataoutputstream.writeBoolean(true);
            dataoutputstream.writeInt(chartImage.getWidth(c));
            dataoutputstream.writeInt(chartImage.getHeight(c));
        }
        dataoutputstream.writeInt(measuredDistanceRadius);
        if(measuredDistanceRadiusString == null)
            measuredDistanceRadiusString = Integer.toString(measuredDistanceRadius);
        dataoutputstream.writeUTF(measuredDistanceRadiusString);
        dataoutputstream.writeDouble(magnitude);
        dataoutputstream.writeInt(myHeight);
        dataoutputstream.writeDouble(heightFactor);
    }

    private double computeNextStep(double d, int i)
    {
        double d1 = 0.0D;
        int k = lastY <= 0.0D ? -1 : 1;
        if(k != lastYSign)
            a = ran(0.45000000000000001D) - 0.29999999999999999D * ran(0.45000000000000001D);
        lastYSign = k;
        xOn += d - lastTime;
        xOn = d;
        int j = Math.cos(b * xOn * 4D) <= 0.0D ? -1 : 1;
        if(j != lastCosSign)
        {
            b = ran(0.22500000000000001D) - 0.14999999999999999D;
            if(j < 0)
                xOn = 1.5707963267948966D / b / 4D;
            else
                xOn = 4.7123889803846897D / b / 4D;
        }
        lastCosSign = j;
        if(d < spInterval + distance / 10D && d >= distance / 10D)
        {
            double d3 = (d - distance / 10D) * 0.90000000000000002D;
            double d4;
            if(distance > 500D && magnitude < 6.4000000000000004D)
                d4 = 14.300000000000001D;
            else
            if(distance > 500D)
                d4 = 5D;
            else
            if(distance > 400D)
                d4 = 3D;
            else
                d4 = 2.1000000000000001D;
            double d2 = (d4 * Math.exp(-0.050000000000000003D * (d3 - 15D) * (100D / spInterval)) * sAmplitude) / 10D;
            if(distance < 150D)
                d2 /= d - distance / 10D;
            double d5 = ((d2 * (double)i) / heightFactor) * a;
            if(d5 < 0.0D)
                d5 = 0.0D * a;
            d1 = d5 * Math.sin(b * xOn * 4D);
        } else
        if(d <= spInterval + distance / 10D + 2D && d >= spInterval + distance / 10D)
            d1 = ((double)i / heightFactor) * pAmplitude * (1.0D - ((spInterval + distance / 10D + 2D) - d) / 4D) * Math.sin(b * xOn * 4D);
        else
        if(d <= spInterval + distance / 10D + 3D && d >= spInterval + distance / 10D)
            d1 = ((double)i / heightFactor) * pAmplitude * Math.sin(b * xOn * 4D);
        else
        if(d < duration + distance / 15D && d >= spInterval + distance / 10D)
        {
            double d7 = d - (spInterval + distance / 10D) * 0.69999999999999996D;
            double d6 = ((double)i / heightFactor) * pAmplitude * (1.0D - d7 / (duration - spInterval)) * ((spInterval + distance / 10D) / d) * a;
            if(d7 >= spInterval * 0.59999999999999998D && d7 <= spInterval * 0.65000000000000002D)
                d6 *= 1.6000000000000001D;
            if(d6 < 4D)
                d6 = 8D * a;
            d1 = d6 * Math.sin(b * xOn * 4D);
        } else
        {
            d1 = ((double)i / heightFactor) * 0.0D * a * 1.8999999999999999D * Math.sin(b * xOn * 4D);
        }
        if(d1 > (pAmplitude * (double)i) / heightFactor)
            d1 = pAmplitude;
        if(d1 < (-pAmplitude * (double)i) / heightFactor)
            d1 = -pAmplitude;
        lastTime = d;
        lastY = d1;
        return d1;
    }

    private double computePAmplitude(double d, double d1)
    {
        return Math.pow(10D, (-1.7214611458189999D - (0.015095238948699999D * (-2.507889D + 0.10811900000000001D * d + 0.00085919999999999996D * d * d)) / (-0.018665000000000001D + 0.0020479999999999999D * d + 1.3349999999999999E-06D * d * d)) + d1);
    }

    private double ran(double d)
    {
        return (r.nextDouble() * d + 1.0D) - d / 2D;
    }

    public static final double BACKGROUNDAMPLITUDE = 0D;
    public static final double BACKGROUNDPERIOD = 3D;
    public static final double INITIALFACTOR = 10D;
    public static final double MINPWAVEHEIGHT = 4D;
    public static final double PERIODFACTOR = 4D;
    public static final double RANDOMPERCENTAGE = 0.45000000000000001D;
    public static final double SPDISTANCEFACTOR = 9.9000000000000004D;
    public static final Font bigFont = new Font("Sans-serif", 1, 12);
    public static final Font font = new Font("Sans-serif", 0, 10);
    public static final int height = 16;
    public static final int width = 16;
    public double magnitude;
    public double scaleFactor;
    public int ampDraggingX;
    public int ampDraggingY;
    public int spDraggingBegin;
    public int spDraggingEnd;
    Random r;
    private Color bColor;
    private Color distanceTextColor;
    private Color normalColor;
    private Color seismogramBackgroundColor;
    private Color selectedColor;
    private Color spMeasuredColor;
    private Component c;
    private Font distanceTextFont;
    private FontMetrics fm;
    private Image chartImage;
    private NumberFormat distanceFormat;
    private String measuredDistanceRadiusString;
    private String stationName;
    private boolean mouseOver;
    private boolean selected;
    private boolean showingDistance;
    private double a;
    private double b;
    private double distance;
    private double duration;
    private double heightFactor;
    private double lastTime;
    private double lastY;
    private double maxDistance;
    private double pAmplitude;
    private double sAmplitude;
    private double scaleX;
    private double spInterval;
    private double xOn;
    private int initialX;
    private int initialY;
    private int lastCosSign;
    private int lastYSign;
    private int measuredDistanceRadius;
    private int myHeight;
    private int number;
    private int scale;
    private int x;
    private int y;
    private long randomSeed;
    private StringServer stringServer;

}
