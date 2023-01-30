package com.hedera.fee.calculation;

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
 * This class tests that the JSON schema works as expected.
 */
public class JsonSchemaTest {

  public static final String SCHEMA_FILE = "fee-model-schema.json";

  private static Schema createSchema() {
    final SchemaStore schemaStore = new SchemaStore();
    return Assertions.assertDoesNotThrow(
        () -> schemaStore.loadSchema(JsonSchemaTest.class.getResource(SCHEMA_FILE)));
  }

  private static void validate(final Schema schema, final URL jsonFileUrl)
      throws ValidationException, IOException {
    Objects.requireNonNull(schema);
    Objects.requireNonNull(jsonFileUrl);
    final Validator validator = new Validator();
    validator.validate(schema, jsonFileUrl);
  }

  @Test
  void testSchemaWorks() {
    //give
    final SchemaStore schemaStore = new SchemaStore();

    //then
    Assertions.assertDoesNotThrow(
        () -> schemaStore.loadSchema(JsonSchemaTest.class.getResource(SCHEMA_FILE)));
  }

  @Test
  void testMinimalFile() {
    //give
    final Schema schema = createSchema();
    final URL data = JsonSchemaTest.class.getResource("minimal.json");

    //then
    Assertions.assertDoesNotThrow(() -> validate(schema, data));
  }

  @Test
  void testOptionalBehavior1() {
    //give
    final Schema schema = createSchema();
    final URL data = JsonSchemaTest.class.getResource("optional1.json");

    //then
    Assertions.assertDoesNotThrow(() -> validate(schema, data));
  }

  @Test
  void testOptionalBehavior2() {
    //give
    final Schema schema = createSchema();
    final URL data = JsonSchemaTest.class.getResource("optional2.json");

    //then
    Assertions.assertThrows(ValidationException.class, () -> validate(schema, data));
  }

  @Test
  void testOptionalBehavior3() {
    //give
    final Schema schema = createSchema();
    final URL data = JsonSchemaTest.class.getResource("optional3.json");

    //then
    Assertions.assertDoesNotThrow(() -> validate(schema, data));
  }

  @Test
  void testComments() {
    //give
    final Schema schema = createSchema();
    final URL data = JsonSchemaTest.class.getResource("comments-allowed.json");

    //then
    Assertions.assertDoesNotThrow(() -> validate(schema, data));
  }


}
