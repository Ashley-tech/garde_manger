import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  passwordVisible = false;

  errorMessage = '';

  togglePassword(): void {
    this.passwordVisible = !this.passwordVisible;
  }

  goSignup(): void {
    window.location.href = "/signup";
  }

  async validate(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form); 
    this.errorMessage = "";
    const response = await fetch("http://127.0.0.1:8080/login",{
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: formData.get("login"),
        password: formData.get("password")
      })
    })
    const data = await response.json();
    if (!data.success){
      this.errorMessage = "L'adresse email et le mot de passe saisi ne correspond à aucun compte"
    } else {

    }
  }

}