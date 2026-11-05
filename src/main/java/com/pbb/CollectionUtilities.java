package com.pbb;

import java.util.Collection;
import java.util.Set;

/**
 * A collection tracking the frequency distribution and metrics of its elements.
 *
 * <p>This interface is the single unified type required to access all functionalities
 * provided by this library. It captures the target dataset and manages the entire
 * lifecycle of its frequency evaluation.
 *
 * <p>The {@code CollectionUtilities} interface provides the following capabilities:
 * <ul>
 *   <li>{@link #findMostFrequent(Collection)} — Extracts all peak frequent elements in a single stateless call.</li>
 *   <li>{@link #source(Collection)} — Captures the dataset to initiate an advanced evaluation pipeline.</li>
 *   <li>{@link #analyzeFrequency()} — Triggers the structural distribution computation pass.</li>
 *   <li>{@link #hasUniqueLeader()} — Assesses whether an undisputed, single frequency leader exists.</li>
 *   <li>{@link #getLeader()} — Extracts the unique absolute winner directly without wrapper overhead.</li>
 *   <li>{@link #getLeaders()} — Retrieves the complete, untruncated set of all top-occurring elements.</li>
 * </ul>
 *
 * <h2>Examples of Use:</h2>
 *
 * <p><b>Example 1: Stateless Extraction (All Leaders)</b></p>
 * <pre>{@code
 * List<String> votes = List.of("apple", "banana", "apple", "cherry", "banana");
 * Set<String> leaders = CollectionUtilities.findMostFrequent(votes);
 * // Output: [apple, banana]
 * }</pre>
 *
 * <p><b>Example 2: Advanced Pipeline (Uniqueness Flow)</b></p>
 * <pre>{@code
 * var fre = CollectionUtilities.source(myCollection)
 *                              //.withMinThreshold(5)
 *                              //.withMaxThreshold(9)
 *                              //.withSomethingElse(se1)
 *                              .analyzeFrequency();
 * if (fre.isUnique()) {
 *     String clearWinner = fre.getLeader();
 * } else {
 *     Set<String> tiedLeaders = fre.getLeaders();
 * }
 * }</pre>
 *
 * <p><b>Concurrency Note:</b></p>
 * This interface contract guarantees safe concurrent execution paths through an absolute immutability strategy:
 * <ul>
 *   <li>The static utility {@link #findMostFrequent(Collection)} is completely stateless and thread-safe,
 *       confining its working memory strictly within the executing thread's stack.</li>
 *   <li>All pipeline configurations initiated via {@link #source(Collection)} are strictly immutable blueprints.
 *       Any subsequent pipeline operation always instantiates and returns a completely new immutable object,
 *       making uncalculated pipeline configurations inherently safe for concurrent sharing across multiple threads.</li>
 *   <li>The terminal calculation pass triggered via {@link #analyzeFrequency()} produces a strictly immutable
 *       {@link FrequencyResult} record which is inherently safe for unrestricted concurrent multithreaded polling.</li>
 * </ul>
 *
 * @param <T> the type of elements tracked within this frequency distribution
 * @author pbb
 * @since  1.0
 */
public interface CollectionUtilities<T> {

    /**
     * Instantly extracts all elements tying for the peak frequency score.
     *
     * @param <E> the type of elements inside the collection
     * @param collection the target dataset to evaluate, may be null or empty
     * @return an unmodifiable {@link Set} of all highest-occurring elements
     * @throws IllegalArgumentException if floating-point types (Float, Double) are detected
     */
    static <E> Set<E> findMostFrequent(Collection<E> collection) {
        return CollectionUtilitiesImpl.findMostFrequent(collection);
    }

    /**
     * Initiates the advanced evaluation flow by capturing the target data source.
     *
     * @param <E> the type of elements inside the collection
     * @param collection the target dataset to analyze, may be null or empty
     * @return an uncalculated instance of {@link CollectionUtilities} acting as a source blueprint
     */
    static <E> CollectionUtilities<E> source(Collection<E> collection) {
        return CollectionUtilitiesImpl.createPipeline(collection);
    }

    /**
     * Triggers the structural frequency analysis computation pass.
     *
     * <p>This terminal operation processes the dataset in a single-pass execution
     * and encapsulates the immutable analytical snapshot into a thread-safe record wrapper.</p>
     *
     * @return a strictly immutable {@link FrequencyResult} holding the pre-calculated frequency metrics
     * @throws IllegalArgumentException if floating-point types (Float, Double) are detected
     */
    FrequencyResult<T> analyzeFrequency();

    /**
     * Assesses whether there is an undisputed, single winner in the data.
     *
     * @return {@code true} if exactly one element holds the maximum frequency;
     *         {@code false} if a tie exists or the input was null/empty
     */
    boolean hasUniqueLeader();

    /**
     * Extracts the single winner directly without wrapper overhead.
     *
     * <p><b>Safety Contract:</b> This method must only be called if {@link #hasUniqueLeader()}
     * returns {@code true}.</p>
     *
     * @return the unique most frequent element
     * @throws IllegalStateException if a frequency tie exists (i.e., {@link #hasUniqueLeader()} is false)
     *                               or if the source collection was empty/null
     */
    T getLeader();

    /**
     * Extracts all peak frequent elements in O(1) from the calculated state.
     *
     * @return an unmodifiable {@link Set} of all elements sharing the highest frequency score
     */
    Set<T> getLeaders();
}
