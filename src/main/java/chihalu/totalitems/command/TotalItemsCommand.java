package chihalu.totalitems.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;
import chihalu.totalitems.config.TotalItemsServerConfig;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class TotalItemsCommand {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerTotalItemsCommand(dispatcher);
        });
    }
    
    private static void registerTotalItemsCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("totalitems")
                .then(
                    CommandManager.literal("on")
                        .executes(ctx -> executeOn(ctx))
                )
                .then(
                    CommandManager.literal("off")
                        .executes(ctx -> executeOff(ctx))
                )
                .then(
                    CommandManager.literal("enable")
                        .then(
                            CommandManager.argument("itemId", StringArgumentType.string())
                                .suggests((context, builder) -> suggestItemIds(context, builder))
                                .executes(ctx -> executeEnable(ctx, StringArgumentType.getString(ctx, "itemId")))
                        )
                        .then(
                            CommandManager.literal("offhand")
                                .executes(ctx -> executeEnableOffhand(ctx))
                        )
                )
                .then(
                    CommandManager.literal("disable")
                        .then(
                            CommandManager.argument("itemId", StringArgumentType.string())
                                .suggests((context, builder) -> suggestTrackedItems(context, builder))
                                .executes(ctx -> executeDisable(ctx, StringArgumentType.getString(ctx, "itemId")))
                        )
                        .then(
                            CommandManager.literal("offhand")
                                .executes(ctx -> executeDisableOffhand(ctx))
                        )
                )
                .then(
                    CommandManager.literal("list")
                        .executes(ctx -> executeList(ctx))
                )
                .then(
                    CommandManager.literal("all")
                        .then(
                            CommandManager.literal("enable")
                                .executes(ctx -> executeAllEnable(ctx))
                        )
                        .then(
                            CommandManager.literal("disable")
                                .executes(ctx -> executeAllDisable(ctx))
                        )
                )
        );
    }
    
    private static int executeOn(CommandContext<ServerCommandSource> ctx) {
        TotalItemsServerConfig.setEnabled(true);
        ctx.getSource().sendFeedback(
            () -> Text.literal("§a[Total Items] Mod が有効になりました"),
            false
        );
        return 1;
    }
    
    private static int executeOff(CommandContext<ServerCommandSource> ctx) {
        TotalItemsServerConfig.setEnabled(false);
        ctx.getSource().sendFeedback(
            () -> Text.literal("§c[Total Items] Mod が無効になりました"),
            false
        );
        return 1;
    }
    
    private static int executeEnable(CommandContext<ServerCommandSource> ctx, String itemIdParam) {
        final String itemId = itemIdParam.trim();
        
        if (!itemId.matches("[a-z0-9_:-]+")) {
            ctx.getSource().sendError(
                Text.literal("§c[Total Items] 無効なアイテムIDです: " + itemId)
            );
            return 0;
        }
        
        java.util.List<String> trackedItems = new ArrayList<>(TotalItemsServerConfig.getTrackedItems());
        if (!trackedItems.contains(itemId)) {
            trackedItems.add(itemId);
            TotalItemsServerConfig.setTrackedItems(trackedItems);
            ctx.getSource().sendFeedback(
                () -> Text.literal("§a[Total Items] " + itemId + " をカウント対象に追加しました"),
                false
            );
        } else {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§e[Total Items] " + itemId + " は既にカウント対象です"),
                false
            );
        }
        return 1;
    }
    
    private static int executeDisable(CommandContext<ServerCommandSource> ctx, String itemIdParam) {
        final String itemId = itemIdParam.trim();
        
        java.util.List<String> trackedItems = new ArrayList<>(TotalItemsServerConfig.getTrackedItems());
        if (trackedItems.remove(itemId)) {
            TotalItemsServerConfig.setTrackedItems(trackedItems);
            ctx.getSource().sendFeedback(
                () -> Text.literal("§a[Total Items] " + itemId + " をカウント対象から削除しました"),
                false
            );
        } else {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§e[Total Items] " + itemId + " はカウント対象に含まれていません"),
                false
            );
        }
        return 1;
    }
    
    /**
     * オフハンド（左手）に持っているアイテムをカウント対象に追加
     */
    private static int executeEnableOffhand(CommandContext<ServerCommandSource> ctx) {
        net.minecraft.server.network.ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) {
            ctx.getSource().sendError(Text.literal("§c[Total Items] このコマンドはプレイヤーのみが実行できます"));
            return 0;
        }
        
        net.minecraft.item.ItemStack leftHandStack = player.getOffHandStack();
        if (leftHandStack.isEmpty()) {
            ctx.getSource().sendError(Text.literal("§c[Total Items] 左手に何も持っていません"));
            return 0;
        }
        
        String itemId = net.minecraft.registry.Registries.ITEM.getId(leftHandStack.getItem()).toString();
        
        java.util.List<String> trackedItems = new ArrayList<>(TotalItemsServerConfig.getTrackedItems());
        if (!trackedItems.contains(itemId)) {
            trackedItems.add(itemId);
            TotalItemsServerConfig.setTrackedItems(trackedItems);
            ctx.getSource().sendFeedback(
                () -> Text.literal("§a[Total Items] " + itemId + " をカウント対象に追加しました"),
                false
            );
        } else {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§e[Total Items] " + itemId + " は既にカウント対象です"),
                false
            );
        }
        return 1;
    }
    
    /**
     * オフハンド（左手）に持っているアイテムをカウント対象から削除
     */
    private static int executeDisableOffhand(CommandContext<ServerCommandSource> ctx) {
        net.minecraft.server.network.ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) {
            ctx.getSource().sendError(Text.literal("§c[Total Items] このコマンドはプレイヤーのみが実行できます"));
            return 0;
        }
        
        net.minecraft.item.ItemStack leftHandStack = player.getOffHandStack();
        if (leftHandStack.isEmpty()) {
            ctx.getSource().sendError(Text.literal("§c[Total Items] 左手に何も持っていません"));
            return 0;
        }
        
        String itemId = net.minecraft.registry.Registries.ITEM.getId(leftHandStack.getItem()).toString();
        
        java.util.List<String> trackedItems = new ArrayList<>(TotalItemsServerConfig.getTrackedItems());
        if (trackedItems.remove(itemId)) {
            TotalItemsServerConfig.setTrackedItems(trackedItems);
            ctx.getSource().sendFeedback(
                () -> Text.literal("§a[Total Items] " + itemId + " をカウント対象から削除しました"),
                false
            );
        } else {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§e[Total Items] " + itemId + " はカウント対象に含まれていません"),
                false
            );
        }
        return 1;
    }
    
    /**
     * 全アイテムIDの補完を提供
     */
    private static CompletableFuture<Suggestions> suggestItemIds(
            CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        
        Registries.ITEM.getIds().forEach(id -> {
            String itemId = id.toString();
            if (itemId.toLowerCase().contains(input)) {
                // コロンを含むアイテムIDは引用符で囲む必要がある
                builder.suggest("\"" + itemId + "\"");
            }
        });
        
        return builder.buildFuture();
    }
    
    /**
     * 現在カウント中のアイテムIDの補完を提供
     */
    private static CompletableFuture<Suggestions> suggestTrackedItems(
            CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        
        TotalItemsServerConfig.getTrackedItems().forEach(itemId -> {
            if (itemId.toLowerCase().contains(input)) {
                // コロンを含むアイテムIDは引用符で囲む必要がある
                builder.suggest("\"" + itemId + "\"");
            }
        });
        
        return builder.buildFuture();
    }
    
    /**
     * 現在カウント中のアイテムを一覧表示
     */
    private static int executeList(CommandContext<ServerCommandSource> ctx) {
        java.util.List<String> trackedItems = TotalItemsServerConfig.getTrackedItems();
        
        if (trackedItems.isEmpty()) {
            ctx.getSource().sendFeedback(
                () -> Text.literal("§e[Total Items] 現在、カウント対象のアイテムはありません"),
                false
            );
            return 0;
        }
        
        ctx.getSource().sendFeedback(
            () -> Text.literal("§a[Total Items] カウント対象のアイテム一覧（" + trackedItems.size() + "個）:"),
            false
        );
        
        trackedItems.forEach(itemId -> {
            ctx.getSource().sendFeedback(
                () -> Text.literal("  §6- " + itemId),
                false
            );
        });
        
        return 1;
    }
    
    /**
     * 全アイテムをカウント対象に追加（有効化）
     */
    private static int executeAllEnable(CommandContext<ServerCommandSource> ctx) {
        java.util.List<String> allItems = new ArrayList<>();
        Registries.ITEM.getIds().forEach(id -> {
            allItems.add(id.toString());
        });
        
        TotalItemsServerConfig.setTrackedItems(allItems);
        
        ctx.getSource().sendFeedback(
            () -> Text.literal("§a[Total Items] 全アイテム（" + allItems.size() + "個）をカウント対象に設定しました"),
            false
        );
        
        return 1;
    }
    
    /**
     * 全カウント対象アイテムを削除（無効化）
     */
    private static int executeAllDisable(CommandContext<ServerCommandSource> ctx) {
        int previousCount = TotalItemsServerConfig.getTrackedItems().size();
        
        TotalItemsServerConfig.setTrackedItems(new ArrayList<>());
        
        ctx.getSource().sendFeedback(
            () -> Text.literal("§a[Total Items] " + previousCount + "個のカウント対象アイテムをすべて削除しました"),
            false
        );
        
        return 1;
    }
}
