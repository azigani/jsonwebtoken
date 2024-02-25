package com.example.jwtcloud.services.servicesImpl;

import com.example.jwtcloud.constantes.EmailConstant;
import com.example.jwtcloud.constantes.FileConstant;
import com.example.jwtcloud.enumerations.Role;
import com.example.jwtcloud.exceptions.domain.*;
import com.example.jwtcloud.models.User;
import com.example.jwtcloud.models.UserPrincipal;
import com.example.jwtcloud.repository.UserRepository;
import com.example.jwtcloud.services.EmailService;
import com.example.jwtcloud.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.jwtcloud.constantes.EmailConstant.EMAIL_SENT;
import static com.example.jwtcloud.constantes.FileConstant.*;
import static com.example.jwtcloud.constantes.UserImplConstant.*;
import static com.example.jwtcloud.enumerations.Role.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Transactional
@Qualifier("userDetailsService")

public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    //    @Autowired
    private UserRepository userRepository;

    //    @Autowired
    LoginAttemptService loginAttemptService;

    //    @Autowired
    EmailService emailService;

    private PasswordEncoder passwordEncoder;

//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            //verifier si non desactiver
            onValiderLoginTentative(user);
            user.getLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            System.out.println("user lecture  " + encodePassword(user.getMotdePasse()));
//            System.out.println("user lecture  " + );
            System.out.println("user username lecture  " + user.getUsername());
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }

    }

    private void onValiderLoginTentative(User user) {
//verifier si le user non desactivé
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);

            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());

        }

    }


//
//    @Override
//    public User register(String fullname, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
//        validateNewUsernameAndEmail(username, email);
//        String password = generatePassword();
//        User user = new User(null, generateUserId(), fullname, username, encodePassword(password),
//                email, getTemporaryProfileImageUrl(username), null, null,
//                LocalDateTime.now(), Role.ROLE_USER.name(), Role.ROLE_USER.getAuthorities(), true, true);
//        userRepository.save(user);
//        log.info("New user's password: " + password);
//        emailService.sendNewPasswordToEmail(fullname, password, email);
//        return user;
//    }

    private void validateNewUsernameAndEmail(String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(userByNewUsername != null) {
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if(userByNewEmail != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
    }


    @Override
    public User senregistrer(String nom, String prenom, String username, String email)
            throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {

        validateNewUsernameAndEmail(username, email);

        User user = new User();
        user.setUserId(generateId());
        String password = genererMotDePasse();
        String encodepassword = encodePassword(password);
        System.out.println("encodepassword " + encodepassword);
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setUsername(username);
        user.setJoinDate(new Date());
        user.setMotdePasse(encodepassword);
        System.out.println("encodepassword " + encodepassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getProfilTemporaire());
        userRepository.save(user);
        LOGGER.info("nouveau mot de passe " + password);
        //envoie du mot de passe après enregistrement de l'utilsateur
        LOGGER.info(EMAIL_SENT + email);
        emailService.envoyerNouveuPasswordEmail(nom, password, email);
        System.out.println("user "   +   user.toString());
        return user;
    }

//    @Override
//    public User senregistrer(String nom, String prenom, String username, String email)
//            throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
//
//        validerExistanceUsernameEtEmail(EMPTY, username, email);
//
//        User user = new User();
//        user.setUserId(generateId());
//        String password = genererMotDePasse();
//        String encodepassword = encodePassword(password);
//        user.setNom(nom);
//        user.setPrenom(prenom);
//        user.setEmail(email);
//        user.setUsername(username);
//        user.setJoinDate(new Date());
//        user.setMotdePasse(encodepassword);
//        user.setActive(true);
//        user.setNotLocked(true);
//        user.setRole(ROLE_USER.name());
//        user.setAuthorities(ROLE_USER.getAuthorities());
//        user.setProfileImageUrl(getProfilTemporaire());
//        userRepository.save(user);
//        LOGGER.info("nouveau mot de passe " + password);
//        //envoie du mot de passe après enregistrement de l'utilsateur
//        LOGGER.info(EMAIL_SENT + email);
//        emailService.envoyerNouveuPasswordEmail(nom, password, email);
//
//        return user;
//    }

    private String genererMotDePasse() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private String getProfilTemporaire() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/image/url/temp")
//                .path(CHEMIN_IMAGE_UTILISATEUR_PAR_DÉFAUT)
                .toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

//    private String encodePassword(String password) {
//        return passwordEncoder.encode(password);
//    }

    //verifier si l'utilisateur existe, si le username et l'email ne sont pas déja utilisé???
//    private User validerExistanceUsernameEtEmail(String usernameCourant, String nouveauUsername, String nouvelEmail)
//            throws UserNotFoundException, UsernameExistException, EmailExistException {
//        User utilisateurCourant = findUserByUsername(usernameCourant);
//        User utilisateurParUsername = findUserByUsername(nouveauUsername);
//        User utilisateurParEmail = findUserByEmail(nouvelEmail);
//
//        if (StringUtils.isNotBlank(usernameCourant)) {
//
//            if (utilisateurCourant == null) {
//                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + usernameCourant);
//            }
//            if (utilisateurParUsername != null && !utilisateurCourant.getId().equals(utilisateurParUsername.getId())) {
//                throw new UsernameExistException(USERNAME_ALREADY_EXISTS + utilisateurParUsername);
//            }
//
//            if (utilisateurParEmail != null && !utilisateurCourant.getId().equals(utilisateurParEmail.getId())) {
//                throw new EmailExistException(EMAIL_ALREADY_EXISTS + utilisateurParEmail);
//            }
//
//            return utilisateurCourant;
//        } else {
////            User utilisateurParUsername = findUserByUsername(nouveauUsername);
//            if (utilisateurParUsername != null) {
//                throw new UsernameExistException(USERNAME_ALREADY_EXISTS + utilisateurParUsername);
//            }
//
//
////            User utilisateurParEmail = findUserByEmail(nouvelEmail);
//            if (utilisateurParEmail != null) {
//                throw new EmailExistException(EMAIL_ALREADY_EXISTS + utilisateurParEmail);
//            }
//            return null;
//        }
//    }


    private User validerExistanceUsernameEtEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }


//    @Override
//    public User senregistrer(String nom, String prenom, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
//        return null;
//    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("userRepository.findAll()" + userRepository.findAll());
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    //    @Override
//    public void findUserById(long id) {
//
//    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByByEmail(email);
    }

    @Override
    public User ajouterNouveauUser(String nom, String prenom, String username, String email, String role,
                                   boolean nonVerouille, boolean estActif, MultipartFile profilImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException {
        validerExistanceUsernameEtEmail(EMPTY, username, email);

        User user = new User();
        String password = genererMotDePasse();
        user.setUserId(generateId());
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setJoinDate(new Date());
        user.setUsername(username);
        user.setEmail(email);
        //encoding password
        user.setMotdePasse(encodePassword(password));
        user.setActive(estActif);
        user.setNotLocked(nonVerouille);
        user.setRole(getRoleEnum(role).name());
        user.setAuthorities(getRoleEnum(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        enregistrerProfileImage(user, profilImage);
        LOGGER.info("New user password: " + password);
        return user;
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }


    private Role getRoleEnum(String role) {
        return Role.valueOf(role.toUpperCase());
    }

//    @Override
//    public User ModifierUser(String nouvelutilisateurCourant, String nouveauNom, String nouveauPrenom, String nouveauUsername, String nouvelemail, String role, boolean nonVerouille, boolean estActif, MultipartFile profilImage)
//            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
//
//        User utilisateurCourant = validerExistanceUsernameEtEmail(nouvelutilisateurCourant, nouveauUsername, nouvelemail);
////        String password = genererMotDePasse(); // non utilisé pour le mot de passe en mode modif
////        user.setUserId(generateId());
//        utilisateurCourant.setNom(nouveauNom);
//        utilisateurCourant.setPrenom(nouveauPrenom);
//        utilisateurCourant.setJoinDate(new Date());
//        utilisateurCourant.setUsername(nouveauUsername);
//        utilisateurCourant.setEmail(nouvelemail);
//        utilisateurCourant.setActive(estActif);
//        utilisateurCourant.setNotLocked(nonVerouille);
//        utilisateurCourant.setAuthorities(getRoleEnum(role).getAuthorities());
//        userRepository.save(utilisateurCourant);
//        enregistrerProfileImage(utilisateurCourant, profilImage);
//        return utilisateurCourant;
//    }


    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User currentUser = validerExistanceUsernameEtEmail(currentUsername, newUsername, newEmail);
        currentUser.setNom(newFirstName);
        currentUser.setPrenom(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnum(role).name());
        currentUser.setAuthorities(getRoleEnum(role).getAuthorities());
        userRepository.save(currentUser);
        enregistrerProfileImage(currentUser, profileImage);
        return currentUser;
    }


    @Override
    public void supprimerUser(long id) {
        userRepository.deleteById(id);

    }

    @Override
    public void motdifierMotDePasse(String email) throws EmailNotFoundException, MessagingException {
        User user = userRepository.findUserByByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(USERNAME_NON_EXISTANT_POUR_CE_EMAIL + email);
        }

        String motDePasse = genererMotDePasse();
        user.setMotdePasse(encodePassword(motDePasse));
        userRepository.save(user);
        emailService.envoyerNouveuPasswordEmail(user.getNom(),
                user.getMotdePasse(), user.getEmail());
    }

    @Override
    public User miseAjourImageProfilUtilisateur(String username, MultipartFile profilImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = validerExistanceUsernameEtEmail(username, null, null);
        enregistrerProfileImage(user, profilImage);
        return user;
    }


    private void enregistrerProfileImage(User user, MultipartFile profileImage)
            throws IOException, NotAnImageFileException {
//        if (profileImage != null) {
//            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
//                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
//            }
        //path
        if (profileImage != null) {
            Path userFolder = Paths.get(DOSSIER_UTILISATEUR + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DOSSIER_CRÉÉ + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + POINT + EXTENSION_JPG));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + POINT + EXTENSION_JPG), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FICHIER_ENREGISTRÉ_DANS_LE_SYSTÈME_DE_FICHIERS + profileImage.getOriginalFilename());
        }
    }


    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path(CHEMIN_IMAGE_UTILISATEUR + username + BARRE_OBLIQUE
                        + username + POINT + EXTENSION_JPG).toUriString();
    }


}
