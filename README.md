# Description
Pergamon is a simple API used for storing, downloading and querying resources from URL addresses.

## Features
API is documented on [OpenAPI 3.0](https://app.swaggerhub.com/apis-docs/Syemon4/Pergamon/1.0.0)
### Resource create
Metadata are stored in MySQL database. Files are stored in file system with UUID name to prevent file conflicts. Resource is being created asynchronously. If given resource already exists then existing resource will be updated to the newest state from given URL.
### Resource list
Resource listing uses HATEOS architecture. Each object contains information about resource and generated link to download endpoint
### Resource query
Resource querying uses resource list endpoint with query string parameter. Query searches resources by url fragments
### Resource download
Resources are downloaded by given URL address. If the resource does not exist then application returns error

# Instalation

Install [docker-compose](https://docs.docker.com/compose/install/).
* Download project from repository: `git clone https://github.com/Syemon/Pergamon.git`
* Go inside directory: `cd Pergamon`
* Run application: `docker-compose up` or `sudo docker-compose up`

<hr>

# Opis
Pergamon to proste API służące to przechowywania i pobierania zasobów z wysłanych adresów URL.

## Funkcjonalności
API zostało udokumentowane na [OpenAPI 3.0](https://app.swaggerhub.com/apis-docs/Syemon4/Pergamon/1.0.0)
### Tworzenie zasobu
Podczas tworzenia zasobu metadane są zapisywane w bazie danych, same pliki są przechowywane w systemie plików z nazwą wygenerowaną przez UUID, aby uniknąć nadpisania już istniejących plików. Pobieranie zasobów z podanego adresu URL odbywa się asynchronicznie.  
Jeżeli zostanie wysłany URL zasobu, który już istnieje w bazie danych to plik zostanie zaktualizowany (nazwa z systemu pliku nie ulegnie zmianie, zostanie zmieniona jego "oficjalna" nazwa).
### Listowanie zasobów
Listowanie zasobów korzysta z architektury HATEOS, wraz z informacjami o zasobie zostaje zwrócony link do jego pobrania.
### Wyszukiwanie zasobów
Wyszukiwanie zasobów wykonuje się w listowaniu zasobów za pomocą parametru query string. Zostaną zwrócone zasoby, które zawierają podaną przez użytkownika frazę w swoim adresie URL.
### Pobieranie zasobów
Zasoby pobiera się po adresie URL. Jeżeli URL nie ma swojego odpowiednika w bazie, to zostanie zwrócony błąd.

# Instalacja

Przed zainstalowaniem aplikacji należy mieć zainstalowany [docker-compose](https://docs.docker.com/compose/install/).
* Pobranie projektu z repozytorium: `git clone https://github.com/Syemon/Pergamon.git`
* Wejście do pobranej aplikacji: `cd Pergamon`
* Uruchomienie aplikacji: `docker-compose up` lub `sudo docker-compose up`



