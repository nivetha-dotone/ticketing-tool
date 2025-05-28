package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mUserLogin_demo")
public class mUserLogin_demo implements UserDetails {

    @Id
    @Column(name = "logId", nullable = false,length = 9,unique = true)
    private Long logId;

    @JoinColumn(name = "mEmployeeMaster_empId")
    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private mEmployeeMaster emp_Id;

    @JoinColumn(name = "mCmeMaster")
    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private mClientCMEMaster cmeMaster;


    @Column(name = "userID", nullable = false, length = 60,unique = true)
    private String userID;

    @Column(name = "userPWD", nullable = false, length = -1) // For nvarchar(max)
    private String userPWD;

    @Column(name = "isactiveUser", columnDefinition = "TINYINT",nullable = false)
    private Boolean isactUser;


    private String role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
    @JsonIgnore
    @Override
    public String getPassword() {
        return userPWD;
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return userID;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }












//    @Column(name = "createdon", nullable = true,updatable = false)
//    private LocalDateTime createdon;
//
//    @Column(name = "lastloggedon", nullable = true)
//    private LocalDateTime lastloggedon;
//
//    @Column(name = "PWDRESETON", nullable = true)
//    private LocalDateTime pwdreseton;
//
//    @Column(name = "nextreston", nullable = true)
//    private LocalDateTime nextreston;
//    @PrePersist
//    public void prePersist() {
//        if (createdon == null) {
//            createdon = LocalDateTime.now();
//        }
//        if (lastloggedon == null) {
//            lastloggedon = LocalDateTime.now();
//        }
//    }
//    @PreUpdate
//    public void updateLoginDate() {
//        this.lastloggedon = LocalDateTime.now();
//    }
//    public void setPwdResetDate() {
//        if (this.pwdreseton == null) {
//            this.pwdreseton = LocalDateTime.now();
//        } else {
//            this.nextreston = LocalDateTime.now();
//        }
//    }








}
