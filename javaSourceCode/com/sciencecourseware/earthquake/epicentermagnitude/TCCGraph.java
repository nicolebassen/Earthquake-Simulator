// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TCCGraph.java

package com.sciencecourseware.earthquake.epicentermagnitude;

import Acme.JPM.Encoders.GifEncoder;
import Acme.JPM.Encoders.ImageEncoder;
import com.sciencecourseware.components.StringServer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

// Referenced classes of package com.sciencecourseware.earthquake.epicentermagnitude:
//            MapComponent, EpicenterMagnitude, Journal, PointData

public class TCCGraph extends Canvas
{
    public class PopupHandler
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            System.out.println("Popup event: " + actionevent.getActionCommand());
            if(actionevent.getActionCommand().equals(TCCGraph.menuLabels[0]))
                mapComponent.printIt();
            else
            if(actionevent.getActionCommand().equals(TCCGraph.menuLabels[1]))
                saveIt(EpicenterMagnitude.parentFrame);
        }

        public PopupHandler()
        {
        }
    }


    public TCCGraph(MapComponent mapcomponent, StringServer stringserver)
    {
        xRoom = 55;
        yRoom = 35;
        yImage = null;
        xLabel = "Distance (kilometers)";
        yLabel = "S-P Lag Time (seconds)";
        spIntervals = null;
        yMax = 80D;
        yMin = 0.0D;
        pointDragging = -1;
        xTics = 14;
        yImageHeight = 0;
        yImageWidth = 0;
        yTics = 7;
        xMax = 600L;
        xMin = 0L;
        axesFont = new Font("helvetica", 1, 9);
        crosshairsFont = new Font("helvetica", 1, 12);
        errorFont = new Font("helvetica", 1, 14);
        tableFont = new Font("helvetica", 0, 10);
        tableTitleFont = new Font("helvetica", 1, 11);
        bufferImage = null;
        points = null;
        hasPrivilege = false;
        showCurve = true;
        showHelpMessage = true;
        lineSlope = 4D;
        crossHairsX = 0x7fffffff;
        crossHairsY = 0x7fffffff;
        xMaxAt = 0;
        xMinAt = 0;
        mapComponent = mapcomponent;
        stringServer = stringserver;
        if(offFrame == null)
        {
            offFrame = new Frame();
            offFrame.setBackground(Color.white);
            offFrame.addNotify();
        }
        distanceFormat = NumberFormat.getInstance(Locale.US);
        distanceFormat.setMaximumFractionDigits(0);
        distanceFormat.setMinimumFractionDigits(0);
        spIntervalFormat = NumberFormat.getInstance(Locale.US);
        spIntervalFormat.setMaximumFractionDigits(1);
        spIntervalFormat.setMinimumFractionDigits(1);
        xLabel = stringserver.getString("graph_xlabel");
        yLabel = stringserver.getString("graph_ylabel");
        addMouseStuff();
        addPopUpMenu();
    }

    public void setMaxes(double d, double d1)
    {
        xMax = Math.round(d);
        yMax = d1;
        xMax = 700L;
        yMax = 70D;
        bufferImage = null;
        repaint();
    }

    public void setSPIntervals(double ad[])
    {
        if(spIntervals == null || spIntervals.length != ad.length)
            spIntervals = new double[ad.length];
        System.arraycopy(ad, 0, spIntervals, 0, ad.length);
    }

    public void setScaleAndTranslation(Rectangle rectangle)
    {
        double d2 = rectangle.width - xRoom - 10;
        double d3 = rectangle.height - yRoom - 20;
        double d = xMax - xMin;
        double d1 = yMax - yMin;
        xScale = d2 / d;
        yScale = d3 / d1;
        xTrans = (double)xRoom - (double)xMin * xScale;
        yTrans = (double)yRoom - yMin * yScale;
    }

    public void setShowHelpMessage(boolean flag)
    {
        showHelpMessage = flag;
    }

    public Image createRotatedImage(String s, FontMetrics fontmetrics, Font font)
    {
        if(System.getProperty("java.vendor").toLowerCase().startsWith("ibm") && System.getProperty("java.version").equals("1.1.7A"))
            return null;
        Graphics g = offFrame.getGraphics();
        if(g == null)
            return null;
        if(fontmetrics == null)
        {
            System.out.println("FontMetrics is null.");
            return null;
        }
        if(s == null || font == null)
            return null;
        int i = fontmetrics.stringWidth(s) + 2;
        int j = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + 2;
        Image image = createImage(i, j);
        if(image == null)
            return null;
        Graphics g1 = image.getGraphics();
        if(g1 == null)
            return null;
        g1.setColor(getBackground());
        g1.fillRect(0, 0, i, j);
        g1.setColor(Color.black);
        g1.setFont(font);
        g1.drawString(s, 1, fontmetrics.getMaxAscent() + 1);
        g1.dispose();
        g.drawImage(image, 0, 0, this);
        g.dispose();
        int ai[] = new int[i * j];
        PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, i, j, ai, 0, i);
        if(pixelgrabber == null)
            return null;
        try
        {
            pixelgrabber.grabPixels();
        }
        catch(InterruptedException interruptedexception) { }
        catch(Exception exception)
        {
            return null;
        }
        int ai1[] = new int[i * j];
        for(int k = 0; k < j; k++)
        {
            for(int l = 0; l < i; l++)
                ai1[l * j + k] = ai[k * i + (i - l - 1)];

        }

        MemoryImageSource memoryimagesource = new MemoryImageSource(j, i, ColorModel.getRGBdefault(), ai1, 0, j);
        if(memoryimagesource == null)
            return null;
        Image image1 = createImage(memoryimagesource);
        if(image1 == null)
            return null;
        MediaTracker mediatracker = new MediaTracker(this);
        if(mediatracker == null)
            return null;
        mediatracker.addImage(image1, 0);
        try
        {
            mediatracker.waitForAll();
        }
        catch(InterruptedException interruptedexception1) { }
        return image1;
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(bufferImage == null || bufferImage.getWidth(this) != i || bufferImage.getHeight(this) != j)
        {
            bufferImage = createImage(i, j);
            setScaleAndTranslation(getBounds());
        }
        if(bufferImage != null)
        {
            Graphics g1;
            if(g instanceof PrintGraphics)
                g1 = g;
            else
                g1 = bufferImage.getGraphics();
            if(g1 != null)
            {
                g1.setColor(getBackground());
                g1.fillRect(0, 0, i, j);
                paintAxes(g1, 0, 0, i, j);
                paintCurve(g1, i, j);
                paintCrosshairs(g1, i, j);
                if(showHelpMessage)
                    paintMessage(g1, i, j);
                if(!(g1 instanceof PrintGraphics))
                    g1.dispose();
            }
            if(!(g instanceof PrintGraphics))
                g.drawImage(bufferImage, 0, 0, this);
        }
    }

    public void print(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height + 25);
        Graphics g1 = g.create(0, 25, getSize().width, getSize().height + 25);
        paint(g1);
        g1.dispose();
        String s = mapComponent.getApplet().getJournal().getUserName();
        String s1 = mapComponent.getApplet().getJournal().getDate();
        g.setColor(Color.black);
        g.setFont(PRINT_TITLE_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        if(s != null && s.length() > 0)
            g.drawString(s, 5, 5 + fontmetrics.getAscent());
        if(s1 != null && s1.length() > 0)
            g.drawString(s1, getSize().width - 5 - fontmetrics.stringWidth(s1), 5 + fontmetrics.getAscent());
    }

    public void saveIt(Frame frame)
    {
        try
        {
            String s = "Save Graph As...";
            FileDialog filedialog = new FileDialog(EpicenterMagnitude.parentFrame, s, 1);
            filedialog.setFile("graph.gif");
            filedialog.show();
            if(System.getProperty("java.vendor").startsWith("Microsoft"))
                while(filedialog.isShowing()) 
                    try
                    {
                        Thread.sleep(250L);
                    }
                    catch(InterruptedException interruptedexception) { }
            String s1;
            if((s1 = filedialog.getFile()) != null)
            {
                System.out.println("Going to save: " + s1);
                String s2 = filedialog.getDirectory() + s1;
                if(s2.indexOf(".*.*") != -1)
                    s2 = s2.substring(0, s2.length() - 4);
                System.out.println("Using: " + s2);
                File file = new File(s2);
                saveIt(file);
            }
        }
        catch(Exception exception)
        {
            System.out.println("Got exception");
            System.out.println(exception);
            exception.printStackTrace();
        }
        catch(Error error)
        {
            System.out.println("Got error");
            System.out.println(error);
        }
    }

    public void saveIt(File file)
    {
        try
        {
            GifEncoder gifencoder = new GifEncoder(bufferImage, new FileOutputStream(file));
            gifencoder.encode();
            System.out.println("Saved");
        }
        catch(Exception exception)
        {
            System.out.println("Got exception");
            System.out.println(exception);
            exception.printStackTrace();
        }
        catch(Error error)
        {
            System.out.println("Got error");
            System.out.println(error);
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    protected double adjustSPInterval(double d)
    {
        double d1 = d;
        if(spIntervals != null)
        {
            double d2 = 1.7976931348623157E+308D;
            int i = -1;
            for(int j = 0; j < spIntervals.length; j++)
            {
                double d3 = Math.abs(spIntervals[j] - d);
                if(d3 < d2)
                {
                    d2 = d3;
                    i = j;
                }
            }

            if(d2 <= 0.5D && i >= 0)
                d1 = spIntervals[i];
        }
        return (double)(int)Math.round(d1 * 10D) / 10D;
    }

    protected void paintCenteredString(Graphics g, String s, FontMetrics fontmetrics, int i, int j, int k, int l)
    {
        paintCenteredString(g, s, fontmetrics, i, j, k, l, false);
    }

    protected void paintCenteredString(Graphics g, String s, FontMetrics fontmetrics, int i, int j, int k, int l, 
            boolean flag)
    {
        int i1 = i + (k - fontmetrics.stringWidth(s)) / 2;
        int j1 = j + (l + fontmetrics.getAscent()) / 2;
        g.drawString(s, i1, j1);
        if(flag)
        {
            for(int k1 = 1; k1 <= 2; k1++)
                g.drawLine(i1, j1 + k1, i1 + fontmetrics.stringWidth(s), j1 + k1);

        }
    }

    protected void processMouse(int i, int j)
    {
        crossHairsY = j;
        if(crossHairsY < 20)
            crossHairsY = 20;
        else
        if(crossHairsY >= getSize().height - yRoom)
            crossHairsY = getSize().height - xRoom;
        setShowHelpMessage(false);
        repaint();
    }

    private void addMouseStuff()
    {
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
                } else
                {
                    int i = mouseevent.getModifiers();
                    if((i & 8) == 0 && (i & 4) == 0)
                        processMouse(mouseevent.getX(), mouseevent.getY());
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
                        processMouse(mouseevent.getX(), mouseevent.getY());
                }
            }

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }

            public void mouseDragged(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
                } else
                {
                    int i = mouseevent.getModifiers();
                    if((i & 8) == 0 && (i & 4) == 0)
                        processMouse(mouseevent.getX(), mouseevent.getY());
                }
            }

        }
);
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
                } else
                {
                    int i = mouseevent.getModifiers();
                    if((i & 8) == 0 && (i & 4) == 0)
                        processMouse(mouseevent.getX(), mouseevent.getY());
                }
            }

            public void mouseMoved(MouseEvent mouseevent)
            {
            }

        }
);
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

    private void drawAxes(Graphics g, Rectangle rectangle)
    {
        double d = (double)(rectangle.width - 10 - xRoom) / (double)xTics;
        double d1 = (xMax - xMin) / (long)xTics;
        double d2 = (double)(rectangle.height - 20 - yRoom) / (double)yTics;
        double d3 = (yMax - yMin) / (double)yTics;
        xMinAt = (int)Math.round((double)xMin * xScale + xTrans);
        xMaxAt = (int)Math.round((double)xMax * xScale + xTrans);
        for(int i = 0; i <= xTics * 2; i++)
        {
            double d4 = d1 * ((double)i / 2D) + (double)xMin;
            int j1 = (int)Math.round(d4 * xScale + xTrans);
            if(i % 2 == 0)
            {
                g.setColor(Color.black);
                if(i > 0)
                    g.drawLine(j1, rectangle.height - yRoom, j1, (rectangle.height - yRoom) + 3);
                String s = EpicenterMagnitude.outputNumberString(distanceFormat.format(d4));
                g.setFont(labelFont);
                FontMetrics fontmetrics1 = g.getFontMetrics();
                g.drawString(s, j1 - g.getFontMetrics().stringWidth(s) / 2, (rectangle.height - yRoom) + 3 + fontmetrics1.getHeight());
            }
            if(i > 0)
            {
                g.setColor(gridColor);
                g.drawLine(j1, 20, j1, rectangle.height - yRoom);
            } else
            {
                g.setColor(Color.black);
                g.drawLine(j1, 20, j1, rectangle.height - yRoom);
                g.drawLine(j1 - 1, 20, j1 - 1, rectangle.height - yRoom);
            }
        }

        for(int j = 0; j <= yTics * 2; j++)
        {
            byte byte0 = 0;
            if(j == 0)
                byte0 = 3;
            int i1 = 0;
            double d5 = d3 * ((double)j / 2D) + yMin;
            i1 = rectangle.height - (int)Math.round(d5 * yScale + yTrans);
            if(j % 2 == 0)
            {
                g.setColor(Color.black);
                if(j > 0)
                    g.drawLine(xRoom - 3, i1, xRoom, i1);
                String s1 = Integer.toString((int)d5);
                g.setFont(labelFont);
                FontMetrics fontmetrics2 = g.getFontMetrics();
                g.drawString(s1, xRoom - 3 - fontmetrics2.stringWidth(s1) - 2, (i1 + fontmetrics2.getHeight() / 2) - byte0);
            }
            if(j > 0)
            {
                g.setColor(gridColor);
                g.drawLine(xRoom, i1, rectangle.width - 10, i1);
            } else
            {
                g.setColor(Color.black);
                g.drawLine(xRoom, i1, rectangle.width - 10, i1);
                g.drawLine(xRoom, i1 - 1, rectangle.width - 10, i1 - 1);
            }
        }

        g.setColor(Color.black);
        g.setFont(titleFont);
        FontMetrics fontmetrics = g.getFontMetrics();
        g.drawString(xLabel, (rectangle.width - xRoom - 10 - fontmetrics.stringWidth(xLabel)) / 2 + xRoom, rectangle.height - fontmetrics.getHeight() / 3);
        if(yImage == null)
        {
            yImage = createRotatedImage(yLabel, fontmetrics, titleFont);
            if(yImage != null)
            {
                yImageHeight = yImage.getHeight(this);
                yImageWidth = yImage.getWidth(this);
            }
        }
        if(yImage == null)
        {
            int l = (rectangle.height - yRoom - 20 - yLabel.length() * (fontmetrics.getHeight() + 2)) / 2 + 20;
            for(int k = 0; k < yLabel.length(); k++)
            {
                String s2 = yLabel.substring(k, k + 1);
                g.setFont(titleFont);
                g.drawString(s2, 7 - fontmetrics.stringWidth(s2) / 2, l + k * fontmetrics.getHeight());
            }

        } else
        {
            g.drawImage(yImage, 2, (rectangle.height - yRoom - 20 - yImageHeight) / 2 + 20, this);
        }
    }

    private void paintAxes(Graphics g, int i, int j, int k, int l)
    {
        Rectangle rectangle = new Rectangle(i, j, k, l);
        drawAxes(g, rectangle);
    }

    private void paintCrosshairs(Graphics g, int i, int j)
    {
        if(crossHairsY == 0x7fffffff)
            crossHairsY = (3 * j) / 5;
        double d = ((double)(getSize().height - crossHairsY) - yTrans) / yScale;
        d = adjustSPInterval(d);
        double d1 = (-0.1031D + Math.sqrt(0.010629609999999999D + -6.2570399999999998E-06D * d)) / -3.1285199999999999E-06D;
        int k = (int)Math.round(d1 * xScale + xTrans);
        int l = j - (int)Math.round(d * yScale + yTrans);
        g.setColor(crosshairsColor);
        g.setFont(crosshairsFont);
        FontMetrics fontmetrics = g.getFontMetrics();
        if(d1 >= 0.0D && d1 <= (double)xMax)
        {
            g.drawLine(xRoom, l, k, l);
            String s = EpicenterMagnitude.outputNumberString(spIntervalFormat.format(d)) + " " + stringServer.getString("sec");
            if(l - fontmetrics.getMaxAscent() - fontmetrics.getMaxDescent() - 2 < 20)
                g.drawString(s, xRoom + 2, l + fontmetrics.getMaxAscent());
            else
                g.drawString(s, xRoom + 2, l - fontmetrics.getMaxDescent() - 2);
            int i1 = fontmetrics.stringWidth(s);
            g.drawLine(k, j - yRoom, k, l);
            s = EpicenterMagnitude.outputNumberString(distanceFormat.format(d1)) + " km";
            if(k < xRoom + 4 + i1)
            {
                g.setColor(getBackground());
                g.fillRect(k, (j - yRoom) + 2, fontmetrics.stringWidth(s) + 4, fontmetrics.getHeight());
                g.setColor(crosshairsColor);
                g.drawString(s, k + 2, (j - yRoom) + 2 + fontmetrics.getMaxAscent());
            } else
            if(k + 2 + fontmetrics.stringWidth(s) < i)
                g.drawString(s, k + 2, j - yRoom - fontmetrics.getMaxDescent());
            else
                g.drawString(s, k - 2 - fontmetrics.stringWidth(s), j - yRoom - fontmetrics.getMaxDescent());
        } else
        {
            g.drawLine(xRoom, l, k, crossHairsY);
            String s1 = EpicenterMagnitude.outputNumberString(spIntervalFormat.format(d)) + " " + stringServer.getString("sec");
            if(l - fontmetrics.getMaxAscent() - fontmetrics.getMaxDescent() < 1)
                g.drawString(s1, xRoom + 2, l + fontmetrics.getMaxAscent());
            else
                g.drawString(s1, xRoom + 2, l - fontmetrics.getMaxDescent());
        }
        crossHairsX = k;
    }

    private void paintCurve(Graphics g, int i, int j)
    {
        java.awt.Shape shape = g.getClip();
        g.setClip(xRoom, 20, i - 10 - xRoom, j - yRoom - 20);
        int k = (int)Math.round((double)xMax * xScale + xTrans);
        g.setColor(curveColor);
        int l = 0x7fffffff;
        int i1 = 0x7fffffff;
        for(int j1 = xRoom; j1 < k; j1++)
        {
            double d = ((double)j1 - xTrans) / xScale;
            double d1 = 0.23200000000000001D * d - 3.0109799999999999E-06D * d * d - (0.12889999999999999D * d - 1.44672E-06D * d * d);
            int k1 = j - (int)Math.round(d1 * yScale + yTrans);
            if(l != 0x7fffffff)
                g.drawLine(l, i1, j1, k1);
            l = j1;
            i1 = k1;
        }

        g.setClip(shape);
    }

    private void paintMessage(Graphics g, int i, int j)
    {
        g.setColor(MESSAGE_COLOR);
        g.setFont(MESSAGE_FONT);
        FontMetrics fontmetrics = g.getFontMetrics();
        try
        {
            int k = Integer.parseInt(stringServer.getString("distance_message_num_lines"));
            String as[] = new String[k];
            int ai[] = new int[k];
            int l = 0;
            for(int i1 = 0; i1 < k; i1++)
            {
                as[i1] = stringServer.getString("distance_message_line_" + i1);
                ai[i1] = fontmetrics.stringWidth(as[i1]);
                l = Math.max(l, ai[i1]);
            }

            int j1 = xRoom + 4;
            int k1 = 20 + fontmetrics.getHeight();
            for(int l1 = 0; l1 < k; l1++)
            {
                int i2 = k1 + fontmetrics.getAscent() * l1;
                g.drawString(as[l1], j1, i2);
            }

            g.drawLine(j1 + l / 2, k1 + fontmetrics.getAscent() * (k - 1), crossHairsX, crossHairsY);
        }
        catch(NumberFormatException numberformatexception)
        {
            System.err.println("Could not read num of lines in need to fill in more data message.");
        }
    }

    public static final Color MESSAGE_COLOR;
    public static final Font MESSAGE_FONT = new Font("Helvetica", 1, 13);
    public static final Font PRINT_TITLE_FONT = new Font("Helvetica", 1, 14);
    public static final Color crosshairsColor;
    public static final Color curveColor;
    public static final Color gridColor;
    public static final Font labelFont = new Font("helvetica", 0, 9);
    public static final String menuLabels[] = {
        "Print", "Save"
    };
    public static Frame offFrame = null;
    public static final Font titleFont = new Font("helvetica", 1, 11);
    public static final int xEndRoom = 10;
    public static final int xTicSize = 3;
    public static final int yEndRoom = 20;
    public static final int yTicSize = 3;
    public int xRoom;
    public int yRoom;
    protected Image yImage;
    protected String xLabel;
    protected String yLabel;
    protected double spIntervals[];
    protected double xScale;
    protected double xTrans;
    protected double yMax;
    protected double yMin;
    protected double yScale;
    protected double yTrans;
    protected int pointDragging;
    protected int xTics;
    protected int yImageHeight;
    protected int yImageWidth;
    protected int yTics;
    protected long xMax;
    protected long xMin;
    private Font axesFont;
    private Font crosshairsFont;
    private Font errorFont;
    private Font tableFont;
    private Font tableTitleFont;
    private Image bufferImage;
    private MapComponent mapComponent;
    private NumberFormat distanceFormat;
    private NumberFormat spIntervalFormat;
    private PopupMenu popup;
    private StringServer stringServer;
    private PointData points[];
    private boolean hasPrivilege;
    private boolean showCurve;
    private boolean showHelpMessage;
    private double lineSlope;
    private int crossHairsX;
    private int crossHairsY;
    private int xMaxAt;
    private int xMinAt;

    static 
    {
        MESSAGE_COLOR = Color.blue;
        crosshairsColor = Color.red;
        curveColor = Color.blue;
        gridColor = Color.lightGray;
    }


}
