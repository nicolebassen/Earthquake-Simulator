// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ComponentToolTipListener.java

package com.sciencecourseware.components;

import java.awt.Point;

// Referenced classes of package com.sciencecourseware.components:
//            ToolTipComponent

public interface ComponentToolTipListener
{

    public abstract void hideComponentToolTip(ToolTipComponent tooltipcomponent);

    public abstract void showComponentToolTip(ToolTipComponent tooltipcomponent, int i, int j);

    public abstract void showComponentToolTip(ToolTipComponent tooltipcomponent, Point point);
}
