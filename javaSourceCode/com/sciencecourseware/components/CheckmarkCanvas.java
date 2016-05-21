// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CheckmarkCanvas.java

package com.sciencecourseware.components;

import java.awt.*;

public class CheckmarkCanvas extends Canvas
{

    public CheckmarkCanvas()
    {
        checkImage = null;
        myPrefSize = null;
        xImage = null;
        state = 0;
        setChecked(false);
    }

    public void setCheckImage(Image image)
    {
        checkImage = image;
        repaint();
    }

    public Image getCheckImage()
    {
        return checkImage;
    }

    public void setChecked(boolean flag)
    {
        if(flag)
            state = 1;
        else
            state = 0;
        repaint();
    }

    public boolean isChecked()
    {
        return state == 1;
    }

    public void setState(int i)
    {
        if(i >= 0 && i <= 2)
            state = i;
        repaint();
    }

    public int getState()
    {
        return state;
    }

    public void setXImage(Image image)
    {
        xImage = image;
        repaint();
    }

    public void paint(Graphics g)
    {
        int i = getSize().height;
        if(state == 1)
            paintCheckmark(g, 2, (i - 10) / 2 - 4);
        else
        if(state == 2)
            paintX(g, 2, (i - 10) / 2 - 4);
    }

    public Dimension getPreferredSize()
    {
        if(myPrefSize == null)
            myPrefSize = new Dimension(20, 20);
        return myPrefSize;
    }

    private void paintCheckmark(Graphics g, int i, int j)
    {
        g.drawImage(checkImage, i, j, this);
    }

    private void paintX(Graphics g, int i, int j)
    {
        g.drawImage(xImage, i, j, this);
    }

    public static final int CHECKED = 1;
    public static final int CLEAR = 0;
    public static final int PREF_HEIGHT = 20;
    public static final int PREF_WIDTH = 20;
    public static final int XED = 2;
    private Image checkImage;
    private Dimension myPrefSize;
    private Image xImage;
    private int state;
}
