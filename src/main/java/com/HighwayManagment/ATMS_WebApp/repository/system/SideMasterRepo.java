package com.HighwayManagment.ATMS_WebApp.repository.system;
import com.HighwayManagment.ATMS_WebApp.entity.system.SideMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface SideMasterRepo extends JpaRepository<SideMaster, Integer> {

    boolean existsBySideName(String sideName);

    List<SideMaster> findBySideDeleteStatusFalse();

    Optional<SideMaster> findBySideIdAndSideDeleteStatusFalse(Integer sideId);
}