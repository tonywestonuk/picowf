##Simple application Redirect.

This is a simple walkthrough to show how to get 2 applications linking to each other, without knowing each others URL.


1. Create a simple Web application named PicoTest1, in Eclipse this would be File/New Dynamic Web Project.

2. Include PicoCore in the deployment assembly. In Eclipse, Right click the project, properties, Web Deployment Assembly.

3. add the following context.xml at WebContent/META-INF/context.xml
    
    <Context>
	    <Parameter name="pico_serviceType" value="picoTest1"/>	
    </Context>

4. Add the following index.html at WebContent/index.html

    <html>
	    <h2>This is Pico Test 1  </h2>
	    <a href="picoTest2.redirect.pico">Click here to go to Pico Test 2</a>
    </html>
    

    
5. Create a second simple web application named PicoTest2
6. Include PicoCore (like above)
7. Add the following context.xml at WebContent/META-INF/context.xml
 
    <Context>
	    <Parameter name="pico_serviceType" value="picoTest1"/>	
    </Context>

8. Add the following index.html at WebContent/index.html

    <html>
	    <h2>This is Pico Test 2  </h2>
	    <a href="picoTest1.redirect.pico">Click here to go to Pico Test 2</a>
    </html>


9. Run both of these webapps on tomcat.


The two applications will be linking to each other. Clicking the anchor tag in the first, will link to the second, and vice versa.