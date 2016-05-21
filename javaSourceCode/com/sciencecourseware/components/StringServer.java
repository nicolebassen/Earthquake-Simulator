// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringServer.java

package com.sciencecourseware.components;

import electric1.util.Nodes;
import electric1.xml.*;
import java.util.Hashtable;

public class StringServer
{

    public StringServer(Element element)
    {
        setRoot(element);
    }

    public void setRoot(Element element)
    {
        Elements elements = element.getElements("string");
        int i = elements.size();
        strings = new Hashtable(i + 10);
        addElements(elements);
    }

    public String getString(String s)
    {
        if(strings == null || s == null)
            return "";
        String s1 = (String)strings.get(s);
        if(s1 == null)
            return "";
        else
            return s1;
    }

    public void addElements(Elements elements)
    {
        do
        {
            if(!elements.hasMoreElements())
                break;
            Element element = elements.next();
            Attribute attribute = element.getAttribute("name");
            if(attribute != null)
            {
                String s = attribute.getValue();
                String s1 = element.getTextString();
                if(s != null && s1 != null)
                {
                    s1 = translateSpecialChars(s1);
                    addString(s, s1);
                }
            }
        } while(true);
    }

    public void addRoot(Element element)
    {
        Elements elements = element.getElements("string");
        addElements(elements);
    }

    public void addString(String s, String s1)
    {
        if(strings == null)
            strings = new Hashtable();
        strings.put(s, s1);
    }

    private String translateSpecialChars(String s)
    {
        for(int i = s.indexOf("\\deg;"); i >= 0; i = s.indexOf("&deg;"))
        {
            if(i == 0 && s.length() > 5)
            {
                s = "\260" + s.substring(5);
                continue;
            }
            if(i == 0)
            {
                s = "\260";
                continue;
            }
            if(i == s.length() - 5)
                s = s.substring(0, s.length() - 5) + "\260";
            else
                s = s.substring(0, i) + "\260" + s.substring(i + 5);
        }

        return s;
    }

    Hashtable strings;
}
