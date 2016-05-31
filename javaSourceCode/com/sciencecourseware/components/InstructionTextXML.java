// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InstructionTextXML.java

package com.sciencecourseware.components;

import com.sciencecourseware.earthquake.epicentermagnitude.EpicenterMagnitude;
import electric1.util.Nodes;
import electric1.xml.*;
import java.awt.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.*;

// Referenced classes of package com.sciencecourseware.components:
//            InstructionText, ToolTipCanvas, Printer, InstructionTextAction, 
//            ToolTipComponent, ImageServer, ComponentToolTipListener

public class InstructionTextXML extends InstructionText
{
    public class ToolTipHandler
        implements ComponentToolTipListener
    {

        public void hideComponentToolTip(ToolTipComponent tooltipcomponent)
        {
            System.out.println("In InstructionTextXML.hideComponentToolTip");
            getToolTipCanvas().setVisible(false);
        }

        public void showComponentToolTip(ToolTipComponent tooltipcomponent, Point point)
        {
            showComponentToolTip(tooltipcomponent, point.x, point.y);
        }

        public void showComponentToolTip(ToolTipComponent tooltipcomponent, int i, int j)
        {
            System.out.println("In InstructionTextXML.showComponentToolTip");
            if(tooltipcomponent.getToolTipText() != null)
            {
                System.out.println("The tool tip text is \"" + tooltipcomponent.getToolTipText() + "\"");
                getToolTipCanvas().setLabel(tooltipcomponent.getToolTipText());
                int k = i + 4;
                if(k + getToolTipCanvas().getSize().width > tooltipcomponent.getSize().width)
                    k = tooltipcomponent.getSize().width - getToolTipCanvas().getSize().width;
                if(k < 0)
                    k = 0;
                int l = j - 4 - getToolTipCanvas().getSize().height;
                if(l + getToolTipCanvas().getSize().height > tooltipcomponent.getSize().height)
                    l = tooltipcomponent.getSize().height - getToolTipCanvas().getSize().height;
                if(l < 0)
                    l = 0;
                getToolTipCanvas().setLocation(k, l);
                getToolTipCanvas().setVisible(true);
            } else
            {
                System.out.println("The tool tip text is null");
                getToolTipCanvas().setVisible(false);
            }
        }

        public ToolTipHandler()
        {
        }
    }

    public class TextPaintingInfo
    {

        public FontMetrics fm;
        public int lineHeight;
        public String lineSeparator;
        public int lineWidth;
        public int x;
        public int x0;
        public int y;
        public int y0;

        public TextPaintingInfo()
        {
            fm = null;
            lineHeight = 0;
            lineSeparator = System.getProperty("line.separator");
            lineWidth = 0;
            x = 0;
            x0 = 0;
            y = 0;
            y0 = 0;
        }
    }


    public InstructionTextXML(Frame frame)
    {
        super(frame);
        boldPrinterFont = new Font("Courier", 1, 12);
        headerColor = null;
        headerFont = null;
        normalPrinterFont = new Font("Courier", 0, 12);
        rand = null;
        randomNumbers = null;
        root = null;
        textElement = null;
        toolTipFont = new Font("sansserif", 0, 10);
        doingToolTips = false;
        imageServer = null;
        toolTipCanvas = null;
        toolTipHandler = null;
    }

    public void setDoingToolTips(boolean flag)
    {
        doingToolTips = flag;
        if(toolTipHandler == null)
        {
            toolTipHandler = new ToolTipHandler();
            setToolTipListener(toolTipHandler);
        }
    }

    public boolean isDoingToolTips()
    {
        return doingToolTips;
    }

    public void setHeaderColor(Color color)
    {
        headerColor = color;
        repaint();
    }

    public Color getHeaderColor()
    {
        return headerColor;
    }

    public void setImageServer(ImageServer imageserver)
    {
        imageServer = imageserver;
    }

    public double getRandomNumber(String s)
    {
        if(randomNumbers == null)
            randomNumbers = new Hashtable();
        Double double1 = (Double)randomNumbers.get(s);
        if(double1 == null)
            return 1.7976931348623157E+308D;
        else
            return double1.doubleValue();
    }

    public void setRoot(Element element)
    {
        root = element;
        if(root.getName().equals("text") || root.getName().equals("content"))
        {
            textElement = root;
        } else
        {
            textElement = root.getElement("text");
            getHeightFromXML(root);
        }
        super.reparseText = true;
        repaint();
    }

    public void setText(String s)
    {
        try
        {
            Document document = new Document(s);
            root = document.getRoot();
            getHeightFromXML(root);
            textElement = root.getElement("text");
        }
        catch(ParseException parseexception)
        {
            System.err.println(parseexception);
        }
        super.setText(s);
    }

    public ToolTipCanvas getToolTipCanvas()
    {
        if(toolTipCanvas == null)
        {
            toolTipCanvas = new ToolTipCanvas();
            toolTipCanvas.setBounds(0, 0, 20, 10);
            toolTipCanvas.setBackground(Color.yellow);
            toolTipCanvas.setForeground(Color.blue);
            toolTipCanvas.setFont(toolTipFont);
            add(toolTipCanvas);
            toolTipCanvas.setVisible(false);
        }
        return toolTipCanvas;
    }

    public void printChild(Printer printer, Child child, StringBuffer stringbuffer, int i)
    {
        System.out.println("In printChild");
        if(child instanceof Element)
        {
            System.out.println("Have an element");
            Element element = (Element)child;
            if(element.getName().equals("glossary"))
            {
                System.out.println("Have a glossary entry");
                printGlossaryAction(printer, element, stringbuffer);
            } else
            if(element.getName().equals("p"))
            {
                Attribute attribute = element.getAttribute("indentlevel");
                if(attribute != null)
                    try
                    {
                        int j = Integer.parseInt(attribute.getValue());
                        i = j;
                    }
                    catch(NumberFormatException numberformatexception) { }
                System.out.println("Have a paragraph");
                Child child1;
                for(Children children = element.getChildren(); children.hasMoreElements(); printChild(printer, child1, stringbuffer, i))
                    child1 = children.next();

                if(stringbuffer.length() > 0)
                {
                    printer.println("     " + stringbuffer.toString());
                    stringbuffer.setLength(0);
                }
                Attribute attribute2 = element.getAttribute("extraspacing");
                if(attribute2 != null)
                    try
                    {
                        int i1 = Integer.parseInt(attribute2.getValue());
                        if(i1 > 0)
                            printer.println();
                    }
                    catch(NumberFormatException numberformatexception2)
                    {
                        printer.println();
                    }
                else
                    printer.println();
            } else
            if(element.getName().equals("h"))
            {
                System.out.println("Have a heading");
                Attribute attribute1 = element.getAttribute("indentlevel");
                if(attribute1 != null)
                    try
                    {
                        int k = Integer.parseInt(attribute1.getValue());
                        i += k;
                    }
                    catch(NumberFormatException numberformatexception1) { }
                if(stringbuffer.length() > 0)
                {
                    printer.println("     " + stringbuffer.toString());
                    stringbuffer.setLength(0);
                }
                int l = i / 10;
                for(int j1 = 0; j1 < l; j1++)
                    stringbuffer.append(" ");

                printer.println();
                printer.setFont(boldPrinterFont);
                printer.println("     " + element.getTextString());
                printer.setFont(normalPrinterFont);
                printer.println();
            }
        } else
        if(child instanceof Text)
        {
            System.out.println("Have text");
            printTextNode(printer, (Text)child, stringbuffer, i);
        }
    }

    public void printIt(String s, Frame frame)
    {
        System.out.println("In InstructionTextXML.printIt()");
        Printer printer = new Printer(frame, s);
        System.out.println("Created Printer object");
        printer.setLinesPerPage(42);
        System.out.println("Set printer lines per page");
        printer.setFont(normalPrinterFont);
        System.out.println("Set printer font");
        StringBuffer stringbuffer = new StringBuffer(100);
        Child child;
        for(Children children = textElement.getChildren(); children.hasMoreElements(); printChild(printer, child, stringbuffer, 0))
        {
            System.out.println("In children loop");
            child = children.next();
        }

        System.out.println("Out of children loop");
        printer.newPage();
        printer.endJob();
        System.out.println("Done with printIt()");
    }

    public void writeChild(PrintWriter printwriter, Child child, StringBuffer stringbuffer)
    {
        if(child instanceof Element)
        {
            Element element = (Element)child;
            if(element.getName().equals("glossary"))
                writeGlossaryAction(printwriter, element, stringbuffer);
            else
            if(element.getName().equals("p"))
            {
                Child child1;
                for(Children children = element.getChildren(); children.hasMoreElements(); writeChild(printwriter, child1, stringbuffer))
                    child1 = children.next();

                if(stringbuffer.length() > 0)
                {
                    printwriter.println(stringbuffer.toString());
                    stringbuffer.setLength(0);
                }
                Attribute attribute = element.getAttribute("extraspacing");
                if(attribute != null)
                    try
                    {
                        int i = Integer.parseInt(attribute.getValue());
                        if(i > 0)
                            printwriter.println();
                    }
                    catch(NumberFormatException numberformatexception)
                    {
                        printwriter.println();
                    }
                else
                    printwriter.println();
            } else
            if(element.getName().equals("h"))
            {
                if(stringbuffer.length() > 0)
                {
                    printwriter.println(stringbuffer.toString());
                    stringbuffer.setLength(0);
                }
                printwriter.println();
                printwriter.println(element.getTextString());
                StringBuffer stringbuffer1 = new StringBuffer(element.getTextString().length() + 2);
                for(int j = 0; j < element.getTextString().length(); j++)
                    stringbuffer1.append("-");

                printwriter.println(stringbuffer1.toString());
                printwriter.println();
            }
        } else
        if(child instanceof Text)
            writeTextNode(printwriter, (Text)child, stringbuffer);
    }

    protected void getHeightFromXML(Element element)
    {
        Element element1 = element.getElement("height");
        if(element1 != null)
        {
            String s = element1.getTextString();
            if(s != null)
                try
                {
                    int i = Integer.parseInt(s);
                    Dimension dimension = getPreferredSize();
                    dimension.height = i + getMyInsets().top + getMyInsets().bottom;
                }
                catch(NumberFormatException numberformatexception)
                {
                    System.err.println("Couldn't read height from " + s);
                }
        }
    }

    protected void paintChild(Graphics g, Child child, TextPaintingInfo textpaintinginfo, int i)
    {
        if(child instanceof Element)
        {
            Element element = (Element)child;
            if(element.getName().equals("glossary"))
                paintGlossaryAction(g, element, textpaintinginfo);
            else
            if(element.getName().equals("img"))
                paintImage(g, element, textpaintinginfo, i);
            else
            if(element.getName().equals("randomnumber"))
                paintRandomNumber(g, element, textpaintinginfo, i);
            else
            if(element.getName().equals("p"))
            {
                Attribute attribute = element.getAttribute("indentlevel");
                if(attribute != null)
                    try
                    {
                        int j = Integer.parseInt(attribute.getValue());
                        i = j;
                    }
                    catch(NumberFormatException numberformatexception) { }
                Attribute attribute2 = element.getAttribute("number");
                if(attribute2 != null)
                {
                    if(textpaintinginfo.x == textpaintinginfo.x0)
                        textpaintinginfo.x = i;
                    g.drawString(attribute2.getValue(), textpaintinginfo.x, textpaintinginfo.y);
                    textpaintinginfo.x += 20;
                    i += 20;
                }
                Child child1;
                for(Children children = element.getChildren(); children.hasMoreElements(); paintChild(g, child1, textpaintinginfo, i))
                    child1 = children.next();

                textpaintinginfo.x = textpaintinginfo.x0;
                Attribute attribute3 = element.getAttribute("extraspacing");
                if(attribute3 != null)
                    try
                    {
                        int l = Integer.parseInt(attribute3.getValue());
                        textpaintinginfo.y += textpaintinginfo.lineHeight + l;
                    }
                    catch(NumberFormatException numberformatexception2)
                    {
                        textpaintinginfo.y += textpaintinginfo.lineHeight + 6;
                    }
                else
                    textpaintinginfo.y += textpaintinginfo.lineHeight + 6;
            } else
            if(element.getName().equals("h"))
            {
                Attribute attribute1 = element.getAttribute("indentlevel");
                if(attribute1 != null)
                    try
                    {
                        int k = Integer.parseInt(attribute1.getValue());
                        i += k;
                    }
                    catch(NumberFormatException numberformatexception1) { }
                if(textpaintinginfo.x > textpaintinginfo.x0)
                {
                    textpaintinginfo.x = textpaintinginfo.x0 + i;
                    textpaintinginfo.y += textpaintinginfo.lineHeight;
                }
                textpaintinginfo.y += textpaintinginfo.lineHeight / 2;
                Font font = getFont();
                if(headerFont == null)
                    headerFont = new Font(font.getName(), 1, font.getSize());
                g.setFont(headerFont);
                Color color = g.getColor();
                if(getHeaderColor() != null)
                    g.setColor(getHeaderColor());
                g.drawString(element.getTextString(), textpaintinginfo.x, textpaintinginfo.y);
                if(getHeaderColor() != null)
                    g.setColor(color);
                g.setFont(font);
                textpaintinginfo.x = textpaintinginfo.x0;
                Attribute attribute4 = element.getAttribute("extraspacing");
                if(attribute4 != null)
                    try
                    {
                        int i1 = Integer.parseInt(attribute4.getValue());
                        textpaintinginfo.y += i1;
                    }
                    catch(NumberFormatException numberformatexception3)
                    {
                        textpaintinginfo.y += (textpaintinginfo.lineHeight / 4) * 5;
                    }
                else
                    textpaintinginfo.y += (textpaintinginfo.lineHeight / 4) * 5;
            }
        } else
        if(child instanceof Text)
            paintTextNode(g, (Text)child, textpaintinginfo, i);
    }

    protected void paintGlossaryAction(Graphics g, Element element, TextPaintingInfo textpaintinginfo)
    {
        String s = element.getTextString();
        if(textpaintinginfo.fm.stringWidth(s + " ") + textpaintinginfo.x > textpaintinginfo.lineWidth)
        {
            textpaintinginfo.x = textpaintinginfo.x0;
            textpaintinginfo.y += textpaintinginfo.lineHeight;
        }
        g.setColor(getLinkColor());
        g.drawString(s, textpaintinginfo.x, textpaintinginfo.y);
        if(isUnderlineLinks())
            g.drawLine(textpaintinginfo.x, textpaintinginfo.y + 2, textpaintinginfo.x + textpaintinginfo.fm.stringWidth(s), textpaintinginfo.y + 2);
        InstructionTextAction instructiontextaction = new InstructionTextAction();
        instructiontextaction.setRectangle(new Rectangle(textpaintinginfo.x, textpaintinginfo.y - textpaintinginfo.fm.getMaxAscent(), textpaintinginfo.fm.stringWidth(s), textpaintinginfo.lineHeight + 1));
        instructiontextaction.setActionCommand("glossary;" + element.getAttributeValue("term"));
        instructiontextaction.setToolTipText("Click for definition");
        super.actions.addElement(instructiontextaction);
        textpaintinginfo.x += textpaintinginfo.fm.stringWidth(s + " ");
        if(textpaintinginfo.x > textpaintinginfo.lineWidth)
        {
            textpaintinginfo.x = textpaintinginfo.x0;
            textpaintinginfo.y += textpaintinginfo.lineHeight;
        }
    }

    protected void paintImage(Graphics g, Element element, TextPaintingInfo textpaintinginfo, int i)
    {
        if(imageServer != null)
        {
            String s = element.getAttributeValue("name");
            if(s != null)
            {
                Image image = imageServer.getImage(s);
                int j = image.getWidth(this);
                Attribute attribute = element.getAttribute("yadjust");
                int k = 0;
                if(attribute != null)
                    try
                    {
                        k = Integer.parseInt(attribute.getValue());
                    }
                    catch(NumberFormatException numberformatexception)
                    {
                        k = 0;
                    }
                Attribute attribute1 = element.getAttribute("xadjust");
                int l = 0;
                if(attribute1 != null)
                    try
                    {
                        l = Integer.parseInt(attribute1.getValue());
                    }
                    catch(NumberFormatException numberformatexception1)
                    {
                        l = 0;
                    }
                if(textpaintinginfo.x + j + l > textpaintinginfo.lineWidth)
                {
                    textpaintinginfo.y += textpaintinginfo.lineHeight;
                    textpaintinginfo.x = textpaintinginfo.x0 + i;
                }
                g.drawImage(image, textpaintinginfo.x, textpaintinginfo.y - g.getFontMetrics().getAscent() - k, this);
                textpaintinginfo.x += j + l;
            }
        }
    }

    protected void paintRandomNumber(Graphics g, Element element, TextPaintingInfo textpaintinginfo, int i)
    {
        double d = 0.0D;
        double d1 = 100D;
        int j = 0;
        Attribute attribute = element.getAttribute("name");
        if(attribute != null)
        {
            String s = attribute.getValue();
            double d2 = getRandomNumber(s);
            if(d2 == 1.7976931348623157E+308D)
            {
                attribute = element.getAttribute("lowerbound");
                try
                {
                    if(attribute != null)
                    {
                        Double double1 = Double.valueOf(attribute.getValue());
                        if(double1 != null)
                            d = double1.doubleValue();
                    }
                }
                catch(NumberFormatException numberformatexception)
                {
                    d = 100D;
                }
                attribute = element.getAttribute("upperbound");
                if(attribute != null)
                    try
                    {
                        Double double2 = Double.valueOf(attribute.getValue());
                        if(double2 != null)
                            d1 = double2.doubleValue();
                    }
                    catch(NumberFormatException numberformatexception1)
                    {
                        d1 = 100D;
                    }
                if(rand == null)
                    rand = new Random();
                d2 = rand.nextDouble() * (d1 - d) + d;
                randomNumbers.put(s, new Double(d2));
            }
            attribute = element.getAttribute("numplaces");
            if(attribute != null)
                try
                {
                    j = Integer.parseInt(attribute.getValue());
                }
                catch(NumberFormatException numberformatexception2) { }
            NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
            numberformat.setMinimumFractionDigits(j);
            numberformat.setMaximumFractionDigits(j);
            String s1 = EpicenterMagnitude.outputNumberString(numberformat.format(d2));
            int k = textpaintinginfo.fm.stringWidth(s1);
            if(textpaintinginfo.x + k > textpaintinginfo.lineWidth)
            {
                textpaintinginfo.x = textpaintinginfo.x0 + i;
                textpaintinginfo.y += textpaintinginfo.lineHeight;
            }
            g.drawString(s1, textpaintinginfo.x, textpaintinginfo.y);
            textpaintinginfo.x += k;
        }
    }

    protected void paintText(Graphics g, int i, int j, int k, int l)
    {
        super.rawText = new StringBuffer(getText().length());
        if(super.actions == null)
            super.actions = new Vector();
        else
            super.actions.removeAllElements();
        FontMetrics fontmetrics = null;
        if(g != null)
        {
            g.setFont(getFont());
            fontmetrics = g.getFontMetrics();
        } else
        if(super.storedFont != null)
            fontmetrics = Toolkit.getDefaultToolkit().getFontMetrics(super.storedFont);
        if(fontmetrics != null)
        {
            TextPaintingInfo textpaintinginfo = new TextPaintingInfo();
            textpaintinginfo.x = i;
            textpaintinginfo.y = j + fontmetrics.getAscent() + 1;
            if(super.isClassicMacOS)
                textpaintinginfo.y -= 2;
            if(super.isClassicMacOS)
                textpaintinginfo.lineHeight = fontmetrics.getHeight() + 1;
            else
                textpaintinginfo.lineHeight = fontmetrics.getAscent() + 1;
            textpaintinginfo.lineWidth = k;
            textpaintinginfo.x0 = i;
            textpaintinginfo.y0 = j;
            textpaintinginfo.fm = fontmetrics;
            if(textElement != null)
            {
                Child child;
                for(Children children = textElement.getChildren(); children != null && children.hasMoreElements(); paintChild(g, child, textpaintinginfo, 0))
                    child = children.next();

            }
        }
    }

    protected void paintTextNode(Graphics g, Text text, TextPaintingInfo textpaintinginfo, int i)
    {
        if(textpaintinginfo.x == textpaintinginfo.x0)
            textpaintinginfo.x += i;
        StringBuffer stringbuffer = new StringBuffer(100);
        for(StringTokenizer stringtokenizer = new StringTokenizer(text.getString(), " "); stringtokenizer.hasMoreTokens();)
        {
            String s = stringtokenizer.nextToken();
            if(textpaintinginfo.fm.stringWidth(stringbuffer.toString() + " " + s) + textpaintinginfo.x > textpaintinginfo.lineWidth)
            {
                g.setColor(getForeground());
                g.drawString(stringbuffer.toString(), textpaintinginfo.x, textpaintinginfo.y);
                stringbuffer.setLength(0);
                stringbuffer.append(s);
                textpaintinginfo.x = textpaintinginfo.x0 + i;
                textpaintinginfo.y += textpaintinginfo.lineHeight;
            } else
            {
                if(stringbuffer.length() > 0)
                    stringbuffer.append(" ");
                stringbuffer.append(s);
            }
        }

        if(stringbuffer.length() > 0)
        {
            g.setColor(getForeground());
            g.drawString(stringbuffer.toString(), textpaintinginfo.x, textpaintinginfo.y);
            textpaintinginfo.x += textpaintinginfo.fm.stringWidth(stringbuffer.toString() + " ");
        }
    }

    protected void printGlossaryAction(Printer printer, Element element, StringBuffer stringbuffer)
    {
        String s = element.getTextString();
        if(stringbuffer.length() + s.length() + 1 > 56)
        {
            printer.println("     " + stringbuffer.toString());
            stringbuffer.setLength(0);
        } else
        {
            if(stringbuffer.length() > 0)
                stringbuffer.append(" ");
            stringbuffer.append(s);
        }
    }

    protected void printTextNode(Printer printer, Text text, StringBuffer stringbuffer, int i)
    {
        for(StringTokenizer stringtokenizer = new StringTokenizer(text.getString(), " "); stringtokenizer.hasMoreTokens();)
        {
            String s = stringtokenizer.nextToken();
            if(stringbuffer.length() + s.length() + 1 > 56)
            {
                printer.println("     " + stringbuffer.toString());
                stringbuffer.setLength(0);
                int j = i / 10;
                for(int k = 0; k < j; k++)
                    stringbuffer.append(" ");

                stringbuffer.append(s);
            } else
            {
                if(stringbuffer.length() > 0)
                    stringbuffer.append(" ");
                stringbuffer.append(s);
            }
        }

    }

    protected void writeGlossaryAction(PrintWriter printwriter, Element element, StringBuffer stringbuffer)
    {
        String s = element.getTextString();
        if(stringbuffer.length() + s.length() + 1 > 56)
        {
            printwriter.println(stringbuffer.toString());
            stringbuffer.setLength(0);
        } else
        {
            if(stringbuffer.length() > 0)
                stringbuffer.append(" ");
            stringbuffer.append(s);
        }
    }

    protected void writeTextNode(PrintWriter printwriter, Text text, StringBuffer stringbuffer)
    {
        for(StringTokenizer stringtokenizer = new StringTokenizer(text.getString(), " "); stringtokenizer.hasMoreTokens();)
        {
            String s = stringtokenizer.nextToken();
            if(stringbuffer.length() + s.length() + 1 > 56)
            {
                printwriter.println(stringbuffer.toString());
                stringbuffer.setLength(0);
                stringbuffer.append(s);
            } else
            {
                if(stringbuffer.length() > 0)
                    stringbuffer.append(" ");
                stringbuffer.append(s);
            }
        }

    }

    protected void writeToStream(PrintWriter printwriter)
    {
        StringBuffer stringbuffer = new StringBuffer(100);
        Child child;
        for(Children children = textElement.getChildren(); children.hasMoreElements(); writeChild(printwriter, child, stringbuffer))
            child = children.next();

    }

    public static final int charsPerLine = 56;
    public static final int extraParagraphSpacing = 6;
    public static final int linesPerPage = 42;
    protected Font boldPrinterFont;
    protected Color headerColor;
    protected Font headerFont;
    protected Font normalPrinterFont;
    protected Random rand;
    protected Hashtable randomNumbers;
    protected Element root;
    protected Element textElement;
    protected Font toolTipFont;
    private boolean doingToolTips;
    private ImageServer imageServer;
    private ToolTipCanvas toolTipCanvas;
    private ToolTipHandler toolTipHandler;
}
