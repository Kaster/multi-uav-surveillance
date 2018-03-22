package me.dufek.securitydrones.algorithm.grid;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Data structure for all sub cells of one particular cell.
 *
 * @author Jan Dufek
 */
public class SubCells implements Iterable<SubCell> {

    private SubCell topLeft;
    private SubCell topRight;
    private SubCell bottomLeft;
    private SubCell bottomRight;

    public SubCells(double x, double y, double width, double height, Cell cell) {
        topLeft = new SubCell(x + height / 4, y - width / 4, width / 2, height / 2, SubCellType.TOP_LEFT, cell);
        topRight = new SubCell(x + height / 4, y + width / 4, width / 2, height / 2, SubCellType.TOP_RIGHT, cell);
        bottomLeft = new SubCell(x - height / 4, y - width / 4, width / 2, height / 2, SubCellType.BOTTOM_LEFT, cell);
        bottomRight = new SubCell(x - height / 4, y + width / 4, width / 2, height / 2, SubCellType.BOTTOM_RIGHT, cell);
    }

    public void setTopLeftSubCell(SubCell subCell) {
        this.topLeft = subCell;
    }

    public SubCell getTopLeftSubCell() {
        return this.topLeft;
    }

    public void setTopRightSubCell(SubCell subCell) {
        this.topRight = subCell;
    }

    public SubCell getTopRightSubCell() {
        return this.topRight;
    }

    public void setBottomLeftSubCell(SubCell subCell) {
        this.bottomLeft = subCell;
    }

    public SubCell getBottomLeftSubCell() {
        return this.bottomLeft;
    }

    public void setBottomRightSubCell(SubCell subCell) {
        this.bottomRight = subCell;
    }

    public SubCell getBottomRightSubCell() {
        return this.bottomRight;
    }

    /**
     * Used to iterate through sub cells.
     *
     * @return Iterator.
     */
    @Override
    public Iterator<SubCell> iterator() {
        ArrayList<SubCell> list = new ArrayList<SubCell>(4);

        list.add(topLeft);
        list.add(topRight);
        list.add(bottomLeft);
        list.add(bottomRight);

        return list.iterator();
    }
}
