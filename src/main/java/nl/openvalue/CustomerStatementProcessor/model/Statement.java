package nl.openvalue.CustomerStatementProcessor.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.math.BigDecimal;

@JacksonXmlRootElement(localName = "record")
public record Statement(
        @JacksonXmlProperty(localName = "transactionReference") Long transactionReference,
        @JacksonXmlProperty(localName = "accountNumber") String accountNumber,
        @JacksonXmlProperty(localName = "startBalance") BigDecimal startBalance,
        @JacksonXmlProperty(localName = "mutation") BigDecimal mutation,
        @JacksonXmlProperty(localName = "endBalance") BigDecimal endBalance,
        @JacksonXmlProperty(localName = "description") String description
) {}
