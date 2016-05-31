// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MapComponent.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import Acme.JPM.Encoders.GifEncoder;
import Acme.JPM.Encoders.ImageEncoder;
import com.sciencecourseware.components.*;
import electric1.xml.Element;
import electric1.xml.Parent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            SeismographStation, LatitudeLongitudePoint, NomogramCanvas, TCCGraph, 
//            ToDo, EpicenterMagnitude, Journal

public class MapComponent extends Panel
    implements Runnable, ItemListener
{
    public class PopupHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            if(actionevent.getActionCommand().equals(menuLabels[0]))
                printIt();
            else
            if(actionevent.getActionCommand().equals(menuLabels[1]))
                saveIt();
        }

        public PopupHandler()
        {
        }
    }

    public class MapEventHandler
        implements MouseListener, MouseMotionListener
    {

        public void mouseClicked(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
        }

        public void mouseDragged(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            else
            if(mouseevent.getSource() == MapComponent.this)
            {
                int i = mouseevent.getModifiers();
                if((i & 8) == 0 && (i & 4) == 0)
                    mapComponent_MouseDragged(mouseevent);
            }
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
        }

        public void mouseExited(MouseEvent mouseevent)
        {
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
            if(mouseevent.getSource() == MapComponent.this)
                mapComponent_MouseMoved(mouseevent);
        }

        public void mousePressed(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            else
            if(mouseevent.getSource() == MapComponent.this)
            {
                int i = mouseevent.getModifiers();
                if((i & 8) == 0 && (i & 4) == 0)
                    mapComponent_MousePressed(mouseevent);
            }
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            else
            if(mouseevent.getSource() == MapComponent.this)
            {
                int i = mouseevent.getModifiers();
                if((i & 8) == 0 && (i & 4) == 0)
                    mapComponent_MouseReleased(mouseevent);
            }
        }

        public MapEventHandler()
        {
        }
    }


    public MapComponent(EpicenterMagnitude epicentermagnitude, StringServer stringserver)
    {
        stations = new SeismographStation[10];
        imageXAdjust = 0;
        imageYAdjust = 0;
        bColor = Color.white;
        mapEventHandler = new MapEventHandler();
        th = null;
        triggerEarthquakeButton = null;
        rotatedImages = new Hashtable();
        bufferImage = null;
        deselectedOverlayImage = null;
        image = null;
        overlayImage = null;
        epicenterLLP = null;
        nomogramCanvas = null;
        epicenterGuess = null;
        epicenterLocation = null;
        r = new Random();
        draggingStation = null;
        selectedStation = null;
        travelTimeCurveGraph = null;
        toDo = null;
        stationOffScreen = new boolean[10];
        amplitudeMeasuredWell = false;
        fieldQuakeRunning = false;
        isClassicMacOS = false;
        justFlippedToThisMode = false;
        nothingDone = true;
        showEpicenterMessage = false;
        spIntervalMeasuringMouseDown = false;
        spMeasuredWell = false;
        magnitude = 5D;
        mapScale = 1.0D;
        maxDistance = 0.0D;
        distanceCircleRadius = -1;
        fieldMode = 0;
        fieldPreferredHeight = 0;
        fieldPreferredWidth = 0;
        selectedStationNumber = -1;
        applet = epicentermagnitude;
        stringServer = stringserver;
        if(offFrame == null)
        {
            offFrame = new Frame();
            offFrame.setBackground(Color.white);
            offFrame.addNotify();
        }
        checkOS();
        setLayout(null);
        setBackground(bColor);
        setForeground(Color.black);
        setName("MapComponent");
        setSize(1, 1);
        addMouseListener(mapEventHandler);
        addMouseMotionListener(mapEventHandler);
        distanceFormat = NumberFormat.getInstance(Locale.US);
        distanceFormat.setMaximumFractionDigits(1);
        distanceFormat.setMinimumFractionDigits(1);
        amplitudeFormat = NumberFormat.getInstance(Locale.US);
        amplitudeFormat.setMaximumFractionDigits(0);
        amplitudeFormat.setMinimumFractionDigits(0);
        spFormat = NumberFormat.getInstance(Locale.US);
        spFormat.setMaximumFractionDigits(1);
        spFormat.setMinimumFractionDigits(0);
        stationChoice = new Choice();
        for(int i = 1; i <= stations.length; i++)
            stationChoice.add(stringserver.getString("station_number") + i);

        stationChoice.setBackground(bColor);
        stationChoice.setFont(scaleFont);
        stationChoice.select(0);
        scaleLabel = new Label(stringserver.getString("seismogram_scale") + ":", 2);
        scaleLabel.setFont(scaleFont);
        scaleLabel.setBackground(bColor);
        scaleChoice = new Choice();
        for(int j = 5; j < 25; j += 5)
            scaleChoice.add(Integer.toString(j) + "%");

        for(int k = 25; k <= 200; k += 25)
            scaleChoice.add(Integer.toString(k) + "%");

        for(int l = 300; l <= 1000; l += 100)
            scaleChoice.add(Integer.toString(l) + "%");

        scaleChoice.select("100%");
        scaleChoice.setFont(scaleFont);
        add(scaleLabel);
        add(scaleChoice);
        add(stationChoice);
        add(getTriggerEarthquakeButton());
        getTriggerEarthquakeButton().setEnabled(true);
        scaleLabel.setVisible(false);
        scaleChoice.setVisible(false);
        getTriggerEarthquakeButton().setVisible(false);
        scaleChoice.addItemListener(this);
        stationChoice.setVisible(false);
        stationChoice.addItemListener(this);
        String s = stringserver.getString("north_abbrev");
        String s1 = stringserver.getString("west_abbrev");
        upperRight = new LatitudeLongitudePoint(37, 57, s, 106, 40, s1);
        lowerLeft = new LatitudeLongitudePoint(33, 30, s, 122, 40, s1);
        addPopUpMenu();
        magnitude = r.nextDouble() * 2D + 3.5D;
    }

    public EpicenterMagnitude getApplet()
    {
        return applet;
    }

    public void getDataFromXML(Element element)
    {
        nomogramCanvas = null;
        Element element1 = element.getElement("mode");
        if(element1 != null)
            try
            {
                fieldMode = Integer.parseInt(element1.getTextString());
            }
            catch(NumberFormatException numberformatexception) { }
        int i = 0;
        Element element2 = element.getElement("stations");
        if(element2 != null)
        {
            String s = element2.getAttributeValue("num");
            try
            {
                int l = Integer.parseInt(s);
                stations = new SeismographStation[l];
                stationOffScreen = new boolean[l];
                for(int k1 = 0; k1 < stations.length; k1++)
                {
                    element1 = element2.getElement("station" + k1);
                    if(element1 != null)
                    {
                        stations[k1] = new SeismographStation(element1, distanceFormat, this, stringServer);
                        if(stations[k1] != null)
                        {
                            stations[k1].setNormalColor(Color.red);
                            stations[k1].setSelectedColor(Color.magenta);
                            stations[k1].setDistanceTextFont(epicenterFont);
                            stations[k1].setDistanceTextColor(distanceTextColor);
                            stationOffScreen[k1] = false;
                        }
                    }
                }

            }
            catch(NumberFormatException numberformatexception5) { }
            s = element2.getAttributeValue("selected");
            try
            {
                i = Integer.parseInt(s);
                if(i >= 0 && i < stations.length)
                {
                    selectedStation = stations[i];
                    selectedStationNumber = i;
                } else
                {
                    selectedStation = null;
                    selectedStationNumber = -1;
                }
            }
            catch(NumberFormatException numberformatexception6) { }
        }
        element1 = element.getElement("quakerunning");
        if(element1 != null)
            fieldQuakeRunning = Boolean.valueOf(element1.getTextString()).booleanValue();
        element1 = element.getElement("maxdistance");
        if(element1 != null)
            try
            {
                maxDistance = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception1) { }
        element1 = element.getElement("epicenterlocation");
        if(element1 != null)
            try
            {
                int j = Integer.parseInt(element1.getAttributeValue("x"));
                int i1 = Integer.parseInt(element1.getAttributeValue("y"));
                if(j >= 0 && i1 >= 0)
                    epicenterLocation = new Point(j, i1);
                else
                    epicenterLocation = null;
            }
            catch(NumberFormatException numberformatexception2)
            {
                epicenterLocation = null;
            }
        else
            epicenterLocation = null;
        element1 = element.getElement("epicenterguess");
        if(element1 != null)
            try
            {
                int k = Integer.parseInt(element1.getAttributeValue("x"));
                int j1 = Integer.parseInt(element1.getAttributeValue("y"));
                if(k >= 0 && j1 >= 0)
                    epicenterGuess = new Point(k, j1);
                else
                    epicenterGuess = null;
            }
            catch(NumberFormatException numberformatexception3)
            {
                epicenterLocation = null;
            }
        else
            epicenterGuess = null;
        element1 = element.getElement("magnitude");
        if(element1 != null)
            try
            {
                magnitude = Double.valueOf(element1.getTextString()).doubleValue();
            }
            catch(NumberFormatException numberformatexception4) { }
        element1 = element.getElement("upperright");
        if(element1 != null)
            upperRight = new LatitudeLongitudePoint(element1);
        element1 = element.getElement("lowerleft");
        if(element1 != null)
            lowerLeft = new LatitudeLongitudePoint(element1);
        element1 = element.getElement("epicenterllp");
        if(element1 != null)
            epicenterLLP = new LatitudeLongitudePoint(element1);
        else
            epicenterLLP = null;
        element1 = element.getElement("todo");
        if(element1 != null)
            getToDo().getDataFromXML(element1);
        if(fieldMode == 15)
            getToDo().setVisible(true);
        else
            getToDo().setVisible(false);
        if(fieldMode == 6 || fieldMode == 7)
        {
            scaleLabel.setVisible(true);
            scaleChoice.setVisible(true);
            String s1 = selectedStation.getScale() + "%";
            scaleChoice.select(s1);
            stationChoice.setVisible(true);
            stationChoice.select(i);
        } else
        if(fieldMode == 13 || fieldMode == 14)
        {
            if(nomogramCanvas == null)
            {
                nomogramCanvas = new NomogramCanvas(applet, stringServer);
                nomogramCanvas.setBounds(0, 0, getBounds().width, getBounds().height);
                nomogramCanvas.setBackground(bColor);
                nomogramCanvas.setForeground(Color.black);
                add(nomogramCanvas);
                nomogramCanvas.setVisible(true);
            } else
            {
                nomogramCanvas.setBounds(0, 0, getBounds().width, getBounds().height);
                nomogramCanvas.setVisible(true);
            }
            int ai[] = new int[stations.length];
            double ad[] = new double[stations.length];
            for(int l1 = 0; l1 < stations.length; l1++)
            {
                ai[l1] = (int)Math.round((-0.1031D + Math.sqrt(0.010629609999999999D + -6.2570399999999998E-06D * stations[l1].getSPInterval())) / -3.1285199999999999E-06D);
                ad[l1] = ((double)Math.abs(stations[l1].ampDraggingY - getSize().height / 2) / (double)stations[l1].getScale()) * 18D;
            }

            nomogramCanvas.setDistancesAndAmplitudes(ai, ad);
        }
        repaint();
    }

    public void setDistanceFractionDigits(int i)
    {
        if(i >= 0 && i < 10)
        {
            distanceFormat.setMaximumFractionDigits(i);
            distanceFormat.setMinimumFractionDigits(i);
        }
    }

    public void setEpicenterAndMagnitude(LatitudeLongitudePoint latitudelongitudepoint, double d)
    {
        if(d == 1.7976931348623157E+308D)
            magnitude = r.nextDouble() * 2D + 3.5D;
        else
            magnitude = d;
        if(latitudelongitudepoint != null)
            epicenterLocation = convertLatitudeLongitudetoXY(latitudelongitudepoint);
        else
            epicenterLocation = null;
        epicenterLLP = null;
    }

    public double getEpicenterGuessCloseness()
    {
        if(epicenterLocation == null || epicenterGuess == null)
        {
            return 1.7976931348623157E+308D;
        } else
        {
            double d = epicenterLocation.x - epicenterGuess.x;
            double d1 = epicenterLocation.y - epicenterGuess.y;
            double d2 = Math.sqrt(d * d + d1 * d1);
            double d3 = d2 * mapScale;
            return d3;
        }
    }

    public LatitudeLongitudePoint getEpicenterLatitudeLongitude()
    {
        if(epicenterLLP == null)
            epicenterLLP = convertXYtoLatitudeLongitude(epicenterLocation.x, epicenterLocation.y);
        return epicenterLLP;
    }

    public void setImage(Image image1, Image image2)
    {
        setImage(image1, image2, null);
    }

    public void setImage(Image image1, Image image2, EpicenterMagnitude epicentermagnitude)
    {
        if(epicentermagnitude != null)
            applet = epicentermagnitude;
        image = image1;
        if(image == null)
            System.err.println("The image passed to MapComponent is null!");
        getTriggerEarthquakeButton().setBackgroundImage(image2);
        bufferImage = null;
        repaint();
    }

    public double getMagnitude()
    {
        if(stations != null && stations[0] != null && stations[0].getMagnitude() > 0.0D && magnitude != stations[0].getMagnitude())
            magnitude = stations[0].getMagnitude();
        return magnitude;
    }

    public void setMapLatitudeLongitude(LatitudeLongitudePoint latitudelongitudepoint, LatitudeLongitudePoint latitudelongitudepoint1)
    {
        if(latitudelongitudepoint != null)
            upperRight = latitudelongitudepoint;
        if(latitudelongitudepoint1 != null)
            lowerLeft = latitudelongitudepoint1;
        repaint();
    }

    public void setMapScale(double d)
    {
        if(d > 0.0D)
            mapScale = d;
    }

    public double getMapScale()
    {
        return mapScale;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public void setMode(int i)
    {
        if(i != fieldMode)
            justFlippedToThisMode = true;
        int j = fieldMode;
        fieldMode = i;
        firePropertyChange("mode", new Integer(j), new Integer(i));
        if(i == 1)
        {
            scaleLabel.setVisible(false);
            scaleChoice.setVisible(false);
            stationChoice.setVisible(false);
            getToDo().setVisible(false);
            getTriggerEarthquakeButton().setVisible(true);
            magnitude = r.nextDouble() * 2D + 3.5D;
            epicenterLocation = null;
            stations = new SeismographStation[3];
            stationOffScreen = new boolean[3];
            if(nomogramCanvas != null)
                nomogramCanvas.setVisible(false);
            if(travelTimeCurveGraph != null)
                travelTimeCurveGraph.setVisible(false);
        } else
        if(i == 9)
        {
            scaleChoice.setVisible(false);
            scaleLabel.setVisible(false);
            stationChoice.setVisible(false);
            if(nomogramCanvas != null)
                nomogramCanvas.setVisible(false);
            getToDo().setVisible(false);
            getTriggerEarthquakeButton().setVisible(false);
            selectedStation = null;
            selectedStationNumber = -1;
            double d = 0.0D;
            double d1 = 0.0D;
            NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
            for(int i1 = 0; i1 < spIntervalTextFields.length; i1++)
                try
                {
                    Number number = numberformat.parse(EpicenterMagnitude.normalizeNumberString(spIntervalTextFields[i1].getText()));
                    double d2 = number.doubleValue();
                    if(d2 > d)
                    {
                        d = d2;
                        d1 = (-0.1031D + Math.sqrt(0.010629609999999999D + -6.2570399999999998E-06D * d2)) / -3.1285199999999999E-06D;
                    }
                    d = (int)((d + 10D) / 10D) * 10;
                    d1 = (int)((d1 + 50D) / 10D) * 10;
                }
                catch(ParseException parseexception) { }

            if(travelTimeCurveGraph == null)
            {
                travelTimeCurveGraph = new TCCGraph(this, stringServer);
                travelTimeCurveGraph.setBackground(bColor);
                add(travelTimeCurveGraph);
            }
            travelTimeCurveGraph.setBounds(0, 0, getBounds().width, getBounds().height);
            travelTimeCurveGraph.setVisible(true);
            travelTimeCurveGraph.setMaxes(d1 > 0.0D ? d1 : 500D, d > 0.0D ? d : 50D);
            travelTimeCurveGraph.setShowHelpMessage(true);
            double ad1[] = new double[spIntervalTextFields.length];
            for(int j1 = 0; j1 < ad1.length; j1++)
                try
                {
                    String s2 = EpicenterMagnitude.normalizeNumberString(spIntervalTextFields[j1].getText());
                    ad1[j1] = Double.valueOf(s2).doubleValue();
                }
                catch(NumberFormatException numberformatexception2)
                {
                    ad1[j1] = 0.0D;
                }

            travelTimeCurveGraph.setSPIntervals(ad1);
        } else
        if(i == 15)
        {
            if(travelTimeCurveGraph != null)
                travelTimeCurveGraph.setVisible(false);
            scaleChoice.setVisible(false);
            scaleLabel.setVisible(false);
            stationChoice.setVisible(false);
            getToDo().setVisible(true);
            getTriggerEarthquakeButton().setVisible(false);
        } else
        {
            if(travelTimeCurveGraph != null)
                travelTimeCurveGraph.setVisible(false);
            getToDo().setVisible(false);
            getTriggerEarthquakeButton().setVisible(false);
            if(stations[0] != null)
            {
                for(int k = 0; k < stations.length; k++)
                    stations[k].setSelected(false);

            }
            if(i == 13 || i == 14)
            {
                scaleChoice.setVisible(false);
                scaleLabel.setVisible(false);
                stationChoice.setVisible(false);
                if(nomogramCanvas == null)
                {
                    nomogramCanvas = new NomogramCanvas(applet, stringServer);
                    nomogramCanvas.setBounds(0, 0, getBounds().width, getBounds().height);
                    nomogramCanvas.setBackground(bColor);
                    nomogramCanvas.setForeground(Color.black);
                    add(nomogramCanvas);
                } else
                {
                    nomogramCanvas.setBounds(0, 0, getBounds().width, getBounds().height);
                    nomogramCanvas.setVisible(true);
                }
                nomogramCanvas.setShowMessage(true);
                int ai[] = new int[distanceTextFields.length];
                double ad[] = new double[amplitudeTextFields.length];
                for(int l = 0; l < ai.length; l++)
                {
                    try
                    {
                        ai[l] = Integer.parseInt(distanceTextFields[l].getText());
                    }
                    catch(NumberFormatException numberformatexception)
                    {
                        ai[l] = 0;
                    }
                    try
                    {
                        String s1 = EpicenterMagnitude.normalizeNumberString(amplitudeTextFields[l].getText());
                        ad[l] = Double.valueOf(s1).doubleValue();
                    }
                    catch(NumberFormatException numberformatexception1)
                    {
                        ad[l] = 0.0D;
                    }
                }

                nomogramCanvas.setDistancesAndAmplitudes(ai, ad);
            } else
            if(i == 6 || i == 7)
            {
                if(nomogramCanvas != null)
                    nomogramCanvas.setVisible(false);
                if(travelTimeCurveGraph != null)
                    travelTimeCurveGraph.setVisible(false);
                if(selectedStation == null)
                {
                    selectedStation = stations[stationChoice.getSelectedIndex()];
                    selectedStationNumber = stationChoice.getSelectedIndex();
                }
                scaleLabel.setVisible(true);
                scaleChoice.setVisible(true);
                stationChoice.setVisible(true);
                String s = selectedStation.getScale() + "%";
                scaleChoice.select(s);
                if(i == 7 && stationOffScreen[selectedStationNumber])
                {
                    showOffScreenMessage();
                    stationOffScreen[selectedStationNumber] = false;
                }
            } else
            {
                selectedStation = null;
                selectedStationNumber = -1;
                if(nomogramCanvas != null)
                    nomogramCanvas.setVisible(false);
                if(travelTimeCurveGraph != null)
                    travelTimeCurveGraph.setVisible(false);
                scaleLabel.setVisible(false);
                scaleChoice.setVisible(false);
                stationChoice.setVisible(false);
            }
        }
        repaint();
    }

    public int getMode()
    {
        return fieldMode;
    }

    public void setNothingDone(boolean flag)
    {
        nothingDone = flag;
    }

    public boolean isNothingDone()
    {
        return nothingDone;
    }

    public void setPreferredHeight(int i)
    {
        fieldPreferredHeight = i;
    }

    public int getPreferredHeight()
    {
        return fieldPreferredHeight;
    }

    public Dimension getPreferredSize()
    {
        Dimension dimension = super.getPreferredSize();
        if(getPreferredWidth() > dimension.width)
            dimension.width = getPreferredWidth();
        if(getPreferredHeight() > dimension.height)
            dimension.height = getPreferredHeight();
        return dimension;
    }

    public void setPreferredWidth(int i)
    {
        fieldPreferredWidth = i;
    }

    public int getPreferredWidth()
    {
        return fieldPreferredWidth;
    }

    public void setQuakeRunning(boolean flag)
    {
        boolean flag1 = fieldQuakeRunning;
        fieldQuakeRunning = flag;
        firePropertyChange("quakeRunning", new Boolean(flag1), new Boolean(flag));
    }

    public boolean getQuakeRunning()
    {
        return fieldQuakeRunning;
    }

    public Image getRotatedImage(String s, FontMetrics fontmetrics, Font font, Color color, Color color1)
    {
        if(rotatedImages == null)
            return null;
        Image image1 = (Image)rotatedImages.get(s);
        if(image1 == null)
        {
            Graphics g = offFrame.getGraphics();
            if(g == null || font == null || s == null || color == null || color1 == null)
                return null;
            if(fontmetrics == null)
            {
                System.out.println("FontMetrics is null.");
                return null;
            }
            int i = fontmetrics.stringWidth(s) + 2;
            int j = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + 2;
            Image image2 = createImage(i, j);
            if(image2 == null)
                return null;
            Graphics g1 = image2.getGraphics();
            if(g1 == null)
                return null;
            g1.setColor(color);
            g1.fillRect(0, 0, i, j);
            g1.setColor(color1);
            g1.setFont(font);
            g1.drawString(s, 1, fontmetrics.getMaxAscent() + 1);
            g1.dispose();
            g.drawImage(image2, 0, 0, this);
            g.dispose();
            int ai[] = new int[i * j];
            PixelGrabber pixelgrabber = new PixelGrabber(image2, 0, 0, i, j, ai, 0, i);
            if(pixelgrabber == null)
                return null;
            try
            {
                pixelgrabber.grabPixels();
            }
            catch(InterruptedException interruptedexception) { }
            int ai1[] = new int[i * j];
            for(int k = 0; k < j; k++)
            {
                for(int l = 0; l < i; l++)
                    ai1[l * j + k] = ai[k * i + (i - l - 1)];

            }

            MemoryImageSource memoryimagesource = new MemoryImageSource(j, i, ColorModel.getRGBdefault(), ai1, 0, j);
            if(memoryimagesource == null)
                return null;
            image1 = createImage(memoryimagesource);
            if(image1 == null)
                return null;
            MediaTracker mediatracker = new MediaTracker(this);
            if(mediatracker == null)
                return null;
            mediatracker.addImage(image1, 0);
            try
            {
                mediatracker.waitForAll();
            }
            catch(InterruptedException interruptedexception1) { }
            rotatedImages.put(s, image1);
        }
        return image1;
    }

    public SeismographStation getStation(int i)
    {
        if(i < 0 || i >= stations.length)
            return null;
        else
            return stations[i];
    }

    public void setStationLocation(int i, LatitudeLongitudePoint latitudelongitudepoint)
    {
        Point point = convertLatitudeLongitudetoXY(latitudelongitudepoint);
        stations[i].setXY(point.x, point.y);
    }

    public void setStations(String as[], LatitudeLongitudePoint alatitudelongitudepoint[])
    {
        stations = new SeismographStation[alatitudelongitudepoint.length];
        stationOffScreen = new boolean[alatitudelongitudepoint.length];
        stationChoice.removeAll();
        for(int i = 0; i < alatitudelongitudepoint.length; i++)
        {
            Point point = convertLatitudeLongitudetoXY(alatitudelongitudepoint[i]);
            stations[i] = new SeismographStation(i + 1, point.x, point.y, distanceFormat, magnitude, stringServer);
            stations[i].setNormalColor(Color.red);
            stations[i].setSelectedColor(Color.magenta);
            stations[i].setDistanceTextFont(epicenterFont);
            stations[i].setDistanceTextColor(distanceTextColor);
            stations[i].setXY(point.x, point.y);
            stations[i].setStationName(as[i]);
            stationChoice.add(stringServer.getString("station_number") + (i + 1));
            stationOffScreen[i] = false;
        }

    }

    public void setTextFields(TextField atextfield[], TextField atextfield1[], TextField atextfield2[])
    {
        distanceTextFields = atextfield;
        spIntervalTextFields = atextfield1;
        amplitudeTextFields = atextfield2;
    }

    public void setToDo(ToDo todo)
    {
        toDo = todo;
    }

    public ToDo getToDo()
    {
        if(toDo == null)
        {
            toDo = new ToDo(applet, stringServer, applet.imageServer);
            toDo.setBounds(0, 0, getBounds().width, getBounds().height);
            toDo.setVisible(false);
            add(toDo);
        }
        return toDo;
    }

    public EECButton getTriggerEarthquakeButton()
    {
        if(triggerEarthquakeButton == null)
        {
            triggerEarthquakeButton = new EECButton();
            triggerEarthquakeButton.setImages(applet.imageServer.getImage("buttonUpImage"), applet.imageServer.getImage("buttonDownImage"), applet.imageServer.getImage("buttonOverImage"), applet.imageServer.getImage("buttonOverDownImage"));
            triggerEarthquakeButton.setDisabledImage(applet.imageServer.getImage("buttonDisabledImage"));
            triggerEarthquakeButton.setBackgroundImage(applet.imageServer.getImage("trigger_sc"));
            triggerEarthquakeButton.setRescaleToFit(false);
            triggerEarthquakeButton.setName("triggerEarthquakeButton");
            triggerEarthquakeButton.setForeground(applet.buttonLabelColor);
            triggerEarthquakeButton.setDisabledForeground(Color.gray);
            triggerEarthquakeButton.setBackground(Color.darkGray);
            triggerEarthquakeButton.setFont(applet.buttonFont);
            triggerEarthquakeButton.setLabel(EpicenterMagnitude.triggerEarthquake);
            triggerEarthquakeButton.setRolloverColor(new Color(51, 204, 0));
            triggerEarthquakeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent)
                {
                    applet.doAction(EpicenterMagnitude.triggerEarthquake);
                }

            }
);
        }
        return triggerEarthquakeButton;
    }

    public void addDataToXML(Element element)
    {
        element.addElement(new Element("mode")).setText(Integer.toString(fieldMode));
        int i = -1;
        Element element1 = new Element("stations");
        element1.setAttribute("num", Integer.toString(stations.length));
        element.addElement(element1);
        for(int j = 0; j < stations.length; j++)
        {
            if(selectedStation == stations[j])
                i = j;
            Element element2 = new Element("station" + j);
            stations[j].addDataToXML(element2);
            element1.addElement(element2);
        }

        element1.setAttribute("selected", Integer.toString(i));
        String s = EpicenterMagnitude.booleanToString(fieldQuakeRunning);
        element.addElement(new Element("quakerunning")).setText(s);
        element.addElement(new Element("maxdistance")).setText(Double.toString(maxDistance));
        if(epicenterLocation != null)
        {
            Element element3 = new Element("epicenterlocation");
            element3.setAttribute("x", Integer.toString(epicenterLocation.x));
            element3.setAttribute("y", Integer.toString(epicenterLocation.y));
            element.addElement(element3);
        }
        if(epicenterGuess != null)
        {
            Element element4 = new Element("epicenterguess");
            element4.setAttribute("x", Integer.toString(epicenterGuess.x));
            element4.setAttribute("y", Integer.toString(epicenterGuess.y));
            element.addElement(element4);
        }
        element.addElement(new Element("magnitude")).setText(Double.toString(magnitude));
        Element element5 = new Element("upperright");
        upperRight.addDataToXML(element5);
        element.addElement(element5);
        element5 = new Element("lowerleft");
        lowerLeft.addDataToXML(element5);
        element.addElement(element5);
        if(epicenterLLP != null)
        {
            Element element6 = new Element("epicenterllp");
            epicenterLLP.addDataToXML(element6);
            element.addElement(element6);
        }
        element5 = new Element("todo");
        getToDo().addDataToXML(element5);
        element.addElement(element5);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().addPropertyChangeListener(propertychangelistener);
    }

    public Point convertLatitudeLongitudetoXY(LatitudeLongitudePoint latitudelongitudepoint)
    {
        double d = lowerLeft.getLatitudeDouble();
        double d1 = upperRight.getLatitudeDouble();
        double d2 = latitudelongitudepoint.getLatitudeDouble();
        int i = image.getHeight(this) - (int)Math.round(((d2 - d) / (d1 - d)) * (double)image.getHeight(this));
        double d3 = lowerLeft.getLongitudeDouble();
        double d4 = upperRight.getLongitudeDouble();
        double d5 = latitudelongitudepoint.getLongitudeDouble();
        int j = (int)Math.round(((d5 - d3) / (d4 - d3)) * (double)image.getWidth(this));
        return new Point(j, i);
    }

    public LatitudeLongitudePoint convertXYtoLatitudeLongitude(int i, int j)
    {
        double d = lowerLeft.getLatitudeDouble();
        double d1 = upperRight.getLatitudeDouble();
        double d2 = (d1 - d) * ((double)(image.getHeight(this) - j) / (double)image.getHeight(this)) + d;
        double d3 = lowerLeft.getLongitudeDouble();
        double d4 = upperRight.getLongitudeDouble();
        double d5 = (d4 - d3) * ((double)i / (double)image.getWidth(this)) + d3;
        LatitudeLongitudePoint latitudelongitudepoint = new LatitudeLongitudePoint(d2, d5);
        return latitudelongitudepoint;
    }

    public void eraseAmplitudeMeasurement(int i)
    {
        if(i >= 0 && i < stations.length)
        {
            stations[i].ampDraggingX = 0x7fffffff;
            stations[i].ampDraggingY = 0x7fffffff;
            amplitudeTextFields[i].setText("");
            amplitudeMeasuredWell = false;
        }
    }

    public void eraseStationMeasurements(int i)
    {
        if(i >= 0 && i < stations.length)
        {
            stations[i].spDraggingBegin = 0x7fffffff;
            stations[i].spDraggingEnd = 0x7fffffff;
            spIntervalTextFields[i].setText("");
            distanceTextFields[i].setText("");
            spMeasuredWell = false;
            eraseAmplitudeMeasurement(i);
        }
    }

    public void firePropertyChange(String s, Object obj, Object obj1)
    {
        getPropertyChange().firePropertyChange(s, obj, obj1);
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        if(itemevent.getSource() == scaleChoice)
        {
            String s = scaleChoice.getSelectedItem();
            try
            {
                int j = Integer.parseInt(s.substring(0, s.length() - 1));
                setCursor(Cursor.getPredefinedCursor(3));
                stationOffScreen[selectedStationNumber] = selectedStation.setupChartImage(getSize().width, getSize().height, selectedStation.getDistance(), maxDistance, this, j);
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }
            catch(NumberFormatException numberformatexception)
            {
                System.err.println("Error getting scale from Choice.");
                numberformatexception.printStackTrace();
            }
        } else
        {
            int i = stationChoice.getSelectedIndex();
            selectedStation = stations[i];
            selectedStationNumber = i;
            String s1 = selectedStation.getScale() + "%";
            scaleChoice.select(s1);
            repaint();
            if(stationOffScreen[selectedStationNumber] && getMode() == 7)
                showOffScreenMessage();
        }
    }

    public void mapComponent_MouseDragged(MouseEvent mouseevent)
    {
        if(getMode() == 6 || getMode() == 7)
            processViewingSeismogramMouse(mouseevent.getX(), mouseevent.getY(), false);
        else
        if(getMode() == 10)
            processMeasuringDistancesMouse(mouseevent.getX(), mouseevent.getY(), false);
        else
        if(getMode() == 11)
            processFindEpicenterMouse(mouseevent.getX(), mouseevent.getY(), false);
    }

    public void mapComponent_MouseEntered(MouseEvent mouseevent)
    {
    }

    public void mapComponent_MouseExited(MouseEvent mouseevent)
    {
        if(showEpicenterMessage)
        {
            showEpicenterMessage = false;
            repaint();
        }
        if(getCursor() != Cursor.getPredefinedCursor(0))
            setCursor(Cursor.getPredefinedCursor(0));
    }

    public void mapComponent_MouseMoved(MouseEvent mouseevent)
    {
        Cursor cursor = Cursor.getDefaultCursor();
        if(getMode() != 6 && getMode() != 7 && epicenterGuess != null)
        {
            if(mouseevent.getX() >= epicenterGuess.x - 9 && mouseevent.getX() <= epicenterGuess.x + 9 && mouseevent.getY() >= epicenterGuess.y - 9 && mouseevent.getY() <= epicenterGuess.y + 9)
            {
                showEpicenterMessage = true;
                if(getMode() == 11)
                    cursor = Cursor.getPredefinedCursor(12);
                repaint();
            } else
            if(showEpicenterMessage)
            {
                showEpicenterMessage = false;
                repaint();
            }
            boolean flag = false;
            boolean flag2 = false;
            if(stations[0] != null)
            {
                for(int i = 0; i < stations.length; i++)
                {
                    if(stations[i].contains(mouseevent.getX(), mouseevent.getY()))
                    {
                        if(getMode() == 1)
                            cursor = Cursor.getPredefinedCursor(12);
                        stations[i].setMouseOver(true);
                        boolean flag1 = true;
                        flag2 = true;
                        continue;
                    }
                    if(stations[i].isMouseOver())
                    {
                        stations[i].setMouseOver(false);
                        flag2 = true;
                    }
                }

            }
            if(flag2)
                repaint();
        } else
        if(getMode() == 6 || getMode() == 7)
            showEpicenterMessage = false;
        else
            showEpicenterMessage = false;
        if(applet.getSaveButton().getRolledOver())
        {
            applet.getSaveButton().setRolledOver(false);
            applet.getSaveButton().repaint();
        }
        if(getCursor() != cursor)
            setCursor(cursor);
    }

    public void mapComponent_MousePressed(MouseEvent mouseevent)
    {
        if(getMode() == 6)
        {
            selectedStation.spDraggingBegin = 0x7fffffff;
            selectedStation.spDraggingEnd = 0x7fffffff;
            processViewingSeismogramMouse(mouseevent.getX(), mouseevent.getY(), false);
        } else
        if(getMode() == 7)
        {
            selectedStation.ampDraggingX = 0x7fffffff;
            selectedStation.ampDraggingY = 0x7fffffff;
            processViewingSeismogramMouse(mouseevent.getX(), mouseevent.getY(), false);
        } else
        if(getMode() == 10)
            processMeasuringDistancesMouse(mouseevent.getX(), mouseevent.getY(), false);
        else
        if(getMode() == 11)
            processFindEpicenterMouse(mouseevent.getX(), mouseevent.getY(), false);
    }

    public void mapComponent_MouseReleased(MouseEvent mouseevent)
    {
        switch(getMode())
        {
        case 5: // '\005'
        case 8: // '\b'
            processClickToViewMouse(mouseevent.getX(), mouseevent.getY());
            break;

        case 6: // '\006'
        case 7: // '\007'
            processViewingSeismogramMouse(mouseevent.getX(), mouseevent.getY(), true);
            break;

        case 10: // '\n'
            processMeasuringDistancesMouse(mouseevent.getX(), mouseevent.getY(), true);
            break;

        case 11: // '\013'
            processFindEpicenterMouse(mouseevent.getX(), mouseevent.getY(), true);
            break;
        }
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(epicenterLocation == null)
        {
            epicenterLocation = new Point();
            if(applet.setEpicenter)
            {
                epicenterLocation.x = (int)Math.round((double)i / 2D);
                epicenterLocation.y = (int)Math.round((double)j / 2D);
            } else
            {
                epicenterLocation.x = (int)Math.round(r.nextDouble() * ((double)i / 2D) + (double)i * 0.25D);
                epicenterLocation.y = (int)Math.round(r.nextDouble() * ((double)j / 2D) + (double)j * 0.25D);
            }
        }
        if((bufferImage == null || bufferImage.getWidth(this) != i || bufferImage.getHeight(this) != j) && !(g instanceof PrintGraphics))
        {
            bufferImage = createImage(i, j);
            byte byte0 = 80;
            byte byte1 = 120;
            byte byte2 = 20;
            byte byte3 = 10;
            scaleLabel.setBounds(i - (byte3 * 3) / 2 - byte1 - byte0, byte3, byte1, byte2);
            scaleChoice.setBounds(i - byte3 - byte0, byte3, byte0, byte2);
            getTriggerEarthquakeButton().setBounds(i - 128, 0, 128, 28);
            stationChoice.setBounds(2, 2, 100, byte2);
            createOverlayImage(i, j);
        }
        if(bufferImage != null || (g instanceof PrintGraphics))
        {
            Graphics g1;
            if(g instanceof PrintGraphics)
                g1 = g;
            else
                g1 = bufferImage.getGraphics();
            g1.setColor(bColor);
            g1.fillRect(0, 0, i, j);
            int k = getMode();
            if(k == 0)
            {
                g1.setColor(Color.black);
                g1.drawString("Press start to start.", 20, j / 2 - 5);
            } else
            if(k == 6 || k == 7)
            {
                selectedStation.setMouseOver(false);
                Image image1 = selectedStation.getChartImage();
                if(image1 == null)
                {
                    selectedStation.setupChartImage(i, j, this);
                    image1 = selectedStation.getChartImage();
                }
                g1.drawImage(image1, 0, 0, this);
                if(selectedStation.spDraggingBegin != 0x7fffffff && selectedStation.spDraggingEnd != 0x7fffffff)
                {
                    int i1;
                    int k1;
                    if(selectedStation.spDraggingBegin < selectedStation.spDraggingEnd)
                    {
                        i1 = selectedStation.spDraggingBegin;
                        k1 = selectedStation.spDraggingEnd;
                    } else
                    {
                        i1 = selectedStation.spDraggingEnd;
                        k1 = selectedStation.spDraggingBegin;
                    }
                    if(k != 7)
                    {
                        g1.setColor(Color.red);
                        if(isClassicMacOS)
                        {
                            for(int i2 = -1; i2 <= 1; i2++)
                                g1.drawLine(i1, j / 2 + i2, k1, j / 2 + i2);

                        } else
                        {
                            g1.drawImage(overlayImage, i1, 0, k1, j, i1, 0, k1, j, this);
                        }
                    }
                    g1.drawLine(i1, 0, i1, j);
                    g1.drawLine(k1, 0, k1, j);
                    g1.setColor(bColor);
                    g1.setFont(spFont);
                    FontMetrics fontmetrics1 = g1.getFontMetrics();
                    String s1 = stringServer.getString("spinterval") + " = " + EpicenterMagnitude.outputNumberString(spFormat.format(selectedStation.getSPInterval())) + " " + stringServer.getString("sec");
                    int j3 = fontmetrics1.stringWidth(s1);
                    if(k != 7)
                        if(k1 + 2 + j3 < i)
                        {
                            g1.fillRect(k1 + 1, (j - fontmetrics1.getMaxDescent() - fontmetrics1.getHeight()) + 1, j3 + 2, fontmetrics1.getHeight() + 2);
                            g1.setColor(Color.black);
                            g1.drawString(s1, k1 + 2, j - fontmetrics1.getMaxDescent());
                        } else
                        {
                            g1.fillRect(i1 - 3 - j3, (j - fontmetrics1.getMaxDescent() - fontmetrics1.getHeight()) + 1, j3 + 2, fontmetrics1.getHeight() + 2);
                            g1.setColor(Color.black);
                            g1.drawString(s1, i1 - 2 - j3, j - fontmetrics1.getMaxDescent());
                        }
                    double d1 = selectedStation.getSPInterval();
                    double d2 = (-0.1031D + Math.sqrt(0.010629609999999999D + -6.2570399999999998E-06D * d1)) / -3.1285199999999999E-06D;
                } else
                if(k == 6)
                    paintSeismogramMessage(g1, selectedStation.getSWaveStart(), j);
                if(k == 7)
                    if(selectedStation.ampDraggingY != 0x7fffffff)
                    {
                        int j1 = j / 2;
                        g1.setColor(Color.red);
                        for(int l1 = -1; l1 < 2; l1++)
                            g1.drawLine(selectedStation.ampDraggingX + l1, j1, selectedStation.ampDraggingX + l1, selectedStation.ampDraggingY);

                        g1.drawLine(selectedStation.ampDraggingX - 60, j1, selectedStation.ampDraggingX + 60, j1);
                        g1.drawLine(selectedStation.ampDraggingX - 60, selectedStation.ampDraggingY, selectedStation.ampDraggingX + 60, selectedStation.ampDraggingY);
                        double d = ((double)Math.abs(selectedStation.ampDraggingY - j1) / (double)selectedStation.getScale()) * 18D;
                        if(d < 10D)
                        {
                            amplitudeFormat.setMaximumFractionDigits(1);
                            amplitudeFormat.setMinimumFractionDigits(1);
                        } else
                        {
                            amplitudeFormat.setMaximumFractionDigits(0);
                            amplitudeFormat.setMinimumFractionDigits(0);
                        }
                        String s2 = EpicenterMagnitude.outputNumberString(amplitudeFormat.format(d));
                        s2 = s2 + "mm";
                        FontMetrics fontmetrics2 = g1.getFontMetrics();
                        int l3 = fontmetrics2.stringWidth(s2);
                        int j4 = selectedStation.ampDraggingY - 2;
                        if(j4 < 30)
                            j4 = selectedStation.ampDraggingY + fontmetrics2.getAscent() + 4;
                        int i4;
                        if(selectedStation.ampDraggingX + 265 < i)
                            i4 = (selectedStation.ampDraggingX + 60) - l3;
                        else
                            i4 = selectedStation.ampDraggingX - 60;
                        g1.setColor(bColor);
                        g1.fillRect(i4 - 1, j4 - fontmetrics2.getAscent() - 1, l3 + 2, fontmetrics2.getAscent() + 2);
                        g1.setColor(Color.black);
                        g1.drawString(s2, i4, j4);
                    } else
                    if(!stationOffScreen[selectedStationNumber])
                        paintAmplitudeMessage(g1, selectedStation.getPWaveMax(), i, j);
                g1.setFont(spFont);
                FontMetrics fontmetrics = g1.getFontMetrics();
                String s = stringServer.getString("scale") + " = " + scaleChoice.getSelectedItem();
                int j2 = fontmetrics.stringWidth(s);
                g1.setColor(Color.white);
                g1.fillRect(i - j2 - 14, 10, j2 + 4, fontmetrics.getAscent() + 4);
                int i3 = i - j2 - 12;
                int k3 = fontmetrics.getAscent() + 12;
                g1.setColor(Color.black);
                g1.drawString(s, i3, k3);
            } else
            if(k == 13 || k == 14)
            {
                nomogramCanvas.setVisible(true);
                nomogramCanvas.repaint();
            } else
            if(k != 0)
            {
                if(k != 3)
                {
                    imageXAdjust = 0;
                    imageYAdjust = 0;
                }
                if(image != null)
                    g1.drawImage(image, imageXAdjust, imageYAdjust, this);
                if(stations[0] == null)
                {
                    for(int l = 0; l < stations.length; l++)
                    {
                        stations[l] = new SeismographStation(l + 1, 16 + l * 16 * 2, j - 16, distanceFormat, magnitude, stringServer);
                        stations[l].setNormalColor(Color.red);
                        stations[l].setSelectedColor(Color.magenta);
                        stations[l].setDistanceTextFont(epicenterFont);
                        stations[l].setDistanceTextColor(distanceTextColor);
                    }

                }
                java.awt.Shape shape = g1.getClip();
                g1.clipRect(imageXAdjust, imageYAdjust, image.getWidth(this), image.getHeight(this));
                SeismographStation seismographstation = null;
                Object obj = null;
                if(k == 12)
                    paintLatitudeLongitudeLines(g1, imageXAdjust, imageYAdjust, image.getWidth(this), image.getHeight(this));
                for(int k2 = 0; k2 < stations.length; k2++)
                {
                    if(stations[k2] == null)
                        continue;
                    if(stations[k2].isMouseOver())
                        seismographstation = stations[k2];
                    else
                        stations[k2].paintStation(g1, imageXAdjust, imageYAdjust, image.getWidth(this), k);
                }

                if(seismographstation != null)
                    seismographstation.paintStation(g1, imageXAdjust, imageYAdjust, image.getWidth(this), k);
                if(k == 10 || k == 12 || k == 11)
                {
                    if(k == 10 || k == 11)
                    {
                        for(int l2 = 0; l2 < stations.length; l2++)
                            if(stations[l2].getMeasuredDistanceRadius() != 0x7fffffff)
                                paintMeasuringDistanceCircles(g1, imageXAdjust, imageYAdjust, stationDistanceCircleColor, distanceTextColor);

                        if(distanceCircleRadius > 0)
                            paintMeasuringDistanceCircles(g1, imageXAdjust, imageYAdjust, measuringDistanceCircleColor, distanceTextColor);
                    }
                    if(k == 11 || k == 12)
                        paintEpicenterGuess(g1, imageXAdjust, imageYAdjust, i, j);
                }
                if(k == 10 && justFlippedToThisMode)
                    paintMeasuringDistanceMessage(g1, i, j);
                g1.setClip(shape);
            }
            if(!(g instanceof PrintGraphics))
                g1.dispose();
        }
        if(!(g instanceof PrintGraphics))
            g.drawImage(bufferImage, 0, 0, this);
    }

    public void print(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height + 25);
        Graphics g1 = g.create(0, 25, getSize().width, getSize().height + 25);
        paint(g1);
        g1.dispose();
        String s = applet.getJournal().getUserName();
        String s1 = applet.getJournal().getDate();
        g.setColor(Color.black);
        g.setFont(PRINT_TITLE_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        if(s != null && s.length() > 0)
            g.drawString(s, 5, 5 + fontmetrics.getAscent());
        if(s1 != null && s1.length() > 0)
            g.drawString(s1, getSize().width - 5 - fontmetrics.stringWidth(s1), 5 + fontmetrics.getAscent());
    }

    public void printIt()
    {
        PrintJob printjob = getToolkit().getPrintJob(EpicenterMagnitude.parentFrame, "Print", (Properties)null);
        if(printjob != null)
        {
            Graphics g = printjob.getGraphics();
            if(g != null)
            {
                if(getMode() == 13 || getMode() == 14)
                    nomogramCanvas.print(g);
                else
                if(getMode() == 9)
                    travelTimeCurveGraph.print(g);
                else
                    print(g);
                g.dispose();
            }
            printjob.end();
        }
    }

    public void readDataFromStream(DataInputStream datainputstream)
        throws IOException
    {
        nomogramCanvas = null;
        fieldMode = datainputstream.readInt();
        int i = datainputstream.readInt();
        stations = new SeismographStation[i];
        stationOffScreen = new boolean[i];
        for(int j = 0; j < stations.length; j++)
        {
            stations[j] = new SeismographStation(datainputstream, distanceFormat, this, stringServer);
            stations[j].setNormalColor(Color.red);
            stations[j].setSelectedColor(Color.magenta);
            stations[j].setDistanceTextFont(epicenterFont);
            stations[j].setDistanceTextColor(distanceTextColor);
            stationOffScreen[j] = false;
        }

        magnitude = stations[0].getMagnitude();
        int k = datainputstream.readInt();
        if(k >= 0 && k < stations.length)
        {
            selectedStation = stations[k];
            selectedStationNumber = k;
        } else
        {
            selectedStation = null;
            selectedStationNumber = -1;
        }
        fieldQuakeRunning = datainputstream.readBoolean();
        maxDistance = datainputstream.readDouble();
        int l = datainputstream.readInt();
        int i1 = datainputstream.readInt();
        if(l >= 0 && i1 >= 0)
            epicenterLocation = new Point(l, i1);
        else
            epicenterLocation = null;
        int j1 = datainputstream.readInt();
        int k1 = datainputstream.readInt();
        if(j1 >= 0 && k1 >= 0)
            epicenterGuess = new Point(j1, k1);
        else
            epicenterGuess = null;
        magnitude = datainputstream.readDouble();
        upperRight = new LatitudeLongitudePoint(datainputstream);
        lowerLeft = new LatitudeLongitudePoint(datainputstream);
        boolean flag = datainputstream.readBoolean();
        if(flag)
            epicenterLLP = new LatitudeLongitudePoint(datainputstream);
        else
            epicenterLLP = null;
        if(fieldMode == 6 || fieldMode == 7)
        {
            scaleLabel.setVisible(true);
            scaleChoice.setVisible(true);
            String s = selectedStation.getScale() + "%";
            scaleChoice.select(s);
            stationChoice.setVisible(true);
            stationChoice.select(k);
        } else
        if(fieldMode == 13 || fieldMode == 14)
        {
            if(nomogramCanvas == null)
            {
                nomogramCanvas = new NomogramCanvas(applet, stringServer);
                nomogramCanvas.setBounds(0, 0, getBounds().width, getBounds().height);
                nomogramCanvas.setBackground(bColor);
                nomogramCanvas.setForeground(Color.black);
                add(nomogramCanvas);
            } else
            {
                nomogramCanvas.setBounds(0, 0, getBounds().width, getBounds().height);
                nomogramCanvas.setVisible(true);
            }
            int ai[] = new int[stations.length];
            double ad[] = new double[stations.length];
            for(int l1 = 0; l1 < stations.length; l1++)
            {
                ai[l1] = (int)Math.round((-0.1031D + Math.sqrt(0.010629609999999999D + -6.2570399999999998E-06D * stations[l1].getSPInterval())) / -3.1285199999999999E-06D);
                ad[l1] = ((double)Math.abs(stations[l1].ampDraggingY - getSize().height / 2) / (double)stations[l1].getScale()) * 18D;
            }

            nomogramCanvas.setDistancesAndAmplitudes(ai, ad);
        }
        repaint();
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().removePropertyChangeListener(propertychangelistener);
    }

    public void run()
    {
        if(getMode() == 3)
        {
            double ad[] = new double[stations.length];
            maxDistance = 0.0D;
            for(int i = 0; i < stations.length; i++)
            {
                ad[i] = Math.sqrt(((double)stations[i].getX() - (double)epicenterLocation.x) * ((double)stations[i].getX() - (double)epicenterLocation.x) + ((double)stations[i].getY() - (double)epicenterLocation.y) * ((double)stations[i].getY() - (double)epicenterLocation.y)) * mapScale;
                if(ad[i] > maxDistance)
                    maxDistance = ad[i];
            }

            byte byte0 = 20;
            for(int j = 0; j < byte0; j++)
            {
                if(j < stations.length)
                    stationOffScreen[j] = stations[j].setupChartImage(getSize().width, getSize().height, ad[j], maxDistance, this, 100, r.nextLong());
                imageXAdjust = (int)(r.nextDouble() * 14D) - 7;
                imageYAdjust = (int)(r.nextDouble() * 14D) - 7;
                repaint();
                try
                {
                    Thread.sleep(250L);
                }
                catch(InterruptedException interruptedexception) { }
            }

            setQuakeRunning(false);
            imageXAdjust = 0;
            imageYAdjust = 0;
            repaint();
        }
    }

    public void saveIt()
    {
        try
        {
            String s;
            String s1;
            if(getMode() == 6 || getMode() == 7)
            {
                s = stringServer.getString("Seismogram");
                s1 = "save_seismogram_as";
            } else
            if(getMode() == 13 || getMode() == 14)
            {
                s = stringServer.getString("Nomogram");
                s1 = "save_nomogram_as";
            } else
            if(getMode() == 9)
            {
                s = stringServer.getString("TTC");
                s1 = "save_traveltimecurve_as";
            } else
            {
                s = stringServer.getString("Map");
                s1 = "save_map_as";
            }
            FileDialog filedialog = new FileDialog(EpicenterMagnitude.parentFrame, s1, 1);
            filedialog.setFile(s + ".gif");
            filedialog.show();
            if(System.getProperty("java.vendor").startsWith("Microsoft"))
                while(filedialog.isShowing()) 
                    try
                    {
                        Thread.sleep(250L);
                    }
                    catch(InterruptedException interruptedexception) { }
            String s2;
            if((s2 = filedialog.getFile()) != null)
            {
                String s3 = filedialog.getDirectory() + s2;
                if(s3.indexOf(".*.*") != -1)
                    s3 = s3.substring(0, s3.length() - 4);
                File file = new File(s3);
                if(getMode() == 13 || getMode() == 14)
                    nomogramCanvas.saveIt(file);
                else
                if(getMode() == 9)
                {
                    travelTimeCurveGraph.saveIt(file);
                } else
                {
                    GifEncoder gifencoder = new GifEncoder(bufferImage, new FileOutputStream(file));
                    gifencoder.encode();
                }
                System.out.println("Saved");
            }
        }
        catch(Exception exception)
        {
            System.out.println("Got exception");
            System.out.println(exception);
            exception.printStackTrace();
        }
        catch(Error error)
        {
            System.out.println("Got error");
            System.out.println(error);
        }
    }

    public void showFivePercentOffScreenMessage()
    {
        String s = stringServer.getString("off_screen_title");
        String s1 = stringServer.getString("off_screen_message_five_percent");
        EpicenterMagnitude _tmp = applet;
        MessageDialog messagedialog = new MessageDialog(EpicenterMagnitude.parentFrame, s, s1, 420, 300, bColor, Color.black);
        messagedialog.setBackground(dialogColor);
        messagedialog.setListener(new MessageDialogListener() {

            public void messageDialogDismissed(String s2)
            {
                stationOffScreen[selectedStationNumber] = false;
            }

        }
);
        messagedialog.show();
    }

    public void showOffScreenMessage()
    {
        showOffScreenMessage(false);
    }

    public void showOffScreenMessage(boolean flag)
    {
        String s = stringServer.getString("off_screen_title");
        String s1 = stringServer.getString("off_screen_message");
        EpicenterMagnitude _tmp = applet;
        MessageDialog messagedialog = new MessageDialog(EpicenterMagnitude.parentFrame, s, s1, 420, 300, bColor, Color.black);
        messagedialog.setBackground(dialogColor);
        messagedialog.setListener(new MessageDialogListener() {

            public void messageDialogDismissed(String s2)
            {
                stationOffScreen[selectedStationNumber] = false;
            }

        }
);
        messagedialog.show();
    }

    public String spsAllMeasured()
    {
        StringBuffer stringbuffer = new StringBuffer(30);
        for(int i = 0; i < stations.length; i++)
        {
            if(stations[i].spDraggingBegin != 0x7fffffff && stations[i].spDraggingEnd != 0x7fffffff && stations[i].ampDraggingY != 0x7fffffff)
                continue;
            if(stringbuffer.length() > 0)
                stringbuffer.append(", ");
            stringbuffer.append(i + 1);
        }

        if(stringbuffer.length() == 0)
            return null;
        if(stringbuffer.length() > 1)
        {
            stringbuffer.setCharAt(stringbuffer.length() - 3, ' ');
            stringbuffer.insert(stringbuffer.length() - 2, "and");
        }
        return stringbuffer.toString();
    }

    public void startQuake()
    {
        setQuakeRunning(true);
        setMode(3);
        th = new Thread(this);
        th.start();
    }

    public String stationsAllMeasured()
    {
        StringBuffer stringbuffer = new StringBuffer(30);
        for(int i = 0; i < stations.length; i++)
        {
            if(stations[i].getMeasuredDistanceRadius() != 0x7fffffff)
                continue;
            if(stringbuffer.length() > 0)
                stringbuffer.append(", ");
            stringbuffer.append(i + 1);
        }

        if(stringbuffer.length() == 0)
            return null;
        if(stringbuffer.length() > 1)
        {
            stringbuffer.setCharAt(stringbuffer.length() - 3, ' ');
            stringbuffer.insert(stringbuffer.length() - 2, "and");
        }
        return stringbuffer.toString();
    }

    public String stationsAllSet()
    {
        StringBuffer stringbuffer = new StringBuffer(20);
        for(int i = 0; i < stations.length; i++)
        {
            if(stations[i].isMovedFromInitialPoint())
                continue;
            if(stringbuffer.length() > 0)
                stringbuffer.append(", ");
            stringbuffer.append(stations[i].getNumber());
        }

        if(stringbuffer.length() == 0)
            return null;
        if(stringbuffer.length() > 1)
        {
            stringbuffer.insert(0, "stations ");
            stringbuffer.setCharAt(stringbuffer.length() - 3, ' ');
            stringbuffer.insert(stringbuffer.length() - 2, "and");
        } else
        if(stringbuffer.length() > 0)
            stringbuffer.insert(0, "station ");
        if(stringbuffer.length() > 0)
            stringbuffer.insert(0, "You have not yet moved ");
        String s;
        if(stringbuffer.length() > 0)
            s = stringbuffer.toString();
        else
            s = "";
        return s;
    }

    public void textFieldChanged()
    {
        if(getMode() == 9)
        {
            double d = 0.0D;
            double d1 = 0.0D;
            for(int j = 0; j < spIntervalTextFields.length; j++)
                try
                {
                    String s1 = EpicenterMagnitude.normalizeNumberString(spIntervalTextFields[j].getText());
                    double d2 = Double.valueOf(s1).doubleValue();
                    if(d2 > d)
                    {
                        d = d2;
                        d1 = (-0.1031D + Math.sqrt(0.010629609999999999D + -6.2570399999999998E-06D * d2)) / -3.1285199999999999E-06D;
                    }
                    d = (int)((d + 10D) / 10D) * 10;
                    d1 = (int)((d1 + 50D) / 10D) * 10;
                }
                catch(NumberFormatException numberformatexception2) { }

            travelTimeCurveGraph.setMaxes(d1 > 0.0D ? d1 : 500D, d > 0.0D ? d : 50D);
            double ad1[] = new double[spIntervalTextFields.length];
            for(int k = 0; k < ad1.length; k++)
                try
                {
                    String s2 = EpicenterMagnitude.normalizeNumberString(spIntervalTextFields[k].getText());
                    ad1[k] = Double.valueOf(s2).doubleValue();
                }
                catch(NumberFormatException numberformatexception3)
                {
                    ad1[k] = 0.0D;
                }

            travelTimeCurveGraph.setSPIntervals(ad1);
        } else
        if(getMode() == 13 || getMode() == 14)
        {
            int ai[] = new int[distanceTextFields.length];
            double ad[] = new double[amplitudeTextFields.length];
            for(int i = 0; i < ai.length; i++)
            {
                try
                {
                    ai[i] = Integer.parseInt(distanceTextFields[i].getText());
                }
                catch(NumberFormatException numberformatexception)
                {
                    ai[i] = 0;
                }
                try
                {
                    String s = EpicenterMagnitude.normalizeNumberString(amplitudeTextFields[i].getText());
                    ad[i] = Double.valueOf(s).doubleValue();
                }
                catch(NumberFormatException numberformatexception1)
                {
                    ad[i] = 0.0D;
                }
            }

            nomogramCanvas.setDistancesAndAmplitudes(ai, ad);
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void writeDataToStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(fieldMode);
        int i = -1;
        dataoutputstream.writeInt(stations.length);
        for(int j = 0; j < stations.length; j++)
        {
            if(selectedStation == stations[j])
                i = j;
            stations[j].writeDataToStream(dataoutputstream);
        }

        dataoutputstream.writeInt(i);
        dataoutputstream.writeBoolean(fieldQuakeRunning);
        dataoutputstream.writeDouble(maxDistance);
        if(epicenterLocation != null)
        {
            dataoutputstream.writeInt(epicenterLocation.x);
            dataoutputstream.writeInt(epicenterLocation.y);
        } else
        {
            dataoutputstream.writeInt(-1);
            dataoutputstream.writeInt(-1);
        }
        if(epicenterGuess != null)
        {
            dataoutputstream.writeInt(epicenterGuess.x);
            dataoutputstream.writeInt(epicenterGuess.y);
        } else
        {
            dataoutputstream.writeInt(-1);
            dataoutputstream.writeInt(-1);
        }
        dataoutputstream.writeDouble(magnitude);
        upperRight.writeDataToStream(dataoutputstream);
        lowerLeft.writeDataToStream(dataoutputstream);
        if(epicenterLLP == null)
        {
            dataoutputstream.writeBoolean(false);
        } else
        {
            dataoutputstream.writeBoolean(true);
            epicenterLLP.writeDataToStream(dataoutputstream);
        }
    }

    protected PropertyChangeSupport getPropertyChange()
    {
        if(propertyChange == null)
            propertyChange = new PropertyChangeSupport(this);
        return propertyChange;
    }

    protected void paintAmplitudeMessage(Graphics g, int i, int j, int k)
    {
        try
        {
            g.setFont(SEISMOGRAM_MESSAGES_FONT);
            FontMetrics fontmetrics = g.getFontMetrics();
            int l = Integer.parseInt(stringServer.getString("amp_measuring_message_num_lines"));
            String as[] = new String[l];
            int ai[] = new int[l];
            int i1 = 0;
            for(int j1 = 0; j1 < l; j1++)
            {
                as[j1] = stringServer.getString("amp_measuring_message_line_" + j1);
                ai[j1] = fontmetrics.stringWidth(as[j1]);
                i1 = Math.max(i1, ai[j1]);
            }

            int k1 = i + 13;
            int l1 = i + 15;
            if(i1 + k1 > j)
            {
                k1 = i - 13 - i1;
                l1 = i - 13;
            }
            g.setColor(SEISMOGRAM_MESSAGES_BACKGROUND_COLOR);
            for(int i2 = -3; i2 <= 3; i2++)
                g.drawLine(i + i2, k / 2 - 8, l1 + i2, k / 2 - 15);

            g.fillRect(k1, k / 2 - 15 - fontmetrics.getAscent() * l, i1 + 4, fontmetrics.getAscent() * l + 2);
            g.setColor(SEISMOGRAM_MESSAGES_COLOR);
            for(int j2 = 0; j2 < l; j2++)
                g.drawString(as[j2], k1, k / 2 - 15 - fontmetrics.getAscent() * (l - j2 - 1));

            g.drawLine(i, k / 2 - 7, l1, k / 2 - 15);
        }
        catch(NumberFormatException numberformatexception)
        {
            System.err.println("Could not read number of lines in seismogram message");
        }
    }

    protected void paintEpicenter(Graphics g, int i, int j, int k, Color color)
    {
        g.setColor(color);
        g.fillOval(i - k / 2, j - k / 2, k, k);
        g.setFont(epicenterFont);
        Color color1 = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
        g.setColor(color1);
        FontMetrics fontmetrics = g.getFontMetrics();
        String s = stringServer.getString("epicenter_letter");
        int l = fontmetrics.stringWidth(s);
        g.drawString(s, i - l / 2, (j + fontmetrics.getHeight() / 2) - 2);
        if(showEpicenterMessage)
        {
            String s1 = stringServer.getString("Epicenter");
            int i1 = fontmetrics.stringWidth(s1);
            int j1 = i + k;
            if(j1 + i1 > getSize().width)
                j1 = i - k - i1;
            int k1 = j;
            if(k1 + fontmetrics.getMaxAscent() > getSize().height)
                k1 = j - fontmetrics.getMaxAscent() - fontmetrics.getMaxDescent();
            g.setColor(color1);
            g.fillRect(j1, k1, i1 + 4, fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent());
            g.setColor(color);
            g.drawString(s1, j1 + 2, k1 + fontmetrics.getMaxAscent());
        }
    }

    protected void paintMeasuringDistanceCircles(Graphics g, int i, int j, Color color, Color color1)
    {
        for(int k = 0; k < stations.length; k++)
        {
            int l = stations[k].getMeasuredDistanceRadius();
            if(l != 0x7fffffff && l >= 0)
            {
                int i1 = stations[k].getX();
                int j1 = stations[k].getY();
                g.setColor(color);
                g.drawOval(i1 - l, j1 - l, l * 2, l * 2);
            }
        }

    }

    protected void paintMeasuringDistanceMessage(Graphics g, int i, int j)
    {
        g.setFont(MAP_MESSAGES_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        try
        {
            int k = Integer.parseInt(stringServer.getString("map_distance_message_num_lines"));
            String as[] = new String[k];
            int ai[] = new int[k];
            int l = 0;
            for(int i1 = 0; i1 < k; i1++)
            {
                as[i1] = stringServer.getString("map_distance_message_line_" + i1);
                ai[i1] = fontmetrics.stringWidth(as[i1]);
                l = Math.max(l, ai[i1]);
            }

            int j1 = i / 5;
            int k1 = j / 3;
            Rectangle rectangle = new Rectangle(j1 - 2, k1, l + 4, k * fontmetrics.getAscent() + 4);
            g.setColor(MAP_MESSAGES_COLOR);
            Point point = new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            for(int l1 = 0; l1 < stations.length; l1++)
                g.drawLine(point.x, point.y, stations[l1].getX(), stations[l1].getY());

            g.setColor(MAP_MESSAGES_BACKGROUND_COLOR);
            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g.setColor(MAP_MESSAGES_COLOR);
            for(int i2 = 0; i2 < k; i2++)
            {
                int j2 = k1 + fontmetrics.getAscent() * (i2 + 1);
                g.drawString(as[i2], j1, j2);
            }

        }
        catch(NumberFormatException numberformatexception)
        {
            System.err.println("Could not read number of lines in measuring distances message.");
        }
    }

    protected void paintSeismogramMessage(Graphics g, int i, int j)
    {
        try
        {
            g.setFont(SEISMOGRAM_MESSAGES_FONT);
            FontMetrics fontmetrics = g.getFontMetrics();
            int k = Integer.parseInt(stringServer.getString("sp_measuring_message_num_lines"));
            String as[] = new String[k];
            int ai[] = new int[k];
            int l = 0;
            for(int i1 = 0; i1 < k; i1++)
            {
                as[i1] = stringServer.getString("sp_measuring_message_line_" + i1);
                ai[i1] = fontmetrics.stringWidth(as[i1]);
                l = Math.max(l, ai[i1]);
            }

            g.setColor(SEISMOGRAM_MESSAGES_BACKGROUND_COLOR);
            for(int j1 = -3; j1 <= 3; j1++)
                g.drawLine(i + j1, j / 2 - 1, i + j1 + 15, j / 2 - 15);

            g.fillRect(i + 13, j / 2 - 15 - fontmetrics.getAscent() * k, l + 4, fontmetrics.getAscent() * k + 2);
            g.setColor(SEISMOGRAM_MESSAGES_COLOR);
            for(int k1 = 0; k1 < k; k1++)
                g.drawString(as[k1], i + 15, j / 2 - 15 - fontmetrics.getAscent() * (k - k1 - 1));

            g.drawLine(i, j / 2, i + 15, j / 2 - 15);
        }
        catch(NumberFormatException numberformatexception)
        {
            System.err.println("Could not read number of lines in seismogram message");
        }
    }

    protected void processMeasuringDistancesMouse(int i, int j, boolean flag)
    {
        if(stations[0] != null)
        {
            if(selectedStation == null)
            {
                int k = 0;
                do
                {
                    if(k >= stations.length)
                        break;
                    if(stations[k].contains(i, j))
                    {
                        selectedStation = stations[k];
                        selectedStationNumber = k;
                        selectedStation.setSelected(true);
                        break;
                    }
                    k++;
                } while(true);
            }
            if(selectedStation != null)
            {
                int l = -1;
                int ai[] = applet.getJournal().getStationNumbers();
                int i1 = 0;
                do
                {
                    if(i1 >= ai.length)
                        break;
                    if(selectedStationNumber == ai[i1])
                    {
                        l = i1;
                        break;
                    }
                    i1++;
                } while(true);
                distanceCircleRadius = (int)Math.round(Math.sqrt((i - selectedStation.getX()) * (i - selectedStation.getX()) + (j - selectedStation.getY()) * (j - selectedStation.getY())));
                String s = null;
                if(l >= 0)
                {
                    s = distanceTextFields[l].getText();
                    System.out.println("s = " + s);
                }
                double d = 0.0D;
                String s1 = Integer.toString((int)Math.round((double)distanceCircleRadius * mapScale));
                if(s != null)
                    try
                    {
                        double d1 = Double.valueOf(s).doubleValue();
                        if(Math.abs((double)distanceCircleRadius * mapScale - d1) < 5D)
                        {
                            distanceCircleRadius = (int)Math.round(d1 / mapScale);
                            if(distanceCircleRadius < 10000)
                                s1 = s;
                            else
                                s1 = null;
                        }
                    }
                    catch(NumberFormatException numberformatexception) { }
                selectedStation.setMeasuredDistanceRadius(distanceCircleRadius, s1);
                if(flag)
                {
                    selectedStation = null;
                    selectedStationNumber = -1;
                    String s2 = stationsAllMeasured();
                    if(s2 == null)
                        applet.doAction("distancesmeasured");
                }
            }
            justFlippedToThisMode = false;
            repaint();
        }
    }

    protected void processStationDraggingMouse(int i, int j, boolean flag)
    {
    }

    private void addPopUpMenu()
    {
        popup = new PopupMenu();
        for(int i = 0; i < menuLabels.length; i++)
        {
            String s = stringServer.getString(menuLabels[i]);
            if(s != null)
                menuLabels[i] = s;
            MenuItem menuitem = new MenuItem(menuLabels[i]);
            menuitem.setActionCommand(menuLabels[i]);
            menuitem.addActionListener(new PopupHandler());
            popup.add(menuitem);
        }

        add(popup);
    }

    private void checkOS()
    {
        if(System.getProperty("java.vendor").toLowerCase().startsWith("apple") && System.getProperty("java.version").startsWith("1.1"))
            isClassicMacOS = true;
    }

    private void createOverlayImage(int i, int j)
    {
        int ai[] = new int[i * j];
        for(int k = ai.length - 1; k >= 0; k--)
            ai[k] = 0x55ff0000;

        overlayImage = createImage(new MemoryImageSource(i, j, ai, 0, i));
        MediaTracker mediatracker = new MediaTracker(this);
        mediatracker.addImage(overlayImage, 1);
        try
        {
            mediatracker.waitForAll();
        }
        catch(InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
        for(int l = ai.length - 1; l >= 0; l--)
            ai[l] = 0x55888888;

        deselectedOverlayImage = createImage(new MemoryImageSource(i, j, ai, 0, i));
        mediatracker = new MediaTracker(this);
        mediatracker.addImage(deselectedOverlayImage, 2);
        try
        {
            mediatracker.waitForAll();
        }
        catch(InterruptedException interruptedexception1)
        {
            interruptedexception1.printStackTrace();
        }
    }

    private void paintEpicenterGuess(Graphics g, int i, int j, int k, int l)
    {
        if(epicenterGuess == null)
            epicenterGuess = new Point(i + k / 2, j + l / 2);
        if(justFlippedToThisMode && getMode() == 11)
            paintEpicenterMessage(g, k, l);
        paintEpicenter(g, i + epicenterGuess.x, j + epicenterGuess.y, 18, epicenterColor);
    }

    private void paintEpicenterMessage(Graphics g, int i, int j)
    {
        g.setFont(MAP_MESSAGES_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        try
        {
            int k = Integer.parseInt(stringServer.getString("map_epicenter_message_num_lines"));
            String as[] = new String[k];
            int ai[] = new int[k];
            int l = 0;
            for(int i1 = 0; i1 < k; i1++)
            {
                as[i1] = stringServer.getString("map_epicenter_message_line_" + i1);
                ai[i1] = fontmetrics.stringWidth(as[i1]);
                l = Math.max(l, ai[i1]);
            }

            int j1 = epicenterGuess.x + 15;
            if(j1 + l > i)
                j1 = epicenterGuess.x - 15 - l;
            int k1 = epicenterGuess.y - 15 - fontmetrics.getAscent() * k;
            if(k1 < 0)
                k1 = epicenterGuess.y + 15;
            Rectangle rectangle = new Rectangle(j1 - 2, k1, l + 4, k * fontmetrics.getAscent() + 4);
            g.setColor(MAP_MESSAGES_COLOR);
            g.drawLine(epicenterGuess.x, epicenterGuess.y, rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            g.setColor(MAP_MESSAGES_BACKGROUND_COLOR);
            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g.setColor(MAP_MESSAGES_COLOR);
            for(int l1 = 0; l1 < k; l1++)
            {
                int i2 = k1 + fontmetrics.getAscent() * (l1 + 1);
                g.drawString(as[l1], j1, i2);
            }

        }
        catch(NumberFormatException numberformatexception)
        {
            System.err.println("Could not read number of lines in epicenter message.");
        }
    }

    private void paintLatitudeLongitudeLines(Graphics g, int i, int j, int k, int l)
    {
        double d = upperRight.getLatitudeDouble();
        double d1 = lowerLeft.getLatitudeDouble();
        double d2 = lowerLeft.getLongitudeDouble();
        double d3 = upperRight.getLongitudeDouble();
        double d4 = (double)l / (d - d1);
        double d5 = (double)k / (d3 - d2);
        double d6 = -d1 * d4;
        double d7 = -d2 * d5;
        g.setFont(degreesFont);
        FontMetrics fontmetrics = g.getFontMetrics();
        double d8 = (int)d1;
        double d9;
        if(d8 < d)
            d9 = 0.25D;
        else
            d9 = -0.25D;
        for(; d8 < d1; d8 += d9);
        boolean flag = false;
        double d10 = d8;
        do
        {
            if(flag)
                break;
            int i1 = (l - (int)Math.round(d10 * d4 + d6)) + j;
            g.setColor(latitudeLinesColor);
            g.drawLine(i, i1, i + k, i1);
            d10 += d9;
            if(d8 < d)
            {
                if(d10 > d)
                    flag = true;
            } else
            if(d10 < d)
                flag = true;
        } while(true);
        double d11 = (int)d2;
        if(d11 < d3)
            d9 = 0.25D;
        else
            d9 = -0.25D;
        flag = false;
        for(; d11 < d2; d11 += d9);
        d10 = d11;
        do
        {
            if(flag)
                break;
            int j1 = (int)Math.round(d10 * d5 + d7) + i;
            g.setColor(latitudeLinesColor);
            g.drawLine(j1, j, j1, j + l);
            if(j1 - fontmetrics.getAscent() > 0 && d10 == (double)(int)d10)
            {
                String s = (int)Math.abs(d10) + "\260 ";
                if(d10 < 0.0D)
                    s = s + stringServer.getString("west_abbrev");
                else
                    s = s + stringServer.getString("east_abbrev");
                g.setColor(latitudeLinesTextColor);
                Image image1 = getRotatedImage(s, fontmetrics, degreesFont, latitudeLinesColor, latitudeLinesTextColor);
                if(image1 != null)
                {
                    g.drawImage(image1, j1 - image1.getWidth(this), j, this);
                } else
                {
                    int i2 = fontmetrics.stringWidth(s);
                    g.setColor(latitudeLinesColor);
                    g.fillRect(j1 - i2 - 4, 0, i2 + 3, fontmetrics.getMaxAscent() + 1);
                    g.setColor(latitudeLinesTextColor);
                    g.drawString(s, j1 - i2 - 2, fontmetrics.getAscent());
                }
                g.setColor(latitudeLinesColor);
                g.drawLine(j1 - 1, j, j1 - 1, j + l);
            }
            d10 += d9;
            if(d11 < d3)
            {
                if(d10 > d3)
                    flag = true;
            } else
            if(d10 < d3)
                flag = true;
        } while(true);
        d8 = (int)d1;
        if(d8 < d)
            d9 = 1.0D;
        else
            d9 = -1D;
        d10 = d8;
        flag = false;
        do
        {
            if(flag)
                break;
            int k1 = (l - (int)Math.round(d10 * d4 + d6)) + j;
            if(k1 - fontmetrics.getAscent() - 1 > 0)
            {
                String s1 = (int)Math.abs(d10) + "\260 ";
                if(d10 < 0.0D)
                    s1 = s1 + stringServer.getString("south_abbrev");
                else
                    s1 = s1 + stringServer.getString("north_abbrev");
                int l1 = fontmetrics.stringWidth(s1);
                g.setColor(latitudeLinesColor);
                g.fillRect((i + k) - l1 - 4, k1 - 1 - fontmetrics.getMaxAscent(), l1 + 3, fontmetrics.getMaxAscent() + 1);
                g.drawLine(i, k1 - 1, i + k, k1 - 1);
                g.setColor(latitudeLinesTextColor);
                g.drawString(s1, (i + k) - l1 - 2, k1 - 1);
            }
            d10 += d9;
            if(d8 < d)
            {
                if(d10 > d)
                    flag = true;
            } else
            if(d10 < d)
                flag = true;
        } while(true);
    }

    private void processClickToViewMouse(int i, int j)
    {
    }

    private void processFindEpicenterMouse(int i, int j, boolean flag)
    {
        int k = image.getWidth(this);
        int l = image.getHeight(this);
        if(i < 9)
            i = 9;
        if(i > k - 9)
            i = k - 9;
        if(j < 9)
            j = 9;
        if(j > l - 9)
            j = l - 9;
        if(epicenterGuess == null)
        {
            epicenterGuess = new Point(i, j);
        } else
        {
            epicenterGuess.x = i;
            epicenterGuess.y = j;
        }
        justFlippedToThisMode = false;
        repaint();
        if(flag && epicenterGuess != null)
            applet.doAction("epicenterplaced");
    }

    private void processViewingSeismogramMouse(int i, int j, boolean flag)
    {
        if(getMode() == 6)
        {
            if(selectedStation.spDraggingBegin == 0x7fffffff)
                selectedStation.spDraggingBegin = i;
            selectedStation.spDraggingEnd = i;
            repaint();
        } else
        if(getMode() == 7)
            if(stationOffScreen[selectedStationNumber])
            {
                stationOffScreen[selectedStationNumber] = false;
                if(scaleChoice.getSelectedIndex() == 0)
                    showFivePercentOffScreenMessage();
                else
                    showOffScreenMessage(true);
            } else
            {
                selectedStation.ampDraggingY = j;
                if(selectedStation.ampDraggingX == 0x7fffffff)
                    selectedStation.ampDraggingX = i;
                repaint();
            }
    }

    public static final int ALL_DONE_MODE = 14;
    public static final int CLICK_TO_VIEW_HAVE_DONE_ONCE_MODE = 8;
    public static final int CLICK_TO_VIEW_SEISMOGRAM_MODE = 5;
    public static final int DEFAULT_DISTANCE_FRACTION_DIGITS = 1;
    public static final int EPICENTER_DIAMETER = 18;
    public static final int FIND_EPICENTER_MODE = 11;
    public static final int LATITUDE_LONGITUDE_MODE = 12;
    public static final Color MAP_MESSAGES_BACKGROUND_COLOR;
    public static final Color MAP_MESSAGES_COLOR;
    public static final Font MAP_MESSAGES_FONT = new Font("Helvetica", 1, 14);
    public static final int MEASURING_DISTANCES_MODE = 10;
    public static final int NOMOGRAM_MODE = 13;
    public static final int NOT_STARTED_MODE = 0;
    public static final Font PRINT_TITLE_FONT = new Font("Helvetica", 1, 14);
    public static final int QUAKE_RUNNING_MODE = 3;
    public static final int QUAKE_STOPPED_MODE = 4;
    public static final Color SEISMOGRAM_MESSAGES_BACKGROUND_COLOR;
    public static final Color SEISMOGRAM_MESSAGES_COLOR;
    public static final Font SEISMOGRAM_MESSAGES_FONT = new Font("Helvetica", 1, 14);
    public static final int STATIONS_DRAGGED_MODE = 2;
    public static final int STATION_DRAGGING_MODE = 1;
    public static final int TO_DO_MODE = 15;
    public static final int TRAVEL_TIME_CURVE_MODE = 9;
    public static final int VIEWING_SEISMOGRAM_MODE = 6;
    public static final int VIEWING_SEISMOGRAM_MODE_AMPLITUDE = 7;
    public static final Color amplitudeDraggingColor;
    public static final Font degreesFont = new Font("sansserif", 0, 9);
    public static final Color dialogColor = new Color(255, 204, 102);
    public static final Color distanceTextColor;
    public static final Color epicenterColor;
    public static final Font epicenterFont = new Font("sansserif", 1, 12);
    public static final Color latitudeLinesColor = new Color(255, 255, 102);
    public static final Color latitudeLinesTextColor;
    public static final Color measuringDistanceCircleColor;
    public static Frame offFrame = null;
    public static final Font scaleFont;
    public static final Color stationDistanceCircleColor;
    public static final Font spFont;
    public String menuLabels[] = {
        "Print", "Save"
    };
    public SeismographStation stations[];
    protected transient PropertyChangeSupport propertyChange;
    protected int imageXAdjust;
    protected int imageYAdjust;
    Color bColor;
    MapEventHandler mapEventHandler;
    Thread th;
    private Choice scaleChoice;
    private Choice stationChoice;
    private EECButton triggerEarthquakeButton;
    private EpicenterMagnitude applet;
    private Hashtable rotatedImages;
    private Image bufferImage;
    private Image deselectedOverlayImage;
    private Image image;
    private Image overlayImage;
    private Label scaleLabel;
    private LatitudeLongitudePoint epicenterLLP;
    private LatitudeLongitudePoint lowerLeft;
    private LatitudeLongitudePoint upperRight;
    private NomogramCanvas nomogramCanvas;
    private NumberFormat amplitudeFormat;
    private NumberFormat distanceFormat;
    private NumberFormat spFormat;
    private Point epicenterGuess;
    private Point epicenterLocation;
    private PopupMenu popup;
    private Random r;
    private SeismographStation draggingStation;
    private SeismographStation selectedStation;
    private StringServer stringServer;
    private TCCGraph travelTimeCurveGraph;
    private ToDo toDo;
    private TextField amplitudeTextFields[];
    private TextField distanceTextFields[];
    private TextField spIntervalTextFields[];
    private boolean stationOffScreen[];
    private boolean amplitudeMeasuredWell;
    private boolean fieldQuakeRunning;
    private boolean isClassicMacOS;
    private boolean justFlippedToThisMode;
    private boolean nothingDone;
    private boolean showEpicenterMessage;
    private boolean spIntervalMeasuringMouseDown;
    private boolean spMeasuredWell;
    private double magnitude;
    private double mapScale;
    private double maxDistance;
    private int distanceCircleRadius;
    private int fieldMode;
    private int fieldPreferredHeight;
    private int fieldPreferredWidth;
    private int selectedStationNumber;

    static 
    {
        MAP_MESSAGES_BACKGROUND_COLOR = Color.gray;
        MAP_MESSAGES_COLOR = Color.yellow;
        SEISMOGRAM_MESSAGES_BACKGROUND_COLOR = Color.white;
        SEISMOGRAM_MESSAGES_COLOR = Color.blue;
        amplitudeDraggingColor = Color.gray;
        distanceTextColor = Color.yellow;
        epicenterColor = Color.blue;
        latitudeLinesTextColor = Color.black;
        measuringDistanceCircleColor = Color.orange;
        scaleFont = new Font("sansserif", 0, 10);
        stationDistanceCircleColor = Color.yellow;
        spFont = scaleFont;
    }




}
