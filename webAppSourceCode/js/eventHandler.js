var latDecimal;
var lngDecimal;

// Stores an array of seismograph stations
var stationArray = {
    length: 0,
    // Pushes station objects into stations array
    addElem: function addElem(elem) {
        // stations.length is automatically incremented every time an element is added.
        [].push.call(this, elem);
    }
};
// Used to load Google api
var map;

// Map loaded
var currentMap;

// Simulated earthquake info
var earthquake;

// Stores an array of markers referencing the stations
var markersArray = [];

/**
 * Creates an earthquake when user clicks the earthquake button
 */
function createEarthquake(){
    //TODO: It isn't removing from screen
    // Clears Markers on screen
    deleteOverlays();
    
    //Make Audio Sound
    var audio = new Audio('audio/BreakingSound.mp3');
    audio.play();
    // Shakes map
    this.shakeMap(80,2);
    
    // Create earthquake 
    earthquake = new Earthquake(currentMap.mapGrid.upperLatitude, currentMap.mapGrid.lowerLatitude,currentMap.mapGrid.leftLongitude,currentMap.mapGrid.rightLongitude);
    
    //DELETE ONCE DONE TESTING
    console.log("Current Mag: "+ earthquake.magnitude + " Current Lat: "+earthquake.latitude + "Current Long: " + earthquake.longitude);
    
    //Loads stations
    for (var i = 0; i < stationArray.length; i++) {
        showMarker(stationArray[i]);
        addClickEvent(stationArray[i], i);
    }
}
/**
 * Animates map to simulate earthquake. Time is in milliseconds and repeat determines how many
 * cycles of the animation to run
 *
 * @param {*} time The speed of the animation
 * @param {number} repeat The number of times to repeat the animation sequence
 */
function shakeMap(time, repeat) {
    for(var count = 0; count < repeat; count++){
        $( "#map" ).animate({
            top: "+=10"
          }, time).animate({
            top: "-=10"
          }, time).animate({
            left: "+=10"
          }, time).animate({
            left: "-=10"
          }, time
        );
    }
}
/**
 * Add station to map created
 * 
 * @param {Station} singleStation The station that will be added to the map
 */
function showMarker(singleStation) {
    console.log(singleStation.name + ": Lat="+singleStation.latitude+" Lng="+singleStation.longitude);
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(singleStation.latitude,singleStation.longitude),
        // Adds a number inside marker
         label: {
            text: singleStation.markerID
          },
        map: map
    });
    
    //addClickEvent(marker);
    markersArray.push(marker);

}

/**
 * Deletes all markers from the map
 * 
 */
function deleteOverlays() {
      if (markersArray) {
          for (i=0; i < markersArray.length; i++) {
              markersArray[i].setMap(null);
          }
      markersArray.length = 0;
      }
}

/**
 * Adds an event where click on a marker you see it's name
 * 
 * @param {Station} singleStation The station that was clicked
 * @param {number} index The position in the markers array for that marker
 */
function addClickEvent(singleStation, index){
    var infowindow = new google.maps.InfoWindow({ content: '<div><h3>'+ singleStation.name +'</h3></div>' });
    markersArray[index].addListener('click', function() {
    infowindow.open(map, markersArray[index]);
  });
}

/**
 * Takes data from JSON loads station and earthquake
 */
function loadJSON(){
    //alert("json");
    $.getJSON("js/eq-data.json", function(result){
        console.log(result);
        var obj = JSON.stringify(result);
                      
        // Loads two different earthquakes                
        var eq1 = result.earthquakes.earthquake[0];
        var eq2 = result.earthquakes.earthquake[1];
        
        //Counter to name each station
        var counter = 1;
        
        //Load set of stations to an array
        for (i = 0; i < eq1.station.length; i++) {
            var tempLatitude = dmsToDeg(eq1.station[i].latitude.degrees,eq1.station[i].latitude.minutes,eq1.station[i].latitude.direction);
            var tempLongitude = dmsToDeg(eq1.station[i].longitude.degrees,eq1.station[i].longitude.minutes,eq1.station[i].longitude.direction);
          
            stationArray.addElem(new Station(tempLatitude,tempLongitude, "station".concat(counter), String(counter)));
            counter++;
            console.log("Station Count:"+ stationArray.length+ "Single Station: " + counter);
        }
        
        var eq1_loc = eq1.location
        
        // Decimal Degrees = Degrees + minutes/60 + seconds/3600     
        latDecimal = dmsToDeg(eq1_loc.latitude.degrees,eq1_loc.latitude.minutes, eq1_loc.latitude.direction);
        lngDecimal = dmsToDeg(eq1_loc.longitude.degrees,eq1_loc.longitude.minutes, eq1_loc.longitude.direction);
    
        console.log(latDecimal);
        console.log(lngDecimal);
        // Convert degrees, minutes, and direction to decimal
        var upperLat = dmsToDeg(eq1.map.upperlatitude.degrees, eq1.map.upperlatitude.minutes, eq1.map.upperlatitude.direction);
        var lowerLat = dmsToDeg(eq1.map.lowerlatitude.degrees, eq1.map.lowerlatitude.minutes, eq1.map.lowerlatitude.direction);
        var leftLong = dmsToDeg(eq1.map.leftlongitude.degrees, eq1.map.leftlongitude.minutes, eq1.map.leftlongitude.direction);
        var rightLong = dmsToDeg(eq1.map.rightlongitude.degrees, eq1.map.rightlongitude.minutes, eq1.map.rightlongitude.direction);

        // Create boundaries of map
        var mapGrid = new MapBoundaries(upperLat, lowerLat, leftLong, rightLong);
        
        // Creates current map info such as boundaries and center of map
        currentMap = new MapInfo(mapGrid,latDecimal, lngDecimal);
        
        //alert(earthquake.magnitude);
        // Starts Loading Google Map
        initialize();
    });
    
    /* Converts Degrees, Minutes, and Direction to Decimal format
     * 
     * @param {number} degrees The degrees
     * @param {number} minutes The minutes 
     * @param {string} direction The direction in the format ("W", "S", etc)
     * 
     * @returns {number}
     */
    function dmsToDeg(degrees,minutes,direction)
    {
        var decimal = parseInt(degrees)+(parseInt(minutes) * 60)/(60*60);
        
        if (direction == 'W'|| direction == 'S') {
            decimal = decimal * -1;
        }
        return decimal;
    }
}

/**
 * This function initializes the Google maps Map
 */
function initialize() {
    //alert("init");
    //alert(latDecimal+" " + lngDecimal);
    map = new google.maps.Map(document.getElementById('map'), {
                center: new google.maps.LatLng(currentMap.latitude,currentMap.longitude),
        scrollwheel: false,
        zoom: 7,
        draggable: false,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        disableDefaultUI: true
    });
}

/**
 * Station Class
 * Stores a latitude & longitude in decimal form and name/id strings
 * 
 * @param {number} latStation The latitude coordinate of the station
 * @param {number} longStation The longitude coordinate of the station 
 * @param {string} nameStation The name of the station
 * @param {string} id The id assigned to the station
 */

function Station(latStation,longStation,nameStation,id){
    this.latitude =  latStation;
    this.longitude = longStation;
    this.name = nameStation;
    this.markerID = id;
}

/** 
 * Earthquake Class
 * creates a random earthquake with the given parameters
 * 
 * @param {number} upperLat The upper bound for the latitude of the quake
 * @param {number} lowerLat The lower bound for the latitude of the quake
 * @param {number} leftLong The left bound for the longitude of the quake
 * @param {number} rightLong The right bound for the longitude of the quake
 */
function Earthquake(upperLat, lowerLat, leftLong, rightLong){
    this.latitude = getRandomNumber(lowerLat, upperLat);
    this.longitude = getRandomNumber(leftLong, rightLong);
    this.magnitude = getRandomNumber(0, 9).toFixed(2);
}

/**
 * MapInfo class
 * This class is used to store information about the map
 * 
 * @param {MapBoundaries} mapGrid The MapBoundaries object for this map
 * @param {number} centerLatitude The center of the map's latitude
 * @param {number} centerLongitude The center of the map's longitude
 */
function MapInfo(mapGrid, centerLatitude, centerLongitude){
    this.mapGrid = mapGrid;
    this.latitude = centerLatitude;
    this.longitude = centerLongitude;
}

/**
 * MapBoundaries class
 * Class used to hold the boundaries of map. 
 * Used to help determine the range of earthquake
 *
 * @param {number} upperLat The highest latitude value of the map
 * @param {number} lowerLat The lowest latitude value of the map
 * @param {number} leftLong The farthest left value of the map
 * @param {number} rightLong The farthest right value of the map
 */
function MapBoundaries(upperLat,lowerLat,leftLong, rightLog){
    this.upperLatitude = upperLat;
    this.lowerLatitude = lowerLat;
    this.leftLongitude = leftLong;
    this.rightLongitude = rightLog;
}

/**
 * getRandomNumber
 * This function returns a random number between two values
 * 
 * @param {number} min The minimum value that the number can be
 * @param {number} max The maximum value that the number can be
 * 
 * @returns {number}
 */ 
function getRandomNumber(min, max) {
  return Math.random() * (max - min) + min;
}


//google.maps.event.addDomListener(window, 'load', loadJSON);