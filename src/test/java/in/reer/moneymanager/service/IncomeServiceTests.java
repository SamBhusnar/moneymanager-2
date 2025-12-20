package in.reer.moneymanager.service;

import in.reer.moneymanager.repository.CategoryRepository;
import in.reer.moneymanager.repository.ExpenseRepository;
import in.reer.moneymanager.repository.IncomeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
@DisplayName("learning testing")
public class IncomeServiceTests {
//    @Mock
//    private  IncomeRepository incomeRepository;
//    @Mock
//
//    private  ProfileService profileService;
//    @Mock
//
//    private  CategoryRepository categoryRepository;
//    @Mock

//    @InjectMocks
//    private  ExpenseRepository expenseRepository;

//    private IncomeService incomeService; // this is what we want to test

 
    @Test
    @DisplayName("first test method")
    void  firstTestMethod(){
        System.out.println("samadhan bhusnar is learning testing");
        assertEquals(1,31);
    }
}
