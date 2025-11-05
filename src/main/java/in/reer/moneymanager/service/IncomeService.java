package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.IncomeDTO;
import in.reer.moneymanager.entity.CategoryEntity;
import in.reer.moneymanager.entity.IncomeEntity;
import in.reer.moneymanager.entity.ProfileEntity;
import in.reer.moneymanager.repository.CategoryRepository;
import in.reer.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    // Retrieves all expense for current month/based on the start and end date
    public List<IncomeDTO> getCurrentMonthExpenses() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<IncomeEntity> byProfileIdAndDateBetween = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return byProfileIdAndDateBetween.stream().map(this::toDto).collect(Collectors.toList());
    }

    // Get latest 5 income for current profile with the help this method : findTop5ByProfileIdOrderByDateDesc
    public List<IncomeDTO> getLatestIncome() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> latestExpenses = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return latestExpenses.stream().map(this::toDto).collect(Collectors.toList());
    }

    // Get the total sum of income for current profile
    public BigDecimal getTotalIncomes() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return incomeRepository.findTotalExpenseByProfileId(profile.getId()) != null ? incomeRepository.findTotalExpenseByProfileId(profile.getId()) : BigDecimal.ZERO;
    }

    // delete income by id of current profile

    public void deleteExpenseById(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity incomeEntity = incomeRepository.findById(incomeId).orElseThrow(() -> new RuntimeException("Income not found"));
        if (!incomeEntity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("You are not authorized to delete this income");
        }
        incomeRepository.delete(incomeEntity);
    }

    public IncomeDTO addExpense(IncomeDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        // check given category belong the current profile
        if (!category.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("You are not authorized to add this income that belong to this category");
        }
        IncomeEntity entity = toEntity(dto, profile, category);
        IncomeEntity save = incomeRepository.save(entity);
        return toDto(save);

    }

    // filter incomes
    public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndIncomeNameContainingIgnoreCase(currentProfile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDto).toList();
    }


    // helper methods
    private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .id(incomeDTO.getId())
                .incomeName(incomeDTO.getName())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .icon(incomeDTO.getIcon())
                .profile(profile)
                .category(category)
                .profile(profile)
                .build();
    }

    private IncomeDTO toDto(IncomeEntity incomeEntity) {
        return IncomeDTO.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getIncomeName())
                .amount(incomeEntity.getAmount())
                .date(incomeEntity.getDate())
                .icon(incomeEntity.getIcon())
                .categoryId(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId() : null)
                .categoryName(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getName() : "N/A")
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
}
