package org.royaldev.royalcommands.spawninfo;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Store meta-data in an ItemStack as attributes.
 *
 * @author Kristian
 */
public class AttributeStorage {
    private final UUID uniqueKey;
    private ItemStack target;

    private AttributeStorage(ItemStack target, UUID uniqueKey) {
        this.target = Preconditions.checkNotNull(target, "target cannot be NULL");
        this.uniqueKey = Preconditions.checkNotNull(uniqueKey, "uniqueKey cannot be NULL");
    }

    /**
     * Construct a new attribute storage system.
     * <p/>
     * The key must be the same in order to retrieve the same data.
     *
     * @param target    - the item stack where the data will be stored.
     * @param uniqueKey - the unique key used to retrieve the correct data.
     */
    public static AttributeStorage newTarget(ItemStack target, UUID uniqueKey) {
        return new AttributeStorage(target, uniqueKey);
    }

    /**
     * Retrieve the data stored in the item's attribute.
     *
     * @return The stored data, or defaultValue if not found.
     */
    public String getData() {
        Attributes attr = new Attributes(target);
        Attributes.Attribute current = getAttribute(attr, uniqueKey);
        attr.removeTagIfEmpty();
        return current != null ? current.getName() : null;
    }

    /**
     * Set the data stored in the attributes.
     *
     * @param data - the data.
     */
    public void setData(String data) {
        Attributes attributes = new Attributes(target);
        Attributes.Attribute current = getAttribute(attributes, uniqueKey);

        if (current == null) {
            attributes.add(
                    Attributes.Attribute.newBuilder().
                            name(data).
                            amount(0).
                            uuid(uniqueKey).
                            operation(Attributes.Operation.ADD_NUMBER).
                            type(Attributes.AttributeType.GENERIC_ATTACK_DAMAGE).
                            build()
            );
        } else {
            current.setName(data);
        }
        this.target = attributes.getStack();
    }

    /**
     * Removes the data stored in the attributes.
     */
    public void removeData() {
        Attributes attributes = new Attributes(target);
        Attributes.Attribute current = getAttribute(attributes, uniqueKey);
        if (current == null) return;
        attributes.remove(current);
        attributes.removeTagIfEmpty();
        this.target = attributes.getStack();
    }

    /**
     * Retrieve the target stack. May have been changed.
     *
     * @return The target stack.
     */
    public ItemStack getTarget() {
        return target;
    }

    /**
     * Retrieve an attribute by UUID.
     *
     * @param attributes - the attribute.
     * @param id         - the UUID to search for.
     * @return The first attribute associated with this UUID, or NULL.
     */
    private Attributes.Attribute getAttribute(Attributes attributes, UUID id) {
        for (Attributes.Attribute attribute : attributes.values()) {
            if (Objects.equal(attribute.getUUID(), id)) {
                return attribute;
            }
        }
        return null;
    }
}
