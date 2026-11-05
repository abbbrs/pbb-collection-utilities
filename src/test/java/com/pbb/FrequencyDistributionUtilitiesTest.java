package com.pbb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * Unit testing suite for the isolated {@link FrequencyDistributionUtilities} computational engine.
 *
 * <p>This suite ensures the correctness of the core frequency mapping algorithm and verifies
 * compliance with strict type constraints across nominal, tie, and failure scenarios.
 *
 * <p>The {@code FrequencyDistributionUtilitiesTest} class provides the following validation coverage:
 * <ul>
 *   <li>{@link #testFindMostFrequent_Nominal()} — Verifies the extraction of a single majority string leader.</li>
 *   <li>{@link #testFindMostFrequent_Integers()} — Validates signed numeric frequency boundaries using sequential and repeating integer blocks.</li>
 *   <li>{@link #testFindMostFrequent_Tie()} — Ensures that multiple elements sharing the peak occurrence score are preserved without truncation.</li>
 *   <li>{@link #testFindMostFrequent_ThrowsOnFloat()} — Proves strict execution rejection upon detecting fractional {@link Float} instances.</li>
 *   <li>{@link #testFindMostFrequent_ThrowsOnDouble()} — Proves strict execution rejection upon detecting fractional {@link Double} instances.</li>
 * </ul>
 *
 * @author pbb
 * @since  1.0
 */
class FrequencyDistributionUtilitiesTest {

    @Test
    @DisplayName("Nominal Case: Extract single majority leader")
    void testFindMostFrequent_Nominal() {
        List<String> input = List.of("apple", "banana", "apple", "cherry", "banana", "apple");
        Set<String> result = FrequencyDistributionUtilities.findMostFrequent(input);
        assertEquals(Set.of("apple"), result);
    }

    @Test
    @DisplayName("Numeric Evaluation: Validate signed integer processing stability")
    void testFindMostFrequent_Integers() {
        List<Integer> input = List.of(-5, 42, -5, 0, 42, -5);
        Set<Integer> result = FrequencyDistributionUtilities.findMostFrequent(input);
        assertEquals(Set.of(-5), result);
    }

    @Test
    @DisplayName("Tie Scenario: Extract all elements sharing maximum frequency score")
    void testFindMostFrequent_Tie() {
        List<String> input = List.of("apple", "banana", "apple", "cherry", "banana");
        Set<String> result = FrequencyDistributionUtilities.findMostFrequent(input);
        assertEquals(Set.of("apple", "banana"), result);
    }

    @Test
    @DisplayName("Numerical Constraint: Throws IllegalArgumentException on Float type detection")
    void testFindMostFrequent_ThrowsOnFloat() {
        List<Float> input = List.of(1.5f, 2.0f, 1.5f);
        assertThrows(IllegalArgumentException.class, () -> {
            FrequencyDistributionUtilities.findMostFrequent(input);
        });
    }

    @Test
    @DisplayName("Numerical Constraint: Throws IllegalArgumentException on Double type detection")
    void testFindMostFrequent_ThrowsOnDouble() {
        List<Double> input = List.of(10.5, 3.14, 10.5);
        assertThrows(IllegalArgumentException.class, () -> {
            FrequencyDistributionUtilities.findMostFrequent(input);
        });
    }
}
