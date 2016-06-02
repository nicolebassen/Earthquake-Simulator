<!DOCTYPE html>


<html lang="en">



<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Earthquake Simulator</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <link rel="stylesheet" href="css/indexStyle.css">
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid.min.css" />
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid-theme.min.css" />   
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
    <div class="container">
        <div class="row">
            <nav class="navbar navbar-right" role="search">
                <button onclick="createEarthquake();"class="btn btn-success">Earthquake!</button>
                <button class="btn btn-default">Button</button>
                <button class="btn btn-default">Button</button>
                <button class="btn btn-default">Button</button>
            </nav>
        </div>
    </div>
    
    <div class="row-fluid">
        <div class="row">
            <div id="main" style="" class="col-lg-7">
                <div id="map">
                
                </div>
                <div id="messages">
                    <p>down </p>
                </div>
            </div>
    
            <div id="journalWindow" class="col-lg-5">
                <div class="row">
                    <?php include 'journal.php';?>   
                </div>
                
            </div>
        </div>    
    </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>   
    <script src="js/eventHandler.js"></script>
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid.min.js"></script>
    
    
    
    
    <script>
            $(function() {
                    $( "#datePicker" ).datepicker();
            });
            
            
    </script>
    
    <script>
    /* global $ */
    
    var clients = [
            { "Station" : 0, "S-P (sec)" : 0, "Distance (km)": 0, "Amplitude": "0", "Magnitude": "0"},
            { "Station" : 0, "S-P (sec)" : 0, "Distance (km)": 0, "Amplitude": "0", "Magnitude": "0"},
            { "Station" : 0, "S-P (sec)" : 0, "Distance (km)": 0, "Amplitude": "0", "Magnitude": "0"},
            ];
    
    var stations = [
            { stationNumber: "-", Id: 0},
            { stationNumber: "1", Id: 1},
            { stationNumber: "2", Id: 2},
            { stationNumber: "3", Id: 3},
            { stationNumber: "4", Id: 4},
            { stationNumber: "5", Id: 5},
            { stationNumber: "6", Id: 6},
            { stationNumber: "7", Id: 7},
            { stationNumber: "8", Id: 8},
            { stationNumber: "9", Id: 9},
            ];
            $(function() {
                $("#jsGrid").jsGrid({
                    height: "90%",
                    width: "100%",
                    align: "center",
                    editing: true,
                    data: clients,
                    fields: [
                        { name: "Station", type: "select", items: stations, valueField: "Id", textField: "stationNumber"},
                        { name: "S-P (sec)", type: "text"},
                        { name: "Distance (km)", type: "text"},
                        { name: "Amplitude", type: "text"},
                        { name: "Magnitude", type: "text"}
                    ]
                });
            });
    </script>
    
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAV7Ogvd9V3Ot-DmjXEO2bqjsV55rXB11k&callback=loadJSON" async defer></script>

</body>

</html>

