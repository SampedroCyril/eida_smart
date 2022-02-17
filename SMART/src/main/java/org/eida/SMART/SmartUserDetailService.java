package org.eida.SMART;

import org.eida.SMART.m.User;
import org.eida.SMART.m.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

;

@Service
public class SmartUserDetailService implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User u = userDAO.findByEmail(userName);
        if (u == null) {
            throw new UsernameNotFoundException("Not found: " + userName);
        }
        return new SmartUserDetails(u);
    }

}
