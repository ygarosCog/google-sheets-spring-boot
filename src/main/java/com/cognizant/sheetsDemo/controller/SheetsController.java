package com.cognizant.sheetsDemo.controller;

import com.cognizant.sheetsDemo.model.FormData;
import com.cognizant.sheetsDemo.model.Row;
import com.cognizant.sheetsDemo.service.GoogleServiceAuthentication;
import com.cognizant.sheetsDemo.service.SheetsService;
import com.cognizant.sheetsDemo.service.util.DataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
public class SheetsController {

    private final Logger log = LoggerFactory.getLogger(SheetsController.class);
    private final String spreadsheetId ;
    private final String sheetRange = "Arkusz1!A:Z";
    private final SheetsService sheetsService;

    private final GoogleServiceAuthentication authService;

    @Autowired
    public SheetsController(SheetsService sheetsService,
                            GoogleServiceAuthentication authService,
                            @Value("${app.spreadsheet.id}")String spreadsheetId){
        this.sheetsService = sheetsService;
        this.authService = authService;
        this.spreadsheetId = spreadsheetId;
    }

    @ModelAttribute("formData")
    public FormData newFormData(){
        return new FormData(
                this.spreadsheetId,
                this.sheetRange,
                new String[][]{
                        new String[]{"just", "a", "very"},
                        new String[]{"dummy", "data", "to"},
                        new String[]{"post", "into", "spreadsheet"}
                    }
                );
    }

    @GetMapping("/")
    public String getAuth(){
        if(this.sheetsService.isSheetsInitialized()) {
            return "index";
        }
        return "redirect:"+authService.getNewAuthorizationUrl();
    }

    @PostMapping("/appendDataToSpreadsheet")
    public String postData(@ModelAttribute FormData formData) throws IOException {
        log.info(formData.toString());
        sheetsService.appendData(formData);
        return "redirect:";
    }

    @GetMapping("/getDataFromSpreadsheet")
    public String getData(ModelMap map) throws IOException {
        List<Row> data = sheetsService.getData(spreadsheetId, this.sheetRange);
        map.addAttribute("data", data);
        map.addAttribute("colHeaders", this.getChars(data.get(0).getCells().size()));
        return "data";
    }

    @PostMapping("/clearDataFromSpreadsheet")
    public String deleteData() throws IOException {
        this.sheetsService.clearAllData(spreadsheetId, this.sheetRange);
        return "redirect:";
    }

    @GetMapping("/authorized")
    public String getAuthorized(@RequestParam("code")String code) throws GeneralSecurityException, IOException {
        this.sheetsService.setSheets(this.authService.getSheetsService(code));
        return "redirect:";
    }
    private char[] getChars(int noOfCols) {
        char[] chars = new char[noOfCols + 1];
        chars[0] = ' ';
        for (int i = 1; i < chars.length; i++) {
            chars[i] = (char) (64 + i);
        }
        return chars;
    }
}
