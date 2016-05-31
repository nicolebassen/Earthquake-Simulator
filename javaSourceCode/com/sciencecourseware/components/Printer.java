// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Printer.java

package com.sciencecourseware.components;

import java.awt.*;
import java.util.Vector;

public class Printer extends Canvas
{
    class PrintTab extends PrinterObject
    {

        public void draw(Graphics g, Point point)
        {
            if(Printer.tab_width == 0)
                Printer.tab_width = g.getFontMetrics(tabFnt).stringWidth("W");
            if(point.x < tab_dist * Printer.tab_width)
                point.x = tab_dist * Printer.tab_width;
        }

        Font tabFnt;
        int tab_dist;

        public PrintTab(Font font, int i)
        {
            tabFnt = font;
            tab_dist = i;
        }
    }

    class NewLine extends PrinterObject
    {

        public void draw(Graphics g, Point point)
        {
            point.x = 20;
            point.y += g.getFontMetrics(g.getFont()).getHeight();
        }

        NewLine()
        {
        }
    }

    abstract class PrinterObject
    {

        abstract void draw(Graphics g, Point point);

        PrinterObject()
        {
        }
    }

    class PrintString extends PrinterObject
    {

        public void draw(Graphics g, Point point)
        {
            FontMetrics fontmetrics = g.getFontMetrics();
            g.drawString(st, point.x, point.y + fontmetrics.getHeight());
            point.x += fontmetrics.stringWidth(st);
        }

        String st;

        public PrintString(String s)
        {
            st = s;
        }
    }

    class PrintFont extends PrinterObject
    {

        public void draw(Graphics g, Point point)
        {
            g.setFont(fnt);
            if(point.y <= 0)
                point.y = g.getFontMetrics(fnt).getHeight();
        }

        Font fnt;

        public PrintFont(Font font)
        {
            fnt = font;
        }
    }


    public Printer(Frame frame)
    {
        this(frame, "Printer");
    }

    public Printer(Frame frame, String s)
    {
        linesPerPage = 48;
        title = s;
        f = frame;
        f.add(this);
        setVisible(false);
        pjob = null;
        pt = new Point(20, 20);
        objects = new Vector();
        tabFont = new Font("Courier", 0, 12);
        fnt = new Font("Helvetica", 0, 12);
        curFont = fnt;
        numLines = 0;
    }

    public void setFont(Font font)
    {
        objects.addElement(new PrintFont(font));
        curFont = font;
    }

    public void setLinesPerPage(int i)
    {
        linesPerPage = i;
    }

    public int getLinesRemainingOnPage()
    {
        int i = linesPerPage - numLines;
        if(i < 0)
            i = 0;
        return i;
    }

    public int getPageWidth()
    {
        return pageSize().width - 40;
    }

    public void endJob()
    {
        pjob.end();
    }

    public void finalize()
    {
        if(objects.size() > 0)
            newPage();
        endJob();
    }

    public void newPage()
    {
        if(pjob == null)
            pjob = getToolkit().getPrintJob(f, title, null);
        pg = pjob.getGraphics();
        print(pg);
        pg.dispose();
        pt = new Point(20, 0);
        objects = new Vector();
        numLines = 0;
    }

    public Dimension pageSize()
    {
        if(pjob == null)
        {
            return new Dimension(620, 790);
        } else
        {
            pjob = getToolkit().getPrintJob(f, title, null);
            return pjob.getPageDimension();
        }
    }

    public void paint(Graphics g)
    {
        pt = new Point(0, 0);
        print(g);
    }

    public void print(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        g.setColor(Color.white);
        g.fillRect(0, 0, i, j);
        g.setColor(Color.black);
        f.setFont(fnt);
        for(int k = 0; k < objects.size(); k++)
        {
            PrinterObject printerobject = (PrinterObject)objects.elementAt(k);
            printerobject.draw(g, pt);
        }

    }

    public void print(String s)
    {
        objects.addElement(new PrintString(s));
    }

    public void println()
    {
        objects.addElement(new NewLine());
        if(++numLines >= linesPerPage)
        {
            Font font = curFont;
            newPage();
            setFont(font);
        }
    }

    public void println(String s)
    {
        print(s);
        println();
    }

    public void tab(int i)
    {
        objects.addElement(new PrintTab(tabFont, i));
    }

    public static final int LEFTMARGIN = 20;
    static int tab_width = 0;
    Font curFont;
    Frame f;
    Font fnt;
    int linesPerPage;
    int numLines;
    Vector objects;
    Graphics pg;
    PrintJob pjob;
    Point pt;
    Font tabFont;
    String title;

}
