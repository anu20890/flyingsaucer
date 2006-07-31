/*
 * {{{ header & license
 * Copyright (c) 2004 Joshua Marinacci
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package org.xhtmlrenderer.demo.browser;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.xhtmlrenderer.util.GeneralUtil;
import org.xhtmlrenderer.util.Uu;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.*;

/**
 * Description of the Class
 *
 * @author empty
 */
public class BrowserStartup {
    /**
     * Description of the Field
     */
    public BrowserPanel panel;
    /**
     * Description of the Field
     */
    protected BrowserMenuBar menu;
    /**
     * Description of the Field
     */
    protected JFrame frame;
    /**
     * Description of the Field
     */
    protected JFrame validation_console = null;
    /**
     * Description of the Field
     */
    protected BrowserActions actions;
    /**
     * Page to view at startup
     */
    protected String startPage;

    /**
     * Description of the Field
     */
    protected ValidationHandler error_handler = new ValidationHandler();
    /**
     * Description of the Field
     */
    public static Logger logger = Logger.getLogger("app.browser");

    /**
     * Constructor for the BrowserStartup object
     */
    public BrowserStartup() {
        this("demo:demos/splash/splash.html");
    }

    /**
     * Constructor for the BrowserStartup object
     */
    public BrowserStartup(String startPage) {
        logger.info("starting up");
        this.startPage = startPage;
    }

    /**
     * Description of the Method
     */
    public void init() {
        logger.info("creating UI");
        actions = new BrowserActions(this);
        actions.init();

        panel = new BrowserPanel(this, new FrameBrowserPanelListener());
        panel.init(startPage);
        //panel.createLayout();
        panel.createActions();

        menu = new BrowserMenuBar(this);
        menu.init();
        menu.createLayout();
        menu.createActions();

        try {
            panel.loadPage(startPage);
        } catch (Exception ex) {
            Uu.p(ex);
        }
    }

    /**
     * The main program for the BrowserStartup class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        setLookAndFeel();

        if (GeneralUtil.isMacOSX()) {
            try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "FS Browser");
            } catch (Exception ex) {
                try {
                logger.log(Level.SEVERE,"error initalizing the mac properties",ex);
                } catch (Exception ex2) {
                    //System.out.println("error writing to the log file!" + ex2);
                    //ex2.printStackTrace();
                }
            }
        }

        //System.out.println(new URI("images/Stop24.gif"));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final BrowserStartup bs = new BrowserStartup();
        bs.frame = frame;
        if (args.length > 0) {
            bs.startPage = args[0];
        }
        bs.init();
        frame.setJMenuBar(bs.menu);

        frame.getContentPane().add(bs.panel.toolbar, BorderLayout.PAGE_START);
        frame.getContentPane().add(bs.panel, BorderLayout.CENTER);
        bs.panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        frame.getContentPane().add(bs.panel.status, BorderLayout.PAGE_END);
        frame.pack();
        frame.setSize(700, 600);

        frame.show();
    }

    private static void setLookAndFeel() {
        boolean lnfSet = false;
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
            lnfSet = true;
        } catch (Exception e) {}
        if (!lnfSet) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                lnfSet = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description of the Class
     *
     * @author empty
     */
    class FrameBrowserPanelListener implements BrowserPanelListener {
        /**
         * Description of the Method
         *
         * @param url   PARAM
         * @param title PARAM
         */
        public void pageLoadSuccess(String url, String title) {
            frame.setTitle(title + (title.length() > 0 ? " - " : "") + "Flying Saucer");
        }
    }

}


/*
 * $Id$
 *
 * $Log$
 * Revision 1.20  2006/07/31 14:20:54  pdoubleya
 * Bunch of cleanups and fixes. Now using a toolbar for actions, added Home button, next/prev navigation actions to facilitate demo file browsing, loading demo pages from a list, about dlg and link to user's manual.
 *
 * Revision 1.19  2006/01/01 04:01:07  peterbrant
 * Avoid double layout on component resize / Don't relayout if print mode and window size changes
 *
 * Revision 1.18  2005/11/28 17:02:33  peterbrant
 * Add ability to launch browser with URL provided on the command line (from Andrew Goodnough)
 *
 * Revision 1.17  2005/11/17 21:41:21  peterbrant
 * Allow custom start page
 *
 * Revision 1.16  2005/09/08 03:37:57  joshy
 * final R5 release
 *
 * Revision 1.15  2005/07/31 01:12:29  joshy
 * updated browser demos, about box demos, and added pack200 to the distro
 *
 * Revision 1.14  2005/07/21 22:56:07  joshy
 * tweaked the splash screen
 *
 * Revision 1.13  2005/06/16 07:24:44  tobega
 * Fixed background image bug.
 * Caching images in browser.
 * Enhanced LinkListener.
 * Some house-cleaning, playing with Idea's code inspection utility.
 *
 * Revision 1.12  2005/06/15 13:35:27  tobega
 * Fixed history
 *
 * Revision 1.11  2005/04/03 21:51:31  joshy
 * fixed code that gets the XMLReader on the mac
 * added isMacOSX() to GeneralUtil
 * added app name and single menu bar to browser
 *
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 *
 * Revision 1.10  2005/03/28 19:18:47  pdoubleya
 * Fixed to update layout on resize.
 *
 * Revision 1.9  2005/03/28 19:03:32  pdoubleya
 * Changed startup page.
 *
 * Revision 1.8  2004/12/12 03:33:07  tobega
 * Renamed x and u to avoid confusing IDE. But that got cvs in a twist. See if this does it
 *
 * Revision 1.7  2004/12/12 02:54:30  tobega
 * Making progress
 *
 * Revision 1.6  2004/11/16 07:25:20  tobega
 * Renamed HTMLPanel to BasicPanel
 *
 * Revision 1.5  2004/11/16 03:43:25  joshy
 * first pass at printing support
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 *
 * Revision 1.4  2004/11/10 17:28:54  joshy
 * initial support for anti-aliased text w/ minium
 *
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 *
 * Revision 1.3  2004/10/23 14:38:58  pdoubleya
 * Re-formatted using JavaStyle tool.
 * Cleaned imports to resolve wildcards except for common packages (java.io, java.util, etc)
 * Added CVS log comments at bottom.
 *
 *
 */
