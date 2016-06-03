const MILLIMETER_INTERVALS= 10;

var c = document.getElementById("myCanvas");
var ctx = c.getContext("2d");
var myHeight;
var distance;
var maxDistance;
var spInterval;
var pAmplitude;
var sAmplitude;
var duration;
var scale;
var scaleFactor = 5.0;
var randomSeed;
var ampDraggingY;
var scaleX;
var flag = true; //Set true to run
var lastY = 0.0;
var lastCosSign = 1;
var lastYSign = 1;
var xOn = 0.0;
var lastTime = 0.0;
var heightFactor = 50.0;

//Magnitude and maxdistance/distance impact seismagraph
var magnitude = 7.0
// NOTE: Need to research what a and b represent, something to do with maybe randomizing Y values with no other significants
var a = 1.0;
var b = 1.0;
    
/**
 * This method computes the p amplitude for the graph
 * 
 * @param {number} distance The current distance
 * @param {number} maxDistance The maximum distance
 */
function computePAmplitude(distance, maxDistance)
{
    return Math.pow(10.0, (-1.7214611458189999 - (0.015095238948699999 * (-2.507889 + 0.10811900000000001 * distance + 0.00085919999999999996 * distance * distance)) / (-0.018665000000000001 + 0.0020479999999999999 * distance + 1.3349999999999999E-06 * distance * distance)) + maxDistance);
}
    
/**
 * Sets the distance of station to epicenter
 * 
 * @param {number} newDistance The new distance 
 * @param {number} newMaxDistance The new maximum distance
 */
function setDistance(newDistance, newMaxDistance)
{
    ctx.beginPath();
    ctx.clearRect(0, 0, c.width, c.height);
    distance = newDistance;
    maxDistance = newMaxDistance;
    spInterval = 0.23200000000000001 * newDistance - 3.0109799999999999E-06 * newDistance * newDistance - (0.12889999999999999 * newDistance - 1.44672E-06 * newDistance * newDistance);
    pAmplitude = this.computePAmplitude(newDistance, magnitude);
    sAmplitude = pAmplitude / 5;
    duration = spInterval * ((newMaxDistance * 3) / newDistance);
}
    
/**
 * Pseudo Random using a seed to generate randomness
 * 
 * @param {number} seed To use when generating a random number
 */
function ran(seed) {
    console.log((Math.random() * seed + 1.0) - seed / 2.0);
    return (Math.random() * seed + 1.0) - seed / 2.0;
}
    
/**
 * This function is used compute the next step of the graph
 * @param {number} currentTime The time interval for the current point
 * @param {number} height The height of the step
 */
function computeNextStep(currentTime, height) {
    var d1 = 0.0;
    var k;
    if (lastY <= 0.0) {
        k = -1;
    }else{
        k = 1;
    }
    console.log("K: "+k);
    if (k != lastYSign) {
        a = this.ran(0.45000000000000001) - 0.29999999999999999 * this.ran(0.45000000000000001);
    }
    
    lastYSign = k;
    // NOTE: This looks odd I think xOn is the position of x it is adding the current interval minus last time
    // but the next line equals current time interval which erases the previous calculation?
    xOn += currentTime - lastTime;
    //xOn = currentTime;
    var j;
    
    if(Math.cos(b * xOn * 4.0) <= 0.0){
        j = -1;
    }else{
        j = 1;
    }
    
    if (j != lastCosSign) {
         b = this.ran(0.22500000000000001) - 0.14999999999999999;
        if(j < 0)
            xOn = 1.5707963267948966 / b / 4.0;
        else
            xOn = 4.7123889803846897 / b / 4.0; 
    }
    
    lastCosSign = j;
    console.log("Current Time: "+currentTime+"<"+ spInterval + distance/10.0);
    
    if (currentTime < spInterval + (distance/10.0) && currentTime >= distance /10.0){
        var d3 = (currentTime - distance/10.0)* 0.90000000000000002;
        console.log("d3: "+d3);
        var d4;
        if (distance > 500.0 && magnitude < 6.4000000000000004) {
            d4 = 14.300000000000001;
        }else if (distance > 500.0) {
            d4 = 5.0;
        }else if (distance > 400.0) {
            d4 = 3.0;
        }else{
            d4 = 2.1000000000000001;
        }
        var d2 = (d4 * Math.exp(-0.050000000000000003 * (d3 - 15.0) * (100.0 / spInterval)) * sAmplitude) / 10.0;
        if(distance < 150.0){
            d2 /= currentTime - distance / 10.0;
        }
        console.log("d2: "+d2);
        var d5 = ((d2 * height) / heightFactor) * a;
        if(d5 < 0.0){
            d5 = 0.0 * a;
        }
        d1 = d5 * Math.sin(b * xOn * 4.0);
    }else if (currentTime <= spInterval + distance / 10.0 + 2.0 && currentTime >= spInterval + distance / 10.0) {
        d1 = (height / heightFactor) * pAmplitude * (1.0 - ((spInterval + distance / 10.0 + 2.0) - currentTime) / 4.0) * Math.sin(b * xOn * 4.0);
    }else if(currentTime <= spInterval + distance / 10.0 + 3.0 && currentTime >= spInterval + distance / 10.0){
        d1 = (height / heightFactor) * pAmplitude * Math.sin(b * xOn * 4.0);
    }else if(currentTime < duration + distance / 15.0 && currentTime >= spInterval + distance / 10.0){
        var d7 = currentTime - (spInterval + distance / 10.0) * 0.69999999999999996;
        var d6 = (height / heightFactor) * pAmplitude * (1.0 - d7 / (duration - spInterval)) * ((spInterval + distance / 10.0) / currentTime) * a;
        if(d7 >= spInterval * 0.59999999999999998 && d7 <= spInterval * 0.65000000000000002){
            d6 *= 1.6000000000000001;
        }
        if(d6 < 4.0){
            d6 = 8.0 * a;
        }
        d1 = d6 * Math.sin(b * xOn * 4.0);
    }else{
        d1 = (height / heightFactor) * 0.0 * a * 1.8999999999999999 * Math.sin(b * xOn * 4.0);
    }
    if(d1 > (pAmplitude * height) / heightFactor){
        d1 = pAmplitude;
    }
    if(d1 < (-pAmplitude * height) / heightFactor){
        d1 = -pAmplitude;
    }
    lastTime = currentTime;
    lastY = d1;
    console.log("d1: "+d1);
    return d1;
}
    
/**
 * Draws seismagraph and sets basic stats for seismagraph chart
 * 
 * @param {number} setWidth The width of the chart
 * @param {number} setHeight The height of the chart
 * @param {number} setDistance Set the distance from the quake
 * @param {number} setMaxDistance Set the farthest distance from the quake
 * @param {number} setScale Set the scale of the graph
 * @param {boolean} setFlag 
 */
function setupChart(setWidth,setHeight,setDistance, setMaxDistance, setScale, setFlag){
    myHeight = setHeight;
    // TODO: Need to determine what it does
    var flag1 = false;
    //if seed used for random is not unsigned, then set seed to variable randomSeed
 
    scale = setScale;
    // TODO: Need to determine what it does
    var d2 = 0.0;
    // If parameter flag is true and ampDraggingY is not unsigned then y moves from the half way point of max height.
    // See in original design the ability to measure amplitude on y-axis starting from 0 to the peak of the wave.
    if(flag && ampDraggingY != 0x7fffffff){
        d2 = (ampDraggingY - setHeight/ 2) / scaleFactor;
    }
    scaleFactor = setScale / 100;
    if(flag && ampDraggingY != 0x7fffffff){
        ampDraggingY = Math.round(d2 * scaleFactor) + setHeight / 2;
    }
    
    this.setDistance(setDistance, setMaxDistance);
    
    // Scale length of X when drawn
    scaleX = setWidth / duration;
    
    var startX = 0x7fffffff;
    var startY = 0x7fffffff;
    
    // Time from start of earthquake til end. Basically following on the x-axis
    for(var earthquakeInterval = 0.0; earthquakeInterval <= duration * 1.0; earthquakeInterval += 0.050000000000000003){
       var endX = Math.round(earthquakeInterval * scaleX);
       console.log("Height In:"+ setHeight);
       console.log("Before subtracting from "+setHeight/2+"-"+Math.round(computeNextStep(earthquakeInterval, setHeight) * scaleFactor));
       var endY = setHeight / 2 - Math.round(computeNextStep(earthquakeInterval, setHeight) * scaleFactor);
       if (startX != 0x7fffffff) {
            console.log("PointStart("+startX+","+startY+")");
            console.log("PointEnd("+endX+","+endY+")");
            ctx.moveTo(startX,startY);
            ctx.lineTo(endX,endY);
            ctx.stroke();
       }
       
       startX = endX;
       startY = endY;
       if (endY < 0 || endY > setHeight - 1){
            flag1 = true;
       }
    }
    console.log('Flag:'+flag1);
    return flag1;
    
}

/**
 *  This is a function that adds a click listener to then set up the data for the 
 *  graph canvas.
 */
document.getElementById("button").addEventListener("click", function(){
    var changeDistance = document.getElementById("distance").value;
    var changeMaxDistance = document.getElementById("maxDistance").value;
    var changeScale = document.getElementById("scale").value;
    this.magnitude = document.getElementById("magnitude").value;
    this.heightFactor = document.getElementById("heightFactor").value;
    console.log(changeDistance);
    console.log(changeMaxDistance);
    console.log(changeScale);
    setupChart(600, 300, changeDistance, changeMaxDistance, changeScale,false);
});