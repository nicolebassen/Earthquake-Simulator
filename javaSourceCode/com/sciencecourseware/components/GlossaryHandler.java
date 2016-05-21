// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GlossaryHandler.java

package com.sciencecourseware.components;

import java.applet.AppletContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GlossaryHandler
    implements ActionListener
{

    public GlossaryHandler()
    {
        this(null, null, null);
    }

    public GlossaryHandler(AppletContext appletcontext)
    {
        this(appletcontext, null, null);
    }

    public GlossaryHandler(AppletContext appletcontext, URL url)
    {
        this(appletcontext, url, null);
    }

    public GlossaryHandler(AppletContext appletcontext, URL url, String s)
    {
        setAppletContext(appletcontext);
        setGlossaryURL(url);
        setGlossaryTarget(s);
    }

    public void setAppletContext(AppletContext appletcontext)
    {
        appletContext = appletcontext;
    }

    public AppletContext getAppletContext()
    {
        return appletContext;
    }

    public void setGlossaryTarget(String s)
    {
        glossaryTarget = s;
    }

    public String getGlossaryTarget()
    {
        return glossaryTarget;
    }

    public void setGlossaryURL(URL url)
    {
        glossaryURL = url;
    }

    public URL getGlossaryURL()
    {
        return glossaryURL;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = actionevent.getActionCommand();
        System.out.println("Action Command received by GlossaryHandler = \"" + s + "\"");
        if(getGlossaryURL() != null)
            try
            {
                URL url = new URL(getGlossaryURL(), "&term=" + actionevent.getActionCommand());
                if(getGlossaryTarget() != null)
                    getAppletContext().showDocument(url, getGlossaryTarget());
                else
                    getAppletContext().showDocument(url);
            }
            catch(MalformedURLException malformedurlexception)
            {
                malformedurlexception.printStackTrace();
            }
    }

    private AppletContext appletContext;
    private String glossaryTarget;
    private URL glossaryURL;
}
