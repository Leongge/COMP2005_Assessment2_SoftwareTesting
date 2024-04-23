package com.leong.SDTP;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class SdtpApplication extends JFrame {
	private JTextField inputField;
	private JButton searchButton;
	private JTextArea resultArea;
	private JLabel patientIdLabel;
	private JLabel surnameLabel;
	private JLabel forenameLabel;
	private JLabel nhsnumberLabel;

	public SdtpApplication() {
		setTitle("Patient Search");
		setSize(700, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
		addListeners();
		setVisible(true);
	}

	private void initComponents() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc =new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 0;

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		inputField = new JTextField(10);
		searchButton = new JButton("Search");
		searchButton.setPreferredSize(new Dimension(100, searchButton.getPreferredSize().height));
		inputPanel.add(inputField);
		inputPanel.add(searchButton, gbc);
		mainPanel.add(inputPanel);

		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;

		JPanel detailsPanel = new JPanel(new GridLayout(2, 2));
		detailsPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
		patientIdLabel = new JLabel("Patient ID: ");
		surnameLabel = new JLabel("Surname: ");
		forenameLabel = new JLabel("Forename: ");
		nhsnumberLabel = new JLabel("nhsnumberLabel: ");
		detailsPanel.add(patientIdLabel);
		detailsPanel.add(surnameLabel);
		detailsPanel.add(forenameLabel);
		detailsPanel.add(nhsnumberLabel);
		mainPanel.add(detailsPanel, gbc);

		gbc.gridy = 3;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 2;

		resultArea = new JTextArea();
		resultArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(resultArea);
		mainPanel.add(scrollPane,gbc);

		add(mainPanel);
	}


	private void addListeners() {
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputField.getText().trim();
				if (!input.isEmpty()) {
					fetchAndDisplayData(input);
				} else {
					JOptionPane.showMessageDialog(SdtpApplication.this, "Please enter a patient ID");
				}
			}
		});
	}

	private void fetchAndDisplayData(String patientId) {
		String apiUrl = "http://localhost:8080/f1/" + patientId;

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				JSONArray jsonArray = new JSONArray(response.toString());

				if (jsonArray.length() > 0) {
					JSONObject jsonObject = jsonArray.getJSONObject(0);
//					String result = "Patient Details:\n" +
//							"ID: " + jsonObject.getInt("id") + "\n" +
//							"Surname: " + jsonObject.getString("surname") + "\n" +
//							"Forename: " + jsonObject.getString("forename") + "\n" +
//							"NHS Number: " + jsonObject.getString("nhsNumber");
					patientIdLabel.setText("Patient ID: " + jsonObject.getInt("id"));
					surnameLabel.setText("Surname: " + jsonObject.getString("surname"));
					forenameLabel.setText("Forename: " + jsonObject.getString("forename"));
					nhsnumberLabel.setText("NHS Number: " + jsonObject.getString("nhsNumber"));

					String result="";
					if (jsonArray.length() > 1) {
						result += "\n\nAdmission Records:";
						for (int i = 1; i < jsonArray.length(); i++) {
							JSONObject admissionRecord = jsonArray.getJSONObject(i);
							result += "\n- Admission ID: " + admissionRecord.getInt("id") +
									", Admission Date: " + admissionRecord.getString("admissionDate") +
									", Discharge Date: " + admissionRecord.getString("dischargeDate");
						}
					}else{
						result = "No admission records found for patient ID: " + patientId;
					}

					resultArea.setText(result);
				} else {
					resultArea.setText("No data found for patient ID: " + patientId);
				}
			} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
				resultArea.setText("Please don't input any symbol");
			} else {
				resultArea.setText("Failed to fetch data from the API. Response code: " + responseCode);
			}

			connection.disconnect();
		} catch (JSONException e) {
			e.printStackTrace();
			resultArea.setText("Invalid patient ID");
			patientIdLabel.setText("Patient ID: ");
			surnameLabel.setText("Surname: " );
			forenameLabel.setText("Forename: " );
			nhsnumberLabel.setText("NHS Number: ");
		}catch (IOException i){
			i.printStackTrace();
			resultArea.setText("Invalid patient ID");
		}
	}

	public static void main(String[] args) {

		SpringApplication.run(SdtpApplication.class, args);
	}

}
