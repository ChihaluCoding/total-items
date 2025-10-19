# Total Items

インベントリ内でアイテムにマウスをホバーした時に、ツールチップにインベントリ内にあるそのアイテムの合計数量を表示するModです。

## 機能

- **アイテムカウント表示**: カウント対象のアイテムをマウスホバーすると、インベントリ内の合計数が表示されます
- **カスタマイズ可能**: コマンドでどのアイテムをカウントするか自由に設定できます
- **簡単な操作**: スラッシュコマンドで直感的に管理できます

## コマンド

### MOD全体の有効/無効

```
/totalitems on      # MODを有効化
/totalitems off     # MODを無効化
```

### アイテムのカウント対象管理

#### 方法1: アイテムIDを直接指定

```
/totalitems enable <itemId>    # アイテムをカウント対象に追加
/totalitems disable <itemId>   # アイテムをカウント対象から削除
```

**例:**
```
/totalitems enable "minecraft:diamond"      # ダイヤモンドをカウント対象に追加
/totalitems enable "minecraft:emerald"      # エメラルドをカウント対象に追加
/totalitems disable "minecraft:diamond"     # ダイヤモンドをカウント対象から削除

※ アイテムIDは引用符で囲む必要があります
アイテムIDは高度な表示（F3 + H）をオンにすることで確認できます
```

**補完機能:** `enable`と`disable`コマンドではTABキーで補完が利用できます
- `enable`: ゲーム内の全アイテムから補完候補を提案
- `disable`: 現在追跡中のアイテムのみ補完候補を提案

#### 方法2: オフハンド（左手）のアイテムで登録/解除

```
/totalitems enable offhand    # オフハンド（左手）に持っているアイテムをカウント対象に追加
/totalitems disable offhand   # オフハンド（左手）に持っているアイテムをカウント対象から削除
```

**使い方:**
1. オフハンド（左手）にアイテムを持つ
2. `/totalitems enable offhand` コマンドを実行
3. 自動的にそのアイテムがカウント対象に追加されます

**例:**
```
1. オフハンド（左手）にダイヤモンドを持つ
2. /totalitems enable offhand を実行
3. minecraft:diamondがカウント対象に追加される
```

## デフォルトのカウント対象

初期状態ではカウント対象のアイテムはありません。コマンドで自由に追加してください。

## 使用例

```bash
# MODを有効化
/totalitems on

# ダイヤモンドとエメラルドをカウント対象に
/totalitems enable "minecraft:diamond"
/totalitems enable "minecraft:emerald"

# 金をオフハンド（左手）に持った状態でカウント対象に追加
/totalitems enable offhand

# ネザライトをカウント対象から削除
/totalitems disable "minecraft:netherite_ingot"
```

## カウント設定の確認と一括管理

```
/totalitems list         # 現在のカウント対象アイテム一覧を表示
/totalitems all enable   # 全アイテムをカウント対象に追加（有効化）
/totalitems all disable  # 全カウント対象アイテムを削除（無効化）
```

## トラブルシューティング

**アイテムカウントが表示されない場合：**
1. MODが有効化されているか確認してください（`/totalitems on`）
2. ホバーしているアイテムがカウント対象に含まれているか確認してください
3. 左手（オフハンド）のアイテムは表示対象外です







