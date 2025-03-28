package nl.openvalue.customerstatementprocessor.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;

public record Statement(
		@JacksonXmlProperty(localName = "reference", isAttribute = true) Long reference,
		@JacksonXmlProperty(localName = "accountNumber") String accountNumber,
		@JacksonXmlProperty(localName = "description") String description,
		@JacksonXmlProperty(localName = "startBalance") BigDecimal startBalance,
		@JacksonXmlProperty(localName = "mutation") BigDecimal mutation,
		@JacksonXmlProperty(localName = "endBalance") BigDecimal endBalance
) {
}
