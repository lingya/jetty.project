//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//


package org.eclipse.jetty.its.jetty_run_mojo_it;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import org.eclipse.jetty.client.HttpClient;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TestHelloServlet
{
    @Test
    public void hello_servlet()
        throws Exception
    {
        int port = getPort();
        HttpClient httpClient = new HttpClient();
        try
        {
            httpClient.start();

            String response = httpClient.GET( "http://localhost:" + port + "/hello?name=beer" ).getContentAsString();

            Assert.assertEquals( "hello beer", response.trim() );

            response = httpClient.GET( "http://localhost:" + port + "/ping?name=beer" ).getContentAsString();

            Assert.assertEquals( "pong beer", response.trim() );
        }
        finally
        {
            httpClient.stop();
        }
    }

    public int getPort()
    throws Exception
    {
        int attempts = 20;
        int port = -1;
        String s = System.getProperty("jetty.port.file");
        Assert.assertNotNull(s);
        File f = new File(s);
        while (true)
        {
            if (f.exists())
            {
                try (FileReader r = new FileReader(f);
                     LineNumberReader lnr = new LineNumberReader(r);
                    )
                {
                    s = lnr.readLine();
                    Assert.assertNotNull(s);
                    port = Integer.parseInt(s.trim());
                }
                break;
            }
            else
            {
                if (--attempts < 0)
                    break;
                else
                    Thread.currentThread().sleep(100);
            }
        }        
        return port;
    }
}
