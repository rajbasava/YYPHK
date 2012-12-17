/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.common;

import java.util.LinkedHashMap;
import java.util.Map;

public enum SeatingType
{
    Numerical("0", "Numerical - 1,2, ..."),
    AlphaNumerical("1", "AlphaNumerical - A1, A2, ...");

    private String key;
    private String name;

    private static Map<String, String> allSeatingTypes;

    private SeatingType (String key, String name)
    {
        this.key = key;
        this.name = name;
    }

    public String getKey ()
    {
        return key;
    }

    public String getName ()
    {
        return name;
    }

    public static Map<String, String> allSeatingTypes ()
    {
        if (allSeatingTypes == null) {
            allSeatingTypes = new LinkedHashMap<String, String>();

            allSeatingTypes.put(SeatingType.Numerical.getKey(),
                    SeatingType.Numerical.getName());
            allSeatingTypes.put(SeatingType.AlphaNumerical.getKey(),
                    SeatingType.AlphaNumerical.getName());
        }

        return allSeatingTypes;
    }

    public static String getName (String key)
    {
        return allSeatingTypes().get(key);
    }

}
