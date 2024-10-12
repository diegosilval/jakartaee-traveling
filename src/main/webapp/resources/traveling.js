/* 
 * Copyright 2024 Diego Silva <diego.silva at apuntesdejava.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


let map;

async function initMap() {

    const {Map, InfoWindow} = await google.maps.importLibrary("maps");
    const {AdvancedMarkerElement, PinElement} = await google.maps.importLibrary("marker");

    map = new Map(document.getElementById("map"), {
        zoom: 12,
        center: ORIGIN,
        mapId: '6c633a659a7ebad6'
    });
    const directionsRenderer = new google.maps.DirectionsRenderer();
    const directionsService = new google.maps.DirectionsService();
    directionsRenderer.setMap(map);
    var converter = new showdown.Converter();
    calculateAndDisplayRoute(directionsService, directionsRenderer);
    addPlaces(converter,map, InfoWindow, AdvancedMarkerElement, PinElement, ORIGIN, DESTINATION);
}

function calculateAndDisplayRoute(directionsService, directionsRenderer) {
    console.debug("ORIGIN:", ORIGIN);
    console.debug("DESTINATION:", DESTINATION);
    directionsService.route({
        origin: ORIGIN,
        destination: DESTINATION,
        travelMode: google.maps.TravelMode.DRIVING//google.maps.TravelMode["DRIVING"]
    }).then((response) => {
        directionsRenderer.setDirections(response);
    });
}

async function addPlaces(converter,map, InfoWindow, AdvancedMarkerElement, PinElement, origin, destination) {
    const infoWindow = new InfoWindow();
    const url = `${CONTEXT_PATH}/resources/routes?originLat=${origin.lat}&originLng=${origin.lng}&destinationLat=${destination.lat}&destinationLng=${destination.lng}`;
    console.debug("url:", url);
    fetch(url)
        .then(response => response.json())
        .then(response => {
            let places = Object.values(response);
            console.debug("places:", places);
            places.forEach(place => {
                const placeMap = place.placeMap;
                getRecommendationText(place, (response) => {
                    const pin = new PinElement({
                        scale: 1.5
                    });
                    const description = createPlaceAtMap(converter,place, response.content);
                    const marker = new AdvancedMarkerElement({
                        map,
                        position: {
                            lat: placeMap.location.latitude,
                            lng: placeMap.location.longitude
                        },
                        title: place.displayName,
                     //   content: pin.element,
                        gmpClickable: true
                    });
                    marker.addListener("click", ({domEvent, latLng}) => {
                        const {target} = domEvent;
                        console.debug("place clicked:",place);
                        const headerContent = document.createElement("div");
                        headerContent.innerHTML = `<strong>${place.displayName}</strong>`;
                        infoWindow.close();
                        infoWindow.setHeaderContent(headerContent);
                        infoWindow.setContent(description);
                        infoWindow.open(marker.map, marker);
                    });
                });
            });
        });
}


async function getRecommendationText(place, callback) {
    const url = `${CONTEXT_PATH}/resources/recommendations/?locationName=${place.displayName}&location=${place.formattedAddress}`;
    console.debug("url:", url);
    fetch(url)
        .then(response => response.json())
        .then(callback);
}

function createPlaceAtMap(converter,place, text) {
    const htmlText = converter.makeHtml(text);
    const content = document.createElement("div");
    content.innerHTML = `
        <div>${place.formattedAddress}</div>
        <div><a target="_blank" href="${place.placeMap.googleMapsUri}">Ver en Google Maps</a></div>
        <div>${htmlText}</div>
    `;
    return content;
}