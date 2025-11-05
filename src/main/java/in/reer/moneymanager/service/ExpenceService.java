package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.ExpenseDTO;
import in.reer.moneymanager.entity.CategoryEntity;
import in.reer.moneymanager.entity.ExpenceEntity;
import in.reer.moneymanager.entity.ProfileEntity;
import in.reer.moneymanager.repository.CategoryRepository;
import in.reer.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenceService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    // Retrieves all expense for current month/based on the start and end date
    public List<ExpenseDTO> getCurrentMonthExpenses() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<ExpenceEntity> byProfileIdAndDateBetween = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return byProfileIdAndDateBetween.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExpenseDTO addExpense(ExpenseDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        // check given category belong the current profile
        if (!category.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("You are not authorized to add this expense that belong to this category");
        }
        ExpenceEntity entity = toEntity(dto, profile, category);
        ExpenceEntity save = expenseRepository.save(entity);
        return toDto(save);

    }

    // delete expense by id of current profile
    public void deleteExpenseById(Long expenseId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenceEntity expenceEntity = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expenceEntity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("You are not authorized to delete this expense");
        }
        expenseRepository.delete(expenceEntity);
    }

    // Get latest 5 expence for crrent profile with the help this method : findTop5ByProfileIdOrderByDateDesc
    public List<ExpenseDTO> getLatestExpenses() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenceEntity> latestExpenses = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return latestExpenses.stream().map(this::toDto).collect(Collectors.toList());
    }

    // Get the total sum of expenses for current profile
    public BigDecimal getTotalExpenses() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return expenseRepository.findTotalExpenseByProfileId(profile.getId()) != null ? expenseRepository.findTotalExpenseByProfileId(profile.getId()) : BigDecimal.ZERO;
    }

    // filter expence
    public List<ExpenseDTO> filterExpence(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();
        List<ExpenceEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndExpenseNameContainingIgnoreCase(currentProfile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDto).toList();


    }

    // notifications
    public List<ExpenseDTO> getExpensesForProfileOnDate(Long profileId, LocalDate date) {
        List<ExpenceEntity> expenses = expenseRepository.findByProfileIdAndDate(profileId, date);
        return expenses.stream().map(this::toDto).collect(Collectors.toList());
        
    }

    // helper methods
    private ExpenceEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profile, CategoryEntity category) {
        return ExpenceEntity.builder()
                .id(expenseDTO.getId())
                .expenseName(expenseDTO.getName())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .icon(expenseDTO.getIcon())
                .profile(profile)
                .category(category)
                .profile(profile)
                .build();
    }

    // toDto
    private ExpenseDTO toDto(ExpenceEntity expenceEntity) {
        return ExpenseDTO.builder()
                .id(expenceEntity.getId())
                .name(expenceEntity.getExpenseName())
                .amount(expenceEntity.getAmount())
                .date(expenceEntity.getDate())
                .icon(expenceEntity.getIcon())
                .categoryId(expenceEntity.getCategory() != null ? expenceEntity.getCategory().getId() : null)
                .categoryName(expenceEntity.getCategory() != null ? expenceEntity.getCategory().getName() : "N/A")
                .createdAt(expenceEntity.getCreatedAt())
                .updatedAt(expenceEntity.getUpdatedAt())
                .build();
    }

}
