package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mGeneralMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeneralMstRepo extends JpaRepository<mGeneralMaster,Integer> {
    Optional<List<mGeneralMaster>> findBygmType(String gmType);
}
