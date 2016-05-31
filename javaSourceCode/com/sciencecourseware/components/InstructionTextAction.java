// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InstructionTextAction.java

package com.sciencecourseware.components;

import java.awt.Point;
import java.awt.Rectangle;

public class InstructionTextAction
{

    public InstructionTextAction()
    {
        this(null, null);
    }

    public InstructionTextAction(Rectangle rectangle1, String s)
    {
        toolTipText = null;
        setRectangle(rectangle1);
        setActionCommand(s);
    }

    public void setActionCommand(String s)
    {
        actionCommand = s;
    }

    public String getActionCommand()
    {
        return actionCommand;
    }

    public void setRectangle(Rectangle rectangle1)
    {
        rectangle = rectangle1;
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public void setToolTipText(String s)
    {
        toolTipText = s;
    }

    public String getToolTipText()
    {
        return toolTipText;
    }

    public boolean contains(Point point)
    {
        if(rectangle != null)
            return rectangle.contains(point);
        else
            return false;
    }

    public String toString()
    {
        String s = "InstructionTextAction: action=\"" + getActionCommand() + "\", rectangle: " + getRectangle();
        return s;
    }

    private String actionCommand;
    private Rectangle rectangle;
    private String toolTipText;
}
