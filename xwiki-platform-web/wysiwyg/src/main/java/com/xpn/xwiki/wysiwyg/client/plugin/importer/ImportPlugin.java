/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.wysiwyg.client.plugin.importer;

import com.google.gwt.user.client.Window;
import com.xpn.xwiki.wysiwyg.client.Wysiwyg;
import com.xpn.xwiki.wysiwyg.client.editor.Strings;
import com.xpn.xwiki.wysiwyg.client.plugin.importer.ui.ImportWizard;
import com.xpn.xwiki.wysiwyg.client.plugin.importer.ui.ImportWizard.ImportWizardStep;
import com.xpn.xwiki.wysiwyg.client.plugin.internal.AbstractPlugin;
import com.xpn.xwiki.wysiwyg.client.util.Config;
import com.xpn.xwiki.wysiwyg.client.widget.rta.RichTextArea;
import com.xpn.xwiki.wysiwyg.client.widget.rta.cmd.Command;
import com.xpn.xwiki.wysiwyg.client.widget.wizard.Wizard;
import com.xpn.xwiki.wysiwyg.client.widget.wizard.WizardListener;

/**
 * Plugin responsible for importing external content into wysiwyg editor.
 * 
 * @version $Id$
 */
public class ImportPlugin extends AbstractPlugin implements WizardListener
{
    /**
     * Import wizard.
     */
    private Wizard importWizard;

    /**
     * Import menu extension.
     */
    private ImportMenuExtension importMenuExtension;

    /**
     * {@inheritDoc}
     */
    public void init(Wysiwyg wysiwyg, RichTextArea textArea, Config config)
    {
        super.init(wysiwyg, textArea, config);
        this.importMenuExtension = new ImportMenuExtension(this);
        getUIExtensionList().add(importMenuExtension);
    }

    /**
     * Method invoked by {@link ImportMenuExtension} when "Import -> Office File" menu item is clicked.
     */
    public void onImportOfficeFile()
    {
        boolean isOpenOfficeServerConnected =
            getConfig().getParameter("openofficeServerConnected", "false").equals("true");
        if (isOpenOfficeServerConnected) {
            getImportWizard().start(ImportWizardStep.OFFICE_FILE.toString(), null);
        } else {
            Window.alert(Strings.INSTANCE.importOfficeFileFeatureNotAvailable());
        }
    }
    
    /**
     * Method invoked by {@link ImportMenuExtension} when "Import -> Office Content" menu item is clicked.
     */
    public void onImportOfficePaste()
    {
        getImportWizard().start(ImportWizardStep.OFFICE_PASTE.toString(), null);
    }

    /**
     * {@inheritDoc}
     */
    public void onCancel(Wizard sender)
    {
        getTextArea().setFocus(true);
    }

    /**
     * {@inheritDoc}
     */
    public void onFinish(Wizard sender, Object result)
    {
        getTextArea().setFocus(true);
        getTextArea().getCommandManager().execute(Command.INSERT_HTML, result.toString());
    }

    /**
     * {@inheritDoc}
     */
    public void destroy()
    {
        importMenuExtension.destroy();
        super.destroy();
    }

    /**
     * Creates and returns the import wizard.
     * 
     * @return import wizard instance.
     */
    private Wizard getImportWizard()
    {
        if (null == importWizard) {
            importWizard = new ImportWizard(getConfig());
            importWizard.addWizardListener(this);
        }
        return importWizard;
    }
}
