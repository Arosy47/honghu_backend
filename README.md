# 红狐校园社区 (Honghu Campus Community) - Backend

> 🚀 **这是一个功能完善的全栈校园社区平台项目。**
> 本仓库为 **后端 API 服务** 源码。

### 🔗 关联仓库 (Related Repositories)
**本项目为后端 API 源码，前端小程序源码请移步：**
👉 **[honghu_mini_program (微信小程序端)](https://github.com/Arosy47/honghu_mini_program)**

*(⚠️ 注意：请将链接中的 `YourUsername` 替换为您实际的 GitHub 用户名，否则点击会报 404)*

*(🚀 建议同时 Star 两个仓库，以获取完整的前后端开发体验)*

---

## 📖 项目简介
本项目致力于打造一个安全、活跃、便捷的校园生活圈。集成了**论坛社区、二手交易、即时通讯(IM)、校园跑腿、恋爱交友**等核心功能。采用主流的 Java Spring Boot 技术栈开发，结构清晰，适合作为**毕业设计、课程设计或创业项目的基石**。

## 小程序二维码
<img width="239" height="237" alt="image" src="https://github.com/user-attachments/assets/95d00eeb-6da1-4a7e-9bd9-9101eac70fe7" />

## 效果演示
<img width="3689" height="1648" alt="图片2" src="https://github.com/user-attachments/assets/76e11c7b-2df5-43a0-b6da-883bd5f51328" />
<img width="3946" height="1770" alt="图片3" src="https://github.com/user-attachments/assets/49a493cc-62fc-4106-bd3c-482af0e3d5d9" />
<img width="3965" height="1780" alt="图片4" src="https://github.com/user-attachments/assets/3a0c66ca-757f-4a12-9ccf-96aa32443319" />
<img width="3971" height="1780" alt="图片5" src="https://github.com/user-attachments/assets/7590422b-de01-4e2f-88d1-0a8c19446216" />
<img width="3817" height="1730" alt="图片6" src="https://github.com/user-attachments/assets/7ef396ad-d61b-4e11-8913-4584b2f6dc1a" />
<img width="3681" height="1627" alt="图片7" src="https://github.com/user-attachments/assets/20d17f58-fec2-429d-aa46-ea6512857ffd" />
<img width="3769" height="1692" alt="图片8" src="https://github.com/user-attachments/assets/e6658e21-8715-412d-b8fe-0586d0248110" />
<img width="4197" height="1888" alt="图片10" src="https://github.com/user-attachments/assets/b7c78745-d58d-4dde-888b-80e66d081be3" />


## 🚀 功能与版本对比 (Community vs Pro)

| 核心模块 | 功能细分 | 🆓 开源版 | 👑 商业版 (Pro) | 💡 价值/应用场景 |
| :--- | :--- | :---: | :---: | :--- |
| **多校运营** | **多校切换/全国模式** | ❌ (单校) | ✅ | **核心架构**：支持全国多所高校数据隔离与切换，SaaS 平台必备 |
| | 学校入驻/管理 | ❌ | ✅ | 运营者后台一键开通新学校 |
| **高并发架构** | **消息队列削峰 (MQ)** | ❌ | ✅ | **技术壁垒**：集成 RabbitMQ/RocketMQ，抗住 10W+ 并发冲击 |
| | 分布式锁 (Redisson) | ❌ | ✅ | 防止秒杀、抢单场景下的数据超卖 |
| **深度社交** | **匿名爆料贴** | ❌ | ✅ | **流量之王**：校园里的秘密树洞，日活提升 300% |
| | **滑动匹配 (Slide)** | ❌ | ✅ | **荷尔蒙经济**：左滑无感右滑喜欢，最强吸粉功能 |
| | **灵魂匹配 (IM私信)** | ❌ | ✅ | 实时聊天、表情包、图片发送，留存关键 |
| | 扩列/CP墙 | ❌ | ✅ | 陌生人社交刚需 |
| **消息触达** | **IM 即时推送** | ❌ | ✅ | **即时互动**：WebSocket 长连接，消息毫秒级直达 |
| | **微信消息通知** | ❌ | ✅ | **超强召回**：离线也能收到回复/点赞通知，留存神器 |
| **内容生态** | 图文/视频帖子 | ✅ | ✅ | 基础 UGC 能力 |
| | **投票贴/抽奖贴** | ❌ | ✅ | 运营促活利器，裂变拉新必备 |
| | **帖子置顶/加精** | ❌ | ✅ | 运营强控流量，广告位变现基础 |
| | **海报分享** | ❌ | ✅ | **裂变传播**：一键生成精美海报分享至朋友圈 |
| | **发送位置** | ❌ | ✅ | **LBS互动**：发布动态携带地理位置，发现身边趣事 |
| | **敏感词智能拦截** | ❌ | ✅ | **保命符**：自动过滤涉黄涉政内容，低成本合规 |
| | 全站搜索 (ES) | ❌ | ✅ | 海量数据毫秒级检索 |
| **用户体系** | 微信一键登录 | ✅ | ✅ | 极速注册体验 |
| | **学生身份认证** | ❌ | ✅ | **圈层纯净度**：学信网/教务处/邮箱认证，打造真实校园圈 |
| | 个人资料/隐私设置 | ✅ | ✅ | 基础信息编辑 |
| **变现能力** | **流量主广告** | ❌ | ✅ | 接入微信原生广告（Banner/视频），躺着赚钱 |
| | **首屏/轮播广告** | ❌ | ✅ | 品牌方合作，直接变现 |
| | **二手闲置市场** | ❌ | ✅ | 校园闲鱼，交易撮合 |
| | **跑腿/任务大厅** | ❌ | ✅ | **支付系统**：外卖、代取快递，高频刚需 |
| **校园服务** | 课表查询 | ✅ | ✅ | 刚需工具引流 |
| | 校历/空教室 | ✅ | ✅ | 实用工具 |

> 💡 **开源版** 包含了运行一个基础校园社区所需的所有核心功能（发帖、评论、用户系统）。
> 
> 🚀 **商业版** 提供了运营变现、高并发支撑、风控安全及更多好玩的社交功能。
> 
> **如需商业版或定制开发，请联系作者 (微信: xxxx) 获取授权。**

## 🛠️ 技术栈
*   **核心框架**: Spring Boot 2.1.5
*   **ORM 框架**: MyBatis + MyBatis Plus
*   **鉴权安全**: Apache Shiro + JWT
*   **数据库**: MySQL 8.0
*   **缓存与锁**: Redis + Redisson (分布式锁)
*   **即时通讯**: WebSocket (Netty)
*   **搜索引擎**: Elasticsearch 6.5.3 (用于全文检索)
*   **对象存储**: Tencent Cloud COS
*   **工具库**: Lombok, MapStruct, Gson, Google Guava

## 🚀 快速开始

### 1. 环境准备
*   JDK 1.8+
*   MySQL 8.0+
*   Redis 5.0+
*   Elasticsearch 6.5.3 (可选，如不开启搜索功能可跳过)

### 2. 配置文件
由于安全原因，敏感配置文件已在 `.gitignore` 中忽略。请在 `src/main/resources` 下参考 `application.properties` 创建 `application-dev.properties`，并配置以下信息：
*   数据库连接 (url, username, password)
*   Redis 连接 (host, port, password)
*   微信小程序配置 (appId, secret)
*   腾讯云 COS 配置 (secretId, secretKey)

### 3. 运行项目
```bash
# 克隆项目
git clone https://github.com/Arosy47/honghu_backend.git

# 编译打包
mvn clean package -Dmaven.test.skip=true

# 运行
java -jar target/xiaomeng-1.0-SNAPSHOT.jar
```

## 沟通交流
### 电子邮箱：309095898@qq.com
### 群二维码
<img width="237" height="237" alt="图片10" src="https://github.com/user-attachments/assets/3f6686a4-c3dc-4df9-bf1b-72db48c3dbaa" />


## 🤝 贡献与支持
如果你觉得这个项目对你有帮助，请给一个 ⭐️ **Star**！
欢迎提交 Issue 或 PR 参与贡献。

## 📜 开源须知 & 版权声明 (License & Disclaimer)

**使用本项目前，请务必仔细阅读以下条款。**
**一旦您下载、克隆或使用本项目源码，即表示您完全同意并遵守以下规定：**

1.  **🎓 仅限个人学习与研究**
    *   本项目开源的主要目的是为了促进技术交流与学习。
    *   仅允许用于个人毕业设计、课程设计、技术研究等非营利性用途。

2.  **🚫 严禁商业用途**
    *   **禁止**将本项目（包括但不限于代码、设计、文档）用于任何形式的商业盈利活动。
    *   **禁止**将本项目部署为商业运营平台。

3.  **❌ 禁止倒卖与私自改造出售**
    *   **严禁**任何形式的源码倒卖行为。
    *   **严禁**对源码进行二次打包、改造后进行出售或通过会员制盈利。
    *   任何打着“红狐”、“Campus”等名义进行的非法牟利行为均与本项目作者无关，作者保留追究其法律责任的权利。

---
*Copyright © 2024-2026 Honghu Campus Community. All Rights Reserved.*

