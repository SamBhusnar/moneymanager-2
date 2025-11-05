package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.CategoryDTO;
import in.reer.moneymanager.entity.CategoryEntity;
import in.reer.moneymanager.entity.ProfileEntity;
import in.reer.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    //save category
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profileService.getCurrentProfile().getId())) {
            throw new RuntimeException("Category with this name already exists");
        }
        CategoryEntity categoryEntity = toEntity(categoryDTO, profileService.getCurrentProfile());
        CategoryEntity save = categoryRepository.save(categoryEntity);
        return toDto(save);
    }

    // get Categories for current profile
    public List<CategoryDTO> getCategoriesForCurrentProfile() {
        return categoryRepository.findByProfileId(profileService.getCurrentProfile().getId()).stream().map(this::toDto).collect(Collectors.toList());
    }

    // helper method
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {
        return CategoryEntity.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .type(categoryDTO.getType())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .build();
    }

    // update Dto
    public CategoryDTO updateCategoryByProfileId(Long id, CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.findByIdAndProfileId(id, profileService.getCurrentProfile().getId()).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setType(categoryDTO.getType());
        categoryEntity.setIcon(categoryDTO.getIcon());
        CategoryEntity save = categoryRepository.save(categoryEntity);
        return toDto(save);
    }

    private CategoryDTO toDto(CategoryEntity category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .profileId(category.getProfile().getId())
                .name(category.getName())
                .type(category.getType())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public List<CategoryDTO> getCategoriesByTypeForCurrentProfile(String type) {
        return categoryRepository.findByTypeAndProfileId(type, profileService.getCurrentProfile().getId()).stream().map(this::toDto).collect(Collectors.toList());

    }

}
