package me.dufek.securitydrones.graph.analyser;

import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.heatmap.HeatCell;

/**
 * Analyzer is used to analyze the coverage.
 *
 * @author Jan Dufek
 */
public class Analyser {

    public static double getActualCoverage() {
        double sumOfHeat = 0;
        int numberOfCells = 0;
        for (Area area : Areas.listOfAreas) {
            if (area.getGrid() == null) {
                continue;
            }

            for (HeatCell cell : area.getGrid().getCells()) {
                sumOfHeat += cell.getHeat();
                numberOfCells++;
            }
        }

        double actualCoverage = sumOfHeat / numberOfCells;

        return actualCoverage;
    }
}
