// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Journal.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import com.sciencecourseware.components.*;
import electric1.xml.Element;
import electric1.xml.Parent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            EpicenterMagnitude, MapComponent, ToDo

public class Journal extends Panel
    implements ActionListener, MessageDialogListener
{
    public class TextFieldHandler
        implements TextListener
    {

        public void textValueChanged(TextEvent textevent)
        {
            applet.getMapComponent().textFieldChanged();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            for(int i = 0; i < spIntervalTextFields.length; i++)
            {
                String s = spIntervalTextFields[i].getText();
                if(s.length() < 2 || !Character.isDigit(s.charAt(0)))
                    flag = false;
                s = distanceTextFields[i].getText();
                if(s.length() < 2 || !Character.isDigit(s.charAt(0)))
                    flag2 = false;
                s = amplitudeTextFields[i].getText();
                if(s.length() < 2 || !Character.isDigit(s.charAt(0)))
                    flag1 = false;
                s = magnitudeTextFields[i].getText();
                if(s.length() < 1 || !Character.isDigit(s.charAt(0)))
                    flag3 = false;
                s = stationNumberTextFields[i].getText();
                if(s.length() < 1 || !Character.isDigit(s.charAt(0)))
                {
                    flag = false;
                    flag1 = false;
                    flag2 = false;
                    flag3 = false;
                }
            }

            if(flag && flag1)
                applet.getMapComponent().getToDo().setCompleted(1);
            else
            if(flag)
                applet.getMapComponent().getToDo().setCompleted(1, 0);
            else
            if(flag1)
                applet.getMapComponent().getToDo().setCompleted(1, 1);
            if(flag2)
                applet.getMapComponent().getToDo().setCompleted(2);
            if(flag3)
                applet.getMapComponent().getToDo().setCompleted(4);
            boolean flag4 = true;
            int j = 0;
            do
            {
                if(j >= epicenterLatitude.length)
                    break;
                String s1 = epicenterLatitude[j].getText();
                if(s1.length() < 1 || !Character.isDigit(s1.charAt(0)))
                {
                    flag4 = false;
                    break;
                }
                s1 = epicenterLongitude[j].getText();
                if(s1.length() < 1 || !Character.isDigit(s1.charAt(0)))
                {
                    flag4 = false;
                    break;
                }
                j++;
            } while(true);
            if(epicenterLatitudeDirection.getSelectedIndex() == 0 || epicenterLongitudeDirection.getSelectedIndex() == 0)
                flag4 = false;
            if(flag4)
                applet.getMapComponent().getToDo().setCompleted(3);
        }

        public TextFieldHandler()
        {
        }
    }

    public class SpacerCanvas extends Canvas
    {

        public Dimension getMinimumSize()
        {
            return getPreferredSize();
        }

        public Dimension getPreferredSize()
        {
            Dimension dimension = super.getPreferredSize();
            dimension.height = height;
            return dimension;
        }

        int height;

        public SpacerCanvas(int i)
        {
            height = 0;
            height = i;
        }
    }

    public class PopupHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            System.out.println("Popup event: " + actionevent.getActionCommand());
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

    public class ChoiceHandler
        implements ItemListener
    {

        public void itemStateChanged(ItemEvent itemevent)
        {
            boolean flag = true;
            int i = 0;
            do
            {
                if(i >= epicenterLatitude.length)
                    break;
                String s = epicenterLatitude[i].getText();
                if(s.length() < 1 || !Character.isDigit(s.charAt(0)))
                {
                    flag = false;
                    break;
                }
                s = epicenterLongitude[i].getText();
                if(s.length() < 1 || !Character.isDigit(s.charAt(0)))
                {
                    flag = false;
                    break;
                }
                i++;
            } while(true);
            if(epicenterLatitudeDirection.getSelectedIndex() == 0 || epicenterLongitudeDirection.getSelectedIndex() == 0)
                flag = false;
            if(flag)
                applet.getMapComponent().getToDo().setCompleted(3);
        }

        public ChoiceHandler()
        {
        }
    }


    public Journal(EpicenterMagnitude epicentermagnitude, StringServer stringserver)
    {
        fillInMessageDialog = null;
        incorrectMessageDialog = null;
        incorrectStationMessageDialog = null;
        bf = new Font("Monospaced", 1, 11);
        f = new Font("Monospaced", 0, 11);
        myInsets = new Insets(5, 5, 5, 5);
        bColor = new Color(255, 204, 102);
        applet = null;
        stringServer = null;
        gotEarthquakeCorrect = true;
        numVerifyAttempts = 0;
        applet = epicentermagnitude;
        stringServer = stringserver;
        magnitudeFormat = NumberFormat.getNumberInstance(Locale.US);
        magnitudeFormat.setMinimumFractionDigits(2);
        magnitudeFormat.setMaximumFractionDigits(2);
        setBackground(Color.white);
        setLayout(new VerticalFlowLayout(0));
        String s = " " + stringserver.getString("Name");
        String s1 = " " + stringserver.getString("Date");
        int i;
        for(i = Math.max(s.length(), s1.length()); s.length() < i; s = " " + s);
        for(; s1.length() < i; s1 = " " + s1);
        Panel panel = new Panel();
        panel.setLayout(new FlowLayout(0));
        Label label = new Label(s + ":", 0);
        label.setFont(bf);
        panel.add(label);
        nameTextField = new TextField(40);
        nameTextField.setFont(f);
        panel.add(nameTextField);
        add(panel);
        panel = new Panel();
        panel.setLayout(new FlowLayout(0));
        label = new Label(s1 + ":", 0);
        label.setFont(bf);
        panel.add(label);
        Date date = new Date();
        String s2 = DateFormat.getDateInstance(1, EpicenterMagnitude.myLocale).format(date);
        dateTextField = new TextField(s2, 40);
        dateTextField.setFont(f);
        panel.add(dateTextField);
        add(panel);
        SpacerCanvas spacercanvas = new SpacerCanvas(20);
        add(spacercanvas);
        label = new Label(stringserver.getString("journal_data_table_title_0"), 0);
        label.setFont(bf);
        add(label);
        label = new Label(stringserver.getString("journal_data_table_title_1"), 0);
        label.setFont(bf);
        add(label);
        Panel panel1 = new Panel();
        panel1.setLayout(new FlowLayout(1));
        panel = new Panel();
        panel.setLayout(new GridLayout(5, 5, 2, 2));
        stationLinkLabel = new LinkLabel(stringserver.getString("journal_title_station"), 1);
        stationLinkLabel.setFont(f);
        stationLinkLabel.setActionCommand("url;../doglossary.php?term=Station;scripting");
        panel.add(stationLinkLabel);
        spLinkLabel = new LinkLabel(stringserver.getString("journal_title_sp"), 1);
        spLinkLabel.setFont(f);
        spLinkLabel.setActionCommand("url;../doglossary.php?term=S-P+Lag+Time;scripting");
        panel.add(spLinkLabel);
        LinkLabel linklabel = new LinkLabel(stringserver.getString("journal_title_distance"), 1);
        linklabel.setFont(f);
        panel.add(linklabel);
        amplitudeLinkLabel = new LinkLabel(stringserver.getString("journal_title_amplitude"), 1);
        amplitudeLinkLabel.setFont(f);
        amplitudeLinkLabel.setActionCommand("url;../doglossary.php?term=Amplitude;scripting");
        panel.add(amplitudeLinkLabel);
        magnitudeLinkLabel = new LinkLabel(stringserver.getString("journal_title_magnitude"), 1);
        magnitudeLinkLabel.setFont(f);
        magnitudeLinkLabel.setActionCommand("url;../doglossary.php?term=Richter+Magnitude;scripting");
        panel.add(magnitudeLinkLabel);
        stationNumberTextFields = new TextField[3];
        distanceTextFields = new TextField[3];
        spIntervalTextFields = new TextField[3];
        amplitudeTextFields = new TextField[3];
        magnitudeTextFields = new TextField[3];
        TextFieldHandler textfieldhandler = new TextFieldHandler();
        for(int j = 0; j < 3; j++)
        {
            stationNumberTextFields[j] = new TextField(5);
            stationNumberTextFields[j].setFont(f);
            panel.add(stationNumberTextFields[j]);
            stationNumberTextFields[j].addTextListener(textfieldhandler);
            spIntervalTextFields[j] = new TextField(5);
            spIntervalTextFields[j].setFont(f);
            panel.add(spIntervalTextFields[j]);
            spIntervalTextFields[j].addTextListener(textfieldhandler);
            distanceTextFields[j] = new TextField(5);
            distanceTextFields[j].setFont(f);
            panel.add(distanceTextFields[j]);
            distanceTextFields[j].addTextListener(textfieldhandler);
            amplitudeTextFields[j] = new TextField(5);
            amplitudeTextFields[j].setFont(f);
            panel.add(amplitudeTextFields[j]);
            amplitudeTextFields[j].addTextListener(textfieldhandler);
            magnitudeTextFields[j] = new TextField(5);
            magnitudeTextFields[j].setFont(f);
            panel.add(magnitudeTextFields[j]);
            magnitudeTextFields[j].addTextListener(textfieldhandler);
        }

        panel.add(new Canvas());
        panel.add(new Canvas());
        panel.add(new Canvas());
        averageLabel = new Label(stringserver.getString("journal_label_average"), 2);
        averageLabel.setFont(bf);
        panel.add(averageLabel);
        averageLabel.setVisible(false);
        averageMagnitudeLabel = new Label("", 0);
        averageMagnitudeLabel.setFont(f);
        panel.add(averageMagnitudeLabel);
        averageMagnitudeLabel.setVisible(false);
        panel1.add(panel);
        add(panel1);
        panel1 = new Panel();
        panel1.setLayout(new FlowLayout(0));
        linklabel = new LinkLabel(" " + stringserver.getString("Epicenter"), 2);
        linklabel.setFont(bf);
        panel1.add(linklabel);
        latitudeLinkLabel = new LinkLabel(stringserver.getString("capital_latitude") + ":", 2);
        latitudeLinkLabel.setFont(bf);
        latitudeLinkLabel.setActionCommand("url;../doglossary.php?term=Latitude+and+Longitude;scripting");
        panel1.add(latitudeLinkLabel);
        epicenterLatitude = new TextField[2];
        for(int k = 0; k < epicenterLatitude.length; k++)
        {
            epicenterLatitude[k] = new TextField(k != 2 ? 3 : 1);
            epicenterLatitude[k].setFont(f);
            panel1.add(epicenterLatitude[k]);
            Label label1 = new Label(dmsLabels[k], 0);
            label1.setFont(bf);
            panel1.add(label1);
            epicenterLatitude[k].addTextListener(textfieldhandler);
        }

        ChoiceHandler choicehandler = new ChoiceHandler();
        epicenterLatitudeDirection = new Choice();
        panel1.add(epicenterLatitudeDirection);
        epicenterLatitudeDirection.addItem("?");
        epicenterLatitudeDirection.addItem(stringserver.getString("north_abbrev"));
        epicenterLatitudeDirection.addItem(stringserver.getString("south_abbrev"));
        add(panel1);
        epicenterLatitudeDirection.addItemListener(choicehandler);
        panel1 = new Panel();
        panel1.setLayout(new FlowLayout(0));
        String s3;
        for(s3 = stringserver.getString("and"); s3.length() < 9; s3 = " " + s3);
        linklabel = new LinkLabel(s3, 2);
        linklabel.setFont(bf);
        panel1.add(linklabel);
        longitudeLinkLabel = new LinkLabel(stringserver.getString("capital_longitude") + ":", 2);
        longitudeLinkLabel.setFont(bf);
        longitudeLinkLabel.setActionCommand("url;../doglossary.php?term=Latitude+and+Longitude;scripting");
        panel1.add(longitudeLinkLabel);
        epicenterLongitude = new TextField[2];
        for(int l = 0; l < epicenterLongitude.length; l++)
        {
            epicenterLongitude[l] = new TextField(l != 2 ? 3 : 1);
            epicenterLongitude[l].setFont(f);
            panel1.add(epicenterLongitude[l]);
            Label label2 = new Label(dmsLabels[l], 0);
            label2.setFont(bf);
            panel1.add(label2);
            epicenterLongitude[l].addTextListener(textfieldhandler);
        }

        epicenterLongitudeDirection = new Choice();
        epicenterLongitudeDirection.addItem("?");
        epicenterLongitudeDirection.addItem(stringserver.getString("east_abbrev"));
        epicenterLongitudeDirection.addItem(stringserver.getString("west_abbrev"));
        panel1.add(epicenterLongitudeDirection);
        epicenterLongitudeDirection.addItemListener(choicehandler);
        Font font = new Font(applet.buttonFont.getName(), 0, applet.buttonFont.getSize());
        verifyAnswersButton = new EECButton();
        verifyAnswersButton.setImages(applet.imageServer.getImage("buttonUpImage"), applet.imageServer.getImage("buttonDownImage"), applet.imageServer.getImage("buttonOverImage"), applet.imageServer.getImage("buttonOverDownImage"));
        verifyAnswersButton.setRescaleToFit(true);
        verifyAnswersButton.setName("VerifyAnswersButton");
        verifyAnswersButton.setForeground(applet.buttonLabelColor);
        verifyAnswersButton.setBackground(getBackground());
        verifyAnswersButton.setFont(font);
        verifyAnswersButton.setLabel(stringserver.getString("verify_answers_button_label"));
        verifyAnswersButton.setRolloverColor(new Color(51, 204, 0));
        verifyAnswersButton.setPreferredWidth((int)Math.round(179.19999999999999D));
        verifyAnswersButton.setPreferredHeight((int)Math.round(39.199999999999996D));
        verifyAnswersButton.addActionListener(this);
        verifyAnswersButton.setEnabled(true);
        panel1.add(verifyAnswersButton);
        add(panel1);
        addPopUpMenu();
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }

            public void mousePressed(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }

            public void mouseDragged(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }

        }
);
    }

    public void getDataFromXML(Element element)
    {
        Element element1 = element.getElement("name");
        if(element1 != null)
        {
            String s = element1.getTextString();
            nameTextField.setText(s);
        }
        element1 = element.getElement("date");
        if(element1 != null)
        {
            String s1 = element1.getTextString();
            dateTextField.setText(s1);
        }
        for(int i = 0; i < stationNumberTextFields.length; i++)
        {
            element1 = element.getElement("stationnumber" + i);
            if(element1 != null)
            {
                String s5 = element1.getTextString();
                stationNumberTextFields[i].setText(s5);
            }
        }

        for(int j = 0; j < distanceTextFields.length; j++)
        {
            element1 = element.getElement("distance" + j);
            if(element1 != null)
            {
                String s6 = element1.getTextString();
                distanceTextFields[j].setText(s6);
            }
        }

        for(int k = 0; k < spIntervalTextFields.length; k++)
        {
            element1 = element.getElement("spinterval" + k);
            if(element1 != null)
            {
                String s7 = element1.getTextString();
                spIntervalTextFields[k].setText(s7);
            }
        }

        for(int l = 0; l < amplitudeTextFields.length; l++)
        {
            element1 = element.getElement("amplitude" + l);
            if(element1 != null)
            {
                String s8 = element1.getTextString();
                amplitudeTextFields[l].setText(s8);
            }
        }

        for(int i1 = 0; i1 < magnitudeTextFields.length; i1++)
        {
            element1 = element.getElement("magnitude" + i1);
            if(element1 != null)
            {
                String s9 = element1.getTextString();
                magnitudeTextFields[i1].setText(s9);
            }
        }

        element1 = element.getElement("averagemagnitude");
        if(element1 != null)
        {
            String s2 = element1.getTextString();
            averageMagnitudeLabel.setText(s2);
        }
        for(int j1 = 0; j1 < 2; j1++)
        {
            element1 = element.getElement("epicenterlatitude" + j1);
            if(element1 != null)
            {
                String s10 = element1.getTextString();
                epicenterLatitude[j1].setText(s10);
            }
            element1 = element.getElement("epicenterlongitude" + j1);
            if(element1 != null)
            {
                String s11 = element1.getTextString();
                epicenterLongitude[j1].setText(s11);
            }
        }

        element1 = element.getElement("epicenterlatitudedir");
        if(element1 != null)
        {
            String s3 = element1.getTextString();
            epicenterLatitudeDirection.select(s3);
        }
        element1 = element.getElement("epicenterlongitudedir");
        if(element1 != null)
        {
            String s4 = element1.getTextString();
            epicenterLongitudeDirection.select(s4);
        }
        element1 = element.getElement("numverifyattempts");
        if(element1 != null)
            try
            {
                int k1 = Integer.parseInt(element1.getTextString());
                numVerifyAttempts = k1;
            }
            catch(NumberFormatException numberformatexception) { }
    }

    public String getDate()
    {
        return dateTextField.getText();
    }

    public void setFullName(String s)
    {
        if(s != null)
            nameTextField.setText(s);
    }

    public Insets getInsets()
    {
        return myInsets;
    }

    public int[] getStationNumbers()
    {
        int ai[] = new int[stationNumberTextFields.length];
        for(int i = 0; i < ai.length; i++)
        {
            String s = stationNumberTextFields[i].getText();
            try
            {
                ai[i] = Integer.parseInt(s) - 1;
                if(ai[i] < 0 || ai[i] > 10)
                    ai[i] = 0x7fffffff;
            }
            catch(NumberFormatException numberformatexception)
            {
                ai[i] = 0x7fffffff;
            }
        }

        return ai;
    }

    public String getUserName()
    {
        return nameTextField.getText();
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        System.out.println("In actionPerformed");
        if(actionevent.getSource() == verifyAnswersButton)
        {
            numVerifyAttempts++;
            try
            {
                epicenterLatitudeDirection.remove("***");
                epicenterLongitudeDirection.remove("***");
            }
            catch(IllegalArgumentException illegalargumentexception) { }
            if(allFieldsFilledIn())
            {
                int ai[] = new int[stationNumberTextFields.length];
                boolean flag = false;
                for(int i = 0; i < ai.length; i++)
                {
                    String s = EpicenterMagnitude.normalizeNumberString(stationNumberTextFields[i].getText());
                    try
                    {
                        ai[i] = Integer.parseInt(s) - 1;
                        if(ai[i] < 0 || ai[i] > 9)
                        {
                            flag = true;
                            ai[i] = 0x7fffffff;
                            stationNumberTextFields[i].setText(s + "***");
                        }
                        continue;
                    }
                    catch(NumberFormatException numberformatexception)
                    {
                        flag = true;
                    }
                    ai[i] = 0x7fffffff;
                    stationNumberTextFields[i].setText(s + "***");
                }

                if(flag)
                {
                    showIncorrectStationMessageDialog();
                } else
                {
                    boolean flag1 = applet.checkDistances(ai, distanceTextFields);
                    boolean flag2 = applet.checkSpIntervals(ai, spIntervalTextFields);
                    boolean flag3 = applet.checkAmplitudes(ai, amplitudeTextFields);
                    boolean flag4 = applet.checkMagnitudes(magnitudeTextFields, averageMagnitudeLabel);
                    averageLabel.setVisible(true);
                    averageMagnitudeLabel.setVisible(true);
                    boolean flag5 = applet.checkEpicenter(epicenterLatitude, epicenterLatitudeDirection, epicenterLongitude, epicenterLongitudeDirection);
                    if(flag1 || flag2 || flag3 || flag4 || flag5)
                    {
                        showIncorrectMessageDialog();
                        ToDo todo = applet.getMapComponent().getToDo();
                        if(flag3 || flag2)
                            todo.setXed(1);
                        else
                            todo.setCompleted(1, true);
                        if(flag1)
                            todo.setXed(2);
                        else
                            todo.setCompleted(2, true);
                        if(flag4)
                            todo.setXed(4);
                        else
                            todo.setCompleted(4, true);
                        if(flag5)
                            todo.setXed(3);
                        else
                            todo.setCompleted(3, true);
                    } else
                    {
                        applet.answersVerified();
                        for(int j = 1; j <= 4; j++)
                            applet.getMapComponent().getToDo().setCompleted(j, true);

                        gotEarthquakeCorrect = true;
                        verifyAnswersButton.setEnabled(true);
                    }
                }
            } else
            {
                showFillInMessageDialog();
            }
        }
    }

    public void addDataToXML(Element element)
    {
        element.addElement(new Element("name")).setText(nameTextField.getText());
        element.addElement(new Element("date")).setText(dateTextField.getText());
        if(stationNumberTextFields != null)
        {
            for(int i = 0; i < stationNumberTextFields.length; i++)
                element.addElement(new Element("stationnumber" + i)).setText(stationNumberTextFields[i].getText());

        }
        if(distanceTextFields != null)
        {
            for(int j = 0; j < distanceTextFields.length; j++)
                element.addElement(new Element("distance" + j)).setText(distanceTextFields[j].getText());

        }
        if(spIntervalTextFields != null)
        {
            for(int k = 0; k < spIntervalTextFields.length; k++)
                element.addElement(new Element("spinterval" + k)).setText(spIntervalTextFields[k].getText());

        }
        if(amplitudeTextFields != null)
        {
            for(int l = 0; l < amplitudeTextFields.length; l++)
                element.addElement(new Element("amplitude" + l)).setText(amplitudeTextFields[l].getText());

        }
        if(magnitudeTextFields != null)
        {
            for(int i1 = 0; i1 < magnitudeTextFields.length; i1++)
                element.addElement(new Element("magnitude" + i1)).setText(magnitudeTextFields[i1].getText());

        }
        element.addElement(new Element("averagemagnitude")).setText(averageMagnitudeLabel.getText());
        for(int j1 = 0; j1 < 2; j1++)
        {
            element.addElement(new Element("epicenterlatitude" + j1)).setText(epicenterLatitude[j1].getText());
            element.addElement(new Element("epicenterlongitude" + j1)).setText(epicenterLongitude[j1].getText());
        }

        Element element1 = new Element("epicenterlatitudedir");
        element1.setText(epicenterLatitudeDirection.getSelectedItem());
        element.addElement(element1);
        element1 = new Element("epicenterlongitudedir");
        element1.setText(epicenterLongitudeDirection.getSelectedItem());
        element.addElement(element1);
        element.addElement(new Element("numverifyattempts")).setText(Integer.toString(numVerifyAttempts));
    }

    public void clearTextFields()
    {
        for(int i = 0; i < distanceTextFields.length; i++)
        {
            stationNumberTextFields[i].setText("");
            distanceTextFields[i].setText("");
            spIntervalTextFields[i].setText("");
            amplitudeTextFields[i].setText("");
            magnitudeTextFields[i].setText("");
        }

        averageMagnitudeLabel.setText("");
        for(int j = 0; j < epicenterLatitude.length; j++)
        {
            epicenterLatitude[j].setText("");
            epicenterLongitude[j].setText("");
        }

        epicenterLatitudeDirection.select(0);
        epicenterLongitudeDirection.select(0);
    }

    public String formPostData()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("numattempts=");
        stringbuffer.append(numVerifyAttempts);
        return stringbuffer.toString();
    }

    public void initConnections(ActionListener actionlistener)
    {
        stationLinkLabel.addActionListener(actionlistener);
        spLinkLabel.addActionListener(actionlistener);
        amplitudeLinkLabel.addActionListener(actionlistener);
        magnitudeLinkLabel.addActionListener(actionlistener);
        latitudeLinkLabel.addActionListener(actionlistener);
        longitudeLinkLabel.addActionListener(actionlistener);
    }

    public void messageDialogDismissed(String s)
    {
    }

    public void newEarthquake()
    {
        if(gotEarthquakeCorrect)
        {
            gotEarthquakeCorrect = false;
            numVerifyAttempts = 0;
            verifyAnswersButton.setEnabled(true);
        }
    }

    public void print(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height);
        paint(g);
    }

    public void printIt()
    {
        String s = "   ";
        byte byte0 = 42;
        EpicenterMagnitude _tmp = applet;
        Printer printer = new Printer(EpicenterMagnitude.parentFrame, "Journal");
        Font font = new Font("Monospaced", 0, 12);
        Font font1 = new Font("Monospaced", 1, 12);
        printer.setLinesPerPage(byte0);
        printer.setFont(font1);
        printer.println(s + stringServer.getString("Activity") + ":  " + stringServer.getString("journal_title"));
        printer.println(s + stringServer.getString("Name") + ": " + nameTextField.getText());
        printer.println(s + stringServer.getString("Date") + ": " + dateTextField.getText());
        printer.println();
        printer.println(s + stringServer.getString("journal_data_table_title"));
        printer.setFont(font);
        printer.println(s + stringServer.getString("journal_title_station") + "    " + stringServer.getString("journal_title_sp") + "   " + stringServer.getString("journal_title_distance") + "   " + stringServer.getString("journal_title_amplitude") + "   " + stringServer.getString("journal_title_magnitude"));
        for(int i = 0; i < 3; i++)
        {
            StringBuffer stringbuffer1 = new StringBuffer(50);
            stringbuffer1.append("   ");
            String s1 = stationNumberTextFields[i].getText();
            if(s1.length() > 0)
                stringbuffer1.append(s1.charAt(0));
            else
                stringbuffer1.append(' ');
            stringbuffer1.append("       ");
            StringBuffer stringbuffer3;
            for(stringbuffer3 = new StringBuffer(spIntervalTextFields[i].getText()); stringbuffer3.length() < 9;)
                if(stringbuffer3.length() % 2 == 0)
                    stringbuffer3.append(" ");
                else
                    stringbuffer3.insert(0, " ");

            if(stringbuffer3.length() > 9)
                stringbuffer3.setLength(9);
            stringbuffer1.append(stringbuffer3.toString());
            stringbuffer1.append("  ");
            StringBuffer stringbuffer4;
            for(stringbuffer4 = new StringBuffer(distanceTextFields[i].getText()); stringbuffer4.length() < 13;)
                if(stringbuffer4.length() % 2 == 0)
                    stringbuffer4.append(" ");
                else
                    stringbuffer4.insert(0, " ");

            if(stringbuffer4.length() > 13)
                stringbuffer4.setLength(13);
            stringbuffer1.append(stringbuffer4.toString());
            stringbuffer1.append("      ");
            for(stringbuffer3 = new StringBuffer(amplitudeTextFields[i].getText()); stringbuffer3.length() < 9;)
                if(stringbuffer3.length() % 2 == 0)
                    stringbuffer3.append(" ");
                else
                    stringbuffer3.insert(0, " ");

            if(stringbuffer3.length() > 9)
                stringbuffer3.setLength(9);
            stringbuffer1.append(stringbuffer3.toString());
            stringbuffer1.append("      ");
            for(stringbuffer3 = new StringBuffer(magnitudeTextFields[i].getText()); stringbuffer3.length() < 9;)
                if(stringbuffer3.length() % 2 == 0)
                    stringbuffer3.append(" ");
                else
                    stringbuffer3.insert(0, " ");

            if(stringbuffer3.length() > 9)
                stringbuffer3.setLength(9);
            stringbuffer1.append(stringbuffer3.toString());
            printer.println(s + stringbuffer1.toString());
        }

        StringBuffer stringbuffer;
        for(stringbuffer = new StringBuffer(averageMagnitudeLabel.getText()); stringbuffer.length() < 6;)
            if(stringbuffer.length() % 2 == 0)
                stringbuffer.append(" ");
            else
                stringbuffer.insert(0, " ");

        if(stringbuffer.length() > 6)
            stringbuffer.setLength(6);
        printer.println(s + "                                                " + stringServer.getString("journal_label_average") + "  " + stringbuffer.toString());
        printer.println();
        StringBuffer stringbuffer2 = new StringBuffer();
        stringbuffer2.append(stringServer.getString("journal_latitude_label") + ": ");
        for(int j = 0; j < epicenterLatitude.length; j++)
        {
            stringbuffer2.append(epicenterLatitude[j].getText());
            stringbuffer2.append(dmsLabels[j]);
            stringbuffer2.append(" ");
        }

        stringbuffer2.append(epicenterLatitudeDirection.getSelectedItem());
        printer.println(s + stringbuffer2.toString());
        stringbuffer2.setLength(0);
        stringbuffer2.append(s + "  " + stringServer.getString("journal_longitude_label") + ": ");
        for(int k = 0; k < epicenterLongitude.length; k++)
        {
            stringbuffer2.append(epicenterLongitude[k].getText());
            stringbuffer2.append(dmsLabels[k]);
            stringbuffer2.append(" ");
        }

        stringbuffer2.append(epicenterLongitudeDirection.getSelectedItem());
        printer.println(s + stringbuffer2.toString());
        printer.newPage();
        printer.endJob();
    }

    public void readDataFromStream(DataInputStream datainputstream)
        throws IOException
    {
        String s = datainputstream.readUTF();
        if(s != null)
            nameTextField.setText(s);
        s = datainputstream.readUTF();
        if(s != null)
            dateTextField.setText(s);
        for(int i = 0; i < stationNumberTextFields.length; i++)
        {
            s = datainputstream.readUTF();
            if(s != null)
                stationNumberTextFields[i].setText(s);
        }

        for(int j = 0; j < distanceTextFields.length; j++)
        {
            s = datainputstream.readUTF();
            if(s != null)
                distanceTextFields[j].setText(s);
        }

        for(int k = 0; k < spIntervalTextFields.length; k++)
        {
            s = datainputstream.readUTF();
            if(s != null)
                spIntervalTextFields[k].setText(s);
        }

        for(int l = 0; l < amplitudeTextFields.length; l++)
        {
            s = datainputstream.readUTF();
            if(s != null)
                amplitudeTextFields[l].setText(s);
        }

        for(int i1 = 0; i1 < magnitudeTextFields.length; i1++)
        {
            s = datainputstream.readUTF();
            if(s != null)
                magnitudeTextFields[i1].setText(s);
        }

        s = datainputstream.readUTF();
        if(s != null)
            averageMagnitudeLabel.setText(s);
        for(int j1 = 0; j1 < 2; j1++)
        {
            s = datainputstream.readUTF();
            if(s != null)
                epicenterLatitude[j1].setText(s);
            s = datainputstream.readUTF();
            if(s != null)
                epicenterLongitude[j1].setText(s);
        }

        s = datainputstream.readUTF();
        if(s != null)
            epicenterLatitudeDirection.select(s);
        s = datainputstream.readUTF();
        if(s != null)
            epicenterLongitudeDirection.select(s);
        numVerifyAttempts = datainputstream.readInt();
    }

    public void saveIt()
    {
        try
        {
            EpicenterMagnitude _tmp = applet;
            FileDialog filedialog = new FileDialog(EpicenterMagnitude.parentFrame, "Save Journal As...", 1);
            filedialog.setFile("journal.txt");
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
                System.out.println("Using: " + s1);
                File file = new File(s1);
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                PrintWriter printwriter = new PrintWriter(fileoutputstream);
                writeToStream(printwriter);
                printwriter.close();
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

    public void writeDataToStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeUTF(nameTextField.getText());
        dataoutputstream.writeUTF(dateTextField.getText());
        if(stationNumberTextFields != null)
        {
            for(int i = 0; i < stationNumberTextFields.length; i++)
                dataoutputstream.writeUTF(stationNumberTextFields[i].getText());

        }
        if(distanceTextFields != null)
        {
            for(int j = 0; j < distanceTextFields.length; j++)
                dataoutputstream.writeUTF(distanceTextFields[j].getText());

        }
        if(spIntervalTextFields != null)
        {
            for(int k = 0; k < spIntervalTextFields.length; k++)
                dataoutputstream.writeUTF(spIntervalTextFields[k].getText());

        }
        if(amplitudeTextFields != null)
        {
            for(int l = 0; l < amplitudeTextFields.length; l++)
                dataoutputstream.writeUTF(amplitudeTextFields[l].getText());

        }
        if(magnitudeTextFields != null)
        {
            for(int i1 = 0; i1 < magnitudeTextFields.length; i1++)
                dataoutputstream.writeUTF(magnitudeTextFields[i1].getText());

        }
        dataoutputstream.writeUTF(averageMagnitudeLabel.getText());
        for(int j1 = 0; j1 < 2; j1++)
        {
            dataoutputstream.writeUTF(epicenterLatitude[j1].getText());
            dataoutputstream.writeUTF(epicenterLongitude[j1].getText());
        }

        dataoutputstream.writeUTF(epicenterLatitudeDirection.getSelectedItem());
        dataoutputstream.writeUTF(epicenterLongitudeDirection.getSelectedItem());
        dataoutputstream.writeInt(numVerifyAttempts);
    }

    protected boolean allFieldsFilledIn()
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= distanceTextFields.length)
                break;
            if(stationNumberTextFields[i].getText().length() < 1 || distanceTextFields[i].getText().length() < 1 || spIntervalTextFields[i].getText().length() < 1 || amplitudeTextFields[i].getText().length() < 1 || magnitudeTextFields[i].getText().length() < 1)
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        if(epicenterLatitudeDirection.getSelectedIndex() == 0 || epicenterLatitudeDirection.getSelectedIndex() == 0)
            flag = false;
        i = 0;
        do
        {
            if(i >= epicenterLatitude.length)
                break;
            if(epicenterLatitude[i].getText().length() < 1 || epicenterLongitude[i].getText().length() < 1)
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    protected void showFillInMessageDialog()
    {
        if(fillInMessageDialog == null)
        {
            EpicenterMagnitude _tmp = applet;
            fillInMessageDialog = new MessageDialog(EpicenterMagnitude.parentFrame, stringServer.getString("journal_fillin_dialog_title"), stringServer.getString("journal_fillin_dialog_text"), 350, 200, bColor, Color.black);
            fillInMessageDialog.setBackground(bColor);
            fillInMessageDialog.setListener(this);
        }
        fillInMessageDialog.setVisible(true);
    }

    protected void showIncorrectMessageDialog()
    {
        if(incorrectMessageDialog == null)
        {
            EpicenterMagnitude _tmp = applet;
            incorrectMessageDialog = new MessageDialog(EpicenterMagnitude.parentFrame, stringServer.getString("journal_incorrect_dialog_title"), stringServer.getString("journal_incorrect_dialog_text"), 350, 300, bColor, Color.black);
            incorrectMessageDialog.setBackground(bColor);
            incorrectMessageDialog.setListener(this);
        }
        incorrectMessageDialog.setVisible(true);
    }

    protected void showIncorrectStationMessageDialog()
    {
        if(incorrectStationMessageDialog == null)
        {
            EpicenterMagnitude _tmp = applet;
            incorrectStationMessageDialog = new MessageDialog(EpicenterMagnitude.parentFrame, stringServer.getString("journal_incorrect_station_dialog_title"), stringServer.getString("journal_incorrect_station_dialog_text"), 350, 300, bColor, Color.black);
            incorrectStationMessageDialog.setBackground(bColor);
            incorrectStationMessageDialog.setListener(this);
        }
        incorrectStationMessageDialog.setVisible(true);
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

    private void printMultipleLines(Printer printer, int i, String s)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, " \n", true);
        int j = 0;
        StringBuffer stringbuffer = new StringBuffer(i + 1);
        while(stringtokenizer.hasMoreTokens()) 
        {
            String s1 = stringtokenizer.nextToken();
            int k = s1.length() - 1;
            boolean flag = s1.charAt(s1.length() - 1) == '\n';
            if(j + k > i)
            {
                stringbuffer.setLength(stringbuffer.length() - 1);
                printer.println("     " + stringbuffer.toString());
                j = 0;
                stringbuffer.setLength(0);
            }
            if(flag)
            {
                stringbuffer.append(s1.substring(0, s1.length() - 1));
                printer.println("     " + stringbuffer.toString());
                j = 0;
                stringbuffer.setLength(0);
            } else
            {
                stringbuffer.append(s1);
                j += s1.length();
            }
        }
        if(stringbuffer.length() > 0)
            printer.println("     " + stringbuffer.toString());
    }

    private void writeToStream(PrintWriter printwriter)
    {
        printwriter.println(" " + stringServer.getString("Activity") + ":  " + stringServer.getString("journal_title"));
        printwriter.println(" " + stringServer.getString("Name") + ":  " + nameTextField.getText());
        printwriter.println(" " + stringServer.getString("Date") + ":  " + dateTextField.getText());
        printwriter.println();
        printwriter.println(stringServer.getString("journal_data_table_title"));
        printwriter.println(stringServer.getString("journal_title_station") + "    " + stringServer.getString("journal_title_sp") + "   " + stringServer.getString("journal_title_distance") + "   " + stringServer.getString("journal_title_amplitude") + "   " + stringServer.getString("journal_title_magnitude"));
        for(int i = 0; i < 3; i++)
        {
            StringBuffer stringbuffer1 = new StringBuffer(50);
            stringbuffer1.append("   ");
            String s = stationNumberTextFields[i].getText();
            if(s.length() > 0)
                stringbuffer1.append(s.charAt(0));
            else
                stringbuffer1.append(' ');
            stringbuffer1.append("       ");
            StringBuffer stringbuffer3;
            for(stringbuffer3 = new StringBuffer(spIntervalTextFields[i].getText()); stringbuffer3.length() < 9;)
                if(stringbuffer3.length() % 2 == 0)
                    stringbuffer3.append(" ");
                else
                    stringbuffer3.insert(0, " ");

            if(stringbuffer3.length() > 9)
                stringbuffer3.setLength(9);
            stringbuffer1.append(stringbuffer3.toString());
            stringbuffer1.append("  ");
            StringBuffer stringbuffer4;
            for(stringbuffer4 = new StringBuffer(distanceTextFields[i].getText()); stringbuffer4.length() < 13;)
                if(stringbuffer4.length() % 2 == 0)
                    stringbuffer4.append(" ");
                else
                    stringbuffer4.insert(0, " ");

            if(stringbuffer4.length() > 13)
                stringbuffer4.setLength(13);
            stringbuffer1.append(stringbuffer4.toString());
            stringbuffer1.append("      ");
            for(stringbuffer3 = new StringBuffer(amplitudeTextFields[i].getText()); stringbuffer3.length() < 9;)
                if(stringbuffer3.length() % 2 == 0)
                    stringbuffer3.append(" ");
                else
                    stringbuffer3.insert(0, " ");

            if(stringbuffer3.length() > 9)
                stringbuffer3.setLength(9);
            stringbuffer1.append(stringbuffer3.toString());
            stringbuffer1.append("      ");
            for(stringbuffer3 = new StringBuffer(magnitudeTextFields[i].getText()); stringbuffer3.length() < 9;)
                if(stringbuffer3.length() % 2 == 0)
                    stringbuffer3.append(" ");
                else
                    stringbuffer3.insert(0, " ");

            if(stringbuffer3.length() > 9)
                stringbuffer3.setLength(9);
            stringbuffer1.append(stringbuffer3.toString());
            printwriter.println(stringbuffer1.toString());
        }

        StringBuffer stringbuffer;
        for(stringbuffer = new StringBuffer(averageMagnitudeLabel.getText()); stringbuffer.length() < 6;)
            if(stringbuffer.length() % 2 == 0)
                stringbuffer.append(" ");
            else
                stringbuffer.insert(0, " ");

        if(stringbuffer.length() > 6)
            stringbuffer.setLength(6);
        printwriter.println("                                                " + stringServer.getString("journal_label_average") + "  " + stringbuffer.toString());
        printwriter.println();
        StringBuffer stringbuffer2 = new StringBuffer();
        stringbuffer2.append(stringServer.getString("journal_latitude_label") + ": ");
        for(int j = 0; j < epicenterLatitude.length; j++)
        {
            stringbuffer2.append(epicenterLatitude[j].getText());
            stringbuffer2.append(dmsLabels[j]);
            stringbuffer2.append(" ");
        }

        stringbuffer2.append(epicenterLatitudeDirection.getSelectedItem());
        printwriter.println(stringbuffer2.toString());
        stringbuffer2.setLength(0);
        stringbuffer2.append("     " + stringServer.getString("journal_longitude_label") + ": ");
        for(int k = 0; k < epicenterLongitude.length; k++)
        {
            stringbuffer2.append(epicenterLongitude[k].getText());
            stringbuffer2.append(dmsLabels[k]);
            stringbuffer2.append(" ");
        }

        epicenterLongitudeDirection.getSelectedItem();
        printwriter.println(stringbuffer2.toString());
    }

    public static int REQUIRED_EPICENTER_CLOSENESS = 25;
    public static final String dmsLabels[] = {
        "\260", "'", " "
    };
    public Choice epicenterLatitudeDirection;
    public Choice epicenterLongitudeDirection;
    public EECButton verifyAnswersButton;
    public Label averageMagnitudeLabel;
    public TextField dateTextField;
    public TextField nameTextField;
    public TextField amplitudeTextFields[];
    public TextField distanceTextFields[];
    public TextField epicenterLatitude[];
    public TextField epicenterLongitude[];
    public TextField magnitudeTextFields[];
    public String menuLabels[] = {
        "Print", "Save"
    };
    public TextField spIntervalTextFields[];
    public TextField stationNumberTextFields[];
    protected MessageDialog fillInMessageDialog;
    protected MessageDialog incorrectMessageDialog;
    protected MessageDialog incorrectStationMessageDialog;
    Font bf;
    Font f;
    Insets myInsets;
    private Color bColor;
    private EpicenterMagnitude applet;
    private Label averageLabel;
    private LinkLabel amplitudeLinkLabel;
    private LinkLabel latitudeLinkLabel;
    private LinkLabel longitudeLinkLabel;
    private LinkLabel magnitudeLinkLabel;
    private LinkLabel spLinkLabel;
    private LinkLabel stationLinkLabel;
    private NumberFormat magnitudeFormat;
    private PopupMenu popup;
    private StringServer stringServer;
    private boolean gotEarthquakeCorrect;
    private int numVerifyAttempts;



}
