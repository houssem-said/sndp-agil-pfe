# 🏢 SNDP AGIL – Application de gestion de file d'attente et de prise de rendez-vous

Ce projet a été réalisé dans le cadre d’un stage de fin d’études (PFE) de licence en informatique à la société **SNDP AGIL** (Tunisie). Il a pour objectif de concevoir et développer une application web permettant de gérer efficacement les files d'attente et les rendez-vous dans les stations-service et agences de la SNDP AGIL.

## 🚀 Objectifs du projet

- Optimiser la gestion de l’affluence des clients.
- Réduire les temps d’attente et améliorer la satisfaction client.
- Offrir un système de réservation de tickets ou de prise de rendez-vous en ligne.
- Permettre aux opérateurs de gérer les files d’attente en temps réel.
- Offrir aux administrateurs une interface de gestion centralisée et sécurisée.

## 🛠️ Technologies utilisées

- **Frontend** : React.js + Vite + Tailwind CSS
- **Backend** : Node.js + Express
- **Base de données** : MySQL
- **Authentification** : JSON Web Tokens (JWT)
- **Déploiement** : Docker

## 📦 Architecture du projet

```bash
sndp-agil-pfe/
├── client/             # Application frontend (React + Vite)
├── server/             # API backend (Node.js + Express + Prisma)
├── prisma/             # Schéma de la base de données et migrations
├── .env                # Variables d'environnement (à ne pas partager)
├── README.md           # Fichier de présentation du projet
└── ...
