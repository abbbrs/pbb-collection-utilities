package com.pbb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * Unit testing suite for the public entry points of the {@link CollectionUtilities} interface.
 *
 * <p>This suite ensures that the public static facade operations function correctly, validating
 * both the direct stateless extraction layer and the entry point for the stateful pipeline.
 *
 * <p>The {@code CollectionUtilitiesTest} class provides the following validation coverage:
 * <ul>
 *   <li>{@link #testInterface_FindMostFrequent_Nominal()} — Verifies the public static method correctly routes and extracts all peak elements.</li>
 *   <li>{@link #testInterface_Build_Entry()} — Validates that the static factory initialization creates a functional data source without immediate execution.</li>
 * </ul>
 *
 * @author pbb
 * @since  1.0
 */
class CollectionUtilitiesTest {

    @Test
    @DisplayName("Interface Static Layer: Verify direct findMostFrequent static routing works")
    void testInterface_FindMostFrequent_Nominal() {
        List<String> input = List.of("apple", "banana", "apple");
        Set<String> result = CollectionUtilities.findMostFrequent(input);
        assertEquals(Set.of("apple"), result);
    }

    @Test
    @DisplayName("Interface Factory Layer: Verify build factory initializes the pipeline context")
    void testInterface_Build_Entry() {
        List<String> input = List.of("apple", "banana");
        CollectionUtilities<String> source = CollectionUtilities.source(input);
        assertNotNull(source);
    }
}
