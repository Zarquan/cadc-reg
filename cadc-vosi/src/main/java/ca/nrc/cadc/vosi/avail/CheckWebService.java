/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2022.                            (c) 2022.
*  Government of Canada                 Gouvernement du Canada
*  National Research Council            Conseil national de recherches
*  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
*  All rights reserved                  Tous droits réservés
*                                       
*  NRC disclaims any warranties,        Le CNRC dénie toute garantie
*  expressed, implied, or               énoncée, implicite ou légale,
*  statutory, of any kind with          de quelque nature que ce
*  respect to the software,             soit, concernant le logiciel,
*  including without limitation         y compris sans restriction
*  any warranty of merchantability      toute garantie de valeur
*  or fitness for a particular          marchande ou de pertinence
*  purpose. NRC shall not be            pour un usage particulier.
*  liable in any event for any          Le CNRC ne pourra en aucun cas
*  damages, whether direct or           être tenu responsable de tout
*  indirect, special or general,        dommage, direct ou indirect,
*  consequential or incidental,         particulier ou général,
*  arising from the use of the          accessoire ou fortuit, résultant
*  software.  Neither the name          de l'utilisation du logiciel. Ni
*  of the National Research             le nom du Conseil National de
*  Council of Canada nor the            Recherches du Canada ni les noms
*  names of its contributors may        de ses  participants ne peuvent
*  be used to endorse or promote        être utilisés pour approuver ou
*  products derived from this           promouvoir les produits dérivés
*  software without specific prior      de ce logiciel sans autorisation
*  written permission.                  préalable et particulière
*                                       par écrit.
*                                       
*  This file is part of the             Ce fichier fait partie du projet
*  OpenCADC project.                    OpenCADC.
*                                       
*  OpenCADC is free software:           OpenCADC est un logiciel libre ;
*  you can redistribute it and/or       vous pouvez le redistribuer ou le
*  modify it under the terms of         modifier suivant les termes de
*  the GNU Affero General Public        la “GNU Affero General Public
*  License as published by the          License” telle que publiée
*  Free Software Foundation,            par la Free Software Foundation
*  either version 3 of the              : soit la version 3 de cette
*  License, or (at your option)         licence, soit (à votre gré)
*  any later version.                   toute version ultérieure.
*                                       
*  OpenCADC is distributed in the       OpenCADC est distribué
*  hope that it will be useful,         dans l’espoir qu’il vous
*  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
*  without even the implied             GARANTIE : sans même la garantie
*  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
*  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
*  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
*  General Public License for           Générale Publique GNU Affero
*  more details.                        pour plus de détails.
*                                       
*  You should have received             Vous devriez avoir reçu une
*  a copy of the GNU Affero             copie de la Licence Générale
*  General Public License along         Publique GNU Affero avec
*  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
*  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
*                                       <http://www.gnu.org/licenses/>.
*
*  $Revision: 4 $
*
************************************************************************
 */

package ca.nrc.cadc.vosi.avail;

import ca.nrc.cadc.net.HttpGet;
import ca.nrc.cadc.vosi.Availability;
import ca.nrc.cadc.vosi.VOSI;
import ca.nrc.cadc.xml.XmlUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * Check the /availability resource of another web service.
 *
 * @author zhangsa
 *
 */
public class CheckWebService implements CheckResource {

    private static Logger log = Logger.getLogger(CheckWebService.class);
    
    private final URL availabilityURL;
    private final boolean fullCheck;

    /**
     * Constructor.
     * @param availabilityURL the URL of availability endpoint of a service
     * @throws IllegalArgumentException wrapping MalformedURLException
     */
    @Deprecated
    public CheckWebService(String availabilityURL) throws IllegalArgumentException {
        this.fullCheck = true; // default to full check as before
        try {
            this.availabilityURL = new URL(availabilityURL);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("invalid URL: " + availabilityURL, ex);
        }
    }
    
    /**
     * Perform default check. Default is currently a full check.
     * 
     * @param availabilityURL the URL of availability endpoint of a service
     */
    public CheckWebService(URL availabilityURL) {
        this(availabilityURL, false); // default to connectivity check
        //this(availabilityURL, true); // default to full check
    }
    
    /**
     * Perform the specified check. A full check calls the remote availability and checks the
     * the status in the response XML, so this causes a full check of the remote service.
     * Otherwise, this only performs a minimal check (detail=min) which normally only checks
     * connectivity to the remote service.
     * 
     * @param availabilityURL the URL of availability endpoint of a service
     * @param fullCheck true for normal check, false for detail=min connectivity check
     */
    public CheckWebService(URL availabilityURL, boolean fullCheck) {
        this.fullCheck = fullCheck;
        if (fullCheck) {
            this.availabilityURL = availabilityURL;
        } else {
            try {
                this.availabilityURL = new URL(availabilityURL.toExternalForm() + "?detail=min");
            } catch (MalformedURLException ex) {
                throw new RuntimeException("BUG: failed to append params to " + availabilityURL, ex);
            }
        }
    }

    @Override
    public void check() throws CheckException {
        if (fullCheck) {
            log.debug("fullcheck==true " + availabilityURL);
            doFullCheck();
        } else {
            log.debug("fullcheck==false " + availabilityURL);
            HttpGet get = new HttpGet(availabilityURL, true);
            get.setConnectionTimeout(9000);
            get.setReadTimeout(9000);
            long t = System.currentTimeMillis();
            get.run();
            if (get.getResponseCode() != 200 || get.getThrowable() != null) {
                throw new CheckException("availability check failed: " + availabilityURL 
                        + " code: " + get.getResponseCode()
                        + " cause: " + get.getThrowable());
            }
            long dt = System.currentTimeMillis() - t;
            log.debug("fullcheck==false dt=" + dt);
        }
        
    }
    
    private void doFullCheck() throws CheckException {
   
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            HttpGet get = new HttpGet(availabilityURL, bos);
            get.setConnectionTimeout(20000);
            get.setReadTimeout(60000);
            get.run();
            if (get.getThrowable() != null) {
                throw new CheckException("availability check failed: " + availabilityURL 
                        + " code: " + get.getResponseCode() 
                        + " cause: " + get.getThrowable().getMessage());
            }
            Map<String, String> schemaMap = new HashMap<>();
            schemaMap.put(VOSI.AVAILABILITY_NS_URI, XmlUtil.getResourceUrlString(VOSI.AVAILABILITY_SCHEMA, CheckWebService.class));

            StringReader reader = new StringReader(bos.toString());
            Document doc = XmlUtil.buildDocument(reader, schemaMap);

            Availability wsa = Availability.fromXmlDocument(doc);
            if (wsa.isAvailable()) {
                log.debug("test succeeded: " + availabilityURL);
                return;
            }

            throw new CheckException("service " + availabilityURL + " is not available, reported reason: " + wsa.note);
            
        } catch (JDOMException | ParseException ex) {
            throw new CheckException("invalid output from " + availabilityURL, ex);
        } catch (Exception ex) {
            log.error("unexpected test fail: " + availabilityURL, ex);
            throw new CheckException("unexpected test fail: " + availabilityURL, ex);
        }
    }
}
