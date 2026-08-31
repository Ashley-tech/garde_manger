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

  idCompte = 0;
  emailCompte = '';

  nombreProduits = 0;
  nombreBientotPerimes = 0;
  nombrePerimes = 0;

  compte: Compte | null = null;

  profilVisible = false;

  confirmationDeconnexionVisible = false;

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

      this.produits = await response.json();

      this.nombreProduits = this.produits.length;

      this.calculerStatistiques();

    } catch (error) {

      console.error(error);

      this.message =
        "Impossible de contacter le serveur.";
    }
  }


  calculerStatistiques(): void {

    this.nombrePerimes = 0;
    this.nombreBientotPerimes = 0;

    const aujourdHui = new Date();

    const dansSeptJours = new Date();
    dansSeptJours.setDate(aujourdHui.getDate() + 7);

    for (const produit of this.produits) {

      if (!produit.datePeremption) {
        continue;
      }

      const datePeremption = new Date(produit.datePeremption);

      if (datePeremption < aujourdHui) {
        this.nombrePerimes++;
      } else if (datePeremption <= dansSeptJours) {
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
    // À implémenter plus tard
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

  annulerDeconnexion(): void {
    this.confirmationDeconnexionVisible = false;
  }

}