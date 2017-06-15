# Pico Core

This java module broadcasts your service, and looks up other services managed by pico.

### Configuration
Pico configuration is stored as context parameters. As with any context parameters, can be stored within the app Server context, but also the application itself in context.xml. For example:


    <Context>
	    <Parameter name="pico_serviceType" value="helloWorld"/>
	    <Parameter name="pico_menuName" value="Hello World"/>
    </Context>

By default, pico will work with no configuration at all, and assumes that it is being run on a local development PC.




The configuration parameters are as follows.


__pico_serverUrl__ : 
The root URL of the server pico is running on.  Do not include the context path of your application as this will be automatically added.  This configuration should be stored within the global app server context.xml as it will be the same for all applications running on this server.  If not defined, it defaults to http://localhost:8080


__pico_serviceGroup__ :
As pico can work across many servers and networks, a service group identifier is needed to identify which environment the services belong to.  For example, you may have a set of services running on a 'integration' environment,  and another set of services running in a 'development' environment. It would cause problems if the two environments located each other!  setting the service group identifies which group your services belongs to. Pico will only locate other services running in the same group.   By default, service group is set to blanks. however the multicast packets are set to not leave the local computer and so not interfear with other instances of pico running on the network.


__pico_serviceType__ : 
Specifies the type of service. For example, this could be 'customerMaintenance', or 'orderEntry'.  the service type is the identifier that is passed from other services to pico to locate, that want to make use of this service. 


__pico_remoteServiceUrl__ :
Pico can also locate services on a remote server out of range of multicast packets.  If this is required, specify the name of any pico service running on this server. This will retrieve the list of discovered services by this pico service, and included it with the list of services discovered locally. 

 
__pico_debug__ :
This causes pico to output to console any service discovery packets received, for debuging.



### Pico Server APIs

Pico offers the following API's that are available at any context / path.  


__{serviceType}.redirect.pico?path=path/to/endpoint__ :
returns with a redirect (HTTP 302), to the actual URL running on the located service. for example

     <a href="orderInfo.redirect.pico?path=123">
     	  Click for information regarding order 123
     </a>


__{serviceType}.info.pico__ :
Returns information regarding to this service that pico is aware of. This additional info may have been stored as additional context parameters.

__findServices.pico__ :
Returns information about all services known to pico as Json.  For example:
     
     $.get('findServices.pico', function(results){
			$(results.services).each(function(){	
				console.log(this);
			}
		});