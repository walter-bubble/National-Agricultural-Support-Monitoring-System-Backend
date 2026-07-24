package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.FarmingSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FarmingSeasonRepository extends JpaRepository<FarmingSeason,Long> {
    @Query("""
            SELECT f 
            FROM FarmingSeason f 
            WHERE f.closed=false 
            AND CURRENT_DATE BETWEEN f.startDate AND f.endDate
            """)
    Optional<FarmingSeason> findActiveSeason();
}
