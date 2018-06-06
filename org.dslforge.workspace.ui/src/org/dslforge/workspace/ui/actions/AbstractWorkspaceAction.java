/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
package org.dslforge.workspace.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

public abstract class AbstractWorkspaceAction extends AbstractWindowActionDelegate implements IObjectActionDelegate{

	private ISelection selection;
	
	private IWorkbenchWindow window;
	
	@Override
	public abstract void run(IAction action);
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.setSelection(selection);
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		IWorkbenchWindow workbenchWindow = targetPart.getSite().getWorkbenchWindow();
		setWindow(workbenchWindow);
		setSelection(workbenchWindow.getSelectionService().getSelection());
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public IWorkbenchWindow getWindow() {
		return window;
	}

	public void setWindow(IWorkbenchWindow window) {
		this.window = window;
	}
	
	public void setSizeAndLocation(WizardDialog wizardDialog) {
		wizardDialog.getShell().setSize(600, 500);
		Shell activeShell = this.window.getShell();
		Rectangle bounds = activeShell.getBounds();
		Rectangle rect = wizardDialog.getShell().getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		wizardDialog.getShell().setLocation(x, y);
	}
}
