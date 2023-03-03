/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2011.                            (c) 2011.
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
*  $Revision: 5 $
*
************************************************************************
 */

package ca.nrc.cadc.reg;

import ca.nrc.cadc.xml.XmlUtil;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Namespace;

/**
 * This class defines the URI and corresponding namespace constants.
 *
 * @author yeunga
 */
public class XMLConstants {

    public static final URI SIA_11_NS = URI.create("http://www.ivoa.net/xml/SIA/v1.1");
    public static final URI STC_13_NS = URI.create("http://www.ivoa.net/xml/STC/stc-v1.30.xsd");
    public static final URI TAPREGEXT_10_NS = URI.create("http://www.ivoa.net/xml/TAPRegExt/v1.0");
    public static final URI VODATASERVICE_11_NS = URI.create("http://www.ivoa.net/xml/VODataService/v1.1");
    public static final URI VORESOURCE_10_NS = URI.create("http://www.ivoa.net/xml/VOResource/v1.0");
    public static final URI VOSICAPABILITIES_10_NS = URI.create("http://www.ivoa.net/xml/VOSICapabilities/v1.0");
    public static final URI UWSREGEXT_10_NS = URI.create("http://www.ivoa.net/xml/UWSRegExt/v0.1");
    public static final URI REGISTRY_10_NS = URI.create("http://www.ivoa.net/xml/VORegistry/v1.0");
    public static final URI REG_IFACE_10_NS = URI.create("http://www.ivoa.net/xml/RegistryInterface/v1.0");
    public static final URI SCS_11_NS = URI.create("http://www.ivoa.net/xml/ConeSearch/v1.0");

    public static final URI XLINK_NS = URI.create("http://www.w3.org/1999/xlink");

    private static final String SIA_11_SCHEMA = "SIA-v1.1.xsd";
    private static final String STC_13_SCHEMA = "STC-v1.3.xsd";
    private static final String TAPREGEXT_10_SCHEMA = "TAPRegExt-v1.0-with-erratum1.xsd";
    
    @Deprecated
    private static final String VODATASERVICE_11_SCHEMA = "VODataService-v1.1.xsd";
    private static final String VODATASERVICE_12_SCHEMA = "VODataService-v1.2.xsd";
    
    private static final String VORESOURCE_10_SCHEMA = "VOResource-v1.1-with-erratum1.xsd"; // minor update w/ same namespace
    private static final String VOSICAPABILITIES_10_SCHEMA = "VOSICapabilities-v1.0.xsd";
    private static final String UWSREGEXT_10_SCHEMA = "UWSRegExt-v0.1.xsd";
    private static final String REGISTRY_10_SCHEMA = "VORegistry-v1.0.xsd";
    private static final String REG_IFACE_10_SCHEMA = "RegistryInterface-v1.0.xsd";
    private static final String SCS_11_SCHEMA = "SCS-v1.1.xsd";

    private static final String XLINK_SCHEMA = "XLINK.xsd";

    // Maps namespace URI to xsd schema file name
    public static final Map<String, String> SCHEMA_MAP = new HashMap<String, String>();

    static {
        String sia11URL = XmlUtil.getResourceUrlString(SIA_11_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(SIA_11_NS.toString(), sia11URL);

        String stc13URL = XmlUtil.getResourceUrlString(STC_13_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(STC_13_NS.toString(), stc13URL);

        String tapRegEx10URL = XmlUtil.getResourceUrlString(TAPREGEXT_10_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(TAPREGEXT_10_NS.toString(), tapRegEx10URL);

        // yes, VODataService-1.1 namespace with 1.2 xsd
        String voDataSvc11URL = XmlUtil.getResourceUrlString(VODATASERVICE_12_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(VODATASERVICE_11_NS.toString(), voDataSvc11URL);

        String voResource10URL = XmlUtil.getResourceUrlString(VORESOURCE_10_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(VORESOURCE_10_NS.toString(), voResource10URL);

        String voCapabilties10URL = XmlUtil.getResourceUrlString(VOSICAPABILITIES_10_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(VOSICAPABILITIES_10_NS.toString(), voCapabilties10URL);

        String uwsRegEx10URL = XmlUtil.getResourceUrlString(UWSREGEXT_10_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(UWSREGEXT_10_NS.toString(), uwsRegEx10URL);

        String reg10URL = XmlUtil.getResourceUrlString(REGISTRY_10_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(REGISTRY_10_NS.toString(), reg10URL);

        String regInter10URL = XmlUtil.getResourceUrlString(REG_IFACE_10_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(REG_IFACE_10_NS.toString(), regInter10URL);

        String xlinkSchemaURL = XmlUtil.getResourceUrlString(XLINK_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(XLINK_NS.toString(), xlinkSchemaURL);

        String coneSearchURL = XmlUtil.getResourceUrlString(SCS_11_SCHEMA, XMLConstants.class);
        SCHEMA_MAP.put(SCS_11_NS.toString(), coneSearchURL);
    }

    public static final Namespace CAPABILITIES_NS = Namespace.getNamespace("vosi", VOSICAPABILITIES_10_NS.toString());
    public static final Namespace VODATASERVICE_NS = Namespace.getNamespace("vs", VODATASERVICE_11_NS.toString());
    public static final Namespace VORESOURCE_NS = Namespace.getNamespace("vr", VORESOURCE_10_NS.toString());
}
