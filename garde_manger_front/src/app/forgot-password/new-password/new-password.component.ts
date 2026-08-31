import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {

  email = '';
  message = '';
  messageSuccess = false;

  passwordVisible = false;
  passwordConfirmationVisible = false;

  canSubmit = false;

  constructor(private route: ActivatedRoute) {}

  async ngOnInit(): Promise<void> {

    const id = this.route.snapshot.queryParamMap.get('compte');

    if (!id) {
      this.message = "Identifiant du compte manquant.";
      this.canSubmit = false;
      return;
    }

    try {

      const response = await fetch(
        `http://127.0.0.1:8080/comptes/${id}`
      );

      if (!response.ok) {
        this.message =
          "Impossible de récupérer les informations du compte.";
        this.canSubmit = false;
        return;
      }

      const data = await response.json();

      if (!data.email) {
        this.message =
          "Impossible de récupérer l'adresse email du compte.";
        this.canSubmit = false;
        return;
      }

      this.email = data.email;

      // Tout s'est bien passé
      this.canSubmit = true;

    } catch (error) {

      console.error(error);

      this.message =
        "Impossible de contacter le serveur.";

      this.canSubmit = false;
    }
  }

  togglePassword(): void {
    this.passwordVisible = !this.passwordVisible;
  }

  togglePasswordConfirmation(): void {
    this.passwordConfirmationVisible =
      !this.passwordConfirmationVisible;
  }

  goBack(): void{
    window.location.href = ""
  }

  async validate(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form); 
    this.message = "";
    this.messageSuccess = false;
    const password = formData.get('password') as string;
    const passwordConfirmation = formData.get('passwordConfirmation') as string;

    if (password !== passwordConfirmation){
      this.message = "Les 2 mots de passe saisis sont différents. Veuillez recommencer"
      this.messageSuccess = false;
      return;
    }

    if (password.length < 8 || password.length > 50){
      this.message = "Le mot de passe doit contenir entre 8 et 50 caractères. Nombre : "+password.length
      this.messageSuccess = false;
      return;
    }

    const response = await fetch("http://127.0.0.1:8080/comptes/"+this.route.snapshot.queryParamMap.get('compte'),{
      method: "PATCH",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        mdp: password
      })
    })
    if (!response.ok){
      this.message = "Une erreur s'est produite lors de la modification de votre mot de passe. Veuillez réessayer ulétrieurement."
      this.messageSuccess = false;
      return;
    }
    const response1 = await fetch("http://127.0.0.1:8080/sendEmail", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        to: this.email,
        subject: "Mot de passe réinitialisé",
        text: "Bonjour,\n\n Nous vous confirmons que votre mot de passe a été réinitialisé. Vous pouvez désormais vous reconnecter avec votre nouveau mot de passe.\n\nCordialement,\n\nGarde-manger"
      })
    })
    if (!response1.ok){
      this.message = "Votre mot de passe a été modifiée avec succès, mais nous n'avons pas pu vous envoyer un email de confirmation. Retour à la connexion..."
      this.messageSuccess = false;
    } else {
      const data = await response1.json()
      if (data.success){
        this.message = "Votre mot de passe a été modifiée avec succès. Nous vous avons envoyé un email de confirmation. Vous pouvez désormais vous connecter avec votre nouveau mot de passe. Retour à la connexion..."
        this.messageSuccess = true;
      } else {
        this.message = "Votre mot de passe a été modifiée avec succès, mais nous n'avons pas pu vous envoyer un email de confirmation. Retour à la connexion..."
        this.messageSuccess = false
      }
    }
    this.goBack()
  }
}