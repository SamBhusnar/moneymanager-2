package in.reer.moneymanager.repository;

import in.reer.moneymanager.entity.ExpenceEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenceEntity, Long> {
    // select * from expense where profile_id = ? order by date desc
    List<ExpenceEntity> findByProfileIdOrderByDateDesc(Long profileId);

    // select * from expense where profile_id = ? order by date desc limit 5
    List<ExpenceEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(e. amount) FROM ExpenceEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    // Select * from expense where profile_id = ? and date between ? and ? and name like ?
    List<ExpenceEntity> findByProfileIdAndDateBetweenAndExpenseNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String expenseName,
            Sort sort
    );

    // SELECT * FROM expense WHERE profile_id = ? AND date BETWEEN ? AND ?
    List<ExpenceEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    // Select * from expense where profile_id = ? and date = ?
    List<ExpenceEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

}
