/*********************************************************************************************
 *
 * 'RuntimeExceptionHandlerFactory.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.factories;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import msi.gama.common.interfaces.IRuntimeExceptionHandler;
import msi.gama.lang.gaml.web.ui.commands.RuntimeExceptionHandler;

public class RuntimeExceptionHandlerFactory extends AbstractServiceFactory {

	IRuntimeExceptionHandler handler;

	public IRuntimeExceptionHandler getHandler() {
		if (handler == null) {
			handler = new RuntimeExceptionHandler();
		}
		return handler;
	}

	@Override
	public Object create(final Class serviceInterface, final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		return getHandler();
	}

}
