package me.dufek.securitydrones.algorithm.grid;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeEdge;
import java.io.Serializable;
import java.util.ArrayList;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.geometry.Bounds;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 * This class is a grid data structure. The grid is created for the area and
 * contains particular cells.
 *
 * @author Jan Dufek
 */
public class Grid implements Serializable {

    /**
     * List of cells.
     */
    private final ArrayList<Cell> cells;

    /**
     * List of edges between cells.
     */
    private final ArrayList<SpanningTreeEdge> edges;

    /**
     * Number of vertical cells.
     */
    private int numberOfVerticalCells;

    /**
     * Number of horizontal cells.
     */
    private int numberOfHorizontalCells;

    /**
     * UAV request if the grid is assigned to particular UAV.
     */
    private UAVRequest uavRequest;

    /**
     * Initialize grid. It will also call methods for creation of a grid.
     *
     * @param cellSize Size of one cell
     */
    public Grid(double cellSize) {
        cells = new ArrayList<Cell>();
        edges = new ArrayList<SpanningTreeEdge>();
        createGrid(cellSize);
    }

    /**
     * Initialize grid from given cells.
     *
     * @param cells List of cells
     */
    public Grid(ArrayList<Cell> cells) {
        this.cells = new ArrayList<Cell>();
        edges = new ArrayList<SpanningTreeEdge>();

        int iMinOriginal = Integer.MAX_VALUE;
        int iMaxOriginal = -Integer.MAX_VALUE;
        int jMinOriginal = Integer.MAX_VALUE;
        int jMaxOriginal = -Integer.MAX_VALUE;

        for (Cell originalCell : cells) {
            if (originalCell.getI() < iMinOriginal) {
                iMinOriginal = originalCell.getI();
            }

            if (iMaxOriginal < originalCell.getI()) {
                iMaxOriginal = originalCell.getI();
            }

            if (originalCell.getJ() < jMinOriginal) {
                jMinOriginal = originalCell.getJ();
            }

            if (jMaxOriginal < originalCell.getJ()) {
                jMaxOriginal = originalCell.getJ();
            }
        }

        double iMin = Double.MAX_VALUE;
        double iMax = -Double.MAX_VALUE;
        double jMin = Double.MAX_VALUE;
        double jMax = -Double.MAX_VALUE;

        for (Cell originalCell : cells) {

            SubCell originalTopLeft = originalCell.getSubCells().getTopLeftSubCell();
            Cell newTopLeft = new Cell(originalTopLeft.getX(), originalTopLeft.getY(), originalTopLeft.getWidth(), originalTopLeft.getHeight(), originalCell.getNumber() * 4, (originalCell.getI() - iMinOriginal) * 2, (originalCell.getJ() - jMinOriginal) * 2);

            SubCell originalTopRight = originalCell.getSubCells().getTopRightSubCell();
            Cell newTopRight = new Cell(originalTopRight.getX(), originalTopRight.getY(), originalTopRight.getWidth(), originalTopRight.getHeight(), originalCell.getNumber() * 4 + 1, (originalCell.getI() - iMinOriginal) * 2, (originalCell.getJ() - jMinOriginal) * 2 + 1);

            SubCell originalBottomLeft = originalCell.getSubCells().getBottomLeftSubCell();
            Cell newBottomLeft = new Cell(originalBottomLeft.getX(), originalBottomLeft.getY(), originalBottomLeft.getWidth(), originalBottomLeft.getHeight(), originalCell.getNumber() * 4 + 2, (originalCell.getI() - iMinOriginal) * 2 + 1, (originalCell.getJ() - jMinOriginal) * 2);

            SubCell originalBottomRight = originalCell.getSubCells().getBottomRightSubCell();
            Cell newBottomRight = new Cell(originalBottomRight.getX(), originalBottomRight.getY(), originalBottomRight.getWidth(), originalBottomRight.getHeight(), originalCell.getNumber() * 4 + 3, (originalCell.getI() - iMinOriginal) * 2 + 1, (originalCell.getJ() - jMinOriginal) * 2 + 1);

            if (Areas.intersect(newTopLeft)) {
                this.cells.add(newTopLeft);

                if (newTopLeft.getI() < iMin) {
                    iMin = newTopLeft.getI();
                }

                if (iMax < newTopLeft.getI()) {
                    iMax = newTopLeft.getI();
                }

                if (newTopLeft.getJ() < jMin) {
                    jMin = newTopLeft.getJ();
                }

                if (jMax < newTopLeft.getJ()) {
                    jMax = newTopLeft.getJ();
                }
            }

            if (Areas.intersect(newTopRight)) {
                this.cells.add(newTopRight);

                if (newTopRight.getI() < iMin) {
                    iMin = newTopRight.getI();
                }

                if (iMax < newTopRight.getI()) {
                    iMax = newTopRight.getI();
                }

                if (newTopRight.getJ() < jMin) {
                    jMin = newTopRight.getJ();
                }

                if (jMax < newTopRight.getJ()) {
                    jMax = newTopRight.getJ();
                }
            }

            if (Areas.intersect(newBottomLeft)) {
                this.cells.add(newBottomLeft);

                if (newBottomLeft.getI() < iMin) {
                    iMin = newBottomLeft.getI();
                }

                if (iMax < newBottomLeft.getI()) {
                    iMax = newBottomLeft.getI();
                }

                if (newBottomLeft.getJ() < jMin) {
                    jMin = newBottomLeft.getJ();
                }

                if (jMax < newBottomLeft.getJ()) {
                    jMax = newBottomLeft.getJ();
                }
            }

            if (Areas.intersect(newBottomRight)) {
                this.cells.add(newBottomRight);

                if (newBottomRight.getI() < iMin) {
                    iMin = newBottomRight.getI();
                }

                if (iMax < newBottomRight.getI()) {
                    iMax = newBottomRight.getI();
                }

                if (newBottomRight.getJ() < jMin) {
                    jMin = newBottomRight.getJ();
                }

                if (jMax < newBottomRight.getJ()) {
                    jMax = newBottomRight.getJ();
                }
            }
        }

        this.numberOfVerticalCells = (int) (iMax - iMin) + 1;
        this.numberOfHorizontalCells = (int) (jMax - jMin) + 1;
    }

    /**
     * Create a grid with particular cell size.
     *
     * @param cellSize Cell size.
     */
    private void createGrid(double cellSize) {
        Bounds bounds = Areas.getBounds();

        double cellHeight = cellSize;
        double cellWidth = cellSize;
//        double cellHeight = 30;
//        double cellWidth = 22;

        double xMin = bounds.getMinX();
        double xMax = bounds.getMaxX();
        double yMin = bounds.getMinY();
        double yMax = bounds.getMaxY();

        numberOfVerticalCells = (int) Math.ceil((xMax - xMin) / cellHeight);
        numberOfHorizontalCells = (int) Math.ceil((yMax - yMin) / cellWidth);

        for (int i = 0; i < numberOfVerticalCells; i++) { // rows
            for (int j = 0; j < numberOfHorizontalCells; j++) { // columns
                double x = xMax - i * cellHeight;
                double y = yMin + j * cellWidth;
                Cell cell = new Cell(x - cellHeight / 2, y + cellWidth / 2, cellWidth, cellHeight, i * numberOfHorizontalCells + j, i, j);

                cells.add(cell);
            }
        }

        setNeighbours(numberOfVerticalCells, numberOfHorizontalCells);
        removeCellsWithNoAreaCoverage(numberOfVerticalCells, numberOfHorizontalCells);
    }

    /**
     * Set neighbors for all cells in the grid.
     *
     * @param numberOfVerticalCells Number of vertical cells.
     * @param numberOfHorizontalCells Number of horizontal cells.
     */
    private void setNeighbours(int numberOfVerticalCells, int numberOfHorizontalCells) {
        for (int i = 0; i < numberOfVerticalCells; i++) { // rows
            for (int j = 0; j < numberOfHorizontalCells; j++) { // columns
                int actualIndex = i * numberOfHorizontalCells + j;
                Cell cell = cells.get(actualIndex);

                int topNeighbourIndex = (i - 1) * numberOfHorizontalCells + j;

                if (0 <= topNeighbourIndex && topNeighbourIndex < cells.size()) {
                    cell.setTopNeighbour(cells.get(topNeighbourIndex), 1);
                } else {
                    cell.setTopNeighbour(null, Double.MAX_VALUE);
                }

                int bottomNeighbourIndex = (i + 1) * numberOfHorizontalCells + j;

                if (0 <= bottomNeighbourIndex && bottomNeighbourIndex < cells.size()) {
                    cell.setBottomNeighbour(cells.get(bottomNeighbourIndex), 1);
                } else {
                    cell.setBottomNeighbour(null, Double.MAX_VALUE);
                }

                int minRow = i * numberOfHorizontalCells;
                int maxRow = i * numberOfHorizontalCells + numberOfHorizontalCells - 1;

                int rightNeighbourIndex = i * numberOfHorizontalCells + j + 1;

                if (minRow <= rightNeighbourIndex && rightNeighbourIndex <= maxRow) {
                    cell.setRightNeighbour(cells.get(rightNeighbourIndex), 1);
                } else {
                    cell.setRightNeighbour(null, Double.MAX_VALUE);
                }

                int leftNeighbourIndex = i * numberOfHorizontalCells + j - 1;

                if (minRow <= leftNeighbourIndex && leftNeighbourIndex <= maxRow) {
                    cell.setLeftNeighbour(cells.get(leftNeighbourIndex), 1);
                } else {
                    cell.setLeftNeighbour(null, Double.MAX_VALUE);
                }
            }
        }
    }

    /**
     * Removes cells which has no intersection with given area.
     *
     * @param numberOfVerticalCells Number of vertical cells.
     * @param numberOfHorizontalCells Number of horizontal cells.
     */
    private void removeCellsWithNoAreaCoverage(int numberOfVerticalCells, int numberOfHorizontalCells) {
        if (cells.size() == 1) {
            return;
        }

        ArrayList<Cell> cellsToRemove = new ArrayList<Cell>();

        for (int i = 0; i < numberOfVerticalCells; i++) { //
            for (int j = 0; j < numberOfHorizontalCells; j++) {
                int actualIndex = i * numberOfHorizontalCells + j;
                Cell cell = cells.get(actualIndex);

                if (!Areas.intersect(cell)) {
                    cellsToRemove.add(cell);
                }
            }
        }

        for (Cell cell : cellsToRemove) {
            reconnectNeighbours(cell);
            cells.remove(cell);
        }
    }

    /**
     * Reconnect neighbors after cells were removed.
     *
     * @param cell Cell for which we need to reconnect neighbors.
     */
    private void reconnectNeighbours(Cell cell) {
        Neighbour topNeighbour = cell.getTopNeighbour();
        Neighbour bottomNeighbour = cell.getBottomNeighbour();
        Neighbour rightNeighbour = cell.getRightNeighbour();
        Neighbour leftNeighbour = cell.getLeftNeighbour();

        if (topNeighbour.getCell() != null) {

            Cell bottomNeighbourCell = null;
            double bottomNeighbourDistance = Double.MAX_VALUE;

            if (bottomNeighbour.getCell() != null) {
                bottomNeighbourCell = bottomNeighbour.getCell();
                bottomNeighbourDistance = bottomNeighbour.getDistance();
            }

            topNeighbour.getCell().setBottomNeighbour(bottomNeighbourCell, topNeighbour.getDistance() + bottomNeighbourDistance);
        }

        if (bottomNeighbour.getCell() != null) {

            Cell topNeighbourCell = null;
            double topNeighbourDistance = Double.MAX_VALUE;

            if (topNeighbour.getCell() != null) {
                topNeighbourCell = topNeighbour.getCell();
                topNeighbourDistance = topNeighbour.getDistance();
            }

            bottomNeighbour.getCell().setTopNeighbour(topNeighbourCell, bottomNeighbour.getDistance() + topNeighbourDistance);
        }

        if (rightNeighbour.getCell() != null) {

            Cell leftNeighbourCell = null;
            double leftNeighbourDistance = Double.MAX_VALUE;

            if (leftNeighbour.getCell() != null) {
                leftNeighbourCell = leftNeighbour.getCell();
                leftNeighbourDistance = leftNeighbour.getDistance();
            }

            rightNeighbour.getCell().setLeftNeighbour(leftNeighbourCell, rightNeighbour.getDistance() + leftNeighbourDistance);
        }

        if (leftNeighbour.getCell() != null) {

            Cell rightNeighbourCell = null;
            double rightNeighbourDistance = Double.MAX_VALUE;

            if (rightNeighbour.getCell() != null) {
                rightNeighbourCell = rightNeighbour.getCell();
                rightNeighbourDistance = rightNeighbour.getDistance();
            }

            leftNeighbour.getCell().setRightNeighbour(rightNeighbourCell, leftNeighbour.getDistance() + rightNeighbourDistance);
        }
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }

    public int getNumberOfHorizontalCells() {
        return this.numberOfHorizontalCells;
    }

    public int getNumberOfVerticalCells() {
        return this.numberOfVerticalCells;
    }

    /**
     * Gets nearest cell to given location.
     *
     * @param location Location
     * @return Nearest cell.
     */
    public Cell getNearestCell(Location location) {
        double minimumDistance = Double.MAX_VALUE;
        Cell nearestCell = null;

        for (Cell cell : cells) {
            if (cell.isInSpanningTree()) {
                continue;
            }

            Location cellLocation = cell.getCenterLocation();
            double distance = Location.getDistance2D(cellLocation, location);

            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearestCell = cell;
            }
        }

        // We have more UAVs than cells
        if (nearestCell == null) {
            for (Cell cell : cells) {
                Location cellLocation = cell.getCenterLocation();
                double distance = Location.getDistance2D(cellLocation, location);

                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestCell = cell;
                }
            }
        }

        return nearestCell;
    }

    public Cell getCell(int row, int column) {
        Cell resultCell = null;

        for (Cell cell : cells) {
            if (cell.getI() == row && cell.getJ() == column) {
                resultCell = cell;
                break;
            }
        }

        return resultCell;
    }

    public Cell getStartCell(Location location) {
        double minimalDistance = Double.MAX_VALUE;
        Cell startCell = null;

        for (Cell cell : getCells()) {
            Location cellLocation = cell.getLocation();

            double distance = Location.getDistance2D(location, cellLocation);

            if (distance < minimalDistance) {
                minimalDistance = distance;
                startCell = cell;
            }
        }

        return startCell;
    }

    public void setUAVRequest(UAVRequest uavRequest) {
        this.uavRequest = uavRequest;
    }

    public UAVRequest getUAVRequest() {
        return this.uavRequest;
    }

    /**
     * Gets perimeters of the grid.
     *
     * @return Perimeter given as Area.
     */
    public ArrayList<Area> getAreas() {
        ArrayList<Area> areas = new ArrayList<Area>();
        ArrayList<Cell> usedCells = new ArrayList<Cell>();

        boolean end = false;

        while (!end) {
            Cell start = null;

            for (Cell cell : cells) {
                Cell potentialStartCell = cell;

                while (getCell(potentialStartCell.getI() - 1, potentialStartCell.getJ()) != null) {
                    potentialStartCell = getCell(potentialStartCell.getI() - 1, potentialStartCell.getJ());
                }

                while (getCell(potentialStartCell.getI(), potentialStartCell.getJ() - 1) != null) {
                    potentialStartCell = getCell(potentialStartCell.getI(), potentialStartCell.getJ() - 1);
                }

                if (usedCells.contains(potentialStartCell)) {
                    continue;
                } else {
                    start = potentialStartCell;
                    break;
                }
            }

            if (start == null) {
                break;
            }

            Area area = getArea(start, usedCells);

            areas.add(area);
        }

        return areas;
    }

    /**
     * Gets perimeter of the grid which is given as an area.
     *
     * @param start Start location.
     * @param usedCells Cells which are already used.
     * @return
     */
    public Area getArea(Cell start, ArrayList<Cell> usedCells) {
        ArrayList<Location> points = new ArrayList<Location>();

        Cell nextCell = start;
        Cell previousCell = null;

        usedCells.add(start);

        do {
            Cell topCell = getCell(nextCell.getI() - 1, nextCell.getJ());
            Cell rightCell = getCell(nextCell.getI(), nextCell.getJ() + 1);
            Cell bottomCell = getCell(nextCell.getI() + 1, nextCell.getJ());
            Cell leftCell = getCell(nextCell.getI(), nextCell.getJ() - 1);

            if (topCell == null && rightCell != null && bottomCell != null && leftCell != null) {
                if (previousCell == rightCell) {
                    previousCell = nextCell;
                    nextCell = bottomCell;
                } else if (previousCell == bottomCell) {
                    previousCell = nextCell;
                    nextCell = leftCell;
                } else {
                    points.add(nextCell.getTopLeftCornerLocation());
                    points.add(nextCell.getTopRightCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = rightCell;
                }
            } else if (topCell != null && rightCell == null && bottomCell != null && leftCell != null) {
                if (previousCell == bottomCell) {
                    previousCell = nextCell;
                    nextCell = leftCell;
                } else if (previousCell == leftCell) {
                    previousCell = nextCell;
                    nextCell = topCell;
                } else {
                    points.add(nextCell.getTopRightCornerLocation());
                    points.add(nextCell.getBottomRightCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = bottomCell;
                }
            } else if (topCell != null && rightCell != null && bottomCell == null && leftCell != null) {
                if (previousCell == leftCell) {
                    previousCell = nextCell;
                    nextCell = topCell;
                } else if (previousCell == topCell) {
                    previousCell = nextCell;
                    nextCell = rightCell;
                } else {
                    points.add(nextCell.getBottomRightCornerLocation());
                    points.add(nextCell.getBottomLeftCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = leftCell;
                }
            } else if (topCell != null && rightCell != null && bottomCell != null && leftCell == null) {
                if (previousCell == topCell) {
                    previousCell = nextCell;
                    nextCell = rightCell;
                } else if (previousCell == rightCell) {
                    previousCell = nextCell;
                    nextCell = bottomCell;
                } else {
                    points.add(nextCell.getBottomLeftCornerLocation());
                    points.add(nextCell.getTopLeftCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = topCell;
                }
            } else if (topCell == null && rightCell == null && bottomCell != null && leftCell != null) {
                if (previousCell == bottomCell) {
                    previousCell = nextCell;
                    nextCell = leftCell;
                } else {
                    points.add(nextCell.getTopLeftCornerLocation());
                    points.add(nextCell.getTopRightCornerLocation());
                    points.add(nextCell.getBottomRightCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = bottomCell;
                }
            } else if (topCell != null && rightCell == null && bottomCell == null && leftCell != null) {
                if (previousCell == leftCell) {
                    previousCell = nextCell;
                    nextCell = topCell;
                } else {
                    points.add(nextCell.getTopRightCornerLocation());
                    points.add(nextCell.getBottomRightCornerLocation());
                    points.add(nextCell.getBottomLeftCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = leftCell;
                }
            } else if (topCell != null && rightCell != null && bottomCell == null && leftCell == null) {
                if (previousCell == topCell) {
                    previousCell = nextCell;
                    nextCell = rightCell;
                } else {
                    points.add(nextCell.getBottomRightCornerLocation());
                    points.add(nextCell.getBottomLeftCornerLocation());
                    points.add(nextCell.getTopLeftCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = topCell;
                }
            } else if (topCell == null && rightCell != null && bottomCell != null && leftCell == null) {
                if (previousCell == rightCell) {
                    previousCell = nextCell;
                    nextCell = bottomCell;
                } else {
                    points.add(nextCell.getBottomLeftCornerLocation());
                    points.add(nextCell.getTopLeftCornerLocation());
                    points.add(nextCell.getTopRightCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = rightCell;
                }
            } else if (topCell == null && rightCell == null && bottomCell != null && leftCell == null) {
                points.add(nextCell.getBottomLeftCornerLocation());
                points.add(nextCell.getTopLeftCornerLocation());
                points.add(nextCell.getTopRightCornerLocation());
                points.add(nextCell.getBottomRightCornerLocation());
                usedCells.add(nextCell);
                previousCell = nextCell;
                nextCell = bottomCell;
            } else if (topCell == null && rightCell == null && bottomCell == null && leftCell != null) {
                points.add(nextCell.getTopLeftCornerLocation());
                points.add(nextCell.getTopRightCornerLocation());
                points.add(nextCell.getBottomRightCornerLocation());
                points.add(nextCell.getBottomLeftCornerLocation());
                usedCells.add(nextCell);
                previousCell = nextCell;
                nextCell = leftCell;
            } else if (topCell != null && rightCell == null && bottomCell == null && leftCell == null) {
                points.add(nextCell.getTopRightCornerLocation());
                points.add(nextCell.getBottomRightCornerLocation());
                points.add(nextCell.getBottomLeftCornerLocation());
                points.add(nextCell.getTopLeftCornerLocation());
                usedCells.add(nextCell);
                previousCell = nextCell;
                nextCell = topCell;
            } else if (topCell == null && rightCell != null && bottomCell == null && leftCell == null) {
                points.add(nextCell.getBottomRightCornerLocation());
                points.add(nextCell.getBottomLeftCornerLocation());
                points.add(nextCell.getTopLeftCornerLocation());
                points.add(nextCell.getTopRightCornerLocation());
                usedCells.add(nextCell);
                previousCell = nextCell;
                nextCell = rightCell;
            } else if (topCell != null && rightCell == null && bottomCell != null && leftCell == null) {
                if (previousCell == topCell) {
                    points.add(nextCell.getTopRightCornerLocation());
                    points.add(nextCell.getBottomRightCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = bottomCell;
                } else if (previousCell == bottomCell) {
                    points.add(nextCell.getBottomLeftCornerLocation());
                    points.add(nextCell.getTopLeftCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = topCell;
                } else {
                    throw new IllegalStateException("Illegal position on grid.");
                }
            } else if (topCell == null && rightCell != null && bottomCell == null && leftCell != null) {
                if (previousCell == rightCell) {
                    points.add(nextCell.getBottomRightCornerLocation());
                    points.add(nextCell.getBottomLeftCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = leftCell;
                } else if (previousCell == leftCell) {
                    points.add(nextCell.getTopLeftCornerLocation());
                    points.add(nextCell.getTopRightCornerLocation());
                    usedCells.add(nextCell);
                    previousCell = nextCell;
                    nextCell = rightCell;
                } else {
                    throw new IllegalStateException("Illegal position on grid.");
                }
            } else if (topCell == null && rightCell == null && bottomCell == null && leftCell == null) {
                points.add(nextCell.getTopLeftCornerLocation());
                points.add(nextCell.getTopRightCornerLocation());
                usedCells.add(nextCell);
                break;
            } else {
                if (previousCell == topCell) {
                    previousCell = nextCell;
                    nextCell = rightCell;
                } else if (previousCell == rightCell) {
                    previousCell = nextCell;
                    nextCell = bottomCell;
                } else if (previousCell == bottomCell) {
                    previousCell = nextCell;
                    nextCell = leftCell;
                } else if (previousCell == leftCell) {
                    previousCell = nextCell;
                    nextCell = topCell;
                } else {
                    throw new IllegalStateException("Illegal position on grid.");
                }
            }

        } while (!(nextCell == start && previousCell == getCell(start.getI() + 1, start.getJ())) && !(nextCell == start && getCell(start.getI() + 1, start.getJ()) == null && previousCell == getCell(start.getI(), start.getJ() + 1)));

        int npoints = points.size();
        double[] xpoints = new double[npoints];
        double[] ypoints = new double[npoints];

        for (int i = 0;
                i < npoints;
                i++) {
            double x = points.get(i).getX();
            double y = points.get(i).getY();

            xpoints[i] = x;
            ypoints[i] = y;
        }

        Area area = new Area(xpoints, ypoints, npoints);

        return area;
    }

    private boolean contains(ArrayList<Location> locations, Location location) {
        for (Location locationInList : locations) {
            if (Location.equal(locationInList, location, 1)) {
                return true;
            }
        }

        return false;
    }
}
