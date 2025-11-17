# ZSTD 压缩算法集成说明

## ✅ 已完成的工作

ZSTD 压缩算法已经集成到 LogFileInfo 模块中，代码修改已完成：

1. ✅ **CMakeLists.txt** - 添加了 ZSTD 库编译配置和体积优化选项
2. ✅ **SecurityProvider.h** - 添加了 ZSTD 常量定义和实现类
3. ✅ **SecurityFactory.h** - 添加了 ZSTD 工厂方法
4. ✅ **CompressionMode.kt** - 启用了 ZSTD enum

## 📥 需要手动完成的工作

### 1. 下载 ZSTD 库源码

✅ **已完成：** ZSTD v1.5.5 已下载到以下目录：

```
LogFileSDK/LogFileInfo/src/main/cpp/third_party/zstd/
```

**下载命令（已完成）：**

```bash
cd LogFileSDK/LogFileInfo/src/main/cpp/third_party/
git clone --branch v1.5.5 --depth 1 https://github.com/facebook/zstd.git
```

**版本：** v1.5.5（稳定且性能好）

### 2. 验证目录结构

下载后，确保目录结构如下（已下载 v1.5.5 版本）：

```
third_party/zstd/
├── lib/
│   ├── common/
│   │   ├── zstd_common.c
│   │   ├── error_private.c
│   │   ├── fse_decompress.c
│   │   └── xxhash.c
│   ├── compress/
│   │   ├── zstd_compress.c
│   │   ├── zstd_compress_literals.c
│   │   ├── zstd_compress_sequences.c
│   │   ├── zstd_compress_superblock.c
│   │   ├── zstd_fast.c
│   │   ├── zstd_double_fast.c
│   │   ├── zstd_lazy.c
│   │   ├── zstd_opt.c
│   │   ├── fse_compress.c
│   │   ├── huf_compress.c
│   │   └── hist.c
│   ├── decompress/
│   │   ├── zstd_decompress.c
│   │   ├── zstd_decompress_block.c
│   │   └── huf_decompress.c
│   └── zstd.h          # 主头文件
```

**注意：** v1.5.5 版本中，`fse_compress.c` 和 `huf_compress.c` 位于 `compress/` 目录，而不是 `common/` 目录。

## 🔧 使用方法

### Kotlin 代码中使用

```kotlin
val config = LogConfig.builder()
    .setCompressionMode(CompressionMode.ZSTD)  // 使用 ZSTD 压缩
    .createNormalType()
CsLogFile.setConfig(config)
```

### 压缩级别

ZSTD 固定使用压缩级别 **3**（平衡速度和压缩率），无需配置。

## 📊 体积优化

已实施的优化措施：

1. **只编译必要的源文件** - 只包含单次压缩/解压功能，不包含流式、字典等高级功能
2. **编译优化选项** - 使用 `-Os` 优化代码大小
3. **链接优化** - 使用 `--gc-sections` 移除未使用的代码段
4. **符号剥离** - 移除所有符号表和调试信息

**预估体积增加：** 60-90KB（arm64-v8a）

## ✅ 验证步骤

1. 下载 ZSTD 库后，清理并重新编译项目
2. 检查编译是否成功
3. 测试 ZSTD 压缩/解压功能
4. 检查生成的 so 文件大小

## 📝 注意事项

1. **向后兼容**：ZSTD 的集成不影响现有的 LZ4 功能，旧文件仍可正常解压
2. **文件格式**：ZSTD 压缩的文件使用 `algorithm = 2` 标识
3. **解压缩**：`LogFileDecompressor` 已支持自动识别并解压 ZSTD 格式

## 🐛 常见问题

### Q: 编译时找不到 zstd.h？
A: 确保 ZSTD 库已正确下载到 `third_party/zstd/` 目录

### Q: 链接错误？
A: 检查 CMakeLists.txt 中的源文件路径是否正确

### Q: so 文件太大？
A: 确保使用 Release 模式编译，优化选项才会生效

