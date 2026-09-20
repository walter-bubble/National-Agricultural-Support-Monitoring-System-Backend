package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.model.LoanPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface LoanPackageRepository extends JpaRepository<LoanPackage,Long> {
    @Query("""
        SELECT COALESCE(SUM(lp.amount), 0)
        FROM LoanPackage lp
        WHERE lp.farmingSeason = :season
    """)
    BigDecimal sumPackageAmountsByFarmingSeason(
            @Param("season") FarmingSeason season
    );
}
