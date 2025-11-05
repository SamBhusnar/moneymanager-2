package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.AuthDTO;
import in.reer.moneymanager.dto.ProfileDTO;
import in.reer.moneymanager.entity.ProfileEntity;
import in.reer.moneymanager.repository.ProfileRepository;
import in.reer.moneymanager.util.JWTUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    @Value("${app.activation.url}")
    private String activationUrl;

    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity newProfileEntity = toEntity(profileDTO);
        newProfileEntity.setActivationToken(UUID.randomUUID().toString());
        newProfileEntity = profileRepository.save(newProfileEntity);
        // send activation email
        String activationLink = activationUrl + "/api/v1.0/activate?token=" + newProfileEntity.getActivationToken();
        String subject = "Activate your  money manager account";
        String body = "Please click on the link to activate your account: " + activationLink;
        emailService.sendEmail(newProfileEntity.getEmail(), subject, body);
        return toDTO(newProfileEntity);

    }

    public boolean activateProfile(String activationToken) {
        Optional<ProfileEntity> optionalProfileEntity = profileRepository.findByActivationToken(activationToken);
        if (optionalProfileEntity.isPresent()) {
            ProfileEntity profileEntity = optionalProfileEntity.get();
            profileEntity.setIsActive(true);
            profileEntity.setActivationToken(null);
            profileRepository.save(profileEntity);
            return true;
        }
        return false;
    }

    // helper methods
    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())

                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())

                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

    public boolean isAccountActive(String email) {
        return profileRepository.findByEmail(email).map(ProfileEntity::getIsActive).orElse(false);

    }

    public ProfileEntity getCurrentProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(" Profile not found with " + email + " email"));


    }

    public ProfileDTO getCurrentProfileDTO(String email) {
        ProfileEntity profileEntity = null;
        if (email == null) {
            profileEntity = getCurrentProfile();
        } else {
            profileEntity = profileRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(" Profile not found with " + email + " email"));

        }
        return toDTO(profileEntity);

    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            String jwtToken = jwtUtility.generateToken(authDTO.getEmail());
            return Map.of("token", jwtToken, "user", getCurrentProfileDTO(authDTO.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
