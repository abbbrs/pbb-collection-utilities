package com.pbb;

import java.util.*;

/**
 * Structural utilities for computing the frequency distributions of collections.
 *
 * <p>This class contains a static, stateless worker engine designed to analyze raw element
 * configurations and isolate peak occurrences. It operates strictly within a package-private
 * scope to guarantee complete implementation encapsulation and allow decoupled unit testing.
 *
 * @author  pbb
 * @since   1.0
 */
final class FrequencyDistributionUtilities {

    /**
     * Suppresses the default constructor to enforce a strictly static utility contract.
     */
    private FrequencyDistributionUtilities() {
        throw new AssertionError("Instantiation of frequency utilities is prohibited.");
    }

    /**
     * Searches the specified collection for the most frequently occurring elements.
     * The collection does not need to be sorted prior to making this call.
     *
     * <p><b>Example of Use:</b></p>
     * <pre>{@code
     * List<String> votes = List.of("apple", "banana", "apple", "cherry", "banana");
     * Set<String> leaders = FrequencyDistributionUtilities.findMostFrequent(votes);
     * // Result: [apple, banana]
     * }</pre>
     *
     * <p>This method runs in O(n) time and requires O(d) auxiliary space, where
     * n is the total number of elements in the collection and d is the number of
     * distinct elements. This assumes the elements provide a stable, constant-time
     * implementation of {@link Object#hashCode()} and {@link Object#equals(Object)}.
     *
     * <p>If the specified collection is null or empty, this method immediately
     * returns an unmodifiable empty {@link Set} without throwing an exception.
     *
     * @param  <E> the class of the objects in the collection
     * @param  collection the collection to be scanned for the most frequent elements,
     *         may be null.
     * @return an unmodifiable {@link Set} containing all elements that occur most frequently
     *         in the collection; an empty {@link Set} if the collection is null or empty.
     * @throws IllegalArgumentException if the collection contains elements that are instances
     *         of {@link Float} or {@link Double}.
     * @throws NullPointerException if the collection contains {@code null} elements
     *         and the map implementation does not permit null keys.
     */
    static <E> Set<E> findMostFrequent(Collection<E> collection) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptySet();
        }

        Map<E, Integer> frequencyMap = new HashMap<>();
        int maxCount = 0;

        for (E element : collection) {
            if (element instanceof Float || element instanceof Double) {
                throw new IllegalArgumentException(
                        "Floating-point numbers (Float, Double) are disallowed in this version due to precision constraints."
                );
            }

            int newCount = frequencyMap.getOrDefault(element, 0) + 1;
            frequencyMap.put(element, newCount);
            if (newCount > maxCount) {
                maxCount = newCount;
            }
        }

        Set<E> topTier = new HashSet<>();
        for (Map.Entry<E, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() == maxCount) {
                topTier.add(entry.getKey());
            }
        }

        return Collections.unmodifiableSet(topTier);
    }
}
