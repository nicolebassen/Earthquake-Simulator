// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImageServer.java

package com.sciencecourseware.components;

import electric1.util.Nodes;
import electric1.xml.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.PrintStream;
import java.util.Hashtable;

public class ImageServer
{

    public ImageServer(Applet applet1)
    {
        imageHashTable = null;
        applet = applet1;
    }

    public Image getImage(String s)
    {
        if(imageHashTable == null)
            return null;
        else
            return (Image)imageHashTable.get(s);
    }

    public void loadImages(String s)
    {
        try
        {
            Document document = new Document(s);
            Element element = document.getRoot();
            MediaTracker mediatracker = new MediaTracker(applet);
            if(imageHashTable == null)
                imageHashTable = new Hashtable(element.getElements().size());
            int i = 0;
            Elements elements = element.getElements();
            do
            {
                if(!elements.hasMoreElements())
                    break;
                Element element1 = elements.next();
                String s1 = element1.getElement("name").getTextString();
                String s2 = element1.getElement("filename").getTextString();
                java.net.URL url = getClass().getResource("/" + s2);
                Image image = null;
                image = applet.getAppletContext().getImage(url);
                if(image != null)
                {
                    mediatracker.addImage(image, i++);
                    imageHashTable.put(s1, image);
                }
            } while(true);
            mediatracker.waitForAll();
            System.out.println("ImageServer loaded " + i + " images.");
        }
        catch(ParseException parseexception)
        {
            System.err.println(parseexception);
        }
        catch(InterruptedException interruptedexception)
        {
            System.err.println(interruptedexception);
        }
    }

    public static final boolean DEBUG = false;
    protected Applet applet;
    protected Hashtable imageHashTable;
}
