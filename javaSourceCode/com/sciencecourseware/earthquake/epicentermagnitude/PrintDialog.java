// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PrintDialog.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import com.sciencecourseware.components.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            EpicenterMagnitude, DialogListener

public class PrintDialog extends Dialog
{
    public class InsetPanel extends Panel
    {

        public Insets getInsets()
        {
            return insets;
        }

        Insets insets;

        public InsetPanel(int i, int j, int k, int l)
        {
            insets = new Insets(i, j, k, l);
        }
    }


    public PrintDialog(Frame frame, EpicenterMagnitude epicentermagnitude, StringServer stringserver)
    {
        super(frame, stringserver.getString("Print"), true);
        listener = null;
        applet = null;
        okClicked = false;
        stringServer = null;
        applet = epicentermagnitude;
        stringServer = stringserver;
        setLayout(null);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        char c = '\u012C';
        char c1 = '\u0104';
        int i = (dimension.width - c) / 2;
        int j = (dimension.height - c1) / 2 - 10;
        setBounds(i, j, c, c1);
        setBackground(bColor);
        setForeground(Color.black);
        addButtons(c, c1 - 50);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                okClicked = false;
                setVisible(false);
                listener.dialogClosed(PrintDialog.this);
            }

        }
);
        Label label = new Label(stringserver.getString("print_what_would"), 1);
        label.setFont(boldFont);
        add(label);
        FontMetrics fontmetrics = Toolkit.getDefaultToolkit().getFontMetrics(boldFont);
        int k = fontmetrics.stringWidth(label.getText());
        int l = (c - k) / 2;
        int i1 = fontmetrics.getHeight() + 4;
        label.setBounds(l, 29, k, i1);
        addCheckboxes(c, 34 + i1);
    }

    public void setListener(DialogListener dialoglistener)
    {
        listener = dialoglistener;
    }

    public void setMainText(String s)
    {
        mainAreaCheckbox.setLabel(s);
    }

    public int getWhatToPrint()
    {
        System.out.println("In getWhatToPrint()");
        if(okClicked)
        {
            int i = 0;
            if(mainAreaCheckbox.getState())
                i++;
            if(journalCheckbox.getState())
                i += 2;
            if(backgroundCheckbox.getState())
                i += 4;
            if(assignmentsCheckbox.getState())
                i += 8;
            if(glossaryCheckbox.getState())
                i += 16;
            if(infoCheckbox.getState())
                i += 32;
            return i;
        } else
        {
            return 0;
        }
    }

    private void addButtons(int i, int j)
    {
        Image image = applet.imageServer.getImage("buttonUpImage");
        Image image1 = applet.imageServer.getImage("buttonDownImage");
        Image image2 = applet.imageServer.getImage("buttonOverImage");
        Image image3 = applet.imageServer.getImage("buttonOverDownImage");
        int k = Math.max(Math.max(image.getWidth(this), image1.getWidth(this)), Math.max(image2.getWidth(this), image3.getWidth(this)));
        int l = Math.max(Math.max(image.getHeight(this), image1.getHeight(this)), Math.max(image2.getHeight(this), image3.getHeight(this)));
        okButton = new EECButton();
        okButton.setImages(image, image1, image2, image3);
        okButton.setDisabledImage(applet.imageServer.getImage("buttonDisabledImage"));
        okButton.setName("OKButton");
        okButton.setLabel(stringServer.getString("OK"));
        okButton.setForeground(buttonLabelColor);
        okButton.setBackground(bColor);
        okButton.setRolloverColor(new Color(51, 204, 0));
        okButton.setFont(buttonFont);
        add(okButton);
        cancelButton = new EECButton();
        cancelButton.setImages(image, image1, image2, image3);
        cancelButton.setDisabledImage(applet.imageServer.getImage("buttonDisabledImage"));
        cancelButton.setName("cancelButton");
        cancelButton.setLabel(stringServer.getString("Cancel"));
        cancelButton.setForeground(buttonLabelColor);
        cancelButton.setBackground(bColor);
        cancelButton.setRolloverColor(new Color(51, 204, 0));
        cancelButton.setFont(buttonFont);
        add(cancelButton);
        okButton.setBounds(i / 2 - k - 4, j, k, l);
        cancelButton.setBounds(i / 2 + 4, j, k, l);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                okClicked = true;
                setVisible(false);
                listener.dialogClosed(PrintDialog.this);
            }

        }
);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                okClicked = false;
                setVisible(false);
                listener.dialogClosed(PrintDialog.this);
            }

        }
);
    }

    private void addCheckboxes(int i, int j)
    {
        FontMetrics fontmetrics = Toolkit.getDefaultToolkit().getFontMetrics(normalFont);
        int k = fontmetrics.stringWidth(stringServer.getString("TTC")) + 20;
        int l = (i - k) / 2;
        int i1 = fontmetrics.getHeight() + 8;
        mainAreaCheckbox = new Checkbox(stringServer.getString("Map"), false);
        journalCheckbox = new Checkbox(stringServer.getString("tab_journal"), true);
        backgroundCheckbox = new Checkbox(stringServer.getString("tab_background"), false);
        assignmentsCheckbox = new Checkbox(stringServer.getString("tab_assignments"), false);
        glossaryCheckbox = new Checkbox(stringServer.getString("tab_glossaryterms"), false);
        infoCheckbox = new Checkbox(stringServer.getString("tab_info"), false);
        mainAreaCheckbox.setFont(normalFont);
        journalCheckbox.setFont(normalFont);
        backgroundCheckbox.setFont(normalFont);
        assignmentsCheckbox.setFont(normalFont);
        glossaryCheckbox.setFont(normalFont);
        infoCheckbox.setFont(normalFont);
        add(mainAreaCheckbox);
        add(journalCheckbox);
        add(backgroundCheckbox);
        add(assignmentsCheckbox);
        add(glossaryCheckbox);
        add(infoCheckbox);
        mainAreaCheckbox.setBounds(l, j, k, i1);
        journalCheckbox.setBounds(l, j + i1, k, i1);
        backgroundCheckbox.setBounds(l, j + i1 * 2, k, i1);
        assignmentsCheckbox.setBounds(l, j + i1 * 3, k, i1);
        glossaryCheckbox.setBounds(l, j + i1 * 4, k, i1);
        infoCheckbox.setBounds(l, j + i1 * 5, k, i1);
    }

    public static final int ASSIGNMENTS = 8;
    public static final int BACKGROUND = 4;
    public static final int GLOSSARY = 16;
    public static final int INFO = 32;
    public static final int JOURNAL = 2;
    public static final int MAIN_AREA = 1;
    public static final int NOTHING = 0;
    public static final Color bColor = new Color(255, 204, 102);
    public static final Font boldFont = new Font("sansserif", 1, 13);
    public static final Font buttonFont = new Font("sansserif", 1, 13);
    public static final Color buttonLabelColor;
    public static final Font normalFont = new Font("sansserif", 0, 12);
    private Checkbox assignmentsCheckbox;
    private Checkbox backgroundCheckbox;
    private Checkbox glossaryCheckbox;
    private Checkbox infoCheckbox;
    private Checkbox journalCheckbox;
    private Checkbox mainAreaCheckbox;
    private DialogListener listener;
    private EECButton cancelButton;
    private EECButton okButton;
    private EpicenterMagnitude applet;
    private boolean okClicked;
    private StringServer stringServer;

    static 
    {
        buttonLabelColor = Color.yellow;
    }


}
