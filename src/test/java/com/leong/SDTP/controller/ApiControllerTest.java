package com.leong.SDTP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leong.SDTP.model.Admission;
import com.leong.SDTP.model.Employee;
import com.leong.SDTP.model.Patient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class ApiControllerTest {

    private ApiController apiController;
    private CloseableHttpClient httpClient;

    @BeforeEach
    public void setUp() {
        apiController = new ApiController();
        httpClient = Mockito.mock(CloseableHttpClient.class);
        apiController.setHttpClient(httpClient);
    }

    @Test
    void f1Test_WithID_1() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse patientResponse = Mockito.mock(CloseableHttpResponse.class);
        CloseableHttpResponse admissionsResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for patientResponse
        StatusLine patientStatusLine = Mockito.mock(StatusLine.class);
        when(patientStatusLine.getStatusCode()).thenReturn(200);

        // Mock patient JSON response
        String patientJson = "[{\"id\": 1,\"surname\": \"Robinson\",\"forename\": \"Viv\",\"nhsNumber\": \"1113335555\"}]";
        HttpEntity patientEntity = new StringEntity(patientJson);
        when(patientResponse.getEntity()).thenReturn(patientEntity);
        when(patientResponse.getStatusLine()).thenReturn(patientStatusLine);

        // Mock status code for admissionsResponse
        StatusLine admissionsStatusLine = Mockito.mock(StatusLine.class);
        when(admissionsStatusLine.getStatusCode()).thenReturn(200);

        // Mock admissions JSON response
        String admissionsJson = "[{\"id\": 2,\"admissionDate\": \"2020-12-07T22:14:00\",\"dischargeDate\": \"0001-01-01T00:00:00\",\"patientID\": 1}]";
        HttpEntity admissionsEntity = new StringEntity(admissionsJson);
        when(admissionsResponse.getEntity()).thenReturn(admissionsEntity);
        when(admissionsResponse.getStatusLine()).thenReturn(admissionsStatusLine);

        // Mock HttpClient.execute() method for patient and admissions requests
        when(httpClient.execute(any(HttpGet.class)))
                .thenReturn(patientResponse)
                .thenReturn(admissionsResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested
        List<Object> responseList = apiController.showPatientInfo("1");

        // Parse JSON strings to object lists
        ObjectMapper objectMapper = new ObjectMapper();
        List<Patient> expectedPatients = objectMapper.readValue(patientJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Patient.class));
        List<Admission> expectedAdmissions = objectMapper.readValue(admissionsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Admission.class));

        // Assertions
        // Compare 2 object content is equal
        assertEquals(expectedPatients.get(0), responseList.get(0));
        assertEquals(expectedAdmissions.get(0), responseList.get(1));

    }

    @Test
    void f1Test_WithID_2() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse patientResponse = Mockito.mock(CloseableHttpResponse.class);
        CloseableHttpResponse admissionsResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for patientResponse
        StatusLine patientStatusLine = Mockito.mock(StatusLine.class);
        when(patientStatusLine.getStatusCode()).thenReturn(200);

        // Mock patient JSON response
        String patientJson = "[{\"id\": 2,\"surname\": \"Carter\",\"forename\": \"Heather\",\"nhsNumber\": \"2224446666\"}]";
        HttpEntity patientEntity = new StringEntity(patientJson);
        when(patientResponse.getEntity()).thenReturn(patientEntity);
        when(patientResponse.getStatusLine()).thenReturn(patientStatusLine);

        // Mock status code for admissionsResponse
        StatusLine admissionsStatusLine = Mockito.mock(StatusLine.class);
        when(admissionsStatusLine.getStatusCode()).thenReturn(200);

        // Mock admissions JSON response
        String admissionsJson = "[{\"id\": 1,\"admissionDate\": \"2020-11-28T16:45:00\",\"dischargeDate\": " +
                "\"2020-11-28T23:56:00\",\"patientID\": 2},{\"id\": 3,\"admissionDate\": \"2021-09-23T21:50:00\"," +
                "\"dischargeDate\": \"2021-09-27T09:56:00\",\"patientID\": 2}]";
        HttpEntity admissionsEntity = new StringEntity(admissionsJson);
        when(admissionsResponse.getEntity()).thenReturn(admissionsEntity);
        when(admissionsResponse.getStatusLine()).thenReturn(admissionsStatusLine);

        // Mock HttpClient.execute() method for patient and admissions requests
        when(httpClient.execute(any(HttpGet.class)))
                .thenReturn(patientResponse)
                .thenReturn(admissionsResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested
        List<Object> responseList = apiController.showPatientInfo("2");

        // Parse JSON strings to object lists
        ObjectMapper objectMapper = new ObjectMapper();
        List<Patient> expectedPatients = objectMapper.readValue(patientJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Patient.class));
        List<Admission> expectedAdmissions = objectMapper.readValue(admissionsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Admission.class));

        // Assertions
        // Compare 2 object content is equal
        assertEquals(expectedPatients.get(0), responseList.get(0));
        assertEquals(expectedAdmissions.get(0), responseList.get(1));
        assertEquals(expectedAdmissions.get(1), responseList.get(2));
    }



    @Test
    void f1Test_WithID_3() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse patientResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for patientResponse
        StatusLine patientStatusLine = Mockito.mock(StatusLine.class);
        when(patientStatusLine.getStatusCode()).thenReturn(200);

        // Mock patient JSON response
        String patientJson = "[{\"id\": 3,\"surname\": \"Barnes\",\"forename\": \"Nicky\",\"nhsNumber\": \"6663338888\"}]";
        HttpEntity patientEntity = new StringEntity(patientJson);
        when(patientResponse.getEntity()).thenReturn(patientEntity);
        when(patientResponse.getStatusLine()).thenReturn(patientStatusLine);


        // Mock HttpClient.execute() method for patient and admissions requests
        when(httpClient.execute(any(HttpGet.class)))
                .thenReturn(patientResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested
        List<Object> responseList = apiController.showPatientInfo("3");

        // Parse JSON strings to object lists
        ObjectMapper objectMapper = new ObjectMapper();
        List<Patient> expectedPatients = objectMapper.readValue(patientJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Patient.class));

        // Assertions
        // Compare 2 object content is equal
        assertEquals(expectedPatients.get(0), responseList.get(0));
    }

    @Test
    void f1Test_WithID_6() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse patientResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock HttpClient.execute() method for patient request
        when(httpClient.execute(any(HttpGet.class)))
                .thenReturn(patientResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested
        List<Object> responseList = apiController.showPatientInfo("6");

        // Assertions
        assertEquals(1, responseList.size());
        assertEquals("Patient API request failed with response code: 404", responseList.get(0));
    }


    @Test
    void f1Test_WithInvalidInput() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse patientResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for patientResponse
        StatusLine patientStatusLine = Mockito.mock(StatusLine.class);
        when(patientStatusLine.getStatusCode()).thenReturn(400);

        // Mock HttpClient.execute() method for patient request
        when(httpClient.execute(any(HttpGet.class)))
                .thenReturn(patientResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested with invalid input
        List<Object> responseList = apiController.showPatientInfo("SDTP");

        // Assertions
        assertEquals(1, responseList.size());
        assertEquals("Patient API request failed with response code: 400", responseList.get(0));
    }


    @Test
    void f2Test_WithDate_2025() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse admissionsResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for admissionsResponse
        StatusLine admissionsStatusLine = Mockito.mock(StatusLine.class);
        when(admissionsStatusLine.getStatusCode()).thenReturn(200);

        // Mock admissions JSON response
        String admissionsJson = "[{\"id\": 2,\"admissionDate\": \"2020-12-07T22:14:00\",\"dischargeDate\": \"0001-01-01T00:00:00\",\"patientID\": 1}]";
        HttpEntity admissionsEntity = new StringEntity(admissionsJson);
        when(admissionsResponse.getEntity()).thenReturn(admissionsEntity);
        when(admissionsResponse.getStatusLine()).thenReturn(admissionsStatusLine);

        // Mock HttpClient.execute() method for admissions requests
        when(httpClient.execute(any(HttpGet.class))).thenReturn(admissionsResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested with different input dates
        List<ApiController.AdmissionRecord> responseList = apiController.filterAdmissions("{\"date\": \"2025-01-01T00:00:00\"}");

        // Assertions for the first date
        assertTrue(responseList.stream().allMatch(record ->
                record.getDischargeDate().equals("0001-01-01T00:00:00") ||
                        record.getDischargeDate().compareTo("2025-01-01T00:00:00") > 0));
    }

    @Test
    void f2Test_WithDate_2021() throws IOException {

        // Mock HttpClient and HttpResponse
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse admissionsResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for admissionsResponse
        StatusLine admissionsStatusLine = Mockito.mock(StatusLine.class);
        when(admissionsStatusLine.getStatusCode()).thenReturn(200);

        // Mock admissions JSON response
        String admissionsJson = "[{\"id\": 2,\"admissionDate\": \"2020-12-07T22:14:00\",\"dischargeDate\": \"0001-01-01T00:00:00\",\"patientID\": 1}]";
        HttpEntity admissionsEntity = new StringEntity(admissionsJson);
        when(admissionsResponse.getEntity()).thenReturn(admissionsEntity);
        when(admissionsResponse.getStatusLine()).thenReturn(admissionsStatusLine);

        // Mock HttpClient.execute() method for admissions requests
        when(httpClient.execute(any(HttpGet.class))).thenReturn(admissionsResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested with different input dates
        List<ApiController.AdmissionRecord> responseList = apiController.filterAdmissions("{\"date\": \"2021-01-01T00:00:00\"}");

        // Assertions for the second date
        assertTrue(responseList.stream().allMatch(record ->
                record.getDischargeDate().equals("0001-01-01T00:00:00") ||
                        record.getDischargeDate().compareTo("2021-01-01T00:00:00") > 0));
    }

    @Test
    void f3Test_getMostAdmissionsStuffID() throws IOException {
        // Mock HttpClient and HttpResponse for allocations API
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse allocationsResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for allocationsResponse
        StatusLine allocationsStatusLine = Mockito.mock(StatusLine.class);
        Mockito.when(allocationsStatusLine.getStatusCode()).thenReturn(200);

        // Mock allocations JSON response
        String allocationsJson = "[{\"id\": 1,\"admissionID\": 1,\"employeeID\": 4,\"startTime\": \"2020-11-28T16:45:00\",\"endTime\": \"2020-11-28T23:56:00\"}," +
                "{\"id\": 2,\"admissionID\": 3,\"employeeID\": 4,\"startTime\": \"2021-09-23T21:50:00\",\"endTime\": \"2021-09-24T09:50:00\"}," +
                "{\"id\": 3,\"admissionID\": 2,\"employeeID\": 6,\"startTime\": \"2020-12-07T22:14:00\",\"endTime\": \"2020-12-08T20:00:00\"}," +
                "{\"id\": 4,\"admissionID\": 2,\"employeeID\": 3,\"startTime\": \"2020-12-08T20:00:00\",\"endTime\": \"2020-12-09T20:00:00\"}]";
        HttpEntity allocationsEntity = new StringEntity(allocationsJson);
        Mockito.when(allocationsResponse.getEntity()).thenReturn(allocationsEntity);
        Mockito.when(allocationsResponse.getStatusLine()).thenReturn(allocationsStatusLine);

        // Mock HttpClient.execute() method for allocations request
        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(allocationsResponse);

        // Mock HttpClient and HttpResponse for employees API
        CloseableHttpResponse employeesResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for employeesResponse
        StatusLine employeesStatusLine = Mockito.mock(StatusLine.class);
        Mockito.when(employeesStatusLine.getStatusCode()).thenReturn(200);

        // Mock employees JSON response
        String employeesJson = "[{\"id\": 1,\"surname\": \"Finley\",\"forename\": \"Sarah\"}," +
                "{\"id\": 2,\"surname\": \"Jackson\",\"forename\": \"Robert\"}," +
                "{\"id\": 3,\"surname\": \"Allen\",\"forename\": \"Alice\"}," +
                "{\"id\": 4,\"surname\": \"Jones\",\"forename\": \"Sarah\"}," +
                "{\"id\": 5,\"surname\": \"Wicks\",\"forename\": \"Patrick\"}," +
                "{\"id\": 6,\"surname\": \"Smith\",\"forename\": \"Alice\"}]";
        HttpEntity employeesEntity = new StringEntity(employeesJson);
        Mockito.when(employeesResponse.getEntity()).thenReturn(employeesEntity);
        Mockito.when(employeesResponse.getStatusLine()).thenReturn(employeesStatusLine);

        // Mock HttpClient.execute() method for employees request
        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(employeesResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested
        Employee mostAdmissionsEmployee = apiController.getMostAdmissionsStuffID();

        // Assertions
        assertEquals(4, mostAdmissionsEmployee.getId());
        assertEquals("Jones", mostAdmissionsEmployee.getSurname());
        assertEquals("Sarah", mostAdmissionsEmployee.getForename());
    }

    @Test
    void f4Test_getEmployeesNotInAllocations() throws IOException {
        // Mock HttpClient and HttpResponse for employees API
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse employeesResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for employeesResponse
        StatusLine employeesStatusLine = Mockito.mock(StatusLine.class);
        Mockito.when(employeesStatusLine.getStatusCode()).thenReturn(200);

        // Mock employees JSON response
        String employeesJson = "[{\"id\": 1,\"surname\": \"Finley\",\"forename\": \"Sarah\"}," +
                "{\"id\": 2,\"surname\": \"Jackson\",\"forename\": \"Robert\"}," +
                "{\"id\": 3,\"surname\": \"Allen\",\"forename\": \"Alice\"}," +
                "{\"id\": 4,\"surname\": \"Jones\",\"forename\": \"Sarah\"}," +
                "{\"id\": 5,\"surname\": \"Wicks\",\"forename\": \"Patrick\"}," +
                "{\"id\": 6,\"surname\": \"Smith\",\"forename\": \"Alice\"}]";
        HttpEntity employeesEntity = new StringEntity(employeesJson);
        Mockito.when(employeesResponse.getEntity()).thenReturn(employeesEntity);
        Mockito.when(employeesResponse.getStatusLine()).thenReturn(employeesStatusLine);

        // Mock HttpClient.execute() method for employees request
        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(employeesResponse);

        // Mock HttpClient and HttpResponse for allocations API
        CloseableHttpResponse allocationsResponse = Mockito.mock(CloseableHttpResponse.class);

        // Mock status code for allocationsResponse
        StatusLine allocationsStatusLine = Mockito.mock(StatusLine.class);
        Mockito.when(allocationsStatusLine.getStatusCode()).thenReturn(200);

        // Mock allocations JSON response
        String allocationsJson = "[{\"id\": 1,\"admissionID\": 1,\"employeeID\": 4,\"startTime\": \"2020-11-28T16:45:00\",\"endTime\": \"2020-11-28T23:56:00\"}," +
                "{\"id\": 2,\"admissionID\": 3,\"employeeID\": 4,\"startTime\": \"2021-09-23T21:50:00\",\"endTime\": \"2021-09-24T09:50:00\"}," +
                "{\"id\": 3,\"admissionID\": 2,\"employeeID\": 6,\"startTime\": \"2020-12-07T22:14:00\",\"endTime\": \"2020-12-08T20:00:00\"}," +
                "{\"id\": 4,\"admissionID\": 2,\"employeeID\": 3,\"startTime\": \"2020-12-08T20:00:00\",\"endTime\": \"2020-12-09T20:00:00\"}]";
        HttpEntity allocationsEntity = new StringEntity(allocationsJson);
        Mockito.when(allocationsResponse.getEntity()).thenReturn(allocationsEntity);
        Mockito.when(allocationsResponse.getStatusLine()).thenReturn(allocationsStatusLine);

        // Mock HttpClient.execute() method for allocations request
        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(allocationsResponse);

        // Create instance of the class under test
        ApiController apiController = new ApiController();

        // Set the HttpClient instance in the controller
        apiController.setHttpClient(httpClient);

        // Call the method to be tested
        List<Employee> employeesNotInAllocations = apiController.getEmployeesNotInAllocations();

        // Assertions
        assertEquals(3, employeesNotInAllocations.size());
        assertEquals(1, employeesNotInAllocations.get(0).getId());
        assertEquals("Finley", employeesNotInAllocations.get(0).getSurname());
        assertEquals("Sarah", employeesNotInAllocations.get(0).getForename());
        assertEquals(2, employeesNotInAllocations.get(1).getId());
        assertEquals("Jackson", employeesNotInAllocations.get(1).getSurname());
        assertEquals("Robert", employeesNotInAllocations.get(1).getForename());
        assertEquals(5, employeesNotInAllocations.get(2).getId());
        assertEquals("Wicks", employeesNotInAllocations.get(2).getSurname());
        assertEquals("Patrick", employeesNotInAllocations.get(2).getForename());
    }
}