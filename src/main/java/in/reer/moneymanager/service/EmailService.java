package in.reer.moneymanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${MY_API_TO_SEND_EMAIL}")
    private String apiKey;

    private final String apiUrl = "https://api.brevo.com/v3/smtp/email";

    /// don't write it now i will write it later when i swich from free to paid
    public void sendEmailWithJavaMailSender(String to, String subject, String body) {


    }

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


    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) {
        try {
            // 1) Convert file bytes to Base64
            String base64Attachment = java.util.Base64.getEncoder().encodeToString(attachment);

            // 2) Build Payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", Map.of("name", "Samadhan", "email", fromEmail));
            payload.put("to", List.of(Map.of("email", to)));
            payload.put("subject", subject);
            payload.put("htmlContent", body);

            // 3) Add attachment (Brevo API format)
            Map<String, Object> attachmentPart = new HashMap<>();
            attachmentPart.put("name", filename);  // file name
            attachmentPart.put("content", base64Attachment); // Base64 encoding

            payload.put("attachment", List.of(attachmentPart));

            // 4) Convert payload to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(payload);

            // 5) Preparing Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // 6) Execute POST call
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 7) Response Handling
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("📧 Email with attachment sent successfully!");
            } else {
                System.err.println("⚠️ Failed to send email: " + response.getStatusCode());
                System.err.println(response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email with attachment: " + e.getMessage());
        }
    }

}
