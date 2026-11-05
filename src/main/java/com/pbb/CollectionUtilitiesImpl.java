package com.pbb;

import java.util.Collection;
import java.util.Set;

/**
 * Concrete hidden engine implementing the {@link CollectionUtilities} contracts.
 *
 * <p>This structure acts as an immutable configuration blueprint. Every intermediate
 * pipeline operation (for V2+) will instantiate and return a completely new
 * implementation object, ensuring absolute thread-safety during configuration.</p>
 *
 * @param <T> the type of elements tracked within this frequency distribution
 * @author pbb
 * @since  1.0
 */
final class CollectionUtilitiesImpl<T> implements CollectionUtilities<T> {

    private final Collection<T> sourceCollection;

    /**
     * Private constructor enforcing instance immutability.
     */
    private CollectionUtilitiesImpl(Collection<T> collection) {
        this.sourceCollection = collection;
    }

    static <E> CollectionUtilities<E> createPipeline(Collection<E> collection) {
        return new CollectionUtilitiesImpl<>(collection);
    }

    static <E> Set<E> findMostFrequent(Collection<E> collection) {
        return FrequencyDistributionUtilities.findMostFrequent(collection);
    }

    @Override
    public FrequencyResult<T> analyzeFrequency() {
        // Compute the frequency map instantly and freeze the results inside the record
        Set<T> leaders = FrequencyDistributionUtilities.findMostFrequent(this.sourceCollection);
        boolean isUnique = (leaders.size() == 1);

        return new FrequencyResult<>(leaders, isUnique);
    }

    // =========================================================================
    // Stubs to fulfill the legacy interface signatures without operational state
    // =========================================================================

    @Override
    public boolean hasUniqueLeader() {
        throw new UnsupportedOperationException("Invalid operation: call analyzeFrequency() first to obtain a queryable FrequencyResult.");
    }

    @Override
    public T getLeader() {
        throw new UnsupportedOperationException("Invalid operation: call analyzeFrequency() first to obtain a queryable FrequencyResult.");
    }

    @Override
    public Set<T> getLeaders() {
        throw new UnsupportedOperationException("Invalid operation: call analyzeFrequency() first to obtain a queryable FrequencyResult.");
    }
}
