// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VerticalFlowLayout.java

package com.sciencecourseware.components;

import java.awt.*;

public class VerticalFlowLayout
    implements LayoutManager
{

    public VerticalFlowLayout()
    {
        this(0);
    }

    public VerticalFlowLayout(int i)
    {
        vgap = 0;
        if(i < 0)
            vgap = 0;
        else
            vgap = i;
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void layoutContainer(Container container)
    {
        Insets insets = container.getInsets();
        int i = container.getSize().width - insets.left - insets.right;
        int j = container.getComponentCount();
        if(j == 0)
            return;
        int k = insets.top;
        int l = insets.left;
        for(int i1 = 0; i1 < j; i1++)
        {
            Component component = container.getComponent(i1);
            if(component.isVisible())
            {
                Dimension dimension = component.getPreferredSize();
                component.setBounds(l, k, i, dimension.height);
                k += dimension.height + vgap;
            }
        }

    }

    public Dimension minimumLayoutSize(Container container)
    {
        Insets insets = container.getInsets();
        int i = 0;
        int j = 0;
        int k = container.getComponentCount();
        for(int l = 0; l < k; l++)
        {
            Component component = container.getComponent(l);
            if(component.isVisible())
            {
                Dimension dimension1 = component.getMinimumSize();
                i = Math.max(i, dimension1.width);
                j += dimension1.height;
            }
        }

        Dimension dimension = new Dimension(i + insets.left + insets.right, j + insets.top + insets.bottom + vgap * k);
        return dimension;
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Insets insets = container.getInsets();
        int i = 0;
        int j = 0;
        int k = container.getComponentCount();
        for(int l = 0; l < k; l++)
        {
            Component component = container.getComponent(l);
            if(component.isVisible())
            {
                Dimension dimension1 = component.getPreferredSize();
                i = Math.max(i, dimension1.width);
                j += dimension1.height;
            }
        }

        Dimension dimension = new Dimension(i + insets.left + insets.right, j + insets.top + insets.bottom + vgap * k);
        return dimension;
    }

    public void removeLayoutComponent(Component component)
    {
    }

    private int vgap;
}
