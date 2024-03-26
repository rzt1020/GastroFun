package cn.myrealm.gastrofun.listeners;

import cn.myrealm.gastrofun.GastroFun;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author rzt1020
 */
public abstract class BaseListener extends PacketAdapter implements Listener {

    protected final JavaPlugin plugin;

    public BaseListener(JavaPlugin plugin, PacketType... types) {
        super(plugin, ListenerPriority.NORMAL, types);
        this.plugin = plugin;
    }

    public BaseListener(JavaPlugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.RECIPES);
        this.plugin = plugin;
    }

    public void registerBukkitListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void registerProtocolListener() {
        GastroFun.protocolManager.addPacketListener(this);
    }

    public void unRegisterProtocolListener() {
        GastroFun.protocolManager.removePacketListener(this);
    }

    protected void consumeItem(Player player, ItemStack itemStack) {
        ItemStack inHand = player.getInventory().getItemInMainHand();

        if (itemStack.isSimilar(inHand)) {
            inHand.setAmount(inHand.getAmount() - 1);
            player.getInventory().setItemInMainHand(inHand.getAmount() > 0 ? inHand : null);
        } else {
            int slot = player.getInventory().first(itemStack);

            if (slot != -1) {
                ItemStack itemInSlot = player.getInventory().getItem(slot);
                if (itemInSlot != null) {
                    itemInSlot.setAmount(itemInSlot.getAmount() - 1);
                    player.getInventory().setItem(slot, itemInSlot.getAmount() > 0 ? itemInSlot : null);
                }
            }
        }
    }

}

