// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InsetContainer.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;

public class InsetContainer extends Panel
{
    class IvjEventHandler
        implements MouseMotionListener
    {

        public void mouseDragged(MouseEvent mouseevent)
        {
            if(mouseevent.getSource() == InsetContainer.this)
                connEtoC1(mouseevent);
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
        }

        IvjEventHandler()
        {
        }
    }


    public InsetContainer()
    {
        this(0, 0, 0, 0);
    }

    public InsetContainer(int i, int j, int k, int l)
    {
        ivjEventHandler = new IvjEventHandler();
        myGreenColor = new Color(0, 102, 0);
        beveled = false;
        fieldBorderType = 0;
        fieldBorderWidth = 0;
        fieldBottom = 0;
        fieldLeft = 0;
        fieldPreferredHeight = 0;
        fieldPreferredWidth = 0;
        fieldRight = 0;
        fieldTitle = new String();
        fieldTop = 0;
        roundedBackgroundColor = null;
        initialize();
        setTop(i);
        setLeft(j);
        setBottom(k);
        setRight(l);
    }

    public void setBeveled(boolean flag)
    {
        beveled = flag;
        repaint();
    }

    public boolean getBeveled()
    {
        return beveled;
    }

    public void setBorderType(int i)
    {
        fieldBorderType = i;
    }

    public int getBorderType()
    {
        return fieldBorderType;
    }

    public void setBorderWidth(int i)
    {
        fieldBorderWidth = i;
    }

    public int getBorderWidth()
    {
        return fieldBorderWidth;
    }

    public void setBottom(int i)
    {
        fieldBottom = i;
    }

    public int getBottom()
    {
        return fieldBottom;
    }

    public Insets getInsets()
    {
        return new Insets(getTop(), getLeft(), getBottom(), getRight());
    }

    public void setLeft(int i)
    {
        fieldLeft = i;
    }

    public int getLeft()
    {
        return fieldLeft;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
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

    public void setRight(int i)
    {
        fieldRight = i;
    }

    public int getRight()
    {
        return fieldRight;
    }

    public void setRoundedBackgroundColor(Color color)
    {
        roundedBackgroundColor = color;
        repaint();
    }

    public Color getRoundedBackgroundColor()
    {
        return roundedBackgroundColor;
    }

    public boolean isRoundedCorners()
    {
        return getRoundedBackgroundColor() != null;
    }

    public void setTitle(String s)
    {
        fieldTitle = s;
    }

    public String getTitle()
    {
        return fieldTitle;
    }

    public void setTop(int i)
    {
        fieldTop = i;
    }

    public int getTop()
    {
        return fieldTop;
    }

    public void insetContainer_MouseDragged(MouseEvent mouseevent)
    {
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        int k = getBorderWidth();
        if(isRoundedCorners())
        {
            g.setColor(getRoundedBackgroundColor());
            g.fillRect(0, 0, i, j);
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, i, j, 6, 6);
        } else
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, i, j);
        }
        super.paint(g);
        g.setColor(getForeground());
        if(beveled)
            k = 18;
        if((getBorderType() & 1) > 0)
        {
            for(int l = 0; l < k; l++)
            {
                if(beveled)
                    g.setColor(borderColors[l]);
                g.drawLine(0, l, i, l);
            }

        }
        if((getBorderType() & 2) > 0)
        {
            for(int i1 = 0; i1 < k; i1++)
            {
                if(beveled)
                    g.setColor(borderColors[17 - i1]);
                g.drawLine(0, j - i1 - 1, i, j - i1 - 1);
            }

        }
        if((getBorderType() & 4) > 0)
        {
            for(int j1 = 0; j1 < k; j1++)
            {
                if(beveled)
                    g.setColor(borderColors[j1]);
                g.drawLine(j1, 0, j1, j);
            }

        }
        if((getBorderType() & 8) > 0)
        {
            for(int k1 = 0; k1 < k; k1++)
            {
                if(beveled)
                    g.setColor(borderColors[17 - k1]);
                g.drawLine(i - k1 - 1, 0, i - k1 - 1, j);
            }

        }
    }

    private void connEtoC1(MouseEvent mouseevent)
    {
        try
        {
            insetContainer_MouseDragged(mouseevent);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void handleException(Throwable throwable)
    {
    }

    private void initConnections()
        throws Exception
    {
        addMouseMotionListener(ivjEventHandler);
    }

    private void initialize()
    {
        try
        {
            setName("InsetContainer");
            setLayout(null);
            setSize(160, 120);
            initConnections();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    public static final int BEVELED_BORDER_WIDTH = 18;
    public static final int BORDER_BOTTOM = 2;
    public static final int BORDER_LEFT = 4;
    public static final int BORDER_NONE = 0;
    public static final int BORDER_RIGHT = 8;
    public static final int BORDER_TOP = 1;
    public static final Color borderColors[] = {
        new Color(247, 247, 247), new Color(247, 247, 247), new Color(217, 180, 67), new Color(217, 180, 67), new Color(221, 187, 84), new Color(200, 164, 57), new Color(157, 122, 17), new Color(126, 95, 0), new Color(126, 95, 0), new Color(117, 88, 0), 
        new Color(141, 106, 0), new Color(102, 102, 102), new Color(130, 130, 130), new Color(153, 153, 153), new Color(189, 189, 189), new Color(216, 216, 216), new Color(239, 239, 239), new Color(247, 247, 247)
    };
    IvjEventHandler ivjEventHandler;
    Color myGreenColor;
    private boolean beveled;
    private int fieldBorderType;
    private int fieldBorderWidth;
    private int fieldBottom;
    private int fieldLeft;
    private int fieldPreferredHeight;
    private int fieldPreferredWidth;
    private int fieldRight;
    private String fieldTitle;
    private int fieldTop;
    private Color roundedBackgroundColor;


}
