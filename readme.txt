"# garde_manger" 

-Corps de /src/resources/application-local.properties :
spring.datasource.username=TON_LOGIN_POSTGRES
spring.datasource.password=TON_MOT_DE_PASSE_POSTGRES

spring.mail.username=TON_ADRESSE_MAIL_GMAIL
spring.mail.password=TON_MOT_DE_PASSE_GOOGLE_APP_PASSWORD

-Créer une base de données appelée "db_garde_manger" avant de lancer le serveur (.\mvnw spring-boot:run)

Back-end :
Commande à lancer en local :
-.\mvnw spring-boot:run -Dspring-boot.run.profiles=local (CMD Windows)
-.\mvnw spring-boot:run "-Dspring-boot.run.profiles=local" (PowerShell)
-./mvnw spring-boot:run -Dspring-boot.run.profiles=local (Linux et MacOS)

Front-end :
-ng generate component [nom_du_composant] : Créer une nouvelle interface
-Pour démarrer le serveur correctement : ng serve --host 0.0.0.0