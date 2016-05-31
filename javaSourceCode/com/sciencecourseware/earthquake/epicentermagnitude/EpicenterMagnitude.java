// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EpicenterMagnitude.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import com.ms.util.SystemVersionManager;
import com.sciencecourseware.components.ConfirmDialog;
import com.sciencecourseware.components.ConfirmDialogListener;
import com.sciencecourseware.components.EECButton;
import com.sciencecourseware.components.ImageServer;
import com.sciencecourseware.components.InsetContainer;
import com.sciencecourseware.components.InstructionText;
import com.sciencecourseware.components.InstructionTextXML;
import com.sciencecourseware.components.MessageDialog;
import com.sciencecourseware.components.MessageDialogListener;
import com.sciencecourseware.components.StringServer;
import com.sciencecourseware.components.TabButtonsPanel;
import com.sciencecourseware.components.ToolTipCanvas;
import edu.stanford.ejalbert.BrowserLauncher;
import electric1.util.Nodes;
import electric1.xml.Child;
import electric1.xml.Document;
import electric1.xml.Element;
import electric1.xml.Elements;
import electric1.xml.Parent;
import electric1.xml.XMLDecl;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            Journal, MapComponent, Earth, EarthquakeData, 
//            PrintDialog, SaveDialog, DialogListener, ToDo, 
//            SeismographStation, NomogramCanvas, LatitudeLongitudePoint

public class EpicenterMagnitude extends Applet
    implements DialogListener, MessageDialogListener, Runnable, ConfirmDialogListener
{
    public class ToolboxHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            for(int i = 0; i < toolboxButtons.length; i++)
                toolboxButtons[i].setSelected(false);

            ((EECButton)actionevent.getSource()).setSelected(true);
            System.out.println("In ToolboxHandler, action command to send is \"" + actionevent.getActionCommand() + "\"");
            doAction(actionevent.getActionCommand());
        }

        public ToolboxHandler()
        {
        }
    }

    public class TheEventHandler
        implements ActionListener, PropertyChangeListener, ConfirmDialogListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            if(actionevent.getSource() == getTabButtonsPanel())
                tabButtonsPanel_Action();
            if(actionevent.getSource() == getExpandCollapseButton())
            {
                toggleTop();
                getExpandCollapseButton().toggleLabel();
            }
            if(actionevent.getSource() == getPrintButton())
                printButton_Action();
            if(actionevent.getSource() == getTutorialButton())
                tutorialButton_Action();
            if(actionevent.getSource() == getSaveButton())
                if(saveButton.getShowAltLabel())
                    loadSession();
                else
                    saveButton_Action();
            if((actionevent.getSource() instanceof InstructionText) || actionevent.getActionCommand().startsWith("url"))
                instructionTextAction(actionevent);
            if(actionevent.getSource() == getStartQuitButton())
            {
                if(quitDialog == null)
                {
                    quitDialog = new ConfirmDialog(EpicenterMagnitude.parentFrame, stringServer.getString("quit_confirm_title"), stringServer.getString("quit_confirm_text"), 470, 120, bColor, Color.black, stringServer);
                    quitDialog.setBackground(bColor);
                    quitDialog.setListener(this);
                }
                quitDialog.setVisible(true);
            }
        }

        public void confirmDialogClosed(boolean flag)
        {
            if(flag)
                try
                {
                    URL url = new URL(getDocumentBase(), quitPage);
                    if(quitTarget == null)
                        getAppletContext().showDocument(url);
                    else
                        getAppletContext().showDocument(url, quitTarget);
                }
                catch(MalformedURLException malformedurlexception)
                {
                    System.err.println(malformedurlexception);
                }
        }

        public void propertyChange(PropertyChangeEvent propertychangeevent)
        {
            if(propertychangeevent.getSource() == getMapComponent() && propertychangeevent.getPropertyName().equals("quakeRunning"))
                connPtoP2SetTarget();
            if(propertychangeevent.getSource() == EpicenterMagnitude.this && propertychangeevent.getPropertyName().equals("quakeRunning"))
                connPtoP2SetSource();
        }

        ConfirmDialog quitDialog;

        public TheEventHandler()
        {
            quitDialog = null;
        }
    }

    public class HistoricalEarthquakeHandler
        implements ItemListener
    {

        public void itemStateChanged(ItemEvent itemevent)
        {
            setEarthquakeUsing(earthquakeData);
        }

        public EarthquakeData earthquakeData;

        public HistoricalEarthquakeHandler()
        {
            this(null);
        }

        public HistoricalEarthquakeHandler(EarthquakeData earthquakedata)
        {
            earthquakeData = earthquakedata;
        }
    }

    public class HelpHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            try
            {
                URL url = getDocumentBase();
                String s = getParameter("usebrowserlauncher");
                if(s != null && (s.startsWith("t") || s.startsWith("T") || s.startsWith("y") || s.startsWith("Y") || s.startsWith("1")))
                {
                    try
                    {
                        BrowserLauncher.openURL("http://localhost/eec/Earthquake/dohelp.php?context=epicentermagnitude_" + helpContext);
                    }
                    catch(IOException ioexception)
                    {
                        ioexception.printStackTrace();
                    }
                } else
                {
                    URL url1 = new URL(url, "../dohelp.php?context=epicentermagnitude_" + helpContext);
                    System.out.println("Help: " + url1);
                    getAppletContext().showDocument(url1, "scripting");
                }
            }
            catch(MalformedURLException malformedurlexception)
            {
                System.err.println(malformedurlexception);
            }
        }

        public HelpHandler()
        {
        }
    }


    public EpicenterMagnitude()
    {
        buttonLabelColor = Color.yellow;
        saveButton = null;
        buttonFont = new Font("Helvetica", 1, 12);
        instructions = null;
        imageServer = new ImageServer(this);
        mapComponent = null;
        helpContext = "movingstations";
        language = "English";
        mapImageName = "map";
        stringServer = null;
        toolTipCanvas = null;
        tabLabels = null;
        setEpicenter = false;
        glossaryterms = null;
        info = null;
        eecHomeTarget = null;
        username = null;
        eecHomeURL = null;
        bColor = new Color(255, 204, 102);
        instructionColor = new Color(204, 153, 51);
        myBeigeColor = new Color(255, 255, 206);
        theButtonColor = new Color(204, 153, 0);
        startOverDialog = null;
        borderTitleFont = new Font("Helvetica", 1, 16);
        theEventHandler = new TheEventHandler();
        doingMultiple = false;
        earthStep = 0;
        numBTRedraws = 0;
        whatToPrint = 0;
        glassBreaking = null;
        instructionsContainerCardLayout = null;
        expandCollapseButton = null;
        printButton = null;
        startQuitButton = null;
        toolboxHelpButton = null;
        tutorialButton = null;
        radioButtonFont = new Font("Helvetica", 0, 11);
        toolTipFont = new Font("Helvetica", 0, 10);
        topButtonFont = new Font("Helvetica", 1, 12);
        buttonContainerGridLayout = null;
        buttonContainer = null;
        expandCollapseButtonContainer = null;
        instructionsContainer = null;
        tabCardContainer = null;
        tabContainer = null;
        tabTopContainer = null;
        toolboxContainer = null;
        assignments = null;
        background = null;
        instructionText = null;
        stationDraggingInstructionText = null;
        journal = null;
        magnitudeFormat = null;
        printDialog = null;
        saveDialog = null;
        assignmentsScrollPane = null;
        backgroundScrollPane = null;
        glossaryScrollPane = null;
        infoScrollPane = null;
        journalScrollPane = null;
        appletCompleted = "/eec/Earthquake/applet_done.php";
        appletCompletedTarget = "scripting";
        fieldInstructionTextUsing = "Click START to start the Epicenter and Magnitude activity, or select another activity.";
        quitPage = "../quitapplet.php";
        quitTarget = null;
        tabButtonsPanel = null;
        historicalEarthquakes = null;
        allPointsPlotted = false;
        doLoad = false;
        ivjConnPtoP1Aligning = false;
        ivjConnPtoP2Aligning = false;
        quakeRunning = false;
        startedQuake = false;
        fieldPageOn = 1;
    }

    public static String booleanToString(boolean flag)
    {
        if(flag)
            return "true";
        else
            return "false";
    }

    public static Frame parentOf(Component component)
    {
        if(component instanceof Frame)
            return (Frame)component;
        if(component == null)
            return new Frame();
        else
            return parentOf(((Component) (component.getParent())));
    }

    public String getAppletInfo()
    {
        return "Epicenter and Magnitude Applet\n\nAllows user to find the location of the epicenter and the magnitude of an earthquake based upon the seismograms recorded by various seismograph stations.\n@author David_Risner\n@version Date: 2006/02/06";
    }

    public void setEarthquakeUsing(EarthquakeData earthquakedata)
    {
        if(mapComponent.isNothingDone())
        {
            somethingDone();
            mapComponent.setNothingDone(false);
        }
        if(earthquakedata == null)
        {
            mapImageName = historicalEarthquakes[0].getMapName();
            String s = historicalEarthquakes[0].getButtonBackgroundName();
            mapComponent.setImage(imageServer.getImage(mapImageName), imageServer.getImage(s));
            mapComponent.setMapLatitudeLongitude(historicalEarthquakes[0].getMapUpperRight(), historicalEarthquakes[0].getMapLowerLeft());
            mapComponent.setMapScale(historicalEarthquakes[0].getMapScale());
            mapComponent.setEpicenterAndMagnitude(null, 1.7976931348623157E+308D);
            mapComponent.setStations(historicalEarthquakes[0].getStationNames(), historicalEarthquakes[0].getStationLocations());
        } else
        {
            System.out.println("Setting earthquake to:\n" + earthquakedata.toString());
            mapImageName = earthquakedata.getMapName();
            String s1 = earthquakedata.getButtonBackgroundName();
            mapComponent.setImage(imageServer.getImage(mapImageName), imageServer.getImage(s1));
            mapComponent.setMapLatitudeLongitude(earthquakedata.getMapUpperRight(), earthquakedata.getMapLowerLeft());
            mapComponent.setMapScale(earthquakedata.getMapScale());
            mapComponent.setEpicenterAndMagnitude(earthquakedata.getLocation(), earthquakedata.getMagnitude());
            mapComponent.setStations(earthquakedata.getStationNames(), earthquakedata.getStationLocations());
        }
        mapComponent.repaint();
    }

    public void setInstructionTextUsing(String s)
    {
        Element element = (Element)instructions.get(s);
        if(element != null)
            getInstructionText().setRoot(element);
    }

    public Journal getJournal()
    {
        if(journal == null)
            try
            {
                journal = new Journal(this, stringServer);
                journal.setName("Journal");
                journal.setForeground(getForeground());
                String s = getParameter("fullname");
                if(s != null)
                    journal.setFullName(s);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return journal;
    }

    public MapComponent getMapComponent()
    {
        if(mapComponent == null)
            try
            {
                mapComponent = new MapComponent(this, stringServer);
                mapComponent.setName("MapComponent");
                mapImageName = historicalEarthquakes[0].getMapName();
                String s = historicalEarthquakes[0].getButtonBackgroundName();
                mapComponent.setImage(imageServer.getImage(mapImageName), imageServer.getImage(s));
                mapComponent.setMapScale(historicalEarthquakes[0].getMapScale());
                mapComponent.setMapLatitudeLongitude(historicalEarthquakes[0].getMapUpperRight(), historicalEarthquakes[0].getMapLowerLeft());
                mapComponent.setDistanceFractionDigits(0);
                mapComponent.setPreferredHeight(280);
                mapComponent.setPreferredWidth(619);
                mapComponent.setBounds(10, 43, 620, 280);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return mapComponent;
    }

    public void setPageOn(int i)
    {
        int j = fieldPageOn;
        fieldPageOn = i;
        firePropertyChange("pageOn", new Integer(j), new Integer(i));
    }

    public int getPageOn()
    {
        return fieldPageOn;
    }

    public void setQuakeRunning(boolean flag)
    {
        boolean flag1 = quakeRunning;
        quakeRunning = flag;
        firePropertyChange("newQuakeRunning", new Boolean(flag1), new Boolean(flag));
        if(!flag)
            quakeStopped();
    }

    public boolean getQuakeRunning()
    {
        return quakeRunning;
    }

    public ToolTipCanvas getToolTipCanvas()
    {
        if(toolTipCanvas == null)
        {
            toolTipCanvas = new ToolTipCanvas();
            toolTipCanvas.setBounds(0, 0, 20, 10);
            toolTipCanvas.setBackground(Color.yellow);
            toolTipCanvas.setForeground(Color.blue);
            toolTipCanvas.setFont(toolTipFont);
        }
        return toolTipCanvas;
    }

    public void addDataToXML(Element element)
    {
        element.addElement(new Element("sessionversion")).setText("EEC Earthquake Epicenter and Magnitude Applet v1.0");
        element.addElement(new Element("instructiontext")).setText(fieldInstructionTextUsing);
        Element element1 = new Element("quakerunning");
        element1.setText(booleanToString(quakeRunning));
        element.addElement(element1);
        element1 = new Element("startedquake");
        element1.setText(booleanToString(startedQuake));
        element.addElement(element1);
        element1 = new Element("allpointsplotted");
        element1.setText(booleanToString(allPointsPlotted));
        element.addElement(element1);
        element.addElement(new Element("helpcontext")).setText(helpContext);
        element1 = new Element("setepicenter");
        element1.setText(booleanToString(setEpicenter));
        element.addElement(element1);
        Element element2 = new Element("toolbox");
        element2.setAttribute("num", Integer.toString(toolboxButtons.length));
        element.addElement(element2);
        for(int i = 0; i < toolboxButtons.length; i++)
        {
            element1 = new Element("button" + i);
            element1.setAttribute("enabled", booleanToString(toolboxButtons[i].isEnabled()));
            element1.setAttribute("selected", booleanToString(toolboxButtons[i].isSelected()));
            element2.addElement(element1);
        }

        element.addElement(new Element("toolboxzerostate")).setText(toolboxButtons[0].getActionCommand());
        element.addElement(new Element("mapimagename")).setText(mapImageName);
        element1 = new Element("mapcomponent");
        mapComponent.addDataToXML(element1);
        element.addElement(element1);
        element1 = new Element("journal");
        journal.addDataToXML(element1);
        element.addElement(element1);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().addPropertyChangeListener(propertychangelistener);
    }

    public void answersVerified()
    {
        setInstructionTextUsing("alldone");
        helpContext = "alldone";
        getMapComponent().getToDo().setCompleted(5);
        try
        {
            System.out.println("appletCompleted = " + appletCompleted);
            URL url = new URL(getDocumentBase(), appletCompleted);
            System.out.println("Opening " + url);
            getAppletContext().showDocument(url, appletCompletedTarget);
        }
        catch(MalformedURLException malformedurlexception)
        {
            System.err.println(malformedurlexception);
        }
    }

    public static String normalizeNumberString(String s)
    {
        System.out.println("normalizeNumberString in = \"" + s + "\"");
        if(s.indexOf(',') >= 0 && s.indexOf('.') < 0)
        {
            StringBuffer stringbuffer = new StringBuffer();
            int i = s.length();
            for(int j = 0; j < i; j++)
            {
                char c = s.charAt(j);
                if(c == ',')
                    stringbuffer.append('.');
                else
                    stringbuffer.append(c);
            }

            System.out.println("normalizeNumberString out, touched = \"" + stringbuffer.toString() + "\"");
            return stringbuffer.toString();
        } else
        {
            System.out.println("normalizeNumberString out, not touched = \"" + s + "\"");
            return s;
        }
    }

    public static String outputNumberString(String s)
    {
        if(outputNumbersWithCommas)
        {
            StringBuffer stringbuffer = new StringBuffer();
            int i = s.length();
            for(int j = 0; j < i; j++)
            {
                char c = s.charAt(j);
                if(c == '.')
                {
                    stringbuffer.append(',');
                    continue;
                }
                if(c == ',')
                    stringbuffer.append(' ');
                else
                    stringbuffer.append(c);
            }

            return stringbuffer.toString();
        } else
        {
            return s;
        }
    }

    public boolean checkAmplitudes(int ai[], TextField atextfield[])
    {
        stripStarsFromTextFields(atextfield);
        boolean flag = false;
        NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
        for(int i = 0; i < atextfield.length; i++)
        {
            try
            {
                String s = normalizeNumberString(atextfield[i].getText());
                Number number = numberformat.parse(s);
                double d = number.doubleValue();
                double d1 = mapComponent.getStation(ai[i]).getPAmplitude();
                if(d > 0.0D && d1 > 0.0D)
                {
                    double d2 = NomogramCanvas.log10(d);
                    double d3 = NomogramCanvas.log10(d1);
                    if(Math.abs(d2 - d3) > 0.10000000000000001D)
                    {
                        flag = true;
                        atextfield[i].setText(atextfield[i].getText() + "***");
                    }
                } else
                {
                    atextfield[i].setText(atextfield[i].getText() + "***");
                    flag = true;
                }
                continue;
            }
            catch(ParseException parseexception)
            {
                atextfield[i].setText("***");
            }
            flag = true;
        }

        return flag;
    }

    public boolean checkDistances(int ai[], TextField atextfield[])
    {
        stripStarsFromTextFields(atextfield);
        boolean flag = false;
        for(int i = 0; i < atextfield.length; i++)
        {
            try
            {
                String s = normalizeNumberString(atextfield[i].getText());
                int j = Integer.parseInt(s);
                if(Math.abs((double)j - mapComponent.getStation(ai[i]).getDistance()) > 20D)
                {
                    flag = true;
                    atextfield[i].setText(atextfield[i].getText() + "***");
                }
                continue;
            }
            catch(NumberFormatException numberformatexception)
            {
                atextfield[i].setText("***");
            }
            flag = true;
        }

        return flag;
    }

    public boolean checkEpicenter(TextField atextfield[], Choice choice, TextField atextfield1[], Choice choice1)
    {
        stripStarsFromTextFields(atextfield);
        stripStarsFromTextFields(atextfield1);
        String s = atextfield[0].getText();
        String s1 = atextfield[1].getText();
        String s2 = choice.getSelectedItem();
        String s3 = atextfield1[0].getText();
        String s4 = atextfield1[1].getText();
        LatitudeLongitudePoint latitudelongitudepoint = mapComponent.getEpicenterLatitudeLongitude();
        String s5 = choice1.getSelectedItem();
        double d = latitudelongitudepoint.getLatitudeDouble();
        double d1 = latitudelongitudepoint.getLongitudeDouble();
        double d2 = 1.7976931348623157E+308D;
        double d3 = 1.7976931348623157E+308D;
        int i = 0x7fffffff;
        boolean flag = false;
        try
        {
            i = Integer.parseInt(s);
        }
        catch(NumberFormatException numberformatexception) { }
        int j = 0x7fffffff;
        try
        {
            j = Integer.parseInt(s1);
        }
        catch(NumberFormatException numberformatexception1)
        {
            j = 0;
        }
        if(i != 0x7fffffff && j != 0x7fffffff)
        {
            d2 = LatitudeLongitudePoint.convertToDouble(i, j, s2);
            if(Math.abs(Math.abs(d2) - Math.abs(latitudelongitudepoint.getLatitudeDouble())) > 0.5D)
            {
                atextfield[0].setText(s + "***");
                atextfield[1].setText(s1 + "***");
                flag = true;
            }
            if(!choice.getSelectedItem().equals(latitudelongitudepoint.getLatitudeDirection()))
            {
                choice.add("***");
                choice.select("***");
                flag = true;
            }
        } else
        {
            atextfield[0].setText("***");
            atextfield[1].setText("***");
            flag = true;
        }
        i = 0x7fffffff;
        j = 0x7fffffff;
        try
        {
            i = Integer.parseInt(s3);
        }
        catch(NumberFormatException numberformatexception2) { }
        try
        {
            j = Integer.parseInt(s4);
        }
        catch(NumberFormatException numberformatexception3)
        {
            j = 0;
        }
        if(i != 0x7fffffff && j != 0x7fffffff)
        {
            d3 = LatitudeLongitudePoint.convertToDouble(i, j, s5);
            if(Math.abs(Math.abs(d3) - Math.abs(latitudelongitudepoint.getLongitudeDouble())) > 0.5D)
            {
                atextfield1[0].setText(s3 + "***");
                atextfield1[1].setText(s4 + "***");
                flag = true;
            }
            if(!choice1.getSelectedItem().equals(latitudelongitudepoint.getLongitudeDirection()))
            {
                choice1.add("***");
                choice1.select("***");
                flag = true;
            }
        } else
        {
            atextfield1[0].setText("***");
            atextfield1[1].setText("***");
            flag = true;
        }
        if(d2 == 1.7976931348623157E+308D)
            flag = true;
        if(d3 == 1.7976931348623157E+308D)
            flag = true;
        return flag;
    }

    public boolean checkMagnitudes(TextField atextfield[], Label label)
    {
        stripStarsFromTextFields(atextfield);
        boolean flag = false;
        double d = 0.0D;
        int i = 0;
        NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
        for(int j = 0; j < atextfield.length; j++)
        {
            try
            {
                String s = normalizeNumberString(atextfield[j].getText());
                Number number = numberformat.parse(s);
                double d2 = number.doubleValue();
                d += d2;
                i++;
                System.out.println("Magnitude check,  station #" + j + ", entered=" + d2 + ", actual=" + mapComponent.getMagnitude());
                if(Math.abs(d2 - mapComponent.getMagnitude()) > 0.10000000000000001D)
                {
                    atextfield[j].setText(atextfield[j].getText() + "***");
                    flag = true;
                }
                continue;
            }
            catch(ParseException parseexception)
            {
                atextfield[j].setText("***");
            }
            flag = true;
        }

        if(i > 0)
        {
            double d1 = d / (double)i;
            if(magnitudeFormat == null)
            {
                magnitudeFormat = NumberFormat.getNumberInstance(Locale.US);
                magnitudeFormat.setMaximumFractionDigits(2);
                magnitudeFormat.setMinimumFractionDigits(2);
            }
            String s1 = outputNumberString(magnitudeFormat.format(d1));
            if(Math.abs(d1 - mapComponent.getMagnitude()) > 0.10000000000000001D)
            {
                label.setText(s1 + "***");
                flag = true;
            } else
            {
                label.setText(s1);
            }
        } else
        {
            label.setText("***");
            flag = true;
        }
        return flag;
    }

    public boolean checkSpIntervals(int ai[], TextField atextfield[])
    {
        stripStarsFromTextFields(atextfield);
        boolean flag = false;
        NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
        for(int i = 0; i < atextfield.length; i++)
        {
            try
            {
                String s = normalizeNumberString(atextfield[i].getText());
                Number number = numberformat.parse(s);
                double d = number.doubleValue();
                if(Math.abs(d - mapComponent.getStation(ai[i]).getActualSPInterval()) > 3D)
                {
                    flag = true;
                    atextfield[i].setText(atextfield[i].getText() + "***");
                }
                continue;
            }
            catch(ParseException parseexception)
            {
                atextfield[i].setText("***");
            }
            flag = true;
        }

        return flag;
    }

    public void confirmDialogClosed(boolean flag)
    {
        if(flag)
        {
            journal.clearTextFields();
            toolboxButtons[0].setActionCommand(triggerEarthquake);
            mapComponent.setMode(1);
            instructionsContainerCardLayout.show(getInstructionsContainer(), "radiobuttons");
            mapImageName = historicalEarthquakes[0].getMapName();
            String s = historicalEarthquakes[0].getButtonBackgroundName();
            mapComponent.setImage(imageServer.getImage(mapImageName), imageServer.getImage(s));
            mapComponent.setMapScale(historicalEarthquakes[0].getMapScale());
            mapComponent.setMapLatitudeLongitude(historicalEarthquakes[0].getMapUpperRight(), historicalEarthquakes[0].getMapLowerLeft());
            mapComponent.setStations(historicalEarthquakes[0].getStationNames(), historicalEarthquakes[0].getStationLocations());
            mapComponent.setEpicenterAndMagnitude(null, 1.7976931348623157E+308D);
            for(int i = 0; i < toolboxButtons.length; i++)
            {
                toolboxButtons[i].setSelected(false);
                toolboxButtons[i].setEnabled(i == 0);
            }

            ToDo todo = getMapComponent().getToDo();
            for(int j = 0; j < 6; j++)
                todo.clearCompleted();

            Checkbox checkbox = historicalEarthquakeButtons[historicalEarthquakeButtons.length - 1];
            historicalEarthquakeGroup.setSelectedCheckbox(checkbox);
        }
    }

    public void dialogClosed(Dialog dialog)
    {
        if(dialog == printDialog)
        {
            whatToPrint = printDialog.getWhatToPrint();
            int i = 0;
            if((whatToPrint & 1) > 0)
                i++;
            if((whatToPrint & 2) > 0)
                i++;
            if((whatToPrint & 4) > 0)
                i++;
            if((whatToPrint & 8) > 0)
                i++;
            if((whatToPrint & 0x10) > 0)
                i++;
            if((whatToPrint & 0x20) > 0)
                i++;
            if(i > 1)
                doingMultiple = true;
            else
                doingMultiple = false;
            nextPrintJob();
        } else
        if(dialog == saveDialog)
        {
            int j = saveDialog.getWhatToSave();
            if((j & 1) > 0)
                mapComponent.saveIt();
            if((j & 2) > 0)
                saveJournal();
            if((j & 4) > 0)
                saveBackground();
            if((j & 8) > 0)
                saveAssignments();
            if((j & 0x10) > 0)
                saveGlossaryTerms();
            if((j & 0x20) > 0)
                saveInfo();
            if((j & 0x40) > 0)
                saveSession();
        }
    }

    public void firePropertyChange(String s, Object obj, Object obj1)
    {
        getPropertyChange().firePropertyChange(s, obj, obj1);
    }

    public void init()
    {
        System.out.println("Epicenter Location and Richter Magnitude Determination applet");
        System.out.println("Copyright (C) 2001-6 Virtual Courseware Project");
        System.out.println("http://www.sciencecourseware.org/");
        System.out.println("Version Date: 2006/02/06");
        outputSystemProperties();
        super.init();
        getParameters();
        loadStrings();
        LatitudeLongitudePoint.WEST_ABBREV = stringServer.getString("west_abbrev");
        for(int i = 0; i < toolboxButtonLabels.length; i++)
        {
            String s1 = stringServer.getString("toolbox_" + toolboxButtonLabels[i]);
            if(s1 != null)
                toolboxButtonNames[i] = s1;
        }

        instructionTitles[0] = stringServer.getString("instructions_title");
        showStatus(stringServer.getString("donotclose"));
        System.out.println("Running from " + getCodeBase());
        parentFrame = parentOf(this);
        String s = checkVM();
        instructions = loadInstructionText();
        loadImages();
        loadHistoricalEarthquakes();
        if(s == null)
        {
            try
            {
                setName("EpicenterMagnitude");
                setBackground(bColor);
                setFont(new Font("Helvetica", 0, 12));
                setLayout(null);
                setCursor(Cursor.getDefaultCursor());
                InsetContainer insetcontainer = getButtonContainer();
                InsetContainer insetcontainer1 = getToolboxContainer();
                EECButton eecbutton = getToolboxHelpButton();
                Rectangle rectangle = insetcontainer1.getBounds();
                eecbutton.setLocation(rectangle.x, rectangle.y + rectangle.height);
                InsetContainer insetcontainer2 = getInstructionsContainer();
                MapComponent mapcomponent = getMapComponent();
                InsetContainer insetcontainer3 = getTabContainer();
                add(getToolTipCanvas());
                getToolTipCanvas().setVisible(false);
                add(insetcontainer);
                add(insetcontainer2);
                add(mapcomponent);
                add(insetcontainer3);
                add(insetcontainer1);
                add(eecbutton);
                initConnections();
                tabLabels = new String[tabLabelNames.length];
                for(int j = 0; j < tabLabelNames.length; j++)
                    tabLabels[j] = stringServer.getString(tabLabelNames[j]);

                getTabButtonsPanel().setTabLabels(tabLabels);
                System.out.println("os.name = " + System.getProperty("os.name"));
                glassBreaking = getAudioClip(getCodeBase(), "BreakingSound.au");
                validate();
                journal.addNotify();
                getMapComponent().setTextFields(journal.distanceTextFields, journal.spIntervalTextFields, journal.amplitudeTextFields);
            }
            catch(Throwable throwable)
            {
                System.out.println(throwable);
                handleException(throwable);
            }
            earth = new Earth(earthImages);
            earth.setBounds(26, 9, earthImages[0].getWidth(this), earthImages[0].getHeight(this));
            earth.setBackground(new Color(204, 153, 51));
            add(earth);
            startActivity();
            mapComponent.setStations(historicalEarthquakes[0].getStationNames(), historicalEarthquakes[0].getStationLocations());
        } else
        {
            InstructionText instructiontext = new InstructionText(parentFrame);
            instructiontext.setFont(new Font("Helvetica", 0, 16));
            instructiontext.setText(s);
            instructiontext.addActionListener(theEventHandler);
            setLayout(new BorderLayout());
            add(instructiontext, "Center");
            validate();
        }
        getBackgroundText().redraw();
    }

    public void instructionTextAction(ActionEvent actionevent)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(actionevent.getActionCommand(), ";");
        String s = stringtokenizer.nextToken();
        String s1 = stringtokenizer.nextToken();
        System.out.println("type=" + s + ";what=" + s1);
        if(s.equalsIgnoreCase("url"))
        {
            String s2 = stringtokenizer.nextToken();
            openPage(s1, s2);
        } else
        if(s.equalsIgnoreCase("action"))
            doAction(s1);
        else
        if(s.equalsIgnoreCase("glossary"))
            doGlossary(s1);
    }

    public void loadSession()
    {
        boolean flag = false;
        try
        {
            FileDialog filedialog = null;
            String s = null;
            if(!doLoad)
            {
                filedialog = new FileDialog(parentFrame, stringServer.getString("load_session"), 0);
                filedialog.setFile("*.eec");
                filedialog.show();
                s = filedialog.getFile();
            }
            if(doLoad || s != null)
            {
                Object obj;
                if(!doLoad)
                {
                    System.out.println("Going to load: " + s);
                    String s1 = filedialog.getDirectory() + s;
                    if(s1.indexOf(".*.*") != -1)
                        s1 = s1.substring(0, s1.length() - 4);
                    System.out.println("Using: " + s1);
                    File file = new File(s1);
                    if(file.exists())
                        obj = new FileInputStream(file);
                    else
                        obj = null;
                } else
                {
                    obj = getClass().getResourceAsStream("/EpicenterMagnitude.eec");
                }
                if(obj != null)
                {
                    Document document = new Document(((java.io.InputStream) (obj)));
                    Element element = document.getRoot();
                    getDataFromXML(element);
                    flag = true;
                    System.out.println("Loaded");
                }
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
        if(flag)
        {
            mapComponent.setNothingDone(false);
            saveButton.setShowAltLabel(false);
            saveButton.setRolledOver(false);
            saveButton.repaint();
        } else
        {
            doLoad = false;
            startActivity();
        }
    }

    public void messageDialogDismissed(String s)
    {
        if(s.equals(stringServer.getString("print_title_map")) || s.equals(stringServer.getString("print_title_seismogram")) || s.equals(stringServer.getString("print_title_nomogram")))
        {
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception) { }
            mapComponent.printIt();
            nextPrintJob();
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception1) { }
        } else
        if(s.equals(stringServer.getString("print_title_journal")))
        {
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception2) { }
            printJournal();
            nextPrintJob();
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception3) { }
        } else
        if(s.equals(stringServer.getString("print_title_background")))
        {
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception4) { }
            printBackground();
            nextPrintJob();
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception5) { }
        } else
        if(s.equals(stringServer.getString("print_title_assignments")))
        {
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception6) { }
            printAssignments();
            nextPrintJob();
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception7) { }
        } else
        if(s.equals(stringServer.getString("print_title_glossaryterms")))
        {
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception8) { }
            printGlossaryTerms();
            nextPrintJob();
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception9) { }
        } else
        if(s.equals(stringServer.getString("print_title_info")))
        {
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception10) { }
            printInfo();
            nextPrintJob();
            try
            {
                Thread.sleep(400L);
            }
            catch(InterruptedException interruptedexception11) { }
        }
    }

    public void paint(Graphics g)
    {
        if(getMapComponent().isVisible())
            g.drawImage(imageServer.getImage("bordersImage"), 0, 0, this);
        else
            g.drawImage(imageServer.getImage("bordersLessOneImage"), 0, 0, this);
        g.setColor(Color.yellow);
        g.setFont(borderTitleFont);
        g.drawString(stringServer.getString("title"), 26 + earthImages[0].getWidth(this) + 10, 3 + earthImages[0].getHeight(this));
        super.paint(g);
    }

    public void readDataFromStream(DataInputStream datainputstream)
        throws IOException
    {
        String s = datainputstream.readUTF();
        if(s.equals("EEC Earthquake Epicenter and Magnitude Applet v1.0"))
        {
            s = datainputstream.readUTF();
            quakeRunning = datainputstream.readBoolean();
            startedQuake = datainputstream.readBoolean();
            allPointsPlotted = datainputstream.readBoolean();
            helpContext = datainputstream.readUTF();
            setEpicenter = datainputstream.readBoolean();
            int i = datainputstream.readInt();
            int j = Math.min(i, toolboxButtons.length);
            for(int k = 0; k < i; k++)
                if(k < toolboxButtons.length)
                {
                    toolboxButtons[k].setEnabled(datainputstream.readBoolean());
                    toolboxButtons[k].setSelected(datainputstream.readBoolean());
                } else
                {
                    boolean flag = datainputstream.readBoolean();
                    flag = datainputstream.readBoolean();
                }

            toolboxButtons[0].setActionCommand(datainputstream.readUTF());
            mapImageName = datainputstream.readUTF();
            String s1 = historicalEarthquakes[0].getButtonBackgroundName();
            mapComponent.setImage(imageServer.getImage(mapImageName), imageServer.getImage(s1));
            mapComponent.readDataFromStream(datainputstream);
            journal.readDataFromStream(datainputstream);
            setInstructionTextUsing(s);
            mapComponent.repaint();
            instructionsContainerCardLayout.show(instructionsContainer, "instructions");
            MessageDialog messagedialog1 = new MessageDialog(parentFrame, stringServer.getString("session_loaded_title"), stringServer.getString("session_loaded_text"), 300, 200, bColor, Color.black);
            messagedialog1.setBackground(bColor);
            messagedialog1.show();
        } else
        {
            System.err.println("Incorrect session string.");
            System.err.println("      Got: " + s);
            System.err.println("Should be: EEC Earthquake Epicenter and Magnitude Applet v1.0");
            MessageDialog messagedialog = new MessageDialog(parentFrame, stringServer.getString("session_loading_err_title"), stringServer.getString("session_loading_err_text"), 480, 300, bColor, Color.black);
            messagedialog.show();
        }
    }

    public String readTextFile(String s)
    {
        StringBuffer stringbuffer1;
        String s1 = System.getProperty("line.separator");
        java.io.InputStream inputstream = getClass().getResourceAsStream("/" + s);
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "UTF8");
        int i = 0;
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            char ac[] = new char[1024];
            i = inputstreamreader.read(ac, 0, 1024);
            if(i > 0)
                stringbuffer.append(ac, 0, i);
        } while(i > 0);
        stringbuffer1 = new StringBuffer(stringbuffer.length());
        char c = '\0';
        for(int j = 0; j < stringbuffer.length(); j++)
        {
            char c1 = stringbuffer.charAt(j);
            if(!Character.isISOControl(c1))
            {
                stringbuffer1.append(c1);
                c = c1;
                continue;
            }
            if(s1.indexOf(c1) >= 0 && c != ' ')
            {
                stringbuffer1.append(' ');
                c = ' ';
            }
        }

        return stringbuffer1.toString();
        IOException ioexception;
        ioexception;
        System.err.println(ioexception);
        return "";
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().removePropertyChangeListener(propertychangelistener);
    }

    public void run()
    {
        if(doLoad)
        {
            loadSession();
        } else
        {
            System.out.println("Going to update database.");
            String s = "applet_name=epicentermagnitude";
            try
            {
                URL url = new URL(getDocumentBase(), appletCompleted + "?" + s);
                getAppletContext().showDocument(url, "appletdone");
            }
            catch(Exception exception)
            {
                System.err.println(exception);
            }
        }
    }

    public void somethingDone()
    {
        saveButton.setShowAltLabel(false);
        saveButton.setRolledOver(false);
        saveButton.repaint();
    }

    public void startActivity()
    {
        if(doLoad)
        {
            Thread thread = new Thread(this);
            thread.start();
        } else
        {
            setInstructionTextUsing("map");
            getMapComponent().setMode(1);
            mapComponent.setStations(historicalEarthquakes[0].getStationNames(), historicalEarthquakes[0].getStationLocations());
            ToDo todo = getMapComponent().getToDo();
            for(int i = 0; i < 6; i++)
                todo.setButtonsEnabled(i, i == 0);

            flipToToDo();
        }
    }

    public void switchTool(String s)
    {
        for(int i = 0; i < toolboxButtons.length; i++)
            if(toolboxButtonNames[i].equals(s))
                toolboxButtons[i].setSelected(true);
            else
                toolboxButtons[i].setSelected(false);

        doAction(s);
    }

    public void tabButtonsPanel_Action()
    {
        int i = getTabButtonsPanel().getSelectedTab();
        CardLayout cardlayout = (CardLayout)getTabCardContainer().getLayout();
        switch(i)
        {
        case 0: // '\0'
            cardlayout.show(getTabCardContainer(), getBackgroundText().getName());
            break;

        case 1: // '\001'
            cardlayout.show(getTabCardContainer(), getAssignmentsText().getName());
            break;

        case 2: // '\002'
            cardlayout.show(getTabCardContainer(), getJournal().getName());
            break;

        case 3: // '\003'
            cardlayout.show(getTabCardContainer(), getGlossaryText().getName());
            break;

        case 4: // '\004'
            cardlayout.show(getTabCardContainer(), getInfoText().getName());
            break;
        }
    }

    public void toggleTop()
    {
        MapComponent mapcomponent = getMapComponent();
        if(expandCollapseButton.getDiamondUpDown() == 2)
            expandCollapseButton.setDiamondUpDown(1);
        else
            expandCollapseButton.setDiamondUpDown(2);
        mapcomponent.setVisible(!mapcomponent.isVisible());
        if(mapcomponent.isVisible())
            getTabContainer().setBounds(10, 332, 621, 231);
        else
            getTabContainer().setBounds(10, 43, 621, 516);
        repaint();
        validate();
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void writeDataToStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeUTF("EEC Earthquake Epicenter and Magnitude Applet v1.0");
        dataoutputstream.writeUTF(fieldInstructionTextUsing);
        dataoutputstream.writeBoolean(quakeRunning);
        dataoutputstream.writeBoolean(startedQuake);
        dataoutputstream.writeBoolean(allPointsPlotted);
        dataoutputstream.writeUTF(helpContext);
        dataoutputstream.writeBoolean(setEpicenter);
        dataoutputstream.writeInt(toolboxButtons.length);
        for(int i = 0; i < toolboxButtons.length; i++)
        {
            dataoutputstream.writeBoolean(toolboxButtons[i].isEnabled());
            dataoutputstream.writeBoolean(toolboxButtons[i].isSelected());
        }

        dataoutputstream.writeUTF(toolboxButtons[0].getActionCommand());
        dataoutputstream.writeUTF(mapImageName);
        mapComponent.writeDataToStream(dataoutputstream);
        journal.writeDataToStream(dataoutputstream);
    }

    protected void getDataFromXML(Element element)
    {
        Element element1 = element.getElement("sessionversion");
        String s = null;
        if(element1 != null)
            s = element1.getTextString();
        if(s != null && s.equals("EEC Earthquake Epicenter and Magnitude Applet v1.0"))
        {
            Element element2 = element.getElement("quakerunning");
            if(element2 != null)
            {
                s = element2.getTextString();
                quakeRunning = Boolean.valueOf(s).booleanValue();
            }
            element2 = element.getElement("startedquake");
            if(element2 != null)
            {
                s = element2.getTextString();
                startedQuake = Boolean.valueOf(s).booleanValue();
            }
            element2 = element.getElement("allpointsplotted");
            if(element2 != null)
            {
                s = element2.getTextString();
                quakeRunning = Boolean.valueOf(s).booleanValue();
            }
            element2 = element.getElement("helpcontext");
            if(element2 != null)
                helpContext = element2.getTextString();
            element2 = element.getElement("setepicenter");
            if(element2 != null)
            {
                s = element2.getTextString();
                quakeRunning = Boolean.valueOf(s).booleanValue();
            }
            Element element3 = element.getElement("toolbox");
            if(element3 != null)
            {
                s = element3.getAttributeValue("num");
                try
                {
                    int i = Integer.parseInt(s);
                    int j = Math.min(i, toolboxButtons.length);
                    for(int k = 0; k < j; k++)
                        if(k < toolboxButtons.length)
                        {
                            element2 = element3.getElement("button" + k);
                            if(element2 != null)
                            {
                                s = element2.getAttributeValue("enabled");
                                if(s != null)
                                    toolboxButtons[k].setEnabled(Boolean.valueOf(s).booleanValue());
                                s = element2.getAttributeValue("selected");
                                if(s != null)
                                    toolboxButtons[k].setSelected(Boolean.valueOf(s).booleanValue());
                            }
                        }

                }
                catch(NumberFormatException numberformatexception) { }
            }
            element2 = element.getElement("toolboxzerostate");
            if(element2 != null)
            {
                s = element2.getTextString();
                toolboxButtons[0].setActionCommand(s);
            }
            element2 = element.getElement("mapimagename");
            if(element2 != null)
                mapImageName = element2.getTextString();
            String s1 = historicalEarthquakes[0].getButtonBackgroundName();
            mapComponent.setImage(imageServer.getImage(mapImageName), imageServer.getImage(s1));
            element2 = element.getElement("mapcomponent");
            if(element2 != null)
                mapComponent.getDataFromXML(element2);
            element2 = element.getElement("journal");
            if(element2 != null)
                journal.getDataFromXML(element2);
            mapComponent.repaint();
            instructionsContainerCardLayout.show(instructionsContainer, "instructions");
            element2 = element.getElement("instructiontext");
            if(element2 != null)
            {
                s = element2.getTextString();
                setInstructionTextUsing(s);
            }
            String s2 = stringServer.getString("session_loaded_text");
            element2 = element.getElement("load_message");
            if(element2 != null)
            {
                String s3 = element2.getTextString();
                if(s3 != null)
                    s2 = s3;
            }
            MessageDialog messagedialog1 = new MessageDialog(parentFrame, stringServer.getString("session_loaded_title"), s2, 300, 200, bColor, Color.black);
            messagedialog1.setBackground(bColor);
            messagedialog1.show();
        } else
        {
            System.err.println("Incorrect session string.");
            System.err.println("      Got: " + s);
            System.err.println("Should be: EEC Earthquake Epicenter and Magnitude Applet v1.0");
            MessageDialog messagedialog = new MessageDialog(parentFrame, stringServer.getString("session_loading_err_title"), stringServer.getString("session_loading_err_text"), 480, 300, bColor, Color.black);
            messagedialog.show();
        }
    }

    protected void getParameters()
    {
        String s = getParameter("username");
        if(s != null)
            username = s;
        s = getParameter("eechometarget");
        if(s != null)
            eecHomeTarget = s;
        s = getParameter("eechome");
        if(s != null)
            try
            {
                URL url = new URL(s);
                eecHomeURL = url;
            }
            catch(MalformedURLException malformedurlexception)
            {
                try
                {
                    URL url1 = new URL(getDocumentBase(), s);
                    eecHomeURL = url1;
                }
                catch(MalformedURLException malformedurlexception1)
                {
                    System.err.println("Could not set URL from " + s);
                }
            }
        s = getParameter("DOLOAD");
        if(s != null)
        {
            s = s.toLowerCase();
            if(s.startsWith("t") || s.startsWith("y") || s.startsWith("1"))
                doLoad = true;
        }
        s = getParameter("QUITPAGE");
        if(s != null)
            quitPage = s;
        s = getParameter("QUITTARGET");
        if(s != null)
            quitTarget = s;
        s = getParameter("APPLETCOMPLETED");
        if(s != null)
            appletCompleted = s;
        s = getParameter("APPLETCOMPLETEDTARGET");
        if(s != null)
            appletCompletedTarget = s;
        s = getParameter("SETEPICENTER");
        if(s != null)
        {
            s = s.toLowerCase();
            if(s.startsWith("t") || s.startsWith("y") || s.startsWith("1"))
                setEpicenter = true;
        }
        s = getParameter("LANGUAGE");
        if(s != null)
            language = s;
        if(language.equals("Spanish"))
        {
            myLocale = new Locale("es", "es");
            outputNumbersWithCommas = true;
        }
    }

    protected PropertyChangeSupport getPropertyChange()
    {
        if(propertyChange == null)
            propertyChange = new PropertyChangeSupport(this);
        return propertyChange;
    }

    protected EECButton getToolboxHelpButton()
    {
        if(toolboxHelpButton == null)
            try
            {
                toolboxHelpButton = new EECButton();
                toolboxHelpButton.setImages(imageServer.getImage("ToolboxHelpButtonUpImage"), imageServer.getImage("ToolboxHelpButtonDownImage"), imageServer.getImage("ToolboxHelpButtonOverImage"), imageServer.getImage("ToolboxHelpButtonOverDownImage"));
                toolboxHelpButton.setName("ToolboxHelpButton");
                toolboxHelpButton.setForeground(buttonLabelColor);
                toolboxHelpButton.setBackground(bColor);
                toolboxHelpButton.setRolloverColor(new Color(51, 204, 0));
                toolboxHelpButton.setLabel(stringServer.getString("toolbox_help_button_label"));
                toolboxHelpButton.setFont(topButtonFont);
                toolboxHelpButton.setTextYOffset(4);
                toolboxHelpButton.setDepressedOffset(0);
                int i = imageServer.getImage("ToolboxHelpButtonUpImage").getWidth(this);
                int j = imageServer.getImage("ToolboxHelpButtonUpImage").getHeight(this);
                toolboxHelpButton.setSize(i, j);
                toolboxHelpButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent actionevent)
                    {
                        try
                        {
                            URL url = new URL(getDocumentBase(), "open_toolbox_help.html");
                            System.out.println("Going to open " + url);
                            getAppletContext().showDocument(url, "scripting");
                        }
                        catch(MalformedURLException malformedurlexception)
                        {
                            System.err.println("Malformed URL for toolbox help");
                            malformedurlexception.printStackTrace(System.err);
                        }
                    }

                }
);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return toolboxHelpButton;
    }

    protected void doGlossary(String s)
    {
        try
        {
            URL url = getDocumentBase();
            String s1 = getParameter("usebrowserlauncher");
            if(s1 != null && (s1.startsWith("t") || s1.startsWith("T") || s1.startsWith("y") || s1.startsWith("Y") || s1.startsWith("1")))
            {
                try
                {
                    BrowserLauncher.openURL("http://localhost/eec/Earthquake/doglossary.php?term=" + s);
                }
                catch(IOException ioexception)
                {
                    ioexception.printStackTrace();
                }
            } else
            {
                URL url1 = new URL(url, "../doglossary.php?term=" + s);
                System.out.println("Glossary: " + url1);
                getAppletContext().showDocument(url1, "scripting");
            }
        }
        catch(MalformedURLException malformedurlexception)
        {
            System.err.println(malformedurlexception);
        }
    }

    protected void doAction(String s)
    {
        System.out.println("In doAction with action = '" + s + "'");
        if(s.equalsIgnoreCase("startover"))
        {
            if(startOverDialog == null)
            {
                startOverDialog = new ConfirmDialog(parentFrame, stringServer.getString("startover_confirm_title"), stringServer.getString("startover_confirm_text"), 470, 120, bColor, Color.black, stringServer);
                startOverDialog.setBackground(bColor);
                startOverDialog.setListener(this);
            }
            startOverDialog.setVisible(true);
        } else
        if(!s.equalsIgnoreCase("opennolongeravailable"))
            if(s.equalsIgnoreCase("stationsmoved"))
            {
                helpContext = "movingstations";
                toolboxButtons[0].setEnabled(true);
            } else
            if(s.equalsIgnoreCase("stationsnotready"))
            {
                helpContext = "movingstations";
                toolboxButtons[0].setEnabled(false);
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[0]))
            {
                instructionsContainerCardLayout.show(getInstructionsContainer(), "radiobuttons");
                setInstructionTextUsing("map");
                getMapComponent().setMode(1);
                mapComponent.setStations(historicalEarthquakes[0].getStationNames(), historicalEarthquakes[0].getStationLocations());
                doAction("stationsmoved");
            } else
            if(s.equalsIgnoreCase(triggerEarthquake))
            {
                somethingDone();
                toolboxButtons[0].setActionCommand("startover");
                startedQuake = true;
                setInstructionTextUsing("quakegoing");
                helpContext = "earthquakerunning";
                getMapComponent().startQuake();
                glassBreaking.play();
                getJournal().newEarthquake();
                instructionsContainerCardLayout.show(getInstructionsContainer(), "instructions");
            } else
            if(s.equalsIgnoreCase("spmeasurement"))
            {
                setInstructionTextUsing("map");
                mapComponent.setMode(5);
                helpContext = "measuringsp";
            } else
            if(s.startsWith(toolboxButtonNames[2]))
            {
                mapComponent.setMode(6);
                setInstructionTextUsing("sp");
                helpContext = "measuringsp";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[3]))
            {
                mapComponent.setMode(7);
                setInstructionTextUsing("amplitude");
                helpContext = "measuringamplitude";
            } else
            if(s.equalsIgnoreCase("backtomap") || s.startsWith("backtomaperasemeasurements") || s.equalsIgnoreCase(toolboxButtonNames[1]))
            {
                mapComponent.setMode(8);
                setInstructionTextUsing("map");
                helpContext = "mapshowing";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[5]))
            {
                mapComponent.setMode(10);
                setInstructionTextUsing("circle");
                helpContext = "circledragging";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[6]))
            {
                mapComponent.setMode(11);
                setInstructionTextUsing("epicenter");
                helpContext = "dragepicenter";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[7]))
            {
                mapComponent.setMode(12);
                setInstructionTextUsing("latitudelongitude");
                helpContext = "latitudelongitude";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[4]))
            {
                mapComponent.setMode(9);
                setInstructionTextUsing("ttc");
                helpContext = "traveltimecurve";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[8]))
            {
                mapComponent.setMode(13);
                setInstructionTextUsing("nomogram");
                helpContext = "nomogram";
            } else
            if(s.equalsIgnoreCase(toolboxButtonNames[9]))
            {
                instructionsContainerCardLayout.show(getInstructionsContainer(), "instructions");
                mapComponent.setMode(15);
                setInstructionTextUsing("todo");
                helpContext = "todo";
            } else
            if(!s.equalsIgnoreCase("alldone"));
    }

    protected Hashtable loadInstructionText()
    {
        Hashtable hashtable = null;
        try
        {
            String s = readTextFile(language + "InstructionText.xml");
            Document document = new Document(s);
            Element element = document.getRoot();
            hashtable = new Hashtable(element.getElements().size());
            Element element2;
            Element element3;
            for(Elements elements = element.getElements("instruction"); elements.hasMoreElements(); hashtable.put(element2.getTextString(), element3))
            {
                Element element1 = elements.next();
                element2 = element1.getElement("name");
                element3 = element1.getElement("content");
            }

        }
        catch(electric1.xml.ParseException parseexception)
        {
            parseexception.printStackTrace(System.err);
        }
        return hashtable;
    }

    protected void openPage(String s, String s1)
    {
        try
        {
            URL url = new URL(getDocumentBase(), s);
            System.out.println("Going to open: " + url + "; target=" + s1);
            getAppletContext().showDocument(url, s1);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void outputSystemProperties()
    {
        System.out.println("java.vendor  = " + System.getProperty("java.vendor"));
        System.out.println("java.version = " + System.getProperty("java.version"));
        System.out.println("os.name      = " + System.getProperty("os.name"));
    }

    protected void printAssignments()
    {
        assignments.printIt("Assignments", parentFrame);
    }

    protected void printBackground()
    {
        background.printIt("Background", parentFrame);
    }

    protected void printGlossaryTerms()
    {
        glossaryterms.printIt("Glossary Terms", parentFrame);
    }

    protected void printInfo()
    {
        info.printIt("Info", parentFrame);
    }

    protected void printJournal()
    {
        journal.printIt();
    }

    protected void quakeStopped()
    {
        if(startedQuake)
        {
            setInstructionTextUsing("mapfirsttime");
            mapComponent.setMode(5);
            helpContext = "mapfirsttime";
            for(int i = 0; i < toolboxButtons.length; i++)
            {
                toolboxButtons[i].setEnabled(true);
                toolboxButtons[i].setSelected(false);
            }

            getMapComponent().getToDo().setCompleted(0);
            ToDo todo = getMapComponent().getToDo();
            for(int j = 0; j < 6; j++)
                todo.setButtonsEnabled(j, j != 0);

            flipToToDo();
        }
    }

    protected void saveAssignments()
    {
        assignments.saveIt(stringServer.getString("save_assignments_as"), "assignments.txt");
    }

    protected void saveBackground()
    {
        background.saveIt(stringServer.getString("save_background_as"), "background.txt");
    }

    protected void saveGlossaryTerms()
    {
        assignments.saveIt(stringServer.getString("save_glossaryterms_as"), "glossaryterms.txt");
    }

    protected void saveInfo()
    {
        assignments.saveIt(stringServer.getString("save_info_as"), "info.txt");
    }

    protected void saveJournal()
    {
        journal.saveIt();
    }

    protected void saveSession()
    {
        try
        {
            FileDialog filedialog = new FileDialog(parentFrame, stringServer.getString("save_session_as"), 1);
            filedialog.setFile("EpicenterMagnitude.eec");
            filedialog.show();
            if(System.getProperty("java.vendor").startsWith("Microsoft"))
                while(filedialog.isShowing()) 
                    try
                    {
                        Thread.sleep(250L);
                    }
                    catch(InterruptedException interruptedexception) { }
            String s;
            if((s = filedialog.getFile()) != null)
            {
                System.out.println("Going to save: " + s);
                String s1 = filedialog.getDirectory() + s;
                if(s1.indexOf(".*.*") != -1)
                    s1 = s1.substring(0, s1.length() - 4);
                if(!s1.toLowerCase().endsWith(".eec"))
                    s1 = s1 + ".eec";
                System.out.println("Using: " + s1);
                File file = new File(s1);
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                Object obj = null;
                Document document = new Document();
                document.addChild(new XMLDecl("1.0", "UTF-8"));
                Element element = new Element("session");
                document.setRoot(element);
                addDataToXML(element);
                document.write(fileoutputstream);
                System.out.println("Saved");
                MessageDialog messagedialog = new MessageDialog(parentFrame, stringServer.getString("save_session_dialog_title"), stringServer.getString("save_session_dialog_message"), 370, 300, bColor, Color.black);
                messagedialog.setBackground(bColor);
                messagedialog.setListener(this);
                messagedialog.show();
                return;
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
        saveButton.setRolledOver(false);
        saveButton.repaint();
    }

    protected void stripStarsFromTextFields(TextField atextfield[])
    {
        if(atextfield != null)
        {
            for(int i = 0; i < atextfield.length; i++)
            {
                String s = atextfield[i].getText();
                StringBuffer stringbuffer = new StringBuffer(s.length());
                for(int j = 0; j < s.length(); j++)
                {
                    char c = s.charAt(j);
                    if(Character.isDigit(c) || c == '.' || c == ',')
                        stringbuffer.append(c);
                }

                atextfield[i].setText(stringbuffer.toString());
            }

        }
    }

    EECButton getSaveButton()
    {
        if(saveButton == null)
            try
            {
                saveButton = new EECButton();
                saveButton.setImages(imageServer.getImage("topButtonUpImage"), imageServer.getImage("topButtonDownImage"), imageServer.getImage("topButtonOverImage"), imageServer.getImage("topButtonOverDownImage"));
                saveButton.setName("SaveButton");
                saveButton.setForeground(buttonLabelColor);
                saveButton.setBackground(bColor);
                saveButton.setRolloverColor(new Color(51, 204, 0));
                saveButton.setLabel(stringServer.getString("Save"));
                saveButton.setAltLabel(stringServer.getString("Open"));
                saveButton.setShowAltLabel(true);
                saveButton.setFont(topButtonFont);
                saveButton.setTextYOffset(4);
                saveButton.setDepressedOffset(0);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return saveButton;
    }

    private InstructionTextXML getAssignmentsText()
    {
        if(assignments == null)
            try
            {
                assignments = new InstructionTextXML(parentFrame);
                assignments.setPreferredSize(new Dimension(535, 1200));
                assignments.setHeightToFit(true);
                assignments.setName("assignments");
                assignments.setLinkColor(Color.blue);
                assignments.setBackground(Color.white);
                String s = readTextFile(language + "Assignments.xml");
                if(s != null && s.length() > 0)
                    assignments.setText(s);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return assignments;
    }

    private InstructionTextXML getBackgroundText()
    {
        if(background == null)
            try
            {
                background = new InstructionTextXML(parentFrame);
                background.setPreferredSize(new Dimension(540, 1200));
                background.setHeightToFit(true);
                background.setName("Background");
                background.setLinkColor(Color.blue);
                background.setBackground(Color.white);
                String s = readTextFile(language + "Background.xml");
                if(s != null && s.length() > 0)
                    background.setText(s);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return background;
    }

    private InsetContainer getButtonContainer()
    {
        if(buttonContainer == null)
            try
            {
                buttonContainer = new InsetContainer();
                buttonContainer.setName("ButtonContainer");
                buttonContainer.setFont(new Font("Helvetica", 1, 12));
                buttonContainer.setBackground(instructionColor);
                buttonContainer.setLayout(null);
                Image image = imageServer.getImage("topButtonUpImage");
                Image image1 = imageServer.getImage("topButtonDownImage");
                Image image2 = imageServer.getImage("topButtonOverImage");
                Image image3 = imageServer.getImage("topButtonOverDownImage");
                int i = Math.max(Math.max(image.getWidth(this), image1.getWidth(this)), Math.max(image2.getWidth(this), image3.getWidth(this)));
                int j = Math.max(Math.max(image.getHeight(this), image1.getHeight(this)), Math.max(image2.getHeight(this), image3.getHeight(this)));
                EECButton eecbutton = getTutorialButton();
                EECButton eecbutton1 = getSaveButton();
                EECButton eecbutton2 = getPrintButton();
                EECButton eecbutton3 = getStartQuitButton();
                eecbutton.setPreferredHeight(j);
                eecbutton.setPreferredWidth(i);
                eecbutton1.setPreferredHeight(j);
                eecbutton1.setPreferredWidth(i);
                eecbutton2.setPreferredHeight(j);
                eecbutton2.setPreferredWidth(i);
                eecbutton3.setPreferredHeight(j);
                eecbutton3.setPreferredWidth(i);
                int k = i + 2;
                int l = k * 4 - i;
                int i1 = 0;
                buttonContainer.setBounds(778 - k * 4, 12, k * 4, 20);
                eecbutton3.setBounds(l, i1, i, j);
                eecbutton2.setBounds(l - k, i1, i, j);
                eecbutton1.setBounds(l - k * 2, i1, i, j);
                eecbutton.setBounds(l - k * 3, i1, i, j);
                buttonContainer.add(eecbutton, eecbutton.getName());
                buttonContainer.add(eecbutton1, eecbutton1.getName());
                buttonContainer.add(eecbutton2, eecbutton2.getName());
                buttonContainer.add(eecbutton3, eecbutton3.getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return buttonContainer;
    }

    private EECButton getExpandCollapseButton()
    {
        if(expandCollapseButton == null)
            try
            {
                expandCollapseButton = new EECButton();
                expandCollapseButton.setImages(imageServer.getImage("buttonUpImage"), imageServer.getImage("buttonDownImage"), imageServer.getImage("buttonOverImage"), imageServer.getImage("buttonOverDownImage"));
                expandCollapseButton.setDisabledImage(imageServer.getImage("buttonDisabledImage"));
                expandCollapseButton.setRescaleToFit(false);
                expandCollapseButton.setName("ExpandCollapseButton");
                expandCollapseButton.setForeground(buttonLabelColor);
                expandCollapseButton.setBackground(bColor);
                expandCollapseButton.setFont(buttonFont);
                expandCollapseButton.setLabel(stringServer.getString("pane_expand"));
                expandCollapseButton.setRolloverColor(new Color(51, 204, 0));
                expandCollapseButton.setBounds(503, 334, 128, 28);
                expandCollapseButton.setAltLabel(stringServer.getString("pane_collapse"));
                expandCollapseButton.setDiamondUpDown(1);
                expandCollapseButton.setDiamondThickness(3);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return expandCollapseButton;
    }

    private InsetContainer getExpandCollapseButtonContainer()
    {
        if(expandCollapseButtonContainer == null)
            try
            {
                expandCollapseButtonContainer = new InsetContainer();
                expandCollapseButtonContainer.setName("ExpandCollapseButtonContainer");
                expandCollapseButtonContainer.setBackground(bColor);
                expandCollapseButtonContainer.setBottom(0);
                expandCollapseButtonContainer.setLayout(new FlowLayout(1));
                expandCollapseButtonContainer.setPreferredWidth(80);
                expandCollapseButtonContainer.setRight(4);
                expandCollapseButtonContainer.setTop(0);
                expandCollapseButtonContainer.setPreferredHeight(16);
                expandCollapseButtonContainer.setBorderWidth(0);
                expandCollapseButtonContainer.setBorderType(0);
                getExpandCollapseButtonContainer().add(getExpandCollapseButton(), getExpandCollapseButton().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return expandCollapseButtonContainer;
    }

    private InstructionTextXML getGlossaryText()
    {
        if(glossaryterms == null)
            try
            {
                glossaryterms = new InstructionTextXML(parentFrame);
                glossaryterms.setPreferredSize(new Dimension(540, 500));
                glossaryterms.setHeightToFit(true);
                glossaryterms.setName("glossaryterms");
                glossaryterms.setLinkColor(Color.blue);
                glossaryterms.setBackground(Color.white);
                String s = readTextFile(language + "Glossaryterms.xml");
                if(s != null && s.length() > 0)
                    glossaryterms.setText(s);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return glossaryterms;
    }

    private InstructionTextXML getInfoText()
    {
        if(info == null)
            try
            {
                info = new InstructionTextXML(parentFrame);
                info.setPreferredSize(new Dimension(540, 500));
                info.setHeightToFit(true);
                info.setName("info");
                info.setLinkColor(Color.blue);
                info.setBackground(Color.white);
                String s = readTextFile(language + "Info.xml");
                if(s != null && s.length() > 0)
                    info.setText(s);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return info;
    }

    private InstructionTextXML getInstructionText()
    {
        if(instructionText == null)
            try
            {
                instructionText = new InstructionTextXML(parentFrame);
                instructionText.setName("InstructionText");
                instructionText.setDoOffset(false);
                instructionText.setTitleLines(instructionTitles);
                instructionText.setLinkColor(Color.blue);
                instructionText.setBackground(instructionColor);
                instructionText.setFont(new Font("SansSerif", 0, 11));
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return instructionText;
    }

    private InsetContainer getInstructionsContainer()
    {
        if(instructionsContainer == null)
            try
            {
                instructionsContainer = new InsetContainer();
                instructionsContainer.setBackground(instructionColor);
                instructionsContainer.setName("InstructionsContainer");
                instructionsContainer.setBottom(0);
                instructionsContainerCardLayout = new CardLayout();
                instructionsContainer.setLayout(instructionsContainerCardLayout);
                instructionsContainer.setRight(0);
                instructionsContainer.setTop(0);
                instructionsContainer.setLeft(0);
                instructionsContainer.setBounds(645, 260, 133, 296);
                Panel panel = new Panel();
                setUpInitialRadioButtons(panel);
                instructionsContainer.add(panel, "radiobuttons");
                panel = new Panel();
                panel.setLayout(new BorderLayout());
                panel.add(getInstructionText(), "Center");
                instructionsContainer.add(panel, "instructions");
                instructionsContainerCardLayout.show(getInstructionsContainer(), "radiobuttons");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return instructionsContainer;
    }

    private boolean isMagnitudeOK()
    {
        TextField atextfield[] = journal.magnitudeTextFields;
        double ad[] = new double[atextfield.length];
        StringBuffer stringbuffer = new StringBuffer(atextfield.length * 3 + 5);
        NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
        for(int i = 0; i < ad.length; i++)
        {
            try
            {
                Number number = numberformat.parse(atextfield[i].getText());
                if(number != null)
                    ad[i] = number.doubleValue();
                else
                    ad[i] = -5D;
            }
            catch(ParseException parseexception)
            {
                ad[i] = -5D;
            }
            if(ad[i] >= 0.0D && ad[i] <= 8D)
                continue;
            if(stringbuffer.length() > 0)
                stringbuffer.append(", ");
            stringbuffer.append(i + 1);
        }

        if(stringbuffer.length() > 0)
            return false;
        String s = normalizeNumberString(journal.averageMagnitudeLabel.getText());
        double d = 0.0D;
        try
        {
            Number number1 = numberformat.parse(s);
            d = number1.doubleValue();
        }
        catch(ParseException parseexception1)
        {
            d = -999D;
        }
        return Math.abs(d - mapComponent.getMagnitude()) <= 0.10000000000000001D;
    }

    private EECButton getPrintButton()
    {
        if(printButton == null)
            try
            {
                printButton = new EECButton();
                printButton.setImages(imageServer.getImage("topButtonUpImage"), imageServer.getImage("topButtonDownImage"), imageServer.getImage("topButtonOverImage"), imageServer.getImage("topButtonOverDownImage"));
                printButton.setName("PrintButton");
                printButton.setForeground(buttonLabelColor);
                printButton.setBackground(bColor);
                printButton.setRolloverColor(new Color(51, 204, 0));
                printButton.setLabel(stringServer.getString("Print"));
                printButton.setFont(topButtonFont);
                printButton.setTextYOffset(4);
                printButton.setDepressedOffset(0);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return printButton;
    }

    private EECButton getStartQuitButton()
    {
        if(startQuitButton == null)
            try
            {
                startQuitButton = new EECButton();
                startQuitButton.setImages(imageServer.getImage("topButtonUpImage"), imageServer.getImage("topButtonDownImage"), imageServer.getImage("topButtonOverImage"), imageServer.getImage("topButtonOverDownImage"));
                startQuitButton.setName("StartQuitButton");
                startQuitButton.setLabel(stringServer.getString("Quit"));
                startQuitButton.setForeground(buttonLabelColor);
                startQuitButton.setBackground(bColor);
                startQuitButton.setRolloverColor(new Color(51, 204, 0));
                startQuitButton.setFont(topButtonFont);
                startQuitButton.setTextYOffset(4);
                startQuitButton.setDepressedOffset(0);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return startQuitButton;
    }

    private TabButtonsPanel getTabButtonsPanel()
    {
        if(tabButtonsPanel == null)
            try
            {
                tabButtonsPanel = new TabButtonsPanel();
                tabButtonsPanel.setName("TabButtonsPanel");
                tabButtonsPanel.setRight(2);
                tabButtonsPanel.setPreferredHeight(28);
                tabButtonsPanel.setLeft(2);
                tabButtonsPanel.setMaxTabs(5);
                tabButtonsPanel.setForeground(buttonLabelColor);
                tabButtonsPanel.setBackground(bColor);
                tabButtonsPanel.setButtonImages(imageServer.getImage("tabButtonImage"), imageServer.getImage("tabButtonSelectedImage"));
                tabButtonsPanel.setBounds(13, 332, 470, 28);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return tabButtonsPanel;
    }

    private InsetContainer getTabCardContainer()
    {
        if(tabCardContainer == null)
            try
            {
                tabCardContainer = new InsetContainer();
                tabCardContainer.setName("TabCardContainer");
                tabCardContainer.setTop(0);
                tabCardContainer.setBottom(4);
                CardLayout cardlayout = new CardLayout();
                tabCardContainer.setLayout(cardlayout);
                tabCardContainer.setRight(4);
                tabCardContainer.setBorderWidth(0);
                tabCardContainer.setLeft(4);
                tabCardContainer.setBorderType(0);
                tabCardContainer.setBackground(bColor);
                backgroundScrollPane = new ScrollPane(0);
                backgroundScrollPane.setBackground(Color.white);
                backgroundScrollPane.add(getBackgroundText());
                tabCardContainer.add(backgroundScrollPane, getBackgroundText().getName());
                assignmentsScrollPane = new ScrollPane(0);
                assignmentsScrollPane.setBackground(Color.white);
                assignmentsScrollPane.add(getAssignmentsText());
                tabCardContainer.add(assignmentsScrollPane, getAssignmentsText().getName());
                journalScrollPane = new ScrollPane(0);
                journalScrollPane.setBackground(Color.white);
                journalScrollPane.add(getJournal());
                tabCardContainer.add(journalScrollPane, getJournal().getName());
                glossaryScrollPane = new ScrollPane(0);
                glossaryScrollPane.setBackground(Color.white);
                glossaryScrollPane.add(getGlossaryText());
                tabCardContainer.add(glossaryScrollPane, getGlossaryText().getName());
                infoScrollPane = new ScrollPane(0);
                infoScrollPane.setBackground(Color.white);
                infoScrollPane.add(getInfoText());
                tabCardContainer.add(infoScrollPane, getInfoText().getName());
                tabCardContainer.setBounds(5, 15, 621, 100);
                cardlayout.show(tabCardContainer, getJournal().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return tabCardContainer;
    }

    private InsetContainer getTabContainer()
    {
        if(tabContainer == null)
            try
            {
                tabContainer = new InsetContainer();
                tabContainer.setBackground(Color.white);
                tabContainer.setName("TabContainer");
                tabContainer.setPreferredWidth(621);
                tabContainer.setPreferredHeight(231);
                tabContainer.setLayout(new BorderLayout());
                Panel panel = new Panel();
                panel.setLayout(new BorderLayout());
                panel.add(getTabButtonsPanel(), "West");
                panel.add(getExpandCollapseButton(), "Center");
                tabContainer.add(panel, "North");
                tabContainer.add(getTabCardContainer(), "Center");
                tabContainer.setBounds(10, 332, 621, 231);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return tabContainer;
    }

    private InsetContainer getTabTopContainer()
    {
        if(tabTopContainer == null)
            try
            {
                tabTopContainer = new InsetContainer();
                tabTopContainer.setName("TabTopContainer");
                tabTopContainer.setPreferredHeight(16);
                tabTopContainer.setBackground(bColor);
                tabTopContainer.setLayout(new BorderLayout());
                getTabTopContainer().add(getTabButtonsPanel(), "Center");
                getTabTopContainer().add(getExpandCollapseButtonContainer(), "East");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return tabTopContainer;
    }

    private InsetContainer getToolboxContainer()
    {
        if(toolboxContainer == null)
            try
            {
                toolboxContainer = new InsetContainer();
                toolboxContainer.setName("toolboxContainer");
                toolboxContainer.setFont(new Font("Helvetica", 1, 12));
                toolboxContainer.setBackground(bColor);
                int i = toolboxButtonNames.length / 3;
                if(toolboxButtonNames.length % 3 > 0)
                    i++;
                toolboxContainer.setLayout(new GridLayout(i, 3, 0, 0));
                toolboxContainer.setTop(0);
                toolboxContainer.setBottom(0);
                int j = imageServer.getImage("toolboxButtonUpImage").getWidth(this);
                int k = imageServer.getImage("toolboxButtonUpImage").getHeight(this);
                toolboxContainer.setPreferredWidth(j * 3);
                toolboxContainer.setPreferredHeight(k * i + 4);
                toolboxContainer.setBounds(640, 43, j * 3, k * i + 4);
                toolboxButtons = new EECButton[toolboxButtonNames.length];
                ToolboxHandler toolboxhandler = new ToolboxHandler();
                for(int l = 0; l < toolboxButtonNames.length; l++)
                {
                    toolboxButtons[l] = new EECButton();
                    toolboxButtons[l].setName(toolboxButtonNames[l]);
                    toolboxButtons[l].setActionCommand(toolboxButtonNames[l]);
                    toolboxButtons[l].setImages(imageServer.getImage("toolboxButtonUpImage"), imageServer.getImage("toolboxButtonDownImage"), imageServer.getImage("toolboxButtonOverImage"), imageServer.getImage("toolboxButtonOverDownImage"));
                    toolboxButtons[l].setDisabledImage(imageServer.getImage("toolboxButtonDisabledImage"));
                    toolboxButtons[l].setToolTipText(toolboxButtonNames[l]);
                    Image image = imageServer.getImage(toolboxButtonLabels[l] + "enabled");
                    Image image1 = imageServer.getImage(toolboxButtonLabels[l] + "disabled");
                    Image image2 = imageServer.getImage(toolboxButtonLabels[l] + "down");
                    if(image != null)
                        toolboxButtons[l].setOnTopImages(image, image2, image1);
                    else
                        toolboxButtons[l].setLabel(toolboxButtonLabels[l]);
                    if(toolboxButtons.length % 3 != 0 && l == toolboxButtons.length - 1)
                        toolboxContainer.add(new Canvas());
                    toolboxContainer.add(toolboxButtons[l]);
                    if(toolboxButtonLabels[l].equals("Help"))
                    {
                        toolboxButtons[l].addActionListener(new HelpHandler());
                        toolboxButtons[l].setEnabled(true);
                    } else
                    {
                        toolboxButtons[l].addActionListener(toolboxhandler);
                        if(toolboxButtonLabels[l].equals("ToDo") || toolboxButtonLabels[l].equals("EQ"))
                            toolboxButtons[l].setEnabled(true);
                        else
                            toolboxButtons[l].setEnabled(false);
                    }
                }

            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return toolboxContainer;
    }

    private EECButton getTutorialButton()
    {
        if(tutorialButton == null)
            try
            {
                tutorialButton = new EECButton();
                tutorialButton.setImages(imageServer.getImage("topButtonUpImage"), imageServer.getImage("topButtonDownImage"), imageServer.getImage("topButtonOverImage"), imageServer.getImage("topButtonOverDownImage"));
                tutorialButton.setName("TutorialButton");
                tutorialButton.setForeground(buttonLabelColor);
                tutorialButton.setBackground(bColor);
                tutorialButton.setRolloverColor(new Color(51, 204, 0));
                tutorialButton.setLabel(stringServer.getString("Tutorial"));
                tutorialButton.setFont(topButtonFont);
                tutorialButton.setTextYOffset(4);
                tutorialButton.setDepressedOffset(0);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        return tutorialButton;
    }

    private void setUpInitialRadioButtons(Panel panel)
    {
        panel.setLayout(new BorderLayout(0, 10));
        Panel panel1 = new Panel();
        int i = historicalEarthquakes.length + 2;
        boolean flag = false;
        panel1.setLayout(new GridLayout(i, 1, 0, 2));
        historicalEarthquakeGroup = new CheckboxGroup();
        historicalEarthquakeButtons = new Checkbox[historicalEarthquakes.length];
        Label label = new Label(stringServer.getString("choose_from_below"), 1);
        label.setFont(buttonFont);
        panel1.add(label);
        for(int j = 0; j < historicalEarthquakeButtons.length; j++)
        {
            String s;
            boolean flag1;
            EarthquakeData earthquakedata;
            if(j == historicalEarthquakeButtons.length - 1)
            {
                s = stringServer.getString("random_location");
                flag1 = true;
                earthquakedata = null;
            } else
            {
                s = historicalEarthquakes[j + 1].getEarthquakeName();
                flag1 = false;
                earthquakedata = historicalEarthquakes[j + 1];
            }
            historicalEarthquakeButtons[j] = new Checkbox(s, historicalEarthquakeGroup, flag1);
            panel1.add(historicalEarthquakeButtons[j]);
            historicalEarthquakeButtons[j].setFont(radioButtonFont);
            historicalEarthquakeButtons[j].addItemListener(new HistoricalEarthquakeHandler(earthquakedata));
        }

        panel.add(panel1, "North");
        stationDraggingInstructionText = new InstructionTextXML(parentFrame);
        stationDraggingInstructionText.setName("StationDragginInstructionText");
        stationDraggingInstructionText.setDoOffset(false);
        stationDraggingInstructionText.setTitleLines(instructionTitles);
        stationDraggingInstructionText.setLinkColor(Color.blue);
        stationDraggingInstructionText.setBackground(instructionColor);
        if(instructions != null)
            stationDraggingInstructionText.setRoot((Element)instructions.get("stationdragging"));
        panel.add(stationDraggingInstructionText, "Center");
    }

    private String checkVM()
    {
        String s = null;
        if(System.getProperty("java.vendor").toLowerCase().startsWith("microsoft"))
        {
            String s1 = SystemVersionManager.getVMVersion().getProperty("BuildIncrement");
            System.out.println("VM Build Number=" + s1);
            try
            {
                int i = Integer.parseInt(s1);
                if(i < 3300)
                    s = stringServer.getString("needmsvm_beforelink") + " \\" + stringServer.getString("needmsvm_linktext") + "\\url;" + stringServer.getString("needmsvm_link") + ";_blank\\ " + stringServer.getString("needmsvm_afterlink");
            }
            catch(NumberFormatException numberformatexception) { }
        }
        return s;
    }

    private void connPtoP2SetSource()
    {
        try
        {
            if(!ivjConnPtoP2Aligning)
            {
                ivjConnPtoP2Aligning = true;
                getMapComponent().setQuakeRunning(getQuakeRunning());
                ivjConnPtoP2Aligning = false;
            }
        }
        catch(Throwable throwable)
        {
            ivjConnPtoP2Aligning = false;
            handleException(throwable);
        }
    }

    private void connPtoP2SetTarget()
    {
        try
        {
            if(!ivjConnPtoP2Aligning)
            {
                ivjConnPtoP2Aligning = true;
                setQuakeRunning(getMapComponent().getQuakeRunning());
                ivjConnPtoP2Aligning = false;
            }
        }
        catch(Throwable throwable)
        {
            ivjConnPtoP2Aligning = false;
            handleException(throwable);
        }
    }

    private void flipToToDo()
    {
        doAction(toolboxButtonNames[9]);
        for(int i = 0; i < toolboxButtons.length; i++)
            if(toolboxButtonLabels[i].equals("ToDo"))
                toolboxButtons[i].setSelected(true);
            else
                toolboxButtons[i].setSelected(false);

    }

    private void handleException(Throwable throwable)
    {
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        throwable.printStackTrace(System.out);
    }

    private void initConnections()
        throws Exception
    {
        getTabButtonsPanel().addActionListener(theEventHandler);
        getExpandCollapseButton().addActionListener(theEventHandler);
        getPrintButton().addActionListener(theEventHandler);
        getTutorialButton().addActionListener(theEventHandler);
        getSaveButton().addActionListener(theEventHandler);
        getInstructionText().addActionListener(theEventHandler);
        getBackgroundText().addActionListener(theEventHandler);
        getAssignmentsText().addActionListener(theEventHandler);
        getGlossaryText().addActionListener(theEventHandler);
        getInfoText().addActionListener(theEventHandler);
        getStartQuitButton().addActionListener(theEventHandler);
        addPropertyChangeListener(theEventHandler);
        getInstructionText().addPropertyChangeListener(theEventHandler);
        getMapComponent().addPropertyChangeListener(theEventHandler);
        connPtoP2SetTarget();
        getJournal().initConnections(theEventHandler);
    }

    private void loadHistoricalEarthquakes()
    {
        try
        {
            String s = readTextFile(language + "HistoricalEarthquakeData.xml");
            Document document = new Document(s);
            Element element = document.getRoot();
            Elements elements = element.getElements("earthquake");
            historicalEarthquakes = new EarthquakeData[elements.size()];
            elements.reset();
            for(int i = 0; elements.hasMoreElements() && i < 100; i++)
            {
                Element element1 = elements.next();
                EarthquakeData earthquakedata = new EarthquakeData(element1);
                historicalEarthquakes[i] = earthquakedata;
            }

        }
        catch(electric1.xml.ParseException parseexception)
        {
            parseexception.printStackTrace(System.err);
        }
    }

    private void loadImages()
    {
        String s = readTextFile("images.xml");
        if(s != null && s.length() > 0)
            imageServer.loadImages(s);
        earthImages = new Image[36];
        for(int i = 1; i <= 36; i++)
        {
            String s1;
            if(i < 10)
                s1 = "earthImages" + i;
            else
                s1 = "earthImages" + i;
            earthImages[i - 1] = imageServer.getImage(s1);
        }

    }

    private void loadStrings()
    {
        String s = readTextFile(language + "Strings.xml");
        if(s != null && s.length() > 0)
            try
            {
                Document document = new Document(s);
                Element element = document.getRoot();
                stringServer = new StringServer(element);
                triggerEarthquake = stringServer.getString("trigger_earthquake");
            }
            catch(electric1.xml.ParseException parseexception)
            {
                System.err.println(parseexception);
            }
    }

    private void nextPrintJob()
    {
        if((whatToPrint & 1) > 0)
        {
            whatToPrint = whatToPrint & -2;
            String s;
            String s1;
            if(mapComponent.getMode() == 13 || mapComponent.getMode() == 14)
            {
                s1 = stringServer.getString("print_title_nomogram");
                s = stringServer.getString("print_message_nomogram");
            } else
            if(mapComponent.getMode() == 6 || mapComponent.getMode() == 7)
            {
                s1 = stringServer.getString("print_title_seismogram");
                s = stringServer.getString("print_message_seismogram");
            } else
            {
                s1 = stringServer.getString("print_title_map");
                s = stringServer.getString("print_message_map");
            }
            MessageDialog messagedialog5 = new MessageDialog(parentFrame, s1, s, 420, 300, bColor, Color.black);
            messagedialog5.setBackground(bColor);
            messagedialog5.setListener(this);
            messagedialog5.show();
            return;
        }
        if((whatToPrint & 2) > 0)
        {
            whatToPrint = whatToPrint & -3;
            if(!doingMultiple)
            {
                printJournal();
            } else
            {
                MessageDialog messagedialog = new MessageDialog(parentFrame, stringServer.getString("print_title_journal"), stringServer.getString("print_message_journal"), 370, 300, bColor, Color.black);
                messagedialog.setListener(this);
                messagedialog.show();
                return;
            }
        }
        if((whatToPrint & 4) > 0)
        {
            whatToPrint = whatToPrint & -5;
            if(!doingMultiple)
            {
                printBackground();
            } else
            {
                MessageDialog messagedialog1 = new MessageDialog(parentFrame, stringServer.getString("print_title_background"), stringServer.getString("print_message_background"), 370, 300, bColor, Color.black);
                messagedialog1.setListener(this);
                messagedialog1.show();
                return;
            }
        }
        if((whatToPrint & 8) > 0)
        {
            whatToPrint = whatToPrint & -9;
            if(!doingMultiple)
            {
                printAssignments();
            } else
            {
                MessageDialog messagedialog2 = new MessageDialog(parentFrame, stringServer.getString("print_title_assignments"), stringServer.getString("print_message_assignments"), 370, 300, bColor, Color.black);
                messagedialog2.setListener(this);
                messagedialog2.show();
                return;
            }
        }
        if((whatToPrint & 0x10) > 0)
        {
            whatToPrint = whatToPrint & 0xffffffef;
            if(!doingMultiple)
            {
                printGlossaryTerms();
            } else
            {
                MessageDialog messagedialog3 = new MessageDialog(parentFrame, stringServer.getString("print_title_glossaryterms"), stringServer.getString("print_message_glossaryterms"), 370, 300, bColor, Color.black);
                messagedialog3.setListener(this);
                messagedialog3.show();
                return;
            }
        }
        if((whatToPrint & 0x20) > 0)
        {
            whatToPrint = whatToPrint & 0xffffffdf;
            if(!doingMultiple)
            {
                printInfo();
            } else
            {
                MessageDialog messagedialog4 = new MessageDialog(parentFrame, stringServer.getString("print_title_info"), stringServer.getString("print_message_info"), 370, 300, bColor, Color.black);
                messagedialog4.setListener(this);
                messagedialog4.show();
                return;
            }
        }
    }

    private void printButton_Action()
    {
        if(printDialog == null)
        {
            printDialog = new PrintDialog(parentFrame, this, stringServer);
            printDialog.setListener(this);
        }
        printButton.setRolledOver(false);
        printButton.repaint();
        int i = mapComponent.getMode();
        if(i == 6 || i == 7)
            printDialog.setMainText(stringServer.getString("Seismogram"));
        else
        if(i == 13 || i == 14)
            printDialog.setMainText(stringServer.getString("Nomogram"));
        else
        if(i == 9)
            printDialog.setMainText(stringServer.getString("TTC"));
        else
            printDialog.setMainText(stringServer.getString("Map"));
        printDialog.setVisible(true);
    }

    private void saveButton_Action()
    {
        if(saveDialog == null)
        {
            saveDialog = new SaveDialog(parentFrame, this, stringServer);
            saveDialog.setListener(this);
        }
        saveButton.setRolledOver(false);
        saveButton.repaint();
        int i = mapComponent.getMode();
        if(i == 6 || i == 7)
            saveDialog.setMainText(stringServer.getString("Seismogram"));
        else
        if(i == 13 || i == 14)
            saveDialog.setMainText(stringServer.getString("Nomogram"));
        else
        if(i == 9)
            saveDialog.setMainText(stringServer.getString("TTC"));
        else
            saveDialog.setMainText(stringServer.getString("Map"));
        saveDialog.setVisible(true);
    }

    private void tutorialButton_Action()
    {
        try
        {
            URL url = new URL(getDocumentBase(), stringServer.getString("tutorial_url"));
            getAppletContext().showDocument(url, stringServer.getString("tutorial_target"));
        }
        catch(MalformedURLException malformedurlexception)
        {
            System.err.println("Bad URL for tutorial");
        }
    }

    public static final boolean USE_ZIP = false;
    public static final boolean debug = false;
    public static String instructionTitles[] = {
        "Instructions"
    };
    public static Frame parentFrame = null;
    public static final String sessionString = "EEC Earthquake Epicenter and Magnitude Applet v1.0";
    public static final String toolboxButtonLabels[] = {
        "EQ", "Map", "SP", "Amp", "TTC", "Circ", "Epi", "Lat", "Nomo", "ToDo", 
        "Help"
    };
    public static String toolboxButtonNames[] = {
        "Select Earthquake", "Map", "S-P Measurement", "Amplitude Measurement", "Distance", "Triangulation", "Epicenter", "Latitude/Longitude", "Magnitude", "Tasks", 
        "Help"
    };
    public static String triggerEarthquake = "Trigger Earthquake";
    public static final String version = "Date: 2006/02/06";
    public Color buttonLabelColor;
    public EECButton saveButton;
    public Font buttonFont;
    public Hashtable instructions;
    public ImageServer imageServer;
    public MapComponent mapComponent;
    public String helpContext;
    public String language;
    public static Locale myLocale;
    public static boolean outputNumbersWithCommas = false;
    public String mapImageName;
    public StringServer stringServer;
    public ToolTipCanvas toolTipCanvas;
    public String tabLabelNames[] = {
        "tab_background", "tab_assignments", "tab_journal", "tab_glossaryterms", "tab_info"
    };
    public String tabLabels[];
    public EECButton toolboxButtons[];
    public boolean setEpicenter;
    protected InstructionTextXML glossaryterms;
    protected InstructionTextXML info;
    protected transient PropertyChangeSupport propertyChange;
    protected String eecHomeTarget;
    protected String username;
    protected URL eecHomeURL;
    Color bColor;
    Color instructionColor;
    Color myBeigeColor;
    Color theButtonColor;
    ConfirmDialog startOverDialog;
    Earth earth;
    Font borderTitleFont;
    TheEventHandler theEventHandler;
    Image earthImages[];
    boolean doingMultiple;
    int earthStep;
    int numBTRedraws;
    int whatToPrint;
    private AudioClip glassBreaking;
    private CardLayout instructionsContainerCardLayout;
    private CheckboxGroup historicalEarthquakeGroup;
    private EECButton expandCollapseButton;
    private EECButton printButton;
    private EECButton startQuitButton;
    private EECButton toolboxHelpButton;
    private EECButton tutorialButton;
    private Font radioButtonFont;
    private Font toolTipFont;
    private Font topButtonFont;
    private GridLayout buttonContainerGridLayout;
    private InsetContainer buttonContainer;
    private InsetContainer expandCollapseButtonContainer;
    private InsetContainer instructionsContainer;
    private InsetContainer tabCardContainer;
    private InsetContainer tabContainer;
    private InsetContainer tabTopContainer;
    private InsetContainer toolboxContainer;
    private InstructionTextXML assignments;
    private InstructionTextXML background;
    private InstructionTextXML instructionText;
    private InstructionTextXML stationDraggingInstructionText;
    private Journal journal;
    private NumberFormat magnitudeFormat;
    private PrintDialog printDialog;
    private SaveDialog saveDialog;
    private ScrollPane assignmentsScrollPane;
    private ScrollPane backgroundScrollPane;
    private ScrollPane glossaryScrollPane;
    private ScrollPane infoScrollPane;
    private ScrollPane journalScrollPane;
    private String appletCompleted;
    private String appletCompletedTarget;
    private String fieldInstructionTextUsing;
    private String quitPage;
    private String quitTarget;
    private TabButtonsPanel tabButtonsPanel;
    private Checkbox historicalEarthquakeButtons[];
    private EarthquakeData historicalEarthquakes[];
    private boolean allPointsPlotted;
    private boolean doLoad;
    private boolean ivjConnPtoP1Aligning;
    private boolean ivjConnPtoP2Aligning;
    private boolean quakeRunning;
    private boolean startedQuake;
    private int fieldPageOn;

    static 
    {
        myLocale = Locale.US;
    }












}
