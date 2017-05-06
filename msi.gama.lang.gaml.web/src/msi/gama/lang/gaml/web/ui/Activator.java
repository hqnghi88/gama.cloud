/*********************************************************************************************
 *
 * 'Activator.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import msi.gama.lang.gaml.web.ui.utils.SwtGui;
import msi.gama.runtime.GAMA;

public class Activator extends AbstractUIPlugin {

	public Activator() {
		if (GAMA.getRegularGui() == null) {
			GAMA.setRegularGui(new SwtGui());
		}

	}

}
