// ==UserScript==
// @name         3DAgarMassBot
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       BadPlayerMaster
// @run-at       document-end
// @match        http://asia1.biome3d.com/
// @match        http://asia.biome3d.com/
// @match        http://*.biome3d.com/
// @require      https://cdnjs.cloudflare.com/ajax/libs/socket.io/1.7.3/socket.io.min.js
// @require      https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js
// @require      https://code.jquery.com/ui/1.12.1/jquery-ui.min.js
// @grant        none
// ==/UserScript==
var clientServerIp = "ws://127.0.0.1:8080";
window.user = {
    x: 0,
    y: 0,
    connected: false,
    ip: null,
    origin: null
};

function prepareData(a) {
    return new DataView(new ArrayBuffer(a))
}

function _x(STR_XPATH) {
    var xresult = document.evaluate(STR_XPATH, document, null, XPathResult.ANY_TYPE, null);
    var xnodes = [];
    var xres;
    while (xres = xresult.iterateNext()) {
        xnodes.push(xres);
    }

    return xnodes;
}

(function() {
    function IsJsonString(str) {
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
    }
    WebSocket.prototype._send = WebSocket.prototype.send;
    WebSocket.prototype.send = function(data) {
        this._send(data);
        this.addEventListener('message', function(msg) {
            var buf = new Uint8Array(msg.data);
            if (!IsJsonString(msg.data)) {
                try {
                    var view1 = new DataView(msg.data);
                    if (view1.getUint8(0, true) === 6) {
                        console.log("DEAD");
                    }
                } catch (e) {

                }
            }
        }, false);
        this.send = function(data) {
            var buf = new Uint8Array(data);
            if (!IsJsonString(data)) {
                try {
                    var view1 = new DataView(data);
                    if (view1.byteLength === 9) {
                        window.user.x = (view1.getFloat32(1, true) / 100);
                        window.user.y = (view1.getFloat32(5, true) / 100);
                        //console.log("x: " + (view1.getFloat32(1, true) / 100) + " y: " + (view1.getFloat32(5, true) / 100));
                    }
                } catch (e) {

                }
            }
            window.user.origin = location.origin;
            //console.log(window.user);
            if (this.url !== null) {
                window.user.ip = this.url;
                //console.log(window.user.ip);
            }

            this._send(data);
            //console.log("<< " + buf);
        };
        var buf = new Uint8Array(data);
        console.log("<< " + buf);
    }

    var socket = new WebSocket(clientServerIp);
    socket.onopen = function() {
        window.user.connected = true
    };
    socket.onclose = function() {
        window.user.connected = false
    }

    setTimeout(function() { //<div style='box-shadow: 0px 0px 20px black;z-index:9999999; background-color: #000000; -moz-opacity: 0.4; -khtml-opacity: 0.4; opacity: 0.7; zoom: 1; width: 205px; top: 300px; left: 10px; display: block; position: absolute; text-align: center; font-size: 15px; color: #ffffff; font-family: Ubuntu;border: 2px solid #0c31d4;'> <div style='color:#ffffff; display: inline; -moz-opacity:1; -khtml-opacity: 1; opacity:1;font-size: 22px; filter:alpha(opacity=100); padding: 10px;'><a>Trap Client</a></div> <div style=' color:#ffffff; display: inline; -moz-opacity:1; -khtml-opacity: 1; opacity:1; filter:alpha(opacity=100); padding: 10px;'><br>Minions: <a id='minionCount'>Offline</a> </div><button id='start-bots' style='display: block;border-radius: 5px;border: 2px solid #6495ED;background-color: #BCD2EE;height: 50px;width: 120px;margin: auto;text-align: center;'>StartBots </button><marquee>TrapKillo - Owner</marquee> </div>
        $(_x('//*[@id="screen"]/canvas')).after("<div  id = 'gui' style='box-shadow: 0px 0px 20px black;z-index:9999999; background-color: #000000; -moz-opacity: 0.4; -khtml-opacity: 0.4; opacity: 0.7; zoom: 1; width: 205px; top: 300px; left: 10px; display: block; position: absolute; text-align: center; font-size: 15px; color: #ffffff; font-family: Ubuntu;border: 2px solid #0c31d4; border-radius: 15px 50px;'> <div style='color:#ffffff; display: inline; -moz-opacity:1; -khtml-opacity: 1; opacity:1;font-size: 22px; filter:alpha(opacity=100); padding: 10px;'><a id='Client_Name'>Agar infinity</a></div> <div style=' color:#ffffff; display: inline; -moz-opacity:1; -khtml-opacity: 1; opacity:1; filter:alpha(opacity=100); padding: 10px;'><br>Minions: <a id='minionCount'>Offline</a> </div><button id='start-bots' style='display: block;border-radius: 5px;border: 2px solid #6495ED;background-color: #BCD2EE;height: 50px;width: 120px;margin: auto;text-align: center;'>StartBots </button></div>");
        document.getElementById('start-bots').onclick = function() {
            if (window.user.connected) {
                socket.send(window.user.ip + " " + window.user.origin);
                var msg = prepareData(1);
                msg.setUint8(0, 0, true);
                socket.send(msg);
            }
        };
    }, 5000);
    socket.onmessage = function(event) {
        $('#minionCount').html(event);
    }
    setInterval(function() {
        var msg = prepareData(10);
        msg.setUint8(0, 1);
        msg.setFloat32(1, window.user.x, true);
        msg.setFloat32(5, window.user.y, true);
        if (window.user.connected) {
            socket.send(msg);
        }

    }, 100);
    $(this).on('keypress', function(event) {
        if (event.keyCode == 69) {
            var msg = prepareData(1);
            msg.setUint8(0, 2, true);
            socket.send(msg);
        }
    })
})()

var speed = 100;
var hex = new Array("00", "14", "28", "3C", "50", "64", "78", "8C", "A0", "B4", "C8", "DC", "F0");
var r = 1;
var g = 1;
var b = 1;
var seq = 1;

function changetext() {
    var rainbow = "#" + hex[r] + hex[g] + hex[b];
    document.getElementById("gui").style.borderColor = rainbow;
    document.getElementById("Client_Name").style.color = rainbow;
    document.getElementById("minionCount").style.color = rainbow;
}

function change() {
    if (seq == 6) {
        b--;
        if (b == 0)
            seq = 1;
    }
    if (seq == 5) {
        r++;
        if (r == 12)
            seq = 6;
    }
    if (seq == 4) {
        g--;
        if (g == 0)
            seq = 5;
    }
    if (seq == 3) {
        b++;
        if (b == 12)
            seq = 4;
    }
    if (seq == 2) {
        r--;
        if (r == 0)
            seq = 3;
    }
    if (seq == 1) {
        g++;
        if (g == 12)
            seq = 2;
    }
    changetext()
}
setTimeout(function() {
    setInterval(function() {
        change();
    }, speed);
}, 5000);
setTimeout(function() {
    $(function() {
        $("#gui").draggable();
    });
}, 5000);
