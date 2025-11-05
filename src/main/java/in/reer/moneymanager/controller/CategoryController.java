package in.reer.moneymanager.controller;

import in.reer.moneymanager.dto.CategoryDTO;
import in.reer.moneymanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    // save category

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO savedCategoryDTO = categoryService.saveCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all categories for current profile
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategoriesForCurrentProfile() {
        List<CategoryDTO> categories = categoryService.getCategoriesForCurrentProfile();
        return ResponseEntity.ok(categories);
    }

    // get categories by type for current profile
    @GetMapping("/{type}")
    public List<CategoryDTO> getCategoriesByTypeForCurrentProfile(@PathVariable String type) {
        return categoryService.getCategoriesByTypeForCurrentProfile(type);
    }

    // update category
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategoryDTO = categoryService.updateCategoryByProfileId(categoryId, categoryDTO);
            return ResponseEntity.ok(updatedCategoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
