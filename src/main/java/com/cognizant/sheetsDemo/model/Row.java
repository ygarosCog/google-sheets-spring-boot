package com.cognizant.sheetsDemo.model;

import java.util.List;

public class Row {
    private List<String> cells;

    public Row(List<String> cells) {
        this.cells = cells;
    }

    public Row() {
    }

    public List<String> getCells() {
        return cells;
    }

    public void setCells(List<String> cells) {
        this.cells = cells;
    }
}
