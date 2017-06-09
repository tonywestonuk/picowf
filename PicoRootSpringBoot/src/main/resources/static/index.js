
var updateIframe = function(hash){
var moduleName=hash.substring(1);

// remove iframe from dom...
	var iframe=$("iframe");
	iframe.remove();

	if (moduleName.indexOf("/")>-1){
		var path=moduleName.substring(moduleName.indexOf("/")+1);
		moduleName=moduleName.substring(0,moduleName.indexOf("/"));
		iframe.attr("src",moduleName+'.redirect.pico?path='+path);
	} else{
		iframe.attr("src",moduleName+'.redirect.pico');
	}
	
	$('#container').append(iframe);
}



if (window.location.hash){
	updateIframe(window.location.hash);
}


$.get('findServices.pico', function(results){
		var navBar = $('ul.nav');
		
		// Sort menus by prioroty
		var compareByMenuPriority = function(a,b) {
				 if (a.menuPri==null && b.menuPri!=null)
					 return -1;
				 if (a.menuPri!=null && b.menuPri==null)
					 return 1;
				 if (a.menuPri < b.menuPri)
				    return -1;
				  if (a.menuPri > b.menuPri)
				    return 1;
				  return 0;
				}


		results.services.sort(compareByMenuPriority);
	
		$(results.services).each(function(){	
			if (this.menuName!=null){			
				if (!this.menuName.includes("/"))
					navBar.append('<li><a class="menuLink" href="#'+this.serviceType+'" data-url="'+this.serviceType+'">'+this.menuName+'</a></li>')	
				else { 
					var menuName=this.menuName.substring(0,this.menuName.indexOf("/"))
					var menuItem=this.menuName.substring(this.menuName.indexOf("/")+1)

					if ($('.ddmenu_'+menuName).length==0) {
						navBar.append('<li class="dropdown ddmenu_'+menuName+'"> '+
			            	    	  '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">'+menuName+' <span class="caret"></span></a>' +
			               			  '<ul class="dropdown-menu"></ul></li>');	
						}
						$('.ddmenu_'+menuName).find('ul')
							.append('<li><a class="menuLink" href="#'+this.serviceType+'" data-url="'+this.serviceType+'">'+menuItem+'</a></li>')
					}
				}
			});
	});
	

	$(window).on('hashchange', function(e){
			updateIframe(window.location.hash);
	});
	
	//respond to events
	window.addEventListener('message',function(event) {	
		if (event.data.indexOf('setPath:')==0){
			history.replaceState(null, null, '#'+event.data.substring(8));
		}
		if (event.data.indexOf('launch:')==0){
			history.pushState(null, null, '#'+event.data.substring(7));
			updateIframe(window.location.hash);
		}

		
	},false);
	