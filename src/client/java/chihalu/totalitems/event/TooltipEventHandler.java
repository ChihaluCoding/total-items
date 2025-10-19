package chihalu.totalitems.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import chihalu.totalitems.util.InventoryUtils;
import chihalu.totalitems.config.TotalItemsServerConfig;

public class TooltipEventHandler {
    
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, tooltipType, lines) -> {
            // modが無効な場合は何もしない
            if (!TotalItemsServerConfig.isEnabled()) {
                return;
            }
            
            // クライアントプレイヤーを取得
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                PlayerInventory inventory = client.player.getInventory();
                
                // アイテムのIDを取得
                String itemId = getItemId(stack);
                
                // 追跡対象のアイテムかチェック
                if (!TotalItemsServerConfig.getTrackedItems().contains(itemId)) {
                    return;
                }
                
                // 合計アイテム数を取得
                int totalCount = InventoryUtils.getTotalItemCount(inventory, stack);
                
                // 1個以上ある場合に表示
                if (totalCount >= 1) {
                    lines.add(Text.literal("インベントリ内の合計個数: ")
                        .formatted(Formatting.GRAY)
                        .append(Text.literal(totalCount + "個")
                        .formatted(Formatting.YELLOW)));
                }
            }
        });
    }
    
    /**
     * ItemStackのIDを取得 (例: "minecraft:diamond")
     */
    private static String getItemId(ItemStack stack) {
        if (stack.isEmpty()) {
            return "";
        }
        return Registries.ITEM.getId(stack.getItem()).toString();
    }
}

