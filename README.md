# flere-service, an embedded java HTTP nano service framework

flere-service is an embedded HTTP service which can be used to build small services
which communicate over HTTP.

This service is not intended to serve websites and does not implement all HTTP features.

## What flere-service can do

* start an embedded http service with a few lines of code

* receive parameters from GET or POST requests, or raw POST data


## Known shortcomings

* flere-service does not handle HTTP multipart request	

* does not implement TLS sockets. If you require communication over tls, use a proxy like nginx.

* no support for cookie handling, if you require cookies you will need to set cookie headers manually


## Also, what flere-service is not

* flere-service is not a full-fledged server framework like jetty, tomcat or glassfish

* likely you do not want to implement a complex website with flere-service


## Example Usage

```
import com.augmentedlogic.flere.service.*;

class Test
{
    public static void main( String[] args )
    {
        FlereService ns = new FlereService("localhost", 8080);
                     ns.addHandler(new TestHandler());
                     try {
                         ns.start();
                     } catch(Exception e) {
                         System.out.println(e);
                     }
    }

}
```



```
import com.augmentedlogic.flere.service.*;


public class TestHandler implements StandardHandler
{

   public HttpResponse handle(HttpRequest request)
   {
      HttpResponse response = new HttpResponse();
                   response.setBody("hello world from the test handler!");

      return response;
   }

}
```


```
import com.augmentedlogic.flere.service.*;


public class TestHandler implements StandardHandler
{

   public HttpResponse handle(HttpRequest request)
   {
      HttpResponse response = new HttpResponse();

                   // get the request method like "GET", "POST" etc
                   String http_method = request.getMethod();

                   // get the request path such as "/api/json"
                   String path = request.getPath();  

                   // get the URL query string
                   String query = request.getQuery();

                   // fetching a GET or POST parameter
                   String param = request.getParameter("hello");


                   // fetching a GET or POST parameter as Integer, setting a default
                   // value of 2 if parameter is not set

                   String string_param = request.getString("s", "hello");
                   Integer int_param = request.getInteger("i", 2);
                   Double double_param = request.getDouble("d", 2.0);

                   // get part of the path, e.g. if the request is /product/GDFJSH
                   String product = request.getPathElement(2);

                   // fetching the request body
                   String postdata = request.getPostdata();

                   // get a request header
                   String user_agent = request.getHeader("user-agent");

                   // if a basic auth header has been sent
                   String user = request.getBasicAuthUser();
                   String password = request.getBasicAuthPassword();

                   // set a custom response header
                   response.addHeader("Pragma", "no-cache");

                   // we respond with application/json and 201 created
                   response.setContentType("application/json");
                   response.setHttpStatus(HttpStatus.CREATED);
                   response.setBody("['hello', 'world']");

      return response;
   }

}
```

### Routing/Multiple Handlers


```
import com.augmentedlogic.flere.service.*;

class Test
{
    public static void main( String[] args )
    {
        FlereService ns = new FlereService("localhost", 8080);
                     ns.addHandler("/test", new TestHandler());
                     ns.addHandler("/api/json", new ApiHandler());
                     ns.addHandler("/", new TestHandler());
                     ns.setDebug(false);
                     ns.setBacklog(4096);
                     try {
                         ns.start();
                     } catch(Exception e) {
                         System.out.println(e);
                     }
    }

}
```


