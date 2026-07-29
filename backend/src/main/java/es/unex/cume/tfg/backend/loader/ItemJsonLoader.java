package es.unex.cume.tfg.backend.loader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Loads completed item identifiers from the bundled item JSON file.
 */
@Component
public class ItemJsonLoader {

    private final ObjectMapper objectMapper;

    /**
     * Creates the item JSON loader.
     *
     * @param objectMapper JSON mapper
     */
    public ItemJsonLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Reads completed Summoner's Rift item identifiers.
     *
     * @return completed item identifiers
     */
    public Set<Integer> loadCompletedItemIds() {
        // Opens the item catalog resource
        try (InputStream inputStream = new ClassPathResource("data/item.json").getInputStream()) {

            // Maps the JSON content into its internal structure
            ItemJsonFile itemJsonFile = objectMapper.readValue(inputStream, ItemJsonFile.class);

            if (itemJsonFile == null || itemJsonFile.data() == null || itemJsonFile.data().isEmpty()) {
                throw new IllegalStateException("item.json does not contain item data");
            }

            Set<Integer> completedItemIds = new HashSet<>();

            // Keeps items considered complete and valid for the main map
            for (Map.Entry<String, ItemJsonData> entry : itemJsonFile.data().entrySet()) {

                ItemJsonData item = entry.getValue();

                if (isCompletedItem(item)) {
                    completedItemIds.add(Integer.valueOf(entry.getKey()));
                }
            }

            return completedItemIds;

        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load items from item.json", exception);
        }
    }

    /**
     * Checks whether an item is a completed Summoner's Rift item.
     *
     * @param item item data
     * @return true when the item is completed
     */
    private boolean isCompletedItem(ItemJsonData item) {
        if (item == null || item.gold() == null) {
            return false;
        }

        boolean availableOnSummonersRift = item.maps() != null && Boolean.TRUE.equals(item.maps().get("11"));

        boolean purchasable = Boolean.TRUE.equals(item.gold().purchasable());

        boolean hasRecipe = item.from() != null && !item.from().isEmpty();

        boolean hasUpgrade = item.into() != null && !item.into().isEmpty();

        boolean isBoot = item.tags() != null && item.tags().contains("Boots");

        return availableOnSummonersRift && purchasable && hasRecipe && (!hasUpgrade || isBoot);
    }

    /**
     * Root structure of the item JSON file.
     */
    private record ItemJsonFile(
            Map<String, ItemJsonData> data
    ) {
    }

    /**
     * Item structure required for completed item detection.
     */
    private record ItemJsonData(
            List<String> from,
            List<String> into,
            List<String> tags,
            Map<String, Boolean> maps,
            ItemGold gold
    ) {
    }

    /**
     * Item purchase information.
     */
    private record ItemGold(
            Boolean purchasable
    ) {
    }
}