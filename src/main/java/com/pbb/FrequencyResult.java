package com.pbb;

import java.util.Set;

/**
 * A strictly immutable data container holding pre-calculated frequency distribution metrics.
 *
 * <p>Since this component encapsulates a completely frozen state snapshot with no mutable variables,
 * it is inherently thread-safe. Instances of {@code FrequencyResult} can be freely shared, published,
 * and polled concurrently across multiple thread runtimes without external synchronization or locking overhead.</p>
 *
 * @param <T> the type of elements tracked within this result matrix
 * @param all an unmodifiable {@link Set} of all elements sharing the highest frequency score
 * @param isUnique {@code true} if exactly one element holds the maximum frequency;
 *                 {@code false} if a tie exists or the input data was empty/null
 *
 * @author pbb
 * @since  1.0
 */
public record FrequencyResult<T>(Set<T> all, boolean isUnique) {

    /**
     * Extracts the single winner directly without wrapper overhead.
     *
     * <p><b>Safety Contract:</b> This method must only be called if {@link #isUnique()}
     * returns {@code true}.</p>
     *
     * @return the unique most frequent element
     * @throws IllegalStateException if a frequency tie exists (i.e., {@link #isUnique()} is false)
     *                               or if the source collection was empty/null
     */
    public T getUnique() {
        if (!isUnique) {
            throw new IllegalStateException(
                    "Execution prohibited: The distribution matrix contains a frequency tie or is empty. Verify via isUnique() first."
            );
        }
        return all.iterator().next();
    }
}
