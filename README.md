A simple client-server application on sockets

It is a file server on which the client can save his files, as well as download them and delete them from the server

The client can specify any name by saving the file to the server. If the file needs to be saved with the original name, the client should press enter without entering anything

The files that the client wants to save must be located at the path .../client/data/{file name}. Enter the full name along with the file extension

Multithreading is implemented on the server, so it can serve several clients at once