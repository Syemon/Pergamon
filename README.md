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
* Wejście do pobranej aplikacji: `cd MyDisc`
* Uruchomienie aplikacji: `docker-compose up` lub `sudo docker-compose up`
