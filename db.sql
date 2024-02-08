CREATE TABLE IF NOT EXISTS Utilisateur (idUser SERIAL PRIMARY KEY, Nom VARCHAR(50), Email VARCHAR(120), MotDePasse VARCHAR(100), NumTelephone VARCHAR(50));

INSERT INTO Utilisateur (nom, email, motdepasse, numtelephone) VALUES ('Doe', 'johndoe@example.com', 'test123', '123-456-7890');
INSERT INTO Utilisateur (nom, email, motdepasse, numtelephone) VALUES ('Smith', 'janesmith@example.com', 'toto123', '098-765-4321');
INSERT INTO Utilisateur (nom, email, motdepasse, numtelephone) VALUES ('Johnson', 'alexjohnson@example.com', 'zebi123', '555-123-4567');

SELECT iduser, nom, email, motdepasse, numtelephone FROM Utilisateur WHERE nom = 'Doe'

drop table Utilisateur

SELECT motdepasse FROM Utilisateur WHERE email = 'johndoe@example.com'
UPDATE Utilisateur SET motdepasse = '825e9279' WHERE idUser = 3

CREATE TABLE IF NOT EXISTS Velo (idvelo SERIAL PRIMARY KEY, numeroserie VARCHAR(50), anneefabrication integer, marque VARCHAR(20), taille VARCHAR(50), statut bool);

INSERT INTO Velo (numeroserie, anneefabrication, marque, taille, statut) VALUES
('VELO12345', 2020, 'Giant', 'Medium', true),
('VELO54321', 2019, 'Trek', 'Large', true),
('VELO98765', 2021, 'Specialized', 'Small', false),
('VELO24680', 2018, 'Cannondale', 'Medium', true),
('VELO13579', 2022, 'Scott', 'Large', false);