/*
    Copyright 2005 VTT Biotechnology

    This file is part of MZmine.

    MZmine is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    MZmine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MZmine; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

package net.sf.mzmine.visualizers.alignmentresult;

import net.sf.mzmine.alignmentresultmethods.*;
import net.sf.mzmine.alignmentresultvisualizers.*;
import net.sf.mzmine.datastructures.*;
import net.sf.mzmine.obsoletedistributionframework.*;
import net.sf.mzmine.peaklistmethods.*;
import net.sf.mzmine.rawdatamethods.*;
import net.sf.mzmine.rawdatavisualizers.*;
import net.sf.mzmine.userinterface.*;
import net.sf.mzmine.util.*;

public interface AlignmentResultVisualizer {

	public static final int CHANGETYPE_PEAK_MEASURING_SETTING = 1;

	/**
	 * This method is used to given alignment result object for the visualizer
	 */
	public void setAlignmentResult(AlignmentResult alignmentResult);


	/**
	 * Refresh visualizer is called when height/area (peak measuring) setting is switched
	 */
	public void refreshVisualizer(int changeType);

	/**
	 * This method is called after selected row in alignment result object has changed
	 */
	public void updateSelectedRow();

	public void printMe();
	public void copyMe();

}