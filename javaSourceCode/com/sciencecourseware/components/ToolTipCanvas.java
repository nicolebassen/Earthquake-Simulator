// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ToolTipCanvas.java

package com.sciencecourseware.components;

import java.awt.*;

public class ToolTipCanvas extends Canvas
{

    public ToolTipCanvas()
    {
        fm = null;
        label = null;
        labelWidth = 0;
    }

    public void setFont(Font font)
    {
        super.setFont(font);
        fm = null;
    }

    public void setLabel(String s)
    {
        label = s;
        if(label != null)
        {
            if(fm == null)
                fm = getToolkit().getFontMetrics(getFont());
            int i = fm.stringWidth(label);
            setSize(i + 6, fm.getHeight() + 6);
        }
    }

    public String getLabel()
    {
        return label;
    }

    public void paint(Graphics g)
    {
        g.setColor(getForeground());
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        if(label != null)
            g.drawString(label, 4, 4 + fm.getAscent());
    }

    FontMetrics fm;
    String label;
    int labelWidth;
}
