import { Component } from '@angular/core';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  passwordVisible = false;

  passwordConfirmationVisible = false;

  message = '';
  messageSuccess = false;


  togglePassword(): void {
    this.passwordVisible = !this.passwordVisible;
  }


  togglePasswordConfirmation(): void {
    this.passwordConfirmationVisible =
      !this.passwordConfirmationVisible;
  }


  goBack(): void {
    window.history.back();
  }

  regex(pattern: string, value: string): boolean {
    const regex = new RegExp(pattern);
    return regex.test(value);
  }

  async validate(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form); 
    this.message = "";
    this.messageSuccess = false;
    const password = formData.get('password') as string;
    const passwordConfirmation = formData.get('passwordConfirmation') as string;
    if (formData.get("email") !== formData.get("emailConfirmation")){
      this.message = "Les 2 emails saisis sont différents. Veuillez recommencer"
      return;
    }

    if (password !== passwordConfirmation){
      this.message = "Les 2 mots de passe saisis sont différents. Veuillez recommencer"
      return;
    }

    if (password.length < 8 || password.length > 50){
      this.message = "Le mot de passe doit contenir entre 8 et 50 caractères. Nombre : "+password.length
      return;
    }

    const response = await fetch("http://127.0.0.1:8080/comptes/forgot-password",{
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: formData.get("email")
      })
    })
    if (!response.ok){
      this.message = "Une erreur s'est produite lors de votre inscription. Merci de réessayer ultérieurement."
      return;
    }
    const data = await response.json()
    if (data.success){
      this.message = "L'adresse email saisi existe déjà dans notre base de données."
      return;
    }
    const response0 = await fetch("http://127.0.0.1:8080/comptes",{
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nom: formData.get("lastName"),
        prenom: formData.get("firstName"),
        email: formData.get("email"),
        mdp: password,
        dateNaissance: formData.get('birthDate'),
        adresse: formData.get('address'),
        adresseComp: formData.get('addressComplement'),
        cp: formData.get('postalCode'),
        ville: formData.get('city'),
        pays: formData.get('country'),
        fonction: formData.get('job')
      })
    })
    if (!response0.ok){
      this.message = "Une erreur s'est produite lors de votre inscription. Merci de réessayer ultérieurement."
      return;
    }
    const response1 = await fetch("http://127.0.0.1:8080/sendEmail", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        to: formData.get("email"),
        subject: "Inscription effectué",
        text: "Bonjour,\n\n Nous vous confirmons votre inscription pour faire le suivi de votre stock d'aliments.\n\nCordialement,\n\nGarde-manger"
      })
    })
    const data1 = await response1.json()
    if (data1.success){
      this.message = "Votre inscription a été effectué avec succès. Nous vous avons envoyé un email de confirmation. Vous pouvez désormais vous connecter avec votre adresse email et votre mot de passe."
      this.messageSuccess = true;
    } else {
      this.message = "Votre inscription a été effectué avec succès. Vous pouvez désormais vous connecter avec votre adresse email et votre mot de passe, mais nous n'avons pas pu vous envoyer un email de confirmation."
    }
  }

  onlyNumbers(event: Event): void {
    const input = event.target as HTMLInputElement;

    input.value = input.value.replace(/[^0-9]/g, '');
  }

}