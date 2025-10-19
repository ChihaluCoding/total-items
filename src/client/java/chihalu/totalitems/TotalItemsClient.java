package chihalu.totalitems;

import net.fabricmc.api.ClientModInitializer;
import chihalu.totalitems.event.TooltipEventHandler;

public class TotalItemsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// ツールチップイベントハンドラーを登録
		TooltipEventHandler.register();
	}
}