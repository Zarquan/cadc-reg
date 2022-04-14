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

package ca.nrc.cadc.vosi;

import ca.nrc.cadc.date.DateUtil;
import ca.nrc.cadc.util.Log4jInit;
import ca.nrc.cadc.xml.XmlUtil;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangsa
 */
public class AvailabilityTest {
    private static Logger log = Logger.getLogger(AvailabilityTest.class);

    static {
        Log4jInit.setLevel("ca.nrc.cadc.vosi", Level.INFO);
    }

    private final Map<String, String> schemaMap = new HashMap<>();
    private final DateFormat df = DateUtil.getDateFormat(DateUtil.IVOA_DATE_FORMAT, DateUtil.LOCAL);

    /**
     * @throws java.lang.Exception For any errors during setup.
     */
    @Before
    public void setUp() throws Exception {
        this.schemaMap.put(VOSI.AVAILABILITY_NS_URI,
                           XmlUtil.getResourceUrlString(VOSI.AVAILABILITY_SCHEMA, AvailabilityTest.class));
    }

    @Test
    public void testFullRoundTripAvailability() throws Exception {
        Availability expected = null;
        try {
            expected = new Availability(true, "noteA");
            expected.downAt = df.parse("2009-04-12T11:22:33.444"); //yyyy-MM-dd'T'HH:mm:ss.SSS
            expected.backAt = df.parse("2009-05-12T11:22:33.444"); //yyyy-MM-dd'T'HH:mm:ss.SSS
            expected.upSince = df.parse("2009-06-12T11:22:33.444"); //yyyy-MM-dd'T'HH:mm:ss.SSS
            
        } catch (ParseException e) {
            log.error("test code bug", e);
        }
        expected.clientIP = "192.168.1.3";
        
        Document doc = Availability.toXmlDocument(expected);
        XMLOutputter xop = new XMLOutputter(Format.getPrettyFormat());
        Writer stringWriter = new StringWriter();
        xop.output(doc, stringWriter);
        String xmlString = stringWriter.toString();
        StringReader reader = new StringReader(xmlString);
        Document doc2 = XmlUtil.buildDocument(reader, schemaMap);
        
        Availability actual = Availability.fromXmlDocument(doc2);
        Assert.assertEquals(expected.isAvailable(), actual.isAvailable());
        Assert.assertEquals(expected.note, actual.note);
        Assert.assertEquals(expected.downAt, actual.downAt);
        Assert.assertEquals(expected.upSince, actual.upSince);
        Assert.assertEquals(expected.backAt, actual.backAt);
        Assert.assertEquals(expected.clientIP, actual.clientIP);
    }

    @Test
    public void testMinimalRoundTripAvailability() throws Exception {
        Availability expected = new Availability(true);
        Document doc = Availability.toXmlDocument(expected);
        XMLOutputter xop = new XMLOutputter(Format.getPrettyFormat());
        Writer stringWriter = new StringWriter();
        xop.output(doc, stringWriter);
        String xmlString = stringWriter.toString();
        StringReader reader = new StringReader(xmlString);
        Document doc2 = XmlUtil.buildDocument(reader, schemaMap);
        
        Availability actual = Availability.fromXmlDocument(doc2);
        Assert.assertEquals(expected.isAvailable(), actual.isAvailable());
        Assert.assertEquals(expected.note, actual.note);
        Assert.assertEquals(expected.downAt, actual.downAt);
        Assert.assertEquals(expected.upSince, actual.upSince);
        Assert.assertEquals(expected.backAt, actual.backAt);
        Assert.assertEquals(expected.clientIP, actual.clientIP);
        
    }
}
