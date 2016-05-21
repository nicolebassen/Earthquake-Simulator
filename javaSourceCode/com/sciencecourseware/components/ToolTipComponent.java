// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ToolTipComponent.java

package com.sciencecourseware.components;

import java.awt.Panel;

// Referenced classes of package com.sciencecourseware.components:
//            ComponentToolTipListener

public abstract class ToolTipComponent extends Panel
{

    public ToolTipComponent()
    {
        toolTipListener = null;
    }

    public void setToolTipListener(ComponentToolTipListener componenttooltiplistener)
    {
        toolTipListener = componenttooltiplistener;
    }

    public ComponentToolTipListener getToolTipListener()
    {
        return toolTipListener;
    }

    public abstract void setToolTipText(String s);

    public abstract String getToolTipText();

    protected ComponentToolTipListener toolTipListener;
}
