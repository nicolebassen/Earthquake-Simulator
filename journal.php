<div class="container-fluid">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#journal" aria-controls="journal" role="tab" data-toggle="tab">Journal</a></li>
        <li role="presentation"><a href="#progress" aria-controls="progress" role="tab" data-toggle="tab">Progress</a></li>
    </ul>
    
    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="journal">
            <form>
                <div class="form-group">
                        <label for="inputName">Name</label>
                        <input type="text" class="form-control" id="inputName" placeholder="Name">
                </div>
                <div class="form-group">
                        <label for="datePicker">Date</label>
                        <input type="text" class="form-control" id="datePicker" placeholder="Today's Date">
                </div>
                <div class="form-group">
                    <label>Data Table for Epicenter Location and Richter Magnitude Determination</label>
		    <div class="form-inline">
			<div id="jsGrid"></div>
		    </div>	
                </div>
                <div class="form-group">
                    <label for="epicenterLatitude">Epicenter Latitude</label>    
                    <div class="form-inline">
                        <div class="input-group">
                                <input type="number" class="form-control" id="epicenterLatitudeDegrees">
                                <div class="input-group-addon">&deg;</div>
                        </div>
                        <div class="input-group">
                                <input type="number" class="form-control" id="epicenterLatitudeMinutes">
                                <div class="input-group-addon">"</div>
                        </div>
                    </div>    
                </div>
                <div class="form-group">
                    <label for="epicenterLatitude">Epicenter Longitude</label>
                    <div class="form-inline">
                        <div class="input-group">
                            <input type="number" class="form-control" id="epicenterLongitudeDegrees">
                            <div class="input-group-addon">&deg;</div>
                        </div>
                        <div class="input-group">
                            <input type="number" class="form-control" id="epicenterLongitudeMinutes">
                            <div class="input-group-addon">"</div>
                        </div>    
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary">Check</button> 
            </form>    
            
        </div>
        <div role="tabpanel" class="tab-pane" id="progress">
            <div>
                Future Home for keeping track of progress
            </div>
            
        </div>
    </div>
    
</div>

	