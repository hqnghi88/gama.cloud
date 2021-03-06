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
package msi.gama.lang.gaml.web.workbench;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.dslforge.workspace.jpa.database.User;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import msi.gama.application.Application;
import msi.gama.lang.gaml.web.workspace.ui.DummyCallbackHandler;
import msi.gama.lang.gaml.web.workspace.ui.DummyLoginModule;
import msi.gama.rap.oauth.TokenCallbackServiceHandler;
import ummisco.gama.ui.resources.IGamaColors;

/**
 * Basic Workbench UI entry point
 *
 */
public class BasicWorkbench extends AbstractEntryPoint {
	// private static final String DEMO_PRESENTATION =
	// "org.dslforge.texteditor.demo.theme";
	// abstract class SessionRunnable implements Runnable {
	//
	// final private UISession session;
	// final Client cli;
	//
	// public SessionRunnable() {
	// session = RWT.getUISession();
	// cli = RWT.getClient();
	// System.out.println("\n Client " + cli);
	// }
	//
	// @Override
	// public void run() {
	// session.exec(new Runnable() {
	// @Override
	// public void run() {
	// SessionRunnable.this.runInSession();
	// }
	// });
	// }
	//
	// abstract void runInSession();
	// }

	public static HashMap<String, JavaScriptExecutor> executor = new HashMap<String, JavaScriptExecutor>();

	boolean enableLoggin = true;
//	boolean enableLoggin = false;

	boolean is_controller = false;
	public static String offline_context = "offline_GamaWeb";
	public static String controller_context = "controller_GamaWeb";
	public static String user_context_prefix = "user_GamaWeb";

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (null != ip && !"".equals(ip.trim()) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (null != ip && !"".equals(ip.trim()) && !"unknown".equalsIgnoreCase(ip)) {
			// get first ip from proxy ip
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	public void postLoggedIn(final String uid) {
		RWT.getUISession().setAttribute("user", uid);

//		if (uid.equals(""+uid)) {
		if (executor.get(uid) != null) {
//				WorkbenchHelper.workbench.get(uid).close();
			JavaScriptExecutor ex = executor.get(uid);
			System.out.println("script reload  " + ex);
			ex.execute("window.location.reload(true);");
			// ex.execute("var myUrl = window.location;\r\n" +
			// "window.location.replace(myUrl);");
			ex = null;
			executor.put(uid, ex);
			RWT.getApplicationContext().setAttribute("logged_" + uid, null);// "restart");
			// return 0;
			// MessageDialog.openInformation(Display.getDefault().getActiveShell(),
			// "Information", "This account is currently used
			// somewhere, RETRY!");
		}
//		} 
//		else {
//			if (RWT.getApplicationContext().getAttribute("logged_" + uid) != null) {
//				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information",
//						"This account is currently used somewhere, please try again later!");
//				logged = false;
//			}
//		}
	}

	public static void execBash(final String sc) {

		ProcessBuilder processBuilder = new ProcessBuilder();

		// -- Linux --
		// Run a shell command
		processBuilder.command("bash", "-c", sc);

		// Run a shell script
		// processBuilder.command("path/to/hello.sh");

		// -- Windows --

		// Run a command
		// processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");

		// Run a bat file
		// processBuilder.command("C:\\Users\\mkyong\\hello.bat");

		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
			} else {
				System.out.println("abnormal");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void doWait() {
		Display d = Display.getDefault();
		Shell sh = new Shell(d);
		sh.setSize(500, 50);
		Label lb = new Label(sh, SWT.NONE);
		lb.setForeground(IGamaColors.BLACK.color());
		lb.setText("Creating resources, please wait 20s ");
		lb.setBounds(10, 25, 450, 20);

		d.syncExec(new Runnable() {

			public void run() {
				sh.open();
			}
		});
		try {
			ModalContext.setAllowReadAndDispatch(true); // Works for now.
			ModalContext.run(new IRunnableWithProgress() {

				public void run(final IProgressMonitor monitor) {
					int i = 0;
					while (i < 20) {
						i++;
						d.syncExec(new Runnable() {

							public void run() {

								String ls = lb.getText();
								lb.setText(ls + "!");
							}
						});
						try {
//								System.out.println("waiting");
							Thread.sleep(1000);
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
					d.syncExec(new Runnable() {

						public void run() {
							sh.close();
						}
					});
				}
			}, true, new NullProgressMonitor(), d);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int createUI() {
//		try {
//			enableLoggin=InetAddress.getLocalHost().getHostName().equals("dell3847")?false:true;
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(getIpAddr(RWT.getRequest()));
		String webContext = RWT.getRequest().getContextPath();
		
		if (webContext.startsWith("/" + offline_context) || "127.0.0.1".equals(getIpAddr(RWT.getRequest())) || "0:0:0:0:0:0:0:1".equals(getIpAddr(RWT.getRequest()))) {
			enableLoggin = false;
			System.out.println("the offline prefix ");
		}
		if (webContext.startsWith("/" + controller_context)) {
			is_controller = true;
			System.out.println("the controller ");
		}
		if (webContext.startsWith("/" + user_context_prefix)) {
			enableLoggin = false;
			System.out.println("the user prefix ");
		}
		RWT.getServiceManager().unregisterServiceHandler("tokenCallback");
		RWT.getServiceManager().registerServiceHandler("tokenCallback", new TokenCallbackServiceHandler(this));
		final String splash = "https://raw.githubusercontent.com/gama-platform/gama/master/msi.gama.application/splash.bmp";
		RWT.getClient().getService(JavaScriptExecutor.class).execute("document.body.style.background  = \"url('"
				+ splash + "') top center no-repeat fixed\"; \n document.body.style.backgroundSize = 'contain';");

		try {
			String uid = enableLoggin ? "" : "admin";

			DummyCallbackHandler dch = new DummyCallbackHandler();

			DummyLoginModule dlm = new DummyLoginModule();
			dlm.initialize(new Subject(), dch, null, null);
//			System.out.println("ss    "+RWT.getUISession().getHttpSession());
			boolean logged = enableLoggin
					? (RWT.getApplicationContext()
							.getAttribute("credential" + RWT.getUISession().getHttpSession()) == null ? false : true)
					: true; // false
			while (!logged) {
				logged = dlm.login();
			}
			if (logged || RWT.getApplicationContext()
					.getAttribute("credential" + RWT.getUISession().getHttpSession()) != null) {
				uid = enableLoggin ? dlm.getLoggedUser() : uid; // must enable

				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("start");
				System.out.println(uid);
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				if (is_controller && !uid.equals("admin")) {
					File tmpDir = new File("/opt/tomcat/webapps/" + user_context_prefix + uid);
					if (!tmpDir.exists()) {
						execBash("cp /opt/tomcat/webapps/" + controller_context + ".war /opt/tomcat/webapps/"
								+ user_context_prefix + uid + ".war");
						doWait();
					}
					JavaScriptExecutor ex = RWT.getClient().getService(JavaScriptExecutor.class);
					ex.execute("window.location=\"http://51.255.46.42:8080/" + user_context_prefix + uid
							+ "/texteditor\"");

				} else {
					postLoggedIn(uid);
				}

			} else {
				return 0;
			}
			if (RWT.getApplicationContext().getAttribute("logged_" + uid) == null || executor == null) {
				// ||
				// "restart".equals(RWT.getApplicationContext().getAttribute("logged_"+uid).toString()))
				// {
				WorkbenchAdvisor workbenchAdvisor = new BasicWorkbenchAdvisor();
				((BasicWorkbenchAdvisor) workbenchAdvisor).setLoggedUser(uid);
				System.out.println("logged as " + ((BasicWorkbenchAdvisor) workbenchAdvisor).getLoggedUser());
				User u = new User();
				u.setId(uid);

				ArrayList<User> onlines = (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
				Map listPads = (Map) RWT.getApplicationContext().getAttribute("listPads");
				// Map<String, ArrayList<String>>
				if (onlines == null) {
					onlines = new ArrayList<>();
					listPads = new HashMap<String, ArrayList<String>>();
				}
				boolean exist = false;
				for (User s : onlines) {
					if (s.getId().equals(uid)) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					onlines.add(u);
					listPads.put(uid, new ArrayList<String>());
				} 
//				RWT.getRequest().setCharacterEncoding("UTF-8");
//				RWT.getResponse().setCharacterEncoding("UTF-8");
//				RWT.getResponse().setContentType("text/html; charset=UTF-8");
				String mm=""+getParameter("model");//.replace("\\", "\\\\");
//				System.out.println(URLDecoder.decode(mm,"UTF-8"));
//				System.out.println(URLDecoder.decode(""+getParameter("exp"),"UTF-8"));
//				try {
//					System.out.println("sss");
//					System.out.println(URLDecoder.decode("C:\\Spatial%20Topology\\Agent%20movement\\models\\Continuous%20Field%20of%20Vision.gaml","UTF-8"));
//					System.out.println("bbb");
//					
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				RWT.getApplicationContext().setAttribute("_model", URLDecoder.decode(mm,"UTF-8"));
				RWT.getApplicationContext().setAttribute("_exp", URLDecoder.decode(""+getParameter("exp"),"UTF-8"));
				Application.checkWorkspace();
				RWT.getApplicationContext().setAttribute("onlines", onlines);
				RWT.getApplicationContext().setAttribute("listPads", listPads);
				// JavaScriptExecutor js =
				// RWT.getClient().getService(JavaScriptExecutor.class);
				// if(u.getId().equals(""+uid)) {
				executor.put(uid, RWT.getClient().getService(JavaScriptExecutor.class));
				System.out.println("script new    " + executor);
				// }
				RWT.getApplicationContext().setAttribute("logged_" + uid, RWT.getClient());

				Display display = PlatformUI.createDisplay();

				// GamaFonts.systemFont=Display.getCurrent().getSystemFont();
				int result = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
				display.dispose();
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("end");
				System.out.println(uid);
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				return result;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	protected void createContents(Composite parent) {
		// TODO Auto-generated method stub

	}
}