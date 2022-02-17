package org.eida.SMART;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eida.SMART.m.User;
import org.eida.SMART.m.UserDAO;
import org.eida.SMART.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Log4j2
@Component
@RequiredArgsConstructor
public class InitRunner implements CommandLineRunner {


    private final UserDAO userDAO;

    private final StorageService storageService;

    private final PasswordEncoder passwordEncoder;


    public void run(String[] args) {
        LOGGER.info("The server has started successfully.");
        if (userDAO.findAll().isEmpty()) {
            DateFormat shortDate = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT);
            String hPass = passwordEncoder.encode("aaa");
            User sUser = new User("Super", "Admin", "Avignon", 84000, "Avignon", "s_admin@email.com", "0600000000", "Avignon", hPass, 0, true, shortDate.format(new Date()));
            userDAO.save(sUser);
            storageService.deleteAll();
            LOGGER.info("The Super User has been created.");
            LOGGER.info("The folder upload-dir has been cleared and is ready to be used.");
        } else {
            LOGGER.info("The Super User already exists.");
        }
    }

}