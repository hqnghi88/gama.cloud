/*********************************************************************************************
 *
 * 'GamaSourceViewer.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.editor.participative;


import org.dslforge.styledtext.jface.TextViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xtext.resource.XtextResource;

import msi.gama.lang.gaml.resource.GamlResourceServices;
import msi.gama.lang.gaml.validation.IGamlBuilderListener;
import ummisco.gama.participative.EtherpadBasicText;

/**
 * The class GamaSourceViewer.
 *
 * @author drogoul
 * @since 12 août 2016
 *
 */
public class EtherpadGamaTextViewer extends TextViewer {
	final XtextResource fileURI;
	private IGamlBuilderListener resourceListener;
	private boolean isOverviewVisible;

	/**
	 * @param parent
	 * @param ruler
	 * @param overviewRuler
	 * @param showsAnnotationOverview
	 * @param styles
	 */
//	public GamaTextViewer(final Composite parent, final IVerticalRuler ruler, final IOverviewRuler overviewRuler,
//			final boolean showsAnnotationOverview, final int styles) {
//		super(parent, styles);
//		isOverviewVisible = showsAnnotationOverview && overviewRuler != null;
//	}

	public EtherpadGamaTextViewer(EtherpadBasicText textWidget, Composite parent, int styles, final XtextResource furi) {
		super(parent, styles);
		fileURI=furi;
//		isOverviewVisible = showsAnnotationOverview && overviewRuler != null;
	}

	@Override
	protected void handleDispose() {
		GamlResourceServices.removeResourceListener(resourceListener);
		super.handleDispose();
	}

	public void showAnnotationsOverview(final boolean show) {
		isOverviewVisible = show;
	}

	public boolean isOverviewVisible() {
		return isOverviewVisible;
	}

	/**
	 * @param gamlEditor
	 */
	public void setResourceListener(final IGamlBuilderListener listener) {
		this.resourceListener = listener;
		GamlResourceServices.addResourceListener(fileURI.getURI(), listener);

//		((IXtextDocument) getDocument()).readOnly(new CancelableUnitOfWork<Object, XtextResource>() {
//
//			@Override
//			public Object exec(final XtextResource state, final CancelIndicator cancelIndicator) throws Exception {
//				if (state != null)
//					GamlResourceServices.addResourceListener(state.getURI(), listener);
//				return null;
//			}
//		});
	}

}