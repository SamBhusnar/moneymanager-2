package in.reer.moneymanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${MY_API_TO_SEND_EMAIL}")
    private String apiKey;

    private final String apiUrl = "https://api.brevo.com/v3/smtp/email";

    /**
     * Sends an email using Brevo REST API.
     */
    public void sendEmailWithRestTemplate(String to, String subject, String body) {
        try {
            // Step 1: Create email JSON body
            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", Map.of("name", "Samadhan", "email", fromEmail));
            payload.put("to", List.of(Map.of("email", to)));
            payload.put("subject", subject);
            payload.put("htmlContent", body);

            // Step 2: Convert payload to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(payload);

            // Step 3: Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            // Step 4: Combine headers and body
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // Step 5: POST request to Brevo API
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Step 6: Handle response
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("✅ Email sent successfully to: " + to);
            } else {
                System.err.println("⚠️ Failed to send email: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert email payload to JSON: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Brevo: " + e.getMessage());
        }
    }
}
