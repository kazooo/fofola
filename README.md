# Fofola

[![Build Status](https://travis-ci.com/kazooo/fofola.svg?branch=master)](https://travis-ci.com/kazooo/fofola)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Fofola je jednoduchá webová aplikace pro rozšířenou administraci knihovního systému [Kramerius](https://system-kramerius.cz/en/),
která chybí administrátorům systému. Aplikace nabízí přívětivé grafické rozhraní pro spouštění různých 
procesů jak v rámci Krameria (posíláním požadavků na API) tak i v rámci samotné aplikace.

Fofola slouží ke spouštění následujících procesů:
- reindexace dokumentů přes Krameria
- změna přístupových práv k dokumentům
- editace obrázku jednotlivých stránek
- přidání nebo odebrání dokumentů do/z virtuálních sbírek
- přidání nebo odebrání donátorů z dokumentů, a také kontrolu přítomnosti donátorů u dokumentů z virtuální sbírky
- zveřejnění dokumentů a jejich následovníků podle přístupových práv
- asynchronní generování PDF dokumentů podle kořenových uuid

Fofola je schopna zobrazovat stav jednotlivých dokumentů a celých stromů dokumentů.
Umožňuje zobrazování stavů interních procesů a procesů v Krameriu, a také základní manipulaci s těmito procesy.

## Parametry aplikace

Pro svoji práci Fofola musí byt napojená na Krameria, [Apache Solr](https://lucene.apache.org/solr/)
a [Fedoru](https://duraspace.org/fedora/), které Kramerius používá.
Fofola používá databázi Postgres, a pokud není specifikována externí databáze, 
spouští interní [H2 in-memory databázi](https://www.h2database.com/). 
Fofola je konfigurovatelná přes proměnné prostředí, níže je uveden seznam, popis a defaultní hodnoty.

|       parameter       |                               description                               |              example              | required |    default value   |
|:---------------------:|:-----------------------------------------------------------------------:|:---------------------------------:|:--------:|:------------------:|
|     KRAMERIUS_HOST    |                               url Krameria                              |        http://kramerius.cz        |   true   |                    |
|     KRAMERIUS_USER    |                      login uživatele s admin. právy                     |           krameriusUser           |   true   |                    |
|     KRAMERIUS_PSWD    |                         heslo uživatele Krameria                        |           krameriusPswd           |   true   |                    |
|      FEDORA_HOST      |                                url Fedory                               |          http://fedora.cz         |   true   |                    |
|      FEDORA_USER      |                          login uživatele Fedory                         |             fedoraUser            |   true   |                    |
|      FEDORA_PSWD      |                          heslo uživatele Fedory                         |             fedoraPswd            |   true   |                    |
|       SOLR_HOST       |                         url Solru s názvem jádra                        |    http://solr.cz/solr/kramerius  |   true   |                    |
|  POSTGRES_DB_JDBC_URL |                               url databázi                              | jdbc:postgresql://postgres/fofola |   false  | jdbc:h2:mem:fofola |
|    POSTGRES_DB_USER   |                         login uživatele databázi                        |            postgresUser           |   false  |        user        |
|    POSTGRES_DB_PSWD   |                         heslo uživatele databázi                        |            postgresPswd           |   false  |        pswd        |
|      FOFOLA_PORT      |                               port Fofoly                               |                8080               |   false  |        8081        |
|     H2_LOGGING_LVL    |                       úroveň logování H2 databázi                       |               DEBUG               |   false  |        WARN        |
| HIBERNATE_LOGGING_LVL |           úroveň logování [Hibernate](https://hibernate.org/)           |               DEBUG               |   false  |        WARN        |
|   HIKARI_LOGGING_LVL  | úroveň logování [HikariCP](https://github.com/brettwooldridge/HikariCP) |               DEBUG               |   false  |        WARN        |


Fofola se spouští s uvedenými parametry jako .jar soubor

```bash
./gradlew bootJar
java -DSOLR_HOST=http://solr.cz <jiné parametry s prefixem '-D'> -jar fofola-1.0.0.jar 
```

nebo pomocí příkazu Gradle 'bootRun'

```bash
./gradlew bootRun --args='--SOLR_HOST=http://solr.cz <jiné parametry s prefixem "--">'
```

## Docker

Fofola může byt spouštěna v [Docker](https://www.docker.com/) kontejneru. 
Na [Dockerhubu](https://hub.docker.com/repository/docker/ermak/fofola) najděte oficiální image web aplikace.
Pomocí [Gradle pluginu](https://bmuschko.github.io/gradle-docker-plugin/) pro dockerizace aplikací od [Benjamina Muschko](https://bmuschko.com/),
 lokálně dokážete udělat image příkazy

```bash
./gradlew buildImage   # image s tagem verze 
./gradlew buildLatest  # image s tagem verze a další s tagem 'latest'
```

Pro rychlou konfiguraci a start aplikace se používá i [Docker compose](https://docs.docker.com/compose):

```yaml
version: "3"
services:
  fofola:
    image: ermak/fofola:latest
    container_name: fofola
    ports:
      - "8081:8081"
    environment:
      - KRAMERIUS_HOST=http://kramerius.cz
      - KRAMERIUS_USER=krameriusUser
      - KRAMERIUS_PSWD=krameriusPswd
      - FEDORA_HOST=http://fedora.cz
      - FEDORA_USER=fedoraUser
      - FEDORA_PSWD=fedoraPswd
      - SOLR_HOST=http://solr.cz/solr/kramerius
      - POSTGRES_DB_JDBC_URL=jdbc:postgresql://fofola-db:5432/fofola
      - POSTGRES_DB_USER=postgresUser
      - POSTGRES_DB_PSWD=postgresPswd
  fofola-db:
    image: "postgres:11.5"
    container_name: fofola-db
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_DB=fofola
    ports:
      - "5432:5432"
```

## Použité technologie

Hlavním jádrem Fofoly je REST API pro přijetí požadavků, které je napsáno v Javě
s použitím [Spring Boot](https://spring.io/projects/spring-boot) frameworku. 
Grafické rozhraní je vytvořeno za pomoci čistého JavaScriptu s elementy šablonovacího jazyku [Thymeleaf](https://www.thymeleaf.org/).
V budoucnu se plánuje GUI ve frameworku [React](https://reactjs.org/). 

## Licence

[GPL v3](https://www.gnu.org/licenses/gpl-3.0)

## Autoří
[Aleksei Ermak](https://github.com/kazooo) a skvěle nápady členů týmu OSDD MZK.
 