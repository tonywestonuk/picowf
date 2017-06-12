var pico_info;
$.get("info.pico",function(data){
	pico_info=data;
});


function pico_setPath(fragment){	
	if (window.frameElement){
		parent.postMessage('setPath:'+fragment,'*');
	}  
}

function pico_launch(appURL){
	
}

function findFirstDiffPos(a, b) {
	  var i = 0;
	  if (a === b) return -1;
	  while (a[i] === b[i]) i++;
	  return i;
}

$(document).on("click","a", function(){
	var serverUrl=pico_info.pico_serverUrl;
	var thisUrl=this.href;
	
	if (thisUrl.indexOf(".redirect.pico")>-1){
		var pos=thisUrl.lastIndexOf("/",thisUrl.indexOf(".redirect.pico"))+1;
		var appType=thisUrl.substring(pos,thisUrl.indexOf(".redirect.pico"));		
		parent.postMessage('launch:'+appType,'*');
		return false;
	}
	
	if (thisUrl.indexOf(serverUrl)==0){
		var path=thisUrl.substring(serverUrl.length);	
		parent.postMessage('setPath:'+pico_info.pico_serviceType+path,'*');
	}
	
	return true;
});

