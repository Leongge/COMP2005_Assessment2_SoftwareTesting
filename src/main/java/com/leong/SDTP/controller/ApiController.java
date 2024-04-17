package com.leong.SDTP.controller;

import com.leong.SDTP.model.Admission;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiController {

    @GetMapping("/f1/{patientId}")
    public List<Admission> showPatientInfo(@PathVariable final String patientId) {
        List<Admission> admissions = new ArrayList<>();

        try {
            // URL for the API endpoint
            String apiLink = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";

            // Create HttpClient object
            HttpClient httpClient = HttpClients.createDefault();

            // Create HttpGet object with the URL
            HttpGet httpGet = new HttpGet(apiLink);

            // Execute the request
            HttpResponse response = httpClient.execute(httpGet);

            // Check response status code
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Parse JSON response
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = new JSONArray(jsonResponse);
                System.out.println(jsonArray);

                // Iterate through JSON array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Check if patientID matches
                    if (jsonObject.getInt("patientID") == Integer.parseInt(patientId)) {
                        Admission admission = new Admission(
                                jsonObject.getInt("id"),
                                jsonObject.getString("admissionDate"),
                                jsonObject.getString("dischargeDate"),
                                jsonObject.getInt("patientID")
                        );
                        admissions.add(admission);
                    }
                }
            } else {
                // Handle non-OK response
                System.err.println("HTTP request failed with response code: " + statusCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return admissions;
    }
}
