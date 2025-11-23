package in.reer.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String type;// category allowed only one either income or expense
    private String icon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long profileId;
}
