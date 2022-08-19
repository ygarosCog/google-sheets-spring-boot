package com.cognizant.sheetsDemo.service.util;

import com.cognizant.sheetsDemo.model.Row;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataConverter {

    public static List<Row> convertRawDataToData(ValueRange rawData){
        List<Row> result = new ArrayList<>();
        rawData.getValues().forEach(
                v ->
                        result.add(
                                new Row(
                                        v.stream()
                                                .map(Object::toString)
                                                .collect(Collectors.toList())
                                )
                        )
        );
        return result;
    }
    public static ValueRange convertDataToRawData(List<Row> data){
        List<List<Object>> list = new ArrayList<>();
        for (Row datum : data) {
            List<String> cells = datum.getCells();
            list.add(Collections.singletonList(cells));
        }
        return new ValueRange().setValues(list);
    }
    public static ValueRange convertDataToRawData(String[][] data){
        List<List<Object>> list = new ArrayList<>();
        for (String[] datum : data) {
            List<Object> cells = new ArrayList<>(datum.length);
            cells.addAll(Arrays.asList(datum));
            list.add(cells);
        }
        return new ValueRange().setValues(list);
    }

}
