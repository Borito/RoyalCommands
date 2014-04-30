package org.royaldev.royalcommands.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.StreamSerializer;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.itemrenamer.AbstractRenameProcessor;
import org.shininet.bukkit.itemrenamer.api.RenamerSnapshot;

import java.io.DataInputStream;
import java.io.IOException;

public class SpawnRenameProcessor extends AbstractRenameProcessor {

    public SpawnRenameProcessor() {
        super("org.royaldev.royalcommands.spawninfo.TempSpawn");
    }

    @Override
    protected void processSnapshot(Player player, RenamerSnapshot itemStacks) {
        // We don't use this at all.
    }

    protected void unprocessFieldStack(PacketEvent event) {
        DataInputStream input = event.getNetworkMarker().getInputStream();
        // Skip simulated packets
        if (input == null) return;
        try {
            // Read slot
            if (event.getPacketType() == PacketType.Play.Client.SET_CREATIVE_SLOT) input.skipBytes(2);
            else if (event.getPacketType() == PacketType.Play.Client.BLOCK_PLACE) input.skipBytes(10);
            ItemStack stack = readItemStack(input, new StreamSerializer());
            // Now we can properly unprocess it
            this.unprocess(stack);
            // And write it back
            event.getPacket().getItemModifier().write(0, stack);
        } catch (IOException e) {
            throw new RuntimeException("Cannot undo NBT scrubber.", e);
        }
    }

    /**
     * Read an ItemStack from a input stream without "scrubbing" the NBT content.
     *
     * @param input      The input stream.
     * @param serializer Methods for serializing Minecraft object.
     * @return The deserialized item stack.
     * @throws IOException If anything went wrong.
     */
    private ItemStack readItemStack(DataInputStream input, StreamSerializer serializer) throws IOException {
        ItemStack result = null;
        short type = input.readShort();
        if (type >= 0) {
            byte amount = input.readByte();
            short damage = input.readShort();
            result = new ItemStack(type, amount, damage);
            NbtCompound tag = serializer.deserializeCompound(input);
            if (tag != null) {
                result = MinecraftReflection.getBukkitItemStack(result);
                NbtFactory.setItemTag(result, tag);
            }
        }
        return result;
    }
}
