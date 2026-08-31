import { Component } from '@angular/core';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  message = '';
  messageSuccess = false;

  goBack(): void {
    window.history.back();
  }

  async validate(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form); 
    this.message = "";
    this.messageSuccess = false;
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
      this.message = "Une erreur s'est produite. Merci de réessayer ultérieurement."
      this.messageSuccess = false;
      return;
    }
    const data = await response.json()
    if (!data.success){
      this.message = "Votre adresse mail saisi n'existe pas dans notre base de données. Merci de vous inscrire."
      this.messageSuccess = false;
      return;
    }
    const response1 = await fetch("http://127.0.0.1:8080/sendEmail", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        to: formData.get("email"),
        subject: "Lien de réinitialisation",
        text: "Bonjour,\n\n Veuillez cliquer sur ce <a href='http://127.0.0.1:4200/forgot-password/new?compte="+data.userId+"'>lien</a> pour modifier votre mot de passe.\n\nCordialement,\n\nGarde-manger"
      })
    })
    if (!response1.ok){
      this.message = "Une erreur s'est produite. Merci de réessayer ultérieurement."
      this.messageSuccess = false;
      return;
    }
    const data1 = await response1.json()
    if (data1.success){
      this.message = "Votre adresse mail existe dans notre base de données. Nous venons de vous envoyer un email avec le lien pour réinitialiser votre mot de passe."
      this.messageSuccess = true;
    } else {
      this.message = "Une erreur s'est produite. Merci de réessayer ultérieurement."
      this.messageSuccess = false;
    }
  }

}
