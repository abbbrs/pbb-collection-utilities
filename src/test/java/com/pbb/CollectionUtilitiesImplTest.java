package com.pbb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * Unit testing suite for the concrete {@link CollectionUtilitiesImpl} thread-safe immutable pipeline.
 *
 * <p>This suite tests operation sequencing and result composition across different execution modes.
 * Frequency counting logic is tested separately in an isolated component.
 *
 * <p>The {@code CollectionUtilitiesImplTest} class provides the following validation coverage:
 * <ul>
 *   <li>{@link #testPipeline_HappyPathUnique()} — Validates the successful tracking workflow when a single undisputed winner exists.</li>
 *   <li>{@link #testPipeline_HappyPathMultiple()} — Validates the successful tracking workflow when multiple elements share the top frequency count.</li>
 *   <li>{@link #testPipeline_StateIdempotency()} — Verifies that the returned calculation record dataset is safely locked and immutable.</li>
 *   <li>{@link #testPipeline_UniqueWinnerPollingSafety()} — Proves that unique winner extraction fails instantly if multiple elements share the highest frequency count.</li>
 *   <li>{@link #testPipeline_NullSource()} — Verifies that a null source input transitions gracefully into a non-unique, empty evaluation state.</li>
 *   <li>{@link #testPipeline_EmptySource()} — Verifies that an empty source input transitions gracefully into a non-unique, empty evaluation state.</li>
 * </ul>
 *
 * @author pbb
 * @since  1.0
 */
class CollectionUtilitiesImplTest {

    @Test
    @DisplayName("Happy Path 1: Successful workflow execution delivering a single unique winner")
    void testPipeline_HappyPathUnique() {
        List<String> input = List.of("apple", "banana", "apple");
        FrequencyResult<String> result = CollectionUtilities.source(input).analyzeFrequency();

        assertTrue(result.isUnique(), "Should confirm a unique winner exists.");
        assertEquals("apple", result.getUnique(), "Should extract the correct absolute winner.");
        assertEquals(Set.of("apple"), result.all(), "The combined view should contain exactly the unique winner.");
    }

    @Test
    @DisplayName("Happy Path 2: Successful workflow execution delivering multiple tied frequency leaders")
    void testPipeline_HappyPathMultiple() {
        List<String> input = List.of("apple", "banana", "apple", "banana");
        FrequencyResult<String> result = CollectionUtilities.source(input).analyzeFrequency();

        assertFalse(result.isUnique(), "Should confirm that no single winner exists due to a tie.");
        assertEquals(Set.of("apple", "banana"), result.all(), "The combined view must return all elements sharing the top spot.");
    }

    @Test
    @DisplayName("State Idempotency: Verify calculated metrics set view remains fixed and unmodifiable")
    void testPipeline_StateIdempotency() {
        List<String> input = List.of("apple", "banana", "apple");
        FrequencyResult<String> result = CollectionUtilities.source(input).analyzeFrequency();
        Set<String> computedLeaders = result.all();

        assertThrows(UnsupportedOperationException.class, () -> computedLeaders.add("cherry"),
                "The returned structural analysis set must be unmodifiable to protect the record state.");
    }

    @Test
    @DisplayName("Polling Safety: Enforce Fail-Fast execution crash upon illegal unique access on ties")
    void testPipeline_UniqueWinnerPollingSafety() {
        List<String> input = List.of("apple", "banana");
        FrequencyResult<String> result = CollectionUtilities.source(input).analyzeFrequency();

        assertThrows(IllegalStateException.class, result::getUnique,
                "Invoking getUnique() when multiple elements share the highest frequency count must fail.");
    }

    @Test
    @DisplayName("Edge Case: Handle null source collection gracefully through the pipeline execution")
    void testPipeline_NullSource() {
        FrequencyResult<String> result = CollectionUtilities.<String>source(null).analyzeFrequency();

        assertFalse(result.isUnique(), "A null source collection cannot produce a unique winner.");
        assertTrue(result.all().isEmpty(), "The returned set for a null source must be empty.");
        assertThrows(IllegalStateException.class, result::getUnique,
                "Invoking getUnique() on a null source snapshot execution must fail.");
    }

    @Test
    @DisplayName("Edge Case: Handle empty source collection gracefully through the pipeline execution")
    void testPipeline_EmptySource() {
        List<String> emptyList = List.of();
        FrequencyResult<String> result = CollectionUtilities.source(emptyList).analyzeFrequency();

        assertFalse(result.isUnique(), "An empty source collection cannot produce a unique winner.");
        assertTrue(result.all().isEmpty(), "The returned set for an empty source must be empty.");
        assertThrows(IllegalStateException.class, result::getUnique,
                "Invoking getUnique() on an empty source snapshot execution must fail.");
    }
}
