// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ToDo.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import com.sciencecourseware.components.*;
import electric1.xml.Element;
import electric1.xml.Parent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            EpicenterMagnitude

public class ToDo extends InsetContainer
{
    public class ButtonHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            applet.switchTool(actionevent.getActionCommand());
        }

        public ButtonHandler()
        {
        }
    }


    public ToDo(EpicenterMagnitude epicentermagnitude, StringServer stringserver, ImageServer imageserver)
    {
        toolTipCanvas = null;
        buttonsVector = new Vector[6];
        checkmarkCanvases = new CheckmarkCanvas[6];
        todoInstructionTexts = new InstructionText[6];
        isClassicMacOS = false;
        applet = epicentermagnitude;
        stringServer = stringserver;
        imageServer = imageserver;
        checkOS();
        setBackground(Color.white);
        setForeground(Color.black);
        addComponents();
    }

    public void setButtonsEnabled(int i, boolean flag)
    {
        if(i >= 0 && i < buttonsVector.length && buttonsVector[i] != null)
        {
            EECButton eecbutton;
            for(Enumeration enumeration = buttonsVector[i].elements(); enumeration.hasMoreElements(); eecbutton.setEnabled(flag))
                eecbutton = (EECButton)enumeration.nextElement();

        }
    }

    public void setCompleted(int i)
    {
        setCompleted(i, false);
    }

    public void setCompleted(int i, int j)
    {
        setCompleted(i, j, false);
    }

    public void setCompleted(int i, int j, boolean flag)
    {
        if(i >= 0 && i < checkmarkCanvases.length && (checkmarkCanvases[i].getState() != 2 || flag))
            if(j == -1)
            {
                checkmarkCanvases[i].setChecked(true);
                EECButton eecbutton1;
                for(Enumeration enumeration = buttonsVector[i].elements(); enumeration.hasMoreElements(); eecbutton1.setEnabled(false))
                    eecbutton1 = (EECButton)enumeration.nextElement();

            } else
            if(j >= 0 && j < buttonsVector[i].size())
            {
                EECButton eecbutton = (EECButton)buttonsVector[i].elementAt(j);
                eecbutton.setEnabled(false);
            }
    }

    public void setCompleted(int i, boolean flag)
    {
        setCompleted(i, -1, flag);
    }

    public void getDataFromXML(Element element)
    {
        Element element1 = element.getElement("checkmarks");
        if(element1 != null)
        {
            String s = element1.getAttributeValue("num");
            try
            {
                int i = Integer.parseInt(s);
                for(int j = 0; j < i && j < checkmarkCanvases.length; j++)
                {
                    Element element4 = element1.getElement("checkmark" + j);
                    String s1 = element4.getAttributeValue("checked");
                    checkmarkCanvases[j].setChecked(Boolean.valueOf(s1).booleanValue());
                    s1 = element4.getAttributeValue("enabled");
                    checkmarkCanvases[j].setEnabled(Boolean.valueOf(s1).booleanValue());
                    s1 = element4.getAttributeValue("visible");
                    checkmarkCanvases[j].setVisible(Boolean.valueOf(s1).booleanValue());
                }

            }
            catch(NumberFormatException numberformatexception) { }
        }
        Element element2 = element.getElement("instructiontexts");
        if(element2 != null)
        {
            String s2 = element2.getAttributeValue("num");
            try
            {
                int k = Integer.parseInt(s2);
                for(int l = 0; l < k; l++)
                {
                    Element element5 = element1.getElement("checkmark" + l);
                    String s3 = element5.getAttributeValue("visible");
                    todoInstructionTexts[l].setVisible(Boolean.valueOf(s3).booleanValue());
                }

            }
            catch(NumberFormatException numberformatexception1) { }
        }
        Element element3 = element.getElement("buttons");
        if(element3 != null)
        {
            String s4 = element3.getAttributeValue("num");
            try
            {
                int i1 = Integer.parseInt(s4);
                for(int j1 = 0; j1 < i1; j1++)
                {
                    Element element6 = element3.getElement("buttonrow" + j1);
                    if(element6 != null)
                        try
                        {
                            String s5 = element6.getAttributeValue("num");
                            int k1 = Integer.parseInt(s5);
                            for(int l1 = 0; l1 < k1; l1++)
                            {
                                EECButton eecbutton = (EECButton)buttonsVector[j1].elementAt(l1);
                                Element element7 = element6.getElement("button" + l1);
                                String s6 = element7.getAttributeValue("enabled");
                                eecbutton.setEnabled(Boolean.valueOf(s6).booleanValue());
                                s6 = element7.getAttributeValue("visible");
                                eecbutton.setVisible(Boolean.valueOf(s6).booleanValue());
                            }

                        }
                        catch(NumberFormatException numberformatexception3) { }
                }

            }
            catch(NumberFormatException numberformatexception2) { }
        }
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

    public void setXed(int i)
    {
        if(i >= 0 && i < checkmarkCanvases.length)
        {
            checkmarkCanvases[i].setState(2);
            EECButton eecbutton;
            for(Enumeration enumeration = buttonsVector[i].elements(); enumeration.hasMoreElements(); eecbutton.setEnabled(true))
                eecbutton = (EECButton)enumeration.nextElement();

        }
    }

    public void addDataToXML(Element element)
    {
        Element element1 = new Element("checkmarks");
        element1.setAttribute("num", Integer.toString(checkmarkCanvases.length));
        element.addElement(element1);
        for(int i = 0; i < checkmarkCanvases.length; i++)
        {
            Element element3 = new Element("checkmark" + i);
            element3.setAttribute("checked", EpicenterMagnitude.booleanToString(checkmarkCanvases[i].isChecked()));
            element3.setAttribute("enabled", EpicenterMagnitude.booleanToString(checkmarkCanvases[i].isEnabled()));
            element3.setAttribute("visible", EpicenterMagnitude.booleanToString(checkmarkCanvases[i].isVisible()));
            element1.addElement(element3);
        }

        Element element2 = new Element("instructiontexts");
        element.addElement(element2);
        element2.setAttribute("num", Integer.toString(todoInstructionTexts.length));
        for(int j = 0; j < todoInstructionTexts.length; j++)
        {
            Element element5 = new Element("instructiontext" + j);
            String s = EpicenterMagnitude.booleanToString(todoInstructionTexts[j].isVisible());
            element5.setAttribute("visible", s);
            element2.addElement(element5);
        }

        Element element4 = new Element("buttons");
        element.addElement(element4);
        element4.setAttribute("num", Integer.toString(buttonsVector.length));
        for(int k = 0; k < buttonsVector.length; k++)
        {
            Element element6 = new Element("buttonrow" + k);
            element6.setAttribute("num", Integer.toString(buttonsVector[k].size()));
            for(int l = 0; l < buttonsVector[k].size(); l++)
            {
                EECButton eecbutton = (EECButton)buttonsVector[k].elementAt(l);
                Element element7 = new Element("button" + l);
                element7.setAttribute("enabled", EpicenterMagnitude.booleanToString(eecbutton.isEnabled()));
                element7.setAttribute("visible", EpicenterMagnitude.booleanToString(eecbutton.isVisible()));
                element6.addElement(element7);
            }

            element4.addElement(element6);
        }

    }

    public void clearCompleted()
    {
        for(int i = 0; i < checkmarkCanvases.length; i++)
        {
            checkmarkCanvases[i].setChecked(false);
            EECButton eecbutton;
            for(Enumeration enumeration = buttonsVector[i].elements(); enumeration.hasMoreElements(); eecbutton.setEnabled(true))
                eecbutton = (EECButton)enumeration.nextElement();

        }

    }

    public void paint(Graphics g)
    {
        g.setColor(getForeground());
        g.drawLine(28, 18, 28, 248);
        g.drawLine(582, 18, 582, 248);
        g.drawLine(470, 18, 470, 218);
        for(int i = 0; i < 6; i++)
            g.drawLine(28, 18 + 40 * i, 582, 18 + 40 * i);

        g.drawLine(28, 248, 582, 248);
    }

    protected void checkOS()
    {
        if(System.getProperty("java.vendor").toLowerCase().startsWith("apple") && System.getProperty("java.version").startsWith("1.1"))
            isClassicMacOS = true;
    }

    private void addComponents()
    {
        setLayout(null);
        add(getToolTipCanvas());
        getToolTipCanvas().setVisible(false);
        Label label = new Label(stringServer.getString("todo_title"), 0);
        label.setBounds(30, 0, 550, 17);
        label.setFont(titleFont);
        add(label);
        Panel panel = new Panel();
        panel.setBounds(30, 250, 550, 25);
        panel.setFont(textFont);
        panel.setLayout(new FlowLayout(0));
        CheckmarkCanvas checkmarkcanvas = new CheckmarkCanvas();
        checkmarkcanvas.setCheckImage(imageServer.getImage("checkmark"));
        checkmarkcanvas.setXImage(imageServer.getImage("redx"));
        checkmarkcanvas.setState(1);
        panel.add(checkmarkcanvas);
        label = new Label(" = " + stringServer.getString("todo_completed"));
        panel.add(label);
        checkmarkcanvas = new CheckmarkCanvas();
        checkmarkcanvas.setCheckImage(imageServer.getImage("checkmark"));
        checkmarkcanvas.setXImage(imageServer.getImage("redx"));
        checkmarkcanvas.setState(2);
        panel.add(checkmarkcanvas);
        label = new Label(" = " + stringServer.getString("todo_completed_wrong"));
        panel.add(label);
        add(panel);
        ButtonHandler buttonhandler = new ButtonHandler();
        for(int i = 0; i < 6; i++)
        {
            int j = 20 + 40 * i;
            byte byte0;
            if(i < 5)
                byte0 = 40;
            else
                byte0 = 30;
            checkmarkCanvases[i] = new CheckmarkCanvas();
            checkmarkCanvases[i].setCheckImage(imageServer.getImage("checkmark"));
            checkmarkCanvases[i].setXImage(imageServer.getImage("redx"));
            checkmarkCanvases[i].setBounds(30, j, 21, byte0 - 2);
            add(checkmarkCanvases[i]);
            EpicenterMagnitude _tmp = applet;
            todoInstructionTexts[i] = new InstructionText(EpicenterMagnitude.parentFrame);
            int k = 1;
            try
            {
                String s = stringServer.getString("todo_item_" + i + "_lines");
                k = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                k = 1;
            }
            int l = (j + (byte0 - 14 * k) / 2) - 2;
            if(isClassicMacOS)
                l -= 3;
            todoInstructionTexts[i].setBounds(57, l, 408, 15 * k + 2);
            todoInstructionTexts[i].setFont(textFont);
            todoInstructionTexts[i].setText(stringServer.getString("todo_item_" + i));
            add(todoInstructionTexts[i]);
            buttonsVector[i] = new Vector(2);
            char c = '\u01D9';
            Object obj = null;
            switch(i)
            {
            case 0: // '\0'
                EECButton eecbutton = createButton("EQ", EpicenterMagnitude.toolboxButtonNames[0], buttonhandler);
                buttonsVector[i].addElement(eecbutton);
                eecbutton.setLocation(c, j);
                add(eecbutton);
                break;

            case 1: // '\001'
                EECButton eecbutton1 = createButton("SP", EpicenterMagnitude.toolboxButtonNames[2], buttonhandler);
                buttonsVector[i].addElement(eecbutton1);
                eecbutton1.setLocation(c, j);
                add(eecbutton1);
                eecbutton1 = createButton("Amp", EpicenterMagnitude.toolboxButtonNames[3], buttonhandler);
                buttonsVector[i].addElement(eecbutton1);
                eecbutton1.setLocation(c + eecbutton1.getSize().width + 1, j);
                add(eecbutton1);
                break;

            case 2: // '\002'
                EECButton eecbutton2 = createButton("TTC", EpicenterMagnitude.toolboxButtonNames[4], buttonhandler);
                buttonsVector[i].addElement(eecbutton2);
                eecbutton2.setLocation(c, j);
                add(eecbutton2);
                break;

            case 3: // '\003'
                EECButton eecbutton3 = createButton("Circ", EpicenterMagnitude.toolboxButtonNames[5], buttonhandler);
                buttonsVector[i].addElement(eecbutton3);
                eecbutton3.setLocation(c, j);
                add(eecbutton3);
                eecbutton3 = createButton("Epi", EpicenterMagnitude.toolboxButtonNames[6], buttonhandler);
                buttonsVector[i].addElement(eecbutton3);
                eecbutton3.setLocation(c + eecbutton3.getSize().width + 1, j);
                add(eecbutton3);
                eecbutton3 = createButton("Lat", EpicenterMagnitude.toolboxButtonNames[7], buttonhandler);
                buttonsVector[i].addElement(eecbutton3);
                eecbutton3.setLocation(c + eecbutton3.getSize().width * 2 + 1, j);
                add(eecbutton3);
                break;

            case 4: // '\004'
                EECButton eecbutton4 = createButton("Nomo", EpicenterMagnitude.toolboxButtonNames[8], buttonhandler);
                buttonsVector[i].addElement(eecbutton4);
                eecbutton4.setLocation(c, j);
                add(eecbutton4);
                break;
            }
        }

    }

    private EECButton createButton(String s, String s1, ButtonHandler buttonhandler)
    {
        int i = (imageServer.getImage("toolboxButtonUpImage").getWidth(this) * 4) / 5;
        int j = (imageServer.getImage("toolboxButtonUpImage").getHeight(this) * 4) / 5;
        EECButton eecbutton = new EECButton();
        eecbutton.setActionCommand(s1);
        eecbutton.setImages(imageServer.getImage("toolboxButtonUpImage"), imageServer.getImage("toolboxButtonDownImage"), imageServer.getImage("toolboxButtonOverImage"), imageServer.getImage("toolboxButtonOverDownImage"));
        eecbutton.setDisabledImage(imageServer.getImage("toolboxButtonDisabledImage"));
        eecbutton.setToolTipText(s1);
        eecbutton.setSize(i, j);
        eecbutton.setRescaleToFit(true);
        Image image = imageServer.getImage(s + "enabled");
        Image image1 = imageServer.getImage(s + "disabled");
        Image image2 = imageServer.getImage(s + "down");
        if(image != null)
            eecbutton.setOnTopImages(image, image2, image1);
        else
            eecbutton.setLabel(s);
        eecbutton.addActionListener(buttonhandler);
        return eecbutton;
    }

    public static final int BUTTONS_WIDTH = 109;
    public static final int CHECKMARK_WIDTH = 22;
    public static final int COLUMN_SPACING = 5;
    public static final int LAST_ROW_HEIGHT = 30;
    public static final int LEGEND_HEIGHT = 25;
    public static final int NUM_TODO = 6;
    public static final int ROW_HEIGHT = 40;
    public static final int ROW_INSET = 30;
    public static final int ROW_WIDTH = 550;
    public static final int TITLE_HEIGHT = 20;
    public static final Font textFont = new Font("Helvetica", 0, 11);
    public static final Font titleFont = new Font("Helvetica", 1, 13);
    public static final Font toolTipFont = new Font("Helvetica", 0, 10);
    public ToolTipCanvas toolTipCanvas;
    protected Vector buttonsVector[];
    protected CheckmarkCanvas checkmarkCanvases[];
    protected InstructionText todoInstructionTexts[];
    protected boolean isClassicMacOS;
    private EpicenterMagnitude applet;
    private ImageServer imageServer;
    private StringServer stringServer;


}
