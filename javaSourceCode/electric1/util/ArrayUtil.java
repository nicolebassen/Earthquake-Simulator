// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ArrayUtil.java

package electric1.util;

import java.lang.reflect.Array;

// Referenced classes of package electric1.util:
//            IComparator

public final class ArrayUtil
{

    public ArrayUtil()
    {
    }

    public static Object addElement(Object obj, Object obj1)
    {
        int i = Array.getLength(obj);
        Object obj2 = Array.newInstance(obj.getClass().getComponentType(), i + 1);
        System.arraycopy(obj, 0, obj2, 0, i);
        Array.set(obj2, i, obj1);
        return obj2;
    }

    public static Object addElements(Object obj, Object obj1)
    {
        int i = Array.getLength(obj);
        int j = Array.getLength(obj1);
        Object obj2 = Array.newInstance(obj.getClass().getComponentType(), i + j);
        System.arraycopy(obj, 0, obj2, 0, i);
        System.arraycopy(obj1, 0, obj2, i, j);
        return obj2;
    }

    public static boolean equals(Object aobj[], Object aobj1[])
    {
        if(aobj.length != aobj1.length)
            return false;
        for(int i = 0; i < aobj.length; i++)
        {
            Object obj = aobj[i];
            Object obj1 = aobj1[i];
            if(obj != null && obj1 != null)
            {
                if(!obj.equals(obj1))
                    return false;
                continue;
            }
            if(obj != null || obj1 != null)
                return false;
        }

        return true;
    }

    public static boolean equals(Object obj, Object obj1)
    {
        if(obj == null && obj1 == null)
            return true;
        if(obj == null || obj1 == null)
            return false;
        else
            return obj.equals(obj1);
    }

    public static int indexOf(int i, int ai[])
    {
        for(int j = 0; j < ai.length; j++)
            if(ai[j] == i)
                return j;

        return -1;
    }

    public static int indexOf(Object obj, Object aobj[])
    {
        for(int i = 0; i < aobj.length; i++)
            if(aobj[i] == obj)
                return i;

        return -1;
    }

    public static Object removeElement(Object obj, Object obj1)
    {
        int i = Array.getLength(obj);
        for(int j = 0; j < i; j++)
            if(obj1.equals(Array.get(obj, j)))
                return removeElementAt(obj, j);

        return obj;
    }

    public static Object removeElementAt(Object obj, int i)
    {
        int j = Array.getLength(obj);
        Object obj1 = Array.newInstance(obj.getClass().getComponentType(), j - 1);
        System.arraycopy(obj, 0, obj1, 0, i);
        System.arraycopy(obj, i + 1, obj1, i, j - i - 1);
        return obj1;
    }

    public static void sort(Object aobj[], IComparator icomparator)
    {
        for(int i = aobj.length - 1; i > 0; i--)
        {
            for(int j = 0; j < i; j++)
                if(icomparator.compare(aobj[j], aobj[j + 1]) > 0)
                {
                    Object obj = aobj[j + 1];
                    aobj[j + 1] = aobj[j];
                    aobj[j] = obj;
                }

        }

    }

    public static byte[] toBytes(Object obj)
    {
        if(BYTE_ARRAY.isInstance(obj))
            return (byte[])obj;
        if(obj == null)
            return "null".getBytes();
        else
            return obj.toString().getBytes();
    }

    static final Class BYTE_ARRAY = (new byte[0]).getClass();

}
