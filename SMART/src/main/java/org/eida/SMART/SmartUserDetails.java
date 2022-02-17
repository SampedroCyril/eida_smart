package org.eida.SMART;


import org.eida.SMART.m.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SmartUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String adress;
    private int postalCode;
    private String city;
    private String tel;
    private String agencyPlace;
    private boolean active;
    private List<GrantedAuthority> authorities;
    private int accessLevel;


    public SmartUserDetails() {
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAdress() {
        return adress;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getTel() {
        return tel;
    }

    public String getAgencyPlace() {
        return agencyPlace;
    }

    public SmartUserDetails(User u) {
        this.userName = u.getEmail();
        this.password = u.getPassword();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.adress = u.getAdress();
        this.postalCode = u.getPostalCode();
        this.city = u.getCity();
        this.tel = u.getTel();
        this.agencyPlace = u.getAgencyPlace();
        this.active = u.getActive();
        this.accessLevel = u.getAccessLevel();
        authorities = new LinkedList<GrantedAuthority>();

        if (accessLevel == 0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SADMIN"));
        } else if (accessLevel == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (accessLevel == 2) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
