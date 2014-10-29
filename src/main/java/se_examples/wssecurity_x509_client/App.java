package se_examples.wssecurity_x509_client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.ws.security.SecurityConstants;
import org.example.myservice.MyService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        try {
			String serviceURL = "http://localhost:18080/wssecurity-x509/myService";
			QName serviceName = new QName("http://www.example.org/myService/", "myService");
			URL wsdlURL = new URL(serviceURL + "?wsdl");
			Service service = Service.create(wsdlURL, serviceName);
			MyService proxy = (MyService)service.getPort(MyService.class);
			 
			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.CALLBACK_HANDLER, new KeystorePasswordCallback());
			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.SIGNATURE_PROPERTIES,
			     Thread.currentThread().getContextClassLoader().getResource("META-INF/alice.properties"));
//			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.ENCRYPT_PROPERTIES,
//			     Thread.currentThread().getContextClassLoader().getResource("META-INF/alice.properties"));
			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.SIGNATURE_USERNAME, "alice");
//			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.ENCRYPT_USERNAME, "bob");
			
			Client client = ClientProxy.getClient(proxy);
			Endpoint cxfEndpoint = client.getEndpoint();
			
			cxfEndpoint.getOutInterceptors().add(new LoggingOutInterceptor());
			cxfEndpoint.getInInterceptors().add(new LoggingInInterceptor());
			
//		    Map<String, Object> outProps = new HashMap<String, Object>();
//		    outProps.put(WSHandlerConstants.ACTION, "Signature");
//		    outProps.put(WSHandlerConstants.USER, "alice");
//		    outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
//		    		KeystorePasswordCallback.class.getName());
//		    outProps.put(WSHandlerConstants.SIG_PROP_FILE, "client_sign.properties");
//		    WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
//		    cxfEndpoint.getOutInterceptors().add(wssOut);
			
			System.out.println(proxy.newOperation("entrada"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
}
