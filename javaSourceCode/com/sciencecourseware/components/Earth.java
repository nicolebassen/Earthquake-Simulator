// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Earth.java

package com.sciencecourseware.components;

import java.awt.*;

public class Earth extends Canvas
    implements Runnable
{

    public Earth()
    {
        this(null);
    }

    public Earth(Image aimage[])
    {
        earthStep = 0;
        earthImages = aimage;
    }

    public void setEarthImages(Image aimage[])
    {
        earthImages = aimage;
        repaint();
    }

    public void paint(Graphics g)
    {
        if(earthImages != null && earthStep < earthImages.length)
            g.drawImage(earthImages[earthStep], 0, 0, this);
        if(th == null || !th.isAlive())
        {
            th = new Thread(this);
            th.start();
        }
    }

    public void run()
    {
        for(; isVisible(); repaint())
        {
            try
            {
                Thread.sleep(100L);
            }
            catch(InterruptedException interruptedexception) { }
            if(++earthStep >= earthImages.length)
                earthStep = 0;
        }

    }

    public void update(Graphics g)
    {
        paint(g);
    }

    Image earthImages[];
    int earthStep;
    Thread th;
}
