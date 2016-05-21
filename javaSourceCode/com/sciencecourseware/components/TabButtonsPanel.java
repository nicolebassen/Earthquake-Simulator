// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TabButtonsPanel.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.*;

public class TabButtonsPanel extends Canvas
{

    public TabButtonsPanel()
    {
        aActionListener = null;
        bufferImage = null;
        buttonImages = null;
        fieldTabLabels = null;
        fieldBottom = 0;
        fieldLeft = 0;
        fieldMaxTabs = 0;
        fieldPreferredHeight = 0;
        fieldRight = 0;
        fieldSelectedTab = -1;
        fieldTop = 0;
        tabHeight = 1;
        tabWidth = 1;
        initialize();
        initConnections();
    }

    public void setBottom(int i)
    {
        fieldBottom = i;
    }

    public int getBottom()
    {
        return fieldBottom;
    }

    public void setButtonImages(Image image, Image image1)
    {
        buttonImages = new Image[2];
        if(image == null)
        {
            buttonImages = null;
        } else
        {
            buttonImages[0] = image;
            if(image1 == null)
                buttonImages[1] = image;
            else
                buttonImages[1] = image1;
        }
        if(buttonImages != null)
        {
            tabWidth = Math.max(image.getWidth(this), image1.getWidth(this));
            tabHeight = Math.max(image.getHeight(this), image1.getHeight(this));
        } else
        {
            tabWidth = 40;
            tabHeight = 20;
        }
    }

    public void setLeft(int i)
    {
        fieldLeft = i;
    }

    public int getLeft()
    {
        return fieldLeft;
    }

    public void setMaxTabs(int i)
    {
        fieldMaxTabs = i;
        repaint();
    }

    public int getMaxTabs()
    {
        return fieldMaxTabs;
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
        if(getPreferredHeight() > dimension.height)
            dimension.height = getPreferredHeight();
        return dimension;
    }

    public void setRight(int i)
    {
        fieldRight = i;
    }

    public int getRight()
    {
        return fieldRight;
    }

    public void setSelectedTab(int i)
    {
        fieldSelectedTab = i;
        repaint();
    }

    public int getSelectedTab()
    {
        return fieldSelectedTab;
    }

    public void setTabLabels(String as[])
    {
        fieldTabLabels = as;
        setSelectedTab(2);
        repaint();
    }

    public void setTabLabels(int i, String s)
    {
        if(i >= 0 && i < fieldTabLabels.length)
        {
            fieldTabLabels[i] = s;
            repaint();
        }
    }

    public String[] getTabLabels()
    {
        return fieldTabLabels;
    }

    public String getTabLabels(int i)
    {
        return getTabLabels()[i];
    }

    public void setTop(int i)
    {
        fieldTop = i;
    }

    public int getTop()
    {
        return fieldTop;
    }

    public void addActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.add(aActionListener, actionlistener);
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(bufferImage == null || bufferImage.getWidth(this) != i || bufferImage.getHeight(this) != j)
            bufferImage = createImage(i, j);
        if(bufferImage != null)
        {
            Graphics g1 = bufferImage.getGraphics();
            if(g1 != null)
            {
                g1.setColor(getBackground());
                g1.fillRect(0, 0, i, j);
                int k = (tabWidth * 5) / 6;
                if(fieldTabLabels != null)
                {
                    if(tabPolygons == null || fieldTabLabels.length != tabPolygons.length)
                        tabPolygons = new Polygon[fieldTabLabels.length];
                    for(int l = 0; l < fieldTabLabels.length; l++)
                    {
                        int j1 = l * k + fieldLeft;
                        int l1 = fieldTop;
                        paintTab(g1, j1, l1, tabWidth, tabHeight, fieldTabLabels[l], false);
                        tabPolygons[l] = new Polygon();
                        tabPolygons[l].addPoint(j1, l1);
                        tabPolygons[l].addPoint(j1, l1 + tabHeight);
                        tabPolygons[l].addPoint(j1 + tabWidth, l1 + tabHeight);
                        tabPolygons[l].addPoint(j1 + tabWidth, l1);
                    }

                    if(fieldSelectedTab >= 0 && fieldSelectedTab < fieldTabLabels.length)
                    {
                        int i1 = fieldSelectedTab * k + fieldLeft;
                        int k1 = fieldTop;
                        paintTab(g1, i1, k1, tabWidth, tabHeight, fieldTabLabels[fieldSelectedTab], true);
                        tabPolygons[fieldSelectedTab] = new Polygon();
                        tabPolygons[fieldSelectedTab].addPoint(i1, k1);
                        tabPolygons[fieldSelectedTab].addPoint(i1, k1 + tabHeight);
                        tabPolygons[fieldSelectedTab].addPoint(i1 + tabWidth, k1 + tabHeight);
                        tabPolygons[fieldSelectedTab].addPoint(i1 + tabWidth, k1);
                    }
                }
                g1.dispose();
            }
            g.drawImage(bufferImage, 0, 0, this);
        }
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.remove(aActionListener, actionlistener);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    protected void fireActionPerformed(ActionEvent actionevent)
    {
        if(aActionListener != null)
            aActionListener.actionPerformed(actionevent);
    }

    protected void paintBackground(Graphics g, int i, int j, int k, int l, boolean flag)
    {
        if(buttonImages == null)
        {
            byte byte0 = 3;
            g.setColor(getForeground());
            byte byte1;
            if(flag)
            {
                byte1 = 2;
            } else
            {
                byte1 = 1;
                g.drawLine(i, (j + l) - 1, (i + k) - 1, (j + l) - 1);
            }
            for(int i1 = 0; i1 < byte1; i1++)
            {
                g.drawLine(i + i1, j + byte0 + 1, i + i1, l - 1);
                g.drawLine(i + byte0 + 1, j + i1, (i + k) - byte0 - 2, i1);
                g.drawLine((i + k) - 1 - i1, j + byte0 + 1, (i + k) - 1 - i1, l - 1);
                g.drawArc(i + i1, j + i1, byte0 * 2, byte0 * 2, 180, -90);
                g.drawArc((i + k) - byte0 * 2 - 1 - i1, j + i1, byte0 * 2, byte0 * 2, 0, 90);
            }

        } else
        if(flag)
            g.drawImage(buttonImages[1], i, j, this);
        else
            g.drawImage(buttonImages[0], i, j, this);
    }

    protected void paintLabel(Graphics g, int i, int j, int k, int l, boolean flag, String s)
    {
        if(flag)
        {
            Font font = getFont();
            g.setFont(new Font(font.getName(), 1, font.getSize()));
        } else
        {
            g.setFont(getFont());
        }
        g.setColor(getForeground());
        FontMetrics fontmetrics = g.getFontMetrics();
        g.drawString(s, i + 5, (j + (l + fontmetrics.getAscent()) / 2) - 1);
    }

    private void handleException(Throwable throwable)
    {
    }

    private void initConnections()
    {
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(tabPolygons != null)
                {
                    if(fieldSelectedTab >= 0 && fieldSelectedTab < tabPolygons.length && tabPolygons[fieldSelectedTab].contains(mouseevent.getX(), mouseevent.getY()))
                        return;
                    int i = tabPolygons.length - 1;
                    do
                    {
                        if(i < 0)
                            break;
                        if(tabPolygons[i].contains(mouseevent.getX(), mouseevent.getY()))
                        {
                            setSelectedTab(i);
                            fireActionPerformed(new ActionEvent(TabButtonsPanel.this, 1001, fieldTabLabels[i]));
                            break;
                        }
                        i--;
                    } while(true);
                }
            }

        }
);
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent mouseevent)
            {
                boolean flag = false;
                if(tabPolygons != null)
                {
                    int i = tabPolygons.length - 1;
                    do
                    {
                        if(i < 0)
                            break;
                        if(tabPolygons[i].contains(mouseevent.getX(), mouseevent.getY()))
                        {
                            flag = true;
                            break;
                        }
                        i--;
                    } while(true);
                }
                if(flag)
                    setCursor(handCursor);
                else
                    setCursor(defaultCursor);
            }

            Cursor handCursor;
            Cursor defaultCursor;

            
            {
                handCursor = new Cursor(12);
                defaultCursor = new Cursor(0);
            }
        }
);
    }

    private void initialize()
    {
        try
        {
            setName("TabButtonsPanel");
            setSize(160, 120);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void paintTab(Graphics g, int i, int j, int k, int l, String s, boolean flag)
    {
        paintBackground(g, i, j, k, l, flag);
        paintLabel(g, i, j, k, l, flag, s);
    }

    protected transient ActionListener aActionListener;
    private Image bufferImage;
    private Image buttonImages[];
    private String fieldTabLabels[];
    private Polygon tabPolygons[];
    private int fieldBottom;
    private int fieldLeft;
    private int fieldMaxTabs;
    private int fieldPreferredHeight;
    private int fieldRight;
    private int fieldSelectedTab;
    private int fieldTop;
    private int tabHeight;
    private int tabWidth;



}
