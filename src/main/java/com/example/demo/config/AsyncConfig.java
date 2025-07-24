package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    /*
     * String xml = "<root><inicio>Aquí empieza</inicio><otra>Más texto</otra></root>";

int start = xml.indexOf("<inicio>");
int end = xml.indexOf("</inicio>") + "</inicio>".length();

if (start != -1 && end != -1) {
    String fragmento = xml.substring(start, end);
    System.out.println(fragmento);  // <inicio>Aquí empieza</inicio>
}
     */
}
