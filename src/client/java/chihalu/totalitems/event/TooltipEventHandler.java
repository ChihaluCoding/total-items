package chihalu.totalitems.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import chihalu.totalitems.util.InventoryUtils;

public class TooltipEventHandler {
    
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, tooltipType, lines) -> {
            // クライアントプレイヤーを取得
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                PlayerInventory inventory = client.player.getInventory();
                
                // 合計アイテム数を取得
                int totalCount = InventoryUtils.getTotalItemCount(inventory, stack);
                
                // インベントリ内のアイテムが1個以上の場合に表示
                if (totalCount >= 1) {
                    lines.add(Text.literal("インベントリ内の合計個数: ")
                        .formatted(Formatting.GRAY)
                        .append(Text.literal(totalCount + "個")
                        .formatted(Formatting.YELLOW)));
                }
            }
        });
    }
}
