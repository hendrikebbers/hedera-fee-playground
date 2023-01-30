package com.hedera.fee.calculation;

import static com.hedera.fee.calculation.JsonSchemaTest.SCHEMA_FILE;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.ValidationException;
import net.jimblackler.jsonschemafriend.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class tests that the current JSON model is valid against the JSON schema.
 */
public class ModelTest {

  @Test
  void testRealData() {
    //give
    final SchemaStore schemaStore = new SchemaStore();
    final Schema schema = Assertions.assertDoesNotThrow(() -> schemaStore.loadSchema(JsonSchemaTest.class.getResource(SCHEMA_FILE)));
    final URL data = ModelTest.class.getResource("fee-model.json");
    final Validator validator = new Validator();

    //then
    Assertions.assertDoesNotThrow(() -> validator.validate(schema, data));
  }
}
