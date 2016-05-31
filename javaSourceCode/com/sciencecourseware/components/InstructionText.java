// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InstructionText.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

// Referenced classes of package com.sciencecourseware.components:
//            ToolTipComponent, InstructionTextAction, Printer, ComponentToolTipListener

public class InstructionText extends ToolTipComponent
{
    class IvjEventHandler
        implements MouseListener, MouseMotionListener
    {

        public void mouseClicked(MouseEvent mouseevent)
        {
        }

        public void mouseDragged(MouseEvent mouseevent)
        {
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
        }

        public void mouseExited(MouseEvent mouseevent)
        {
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
            if(mouseevent.getSource() == InstructionText.this)
                connEtoC2(mouseevent);
        }

        public void mousePressed(MouseEvent mouseevent)
        {
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(mouseevent.getSource() == InstructionText.this)
                connEtoC1(mouseevent);
        }

        IvjEventHandler()
        {
        }
    }


    public InstructionText(Frame frame)
    {
        aActionListener = null;
        actionStrings = null;
        actions = null;
        backgroundImage = null;
        doOffset = false;
        fieldLinkColor = new Color(0);
        fieldText = new String();
        glossaryListener = null;
        heightToFit = false;
        isClassicMacOS = false;
        lastComputedHeight = 0x7fffffff;
        pSize = null;
        reparseText = true;
        storedFont = null;
        titleBackgroundColor = null;
        titleForegroundColor = null;
        titleLines = null;
        hasPrivilege = false;
        ivjEventHandler = new IvjEventHandler();
        numValidations = 0;
        parentFrame = null;
        currentToolTipText = null;
        insets = new Insets(0, 0, 0, 0);
        toolTipITA = null;
        toolTipShowing = false;
        underlineLinks = true;
        checkOS();
        parentFrame = frame;
        if(lineSeparator == null)
            lineSeparator = "@";
        initialize();
    }

    public void setBackgroundImage(Image image)
    {
        backgroundImage = image;
        repaint();
    }

    public void setDoOffset(boolean flag)
    {
        doOffset = flag;
    }

    public void setFont(Font font)
    {
        super.setFont(font);
        storedFont = font;
        if(heightToFit && reparseText && pSize != null)
            paintText(null, 0, 0, pSize.width, pSize.height);
        redraw();
    }

    public void setGlossaryListener(ActionListener actionlistener)
    {
        glossaryListener = actionlistener;
        setReparseText(true);
        repaint();
    }

    public ActionListener getGlossaryListener()
    {
        return glossaryListener;
    }

    public void setHeightToFit(boolean flag)
    {
        heightToFit = flag;
        if(pSize != null)
            paintText(null, 0, 0, pSize.width, pSize.height);
        reparseText = true;
    }

    public void setLinkColor(Color color)
    {
        fieldLinkColor = color;
    }

    public Color getLinkColor()
    {
        return fieldLinkColor;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public void setMyInsets(Insets insets1)
    {
        insets = insets1;
        setReparseText(true);
    }

    public Insets getMyInsets()
    {
        return insets;
    }

    public void setPreferredSize(Dimension dimension)
    {
        pSize = dimension;
        invalidate();
    }

    public Dimension getPreferredSize()
    {
        if(pSize != null)
            return pSize;
        if(backgroundImage != null)
            return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
        else
            return super.getPreferredSize();
    }

    public void setReparseText(boolean flag)
    {
        reparseText = flag;
    }

    public void setText(String s)
    {
        String s1 = fieldText;
        fieldText = s;
        firePropertyChange("text", s1, s);
        setReparseText(true);
        repaint();
    }

    public String getText()
    {
        return fieldText;
    }

    public void setTitleBackgroundColor(Color color)
    {
        titleBackgroundColor = color;
    }

    public Color getTitleBackgroundColor()
    {
        return titleBackgroundColor;
    }

    public void setTitleForegroundColor(Color color)
    {
        titleForegroundColor = color;
    }

    public Color getTitleForegroundColor()
    {
        if(titleForegroundColor == null)
            return Color.black;
        else
            return titleForegroundColor;
    }

    public void setTitleLines(String as[])
    {
        titleLines = as;
    }

    public void setToolTipShowing(boolean flag)
    {
        toolTipShowing = flag;
    }

    public boolean isToolTipShowing()
    {
        return toolTipShowing;
    }

    public void setToolTipText(String s)
    {
        currentToolTipText = s;
    }

    public String getToolTipText()
    {
        return currentToolTipText;
    }

    public void setUnderlineLinks(boolean flag)
    {
        underlineLinks = flag;
        repaint();
    }

    public boolean isUnderlineLinks()
    {
        return underlineLinks;
    }

    public void addActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.add(aActionListener, actionlistener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().addPropertyChangeListener(propertychangelistener);
    }

    public void firePropertyChange(String s, Object obj, Object obj1)
    {
        getPropertyChange().firePropertyChange(s, obj, obj1);
    }

    public void instructionText_MouseMoved(MouseEvent mouseevent)
    {
        java.awt.Point point = mouseevent.getPoint();
        if(actions != null)
        {
            boolean flag = false;
            Enumeration enumeration = actions.elements();
            do
            {
                if(!enumeration.hasMoreElements())
                    break;
                InstructionTextAction instructiontextaction = (InstructionTextAction)enumeration.nextElement();
                if(!instructiontextaction.contains(point))
                    continue;
                flag = true;
                if(getCursor() != Cursor.getPredefinedCursor(12))
                    setCursor(Cursor.getPredefinedCursor(12));
                if(getToolTipListener() != null && instructiontextaction != toolTipITA)
                    if(instructiontextaction.getToolTipText() != null)
                    {
                        setToolTipText(instructiontextaction.getToolTipText());
                        getToolTipListener().showComponentToolTip(this, mouseevent.getPoint());
                        setToolTipShowing(true);
                        toolTipITA = instructiontextaction;
                    } else
                    if(isToolTipShowing())
                    {
                        getToolTipListener().hideComponentToolTip(this);
                        setToolTipShowing(false);
                        toolTipITA = null;
                    }
                break;
            } while(true);
            if(!flag)
            {
                if(getCursor() != Cursor.getPredefinedCursor(0))
                    setCursor(Cursor.getPredefinedCursor(0));
                if(getToolTipListener() != null && isToolTipShowing())
                {
                    getToolTipListener().hideComponentToolTip(this);
                    setToolTipShowing(false);
                    toolTipITA = null;
                }
            }
        }
    }

    public void instructionText_MouseReleased(MouseEvent mouseevent)
    {
        java.awt.Point point = mouseevent.getPoint();
        Enumeration enumeration = actions.elements();
        do
        {
            if(!enumeration.hasMoreElements())
                break;
            InstructionTextAction instructiontextaction = (InstructionTextAction)enumeration.nextElement();
            if(!instructiontextaction.contains(point))
                continue;
            if(getGlossaryListener() != null && instructiontextaction.getActionCommand().startsWith("glossary;"))
                getGlossaryListener().actionPerformed(new ActionEvent(this, 1001, instructiontextaction.getActionCommand().substring(9)));
            fireActionPerformed(new ActionEvent(this, 1001, instructiontextaction.getActionCommand()));
            break;
        } while(true);
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(heightToFit && lastComputedHeight != j)
            reparseText = true;
        if((bufferImage == null || bufferImage.getWidth(this) != i || bufferImage.getHeight(this) != j) && !(g instanceof PrintGraphics))
        {
            bufferImage = createImage(i, j);
            reparseText = true;
        } else
        if(g instanceof PrintGraphics)
        {
            reparseText = true;
            i -= 10;
        }
        if(reparseText && (bufferImage != null || (g instanceof PrintGraphics)))
        {
            Graphics g1;
            if(g instanceof PrintGraphics)
            {
                g1 = g;
                g1.setColor(Color.white);
            } else
            {
                g1 = bufferImage.getGraphics();
                g1.setColor(getBackground());
            }
            g1.fillRect(0, 0, i, j);
            if(backgroundImage != null)
                g1.drawImage(backgroundImage, 0, 0, i, j, this);
            if(titleLines != null)
            {
                g1.setFont(titleFont);
                FontMetrics fontmetrics = g1.getFontMetrics();
                if(getTitleBackgroundColor() != null)
                {
                    g1.setColor(getTitleBackgroundColor());
                    int k = 0;
                    for(int i1 = 0; i1 < titleLines.length; i1++)
                        k = Math.max(k, fontmetrics.stringWidth(titleLines[i1]));

                    g1.fillRoundRect((i - k) / 2 - 4, 10, k + 8, fontmetrics.getHeight() * titleLines.length + 4, 4, 4);
                }
                g1.setColor(getTitleForegroundColor());
                for(int l = 0; l < titleLines.length; l++)
                    g1.drawString(titleLines[l], (i - fontmetrics.stringWidth(titleLines[l])) / 2, fontmetrics.getHeight() * (l + 1) + 8);

            }
            g1.setColor(Color.black);
            if(doOffset)
                paintText(g1, 20, 38, i - 20, j - 40);
            else
            if(titleLines == null)
                paintText(g1, getMyInsets().left + 1, getMyInsets().top + 1, i - getMyInsets().left - getMyInsets().right - 2, j - getMyInsets().top - getMyInsets().bottom - 2);
            else
                paintText(g1, getMyInsets().left + 1, getMyInsets().top + 3 + 25 * titleLines.length + 16, i - getMyInsets().left - getMyInsets().right - 2, j - getMyInsets().top - getMyInsets().bottom - 5 - 26 * titleLines.length);
            if(!(g instanceof PrintGraphics))
            {
                reparseText = false;
                g1.dispose();
            }
        }
        if(bufferImage != null && !(g instanceof PrintGraphics))
            g.drawImage(bufferImage, 0, 0, this);
    }

    public void print(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height);
        paint(g);
    }

    public void printIt(String s, Frame frame)
    {
        byte byte0 = 42;
        Printer printer = new Printer(frame, s);
        Font font = new Font("Helvetica", 0, 12);
        FontMetrics fontmetrics = getFontMetrics(font);
        if(fontmetrics == null)
            System.out.println("FontMetrics is null");
        printer.setLinesPerPage(byte0);
        printer.setFont(font);
        int i = printer.getPageWidth();
        String s1 = stripActions(getText());
        for(StringTokenizer stringtokenizer = new StringTokenizer(s1, "@"); stringtokenizer.hasMoreTokens();)
        {
            String s2 = stringtokenizer.nextToken();
            if(s2.length() > 4)
                printMultipleLines(printer, i, s2, fontmetrics);
            else
                printer.println(s2);
        }

        printer.newPage();
        printer.endJob();
    }

    public void redraw()
    {
        reparseText = true;
        repaint();
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.remove(aActionListener, actionlistener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().removePropertyChangeListener(propertychangelistener);
    }

    public void saveIt(String s, String s1)
    {
        try
        {
            FileDialog filedialog = new FileDialog(parentFrame, s, 1);
            filedialog.setFile(s1);
            filedialog.show();
            String s2;
            if((s2 = filedialog.getFile()) != null)
            {
                String s3 = filedialog.getDirectory() + s2;
                if(s3.indexOf(".*.*") != -1)
                    s3 = s3.substring(0, s3.length() - 4);
                File file = new File(s3);
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                PrintWriter printwriter = new PrintWriter(fileoutputstream);
                writeToStream(printwriter);
                printwriter.close();
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

    public void update(Graphics g)
    {
        paint(g);
    }

    protected PropertyChangeSupport getPropertyChange()
    {
        if(propertyChange == null)
            propertyChange = new PropertyChangeSupport(this);
        return propertyChange;
    }

    protected boolean isReparseText()
    {
        return reparseText;
    }

    protected void connEtoC1(MouseEvent mouseevent)
    {
        try
        {
            instructionText_MouseReleased(mouseevent);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    protected void connEtoC2(MouseEvent mouseevent)
    {
        try
        {
            instructionText_MouseMoved(mouseevent);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    protected void fireActionPerformed(ActionEvent actionevent)
    {
        if(aActionListener == null)
        {
            return;
        } else
        {
            aActionListener.actionPerformed(actionevent);
            return;
        }
    }

    protected void handleException(Throwable throwable)
    {
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        throwable.printStackTrace(System.out);
    }

    protected void initConnections()
        throws Exception
    {
        addMouseListener(ivjEventHandler);
        addMouseMotionListener(ivjEventHandler);
    }

    protected void initialize()
    {
        try
        {
            setName("InstructionText");
            setSize(50, 20);
            initConnections();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    protected void paintText(Graphics g, int i, int j, int k, int l)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(getText(), "\\");
        rawText = new StringBuffer(getText().length());
        String s = System.getProperty("line.separator");
        if(actions == null)
            actions = new Vector();
        else
            actions.removeAllElements();
        FontMetrics fontmetrics = null;
        if(g != null)
        {
            g.setFont(getFont());
            fontmetrics = g.getFontMetrics();
        } else
        if(storedFont != null)
            fontmetrics = Toolkit.getDefaultToolkit().getFontMetrics(storedFont);
        if(fontmetrics != null)
        {
            int i1;
            if(isClassicMacOS)
                i1 = fontmetrics.getHeight() + 4;
            else
                i1 = fontmetrics.getAscent() + 1;
            int j1 = i;
            int k1 = j + i1;
label0:
            for(int l1 = 0; stringtokenizer.hasMoreTokens(); l1++)
            {
                String s4;
                for(String s1 = stringtokenizer.nextToken(); s1.length() > 0; s4 = s1)
                {
                    String s3 = s1;
                    boolean flag = false;
                    for(int k2 = s3.indexOf(lineSeparator); s3.length() > 1 && (j1 + fontmetrics.stringWidth(s3) > k || k2 > 0); k2 = s3.indexOf(lineSeparator))
                    {
                        int l2 = s3.lastIndexOf(" ");
                        if(k2 > 0 && k2 < l2)
                            l2 = k2;
                        if(l2 > 0)
                        {
                            s3 = s3.substring(0, l2);
                            flag = true;
                        } else
                        {
                            j1 = i;
                            k1 += i1;
                            s3 = s1;
                        }
                    }

                    if(g != null)
                        g.setColor(getForeground());
                    String s8;
                    if(s3.charAt(0) == ' ' || s3.charAt(0) == lineSeparator.charAt(0))
                        s8 = s3.substring(1);
                    else
                        s8 = s3;
                    if(g != null)
                        g.drawString(s8, j1, k1);
                    rawText.append(s8);
                    if(!flag)
                    {
                        j1 += fontmetrics.stringWidth(s8);
                        s1 = "";
                        continue;
                    }
                    j1 = i;
                    k1 += i1;
                    rawText.append(s);
                    if(s1.length() < s3.length() + 1)
                        break;
                    s1 = s1.substring(s3.length() + 1);
                }

                Vector vector = new Vector();
                if(stringtokenizer.hasMoreTokens())
                {
                    for(String s2 = stringtokenizer.nextToken(); s2.length() > 0; s2 = "")
                    {
                        String s5 = s2;
                        String s7;
                        if(s5.charAt(0) == ' ' || s5.charAt(0) == lineSeparator.charAt(0))
                            s7 = s5.substring(1);
                        else
                            s7 = s5;
                        if(fontmetrics.stringWidth(s7) + j1 > k)
                        {
                            j1 = i;
                            k1 += i1;
                            rawText.append(s);
                        }
                        if(g != null)
                        {
                            g.setColor(getLinkColor());
                            g.drawString(s7, j1, k1);
                        }
                        rawText.append(s7);
                        if(g != null && isUnderlineLinks())
                            g.drawLine(j1, k1 + 1, j1 + fontmetrics.stringWidth(s7), k1 + 1);
                        InstructionTextAction instructiontextaction = new InstructionTextAction();
                        vector.addElement(instructiontextaction);
                        actions.addElement(instructiontextaction);
                        instructiontextaction.setRectangle(new Rectangle(j1, k1 - fontmetrics.getAscent(), fontmetrics.stringWidth(s7), i1));
                        j1 += fontmetrics.stringWidth(s7 + " ");
                    }

                }
                if(!stringtokenizer.hasMoreTokens())
                    continue;
                String s6 = stringtokenizer.nextToken();
                Enumeration enumeration = vector.elements();
                do
                {
                    InstructionTextAction instructiontextaction1;
                    do
                    {
                        if(!enumeration.hasMoreElements())
                            continue label0;
                        instructiontextaction1 = (InstructionTextAction)enumeration.nextElement();
                        instructiontextaction1.setActionCommand(s6);
                    } while(!s6.equals("glossary"));
                    instructiontextaction1.setToolTipText("Click for definition");
                } while(true);
            }

            if(heightToFit)
            {
                int i2 = getSize().height;
                int j2 = k1 + i1;
                if(j2 != i2)
                {
                    setBounds(getBounds().x, getBounds().y, getBounds().width, j2);
                    pSize.height = j2;
                    Container container = getParent();
                    if(container != null && numValidations < 3)
                    {
                        container.validate();
                        numValidations++;
                    }
                }
                lastComputedHeight = j2;
            }
        }
    }

    protected void printMultipleLines(Printer printer, int i, String s, FontMetrics fontmetrics)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, " \n", true);
        int j = 0;
        StringBuffer stringbuffer = new StringBuffer(i + 1);
        while(stringtokenizer.hasMoreTokens()) 
        {
            String s1 = stringtokenizer.nextToken();
            int k = fontmetrics.stringWidth(s1);
            if(j + k > i)
            {
                if(stringbuffer.length() > 0 && stringbuffer.charAt(stringbuffer.length() - 1) == ' ')
                    stringbuffer.setLength(stringbuffer.length() - 1);
                if(stringbuffer.length() > 0 && stringbuffer.charAt(0) == ' ')
                    printer.println(stringbuffer.toString().substring(1));
                else
                    printer.println(stringbuffer.toString());
                j = 0;
                stringbuffer.setLength(0);
            }
            stringbuffer.append(s1);
            j += fontmetrics.stringWidth(s1);
        }
        if(stringbuffer.length() > 0)
            printer.println(stringbuffer.toString());
    }

    protected String stripActions(String s)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, "\\");
        StringBuffer stringbuffer = new StringBuffer(s.length());
        do
        {
            if(!stringtokenizer.hasMoreTokens())
                break;
            stringbuffer.append(stringtokenizer.nextToken());
            if(stringtokenizer.hasMoreTokens())
                stringbuffer.append(stringtokenizer.nextToken());
            if(stringtokenizer.hasMoreTokens())
                stringtokenizer.nextToken();
        } while(true);
        return stringbuffer.toString();
    }

    protected void writeToStream(PrintWriter printwriter)
    {
        if(rawText == null || rawText.length() < 4)
        {
            paintText(null, 0, 0, 400, 1000);
            reparseText = true;
        }
        printwriter.println(rawText.toString());
    }

    private void checkOS()
    {
        if(System.getProperty("java.vendor").toLowerCase().startsWith("apple") && System.getProperty("java.version").startsWith("1.1"))
            isClassicMacOS = true;
    }

    public static final boolean DEBUG = false;
    public static String lineSeparator = null;
    protected transient ActionListener aActionListener;
    protected String actionStrings[];
    protected volatile Vector actions;
    protected Image backgroundImage;
    protected Image bufferImage;
    protected boolean doOffset;
    protected Color fieldLinkColor;
    protected String fieldText;
    protected ActionListener glossaryListener;
    protected boolean heightToFit;
    protected boolean isClassicMacOS;
    protected int lastComputedHeight;
    protected Dimension pSize;
    protected transient PropertyChangeSupport propertyChange;
    protected StringBuffer rawText;
    protected boolean reparseText;
    protected Font storedFont;
    protected Color titleBackgroundColor;
    protected final Font titleFont = new Font("Helvetica", 1, 15);
    protected Color titleForegroundColor;
    protected String titleLines[];
    boolean hasPrivilege;
    IvjEventHandler ivjEventHandler;
    int numValidations;
    Frame parentFrame;
    private String currentToolTipText;
    private Insets insets;
    private InstructionTextAction toolTipITA;
    private boolean toolTipShowing;
    private boolean underlineLinks;

}
