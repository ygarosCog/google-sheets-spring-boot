package com.cognizant.sheetsDemo.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FormData {
    private String spreadsheetId;
    private String range;
    private String[][] data;
    public FormData() {
    }

    public FormData(String spreadsheetId, String range, String[][] data) {
        this.spreadsheetId = spreadsheetId;
        this.range = range;
        this.data = data;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FormData{" +
                "spreadsheetId='" + spreadsheetId + '\'' +
                ", range='" + range + '\'' +
                ", data=" + Arrays.stream(data).flatMap(Arrays::stream).collect(Collectors.joining()) +
                '}';
    }
}
