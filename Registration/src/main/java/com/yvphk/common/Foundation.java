/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.common;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Foundation
{
    F1("YOGA VIDYA PRANIC HEALING FOUNDATION CALICUT - KERALA", "Yoga Vidya Pranic Healing Foundation Calicut - Kerala"),
    F2("YOGA VIDYA PRANIC HEALING FOUNDATION TRUST - UTTAR PRADESH, VARANASI", "Yoga Vidya Pranic Healing Foundation Trust - Uttar Pradesh, Varanasi"),
    F3("GMCKS PRANIC HEALING FOUNDATION OF RAJASTHAN", "GMCKS Pranic Healing Foundation of Rajasthan"),
    F4("PRANIC HEALING GYAN PRACHAR KENDRA, MUMBAI", "Pranic Healing Gyan Prachar Kendra, Mumbai"),
    F5("YOGA VIDYA PRANIC HEALING FOUNDATION OF KARNATAKA", "Yoga Vidya Pranic Healing Foundation of Karnataka"),
    F6("YOGA VIDYA PRANIC HEALING TRUST, MUMBAI", "Yoga Vidya Pranic Healing Trust, Mumbai"),
    F7("PRANIC HEALING TRUST - TAMILNADU", "Pranic Healing Trust - Tamilnadu"),
    F8("YOGA VIDYA PRANIC HEALING FOUNDATION OF WEST BENGAL", "Yoga Vidya Pranic Healing Foundation of West Bengal"),
    F9("YOGA VIDYA PRANIC HEALING FOUNDATION COCHIN, KERALA - KOTTAYAM", "Yoga Vidya Pranic Healing Foundation Cochin, Kerala - Kottayam"),
    F10("PRANIC HEALING FOUNDATION, SECUNDERABAD", "Pranic Healing Foundation, Secunderabad"),
    F11("MCKS PRANIC HEALING TRUST - WEST BENGAL", "MCKS Pranic Healing Trust - West Bengal"),
    F12("PRANIC HEALING HOME, CHENNAI", "Pranic Healing Home, Chennai"),
    F13("MCKS YOGA VIDYA PRANIC HEALING TRUST - NEW DELHI", "MCKS Yoga Vidya Pranic Healing Trust - New Delhi"),
    F14("MCKS YOGA VIDYA PRANIC HEALING TRUST OF UP, LUCKNOW", "MCKS Yoga Vidya Pranic Healing Trust of UP, Lucknow"),
    Others("Others", "Others");

    private String key;
    private String name;

    private static Map<String, String> allFoundations;

    private Foundation (String key, String name)
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

    public static Map<String, String> allFoundations ()
    {
        if (allFoundations == null) {
            allFoundations = new LinkedHashMap<String, String>();

            allFoundations.put(Foundation.F1.getKey(),Foundation.F1.getName());
            allFoundations.put(Foundation.F2.getKey(),Foundation.F2.getName());
            allFoundations.put(Foundation.F3.getKey(),Foundation.F3.getName());
            allFoundations.put(Foundation.F4.getKey(),Foundation.F4.getName());
            allFoundations.put(Foundation.F5.getKey(),Foundation.F5.getName());
            allFoundations.put(Foundation.F6.getKey(),Foundation.F6.getName());
            allFoundations.put(Foundation.F7.getKey(),Foundation.F7.getName());
            allFoundations.put(Foundation.F8.getKey(),Foundation.F8.getName());
            allFoundations.put(Foundation.F9.getKey(),Foundation.F9.getName());
            allFoundations.put(Foundation.F10.getKey(),Foundation.F10.getName());
            allFoundations.put(Foundation.F11.getKey(),Foundation.F11.getName());
            allFoundations.put(Foundation.F12.getKey(),Foundation.F12.getName());
            allFoundations.put(Foundation.F13.getKey(),Foundation.F13.getName());
            allFoundations.put(Foundation.F14.getKey(),Foundation.F14.getName());
            allFoundations.put(Foundation.Others.getKey(),Foundation.Others.getName());
        }

        return allFoundations;
    }

    public static String getName (String key)
    {
        return allFoundations().get(key);
    }
}
