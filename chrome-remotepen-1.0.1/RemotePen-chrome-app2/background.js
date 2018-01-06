// Chrome Extension id
var laserExtensionId = 'opemnehoaehaaknhmgcebmalpkcgnlcc';
var address;

function b64DecodeUnicode(str) {
    return decodeURIComponent(Array.prototype.map.call(atob(str), function(c) {
        return '%' + c.charCodeAt(0).toString(16);
    }).join(''));
};

window.onload = function() {
  var tcpServer = chrome.sockets.tcpServer;
  var tcpSocket = chrome.sockets.tcp;
  var serverSocketId = null;
  
  
  var stringToUint8Array = function(string) {
    var buffer = new ArrayBuffer(string.length);
    var view = new Uint8Array(buffer);
    for (var i = 0; i < string.length; i++) {
      view[i] = string.charCodeAt(i);
    }
    return view;
  };

  var arrayBufferToString = function(buffer) {
    var str = '';
    var uArrayVal = new Uint8Array(buffer);
    for (var s = 0; s < uArrayVal.length; s++) {
      str += String.fromCharCode(uArrayVal[s]);
    }
    return str;
  };

  var destroySocketById = function(socketId) {
    tcpSocket.disconnect(socketId, function() {
      tcpSocket.close(socketId);
    });
  };

  var closeServerSocket = function() {
    if (serverSocketId) {
      tcpServer.close(serverSocketId, function() {
        if (chrome.runtime.lastError) {
          console.warn("chrome.sockets.tcpServer.close:", chrome.runtime.lastError);
        }
      });
    }

    tcpServer.onAccept.removeListener(onAccept);
    tcpSocket.onReceive.removeListener(onReceive);
  };

  var onAccept = function(acceptInfo) {
	console.log("Here is onAccept");

    tcpSocket.setPaused(acceptInfo.clientSocketId, false);

    if (acceptInfo.socketId != serverSocketId)
      return;

    console.log("ACCEPT", acceptInfo);
  };

  var onReceive = function(receiveInfo) {
    console.log("READ", receiveInfo);
    var socketId = receiveInfo.socketId;
    
    // Parse the request
    var data = arrayBufferToString(receiveInfo.data);
    console.log(data);

    if (data == "quit") {
      console.log("QUIT");
      // close socket and exit handler
      destroySocketById(socketId);
      return;
    } else if (data == "shutdown") {
      console.log("SHUTDOWN");
      closeServerSocket();
      return;
    }
    
    var w_data = b64DecodeUnicode(data);
    console.log(w_data);
    
    var json_pack = JSON.stringify({
      action: 'input',
      payload: w_data,
    });
    var pack_send = JSON.parse(json_pack);
    chrome.runtime.sendMessage(laserExtensionId, pack_send);
    
    
    
    destroySocketById(socketId);
    return;
  };
  
  
  window.RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;   //compatibility for firefox and chrome
    var pc = new RTCPeerConnection({iceServers:[]}), noop = function(){};      
    pc.createDataChannel("");    //create a bogus data channel
    pc.createOffer(pc.setLocalDescription.bind(pc), noop);    // create offer and set local description
    pc.onicecandidate = function(ice){  //listen for candidate events
        if(!ice || !ice.candidate || !ice.candidate.candidate)  return;
        address = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/.exec(ice.candidate.candidate)[1];
        console.log('my IP: ', address);   
        pc.onicecandidate = noop;
        
  // Create a socket
  tcpServer.create({}, function(socketInfo) {
    serverSocketId = socketInfo.socketId;

    tcpServer.listen(serverSocketId, address, 10500, 50, function(result) {
      console.log("LISTENING:", result);

      tcpServer.onAccept.addListener(onAccept);
      tcpSocket.onReceive.addListener(onReceive);
    });
  });   
    };
};

chrome.app.runtime.onLaunched.addListener(function() {
  chrome.app.window.create('index.html', {
    innerBounds: {
      width: 250,
      height: 200,
      minWidth: 200,
      minHeight: 150,
    }
  });
});
