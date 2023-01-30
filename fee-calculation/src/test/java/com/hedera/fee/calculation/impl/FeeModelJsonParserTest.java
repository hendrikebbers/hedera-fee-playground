package com.hedera.fee.calculation.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hedera.fee.calculation.JsonSchemaTest;
import com.hedera.fee.calculation.model.OperationMetadata;
import com.hedera.fee.calculation.model.ParameterMetadata;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class FeeModelJsonParserTest {

  @Test
  void testParser() {
    //given
    final URL resource = JsonSchemaTest.class.getResource("minimal.json");

    //when
    final Set<ServiceMetadata> services = assertDoesNotThrow(
        () -> FeeModelJsonParser.parseJsonModel(resource));

    //then
    assertNotNull(services);
    assertEquals(1, services.size());

    final ServiceMetadata service = services.stream().collect(Collectors.toList()).get(0);
    assertNotNull(service);
    assertEquals("TokenService", service.name());
    assertEquals("Service that handle all token operations", service.description());
    assertNotNull(service.operations());
    assertEquals(1, service.operations().size());

    final OperationMetadata operation = service.operations().stream().collect(Collectors.toList())
        .get(0);
    assertNotNull(operation);
    assertEquals("CreateToken", operation.name());
    assertEquals("Operation to create a token", operation.description());
    assertEquals("2 * ${tokenSize}", operation.formulaExpression());
    assertEquals(new BigDecimal(0), operation.minimalValue());
    assertNotNull(operation.parameters());
    assertEquals(1, operation.parameters().size());

    final ParameterMetadata parameter = operation.parameters().stream().collect(Collectors.toList())
        .get(0);
    assertNotNull(parameter);
    assertEquals("tokenSize", parameter.name());
    assertEquals("The size of the token", parameter.description());
    assertEquals(true, parameter.optional());
    assertEquals(null, parameter.defaultValue());
  }

}