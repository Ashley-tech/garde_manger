import { Component, OnInit } from '@angular/core';
import Cookies from 'js-cookie';

interface Produit {
  id: number;
  libelle: string;
  marque: string;
  type: string;
  dateConsommation: string;
  datePeremption: string;
  etat: string;
  proprietaire?: any;
}

interface Compte {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  dateNaissance: string;
  mdp: string;
  adresse: string;
  adresseComp: string;
  cp: string;
  ville: string;
  pays: string;
  fonction: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  produits: Produit[] = [];

  message = '';
  messageAddProduit = "";
  messageUpdateProduit = "";
  messageSuccessA = false;
  messageModifierProduit = "";
  messageSuccessU = false;
  messageUpdateCompte = "";
  passwordConfirmationVisible = false;
  passwordVisible = false;

  produitId = 0;

  idCompte = 0;
  emailCompte = '';

  nombreProduits = 0;
  nombreBientotPerimes = 0;
  nombrePerimes = 0;

  compte: Compte | null = null;

  profilVisible = false;

  confirmationDeconnexionVisible = false;

  produitEnModification: Produit | null = null;

  modif_produit_title = "";

  formUpdateProduitVisible = false;

  confirmationDeleteAccountVisible = false;
  confirmationDeleteProductVisible = false;
  formAddProduitVisible = false;

  formModifierCompte = false;

  confirmationDeleteProductMessage = '';

  async updateProduitState(id: number, etat: string): Promise<void> {
    const response = await fetch(`http://127.0.0.1:8080/produits/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ etat })
    });

    if (!response.ok) {
      throw new Error(`Impossible de mettre à jour l'état du produit n°${id}.`);
    }
  }

  async ngOnInit(): Promise<void> {

    const idCookie = Cookies.get('compte_id');
    const emailCookie = Cookies.get('compte_email');

    if (!idCookie) {
      window.location.href = "";
      return;
    }

    this.idCompte = parseInt(idCookie, 10);

    if (emailCookie) {
      this.emailCompte = emailCookie;
    } else {
      window.location.href = "";
      return;
    }

    await this.chargerCompte();
    await this.chargerProduits();
  }


  async chargerCompte(): Promise<void> {

    try {

      const response = await fetch(
        `http://127.0.0.1:8080/comptes/${this.idCompte}`
      );

      if (!response.ok) {
        this.message =
          "Impossible de récupérer les informations de votre compte.";
        return;
      }

      this.compte = await response.json();

    } catch (error) {

      console.error(error);

      this.message =
        "Impossible de contacter le serveur.";
    }
  }


  async chargerProduits(): Promise<void> {

    try {

      const response = await fetch(
        `http://127.0.0.1:8080/comptes/${this.idCompte}/produits`
      );

      if (!response.ok) {
        this.message =
          "Impossible de récupérer les produits de votre compte.";
        return;
      }

      const produits: Produit[] = await response.json();
      const dateReference = this.dateCourante();
      const produitsActualises = produits.map(produit => ({
        ...produit,
        etat: this.calculerEtatProduit(
          produit.dateConsommation,
          produit.datePeremption,
          dateReference
        )
      }));

      await Promise.all(
        produitsActualises
          .filter((produit, index) => produit.etat !== produits[index].etat)
          .map(produit => this.updateProduitState(produit.id, produit.etat))
      );

      this.produits = produitsActualises;

      this.nombreProduits = this.produits.length;

      this.calculerStatistiques();

    } catch (error) {

      console.error(error);

      this.message =
        "Impossible de charger les produits ou de mettre à jour leur état.";
    }
  }

  calculerEtatProduit(
    dateConsommation: string,
    datePeremption: string,
    dateReference = this.dateCourante()
  ): string {
    if (!dateConsommation && !datePeremption) {
      return "Non périssable";
    }

    if (dateConsommation && !datePeremption) {
      return "Consommé";
    }

    if (dateConsommation && dateConsommation === datePeremption) {
      return "Consommé et périmé";
    }

    if (datePeremption < dateReference) {
      return "Périmé";
    }

    const dateLimite = new Date(`${dateReference}T00:00:00`);
    dateLimite.setDate(dateLimite.getDate() + 7);
    const dateLimiteFormatee = this.formaterDateISO(dateLimite);

    if (datePeremption <= dateLimiteFormatee) {
      return "Bientôt périmé";
    }

    return "Valide";
  }

  private dateCourante(): string {
    return this.formaterDateISO(new Date());
  }

  private formaterDateISO(date: Date): string {
    const annee = date.getFullYear();
    const mois = String(date.getMonth() + 1).padStart(2, "0");
    const jour = String(date.getDate()).padStart(2, "0");
    return `${annee}-${mois}-${jour}`;
  }


  calculerStatistiques(): void {

    this.nombrePerimes = 0;
    this.nombreBientotPerimes = 0;

    for (const produit of this.produits) {
      if (produit.etat === "Périmé" || produit.etat === "Consommé et périmé") {
        this.nombrePerimes++;
      } else if (produit.etat === "Bientôt périmé") {
        this.nombreBientotPerimes++;
      }
    }
  }


  formaterDate(date: string): string {

    if (!date) {
      return '';
    }

    return new Date(date).toLocaleDateString('fr-FR');
  }


  getClassePeremption(date: string): string {

    if (!date) {
      return '';
    }

    const aujourdHui = new Date();
    const datePeremption = new Date(date);

    if (datePeremption < aujourdHui) {
      return 'perime';
    }

    const dansSeptJours = new Date();
    dansSeptJours.setDate(aujourdHui.getDate() + 7);

    if (datePeremption <= dansSeptJours) {
      return 'bientot-perime';
    }

    return 'normal';
  }
  ajouterProduit(): void {
    this.formAddProduitVisible = true;
    this.messageAddProduit = "";
    this.messageSuccessA = false;
  }

  annulerAjoutProduit(): void {
    this.formAddProduitVisible = false;
  }

  annulerModifierCompte(): void {
    this.formModifierCompte = false;
    this.profilVisible = true;
  }

  async modifierCompte(event: Event): Promise<void> {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);
    this.messageUpdateCompte = "";
    var oldPassword = this.compte?.mdp || '';
    let oldEmail = this.compte?.email || '';
    console.log(oldEmail);
    var selectedPassword = "";
    if (formData.get("motDePasse") !== formData.get("motDePasseConfirmation")) {
      this.messageUpdateCompte = "Les 2 mots de passe saisis sont différents. Veuillez recommencer";
      return;
    }
    if (formData.get("motDePasse") !== "" && ((formData.get("motDePasse") as string).length < 8 || (formData.get("motDePasse") as string).length > 50)) {
      this.messageUpdateCompte = "Le mot de passe doit contenir entre 8 et 50 caractères.";
      return;
    }
    if (formData.get("email") !== oldEmail) {
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
        this.message = "Une erreur s'est produite lors de la vérification de l'adresse email. Merci de réessayer ultérieurement."
        return;
      }
      const data = await response.json()
      if (data.success){
        this.message = "L'adresse email saisi existe déjà dans notre base de données."
        return;
      }
    }
    if (formData.get("motDePasse") !== "") {
      selectedPassword = formData.get("motDePasse") as string;
    } else {
      selectedPassword = oldPassword;
    }
    console.log(formData.get('dateNaissance'))
  const response0 = await fetch(`http://127.0.0.1:8080/comptes/${this.compte?.id}`,{
      method: "PATCH",
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nom: formData.get("nom"),
        prenom: formData.get("prenom"),
        email: formData.get("email"),
        mdp: selectedPassword,
        dateNaissance: formData.get('dateNaissance'),
        adresse: formData.get('address'),
        adresseComp: formData.get('addressComp'),
        cp: formData.get('cp'),
        ville: formData.get('ville'),
        pays: formData.get('pays'),
        fonction: formData.get('fonction')
      })
    })
    if (!response0.ok){
      this.messageUpdateCompte = "Une erreur s'est produite lors de la mise à jour de votre compte. Merci de réessayer ultérieurement.";
      return;
    } else {
      Cookies.set('compte_email', formData.get("email") as string);
      if (formData.get("email") !== oldEmail) {
        const response1 = await fetch("http://127.0.0.1:8080/sendEmail", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: formData.get("email"),
            subject: "Modification de votre compte Garde-Manger",
            message: "Bonjour " + formData.get("prenom") + ",\n\nVotre compte Garde-Manger a été modifié avec succès. Votre nouvelle adresse email est : " + formData.get("email") + ".\n\nSi vous n'êtes pas à l'origine de cette modification, veuillez nous contacter immédiatement.\n\nCordialement,\nL'équipe Garde-Manger"
          })
        })
        if (!response1.ok){
          this.messageUpdateCompte = "Information : Votre compte a été mis à jour avec succès, mais nous n'avons pas pu vous envoyer un email de confirmation.";
        } else {
          this.messageUpdateCompte = "Votre compte a été mis à jour avec succès. Nous vous avons envoyé un email de confirmation.";
        }
      } else {
        this.messageUpdateCompte = "Votre compte a été mis à jour avec succès.";
      }
      this.formModifierCompte = false;
      this.profilVisible = true;
      await this.chargerCompte();
    }
  }

  togglePasswordConfirmation(): void {
    const passwordConfirmationInput = document.getElementById('motDePasseConfirmation') as HTMLInputElement;
    if (passwordConfirmationInput) {
      passwordConfirmationInput.type = passwordConfirmationInput.type === 'password' ? 'text' : 'password';
    }
  }

  togglePassword(): void {
    const passwordInput = document.getElementById('motDePasse') as HTMLInputElement;
    if (passwordInput) {
      passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
    }
  }

  openModifierCompte(): void {
    this.messageUpdateCompte = "";
    this.profilVisible = false;
    this.formModifierCompte = true;
  }

  annulerModifierProduit(): void {
    this.formUpdateProduitVisible = false;
  }

  openModifierProduit(id: number): void {
    const produit = this.produits.find(p => p.id === id);

    if (!produit) {
      this.message = "Impossible de trouver ce produit.";
      return;
    }

    this.produitEnModification = produit;
    this.modif_produit_title = `Modification du produit n°${id}`;
    this.formUpdateProduitVisible = true;
  }


  afficherProfil(): void {
    this.profilVisible = true;
  }


  fermerProfil(): void {
    this.profilVisible = false;
  }


  deconnexion(): void {
    this.confirmationDeconnexionVisible = true;
  }

  confirmerDeconnexion(): void {
    Cookies.remove('compte_id');
    Cookies.remove('compte_email');

    window.location.href = '';
  }

  cancelDeleteAccount(): void {
    this.confirmationDeleteAccountVisible = false;
    //this.afficherProfil();
  }

  confirmDeleteAccount(): void {
    // this.fermerProfil();
    this.confirmationDeleteAccountVisible = true;
  }

  async deleteAccount(): Promise<void> {
    try {

      const response = await fetch(
        `http://127.0.0.1:8080/comptes/${this.idCompte}`,{
          headers: {
            'Content-Type': 'application/json'
          },
          method: 'DELETE'
        }
      );

      if (!response.ok) {
        alert("Impossible de supprimer votre compte.");
        return;
      }

      const data = await response.json();

      this.confirmerDeconnexion();

    } catch (error) {

      console.error(error);

      alert("Impossible de contacter le serveur.");
    }
  }

  annulerDeconnexion(): void {
    this.confirmationDeconnexionVisible = false;
  }

  async confirmerDeleteProduct(produitId: number): Promise<void> {
    try {
      const response = await fetch(`http://127.0.0.1:8080/produits/${produitId}`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        alert("Impossible de supprimer le produit.");
        return;
      }

      const data = await response.json();
      this.annulerDeleteProduct();

    } catch (error) {
      console.error(error);
      alert("Impossible de contacter le serveur.");
    }
  }

  annulerDeleteProduct(): void {
    this.confirmationDeleteProductMessage = '';
    this.confirmationDeleteProductVisible = false;
  }

  deleteProduct(produitId: number): void {
    this.produitId = produitId;
    this.confirmationDeleteProductMessage = 'Êtes-vous sûr de vouloir supprimer le produit n°' + produitId + ' ?';
    this.confirmationDeleteProductVisible = true;
  }

  async addProduit(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form); 
    this.messageAddProduit = "";
    this.messageSuccessA = false;
    const d = new Date();
    const dateYYMMDDActuel = d.toISOString().split('T')[0];
    const dateConsommation = formData.get("dateConsommation") as string;
    const datePeremption = formData.get("datePeremption") as string;
    var etat = "";
    if (dateConsommation > dateYYMMDDActuel) {
      this.messageAddProduit = "La date de consommation ne peut pas être dans le futur.";
      return;
    }
    if (dateConsommation == ""){
      if (datePeremption == ""){
        etat = "Non périssable";
      } else {
        if (datePeremption < dateYYMMDDActuel) {
          etat = "Périmé";
        } else if (datePeremption <= new Date(d.setDate(d.getDate() + 7)).toISOString().split('T')[0]) {
          etat = "Bientôt périmé";
        } else {
          etat = "Valide";
        }
      }
    } else {
      if (datePeremption == ""){
        etat = "Consommé";
      } else {
        if (dateConsommation > datePeremption) {
          this.messageAddProduit = "La date de consommation ne peut pas être après la date de péremption.";
          return;
        } else if (dateConsommation > dateYYMMDDActuel) {
          this.messageAddProduit = "La date de consommation ne peut pas être dans le futur.";
          return;
        } else if (dateConsommation == datePeremption) {
          etat = "Consommé et périmé";
        } else if (datePeremption < dateYYMMDDActuel) {
          etat = "Périmé";
        } else if (datePeremption <= new Date(d.setDate(d.getDate() + 7)).toISOString().split('T')[0]) {
          etat = "Bientôt périmé";
        } else {
          etat = "Valide";
        }
      }
    }
    try {
      var corp = {
          libelle: formData.get("libelle"),
          marque: formData.get("marque"),
          type: formData.get("type"),
          dateConsommation: dateConsommation,
          datePeremption: datePeremption,
          etat: etat,
          proprietaireId: this.idCompte
        }
        console.log(corp)
      const response = await fetch("http://127.0.0.1:8080/produits", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(corp)
      });
      if (!response.ok) {
        this.messageAddProduit = "Une erreur s'est produite. Merci de réessayer ultérieurement.";
        this.messageSuccessA = false;
        return;
      }
      const data = await response.json();
      console.log(data)
      //if (data.success) {
        this.messageAddProduit = "Le produit a été ajouté avec succès.";
        this.messageSuccessA = true;
        await this.chargerProduits();
        this.formAddProduitVisible = false;
      /*} else {
        this.messageAddProduit = "Une erreur s'est produite. Merci de réessayer ultérieurement.";
        this.messageSuccessA = false;
      }*/
    } catch (error) {
      console.error(error);
      this.messageAddProduit = "Impossible de contacter le serveur.";
      this.messageSuccessA = false;
    }
  }

  async modifierProduit(event: Event): Promise<void> { 
    event.preventDefault(); 
    const form = event.target as HTMLFormElement; 
    const formData = new FormData(form); 
    this.messageModifierProduit = "";
    this.messageSuccessU = false;
    const d = new Date();
    const dateYYMMDDActuel = d.toISOString().split('T')[0];
    const dateConsommation = formData.get("dateConsommationU") as string;
    const datePeremption = formData.get("datePeremptionU") as string;
    var etat = "";
    if (dateConsommation > dateYYMMDDActuel) {
      this.messageModifierProduit = "La date de consommation ne peut pas être dans le futur.";
      return;
    }
    if (dateConsommation == ""){
      if (datePeremption == ""){
        etat = "Non périssable";
      } else {
        if (datePeremption < dateYYMMDDActuel) {
          etat = "Périmé";
        } else if (datePeremption <= new Date(d.setDate(d.getDate() + 7)).toISOString().split('T')[0]) {
          etat = "Bientôt périmé";
        } else {
          etat = "Valide";
        }
      }
    } else {
      if (datePeremption == ""){
        etat = "Consommé";
      } else {
        if (dateConsommation > datePeremption) {
          this.messageAddProduit = "La date de consommation ne peut pas être après la date de péremption.";
          return;
        } else if (dateConsommation > dateYYMMDDActuel) {
          this.messageAddProduit = "La date de consommation ne peut pas être dans le futur.";
          return;
        } else if (dateConsommation == datePeremption) {
          etat = "Consommé et périmé";
        } else if (datePeremption < dateYYMMDDActuel) {
          etat = "Périmé";
        } else if (datePeremption <= new Date(d.setDate(d.getDate() + 7)).toISOString().split('T')[0]) {
          etat = "Bientôt périmé";
        } else {
          etat = "Valide";
        }
      }
    }
    try {
      const response = await fetch(`http://127.0.0.1:8080/produits/${this.produitEnModification?.id}`, {
        method: "PATCH",
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          libelle: formData.get("libelleU"),
          marque: formData.get("marqueU"),
          type: formData.get("typeU"),
          dateConsommation: dateConsommation,
          datePeremption: datePeremption,
          etat: etat
        })
      });
      if (!response.ok) {
        this.messageModifierProduit = "Une erreur s'est produite. Merci de réessayer ultérieurement.";
        this.messageSuccessU = false;
        return;
      }
      const data = await response.json();
      console.log(data)
      //if (data.success) {
        this.messageModifierProduit = "Le produit a été modifié avec succès.";
        this.messageSuccessU = true;
        await this.chargerProduits();
        this.formUpdateProduitVisible = false;
      /*} else {
        this.messageModifierProduit = "Une erreur s'est produite. Merci de réessayer ultérieurement.";
        this.messageSuccessU = false;
      }*/
    } catch (error) {
      console.error(error);
      this.messageModifierProduit = "Impossible de contacter le serveur.";
      this.messageSuccessU = false;
    }
  }
}