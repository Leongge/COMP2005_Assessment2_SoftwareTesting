package com.leong.SDTP.controller;

import com.leong.SDTP.model.Admission;
import com.leong.SDTP.model.Employee;
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

import java.util.*;

@RestController
public class ApiController {

    private HttpClient httpClient;

    // Setter for HttpClient
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    @GetMapping("/f1/{patientId}")
    public List<Object> showPatientInfo(@PathVariable final String patientId) {
        List<Object> responseList = new ArrayList<>();

        try {
            // URL for the Patient API endpoint
            String patientApiLink = "https://web.socem.plymouth.ac.uk/COMP2005/api/patients/" + patientId;

            // Create HttpClient object
            HttpClient httpClient = HttpClients.createDefault();

            // Create HttpGet object with the URL
            HttpGet patientHttpGet = new HttpGet(patientApiLink);

            // Execute the request for Patient information
            HttpResponse patientResponse = httpClient.execute(patientHttpGet);

            // Check Patient response status code
            int patientStatusCode = patientResponse.getStatusLine().getStatusCode();
            if (patientStatusCode == 200) {
                // Parse Patient JSON response
                String patientJsonResponse = EntityUtils.toString(patientResponse.getEntity());
                JSONObject patientJsonObject = new JSONObject(patientJsonResponse);

                // Check if patient exists
                if (patientJsonObject.length() > 0) {
                    // Parse patient information
                    Patient patient = new Patient(
                            patientJsonObject.getInt("id"),
                            patientJsonObject.getString("surname"),
                            patientJsonObject.getString("forename"),
                            patientJsonObject.getString("nhsNumber")
                    );

                    responseList.add(patient);

                    // URL for the Admissions API endpoint
                    String admissionsApiLink = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";

                    // Create HttpGet object with the URL
                    HttpGet admissionsHttpGet = new HttpGet(admissionsApiLink);

                    // Execute the request for Admissions information
                    HttpResponse admissionsResponse = httpClient.execute(admissionsHttpGet);

                    // Check Admissions response status code
                    int admissionsStatusCode = admissionsResponse.getStatusLine().getStatusCode();
                    if (admissionsStatusCode == 200) {
                        // Parse Admissions JSON response
                        String admissionsJsonResponse = EntityUtils.toString(admissionsResponse.getEntity());
                        JSONArray admissionsJsonArray = new JSONArray(admissionsJsonResponse);

                        // Iterate through Admissions JSON array
                        for (int i = 0; i < admissionsJsonArray.length(); i++) {
                            JSONObject admissionJsonObject = admissionsJsonArray.getJSONObject(i);

                            // Check if admission belongs to the requested patient
                            if (admissionJsonObject.getInt("patientID") == patient.getId()) {
                                // Create Admission object
                                Admission admission = new Admission(
                                        admissionJsonObject.getInt("id"),
                                        admissionJsonObject.getString("admissionDate"),
                                        admissionJsonObject.getString("dischargeDate"),
                                        admissionJsonObject.getInt("patientID")
                                );

                                responseList.add(admission);
                            }
                        }
                    } else {
                        // Handle non-OK Admissions response
                        responseList.add("No admissions found");
                    }
                } else {
                    // Handle empty Patient response
                    responseList.add("No patient found");
                }
            } else {
                // Handle non-OK Patient response
                responseList.add("Patient API request failed with response code: " + patientStatusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseList.add("An error occurred: " + e.getMessage());
        }

        return responseList;
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

    @GetMapping("/f3")
    public Employee getMostAdmissionsStuffID() {
        Employee mostAdmissionsEmployee = null;

        try {
            String apiUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations";
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = new JSONArray(jsonResponse);

                // Count occurrences of employeeIDs
                Map<Integer, Integer> employeeCountMap = new HashMap<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int employeeID = jsonObject.getInt("employeeID");
                    employeeCountMap.put(employeeID, employeeCountMap.getOrDefault(employeeID, 0) + 1);
                }

                // Find employeeID with the most occurrences
                int maxCount = 0;
                int mostAdmissionsStuffID = -1;
                for (Map.Entry<Integer, Integer> entry : employeeCountMap.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        mostAdmissionsStuffID = entry.getKey();
                    }
                }

                // Fetch employee data using mostAdmissionsStuffID
                apiUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees/" + mostAdmissionsStuffID;
                httpGet = new HttpGet(apiUrl);
                response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    String employeeJson = EntityUtils.toString(response.getEntity());
                    JSONObject employeeJsonObject = new JSONObject(employeeJson);
                    mostAdmissionsEmployee = new Employee(
                            employeeJsonObject.getInt("id"),
                            employeeJsonObject.getString("surname"),
                            employeeJsonObject.getString("forename")
                    );
                } else {
                    System.err.println("HTTP request failed with response code: " + response.getStatusLine().getStatusCode());
                }
            } else {
                System.err.println("HTTP request failed with response code: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mostAdmissionsEmployee;
    }

    @GetMapping("/f4")
    public List<Employee> getEmployeesNotInAllocations() {
        List<Employee> employeesNotInAllocations = new ArrayList<>();

        try {
            String employeeApiUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees";
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(employeeApiUrl);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                String employeeResponse = EntityUtils.toString(response.getEntity());
                JSONArray employeeJsonArray = new JSONArray(employeeResponse);
                Set<Integer> employeeIdsInAllocations = new HashSet<>();

                // Fetch employee IDs from allocations
                String allocationApiUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations";
                httpGet = new HttpGet(allocationApiUrl);
                response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    String allocationResponse = EntityUtils.toString(response.getEntity());
                    JSONArray allocationJsonArray = new JSONArray(allocationResponse);

                    // Fetch employee IDs from allocations
                    for (int i = 0; i < allocationJsonArray.length(); i++) {
                        JSONObject allocationJsonObject = allocationJsonArray.getJSONObject(i);
                        employeeIdsInAllocations.add(allocationJsonObject.getInt("employeeID"));
                    }
                }

                // Filter employees that are not in allocations
                for (int i = 0; i < employeeJsonArray.length(); i++) {
                    JSONObject employeeJsonObject = employeeJsonArray.getJSONObject(i);
                    int employeeId = employeeJsonObject.getInt("id");
                    if (!employeeIdsInAllocations.contains(employeeId)) {
                        Employee employee = new Employee(
                                employeeId,
                                employeeJsonObject.getString("surname"),
                                employeeJsonObject.getString("forename")
                        );
                        employeesNotInAllocations.add(employee);
                    }
                }
            } else {
                System.err.println("HTTP request failed with response code: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeesNotInAllocations;
    }
}
