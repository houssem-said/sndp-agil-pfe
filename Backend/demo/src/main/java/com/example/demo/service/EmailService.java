package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service dédié à l'envoi d'emails via Spring Mail.
 * Ce service encapsule la logique d'envoi d'emails pour faciliter la réutilisation
 * et centraliser la gestion des communications par mail dans l'application.
 */
@Service
public class EmailService {

    /**
     * Composant Spring injecté pour envoyer des emails.
     * Il doit être configuré dans le contexte Spring avec les propriétés SMTP adaptées.
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envoie un email de réinitialisation de mot de passe à l'adresse spécifiée.
     *
     * @param to         L'adresse email du destinataire.
     * @param resetLink  Le lien permettant à l'utilisateur de réinitialiser son mot de passe.
     */
    public void sendPasswordResetEmail(String to, String resetLink) {
        // Création d'un message email simple
        SimpleMailMessage message = new SimpleMailMessage();

        // Destinataire de l'email
        message.setTo(to);

        // Sujet de l'email
        message.setSubject("Réinitialisation du mot de passe");

        // Corps de l'email avec le lien de réinitialisation
        message.setText("Bonjour,\n\n" +
                "Vous avez demandé la réinitialisation de votre mot de passe pour votre compte SNDP Agil.\n" +
                "Veuillez cliquer sur le lien ci-dessous pour définir un nouveau mot de passe :\n\n" +
                resetLink + "\n\n" +
                "Ce lien est valable pendant 30 minutes. Si vous n'êtes pas à l'origine de cette demande, " +
                "vous pouvez ignorer cet email.\n\n" +
                "Cordialement,\n" +
                "L'équipe SNDP Agil");

        // Envoi de l'email via le mailSender configuré
        mailSender.send(message);
    }
}
