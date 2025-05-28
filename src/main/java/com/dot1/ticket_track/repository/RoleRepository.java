package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mRoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<mRoleMaster,Integer> {
    Optional<mRoleMaster> findByroleName(String roleName);

}
