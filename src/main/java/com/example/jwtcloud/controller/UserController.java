package com.example.jwtcloud.controller;

import com.example.jwtcloud.constantes.Permissions;
import com.example.jwtcloud.exceptions.ExceptionHandling;
import com.example.jwtcloud.exceptions.domain.*;
import com.example.jwtcloud.models.HttpResponse;
import com.example.jwtcloud.models.User;
import com.example.jwtcloud.models.UserPrincipal;
import com.example.jwtcloud.services.UserService;
import com.example.jwtcloud.utility.JWTTokenProvider;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.example.jwtcloud.constantes.FileConstant.*;
import static com.example.jwtcloud.constantes.Permissions.USER_AUTHORITIES;
import static com.example.jwtcloud.constantes.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/", "/user"})
@CrossOrigin("http://localhost:4200")
public class UserController extends ExceptionHandling {

    public static final String EMAIL_SENT = "Un email contenant un nouveau mot de passe a été envoyé à : ";
    public static final String USER_DELETED_SUCCESSFULLY = "Utilisateur supprimé avec succès!";


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;


    @GetMapping("/home")
    public String trouverUser() throws UserNotFoundException {
//     return "L'application fonctionne correctement!";
        throw new UserNotFoundException("Utilisateur non trouvé(TEST)!");
    }


    @PostMapping("/senregister")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException,
            EmailExistException, MessagingException, javax.mail.MessagingException {
        User newUser = userService.senregistrer(user.getNom(), user.getPrenom(), user.getUsername(),
                user.getEmail());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/ajouter-nouvel-utilisateur")
    public ResponseEntity<User> ajouterNouvelUtilisateur(@RequestParam("nom") String nom,
                                                         @RequestParam("prenom") String prenom,
                                                         @RequestParam("username") String username,
                                                         @RequestParam("email") String email,
                                                         @RequestParam("role") String role,
                                                         @RequestParam("estActif") String estActif,
                                                         @RequestParam("isNonLocked") String isNonLocked,
                                                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException, NotAnImageFileException {
        User newUser = userService.ajouterNouveauUser(nom, prenom, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(estActif), profileImage);
        return new ResponseEntity<>(newUser, OK);
    }


//    @PostMapping("/modifier")
//    public ResponseEntity<User> update(@RequestParam("utilisateurCourant") String utilisateurCourant,
//                                       @RequestParam("nom") String nom,
//                                       @RequestParam("prenom") String prenom,
//                                       @RequestParam("username") String username,
//                                       @RequestParam("email") String email,
//                                       @RequestParam("role") String role,
//                                       @RequestParam("estActif") String estActif,
//                                       @RequestParam("isNonLocked") String isNonLocked,
//                                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
//            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,  NotAnImageFileException {
//        User utilisateurModifierUser = userService.ModifierUser(utilisateurCourant, nom, prenom, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(estActif), profileImage);
//        return new ResponseEntity<>(utilisateurModifierUser, OK);
//
//    }


//    @PostMapping("/modifier")
//    public ResponseEntity<User> update(@RequestParam("utilisateurCourant") String utilisateurCourant,
//                                       @RequestParam("nom") String nom,
//                                       @RequestParam("prenom") String prenom,
//                                       @RequestParam("username") String username,
//                                       @RequestParam("email") String email,
//                                       @RequestParam("role") String role,
//                                       @RequestParam("estActif") String estActif,
//                                       @RequestParam("isNonLocked") String isNonLocked,
//                                     @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
//        User updatedUser = userService.ModifierUser(utilisateurCourant, nom, prenom, username,email, role, Boolean.parseBoolean(estActif), Boolean.parseBoolean(isNonLocked), profileImage);
//        return new ResponseEntity<>(updatedUser, OK);
//    }
//    @PreAuthorize("hasAnyAuthority('user:miseAJour')")
//    @PreAuthorize(USER_AUTHORITIES)
    @PostMapping("/modifier")
    public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
                                       @RequestParam("firstName") String firstName,
                                       @RequestParam("lastName") String lastName,
                                       @RequestParam("username") String username,
                                       @RequestParam("email") String email,
                                       @RequestParam("role") String role,
                                       @RequestParam("isActive") String isActive,
                                       @RequestParam("isNonLocked") String isNonLocked,
                                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username,email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(updatedUser, OK);
    }

    //login endPoint ZIGANI
    @PostMapping("/connexion")
    public ResponseEntity<User> login(@RequestBody User user) {
        sauthentifier(user.getUsername(), user.getMotdePasse());//username et password pour s'authentifier
        User loginUser = userService.findUserByUsername(user.getUsername());
        System.out.println("loginUSER "  +  loginUser);
        //Ici venant de notre model
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        System.out.println("Connexion réussie! pour ce username" + user.getUsername());
        System.out.println("jwtHeader  " + jwtHeader);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }


    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_TOKEN_HEADER, jwtTokenProvider.genererJwtToken(userPrincipal));
        return httpHeaders;
    }


    private void sauthentifier(String username, String motdePasse) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, motdePasse));
    }


    @GetMapping("/liste-utilisateur-par-username/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @PreAuthorize("hasAnyAuthority('user:lire')")
    @GetMapping("/liste-de-tous-les-utilisateurs")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> utilisateurs = userService.getAllUsers();
        System.out.println("utilisateurs controlleurs "  +utilisateurs.toString());
        return new ResponseEntity<>(utilisateurs, OK);

    }

    @GetMapping("/modifier-mot-de-passe/{email}")
    public ResponseEntity<HttpResponse> modifierMoDePasse(@PathVariable("email") String email) throws MessagingException, EmailNotFoundException, javax.mail.MessagingException {
        userService.motdifierMotDePasse(email);
        return response(OK, EMAIL_SENT + email);
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }


    @DeleteMapping("/supprimer-utilisateur/{id}")
    @PreAuthorize("hasAnyAuthority('user:supprimer')")//Si tu n'as pas le droit tu ne peux pas
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long id) {
        userService.supprimerUser(id);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }


    @PostMapping("/mettre-a-jour-profile-image")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username,
                                                   @RequestParam(value = "profileImage") MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException,
            EmailExistException, IOException, NotAnImageFileException {
        User user = userService.miseAjourImageProfilUtilisateur(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }


    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(CHEMIN_IMAGE_UTILISATEUR + username + BARRE_OBLIQUE + fileName));
    }


    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int lectureByte;
            byte[] chunk = new byte[1024];
            while ((lectureByte = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, lectureByte);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }


}
