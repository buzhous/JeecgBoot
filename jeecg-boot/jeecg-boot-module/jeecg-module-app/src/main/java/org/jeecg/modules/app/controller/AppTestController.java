package org.jeecg.modules.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/app/test")
public class AppTestController {


    @GetMapping
    public void test(HttpServletResponse response,
                     HttpServletRequest request,
                     @RequestParam(required = false) Integer change,
                     @RequestParam(required = false) String url) throws IOException {


    }


}
