package org.eida.SMART.c;

import lombok.RequiredArgsConstructor;
import org.eida.SMART.GenNumAccount;
import org.eida.SMART.m.*;
import org.eida.SMART.storage.StorageFileNotFoundException;
import org.eida.SMART.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    /**
     * Storage service.
     */
    private final StorageService storageService;

    /**
     * Password Encoder service.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * User DAO service.
     */
    private final UserDAO userDAO;

    /**
     * Account DAO service.
     */
    private final AccountDAO accountDAO;

    /**
     * Category DAO service.
     */
    private final CategoryDAO categoryDAO;

    /**
     * Resources DAO service.
     */
    private final ResourcesDAO resourcesDAO;

    /**
     * Contact DAO service.
     */
    private final ContactDAO contactDAO;

    /**
     * Operation DAO service
     */
    private final OperationDAO operationDAO;

    /**
     * Mapping for the login page.
     *
     * @return view - load Login template.
     */
    @RequestMapping(value = "/")
    public String home() {
        return "login";
    }

    /**
     * Send a request to Spring Security for set the login page as a custom page
     * and retrieve all usernames.
     *
     * @return view - load Login template.
     */
    @RequestMapping(value = "login")
    public String login() {
        return "login";
    }

    /**
     * Mapping for Super Admin and displays all actives admin in a table.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load Super User template.
     */
    @RequestMapping(value = "/sadmin")
    public String sAdmin(Model model) {
        List<User> admins = userDAO.findByActiveAndAccessLevel(true, 1);
        model.addAttribute("admins", admins);
        return "/sadmin";
    }

    /**
     * Displays all disable admins in a table.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load Super User archives template.
     */
    @RequestMapping(value = "/sadmin/archiveAdmin")
    public String archiveAdmin(Model model) {
        List<User> admins = userDAO.findByActiveAndAccessLevel(false, 1);
        model.addAttribute("admins", admins);
        return "/archiveAdmin";
    }

    /**
     * Displays all disable clients in a table.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load Admin archives template.
     */
    @RequestMapping(value = "/admin/archiveClient")
    public String archiveClient(Model model) {
        List<User> clients = userDAO.findByActiveAndAccessLevel(false, 2);
        model.addAttribute("clients", clients);
        return "/archiveClient";
    }

    /**
     * Mapping for Admins and displays all actives clients in a table,
     * retrieval of categories, resources and messages sent by clients.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load admin template.
     */
    @RequestMapping(value = "/admin")
    public String admin(Model model) {
        List<User> clients = userDAO.findByActiveAndAccessLevel(true, 2);
        model.addAttribute("clients", clients);
        List<Resources> resources = resourcesDAO.findAll();
        model.addAttribute("resources", resources);
        List<Category> categs = categoryDAO.findAll();
        model.addAttribute("categs", categs);
        List<Contact> contacts = contactDAO.findAll();
        model.addAttribute("contacts", contacts);
        return "/admin";
    }

    /**
     * Use for grabbing the client id and redirect to consultationComptes template.
     *
     * @return view - load consultationComptes template.
     */
    @PostMapping(value = "/consult")
    public String consultation(@RequestParam(name = "consultClientId") Integer consultClientId,
                               RedirectAttributes redirectAttributes) {

        Optional<User> opClient = userDAO.findById(consultClientId);
        User client = opClient.get();
        redirectAttributes.addFlashAttribute(client);

        return "redirect:/admin/consultationComptes";
    }

    /**
     * Mapping for the client page and displays their accounts and their operations.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load consultationComptes template.
     */
    @RequestMapping(value = "/admin/consultationComptes")
    public String consultationComptes(User client, Model model) {
        List<Account> accounts = accountDAO.findByUser(client);
        model.addAttribute("accounts", accounts);
        List<Operation> operations = operationDAO.findByOperationAccount(accounts.get(0));
        model.addAttribute("operations", operations);
        if (accounts.size() == 2) {
            List<Operation> operationsE = operationDAO.findByOperationAccount(accounts.get(1));
            model.addAttribute("operationsE", operationsE);
        }
        return "/consultationComptes";
    }

    /**
     * Mapping for the client page and displays their accounts and their operations.
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load client template.
     */
    @RequestMapping(value = "/client")
    public String utilisateurs(Model model, Principal principal) {
        User user = userDAO.findByEmail(currentUserName(principal));
        List<Account> accounts = accountDAO.findByUser(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("ancienSoldeC", accounts.get(0).getMoney());
        if (accounts.size() > 1)
            model.addAttribute("ancienSoldeE", accounts.get(1).getMoney());
        List<Operation> operations = operationDAO.findByOperationAccount(accounts.get(0));
        model.addAttribute("operations", operations);
        if (accounts.size() == 2) {
            List<Operation> operationsE = operationDAO.findByOperationAccount(accounts.get(1));
            model.addAttribute("operationsE", operationsE);
        }
        return "/client";
    }

    /**
     * Mapping for the contact page as a client
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load contact template.
     */
    @RequestMapping(value = "/client/contact")
    public String contacte(Model model) {
        return "/contact";
    }

    /**
     * Display of resources according to their categories
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load ressource template.
     */
    @RequestMapping(value = "/client/ressource")
    public String ressources(Model model) {
        List<Category> categs = categoryDAO.findAll();
        model.addAttribute("categs", categs);
        List<Resources> resources = resourcesDAO.findAll();
        model.addAttribute("resources", resources);
        return "/ressource";
    }

    /**
     * Display of resources according to their categories.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load ressource template.
     */
    @RequestMapping(value = "/selectcateg")
    public String ressourcescat(@RequestParam(name = "cats") int c,
                                Model model) {
        List<Category> categs = categoryDAO.findAll();
        model.addAttribute("categs", categs);
        Optional<Category> category = categoryDAO.findById(c);
        Category categ = category.get();
        List<Resources> resources = resourcesDAO.findByCategory(categ);
        model.addAttribute("resources", resources);
        return "/ressource";
    }

    /**
     * Register in mysql database all contact messages.
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load client template.
     */
    @RequestMapping(value = "/contacter")
    public String contacter(@RequestParam(name = "object") String object,
                            @RequestParam(name = "text") String text,
                            Model model, Principal principal) {

        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findByEmail(currentUserName(principal));
        Contact contact = new Contact(object, text, shortDate.format(new Date()), user);
        contactDAO.save(contact);
        return "redirect:/client";
    }

    /**
     * Del handler for messages.
     *
     * @return view - load admin template.
     */
    @RequestMapping(value = "/delmsg")
    public String delMessages(int contactId) {
        contactDAO.deleteById(contactId);
        return "redirect:/admin";
    }

    /**
     * Del handler for ressources.
     *
     * @return view - load admin template.
     * @throws IOException
     */
    @RequestMapping(value = "/delres")
    public String delRes(int resId) throws IOException {
        Optional<Resources> r = resourcesDAO.findById(resId);
        Resources res = r.get();
        if (!res.getFile().equals("")) {
            storageService.deleteFile(res.getFile());
        }
        resourcesDAO.deleteById(resId);
        return "redirect:/admin";
    }

    /**
     * Delete a client from database
     *
     * @return view - load archiveClient template.
     */
    @RequestMapping(value = "/delclient")
    public String delClient(int clientId) {
        User user = userDAO.findById(clientId);
        List<Account> accounts = accountDAO.findByUser(user);
        List<Contact> contacts = contactDAO.findByUser(user);
        for (int i = 0; i < accounts.size(); i++) {
            List<Operation> operations = operationDAO.findByOperationAccount(accounts.get(i));
            for (int q = 0; q < operations.size(); q++) {
                operationDAO.deleteById(operations.get(q).getId());
            }
            accountDAO.deleteById(accounts.get(i).getId());
        }
        for (int j = 0; j < contacts.size(); j++) {
            contactDAO.deleteById(contacts.get(j).getId());
        }
        userDAO.deleteById(clientId);
        return "redirect:/admin/archiveClient";
    }

    /**
     * Delete an admin from database
     *
     * @return view - load archiveAdmin template.
     */
    @RequestMapping(value = "/deladmin")
    public String delAdmins(int adminId) {
        User user = userDAO.findById(adminId);
        List<User> admins = userDAO.findByAccessLevel(1);
        List<Resources> res = resourcesDAO.findByUser(user);
        int[] idList = new int[admins.size()];
        for (int i = 0; i < admins.size(); i++) {
            idList[i] = admins.get(i).getId();
        }
        Arrays.sort(idList);
        int newestId = idList[idList.length - 1];

        if (adminId == newestId) {
            newestId = idList[idList.length - 2];
        }

        User newestAdmin = userDAO.findById(newestId);
        for (int j = 0; j < res.size(); j++) {
            res.get(j).setUser(newestAdmin);
        }
        userDAO.deleteById(adminId);
        return "redirect:/sadmin/archiveAdmin";
    }

    /**
     * Add Categories.
     *
     * @param title - String
     * @return view - load admin template.
     */
    @RequestMapping(value = "/addCategory")
    public String addCategory(@RequestParam(name = "title") String title) {
        Category category = new Category(title);
        categoryDAO.save(category);
        return "redirect:/admin";
    }

    /**
     * This is the disable handler for users.
     *
     * @return view - load admin template.
     */
    @PostMapping(value = "/disableuser")
    public String disableUser(int clientId) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findById(clientId);
        List<Account> accounts = accountDAO.findByUser(user);
        for (int i = 0; i < accounts.size(); i++) {
            accounts.get(i).setActive(false);
            accountDAO.save(accounts.get(i));
        }
        user.setDisableDate(shortDate.format(new Date()));
        user.setActive(false);
        userDAO.save(user);
        return "redirect:/admin";
    }

    /**
     * This is the reactivation handler Admins to enable clients.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load archiveClient template.
     */
    @PostMapping(value = "/enableclient")
    public String enableClient(int clientId, Model model) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findById(clientId);
        List<Account> accounts = accountDAO.findByUser(user);
        for (int i = 0; i < accounts.size(); i++) {
            accounts.get(i).setActive(true);
            accountDAO.save(accounts.get(i));
        }
        user.setDisableDate(shortDate.format(new Date()));
        user.setActive(true);
        userDAO.save(user);
        return "redirect:/admin/archiveClient";
    }

    /**
     * This is the reactivation handler Sadmins to enable admins.
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load archiveAdmin template.
     */
    @PostMapping(value = "/enableadmin")
    public String enableAdmin(int adminId, Model model) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findById(adminId);
        user.setDisableDate(shortDate.format(new Date()));
        user.setActive(true);
        userDAO.save(user);
        return "redirect:/sadmin/archiveAdmin";
    }

    /**
     * This is the disable handler for admins.
     *
     * @param model - as a Model to add the DB items to the template attributes.
     * @return view - load sadmin template.
     */
    @PostMapping(value = "/disableadmin")
    public String disableAdmin(int adminId, Model model, RedirectAttributes redirAttrs) {
        List<User> lastUser = userDAO.findByAccessLevel(1);
        if (lastUser.size() <= 1) {
            redirAttrs.addFlashAttribute("error", "Vous ne pouvez pas supprimer cet Administrateur. Des ressources sont liées à celui-ci.");
            return "redirect:/sadmin";
        } else {
            DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            User user = userDAO.findById(adminId);
            user.setDisableDate(shortDate.format(new Date()));
            user.setActive(false);
            userDAO.save(user);
            return "redirect:/sadmin";
        }
    }

    /**
     * This is the handler to update admins.
     *
     * @return view - load sadmin template.
     */
    @PostMapping(value = "/updateadmin")
    public String updateAdmin(@RequestParam(name = "agency2") String agencyPlace,
                              @RequestParam(name = "firstName2") String firstName,
                              @RequestParam(name = "lastName2") String lastName,
                              @RequestParam(name = "tel2") String tel,
                              @RequestParam(name = "email2") String email,
                              @RequestParam(name = "adress2") String adress,
                              @RequestParam(name = "postalCode2") int postalCode,
                              @RequestParam(name = "city2") String city,
                              @RequestParam(name = "password2") String password, int adminId,
                              RedirectAttributes redirAttrs) {
        User user = userDAO.findById(adminId);
        if (userDAO.existsByEmail(email) && !user.getEmail().equals(email)) {
            redirAttrs.addFlashAttribute("error", "Cette adresse mail existe déjà.");
            return "redirect:/sadmin";
        } else {
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setAgencyPlace(agencyPlace);
                user.setLastName(lastName);
                user.setFirstName(firstName);
                user.setTel(tel);
                user.setEmail(email);
                user.setAdress(adress);
                user.setPostalCode(postalCode);
                user.setCity(city);
                userDAO.save(user);
                return "redirect:/sadmin";
            } else {
                redirAttrs.addFlashAttribute("error", "Vous n'avez pas rentré le bon mot de passe.");
                return "redirect:/sadmin";
            }
        }
    }

    /**
     * Updating the admin password by a super admin.
     *
     * @return view - load sadmin template.
     */
    @PostMapping(value = "/updateadminmdp")
    public String updateAdminMdp(@RequestParam(name = "password3") String oldPassword,
                                 @RequestParam(name = "password4") String newPassword, int adminIdMdp, RedirectAttributes redirAttrs) {
        User user = userDAO.findById(adminIdMdp);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            newPassword = passwordEncoder.encode(newPassword);
            user.setPassword(newPassword);
            userDAO.save(user);
            return "redirect:/sadmin";
        } else {
            redirAttrs.addFlashAttribute("error", "Vous n'avez pas rentré le bon mot de passe");
            return "redirect:/sadmin";
        }
    }

    /**
     * This is the handler to update clients.
     *
     * @return view - load admin template.
     */
    @PostMapping(value = "/updateclient")
    public String updateClient(@RequestParam(name = "agency2") String agencyPlace,
                               @RequestParam(name = "firstName2") String firstName,
                               @RequestParam(name = "lastName2") String lastName,
                               @RequestParam(name = "tel2") String tel,
                               @RequestParam(name = "email2") String email,
                               @RequestParam(name = "adress2") String adress,
                               @RequestParam(name = "postalCode2") int postalCode,
                               @RequestParam(name = "city2") String city,
                               @RequestParam(name = "password2") String password,
                               @RequestParam(name = "clientId", required = true) int id,
                               RedirectAttributes redirAttrs) {
        User user = userDAO.findById(id);
        if (userDAO.existsByEmail(email) && !user.getEmail().equals(email)) {
            redirAttrs.addFlashAttribute("error", "Cette adresse mail existe déjà.");
            return "redirect:/admin";
        } else {
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setAgencyPlace(agencyPlace);
                user.setLastName(lastName);
                user.setFirstName(firstName);
                user.setTel(tel);
                user.setEmail(email);
                user.setAdress(adress);
                user.setPostalCode(postalCode);
                user.setCity(city);
                userDAO.save(user);
                return "redirect:/admin";
            } else {
                redirAttrs.addFlashAttribute("error", "Vous n'avez pas rentré le bon mot de passe.");
                return "redirect:/admin";
            }
        }
    }

    /**
     * Updating the admin password by an admin.
     *
     * @return view - load admin template.
     */
    @PostMapping(value = "/updateclientmdp")
    public String updateClientMdp(@RequestParam(name = "password3") String oldPassword,
                                  @RequestParam(name = "password4") String newPassword, int clientIdMdp, RedirectAttributes redirAttrs) {
        User user = userDAO.findById(clientIdMdp);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            newPassword = passwordEncoder.encode(newPassword);
            user.setPassword(newPassword);
            userDAO.save(user);
            return "redirect:/admin";
        } else {
            redirAttrs.addFlashAttribute("error", "Vous n'avez pas rentré le bon mot de passe");
            return "redirect:/admin";
        }
    }

    /**
     * This is the mapping that handle the client to update himself.
     *
     * @return view - load client template.
     */
    @RequestMapping(value = "/updatemyprofil")
    public String updateClientProfil(@RequestParam(name = "agency") String agencyPlace,
                                     @RequestParam(name = "firstName") String firstName,
                                     @RequestParam(name = "lastName") String lastName,
                                     @RequestParam(name = "tel") String tel,
                                     @RequestParam(name = "email") String email,
                                     @RequestParam(name = "adress") String adress,
                                     @RequestParam(name = "postalCode") int postalCode,
                                     @RequestParam(name = "city") String city,
                                     @RequestParam(name = "password") String password,
                                     RedirectAttributes redirAttrs, Principal principal) {
        User user = userDAO.findByEmail(currentUserName(principal));
        if (userDAO.existsByEmail(email) && !user.getEmail().equals(email)) {
            redirAttrs.addFlashAttribute("error", "Cette adresse mail existe déjà.");
            return "redirect:/client";
        } else {
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setAgencyPlace(agencyPlace);
                user.setLastName(lastName);
                user.setFirstName(firstName);
                user.setTel(tel);
                user.setEmail(email);
                user.setAdress(adress);
                user.setPostalCode(postalCode);
                user.setCity(city);
                userDAO.save(user);
                return "redirect:/client";
            } else {
                redirAttrs.addFlashAttribute("error", "Vous n'avez pas rentré le bon mot de passe.");
                return "redirect:/client";
            }
        }
    }

    /**
     * Updating a client's password by himself
     *
     * @return view - load client template.
     */
    @PostMapping(value = "/updatemymdp")
    public String updateMyMdp(@RequestParam(name = "password2") String oldPassword,
                              @RequestParam(name = "password3") String newPassword, RedirectAttributes redirAttrs, Principal principal) {
        User user = userDAO.findByEmail(currentUserName(principal));
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            newPassword = passwordEncoder.encode(newPassword);
            user.setPassword(newPassword);
            userDAO.save(user);
            return "redirect:/client";
        } else {
            redirAttrs.addFlashAttribute("error", "Vous n'avez pas rentré le bon mot de passe");
            return "redirect:/client";
        }
    }

    /**
     * Make a transfer for a client by an admin.
     *
     * @return view - load admin template.
     */
    @PostMapping(value = "/virementadmin")
    public String virementAdmin(@RequestParam(name = "clientId", required = true) int id,
                                @RequestParam(name = "montant", required = true) int montant
    ) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findById(id);
        List<Account> accounts = accountDAO.findByUser(user);

        double ancienSolde = accounts.get(0).getMoney();
        accounts.get(0).setMoney(ancienSolde + montant);
        accountDAO.save(accounts.get(0));
        Operation operation = new Operation(montant, shortDate.format(new Date()), "crédit", "eida", "Courant", accounts.get(0));
        operationDAO.save(operation);

        return "redirect:/admin";
    }


    /**
     * Make an internal transfer by a client.
     *
     * @return view - load client template.
     */
    @PostMapping(value = "/virementclient")
    public String credit(@RequestParam(name = "montant", required = true) int montant,
                         @RequestParam(name = "compteSource", required = true) String compteSource,
                         Principal principal) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findByEmail(currentUserName(principal));
        List<Account> accounts = accountDAO.findByUser(user);

        if ((accounts.get(0).getType()).equals(compteSource)) {
            double ancienSolde = accounts.get(0).getMoney();
            double ancienSolde2 = accounts.get(1).getMoney();
            if (ancienSolde >= montant) {
                accounts.get(0).setMoney(ancienSolde - montant);
                Operation operation = new Operation(montant, shortDate.format(new Date()), "Débit", "Courant", "Epargne", accounts.get(0));
                operationDAO.save(operation);
                accounts.get(1).setMoney(ancienSolde2 + montant);
                Operation operation2 = new Operation(montant, shortDate.format(new Date()), "Crédit", "Courant", "Epargne", accounts.get(1));
                operationDAO.save(operation2);
            }

        } else {
            double ancienSolde = accounts.get(1).getMoney();
            double ancienSolde2 = accounts.get(0).getMoney();
            if (ancienSolde >= montant) {
                accounts.get(1).setMoney(ancienSolde - montant);
                Operation operation = new Operation(montant, shortDate.format(new Date()), "Débit", "Epargne", "Courant", accounts.get(1));
                operationDAO.save(operation);
                accounts.get(0).setMoney(ancienSolde2 + montant);
                Operation operation2 = new Operation(montant, shortDate.format(new Date()), "Crédit", "Epargne", "Courant", accounts.get(0));
                operationDAO.save(operation2);
            }
        }
        accountDAO.save(accounts.get(0));
        accountDAO.save(accounts.get(1));
        return "redirect:/client";
    }

    /**
     * Register admins with a setter for the access Level.
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load sadmin template.
     */
    @PostMapping(value = "/registeradmin")
    public String registerAdmin(@RequestParam(name = "agency") String agencyPlace,
                                @RequestParam(name = "firstName") String firstName,
                                @RequestParam(name = "lastName") String lastName,
                                @RequestParam(name = "tel") String tel,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "adress") String adress,
                                @RequestParam(name = "postalCode") int postalCode,
                                @RequestParam(name = "city") String city,
                                @RequestParam(name = "password") String password,
                                RedirectAttributes redirAttrs,
                                Model model) {

        if (!userDAO.existsByEmail(email)) {
            DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            List<Contact> contact = contactDAO.findAll();
            model.addAttribute("contact", contact);
            String hPass = passwordEncoder.encode(password);
            User users = new User(firstName, lastName, adress, postalCode, city, email, tel, agencyPlace, hPass, 1, true, shortDate.format(new Date()));
            userDAO.save(users);
            return "redirect:/sadmin";
        } else {
            redirAttrs.addFlashAttribute("error", "Cet administrateur existe déjà.");
            return "redirect:/sadmin";
        }
    }

    /**
     * Register clients with a setter for the access Level
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load admin template.
     */
    @PostMapping(value = "/registerclient")
    public String registerClient(@RequestParam(name = "agency") String agencyPlace,
                                 @RequestParam(name = "firstName") String firstName,
                                 @RequestParam(name = "lastName") String lastName,
                                 @RequestParam(name = "tel") String tel,
                                 @RequestParam(name = "email") String email,
                                 @RequestParam(name = "adress") String adress,
                                 @RequestParam(name = "postalCode") int postalCode,
                                 @RequestParam(name = "city") String city,
                                 @RequestParam(name = "firsttransfer") int transfer,
                                 @RequestParam(name = "password") String password,
                                 RedirectAttributes redirAttrs,
                                 Model model) {

        if (!userDAO.existsByEmail(email)) {
            DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String hPass = passwordEncoder.encode(password);
            User users = new User(firstName, lastName, adress, postalCode, city, email, tel, agencyPlace, hPass, 2, true, shortDate.format(new Date()));
            userDAO.save(users);

            Account account = new Account(transfer, "num", shortDate.format(new Date()), "Courant", true, users);
            Account savedAccount = accountDAO.save(account);
            savedAccount.setNumAccount("FR225" + GenNumAccount.genNumAccount(account.getId()));
            accountDAO.save(savedAccount);
            Operation operation = new Operation(transfer, shortDate.format(new Date()), "crédit", "eida", "Courant", account);
            operationDAO.save(operation);

            return "redirect:/admin";
        } else {
            redirAttrs.addFlashAttribute("error", "Ce client existe déjà.");
            return "redirect:/admin";
        }
    }

    /**
     * Creation of an saving account.
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load client template.
     */
    @PostMapping(value = "/createspareaccount")
    public String createSpareAccount(RedirectAttributes redirAttrs,
                                     Model model, Principal principal) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findByEmail(currentUserName(principal));
        Account account = new Account(0, "num", shortDate.format(new Date()), "Epargne", true, user);
        Account savedAccount = accountDAO.save(account);
        savedAccount.setNumAccount("FR450" + GenNumAccount.genNumAccount(account.getId()));
        accountDAO.save(savedAccount);
        return "redirect:/client";

    }

    /**
     * Add resource.
     *
     * @param model as a Model to add the DB items to the template attributes.
     * @return view - load admin template.
     */
    @PostMapping(value = "/addressource")
    public String addResource(@RequestParam(name = "cat") int cat,
                              @RequestParam(name = "type") String type,
                              @RequestParam(name = "title") String title,
                              @RequestParam(name = "url", required = false) String url,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              Model model, Principal principal) {
        DateFormat shortDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        User user = userDAO.findByEmail(currentUserName(principal));
        Optional<Category> categ = categoryDAO.findById(cat);
        Category categs = categ.get();
        String filePath = "";
        if (!file.isEmpty()) {
            storageService.store(file);
            String tmpFilePath = storageService.load(file.getOriginalFilename()).toString();
            filePath = "files/" + tmpFilePath.substring(tmpFilePath.lastIndexOf('/') + 1);
        }
        Resources resource = new Resources(type, title, url, description, shortDate.format(new Date()), filePath, user);
        resource.setCategory(categs);
        model.addAttribute("categ", categ);
        resourcesDAO.save(resource);
        return "redirect:/admin";
    }
/*
    @PostMapping(value = "/updateResCateg")
    public String updateResourceCateg(@RequestParam(name = "upResId")int upResId,
                                      @RequestParam(name = "upCatId")int upCatId) {
        Optional<Resources> upRes = resourcesDAO.findById(upResId);
        Resources resources = upRes.get();
        Optional<Category> upCat = categoryDAO.findById(upCatId);
        Category categs = upCat.get();
        resources.setCategory(categs);
        return "redirect:/admin";
    }
  */

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current UserName.
     */
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public static String currentUserName(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current password.
     */
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    @ResponseBody
    public String currentPassword(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current firstName.
     */
    @RequestMapping(value = "/firstName", method = RequestMethod.GET)
    @ResponseBody
    public String currentFirstName(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current lastName.
     */
    @RequestMapping(value = "/lastName", method = RequestMethod.GET)
    @ResponseBody
    public String currentLastName(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return Integer - load current tel.
     */
    @RequestMapping(value = "/tel", method = RequestMethod.GET)
    @ResponseBody
    public String currentTel(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current adress.
     */
    @RequestMapping(value = "/adress", method = RequestMethod.GET)
    @ResponseBody
    public String currentAdress(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return Integer - load current postalCode.
     */
    @RequestMapping(value = "/postalCode", method = RequestMethod.GET)
    @ResponseBody
    public String currentPostalCode(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current city.
     */
    @RequestMapping(value = "/city", method = RequestMethod.GET)
    @ResponseBody
    public String currentCity(Principal principal) {
        return principal.getName();
    }

    /**
     * Request Spring security with the current session.
     *
     * @return String - load current agencyPlace.
     */
    @RequestMapping(value = "/agencyPlace", method = RequestMethod.GET)
    @ResponseBody
    public String currentAgencyPlace(Principal principal) {
        return principal.getName();
    }

    /**
     * Redirect invalid requests to login template.
     *
     * @return view - load login template.
     */
    @RequestMapping(value = "*")
    public String def() {
        return "login";
    }

    /**
     * Find the name of the file using his path.
     *
     * @return String - load the pathFile
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * Find the name of the file using his path.
     *
     * @return String - load the pathFile
     */
    @GetMapping("/client/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFileClient(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * Exception handler for the file uploader.
     *
     * @param exc - .class
     * @return exception
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}