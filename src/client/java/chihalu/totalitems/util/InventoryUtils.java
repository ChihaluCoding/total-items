package chihalu.totalitems.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    /**
     * インベントリ内で指定されたアイテムの合計数を取得します
     */
    public static int getTotalItemCount(PlayerInventory inventory, ItemStack targetItem) {
        if (targetItem.isEmpty()) {
            return 0;
        }

        int total = 0;

        // すべてのインベントリスロットをカウント
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (canStackWith(stack, targetItem)) {
                total += stack.getCount();
            }
        }

        return total;
    }

    /**
     * 2つのアイテムスタックが同じアイテムかどうかを判定します
     */
    private static boolean canStackWith(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }

        // アイテムの種類が同じか確認
        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        }

        // ダメージ値（耐久値）が同じか確認
        if (stack1.getDamage() != stack2.getDamage()) {
            return false;
        }

        // コンポーネントが同じか確認
        return stack1.getComponents().equals(stack2.getComponents());
    }
}
