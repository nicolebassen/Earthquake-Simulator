// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IntHashtableEntry.java

package Acme;


class IntHashtableEntry
{

    IntHashtableEntry()
    {
    }

    protected Object clone()
    {
        IntHashtableEntry inthashtableentry = new IntHashtableEntry();
        inthashtableentry.hash = hash;
        inthashtableentry.key = key;
        inthashtableentry.value = value;
        inthashtableentry.next = next == null ? null : (IntHashtableEntry)next.clone();
        return inthashtableentry;
    }

    int hash;
    int key;
    Object value;
    IntHashtableEntry next;
}
