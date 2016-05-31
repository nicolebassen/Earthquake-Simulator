// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EECButton.java

package com.sciencecourseware.components;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.EventObject;

// Referenced classes of package com.sciencecourseware.components:
//            ToolTipComponent, ComponentToolTipListener

public class EECButton extends ToolTipComponent
    implements Serializable
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
            if(isEnabled())
            {
                if(getToolTipListener() != null)
                {
                    toolTipCursor.setLocation(mouseevent.getPoint());
                    getToolTipListener().showComponentToolTip(EECButton.this, toolTipCursor);
                    toolTipShowing = true;
                }
                if(mouseevent.getSource() == EECButton.this)
                    connEtoC2(mouseevent);
            }
        }

        public void mouseExited(MouseEvent mouseevent)
        {
            if(toolTipShowing && toolTipListener != null)
            {
                toolTipListener.hideComponentToolTip(EECButton.this);
                toolTipShowing = false;
            }
            if(isEnabled() && mouseevent.getSource() == EECButton.this)
                connEtoC3(mouseevent);
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
        }

        public void mousePressed(MouseEvent mouseevent)
        {
            if(isEnabled())
            {
                toolTipCursor.setLocation(mouseevent.getPoint());
                eECButton_MousePressed(mouseevent);
            }
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            if(isEnabled())
            {
                toolTipCursor.setLocation(mouseevent.getPoint());
                if(mouseevent.getSource() == EECButton.this)
                    connEtoC1(mouseevent);
            }
        }

        Point toolTipCursor;
        boolean toolTipShowing;

        IvjEventHandler()
        {
            toolTipCursor = new Point();
            toolTipShowing = false;
        }
    }


    public EECButton()
    {
        aActionListener = null;
        ivjEventHandler = new IvjEventHandler();
        actionString = null;
        backgroundImage = null;
        depressed = false;
        depressedOffset = 3;
        disabledForegroundColor = null;
        disabledImage = null;
        downImage = null;
        fieldAltLabel = new String();
        fieldDiamondBevelHighlightColor = Color.white;
        fieldDiamondBevelShadowColor = Color.black;
        fieldDiamondColor = Color.red;
        fieldDiamondThickness = 4;
        fieldLabel = new String();
        fieldPreferredHeight = 0;
        fieldPreferredWidth = 0;
        fieldRolledOver = false;
        fieldRolloverColor = Color.lightGray;
        fieldShowAltLabel = false;
        onTopDisabledImage = null;
        onTopDownImage = null;
        onTopImage = null;
        overDownImage = null;
        overImage = null;
        rescaleToFit = false;
        selected = false;
        textOffset = 0;
        textYOffset = 0;
        toolTipText = null;
        upImage = null;
        diamondUpDown = 0;
        initialize();
    }

    public static void main(String args[])
    {
        try
        {
            Frame frame = new Frame();
            EECButton eecbutton = new EECButton();
            frame.add("Center", eecbutton);
            frame.setSize(eecbutton.getSize());
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent windowevent)
                {
                    System.exit(0);
                }

            }
);
            frame.setVisible(true);
        }
        catch(Throwable throwable)
        {
            System.err.println("Exception occurred in main() of edu.calstatela.eec.EECButton");
            throwable.printStackTrace(System.out);
        }
    }

    public void setActionCommand(String s)
    {
        actionString = s;
    }

    public String getActionCommand()
    {
        if(actionString == null || actionString == "")
        {
            if(getShowAltLabel())
                return getAltLabel();
            else
                return getLabel();
        } else
        {
            return actionString;
        }
    }

    public void setAltLabel(String s)
    {
        fieldAltLabel = s;
    }

    public String getAltLabel()
    {
        return fieldAltLabel;
    }

    public void setBackgroundImage(Image image)
    {
        backgroundImage = image;
        repaint();
    }

    public Image getBackgroundImage()
    {
        return backgroundImage;
    }

    public void setDepressedOffset(int i)
    {
        depressedOffset = i;
    }

    public int getDepressedOffset()
    {
        return depressedOffset;
    }

    public void setDiamondBevelHighlightColor(Color color)
    {
        fieldDiamondBevelHighlightColor = color;
    }

    public Color getDiamondBevelHighlightColor()
    {
        return fieldDiamondBevelHighlightColor;
    }

    public void setDiamondBevelShadowColor(Color color)
    {
        fieldDiamondBevelShadowColor = color;
    }

    public Color getDiamondBevelShadowColor()
    {
        return fieldDiamondBevelShadowColor;
    }

    public void setDiamondColor(Color color)
    {
        fieldDiamondColor = color;
    }

    public Color getDiamondColor()
    {
        return fieldDiamondColor;
    }

    public void setDiamondThickness(int i)
    {
        fieldDiamondThickness = i;
    }

    public int getDiamondThickness()
    {
        return fieldDiamondThickness;
    }

    public void setDiamondUpDown(int i)
    {
        diamondUpDown = i;
    }

    public int getDiamondUpDown()
    {
        return diamondUpDown;
    }

    public void setDisabledForeground(Color color)
    {
        disabledForegroundColor = color;
        repaint();
    }

    public void setDisabledImage(Image image)
    {
        disabledImage = image;
        if(!isEnabled())
            repaint();
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        if(flag)
            setCursor(Cursor.getPredefinedCursor(12));
        else
            setCursor(null);
        repaint();
    }

    public void setImages(Image image, Image image1, Image image2, Image image3)
    {
        upImage = image;
        downImage = image1;
        overImage = image2;
        overDownImage = image3;
    }

    public void setLabel(String s)
    {
        fieldLabel = s;
    }

    public String getLabel()
    {
        return fieldLabel;
    }

    public void setOnTopImages(Image image, Image image1, Image image2)
    {
        onTopImage = image;
        onTopDownImage = image1;
        onTopDisabledImage = image2;
    }

    public void setPreferredHeight(int i)
    {
        fieldPreferredHeight = i;
    }

    public int getPreferredHeight()
    {
        return fieldPreferredHeight;
    }

    public void setPreferredWidth(int i)
    {
        fieldPreferredWidth = i;
    }

    public int getPreferredWidth()
    {
        return fieldPreferredWidth;
    }

    public void setRescaleToFit(boolean flag)
    {
        rescaleToFit = flag;
    }

    public boolean getRescaleToFit()
    {
        return rescaleToFit;
    }

    public void setRolledOver(boolean flag)
    {
        fieldRolledOver = flag;
    }

    public boolean getRolledOver()
    {
        return fieldRolledOver;
    }

    public void setRolloverColor(Color color)
    {
        fieldRolloverColor = color;
    }

    public Color getRolloverColor()
    {
        return fieldRolloverColor;
    }

    public void setSelected(boolean flag)
    {
        selected = flag;
        repaint();
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setShowAltLabel(boolean flag)
    {
        fieldShowAltLabel = flag;
        repaint();
    }

    public boolean getShowAltLabel()
    {
        return fieldShowAltLabel;
    }

    public void setTextYOffset(int i)
    {
        textYOffset = i;
    }

    public int getTextYOffset()
    {
        return textYOffset;
    }

    public void setToolTipText(String s)
    {
        toolTipText = s;
    }

    public String getToolTipText()
    {
        return toolTipText;
    }

    public void addActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.add(aActionListener, actionlistener);
    }

    public void eECButton_MouseClicked()
    {
    }

    public void eECButton_MouseEntered()
    {
        setRolledOver(true);
        repaint();
    }

    public void eECButton_MouseExited()
    {
        setRolledOver(false);
        depressed = false;
        repaint();
    }

    public void eECButton_MousePressed(MouseEvent mouseevent)
    {
        depressed = true;
        repaint();
    }

    public void eECButton_MouseReleased(MouseEvent mouseevent)
    {
        if(depressed)
            fireActionPerformed(new ActionEvent(this, 1001, getActionCommand()));
        depressed = false;
        repaint();
    }

    public Dimension iminimumSize()
    {
        return getPreferredSize();
    }

    public void paint(Graphics g)
    {
        int i = getSize().width;
        int j = getSize().height;
        if(bufferImage == null || bufferImage.getWidth(this) != i || bufferImage.getHeight(this) != j)
            bufferImage = createImage(i, j);
        if(bufferImage != null)
        {
            Graphics g1 = bufferImage.getGraphics();
            if(g1 != null)
            {
                paintBackground(g1, i, j);
                g1.dispose();
            }
            g.drawImage(bufferImage, 0, 0, this);
        }
    }

    public void paintBackground(Graphics g, int i, int j)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, i, j);
        if(backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, this);
        int k = textOffset;
        Image image;
        Image image1;
        if(!isEnabled())
        {
            if(disabledImage == null)
                image = upImage;
            else
                image = disabledImage;
            if(onTopDisabledImage == null)
                image1 = onTopImage;
            else
                image1 = onTopDisabledImage;
        } else
        if(depressed || selected)
        {
            if(getRolledOver())
                image = overDownImage;
            else
                image = downImage;
            if(onTopDownImage != null)
                image1 = onTopDownImage;
            else
                image1 = onTopImage;
            k += depressedOffset;
        } else
        {
            if(getRolledOver())
                image = overImage;
            else
                image = upImage;
            image1 = onTopImage;
        }
        int j1 = image.getWidth(this);
        int k1 = image.getHeight(this);
        double d = 1.0D;
        double d1 = 1.0D;
        int l;
        int i1;
        if(!rescaleToFit)
        {
            l = (i - j1) / 2;
            i1 = (j - k1) / 2;
            g.drawImage(image, l, i1, this);
        } else
        {
            d = (double)(i - 3) / (double)j1;
            d1 = (double)(j - 3) / (double)k1;
            j1 = i - 3;
            k1 = j - 3;
            l = 0;
            i1 = 0;
            g.drawImage(image, l, i1, j1, k1, this);
        }
        if(image1 != null)
            if(!rescaleToFit)
            {
                g.drawImage(image1, (i - image1.getWidth(this)) / 2, (j - image1.getHeight(this)) / 2, this);
            } else
            {
                int l1 = (int)Math.round((double)image1.getWidth(this) * d);
                int i2 = (int)Math.round((double)image1.getHeight(this) * d1);
                g.drawImage(image1, (i - l1) / 2, (j - i2) / 2, l1, i2, this);
            }
        if(!isEnabled() && disabledForegroundColor != null)
            g.setColor(disabledForegroundColor);
        else
            g.setColor(getForeground());
        g.setFont(getFont());
        FontMetrics fontmetrics = g.getFontMetrics();
        String s;
        if(getShowAltLabel())
            s = getAltLabel();
        else
            s = getLabel();
        int j2 = fontmetrics.stringWidth(s);
        int k2 = (j1 - 3 - j2) / 2 + l + k;
        int l2 = (((((k1 - 3) + fontmetrics.getAscent()) / 2 - fontmetrics.getAscent() / 4) + i1 + k) - 1) + textYOffset;
        g.drawString(s, k2, l2);
    }

    public Dimension getPreferredSize()
    {
        Dimension dimension = new Dimension(getPreferredWidth(), getPreferredHeight());
        return dimension;
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        aActionListener = AWTEventMulticaster.remove(aActionListener, actionlistener);
    }

    public void toggleLabel()
    {
        setShowAltLabel(!getShowAltLabel());
    }

    public void update(Graphics g)
    {
        paint(g);
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

    private void connEtoC1(MouseEvent mouseevent)
    {
        try
        {
            eECButton_MouseReleased(null);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC2(MouseEvent mouseevent)
    {
        try
        {
            eECButton_MouseEntered();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC3(MouseEvent mouseevent)
    {
        try
        {
            eECButton_MouseExited();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void handleException(Throwable throwable)
    {
    }

    private void initConnections()
        throws Exception
    {
        addMouseListener(ivjEventHandler);
    }

    private void initialize()
    {
        try
        {
            setName("EECButton");
            setSize(150, 150);
            initConnections();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        setCursor(new Cursor(12));
    }

    private void readObject(ObjectInputStream objectinputstream)
        throws IOException, ClassNotFoundException
    {
    }

    private void writeObject(ObjectOutputStream objectoutputstream)
        throws IOException
    {
    }

    public static final int DOWN = 2;
    public static final int NEITHER = 0;
    public static final int UP = 1;
    protected transient ActionListener aActionListener;
    IvjEventHandler ivjEventHandler;
    private String actionString;
    private Image backgroundImage;
    private Image bufferImage;
    private boolean depressed;
    private int depressedOffset;
    private Color disabledForegroundColor;
    private Image disabledImage;
    private Image downImage;
    private String fieldAltLabel;
    private Color fieldDiamondBevelHighlightColor;
    private Color fieldDiamondBevelShadowColor;
    private Color fieldDiamondColor;
    private int fieldDiamondThickness;
    private String fieldLabel;
    private int fieldPreferredHeight;
    private int fieldPreferredWidth;
    private boolean fieldRolledOver;
    private Color fieldRolloverColor;
    private boolean fieldShowAltLabel;
    private Image onTopDisabledImage;
    private Image onTopDownImage;
    private Image onTopImage;
    private Image overDownImage;
    private Image overImage;
    private boolean rescaleToFit;
    private boolean selected;
    private int textOffset;
    private int textYOffset;
    private String toolTipText;
    private Image upImage;
    private int diamondUpDown;



}
