// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LinkLabel.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.sciencecourseware.components:
//            ToolTipComponent, ImageServer, ComponentToolTipListener

public class LinkLabel extends ToolTipComponent
{
    public class SpecialText
    {

        public void setText(String s)
        {
            text = s;
        }

        public String getText()
        {
            return text;
        }

        public void setType(int i)
        {
            type = i;
        }

        public int getType()
        {
            return type;
        }

        private String text;
        private int type;

        public SpecialText(String s, int i)
        {
            setText(s);
            setType(i);
        }
    }

    public class MouseHandler extends MouseAdapter
        implements MouseMotionListener
    {

        public void mouseDragged(MouseEvent mouseevent)
        {
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
            if(isEnabled() && getToolTipListener() != null)
            {
                toolTipCursor.setLocation(mouseevent.getPoint());
                getToolTipListener().showComponentToolTip(LinkLabel.this, toolTipCursor);
                toolTipShowing = true;
            }
            mouseMoved(mouseevent);
        }

        public void mouseExited(MouseEvent mouseevent)
        {
            if(toolTipShowing && toolTipListener != null)
            {
                toolTipListener.hideComponentToolTip(LinkLabel.this);
                toolTipShowing = false;
            }
            setDefaultCursor();
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
            if(linkRectangle != null)
                if(linkRectangle.contains(mouseevent.getX(), mouseevent.getY()))
                    setHandCursor();
                else
                    setDefaultCursor();
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(getActionCommand() != null && linkRectangle.contains(mouseevent.getX(), mouseevent.getY()))
                fireActionPerformed(new ActionEvent(this, 1001, getActionCommand()));
        }

        Point toolTipCursor;
        boolean toolTipShowing;

        public MouseHandler()
        {
            toolTipCursor = new Point();
            toolTipShowing = false;
        }
    }


    public LinkLabel()
    {
        this("", 0);
    }

    public LinkLabel(String s)
    {
        this(s, 0, null);
    }

    public LinkLabel(String s, int i)
    {
        this(s, i, null);
    }

    public LinkLabel(String s, int i, ImageServer imageserver)
    {
        aActionListener = null;
        actionCommand = null;
        bufferImage = null;
        defaultCursor = Cursor.getDefaultCursor();
        handCursor = Cursor.getPredefinedCursor(12);
        imageServer = null;
        linkColor = Color.blue;
        linkRectangle = null;
        maxImageHeight = 0x80000000;
        pHeight = 0x7fffffff;
        pWidth = 0x7fffffff;
        plainFontMetrics = null;
        superFont = null;
        superFontMetrics = null;
        textVector = null;
        textWidth = 0;
        toolTipText = null;
        underlineLink = true;
        alignment = 0;
        setImageServer(imageserver);
        setText(s);
        setAlignment(i);
        MouseHandler mousehandler = new MouseHandler();
        addMouseListener(mousehandler);
        addMouseMotionListener(mousehandler);
    }

    public LinkLabel(String s, ImageServer imageserver)
    {
        this(s, 0, imageserver);
    }

    public void setActionCommand(String s)
    {
        actionCommand = s;
    }

    public String getActionCommand()
    {
        return actionCommand;
    }

    public void setAlignment(int i)
    {
        alignment = i;
        repaint();
    }

    public int getAlignment()
    {
        return alignment;
    }

    public void setDefaultCursor()
    {
        if(getCursor() != defaultCursor)
            setCursor(defaultCursor);
    }

    public void setFont(Font font)
    {
        super.setFont(font);
        superFont = null;
        superFontMetrics = null;
        plainFontMetrics = null;
        parseText();
    }

    public void setHandCursor()
    {
        if(getCursor() != handCursor)
            setCursor(handCursor);
    }

    public void setImageServer(ImageServer imageserver)
    {
        imageServer = imageserver;
    }

    public void setLinkColor(Color color)
    {
        linkColor = color;
        repaint();
    }

    public Color getLinkColor()
    {
        return linkColor;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        if(pWidth == 0x7fffffff || pHeight == 0x7fffffff)
        {
            FontMetrics fontmetrics = getPlainFontMetrics();
            if(fontmetrics != null)
            {
                pWidth = textWidth;
                pHeight = Math.max(fontmetrics.getHeight(), maxImageHeight);
            } else
            {
                return super.getPreferredSize();
            }
        }
        return new Dimension(pWidth, pHeight);
    }

    public void setText(String s)
    {
        text = s;
        parseText();
        if(text != null)
        {
            pWidth = 0x7fffffff;
            pHeight = 0x7fffffff;
            invalidate();
        } else
        {
            pWidth = 0;
            pHeight = 0;
        }
        repaint();
    }

    public String getText()
    {
        return text;
    }

    public void setToolTipText(String s)
    {
        toolTipText = s;
    }

    public String getToolTipText()
    {
        return toolTipText;
    }

    public void setUnderlineLink(boolean flag)
    {
        underlineLink = flag;
    }

    public boolean isUnderlineLink()
    {
        return underlineLink;
    }

    public void addActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.add(aActionListener, actionlistener);
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(bufferImage == null || i != bufferImage.getWidth(this) || j != bufferImage.getHeight(this))
            bufferImage = createImage(i, j);
        if(bufferImage != null)
        {
            Graphics g1 = bufferImage.getGraphics();
            if(g1 != null && textVector != null)
            {
                g1.setColor(getBackground());
                g1.fillRect(0, 0, i, j);
                if(getActionCommand() == null)
                    g1.setColor(Color.black);
                else
                    g1.setColor(getLinkColor());
                g1.setFont(getFont());
                FontMetrics fontmetrics = getPlainFontMetrics();
                int k = 0;
                switch(getAlignment())
                {
                case 0: // '\0'
                    k = 0;
                    break;

                case 1: // '\001'
                    k = (i - textWidth) / 2;
                    break;

                case 2: // '\002'
                    k = i - textWidth;
                    break;
                }
                int l = k;
                int i1 = j - fontmetrics.getMaxDescent() - 2;
                int j1 = j - fontmetrics.getMaxDescent() - 2 - fontmetrics.getAscent();
                Enumeration enumeration = textVector.elements();
                do
                {
                    if(!enumeration.hasMoreElements())
                        break;
                    Object obj = enumeration.nextElement();
                    if(obj instanceof String)
                    {
                        int k1 = fontmetrics.stringWidth((String)obj);
                        g1.drawString((String)obj, l, i1);
                        l += k1;
                    } else
                    if(obj instanceof Image)
                    {
                        g1.drawImage((Image)obj, l, j1, this);
                        l += ((Image)obj).getWidth(this);
                    } else
                    if(obj instanceof SpecialText)
                        l += paintSpecialText(g1, (SpecialText)obj, l, i1);
                } while(true);
                if(getActionCommand() != null)
                {
                    if(isUnderlineLink())
                        g1.drawLine(k, i1 + 2, k + textWidth, i1 + 2);
                    if(linkRectangle == null)
                        linkRectangle = new Rectangle(k - 1, i1 - fontmetrics.getMaxAscent() - 1, textWidth + 2, fontmetrics.getHeight() + 2);
                    else
                        linkRectangle.setBounds(k - 1, i1 - fontmetrics.getMaxAscent() - 1, textWidth + 2, fontmetrics.getHeight() + 2);
                } else
                {
                    linkRectangle = null;
                }
                g1.dispose();
            }
            g.drawImage(bufferImage, 0, 0, this);
        }
    }

    public void parseText()
    {
        int i = 0;
        int j = text.indexOf('&');
        textWidth = 0;
        textVector = new Vector();
        for(; j >= 0; j = text.indexOf('&', i))
        {
            if(j - i > 0)
            {
                String s = text.substring(i, j);
                textVector.addElement(s);
                if(getPlainFontMetrics() != null)
                    textWidth += getPlainFontMetrics().stringWidth(s);
            }
            int k = text.indexOf(';', j);
            String s2 = text.substring(j + 1, k);
            if(s2.startsWith("img=") && imageServer != null)
            {
                String s3 = s2.substring(4);
                if(s3 != null)
                {
                    Image image = imageServer.getImage(s3);
                    if(image != null)
                    {
                        textVector.addElement(image);
                        textWidth += image.getWidth(this);
                        maxImageHeight = Math.max(maxImageHeight, image.getHeight(this));
                    }
                }
            } else
            if(s2.startsWith("sup="))
            {
                String s4 = s2.substring(4);
                if(s4 != null)
                {
                    SpecialText specialtext = new SpecialText(s4, 0);
                    if(getSuperFontMetrics() != null)
                        textWidth += getSuperFontMetrics().stringWidth(s4);
                    textVector.addElement(specialtext);
                }
            }
            i = k + 1;
        }

        if(i < text.length())
        {
            String s1 = text.substring(i);
            textVector.addElement(s1);
            if(getPlainFontMetrics() != null)
                textWidth += getPlainFontMetrics().stringWidth(s1);
        }
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.remove(aActionListener, actionlistener);
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("LinkLabel; text=\"");
        stringbuffer.append(text);
        stringbuffer.append("\"");
        if(textVector != null)
        {
            stringbuffer.append("; parts=");
            Enumeration enumeration = textVector.elements();
            do
            {
                if(!enumeration.hasMoreElements())
                    break;
                Object obj = enumeration.nextElement();
                if(obj instanceof String)
                {
                    stringbuffer.append("string[");
                    stringbuffer.append((String)obj);
                    stringbuffer.append("],");
                } else
                if(obj instanceof SpecialText)
                {
                    stringbuffer.append("specialtext[");
                    stringbuffer.append(((SpecialText)obj).getText());
                    stringbuffer.append("],");
                } else
                if(obj instanceof Image)
                {
                    stringbuffer.append("image[w=");
                    Image image = (Image)obj;
                    stringbuffer.append(image.getWidth(this));
                    stringbuffer.append(",h=");
                    stringbuffer.append(image.getHeight(this));
                    stringbuffer.append("],");
                }
            } while(true);
            stringbuffer.setLength(stringbuffer.length() - 1);
        }
        return stringbuffer.toString();
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    protected FontMetrics getPlainFontMetrics()
    {
        if(plainFontMetrics == null && getFont() != null)
            plainFontMetrics = getFontMetrics(getFont());
        return plainFontMetrics;
    }

    protected Font getSuperFont()
    {
        if(superFont == null && getFont() != null)
            superFont = new Font(getFont().getName(), getFont().getStyle(), getFont().getSize() - 1);
        return superFont;
    }

    protected FontMetrics getSuperFontMetrics()
    {
        if(superFontMetrics == null)
        {
            Font font = getSuperFont();
            if(font != null)
                superFontMetrics = getFontMetrics(font);
        }
        return superFontMetrics;
    }

    protected void fireActionPerformed(ActionEvent actionevent)
    {
        if(aActionListener == null)
        {
            return;
        } else
        {
            System.out.println("LinkLabel firing action " + actionevent.getActionCommand());
            aActionListener.actionPerformed(actionevent);
            return;
        }
    }

    protected int paintSpecialText(Graphics g, SpecialText specialtext, int i, int j)
    {
        Font font = g.getFont();
        int k = 0;
        if(specialtext.getType() == 0)
        {
            g.setFont(getSuperFont());
            g.drawString(specialtext.getText(), i, j - getPlainFontMetrics().getAscent() / 2);
            if(getSuperFontMetrics() != null)
                k = getSuperFontMetrics().stringWidth(specialtext.getText());
        }
        g.setFont(font);
        return k;
    }

    public static final int CENTER = 1;
    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    public static final int SUPERSCRIPT = 0;
    protected transient ActionListener aActionListener;
    private String actionCommand;
    private Image bufferImage;
    private Cursor defaultCursor;
    private Cursor handCursor;
    private ImageServer imageServer;
    private Color linkColor;
    private Rectangle linkRectangle;
    private int maxImageHeight;
    private int pHeight;
    private int pWidth;
    private FontMetrics plainFontMetrics;
    private Font superFont;
    private FontMetrics superFontMetrics;
    private String text;
    private Vector textVector;
    private int textWidth;
    private String toolTipText;
    private boolean underlineLink;
    private int alignment;

}
