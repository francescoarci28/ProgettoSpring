DROP SCHEMA ecommercedb;
CREATE SCHEMA ecommercedb;
USE ecommercedb;



CREATE TABLE user (
                      id INTEGER AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(30) UNIQUE

);

CREATE TABLE carrello (
                      id INTEGER AUTO_INCREMENT PRIMARY KEY ,
                      prezzo_totale FLOAT NOT NULL,
                      version LONG,
                      user INTEGER UNIQUE,
                      FOREIGN KEY(user) REFERENCES user(id)

);

CREATE TABLE prodotto (
                  id INTEGER AUTO_INCREMENT PRIMARY KEY,
                  immagine VARCHAR(500),
                  nome VARCHAR(100) UNIQUE NOT NULL,
                  descrizione VARCHAR(300),
                  prezzo FLOAT not null,
                  categoria VARCHAR(100) NOT NULL,
                  qta INTEGER NOT NULL,
                  version LONG
);

CREATE TABLE acquisto (
                          id INTEGER AUTO_INCREMENT PRIMARY KEY,
                          version LONG,
                          data DATE,
                          prezzo FLOAT,
                          buyer INTEGER,
                          prodotto INTEGER,
                          quantita INTEGER,
                          FOREIGN KEY(buyer) REFERENCES user(id),
                          FOREIGN KEY(prodotto) REFERENCES prodotto(id)
);


CREATE TABLE prodottonelcarrello(
                            id INTEGER AUTO_INCREMENT PRIMARY KEY,
                            carrello INTEGER,
                            quantita INTEGER,
                            prezzo FLOAT,
                            version LONG NOT NULL,
                            prodotto INTEGER,
                            FOREIGN KEY(carrello) REFERENCES carrello(id)
)
