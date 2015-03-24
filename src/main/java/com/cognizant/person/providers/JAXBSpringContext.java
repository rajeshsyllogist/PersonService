package com.cognizant.person.providers;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.support.SaxResourceUtils;
import org.springframework.util.Assert;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

// Adding comment to test the CICD - Chandra

public class JAXBSpringContext extends Jaxb2Marshaller {

    private JAXBContext jaxbContext;

    private String schemaLanguage;

    private Schema marshallerSchema;

	@SuppressWarnings("unused")
	private Schema unmarshallerSchema;


    public JAXBSpringContext() {
        setSchemaLanguage(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }

    @Override
    public synchronized JAXBContext getJaxbContext() {
        if (this.jaxbContext == null) {
            this.jaxbContext = super.getJaxbContext();
        }
        return jaxbContext;
    }


    @Override
    public Marshaller createMarshaller() {
        final Marshaller marshaller = super.createMarshaller();
        if (marshallerSchema != null) {
            marshaller.setSchema(marshallerSchema);
        }
        return marshaller;
    }

    /**
     * Purposefully commented the schema validation unmarshalling as we are facing few issues.
     * For workaround this validations would be taken care by application
     *
     * @return
     */
    @Override
    public Unmarshaller createUnmarshaller() {
        final Unmarshaller unmarshaller = super.createUnmarshaller();
        /*if (unmarshallerSchema != null) {
            unmarshaller.setSchema(unmarshallerSchema);
        }*/
        return unmarshaller;
    }

    /**
     * Set the schema language. Default is the W3C XML Schema:
     * <code>http://www.w3.org/2001/XMLSchema"</code>.
     *
     * @see XMLConstants#W3C_XML_SCHEMA_NS_URI
     * @see XMLConstants#RELAXNG_NS_URI
     */
    @Override
    public final void setSchemaLanguage(String schemaLanguage) {
        super.setSchemaLanguage(schemaLanguage);
        this.schemaLanguage = schemaLanguage;
    }

    /**
     * Set the schema resource to use for marshaller validation.
     */
    public void setMarshallerSchema(Resource schemaResource) throws IOException, SAXException {
        setMarshallerSchemas(new Resource[]{schemaResource});
    }

    /**
     * Set the schema resources to use for marshaller validation.
     */
    public void setMarshallerSchemas(Resource[] schemaResources) throws IOException, SAXException {
        this.marshallerSchema = schemaResources == null ? null : loadSchema(schemaResources, schemaLanguage);
    }

    /**
     * Set the schema resource to use for unmarshaller validation.
     * Purposefully commented the schema validation as we are facing few issues through XSD Validation
     */
    public void setUnmarshallerSchema(Resource[] schemaResource) throws IOException, SAXException {
        //setUnmarshallerSchemas(schemaResource);
    }

    /**
     * Set the schema resources to use for unmarshaller validation.
     */
    public void setUnmarshallerSchemas(Resource[] schemaResources) throws IOException, SAXException {
        this.unmarshallerSchema = schemaResources == null ? null : loadSchema(schemaResources, schemaLanguage);
    }

    private Schema loadSchema(Resource[] resources, String schemaLanguage) throws IOException, SAXException {
        Assert.notEmpty(resources, "No resources given");
        Assert.hasLength(schemaLanguage, "No schema language provided");
        final Source[] schemaSources = new Source[resources.length];
        final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        for (int i = 0; i < resources.length; i++) {
            Assert.notNull(resources[i], "Resource is null");
            Assert.isTrue(resources[i].exists(), "Resource " + resources[i] + " does not exist");
            final InputSource inputSource = SaxResourceUtils.createInputSource(resources[i]);
            schemaSources[i] = new SAXSource(xmlReader, inputSource);
        }
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);
        return schemaFactory.newSchema(schemaSources);
    }


}
