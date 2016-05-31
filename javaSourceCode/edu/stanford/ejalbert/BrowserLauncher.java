// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BrowserLauncher.java

package edu.stanford.ejalbert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;

public class BrowserLauncher
{

    private BrowserLauncher()
    {
    }

    private static native int ICLaunchURL(int i, byte abyte0[], byte abyte1[], int j, int ai[], int ai1[]);

    private static native int ICStart(int ai[], int i);

    private static native int ICStop(int ai[]);

    private static boolean loadClasses()
    {
        switch(jvm)
        {
        case 2: // '\002'
        default:
            break;

        case 0: // '\0'
            try
            {
                Class class1 = Class.forName("com.apple.MacOS.AETarget");
                Class class3 = Class.forName("com.apple.MacOS.OSUtils");
                Class class4 = Class.forName("com.apple.MacOS.AppleEvent");
                Class class5 = Class.forName("com.apple.MacOS.ae");
                aeDescClass = Class.forName("com.apple.MacOS.AEDesc");
                aeTargetConstructor = class1.getDeclaredConstructor(new Class[] {
                    Integer.TYPE
                });
                appleEventConstructor = class4.getDeclaredConstructor(new Class[] {
                    Integer.TYPE, Integer.TYPE, class1, Integer.TYPE, Integer.TYPE
                });
                aeDescConstructor = aeDescClass.getDeclaredConstructor(new Class[] {
                    java.lang.String.class
                });
                makeOSType = class3.getDeclaredMethod("makeOSType", new Class[] {
                    java.lang.String.class
                });
                putParameter = class4.getDeclaredMethod("putParameter", new Class[] {
                    Integer.TYPE, aeDescClass
                });
                sendNoReply = class4.getDeclaredMethod("sendNoReply", new Class[0]);
                Field field1 = class5.getDeclaredField("keyDirectObject");
                keyDirectObject = (Integer)field1.get(null);
                Field field2 = class4.getDeclaredField("kAutoGenerateReturnID");
                kAutoGenerateReturnID = (Integer)field2.get(null);
                Field field3 = class4.getDeclaredField("kAnyTransactionID");
                kAnyTransactionID = (Integer)field3.get(null);
                break;
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                errorMessage = classnotfoundexception.getMessage();
                return false;
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                errorMessage = nosuchmethodexception.getMessage();
                return false;
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                errorMessage = nosuchfieldexception.getMessage();
                return false;
            }
            catch(IllegalAccessException illegalaccessexception)
            {
                errorMessage = illegalaccessexception.getMessage();
            }
            return false;

        case 1: // '\001'
            try
            {
                mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
                mrjOSTypeClass = Class.forName("com.apple.mrj.MRJOSType");
                Field field = mrjFileUtilsClass.getDeclaredField("kSystemFolderType");
                kSystemFolderType = field.get(null);
                findFolder = mrjFileUtilsClass.getDeclaredMethod("findFolder", new Class[] {
                    mrjOSTypeClass
                });
                getFileCreator = mrjFileUtilsClass.getDeclaredMethod("getFileCreator", new Class[] {
                    java.io.File.class
                });
                getFileType = mrjFileUtilsClass.getDeclaredMethod("getFileType", new Class[] {
                    java.io.File.class
                });
                break;
            }
            catch(ClassNotFoundException classnotfoundexception1)
            {
                errorMessage = classnotfoundexception1.getMessage();
                return false;
            }
            catch(NoSuchFieldException nosuchfieldexception1)
            {
                errorMessage = nosuchfieldexception1.getMessage();
                return false;
            }
            catch(NoSuchMethodException nosuchmethodexception1)
            {
                errorMessage = nosuchmethodexception1.getMessage();
                return false;
            }
            catch(SecurityException securityexception)
            {
                errorMessage = securityexception.getMessage();
                return false;
            }
            catch(IllegalAccessException illegalaccessexception1)
            {
                errorMessage = illegalaccessexception1.getMessage();
            }
            return false;

        case 3: // '\003'
            try
            {
                Class class2 = Class.forName("com.apple.mrj.jdirect.Linker");
                Constructor constructor = class2.getConstructor(new Class[] {
                    java.lang.Class.class
                });
                linkage = constructor.newInstance(new Object[] {
                    edu.stanford.ejalbert.BrowserLauncher.class
                });
                break;
            }
            catch(ClassNotFoundException classnotfoundexception2)
            {
                errorMessage = classnotfoundexception2.getMessage();
                return false;
            }
            catch(NoSuchMethodException nosuchmethodexception2)
            {
                errorMessage = nosuchmethodexception2.getMessage();
                return false;
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                errorMessage = invocationtargetexception.getMessage();
                return false;
            }
            catch(InstantiationException instantiationexception)
            {
                errorMessage = instantiationexception.getMessage();
                return false;
            }
            catch(IllegalAccessException illegalaccessexception2)
            {
                errorMessage = illegalaccessexception2.getMessage();
            }
            return false;

        case 4: // '\004'
            try
            {
                mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
                openURL = mrjFileUtilsClass.getDeclaredMethod("openURL", new Class[] {
                    java.lang.String.class
                });
                break;
            }
            catch(ClassNotFoundException classnotfoundexception3)
            {
                errorMessage = classnotfoundexception3.getMessage();
                return false;
            }
            catch(NoSuchMethodException nosuchmethodexception3)
            {
                errorMessage = nosuchmethodexception3.getMessage();
            }
            return false;
        }
        return true;
    }

    private static Object locateBrowser()
    {
        if(browser != null)
            return browser;
        jvm;
        JVM INSTR tableswitch -1 6: default 522
    //                   -1 522
    //                   0 60
    //                   1 200
    //                   2 522
    //                   3 498
    //                   4 498
    //                   5 506
    //                   6 514;
           goto _L1 _L1 _L2 _L3 _L1 _L4 _L4 _L5 _L6
_L2:
        Object obj2;
        Integer integer = (Integer)makeOSType.invoke(null, new Object[] {
            "MACS"
        });
        Object obj1 = aeTargetConstructor.newInstance(new Object[] {
            integer
        });
        Integer integer1 = (Integer)makeOSType.invoke(null, new Object[] {
            "GURL"
        });
        obj2 = appleEventConstructor.newInstance(new Object[] {
            integer1, integer1, obj1, kAutoGenerateReturnID, kAnyTransactionID
        });
        return obj2;
        Object obj;
        obj;
        browser = null;
        errorMessage = ((Throwable) (obj)).getMessage();
        return browser;
        obj;
        browser = null;
        errorMessage = ((Throwable) (obj)).getMessage();
        return browser;
        obj;
        browser = null;
        errorMessage = ((Throwable) (obj)).getMessage();
        return browser;
_L3:
        File file;
        String as[];
        int i;
        try
        {
            file = (File)findFolder.invoke(null, new Object[] {
                kSystemFolderType
            });
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            browser = null;
            errorMessage = illegalargumentexception.getMessage();
            return browser;
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            browser = null;
            errorMessage = illegalaccessexception.getMessage();
            return browser;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            browser = null;
            errorMessage = invocationtargetexception.getTargetException().getClass() + ": " + invocationtargetexception.getTargetException().getMessage();
            return browser;
        }
        as = file.list();
        i = 0;
_L9:
        if(i >= as.length) goto _L8; else goto _L7
_L7:
        Object obj3;
        Object obj4;
        try
        {
            obj2 = new File(file, as[i]);
            if(!((File) (obj2)).isFile())
                continue; /* Loop/switch isn't completed */
        }
        catch(IllegalArgumentException illegalargumentexception1)
        {
            errorMessage = illegalargumentexception1.getMessage();
            return null;
        }
        catch(IllegalAccessException illegalaccessexception1)
        {
            browser = null;
            errorMessage = illegalaccessexception1.getMessage();
            return browser;
        }
        catch(InvocationTargetException invocationtargetexception1)
        {
            browser = null;
            errorMessage = invocationtargetexception1.getTargetException().getClass() + ": " + invocationtargetexception1.getTargetException().getMessage();
            return browser;
        }
        obj3 = getFileType.invoke(null, new Object[] {
            obj2
        });
        if(!"FNDR".equals(obj3.toString()))
            continue; /* Loop/switch isn't completed */
        obj4 = getFileCreator.invoke(null, new Object[] {
            obj2
        });
        if(!"MACS".equals(obj4.toString()))
            continue; /* Loop/switch isn't completed */
        browser = ((File) (obj2)).toString();
        return browser;
        i++;
          goto _L9
_L8:
        browser = null;
        break; /* Loop/switch isn't completed */
_L4:
        browser = "";
        break; /* Loop/switch isn't completed */
_L5:
        browser = "cmd.exe";
        break; /* Loop/switch isn't completed */
_L6:
        browser = "command.com";
        break; /* Loop/switch isn't completed */
_L1:
        browser = "netscape";
        return browser;
    }

    public static void openURL(String s)
        throws IOException
    {
        Object obj;
        if(!loadedWithoutErrors)
            throw new IOException("Exception in finding browser: " + errorMessage);
        obj = locateBrowser();
        if(obj == null)
            throw new IOException("Unable to locate browser: " + errorMessage);
        jvm;
        JVM INSTR tableswitch -1 6: default 755
    //                   -1 634
    //                   0 120
    //                   1 280
    //                   2 755
    //                   3 305
    //                   4 440
    //                   5 525
    //                   6 525;
           goto _L1 _L2 _L3 _L4 _L1 _L5 _L6 _L7 _L7
_L3:
        Object obj1 = null;
        Object obj3;
        try
        {
            Object obj2 = aeDescConstructor.newInstance(new Object[] {
                s
            });
            putParameter.invoke(obj, new Object[] {
                keyDirectObject, obj2
            });
            sendNoReply.invoke(obj, new Object[0]);
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            throw new IOException("InvocationTargetException while creating AEDesc: " + invocationtargetexception.getMessage());
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new IOException("IllegalAccessException while building AppleEvent: " + illegalaccessexception.getMessage());
        }
        catch(InstantiationException instantiationexception)
        {
            throw new IOException("InstantiationException while creating AEDesc: " + instantiationexception.getMessage());
        }
        obj3 = null;
        obj = null;
        break; /* Loop/switch isn't completed */
        Exception exception;
        exception;
        Object obj4 = null;
        obj = null;
        throw exception;
_L4:
        Runtime.getRuntime().exec(new String[] {
            (String)obj, s
        });
        break; /* Loop/switch isn't completed */
_L5:
        int ai[] = new int[1];
        int i = ICStart(ai, 0);
        if(i == 0)
        {
            int ai1[] = {
                0
            };
            byte abyte0[] = s.getBytes();
            int ai2[] = {
                abyte0.length
            };
            i = ICLaunchURL(ai[0], new byte[] {
                0
            }, abyte0, abyte0.length, ai1, ai2);
            if(i == 0)
                ICStop(ai);
            else
                throw new IOException("Unable to launch URL: " + i);
        } else
        {
            throw new IOException("Unable to create an Internet Config instance: " + i);
        }
        break; /* Loop/switch isn't completed */
_L6:
        try
        {
            openURL.invoke(null, new Object[] {
                s
            });
        }
        catch(InvocationTargetException invocationtargetexception1)
        {
            throw new IOException("InvocationTargetException while calling openURL: " + invocationtargetexception1.getMessage());
        }
        catch(IllegalAccessException illegalaccessexception1)
        {
            throw new IOException("IllegalAccessException while calling openURL: " + illegalaccessexception1.getMessage());
        }
        break; /* Loop/switch isn't completed */
_L7:
        Process process = Runtime.getRuntime().exec(new String[] {
            (String)obj, "/c", "start", "\"\"", '"' + s + '"'
        });
        try
        {
            process.waitFor();
            process.exitValue();
        }
        catch(InterruptedException interruptedexception)
        {
            throw new IOException("InterruptedException while launching browser: " + interruptedexception.getMessage());
        }
        break; /* Loop/switch isn't completed */
_L2:
        Process process1 = Runtime.getRuntime().exec(new String[] {
            (String)obj, "-remote", "'openURL(" + s + ")'"
        });
        try
        {
            int j = process1.waitFor();
            if(j != 0)
                Runtime.getRuntime().exec(new String[] {
                    (String)obj, s
                });
        }
        catch(InterruptedException interruptedexception1)
        {
            throw new IOException("InterruptedException while launching browser: " + interruptedexception1.getMessage());
        }
        break; /* Loop/switch isn't completed */
_L1:
        Runtime.getRuntime().exec(new String[] {
            (String)obj, s
        });
    }

    private static int jvm;
    private static Object browser;
    private static boolean loadedWithoutErrors;
    private static Class mrjFileUtilsClass;
    private static Class mrjOSTypeClass;
    private static Class aeDescClass;
    private static Constructor aeTargetConstructor;
    private static Constructor appleEventConstructor;
    private static Constructor aeDescConstructor;
    private static Method findFolder;
    private static Method getFileCreator;
    private static Method getFileType;
    private static Method openURL;
    private static Method makeOSType;
    private static Method putParameter;
    private static Method sendNoReply;
    private static Object kSystemFolderType;
    private static Integer keyDirectObject;
    private static Integer kAutoGenerateReturnID;
    private static Integer kAnyTransactionID;
    private static Object linkage;
    private static final String JDirect_MacOSX = "/System/Library/Frameworks/Carbon.framework/Frameworks/HIToolbox.framework/HIToolbox";
    private static final int MRJ_2_0 = 0;
    private static final int MRJ_2_1 = 1;
    private static final int MRJ_3_0 = 3;
    private static final int MRJ_3_1 = 4;
    private static final int WINDOWS_NT = 5;
    private static final int WINDOWS_9x = 6;
    private static final int OTHER = -1;
    private static final String FINDER_TYPE = "FNDR";
    private static final String FINDER_CREATOR = "MACS";
    private static final String GURL_EVENT = "GURL";
    private static final String FIRST_WINDOWS_PARAMETER = "/c";
    private static final String SECOND_WINDOWS_PARAMETER = "start";
    private static final String THIRD_WINDOWS_PARAMETER = "\"\"";
    private static final String NETSCAPE_REMOTE_PARAMETER = "-remote";
    private static final String NETSCAPE_OPEN_PARAMETER_START = "'openURL(";
    private static final String NETSCAPE_OPEN_PARAMETER_END = ")'";
    private static String errorMessage;

    static 
    {
        loadedWithoutErrors = true;
        String s = System.getProperty("os.name");
        if(s.startsWith("Mac OS"))
        {
            String s1 = System.getProperty("mrj.version");
            String s2 = s1.substring(0, 3);
            try
            {
                double d = Double.valueOf(s2).doubleValue();
                if(d == 2D)
                    jvm = 0;
                else
                if(d >= 2.1000000000000001D && d < 3D)
                    jvm = 1;
                else
                if(d == 3D)
                    jvm = 3;
                else
                if(d >= 3.1000000000000001D)
                {
                    jvm = 4;
                } else
                {
                    loadedWithoutErrors = false;
                    errorMessage = "Unsupported MRJ version: " + d;
                }
            }
            catch(NumberFormatException numberformatexception)
            {
                loadedWithoutErrors = false;
                errorMessage = "Invalid MRJ version: " + s1;
            }
        } else
        if(s.startsWith("Windows"))
        {
            if(s.indexOf("9") != -1)
                jvm = 6;
            else
                jvm = 5;
        } else
        {
            jvm = -1;
        }
        if(loadedWithoutErrors)
            loadedWithoutErrors = loadClasses();
    }
}
