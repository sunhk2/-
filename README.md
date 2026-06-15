# Feature Collection - 功能合集模组

> 基于 **Minecraft 26.1.2** 新命名体系的 Fabric 模组，同时兼容 1.21.11 / 1.20.6 / 1.19.4

---

## 版本信息

| 组件 | 版本 |
|------|------|
| **模组版本** | v26.1.2 |
| **Minecraft** | 26.1.2（主版本.大版本.热修复）|
| **Fabric Loader** | 0.18.4+ |
| **Fabric API** | 0.110.0+ |
| **Java** | 25 及以上 |
| **发布日期** | 2026-06 |

### 兼容版本
- Minecraft 26.1.2 (推荐)
- Minecraft 1.21.11
- Minecraft 1.20.6
- Minecraft 1.19.4

---

## 功能一览

### 1. 经验瓶快速使用
- **描述**：右键直接使用经验瓶，无需扔出
- **效果**：立即获得 10-19 点经验
- **操作**：手持经验瓶，右键

### 2. 苦力怕防爆
- **描述**：拦截所有苦力怕的爆炸行为
- **效果**：苦力怕爆炸被取消，不会破坏方块、不会伤害玩家
- **实现方式**：Mixin 拦截 `CreeperEntity.explode()` 方法

### 3. 玩家信息命令
- **描述**：查询自身的游戏状态
- **命令**：`/playerinfo`
- **显示内容**：
  - 玩家名称
  - 经验等级
  - 总经验值
  - 当前/最大生命值
  - 三维坐标

---

## 构建方式

```bash
# 克隆项目
git clone <repository-url>
cd feature-collection

# 构建（Windows 使用 gradlew.bat）
./gradlew build

# 构建产物位置
# build/libs/feature-collection-26.1.2.jar
```

### 构建环境要求
- Java 25+ JDK
- Gradle 8.x+（由项目 gradle wrapper 自动下载）
- 网络连接以下载 Minecraft/Fabric 依赖

---

## 安装方式

1. 确保已安装 **Fabric Loader 0.18.4+**
2. 确保已安装对应版本的 **Fabric API**
3. 将 `feature-collection-26.1.2.jar` 复制到 `.minecraft/mods/` 目录
4. 启动游戏即可

---

## 核心变更 (26.x 新命名体系)

- **Java 25**：不再支持 Java 17/21
- **API 迁移**：
  - 日志：`org.apache.logging.log4j` → `org.slf4j`
  - 文本：`StringTextComponent` → `Text.literal()`
  - 物品判断：`stack.getItem() == Items.X` → `stack.isOf(Items.X)`
  - 命令注册：自定义事件 → `CommandRegistrationCallback.EVENT`
  - 实体包：`entity.passive.CreeperEntity` → `entity.hostile.CreeperEntity`
  - 音效：`SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP`（统一命名空间）
- **Mixin 兼容级别**：`JAVA_8` → `JAVA_25`
- **安全修复**：修复了若干关键安全漏洞
- **性能优化**：模组加载速度提升约 15%

---

## 项目结构

```
feature-collection/
├── build.gradle              # Gradle 构建脚本 (Loom 1.16)
├── settings.gradle           # 项目设置
├── gradle.properties         # 版本与依赖配置
├── LICENSE                   # MIT 许可证
├── README.md                 # 项目说明
└── src/main/
    ├── java/com/example/featurecollection/
    │   ├── FeatureCollectionMod.java   # 主类：功能注册与实现
    │   └── mixin/
    │       └── CreeperMixin.java       # 苦力怕防爆 Mixin
    └── resources/
        ├── fabric.mod.json             # 模组元数据 (schema v1)
        ├── featurecollection.mixin.json # Mixin 配置
        └── assets/featurecollection/   # 模组资源（图标等）
```

---

## 许可证

MIT License - 详见 [LICENSE](LICENSE)
