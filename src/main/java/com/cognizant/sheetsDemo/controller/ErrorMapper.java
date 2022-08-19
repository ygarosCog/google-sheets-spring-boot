package com.cognizant.sheetsDemo.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ErrorMapper {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError(Exception e) {
        ModelAndView model = new ModelAndView();
        model.addObject("exception", e);
        model.setViewName("wrong");
        return model;
    }
//   777777777
}
