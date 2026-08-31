import { Component } from '@angular/core';
import Cookies from "js-cookie"

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

  goForgot(event: Event) : void{
    event.preventDefault()
    window.location.href = "forgot-password/email"
  }

  async validate(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form);
    const login = formData.get("login") as string;
    this.errorMessage = "";
    const response = await fetch("http://127.0.0.1:8080/login",{
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: login,
        password: formData.get("password")
      })
    })
    const data = await response.json();
    if (!data.success){
      this.errorMessage = "L'adresse email et le mot de passe saisi ne correspond à aucun compte"
    } else {
      Cookies.set("compte_id",data.userId);
      Cookies.set("compte_email",login)
      //document.cookie = `id_compte=${data.userId}; path=/`;
      //document.cookie = `email_compte=${login}; path=/`;
      window.location.href = "dashboard"
    }
  }

}