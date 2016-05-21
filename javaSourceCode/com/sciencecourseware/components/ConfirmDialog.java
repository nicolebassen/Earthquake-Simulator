// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConfirmDialog.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

// Referenced classes of package com.sciencecourseware.components:
//            StringServer, ConfirmDialogListener

public class ConfirmDialog extends Dialog
    implements ActionListener
{

    public ConfirmDialog(Frame frame, String s, String s1, int i, int j, Color color, Color color1, 
            StringServer stringserver)
    {
        super(frame, s, true);
        listener = null;
        stringServer = stringserver;
        reshapeAndCenter(i, j);
        setBackground(color);
        setForeground(color1);
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        panel.setLayout(new FlowLayout(1));
        yesButton = new Button(stringserver.getString("confirm_yes"));
        noButton = new Button(stringserver.getString("confirm_no"));
        panel.add(yesButton);
        panel.add(noButton);
        yesButton.addActionListener(this);
        noButton.addActionListener(this);
        add(panel, "South");
        panel = new Panel();
        panel.setLayout(new FlowLayout(1));
        label = new Label(s1, 1);
        panel.add(label);
        add(panel, "Center");
    }

    public void setListener(ConfirmDialogListener confirmdialoglistener)
    {
        listener = confirmdialoglistener;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == yesButton)
        {
            if(listener != null)
                listener.confirmDialogClosed(true);
            setVisible(false);
        } else
        if(actionevent.getSource() == noButton)
        {
            if(listener != null)
                listener.confirmDialogClosed(false);
            setVisible(false);
        }
    }

    public void reshapeAndCenter(int i, int j)
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int k = (dimension.width - i) / 2;
        if(k < 0)
            k = 0;
        int l = (dimension.height - j) / 2;
        if(l < 0)
            l = 0;
        setBounds(k, l, i, j);
    }

    Label label;
    ConfirmDialogListener listener;
    Button noButton;
    Button yesButton;
    private StringServer stringServer;
}
