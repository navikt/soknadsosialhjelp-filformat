import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.nio.file.Paths;

public class XsdTest {

    @Test
    public void xmlV1Validerer() throws Exception {
        validerXml("v1/v1_test1.xml", "v1/xsd/sosialhjelp.xsd");
    }

//    @Test
//    public void xmlV2Validerer() throws Exception {
//        validerXml("v2.xml", "v2.xsd");
//    }
//
//    @Test
//    public void xmlV2ValidererForV1Konsumenter() throws Exception {
//        validerXml("v2.xml", "v1.xsd");
//    }



//    @Test
//    public void xmlV1FaarUtVerdier() throws Exception {
//        v1.Skjema element = unmarshall("v1.xml", "v1.xsd", v1.Skjema.class);
//
//        assertEquals("Ola Nordmann", element.getPersonalia().getNavn());
//        assertEquals("12345678901", element.getPersonalia().getPersonIdentifikator());
//        assertEquals(0, element.getAny().size());
//    }
//
//    @Test
//    public void xmlV2FaarUtVerdier() throws Exception {
//        v2.Skjema element = unmarshall("v2.xml", "v2.xsd", v2.Skjema.class);
//
//        assertEquals("Ola Nordmann", element.getPersonalia().getNavn());
//        assertEquals("12345678901", element.getPersonalia().getPersonIdentifikator());
//        assertEquals("Gift", element.getFamiliesituasjon().getStatus());
//        assertEquals(0, element.getAny().size());
//    }
//
//
//    @Test
//    public void xmlV2FaarUtVerdierForV1Konsumenter() throws Exception {
//        v1.Skjema element = unmarshall("v2.xml", "v1.xsd", v1.Skjema.class);
//
//        assertEquals("Ola Nordmann", element.getPersonalia().getNavn());
//        assertEquals("12345678901", element.getPersonalia().getPersonIdentifikator());
//        assertEquals(1, element.getAny().size());
//    }


    private void validerXml(String xml, String xsd) throws Exception {
        StreamSource source = lagXmlSource(xml);
        Validator validator = lagXsdSchema(xsd).newValidator();

        validator.validate(source);
    }

    private <T> T unmarshall(String xml, String xsd, Class<T> clazz) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(lagXsdSchema(xsd));

        return unmarshaller.unmarshal(lagXmlSource(xml), clazz).getValue();
    }

    private Schema lagXsdSchema(String xsd) throws Exception {
        File xsdFil = Paths.get("src/test/resources/" + xsd).toFile();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(xsdFil);
    }

    private StreamSource lagXmlSource(String xml) {
        File xmlFil = Paths.get("src/test/resources/" + xml).toFile();
        return new StreamSource(xmlFil);
    }
}
