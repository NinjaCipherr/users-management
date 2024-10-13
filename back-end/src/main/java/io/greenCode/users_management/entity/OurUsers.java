package io.greenCode.users_management.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ourusers")
public class OurUsers implements UserDetails {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String city;
    private String role;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // dung List.of de tao ra 1 danh sach bat bien
        // SimpleGrantedAuthority dung de xac dinh quyen nguoi dung trong ung dung
        // khi ta return nhu nay la dang return lai 1 cai object GrantedAuthority
        // nghia la khi ma ta goi len 1 user. sau khi ma nguoi dung co gang truy cap 1
        // trang yeu cau quyen thi springboot se goi cai getAuthorities () len
        // no se tra ve 1 list chua SimpleGrantedAuthority va quyen cua no
        return List.of(new SimpleGrantedAuthority(role));
    }

    public String getUsername() {
        return null;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
