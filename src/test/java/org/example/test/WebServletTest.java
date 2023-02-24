package org.example.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;


class WebServletTest {

	@Test
	void testHelloServletGet() throws Exception {
		
		HttpClient client = new HttpClient();
        client.start();

        ContentResponse res = client.GET("http://155.248.227.33:8080/audio0923/HelloServlet");
        
        System.out.println(res.getContentAsString());
        
        client.stop();
		
	}
	
	
	@Test
	void testBlockingServletGet() throws Exception {
		
		HttpClient client = new HttpClient();
        client.start();

        ContentResponse res = client.GET("http://155.248.227.33:8080/audio0923/BlockingServlet");
        
        System.out.println(res.getContentAsString());
        
        client.stop();
		
	}
	
	@Test
	void testAsyncServletGet() throws Exception {
		
		String url = "http://155.248.227.33:8080/audio0923/longtask";
		HttpClient client = new HttpClient();
        client.start();

        ContentResponse response = client.GET(url);

		assertThat(response.getStatus(), equalTo(200));
		
		String responseContent = IOUtils.toString(response.getContent());
		
		 System.out.println(responseContent);
		//assertThat(responseContent, equalTo( "This is some heavy resource that will be served in an async way"));
		
	}

}






