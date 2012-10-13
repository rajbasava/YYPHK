/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.form.ImportFile;
import com.yvphk.model.form.Login;

public interface ImportService
{
    public void processImportFile (ImportFile importFile, Login login);

}
