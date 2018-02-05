WebSever Progam
170024030
10/11/2017
--------------------------------------------------------------------------------------------------
GENERAL USAGE NOTES
--------------------------------------------------------------------------------------------------
- To compile, use
	javac *.java

- To run, use
	java WebServerMain <document_root> <port>
--------------------------------------------------------------------------------------------------
PARTS IMPLEMENTED
--------------------------------------------------------------------------------------------------
- Basic requirements: all implemented

- Enhancements implemented
	Returning of binary images (GIF, JPEG and PNG) - "src/www/jugg.png" and "src/www/jugg.gif".
		 are included for testing.

	Multithreading - support multiple concurrent connection requests.

	Logging - "src/www/server_log.txt".

	Supporting DELETE method.

- Extensions implemented
	415 unspported error included

	Error web pages are shown in web browser when errors encountered (404, 415, 505)
		the web pages were modified on the base of internet sources:
		https://github.com/cba85/template-error-pages.
	
	Allowing the server to quit whenever it get keyboard-input "Q".

	(Added favicon.ico for tabs (Mozilla and Chrome)).
--------------------------------------------------------------------------------------------------
