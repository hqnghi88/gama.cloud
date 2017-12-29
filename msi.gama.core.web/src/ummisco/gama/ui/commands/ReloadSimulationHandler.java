/*********************************************************************************************
 *
 * 'ReloadSimulationHandler.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.commands;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

import msi.gama.core.web.editor.GAMAHelper;
//import ummisco.gama.ui.bindings.GamaKeyBindings;

public class ReloadSimulationHandler extends AbstractHandler implements IElementUpdater {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		// GAMA.pauseFrontmostExperiment();
		GAMAHelper.reloadFrontmostExperiment();
		return this;
	}

	@Override
	public void updateElement(final UIElement element, final Map parameters) {
//		element.setTooltip("Reloads the current experiment (" + GamaKeyBindings.RELOAD_STRING + ")");
//		element.setText("Reload Experiment (" + GamaKeyBindings.RELOAD_STRING + ")");
	}

}