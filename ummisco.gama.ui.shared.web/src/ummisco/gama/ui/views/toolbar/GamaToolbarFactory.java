/*******************************************************************************************************
 *
 * ummisco.gama.ui.views.toolbar.GamaToolbarFactory.java, in plugin ummisco.gama.ui.shared, is part of the source code
 * of the GAMA modeling and simulation platform (v. 1.8)
 * 
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package ummisco.gama.ui.views.toolbar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchSite;

import msi.gama.common.interfaces.IGamaView;
import msi.gama.common.preferences.GamaPreferences;
import msi.gama.common.preferences.Pref;
import msi.gaml.types.IType;
import ummisco.gama.ui.controls.ITooltipDisplayer;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.resources.IGamaColors;

/**
 * The class GamaToolbarFactory.
 *
 * @author drogoul
 * @since 19 janv. 2012
 *
 */
public class GamaToolbarFactory {

	public static final Pref<Boolean> REDUCED_VIEW_TOOLBAR_HEIGHT = GamaPreferences
			.create("pref_view_toolbar_height", "Reduce the height of views' toolbars", false, IType.BOOL, false)
			.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.APPEARANCE);

	public static class GamaComposite extends Composite {

		ITooltipDisplayer displayer;

		public GamaComposite(final Composite parent, final ITooltipDisplayer displayer) {
			super(parent, SWT.None);
			this.displayer = displayer;
		}

	}

	public static ITooltipDisplayer findTooltipDisplayer(final Control c) {
		final GamaComposite gc = findGamaComposite(c);
		return gc == null ? null : gc.displayer;
	}

	public static GamaComposite findGamaComposite(final Control c) {
		if (c instanceof Shell) { return null; }
		if (c instanceof GamaComposite) { return (GamaComposite) c; }
		return findGamaComposite(c.getParent());
	}

	public static abstract class ToggleAction extends Action {

		ToggleAction() {
			super("Toggle toolbar", IAction.AS_PUSH_BUTTON);
			setId("toolbar.toggle");
			setIcon(true);
		}

		protected abstract void setIcon(boolean show);

	}

	public static class ToggleSideControls extends Action {

		boolean show = true;

		ToggleSideControls() {
			super("Toggle Side Controls", IAction.AS_PUSH_BUTTON);
			setIcon();
		}

		protected void setIcon() {
			setImageDescriptor(GamaIcons.create("action.toolbar.toggle.side2").descriptor());
		}

	}

	public static class ToggleOverlay extends Action {

		boolean show = true;

		ToggleOverlay() {
			super("Toggle Overlay", IAction.AS_PUSH_BUTTON);
			setIcon();
		}

		protected void setIcon() {
			setImageDescriptor(GamaIcons.create("action.toolbar.toggle.overlay2").descriptor());
		}

	}

	public static int TOOLBAR_HEIGHT = 24; // CORE_ICONS_HEIGHT.getValue();
	public static int TOOLBAR_SEP = 4;

	private static Composite createIntermediateCompositeFor(final IToolbarDecoratedView view,
			final Composite composite) {
		// First, we create the background composite
		final FillLayout backgroundLayout = new FillLayout(SWT.VERTICAL);
		backgroundLayout.marginHeight = 0;
		backgroundLayout.marginWidth = 0;
		composite.setLayout(backgroundLayout);
		Composite parentComposite;
		if (view instanceof ITooltipDisplayer) {
			parentComposite = new GamaComposite(composite, (ITooltipDisplayer) view);
		} else {
			parentComposite = new Composite(composite, SWT.None);
		}
		final GridLayout parentLayout = new GridLayout(1, false);
		parentLayout.horizontalSpacing = 0;
		parentLayout.verticalSpacing = 0;
		parentLayout.marginHeight = 0;
		parentLayout.marginWidth = 0;
		parentComposite.setLayout(parentLayout);
		return parentComposite;
	}

	public static GridData getLayoutDataForChild() {
		final GridData result = new GridData(SWT.FILL, SWT.FILL, true, true);
		result.verticalSpan = 5;
		return result;
	}

	public static FillLayout getLayoutForChild() {
		final FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		return layout;
	}

	public static Composite createToolbarComposite(final Composite composite) {
		final Composite toolbarComposite = new Composite(composite, SWT.None);
		final GridData toolbarCompositeData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		toolbarComposite.setLayoutData(toolbarCompositeData2);
		final GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginWidth = 0;
		final int margin = REDUCED_VIEW_TOOLBAR_HEIGHT.getValue() ? -1 : 0;
		layout.marginTop = margin;
		layout.marginBottom = margin;
		layout.marginHeight = margin;
		toolbarComposite.setLayout(layout);
		toolbarComposite.setBackground(IGamaColors.WHITE.color());

		return toolbarComposite;

	}

	public static Composite createToolbars(final IToolbarDecoratedView view, final Composite composite) {
		final Composite intermediateComposite = createIntermediateCompositeFor(view, composite);
		final Composite toolbarComposite = createToolbarComposite(intermediateComposite);
		final Composite childComposite = new Composite(intermediateComposite, SWT.None);
		childComposite.setLayoutData(getLayoutDataForChild());
		childComposite.setLayout(getLayoutForChild());

		final GamaToolbar2 tb =
				new GamaToolbar2(toolbarComposite, SWT.FLAT | SWT.HORIZONTAL | SWT.NO_FOCUS, TOOLBAR_HEIGHT);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.minimumWidth = TOOLBAR_HEIGHT * 2;
		tb.setLayoutData(data);
		composite.addDisposeListener(e -> disposeToolbar(view, tb));
		buildToolbar(view, tb);

		// Creating the toggles
		final ToggleAction toggle = new ToggleAction() {

			@Override
			public void run() {
				final boolean show = !tb.isVisible();
				tb.setVisible(show);
				((GridData) tb.getParent().getLayoutData()).exclude = !show;
				tb.getParent().setVisible(show);
				tb.getParent().getParent().layout();
				setIcon(show);
			}

			@Override
			protected void setIcon(final boolean show) {
				setImageDescriptor(GamaIcons
						.create(show ? "action.toolbar.toggle.small2" : "action.toolbar.toggle.small3").descriptor());
			}
		};

		tb.setToogleAction(toggle);

		// Install the toogles in the view site
		final IWorkbenchSite site = view.getSite();
		if (site instanceof IViewSite) {
			final IToolBarManager tm = ((IViewSite) site).getActionBars().getToolBarManager();
			tm.add(toggle);
			if (view instanceof IGamaView.Display) {
				final Action toggleSideControls = new ToggleSideControls() {
					@Override
					public void run() {
						((IGamaView.Display) view).toggleSideControls();
					}
				};

				final Action toggleOverlay = new ToggleOverlay() {
					@Override
					public void run() {
						((IGamaView.Display) view).toggleOverlay();
					}
				};
				tm.add(toggleOverlay);
				tm.add(toggleSideControls);
			}
			tm.update(true);
		}

		if (!view.toolbarVisible()) {
			toggle.run();
			// tb.setVisible(false);
			// ((GridData) tb.getParent().getLayoutData()).exclude = true;
			// tb.getParent().setVisible(false);
			// tb.getParent().getParent().layout();
			// toggle.setIcon(false);
		}
		return childComposite;
	}

	public static void disposeToolbar(final IToolbarDecoratedView view, final GamaToolbar2 tb) {
		if (tb != null && !tb.isDisposed()) {
			tb.dispose();

		}
	}

	public static void buildToolbar(final IToolbarDecoratedView view, final GamaToolbar2 tb) {
		if (view instanceof IToolbarDecoratedView.Sizable) {
			final FontSizer fs = new FontSizer((IToolbarDecoratedView.Sizable) view);
			fs.install(tb);
		}
		if (view instanceof IToolbarDecoratedView.Pausable) {
			final FrequencyController fc = new FrequencyController((IToolbarDecoratedView.Pausable) view);
			fc.install(tb);
		}
		if (view instanceof IToolbarDecoratedView.Zoomable) {
			final ZoomController zc = new ZoomController((IToolbarDecoratedView.Zoomable) view);
			zc.install(tb);
		}
		if (view instanceof IToolbarDecoratedView.Colorizable) {
			final BackgroundChooser b = new BackgroundChooser((IToolbarDecoratedView.Colorizable) view);
			b.install(tb);
		}
		if (view instanceof IToolbarDecoratedView.CSVExportable) {
			final CSVExportationController csv =
					new CSVExportationController((IToolbarDecoratedView.CSVExportable) view);
			csv.install(tb);
		}

		view.createToolItems(tb);
		tb.refresh(true);
	}

}
