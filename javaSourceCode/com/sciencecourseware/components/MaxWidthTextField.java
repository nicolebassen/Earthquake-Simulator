// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MaxWidthTextField.java

package com.sciencecourseware.components;

import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.Enumeration;
import java.util.Vector;

public class MaxWidthTextField extends TextField
    implements TextListener
{

    public MaxWidthTextField()
    {
        acceptedCharacters = null;
        maxWidth = 0x7fffffff;
        textListeners = new Vector();
        addHandlers();
    }

    public MaxWidthTextField(int i)
    {
        super(i);
        acceptedCharacters = null;
        maxWidth = 0x7fffffff;
        textListeners = new Vector();
        addHandlers();
    }

    public MaxWidthTextField(int i, int j)
    {
        super(i);
        acceptedCharacters = null;
        maxWidth = 0x7fffffff;
        textListeners = new Vector();
        setMaxWidth(j);
        addHandlers();
    }

    public MaxWidthTextField(String s)
    {
        super(s);
        acceptedCharacters = null;
        maxWidth = 0x7fffffff;
        textListeners = new Vector();
        addHandlers();
    }

    public MaxWidthTextField(String s, int i)
    {
        super(s, i);
        acceptedCharacters = null;
        maxWidth = 0x7fffffff;
        textListeners = new Vector();
        addHandlers();
    }

    public MaxWidthTextField(String s, int i, int j)
    {
        super(s, i);
        acceptedCharacters = null;
        maxWidth = 0x7fffffff;
        textListeners = new Vector();
        setMaxWidth(j);
        addHandlers();
    }

    public void setAcceptNumberOnly()
    {
        setAcceptedCharacters("0123456789.-");
    }

    public void setAcceptedCharacters(String s)
    {
        acceptedCharacters = s;
    }

    public String getAcceptedCharacters()
    {
        return acceptedCharacters;
    }

    public void setAcceptingNumberOnly(boolean flag)
    {
        if(flag)
            setAcceptedCharacters("0123456789.-");
        else
        if(getAcceptedCharacters() != null && getAcceptedCharacters().equals("0123456789.-"))
            setAcceptedCharacters(null);
    }

    public boolean isAcceptingNumberOnly()
    {
        if(getAcceptedCharacters() == null)
            return false;
        else
            return getAcceptedCharacters().equals("0123456789.-");
    }

    public void setMaxWidth(int i)
    {
        maxWidth = i;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public void addTextListener(TextListener textlistener)
    {
        if(textlistener != null)
            textListeners.addElement(textlistener);
    }

    public void removeTextListener(TextListener textlistener)
    {
        if(textlistener != null)
            textListeners.removeElement(textlistener);
    }

    public void textValueChanged(TextEvent textevent)
    {
        String s = getText();
        boolean flag = false;
        if(acceptedCharacters != null)
        {
            StringBuffer stringbuffer = new StringBuffer(s.length());
            for(int k = 0; k < s.length(); k++)
            {
                char c = s.charAt(k);
                if(acceptedCharacters.indexOf(c) >= 0)
                    stringbuffer.append(c);
                else
                    flag = true;
            }

            s = stringbuffer.toString();
        }
        if(s.length() > getMaxWidth())
        {
            int i = getCaretPosition() - 1;
            setText(savedText);
            if(i < 0)
                i = 0;
            if(i <= savedText.length())
                setCaretPosition(i);
            else
                setCaretPosition(savedText.length());
            return;
        }
        if(flag)
        {
            int j = getCaretPosition() - 1;
            setText(s);
            if(j < 0)
                j = 0;
            if(j <= s.length())
                setCaretPosition(j);
            else
                setCaretPosition(s.length());
        }
        savedText = s;
        fireTextListeners();
    }

    private void addHandlers()
    {
        super.addTextListener(this);
        savedText = getText();
    }

    private void fireTextListeners()
    {
        TextEvent textevent = new TextEvent(this, 900);
        TextListener textlistener;
        for(Enumeration enumeration = textListeners.elements(); enumeration.hasMoreElements(); textlistener.textValueChanged(textevent))
            textlistener = (TextListener)enumeration.nextElement();

    }

    public static final String NUMBER_ONLY_CHARACTERS = "0123456789.-";
    private String acceptedCharacters;
    private int maxWidth;
    private String savedText;
    private Vector textListeners;
}
