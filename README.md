# Adaptive Programming Opdracht 1 - Kaedin Schouten

## Demo
https://user-images.githubusercontent.com/42800737/115681308-b25dbc80-a354-11eb-84a1-f95e6ee2995a.mp4

## Doel
Het doel van deze app is dat de gebruiker zijn voertuigen kan afzetten op bepaalde locaties. In de app kan de gebruiker dit doen wordt zijn locatie meegenomen en opgeslagen. Mocht de gebruiker niet meer weten waar zijn voertuigen staan dan is er een GoogleMaps view die de gebruiker kan navigeren. Natuurlijk wordt er om toestemming gevraagd om toegang te krijgen tot de gebruikers zijn locatie (https://developer.android.com/training/location/permissions).

## Uitleg
Om een voertuig af te zetten (dropoff) hoeft de gebruiker alleen 4 kleine acties te verrichten:
 1. `titel` invullen (wat kan zijn: merk of type).
 2. `naam` of `kenteken` invullen.
 3. `Voertuig-type` selecteren met behulp van de slider.
 4. Op de `dropoff` knop klikken

Nadat deze acties verricht zijn wordt de rest automatisch gedaan zonder user-input:
 - Eerst wordt er een `Vehicle` object gecreëerd met daarin de `titel`, `naam` en `Voertuig-Type`
 - Dan wordt er gekeken op de gebruiker `location-permissions` heeft gegeven
    - Zoja, wordt de gebruiker zijn locatie (`latitude` en `longitude`) ophaald en in het object aangevuld
    - Zonee, doen we niks en gaat GoogleMaps uit van een latitude en longitude van 0.0. Dit wordt gedaan om `NullpointerExceptions` te vermijden
 - Dit object wordt met behulp van GSON omgezet naar een JSON-representatie van het Vehicle object. Dit wordt gedaan zodat we de JSON kunnen opslaan in een database (custom object kunnen namelijk niet zo opgeslagen worden).
 - Deze JSON wordt in de database (https://firebase.google.com/docs/firestore) opgeslagen met een random-generated-documentID.
 - Deze zelfde random-generated-documentID wordt vervolgens lokaal op de gebruiker zijn device opgeslagen. Zo kan de app weten welke voertuigen bij deze gebruiker horen.

Om een voertuig op te halen verloopt het volgende proces (0:37 in de demo):
 - Alle opgeslagen documentIds worden opgehaald van de gebruiker zijn device
 - Van de Firestore worden alle documenten opgehaald waarvan de IDs overeenkomen
 - De JSON van de documemten worden weer omgezet (met behulp van GSON) naar vehicle objecten.
    - Dit process van custom-object naar JSON en JSON naar custom-objecten converteren met behulp van GSON wordt ook wel `Serialize` genoemd. Vandaar dat de Vehicle-object de interface `Serializable` inherit.
 - De RecyclerView (https://developer.android.com/guide/topics/ui/layout/recyclerview) wordt aangevuld met de vehicles 

Vervolgens, als er op een vehicle wordt geklikt van de Recyclerview:
 - Het vehicle object wordt geconverteerd in een: `Car`, `Motorbike` of `Bicycle` object afhankelijk van de `Vehicle-Type`. Deze objecten extenden van het (super)object `Vehicle` en hebben dus dezelfde eigenschappen + meer.
 - De detailsview (0:45 in de demo) wordt aangevuld met de data van het nieuwe object (`Car`, `Motorbike` of `Bicycle`)
 - Het gedrag en uiterlijk van de blauweknop (onder de `navigate` knop) verandert op basis van Vehicle-Type.
    - Bij `Bicycle`:
      - Text = "RING"
      - Vibratie = 2x kort (zoals een fietsbel)
    - Bij `Motorbike`:
      - Text = "HONK HORN"
      - Vibratie = 6x kort (zoals een boze motorrijder :))
    - Bij `Car`:
      - Text = "HONK HORN"
      - Vibratie = 2x lang (zoals een auto-claxon)

Wanneer de gebruiker zijn voertuig heeft opgehaald (er is akkoord gegaan op de dialog, 1:00 in de demo):
 - Dan wordt er een firestore `remove` functie aangeroepen, hierbij wordt de ID van de huidige voertuig meegegeven. 
 - De gebruiker wordt terug gezet op de `MainActivity` oftewel de homepage.

## Interessante bestanden:
Alle is bestanden in https://github.com/Kaediin/AP-Opdracht-1/tree/master/app/src/main/java/com/kaedin/fietsapp


## UML Diagram
![UML Diagram](https://user-images.githubusercontent.com/42800737/115683504-cefaf400-a356-11eb-94dc-a92ce2c238f7.jpg)

Voor deze demo heb ik gekozen voor 3 voertuigen: Fiets, Auto en motor. In eerste instantie was het alleen voor fietsen dus vandaar de naar "FietsApp" maar gaandeweg heb ik besloten meerdere voertuigen toe te voegen.
