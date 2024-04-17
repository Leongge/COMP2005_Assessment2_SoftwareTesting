package com.leong.SDTP.controller;

import com.leong.SDTP.model.Admission;
import com.leong.SDTP.model.Patient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/f2")
    public List<Patient> filterAdmissions(@RequestBody String dateJson) {
        List<Patient> admittedPatients = new ArrayList<>();

        try {
            // Parse the JSON input
            JSONObject dateObject = new JSONObject(dateJson);
            String date = dateObject.getString("date");

            // Fetch admissions data
            String admissionsUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(admissionsUrl);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                String admissionsResponse = EntityUtils.toString(response.getEntity());
                JSONArray admissionsArray = new JSONArray(admissionsResponse);

                // Iterate through admissions
                for (int i = 0; i < admissionsArray.length(); i++) {
                    JSONObject admissionObject = admissionsArray.getJSONObject(i);
                    String dischargeDate = admissionObject.getString("dischargeDate");

                    // Check if discharge date is after the input date
                    if (dischargeDate.compareTo(date) > 0) {
                        int patientID = admissionObject.getInt("patientID");

                        // Fetch patient data using patient ID
                        String patientsUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Patients";
                        httpGet = new HttpGet(patientsUrl);
                        response = httpClient.execute(httpGet);

                        if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                            String patientsResponse = EntityUtils.toString(response.getEntity());
                            JSONArray patientsArray = new JSONArray(patientsResponse);

                            // Iterate through patients
                            for (int j = 0; j < patientsArray.length(); j++) {
                                JSONObject patientObject = patientsArray.getJSONObject(j);
                                if (patientObject.getInt("id") == patientID) {
                                    String surname = patientObject.getString("surname");
                                    String forename = patientObject.getString("forename");
                                    String nhsNumber = patientObject.getString("nhsNumber");
                                    Patient patient = new Patient(patientID, surname, forename, nhsNumber);
                                    admittedPatients.add(patient);
                                    break;
                                }
                            }
                        } else {
                            System.err.println("Failed to fetch patients data");
                        }
                    }
                }
            } else {
                System.err.println("Failed to fetch admissions data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admittedPatients;
    }
}
