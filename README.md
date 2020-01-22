This is a sample microservice to demonstrate API rate limiting using Http Request Interceptor.

The request interceptor will handle the HTTP request prior to passing it to the controller.
This interceptor will then consume the number of requests allowed in the specified time limit and pass them to the controller to give client appropriate API response and if the number of requests exceeds the limit the API will respond with error code 429(request limit exceeded).

We are using Bucket library to keep limit the number of access token provided to the client. The token refill strategy used here is Greedy strategy. 
