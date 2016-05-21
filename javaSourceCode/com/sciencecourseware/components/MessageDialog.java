// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MessageDialog.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

// Referenced classes of package com.sciencecourseware.components:
//            InsetContainer, VerticalFlowLayout, MessageDialogListener

public class MessageDialog extends Dialog
    implements ActionListener
{
    public class ClosingKeyHandler extends KeyAdapter
    {

        public void keyReleased(KeyEvent keyevent)
        {
            int i = keyevent.getKeyCode();
            if(i == 10 || i == 27)
                actionPerformed(null);
        }

        public ClosingKeyHandler()
        {
        }
    }


    public MessageDialog(Frame frame, String s, String s1, int i, int j, Color color, Color color1)
    {
        super(frame, s, true);
        listener = null;
        messagePanel = null;
        if(color != null)
            setBackground(color);
        if(color1 != null)
            setForeground(color1);
        theTitle = s;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int k = (dimension.width - i) / 2;
        int l = (dimension.height - j) / 2;
        setBounds(k, l, i, j);
        setResizable(false);
        setLayout(new BorderLayout());
        InsetContainer insetcontainer = new InsetContainer();
        insetcontainer.setTop(4);
        insetcontainer.setBottom(4);
        insetcontainer.setLayout(new FlowLayout(1));
        add(insetcontainer, "South");
        okButton = new Button("OK");
        okButton.setFont(buttonFont);
        insetcontainer.add(okButton);
        okButton.addActionListener(this);
        insetcontainer = new InsetContainer();
        insetcontainer.setTop(4);
        insetcontainer.setBottom(4);
        insetcontainer.setLeft(4);
        insetcontainer.setRight(4);
        insetcontainer.setLayout(new GridBagLayout());
        add(insetcontainer, "Center");
        messagePanel = new InsetContainer();
        messagePanel.setLayout(new VerticalFlowLayout());
        insetcontainer.add(messagePanel);
        setMessage(s1);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                setVisible(false);
                if(listener != null)
                    listener.messageDialogDismissed(theTitle);
            }

        }
);
        ClosingKeyHandler closingkeyhandler = new ClosingKeyHandler();
        addKeyListener(closingkeyhandler);
        okButton.addKeyListener(closingkeyhandler);
    }

    public MessageDialog(Frame frame, String s, String s1, Color color, Color color1)
    {
        this(frame, s, s1, 250, 100, color, color1);
    }

    public void setListener(MessageDialogListener messagedialoglistener)
    {
        listener = messagedialoglistener;
    }

    public void setMessage(String s)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, "@");
        messageLabels = new Label[stringtokenizer.countTokens()];
        int i = 0;
        if(messagePanel.getComponentCount() > 0)
            messagePanel.removeAll();
        while(stringtokenizer.hasMoreTokens()) 
        {
            messageLabels[i] = new Label(stringtokenizer.nextToken(), 1);
            messageLabels[i].setFont(labelFont);
            messagePanel.add(messageLabels[i]);
            i++;
        }
    }

    public void setTitle(String s)
    {
        super.setTitle(s);
        theTitle = s;
    }

    public String getTitle()
    {
        return theTitle;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        setVisible(false);
        if(listener != null)
            listener.messageDialogDismissed(theTitle);
    }

    public static final Font buttonFont = new Font("Helvetica", 1, 11);
    public static final Font labelFont = new Font("Helvetica", 0, 11);
    MessageDialogListener listener;
    Label messageLabels[];
    Button okButton;
    String theTitle;
    private InsetContainer messagePanel;

}
