package com.dagnis.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class EndpointController {

    private final EndpointService endpointService;

    public EndpointController(EndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @GetMapping("/")
    public String home() {
        return "Hello world";
    }

    @GetMapping("/btc")
    public String getBtcPrice() {
        return endpointService.getBtcPrice();
    }

    @GetMapping("/capital")
    public String getCapital(@RequestParam String country) throws IOException, URISyntaxException {
        return endpointService.getCapital(country);
    }

    @GetMapping("excel-sum")
    public String getSum() throws URISyntaxException, IOException {
        return endpointService.getSumFromXlsx();
    }

}
