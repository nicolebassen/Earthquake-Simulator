// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NomogramCanvas.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import Acme.JPM.Encoders.GifEncoder;
import Acme.JPM.Encoders.ImageEncoder;
import com.sciencecourseware.components.StringServer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            EpicenterMagnitude, Journal, MapComponent

public class NomogramCanvas extends Canvas
{
    public class PopupHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            System.out.println("Popup event: " + actionevent.getActionCommand());
            if(actionevent.getActionCommand().equals(NomogramCanvas.menuLabels[0]))
                applet.mapComponent.printIt();
            else
            if(actionevent.getActionCommand().equals(NomogramCanvas.menuLabels[1]))
                applet.mapComponent.saveIt();
        }

        public PopupHandler()
        {
        }
    }

    public class NomogramMouseMotionListener extends MouseMotionAdapter
    {

        public void mouseDragged(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
            {
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            } else
            {
                int i = mouseevent.getModifiers();
                if((i & 8) == 0 && (i & 4) == 0)
                    handleMouseMovement(mouseevent.getY());
            }
        }

        public NomogramMouseMotionListener()
        {
        }
    }

    public class NomogramMouseListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
            {
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            } else
            {
                int i = mouseevent.getModifiers();
                if((i & 8) == 0 && (i & 4) == 0)
                {
                    if(mouseevent.getX() <= mx)
                        dragging = 1;
                    else
                        dragging = 2;
                    handleMouseMovement(mouseevent.getY());
                }
            }
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(mouseevent.isPopupTrigger())
            {
                popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            } else
            {
                int i = mouseevent.getModifiers();
                if((i & 8) == 0 && (i & 4) == 0)
                    handleMouseMovement(mouseevent.getY());
                dragging = 0;
            }
        }

        public NomogramMouseListener()
        {
        }
    }


    public NomogramCanvas(EpicenterMagnitude epicentermagnitude, StringServer stringserver)
    {
        ax = 0x7fffffff;
        dx = 0x7fffffff;
        mx = 0x7fffffff;
        amplitudesY = null;
        distancesY = null;
        img = null;
        numFontMetrics = null;
        userLineFontMetrics = null;
        amplitudes = null;
        distances = null;
        dvs = null;
        showMessage = false;
        magnitude = 1.7976931348623157E+308D;
        a0 = 0x7fffffff;
        af = 0x7fffffff;
        amplitudeY = 0x7fffffff;
        d0 = 0x7fffffff;
        df = 0x7fffffff;
        distanceY = 0x7fffffff;
        dragging = 0;
        applet = epicentermagnitude;
        stringServer = stringserver;
        addMouseListener(new NomogramMouseListener());
        addMouseMotionListener(new NomogramMouseMotionListener());
        addPopUpMenu();
    }

    public static double log10(double d)
    {
        double d1 = Math.log(d) / Math.log(10D);
        return d1;
    }

    public void setDistancesAndAmplitudes(int ai[], double ad[])
    {
        if(distances == null || distances.length != ai.length)
            distances = new int[ai.length];
        System.arraycopy(ai, 0, distances, 0, ai.length);
        distancesY = null;
        if(amplitudes == null || amplitudes.length != ad.length)
            amplitudes = new double[ad.length];
        System.arraycopy(ad, 0, amplitudes, 0, ad.length);
        amplitudesY = null;
        for(int i = 0; i < amplitudes.length; i++)
        {
            if(amplitudes[i] < 10D)
            {
                amplitudes[i] = (double)(int)Math.round(amplitudes[i] * 10D) / 10D;
                continue;
            }
            if(amplitudes[i] < 99D)
            {
                amplitudes[i] = Math.round(amplitudes[i]);
            } else
            {
                int j = (int)Math.round(amplitudes[i] / 10D);
                amplitudes[i] = (double)j * 10D;
            }
        }

    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public void setShowMessage(boolean flag)
    {
        showMessage = flag;
    }

    public void handleMouseMovement(int i)
    {
        if(dragging == 1)
        {
            setShowMessage(false);
            distanceY = i;
            if(distanceY < d0)
                distanceY = d0;
            else
            if(distanceY > df)
                distanceY = df;
            repaint();
        } else
        if(dragging == 2)
        {
            setShowMessage(false);
            amplitudeY = i;
            if(amplitudeY < a0)
                amplitudeY = a0;
            else
            if(amplitudeY > af)
                amplitudeY = af;
            repaint();
        }
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(img == null || img.getWidth(this) != i || img.getHeight(this) != j)
        {
            if(!(g instanceof PrintGraphics))
                img = createImage(i, j);
            mx = i / 2;
            int k = (int)Math.round(82.799999999999997D);
            dx = mx - k;
            ax = mx + k;
            af = j - ay(0.10000000000000001D);
            a0 = j - ay(500D);
            df = j - dy(20D);
            d0 = j - dy(800D);
            dvs = new double[(df - d0) + 1];
            for(int l = 0; l < dvs.length; l++)
                dvs[l] = 0.0D;

            for(double d = 20D; d <= 800D; d += d >= 50D ? 1.0D : 0.10000000000000001D)
            {
                int i1 = j - dy(d) - d0;
                if(i1 >= 0 && i1 < dvs.length)
                    dvs[i1] = d;
            }

            dvs[0] = 800D;
            dvs[dvs.length - 1] = 20D;
            amplitudeY = j - ay(1.0D);
            distanceY = j - dy(100D);
        }
        if(img != null || (g instanceof PrintGraphics))
        {
            Graphics g1;
            if(g instanceof PrintGraphics)
                g1 = g;
            else
                g1 = img.getGraphics();
            if(g1 != null)
            {
                g1.setColor(getBackground());
                g1.fillRect(0, 0, i, j);
                g1.setColor(getForeground());
                g1.setFont(numFont);
                if(numFontMetrics == null)
                    numFontMetrics = g1.getFontMetrics();
                paintMagnitudeScale(g1, i, j, numFontMetrics);
                paintAmplitudeScale(g1, i, j, numFontMetrics);
                paintDistanceScale(g1, i, j, numFontMetrics);
                g1.setColor(Color.red);
                g1.setFont(userLineFont);
                if(userLineFontMetrics == null)
                    userLineFontMetrics = g1.getFontMetrics();
                paintUserLine(g1, i, j, userLineFontMetrics);
                if(!(g instanceof PrintGraphics))
                    g1.dispose();
            }
            if(!(g instanceof PrintGraphics))
                g.drawImage(img, 0, 0, this);
        }
    }

    public void print(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height + 25);
        Graphics g1 = g.create(0, 25, getSize().width, getSize().height + 25);
        paint(g1);
        g1.dispose();
        String s = applet.getJournal().getUserName();
        String s1 = applet.getJournal().getDate();
        g.setColor(Color.black);
        g.setFont(PRINT_TITLE_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        if(s != null && s.length() > 0)
            g.drawString(s, 5, 5 + fontmetrics.getAscent());
        if(s1 != null && s1.length() > 0)
            g.drawString(s1, getSize().width - 5 - fontmetrics.stringWidth(s1), 5 + fontmetrics.getAscent());
    }

    public void saveIt(File file)
        throws IOException
    {
        GifEncoder gifencoder = new GifEncoder(img, new FileOutputStream(file));
        gifencoder.encode();
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    protected double adjustAmplitude(double d, int i)
    {
        if(amplitudesY == null)
        {
            amplitudesY = new int[amplitudes.length];
            int j = getSize().height;
            for(int l = 0; l < amplitudes.length; l++)
                amplitudesY[l] = j - ay(amplitudes[l]);

        }
        for(int k = 0; k < amplitudesY.length; k++)
            if(Math.abs(amplitudesY[k] - i) < 2)
                return amplitudes[k];

        return d;
    }

    protected int adjustDistance(int i, int j)
    {
        if(distancesY == null)
        {
            distancesY = new int[distances.length];
            int k = getSize().height;
            for(int i1 = 0; i1 < distances.length; i1++)
                if(Math.abs(distances[i1]) < 1)
                    distancesY[i1] = 0x7fffffff;
                else
                    distancesY[i1] = k - dy(distances[i1]);

        }
        for(int l = 0; l < distancesY.length; l++)
            if(Math.abs(distancesY[l] - j) < 2)
            {
                System.out.println("Matched distanceY of " + distancesY[l] + " with user distanceY of " + j + ".");
                System.out.println("The distance for this one is " + distances[l]);
                return distances[l];
            }

        return i;
    }

    private void addPopUpMenu()
    {
        popup = new PopupMenu();
        for(int i = 0; i < menuLabels.length; i++)
        {
            MenuItem menuitem = new MenuItem(menuLabels[i]);
            menuitem.setActionCommand(menuLabels[i]);
            menuitem.addActionListener(new PopupHandler());
            popup.add(menuitem);
        }

        add(popup);
    }

    private int ay(double d)
    {
        double d1 = log10(d * 10D) * 108D + 72D;
        return (int)Math.round(d1 * 0.57499999999999996D);
    }

    private double ayr(double d)
    {
        double d1 = d / 0.57499999999999996D;
        double d2 = Math.pow(10D, (d1 - 72D) / 108D) / 10D;
        if(d2 < 0.10000000000000001D)
            d2 = 0.10000000000000001D;
        else
        if(d2 > 500D)
            d2 = 500D;
        return d2;
    }

    private int dy(double d)
    {
        double d1 = (((d * d * 0.00085919999999999996D + d * 0.10811900000000001D) - 2.507889D) / ((d * d * 1.3349999999999999E-06D + d * 0.0020479999999999999D) - 0.018665000000000001D)) * 1.63025D + 41.9206D;
        return (int)Math.round(d1 * 0.57499999999999996D);
    }

    private double dyr(double d)
    {
        int i = (int)Math.round(d) - d0;
        double d1;
        if(i < 0)
            d1 = dvs[0];
        else
        if(i >= dvs.length)
            d1 = dvs[dvs.length - 1];
        else
            d1 = dvs[i];
        return d1;
    }

    private int my(double d)
    {
        double d1 = d * 54D + 18D;
        return (int)Math.round(d1 * 0.57499999999999996D);
    }

    private double myr(int i)
    {
        double d = (double)i / 0.57499999999999996D;
        double d1 = (d - 18D) / 54D;
        return d1;
    }

    private void paintAmplitudeScale(Graphics g, int i, int j, FontMetrics fontmetrics)
    {
        String s = stringServer.getString("nomogram_amplitude_0");
        int k = j - (fontmetrics.getAscent() * 5) / 4;
        int l = (ax + 8) - fontmetrics.stringWidth(s) / 2;
        g.drawString(s, l, k);
        s = stringServer.getString("nomogram_amplitude_1");
        k += fontmetrics.getAscent();
        l = (ax + 8) - fontmetrics.stringWidth(s) / 2;
        g.drawString(s, l, k);
        for(int i1 = -1; i1 < 2; i1++)
            g.drawLine(ax + i1, j - ay(0.10000000000000001D), ax + i1, j - ay(500D));

        for(double d = 0.10000000000000001D; d <= 1.0D; d += 0.10000000000000001D)
        {
            int k1 = j - ay(d);
            g.drawLine(ax, k1, ax + 4, k1);
        }

        for(double d1 = 1.0D; d1 <= 10D; d1++)
        {
            int l1 = j - ay(d1);
            g.drawLine(ax, l1, ax + 4, l1);
        }

        for(double d2 = 100D; d2 <= 500D; d2 += 100D)
        {
            int i2 = j - ay(d2);
            g.drawLine(ax, i2, ax + 4, i2);
        }

        int j1 = j - ay(0.10000000000000001D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString(EpicenterMagnitude.outputNumberString("0.1"), ax + 11, j1 + fontmetrics.getAscent() / 2);
        for(double d3 = 0.12D; d3 <= 0.20000000000000001D; d3 += 0.02D)
        {
            j1 = j - ay(d3);
            g.drawLine(ax, j1, ax + 4, j1);
        }

        j1 = j - ay(0.20000000000000001D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString(EpicenterMagnitude.outputNumberString("0.2"), ax + 11, j1 + fontmetrics.getAscent() / 2);
        j1 = j - ay(0.5D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString(EpicenterMagnitude.outputNumberString("0.5"), ax + 11, j1 + fontmetrics.getAscent() / 2);
        j1 = j - ay(1.0D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString(EpicenterMagnitude.outputNumberString("1.0"), ax + 11, j1 + fontmetrics.getAscent() / 2);
        for(double d4 = 1.2D; d4 <= 2D; d4 += 0.20000000000000001D)
        {
            j1 = j - ay(d4);
            g.drawLine(ax, j1, ax + 4, j1);
        }

        j1 = j - ay(2D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString(EpicenterMagnitude.outputNumberString("2.0"), ax + 11, j1 + fontmetrics.getAscent() / 2);
        j1 = j - ay(5D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString(EpicenterMagnitude.outputNumberString("5.0"), ax + 11, j1 + fontmetrics.getAscent() / 2);
        j1 = j - ay(10D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString("10", ax + 11, j1 + fontmetrics.getAscent() / 2);
        for(double d5 = 12D; d5 <= 20D; d5 += 2D)
        {
            j1 = j - ay(d5);
            g.drawLine(ax, j1, ax + 4, j1);
        }

        j1 = j - ay(20D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString("20", ax + 11, j1 + fontmetrics.getAscent() / 2);
        for(double d6 = 30D; d6 <= 40D; d6 += 10D)
        {
            j1 = j - ay(d6);
            g.drawLine(ax, j1, ax + 4, j1);
        }

        j1 = j - ay(50D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString("50", ax + 11, j1 + fontmetrics.getAscent() / 2);
        for(double d7 = 60D; d7 <= 90D; d7 += 10D)
        {
            j1 = j - ay(d7);
            g.drawLine(ax, j1, ax + 4, j1);
        }

        j1 = j - ay(100D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString("100", ax + 11, j1 + fontmetrics.getAscent() / 2);
        for(double d8 = 120D; d8 <= 200D; d8 += 20D)
        {
            j1 = j - ay(d8);
            g.drawLine(ax, j1, ax + 4, j1);
        }

        j1 = j - ay(200D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString("200", ax + 11, j1 + fontmetrics.getAscent() / 2);
        j1 = j - ay(500D);
        g.drawLine(ax, j1, ax + 8, j1);
        g.drawString("500", ax + 11, j1 + fontmetrics.getAscent() / 2);
    }

    private void paintDistanceScale(Graphics g, int i, int j, FontMetrics fontmetrics)
    {
        String s = stringServer.getString("nomogram_distance_0");
        int k = j - (fontmetrics.getAscent() * 5) / 4;
        int l = dx - 8 - fontmetrics.stringWidth(s) / 2;
        g.drawString(s, l, k);
        s = stringServer.getString("nomogram_distance_1");
        k += fontmetrics.getAscent();
        l = dx - 8 - fontmetrics.stringWidth(s) / 2;
        g.drawString(s, l, k);
        for(int i1 = -1; i1 < 2; i1++)
            g.drawLine(dx + i1, j - dy(20D), dx + i1, j - dy(800D));

        for(double d = 20D; d <= 30D; d += 2D)
        {
            int j1 = j - dy(d);
            g.drawLine(dx, j1, dx - 4, j1);
        }

        for(double d1 = 30D; d1 <= 40D; d1 += 5D)
        {
            int k1 = j - dy(d1);
            g.drawLine(dx, k1, dx - 4, k1);
        }

        for(double d2 = 40D; d2 <= 100D; d2 += 10D)
        {
            int l1 = j - dy(d2);
            g.drawLine(dx, l1, dx - 4, l1);
        }

        for(double d3 = 100D; d3 <= 800D; d3 += 20D)
        {
            int i2 = j - dy(d3);
            g.drawLine(dx, i2, dx - 4, i2);
        }

        for(double d4 = 20D; d4 <= 40D; d4 += 10D)
        {
            int j2 = j - dy(d4);
            g.drawLine(dx, j2, dx - 8, j2);
            String s1 = Integer.toString((int)d4);
            g.drawString(s1, dx - 10 - fontmetrics.stringWidth(s1), j2 + fontmetrics.getAscent() / 2);
        }

        for(double d5 = 40D; d5 <= 60D; d5 += 20D)
        {
            int k2 = j - dy(d5);
            g.drawLine(dx, k2, dx - 8, k2);
            String s2 = Integer.toString((int)d5);
            g.drawString(s2, dx - 10 - fontmetrics.stringWidth(s2), k2 + fontmetrics.getAscent() / 2);
        }

        for(double d6 = 100D; d6 <= 800D; d6 += 100D)
        {
            int l2 = j - dy(d6);
            g.drawLine(dx, l2, dx - 8, l2);
            String s3 = Integer.toString((int)d6);
            g.drawString(s3, dx - 10 - fontmetrics.stringWidth(s3), l2 + fontmetrics.getAscent() / 2);
        }

    }

    private void paintMagnitudeScale(Graphics g, int i, int j, FontMetrics fontmetrics)
    {
        String s = stringServer.getString("richter_magnitude_0");
        int k = j - (fontmetrics.getAscent() * 5) / 4;
        int l = mx - fontmetrics.stringWidth(s) / 2;
        g.drawString(s, l, k);
        s = stringServer.getString("richter_magnitude_1");
        k += fontmetrics.getAscent();
        l = mx - fontmetrics.stringWidth(s) / 2;
        g.drawString(s, l, k);
        for(int i1 = -1; i1 < 2; i1++)
            g.drawLine(mx + i1, j - my(0.5D), mx + i1, j - my(8.5D));

        for(double d = 0.5D; d <= 8.5D; d += 0.10000000000000001D)
        {
            int j1 = j - my(d);
            g.drawLine(mx - 3, j1, mx + 3, j1);
        }

        for(double d1 = 0.5D; d1 <= 8.5D; d1 += 0.5D)
        {
            int k1 = j - my(d1);
            g.drawLine(mx - 6, k1, mx + 6, k1);
            if(d1 == (double)(int)d1)
            {
                String s1 = EpicenterMagnitude.outputNumberString(Integer.toString((int)d1) + ".0");
                g.drawString(s1, mx - 8 - fontmetrics.stringWidth(s1), k1 + fontmetrics.getAscent() / 2);
            }
        }

    }

    private void paintMessages(Graphics g, int i, int j, int k, int l)
    {
        g.setColor(MESSAGE_COLOR);
        g.setFont(MESSAGE_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        try
        {
            int i1 = Integer.parseInt(stringServer.getString("nomogram_distance_message_num_lines"));
            int j1 = 3;
            int k1 = j / 2 - (fontmetrics.getAscent() * i1) / 2 - 2;
            int l1 = 0;
            for(int i2 = 0; i2 < i1; i2++)
            {
                int j2 = k1 + fontmetrics.getAscent() * (i2 + 1);
                String s = stringServer.getString("nomogram_distance_message_line_" + i2);
                l1 = Math.max(l1, fontmetrics.stringWidth(s));
                g.drawString(s, j1, j2);
            }

            g.drawLine(j1 + l1, j / 2, dx - 30, k);
            i1 = Integer.parseInt(stringServer.getString("nomogram_amplitude_message_num_lines"));
            String as[] = new String[i1];
            l1 = 0;
            for(int k2 = 0; k2 < i1; k2++)
            {
                as[k2] = stringServer.getString("nomogram_amplitude_message_line_" + k2);
                int k3 = fontmetrics.stringWidth(as[k2]);
                l1 = Math.max(l1, k3);
            }

            j1 = i - 3 - l1;
            k1 = j / 2 - (fontmetrics.getAscent() * i1) / 2 - 2;
            for(int l2 = 0; l2 < i1; l2++)
            {
                int l3 = k1 + fontmetrics.getAscent() * (l2 + 1);
                g.drawString(as[l2], j1, l3);
            }

            g.drawLine(j1, j / 2, ax + 30, l);
            i1 = Integer.parseInt(stringServer.getString("nomogram_magnitude_message_num_lines"));
            as = new String[i1];
            l1 = 0;
            for(int i3 = 0; i3 < i1; i3++)
            {
                as[i3] = stringServer.getString("nomogram_magnitude_message_line_" + i3);
                l1 = Math.max(l1, fontmetrics.stringWidth(as[i3]));
            }

            j1 = dx - l1 - 35;
            for(int j3 = 0; j3 < i1; j3++)
            {
                int i4 = fontmetrics.getAscent() * (j3 + 1);
                g.drawString(as[j3], j1, i4);
            }

            g.drawLine(dx - 35, (fontmetrics.getAscent() * i1) / 2, mx, j / 4);
        }
        catch(NumberFormatException numberformatexception)
        {
            System.err.println("Could not read number of lines for one of the nomogram messages.");
        }
    }

    private void paintUserLine(Graphics g, int i, int j, FontMetrics fontmetrics)
    {
        double d = adjustAmplitude(ayr(j - amplitudeY), amplitudeY);
        int k = adjustDistance((int)Math.round(dyr(distanceY)), distanceY);
        int l = j - dy(k);
        int i1 = j - ay(d);
        g.drawLine(dx, l, ax, i1);
        String s = Integer.toString(k);
        g.drawString(s, dx - 31 - fontmetrics.stringWidth(s), l + fontmetrics.getAscent() / 2);
        g.drawLine(dx - 30, l, dx, l);
        NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
        if(d < 10D)
        {
            numberformat.setMaximumFractionDigits(1);
            numberformat.setMinimumFractionDigits(1);
            numberformat.setMaximumIntegerDigits(1);
            numberformat.setMinimumIntegerDigits(1);
        } else
        {
            numberformat.setMaximumFractionDigits(0);
            numberformat.setMinimumFractionDigits(0);
            numberformat.setMaximumIntegerDigits(3);
            numberformat.setMinimumIntegerDigits(1);
        }
        if(d >= 99D)
        {
            int j1 = (int)Math.round(d / 10D);
            s = Integer.toString(j1 * 10);
        } else
        {
            s = EpicenterMagnitude.outputNumberString(numberformat.format(d));
        }
        g.drawString(s, ax + 31, i1 + fontmetrics.getAscent() / 2);
        g.drawLine(ax, i1, ax + 30, i1);
        if(showMessage)
            paintMessages(g, i, j, l, i1);
    }

    public static final int DRAGGING_AMPLITUDE = 2;
    public static final int DRAGGING_DISTANCE = 1;
    public static final int DRAGGING_NOTHING = 0;
    public static final Color MESSAGE_COLOR;
    public static final Font MESSAGE_FONT = new Font("Helvetica", 1, 13);
    public static final Font PRINT_TITLE_FONT = new Font("Helvetica", 1, 14);
    public static final String menuLabels[] = {
        "Print", "Save"
    };
    public static final double nomoScale = 0.57499999999999996D;
    public static final Font numFont = new Font("SansSerif", 1, 9);
    public static final Font userLineFont = new Font("SansSerif", 1, 12);
    public int ax;
    public int dx;
    public int mx;
    protected int amplitudesY[];
    protected int distancesY[];
    Image img;
    private EpicenterMagnitude applet;
    private FontMetrics numFontMetrics;
    private FontMetrics userLineFontMetrics;
    private PopupMenu popup;
    private StringServer stringServer;
    private double amplitudes[];
    private int distances[];
    private double dvs[];
    private boolean showMessage;
    private double magnitude;
    private int a0;
    private int af;
    private int amplitudeY;
    private int d0;
    private int df;
    private int distanceY;
    private int dragging;

    static 
    {
        MESSAGE_COLOR = Color.blue;
    }



}
