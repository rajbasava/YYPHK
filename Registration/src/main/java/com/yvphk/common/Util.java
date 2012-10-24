/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.common;

import com.yvphk.model.form.BaseForm;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Util
{

    public static boolean nullOrEmptyOrBlank (String toValidate)
    {
        if (toValidate == null || toValidate.equalsIgnoreCase("")) {
            return true;
        }

        return false;
    }

    public static Method getDeclaredSetter (Class clazz, String fieldName, Class fieldType)
    {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("set" + fieldName, fieldType);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
        return method;

    }

    public static Object getFieldValue (Object obj, String fieldName)
    {
        if (obj == null || Util.nullOrEmptyOrBlank(fieldName)) {
            return null;
        }

        Object result = null;

        try {
            Method method = obj.getClass().getDeclaredMethod("get" + fieldName);
            if (method != null) {
                result = method.invoke(obj);
            }
        }
        catch (NoSuchMethodException e) {
            return null;
        }
        catch (InvocationTargetException e) {
            return null;
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return result;
    }

    public static void write (HttpServletResponse response, Workbook workbook)
    {

        try {
            // Retrieve the output stream
            ServletOutputStream outputStream = response.getOutputStream();
            // Write to the output stream
            workbook.write(outputStream);
            // Flush the stream
            outputStream.flush();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean evaluate (String expression, BaseForm form)
    {
        boolean result = false;
        try {
            Object obj = Ognl.getValue(expression, form);
            if (obj instanceof Boolean) {
                result = ((Boolean)obj).booleanValue();
            }
        }
        catch (OgnlException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object getDottedFieldValue (String dottedFieldPath, Object obj)
    {
        Object result = null;
        try {
            result = Ognl.getValue(dottedFieldPath, obj);
        }
        catch (OgnlException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCapitalizedFieldName (String dottedFieldPath)
    {
        if (Util.nullOrEmptyOrBlank(dottedFieldPath)) {
            return null;
        }

        String lastField = dottedFieldPath.substring(dottedFieldPath.lastIndexOf(".")+1);
        lastField = lastField.substring(0,1).toUpperCase() + lastField.substring(1).toLowerCase();
        return lastField;
    }

    public static Class loadClass (String classType)
    {
        Class clazz = null;
        try {
            clazz = Class.forName(classType);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
         return clazz;

    }

}