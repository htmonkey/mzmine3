/*
 * Copyright 2006-2015 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MZmine 2; if not, write to the Free Software Foundation, Inc., 51 Franklin
 * St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package net.sf.mzmine.modules.visualization.ida;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import net.sf.mzmine.datamodel.RawDataFile;
import net.sf.mzmine.desktop.impl.WindowsMenu;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.parameters.parametertypes.WindowSettingsParameter;
import net.sf.mzmine.util.dialogs.AxesSetupDialog;

import com.google.common.collect.Range;

/**
 * IDA visualizer using JFreeChart library
 */
public class IDAVisualizerWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private IDAToolBar toolBar;
    private IDAPlot IDAPlot;
    private IDABottomPanel bottomPanel;
    private IDADataSet dataset;
    private RawDataFile dataFile;

    public IDAVisualizerWindow(RawDataFile dataFile, 
	    Range<Double> rtRange, Range<Double> mzRange,
	    ParameterSet parameters) {

	super("IDA visualizer: [" + dataFile.getName() + "]");

	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setBackground(Color.white);

	this.dataFile = dataFile;

	dataset = new IDADataSet(dataFile, rtRange, mzRange, this);

	toolBar = new IDAToolBar(this);
	add(toolBar, BorderLayout.EAST);

	IDAPlot = new IDAPlot(dataFile, this, dataset, rtRange, mzRange);
	add(IDAPlot, BorderLayout.CENTER);

	bottomPanel = new IDABottomPanel(this, dataFile, parameters);
	add(bottomPanel, BorderLayout.SOUTH);

	updateTitle();

	// After we have constructed everything, load the peak lists into the
	// bottom panel
	bottomPanel.rebuildPeakListSelector();

	MZmineCore.getDesktop().addPeakListTreeListener(bottomPanel);

	// Add the Windows menu
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(new WindowsMenu());
	setJMenuBar(menuBar);

	pack();

	// get the window settings parameter
	ParameterSet paramSet = MZmineCore.getConfiguration()
		.getModuleParameters(IDAVisualizerModule.class);
	WindowSettingsParameter settings = paramSet
		.getParameter(IDAParameters.windowSettings);

	// update the window and listen for changes
	settings.applySettingsToWindow(this);
	this.addComponentListener(settings);
    }

    public void dispose() {
	super.dispose();
	MZmineCore.getDesktop().removePeakListTreeListener(bottomPanel);
    }

    void updateTitle() {
	StringBuffer title = new StringBuffer();
	title.append("Time vs. m/z for IDA dependents\n");
	title.append(dataFile.getName());
	IDAPlot.setTitle(title.toString());
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {

	String command = event.getActionCommand();

	if (command.equals("SETUP_AXES")) {
	    AxesSetupDialog dialog = new AxesSetupDialog(this,
		    IDAPlot.getXYPlot());
	    dialog.setVisible(true);
	}

    }

    IDAPlot getPlot() {
	return IDAPlot;
    }
}
