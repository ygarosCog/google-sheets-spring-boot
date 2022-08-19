package com.cognizant.sheetsDemo.service;

import com.cognizant.sheetsDemo.model.FormData;
import com.cognizant.sheetsDemo.model.Row;
import com.cognizant.sheetsDemo.service.util.DataConverter;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SheetsService{

    private Sheets sheets;

    public List<Row> getData(String spreadsheetId, String range) throws IOException {
        ValueRange rawData = sheets.spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute();
        return DataConverter.convertRawDataToData(rawData);
    }

    public AppendValuesResponse appendData(FormData formData) throws IOException {
        return this.sheets.spreadsheets()
                .values()
                .append(
                        formData.getSpreadsheetId(),
                        formData.getRange(),
                        DataConverter.convertDataToRawData(formData.getData())
                )
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    public ClearValuesResponse clearAllData(String spreadsheetId, String range) throws IOException {
        return this.sheets.spreadsheets()
                .values()
                .clear(spreadsheetId, range, new ClearValuesRequest())
                .execute();
    }
    public boolean isSheetsInitialized(){
        return this.sheets != null;
    }
    public void setSheets(Sheets sheets){
        this.sheets = sheets;
    }
}
