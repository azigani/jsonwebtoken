package com.example.jwtcloud.services;


import com.example.jwtcloud.exceptions.domain.*;
import com.example.jwtcloud.models.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    //créer user

    User senregistrer(String nom, String prenom, String username, String email) throws UserNotFoundException,
            UsernameExistException, EmailExistException, MessagingException;

    //


    List<User> getAllUsers();

    User findUserByUsername(String username);

//    void findUserById(long id);

    User findUserByEmail(String email);


    User ajouterNouveauUser(String nom, String prenom, String username, String email,
                            String role, boolean nonVerouille, boolean estActif, MultipartFile profilImage)throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

//
//    User ModifierUser(String utilisateurCourant, String nouveauNom, String nouveauPrenom, String nouveauUsername, String nouvelemail,
//                      String role, boolean nonVerouille, boolean estActif, MultipartFile profilImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
//
//    User updateUser(String utilisateurCourant, String nouveauNom, String nouveauPrenom, String nouveauUsername, String nouvelemail,
//                      String role, boolean nonVerouille, boolean estActif, MultipartFile profilImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
//

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;


    void supprimerUser(long id);

    void motdifierMotDePasse(String email) throws EmailNotFoundException, MessagingException;

//        User miseAjourImageProfilUtilisateur(User user , MultipartFile profilImage);

        User miseAjourImageProfilUtilisateur(String username , MultipartFile profilImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;


//
//    public void activateAccount(User user) ;
//
//
//    public void deactivateAccount(User user);


}
