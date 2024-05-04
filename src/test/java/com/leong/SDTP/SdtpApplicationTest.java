package com.leong.SDTP;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SdtpApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Spy
    @InjectMocks
    private SdtpApplication sdtpApplication;
    @Test
    void testFetchAndDisplayDataForId1() throws Exception {
        // Set Input
        String input = "1";
        sdtpApplication.getInputField().setText(input);

        // Call function
        sdtpApplication.fetchAndDisplayData(input);

        // Verify gui component update as expected
        assertEquals("Patient ID: 1", sdtpApplication.getPatientIdLabel().getText());
        assertEquals("Surname: Robinson", sdtpApplication.getSurnameLabel().getText());
        assertEquals("Forename: Viv", sdtpApplication.getForenameLabel().getText());
        assertEquals("NHS Number: 1113335555", sdtpApplication.getNhsnumberLabel().getText());
        assertEquals("\n\nAdmission Records:\n- Admission ID: 2, Admission Date: 2020-12-07T22:14:00, " +
                "Discharge Date: 0001-01-01T00:00:00", sdtpApplication.getResultArea().getText());
    }

    @Test
    void testFetchAndDisplayDataForId2() throws Exception {
        // Set Input
        String input = "2";
        sdtpApplication.getInputField().setText(input);

        // Call function
        sdtpApplication.fetchAndDisplayData(input);

        // Verify gui component update as expected
        assertEquals("Patient ID: 2", sdtpApplication.getPatientIdLabel().getText());
        assertEquals("Surname: Carter", sdtpApplication.getSurnameLabel().getText());
        assertEquals("Forename: Heather", sdtpApplication.getForenameLabel().getText());
        assertEquals("NHS Number: 2224446666", sdtpApplication.getNhsnumberLabel().getText());
        assertEquals("\n\nAdmission Records:\n- Admission ID: 1, Admission Date: 2020-11-28T16:45:00, " +
                "Discharge Date: 2020-11-28T23:56:00\n- Admission ID: 3, Admission Date: 2021-09-23T21:50:00, " +
                "Discharge Date: 2021-09-27T09:56:00", sdtpApplication.getResultArea().getText());
    }

    @Test
    void testFetchAndDisplayData_PatientID3() throws Exception {
        // Set Input
        String input = "3";
        sdtpApplication.getInputField().setText(input);

        // Call function
        sdtpApplication.fetchAndDisplayData(input);

        // Verify gui component update as expected
        assertEquals("Patient ID: 3", sdtpApplication.getPatientIdLabel().getText());
        assertEquals("Surname: Barnes", sdtpApplication.getSurnameLabel().getText());
        assertEquals("Forename: Nicky", sdtpApplication.getForenameLabel().getText());
        assertEquals("NHS Number: 6663338888", sdtpApplication.getNhsnumberLabel().getText());
        assertEquals("No admission records found for patient ID: 3", sdtpApplication.getResultArea().getText());
    }

    @Test
    void testFetchAndDisplayData_InvalidId() throws Exception {
        // Set Input
        String input = "6";
        sdtpApplication.getInputField().setText(input);

        // Call function
        sdtpApplication.fetchAndDisplayData(input);

        // Verify gui component update as expected
        assertEquals("Patient ID: ", sdtpApplication.getPatientIdLabel().getText());
        assertEquals("Surname: ", sdtpApplication.getSurnameLabel().getText());
        assertEquals("Forename: ", sdtpApplication.getForenameLabel().getText());
        assertEquals("NHS Number: ", sdtpApplication.getNhsnumberLabel().getText());
        assertEquals("Invalid patient ID", sdtpApplication.getResultArea().getText());
    }

    @Test
    void testFetchAndDisplayData_Sysmbol() throws Exception {
        // Set Invalid Input
        String input = "/";
        sdtpApplication.getInputField().setText(input);

        // Call function
        sdtpApplication.fetchAndDisplayData(input);

        // Verify gui component update as expected
        assertEquals("Patient ID: ", sdtpApplication.getPatientIdLabel().getText());
        assertEquals("Surname: ", sdtpApplication.getSurnameLabel().getText());
        assertEquals("Forename: ", sdtpApplication.getForenameLabel().getText());
        assertEquals("NHS Number: ", sdtpApplication.getNhsnumberLabel().getText());
        assertEquals("Please don't input any symbol", sdtpApplication.getResultArea().getText());
    }

    @Test
    void testFetchAndDisplayData_InvalidInput() throws Exception {
        // Set Input
        String input = "%";
        sdtpApplication.getInputField().setText(input);

        // Call function
        sdtpApplication.fetchAndDisplayData(input);

        // Verify gui component update as expected
        assertEquals("Patient ID: ", sdtpApplication.getPatientIdLabel().getText());
        assertEquals("Surname: ", sdtpApplication.getSurnameLabel().getText());
        assertEquals("Forename: ", sdtpApplication.getForenameLabel().getText());
        assertEquals("NHS Number: ", sdtpApplication.getNhsnumberLabel().getText());
        assertEquals("Failed to fetch data from the API. Response code: 400",
                sdtpApplication.getResultArea().getText());
    }

}