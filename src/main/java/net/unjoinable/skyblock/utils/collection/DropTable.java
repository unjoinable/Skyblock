package net.unjoinable.skyblock.utils.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A weighted random selection data structure that allows items to be selected
 * based on their assigned weights. Higher weights correspond to higher probability
 * of selection.
 *
 * <p>This implementation is thread-safe and uses a concurrent hash map for storage.
 * The selection algorithm has O(n) time complexity where n is the number of items.
 *
 * @param <T> the type of items stored in this drop table
 */
public class DropTable<T> {
    private final Random random = ThreadLocalRandom.current(); //NOSONAR
    private final Map<T, Double> weights = new ConcurrentHashMap<>();
    private volatile double totalWeight = 0.0;

    /**
     * Creates an empty drop table with no items.
     */
    public DropTable() {}

    /**
     * Creates a drop table with the specified initial items and weights.
     *
     * @param initialWeights a map of items to their weights
     * @throws IllegalArgumentException if any weight is negative or zero
     * @throws NullPointerException if initialWeights is null
     */
    public DropTable(Map<T, Double> initialWeights) {
        Objects.requireNonNull(initialWeights, "Initial weights cannot be null");
        initialWeights.forEach(this::add);
    }

    /**
     * Adds an item with the specified weight to the drop table.
     * If the item already exists, its weight will be updated.
     *
     * @param item the item to add
     * @param weight the weight of the item (must be positive)
     * @throws IllegalArgumentException if weight is negative or zero
     * @throws NullPointerException if item is null
     */
    public void add(T item, double weight) {
        Objects.requireNonNull(item, "Item cannot be null");
        validateWeight(weight);

        Double previousWeight = weights.put(item, weight);
        if (previousWeight != null) {
            this.totalWeight = totalWeight - previousWeight + weight;
        } else {
            this.totalWeight += weight;
        }
    }

    /**
     * Removes an item from the drop table.
     *
     * @param item the item to remove
     * @return true if the item was present and removed, false otherwise
     * @throws NullPointerException if item is null
     */
    public boolean remove(T item) {
        Objects.requireNonNull(item, "Item cannot be null");

        Double removedWeight = weights.remove(item);
        if (removedWeight != null) {
            this.totalWeight -= removedWeight;
            return true;
        }
        return false;
    }

    /**
     * Updates the weight of an existing item in the drop table.
     *
     * @param item the item whose weight to update
     * @param newWeight the new weight for the item (must be positive)
     * @return true if the item was found and updated, false otherwise
     * @throws IllegalArgumentException if newWeight is negative or zero
     * @throws NullPointerException if item is null
     */
    public boolean updateWeight(T item, double newWeight) {
        Objects.requireNonNull(item, "Item cannot be null");
        validateWeight(newWeight);

        return weights.computeIfPresent(item, (_, oldWeight) -> {
            totalWeight += newWeight - oldWeight;
            return newWeight;
        }) != null;
    }

    /**
     * Selects a random item from the drop table based on weights.
     * Items with higher weights have a proportionally higher chance of being selected.
     *
     * @return a randomly selected item, or null if the table is empty
     */
    public T select() {
        if (isEmpty()) {
            return null;
        }

        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;

        for (Map.Entry<T, Double> entry : weights.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue <= cumulativeWeight) {
                return entry.getKey();
            }
        }

        // Fallback - should rarely happen due to floating point precision
        return weights.keySet().iterator().next();
    }

    /**
     * Selects multiple items from the drop table with replacement.
     * Each selection is independent, so the same item can be selected multiple times.
     *
     * @param count the number of items to select
     * @return a list of selected items
     * @throws IllegalArgumentException if count is negative
     */
    public List<T> selectMultiple(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative: " + count);
        }

        return random.ints(count)
                .mapToObj(_ -> select())
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Calculates the probability of selecting the specified item.
     *
     * @param item the item to calculate probability for
     * @return the probability as a value between 0.0 and 1.0, or 0.0 if item not found
     * @throws NullPointerException if item is null
     */
    public double getProbability(T item) {
        Objects.requireNonNull(item, "Item cannot be null");

        Double weight = weights.get(item);
        return (weight != null && totalWeight > 0) ? weight / totalWeight : 0.0;
    }

    /**
     * Returns an immutable copy of the weights map.
     *
     * @return a new map containing all items and their weights
     */
    public Map<T, Double> getWeights() {
        return Map.copyOf(weights);
    }

    /**
     * Returns the total weight of all items in the drop table.
     *
     * @return the sum of all item weights
     */
    public double getTotalWeight() {
        return totalWeight;
    }

    /**
     * Returns the number of items in the drop table.
     *
     * @return the number of items
     */
    public int size() {
        return weights.size();
    }

    /**
     * Checks if the drop table is empty.
     *
     * @return true if the table contains no items, false otherwise
     */
    public boolean isEmpty() {
        return weights.isEmpty();
    }

    /**
     * Checks if the drop table contains the specified item.
     *
     * @param item the item to check for
     * @return true if the item is present, false otherwise
     * @throws NullPointerException if item is null
     */
    public boolean contains(T item) {
        Objects.requireNonNull(item, "Item cannot be null");
        return weights.containsKey(item);
    }

    /**
     * Returns a set of all items in the drop table.
     *
     * @return a new set containing all items
     */
    public Set<T> getItems() {
        return Set.copyOf(weights.keySet());
    }

    /**
     * Removes all items from the drop table.
     */
    public void clear() {
        weights.clear();
        totalWeight = 0.0;
    }

    /**
     * Returns a string representation of the drop table showing items and their probabilities.
     *
     * @return a formatted string representation
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "DropTable[empty]";
        }

        StringBuilder sb = new StringBuilder("DropTable[");
        weights.entrySet().stream()
                .sorted(Map.Entry.<T, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    double probability = getProbability(entry.getKey()) * 100;
                    sb.append(String.format("%s: %.2f%%, ", entry.getKey(), probability));
                });

        // Remove trailing comma and space
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * Validates that a weight value is positive and finite.
     *
     * @param weight the weight to validate
     * @throws IllegalArgumentException if weight is not positive or not finite
     */
    private void validateWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive: " + weight);
        }
        if (!Double.isFinite(weight)) {
            throw new IllegalArgumentException("Weight must be finite: " + weight);
        }
    }
}
