package com.dagnis.endpoints;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class EndpointService {

    public String getSumFromXlsx() throws URISyntaxException, IOException {
        String fileName = "/static/data.xlsx";
        Path path = Paths.get(Objects.requireNonNull(EndpointService.class.getResource(fileName)).toURI());

        FileInputStream file = new FileInputStream(new File(String.valueOf(path)));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        double result = 0.0;

        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                result += cell.getNumericCellValue();
            }
        }
        file.close();

        return "SUM is " + (int) result;
    }

    public String getBtcPrice() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

        JSONObject jsonData = new JSONObject(getJsonObject(url));
        JSONObject bpi = (JSONObject) jsonData.get("bpi");
        JSONObject eur = (JSONObject) bpi.get("EUR");
        BigDecimal eurPrice = new BigDecimal(eur.get("rate_float").toString());

        return "Price of BTC is " + eurPrice + " Eur";
    }

    public String getCapital(String country) throws IOException, URISyntaxException {
        String url = "https://restcountries.com/v3.1/name/" + country;
        JSONArray jsonArray = new JSONArray(getJsonArray(url));
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String capital = jsonObject.get("capital").toString();

        return "Capital of "
                + country.substring(0, 1).toUpperCase()
                + country.substring(1)
                + " is "
                + capital.substring(2, capital.length() - 2);

    }

    private String getJsonObject(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }

    private JSONArray getJsonArray(String url) throws IOException, URISyntaxException {
        URI uri = new URI(url);
        JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
        return new JSONArray(tokener);
    }

}
