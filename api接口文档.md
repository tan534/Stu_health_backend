# 学生健康系统 API 接口文档

---

## 通用说明

### 请求前缀
```
http://{host}:{port}
```

### 通用响应格式
所有接口返回统一格式 `BaseResponse<T>`：

```json
{
  "code": 0,          // 状态码，0 表示成功，其他表示错误
  "data": {},         // 响应数据，类型视具体接口而定
  "message": ""       // 错误信息，成功时为空
}
```

### 错误码说明
| code | 含义 |
|------|------|
| 0 | 成功 |
| 40000 | 请求参数错误 |
| 40100 | 未登录 |
| 40101 | 无权限 |
| 40300 | 禁止访问 |
| 40400 | 数据不存在 |
| 50000 | 系统内部异常 |
| 50001 | 操作失败 |

### 认证说明
- 登录成功后，服务端通过 Session 维护登录状态
- 标注 `[需登录]` 的接口需要先登录
- 标注 `[管理员]` 的接口需要管理员角色（role = 1）

---

## 一、用户模块 `/user`

### 1.1 用户注册
```
POST /user/register
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名，长度 >= 4 |
| password | String | 是 | 密码，长度 >= 8 |
| checkPassword | String | 是 | 确认密码，需与 password 一致 |

**请求示例**
```json
{
  "username": "zhangsan",
  "password": "12345678",
  "checkPassword": "12345678"
}
```

**响应 data**: `Integer` — 新用户 ID

**响应示例**
```json
{
  "code": 0,
  "data": 3,
  "message": ""
}
```

---

### 1.2 用户登录
```
POST /user/login
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**响应 data**: `UserVO`

**响应示例**
```json
{
  "code": 0,
  "data": {
    "userId": 1,
    "username": "zhangsan",
    "phone": "13800138000",
    "gender": 1,
    "age": 20,
    "height": 175.0,
    "weight": 70.0,
    "fitGoal": "增肌",
    "targetWeight": 75.0,
    "role": 0,
    "status": 1,
    "createTime": "2026-05-01 12:00:00",
    "updateTime": "2026-05-07 10:30:00"
  },
  "message": ""
}
```

---

### 1.3 用户注销 `[需登录]`
```
POST /user/logout
```

**请求参数**: 无（依赖 Session）

**响应 data**: `Boolean` — true 表示成功

---

### 1.4 获取当前登录用户 `[需登录]`
```
GET /user/get/login
```

**请求参数**: 无（依赖 Session）

**响应 data**: `UserVO`（结构同 1.2）

---

### 1.5 创建用户 `[管理员]`
```
POST /user/add
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 否 | 密码，不填则默认 12345678 |
| phone | String | 否 | 手机号 |
| gender | Integer | 否 | 性别：0-未知, 1-男, 2-女 |
| age | Integer | 否 | 年龄 |
| height | Double | 否 | 身高（cm） |
| weight | Double | 否 | 当前体重（kg） |
| fitGoal | String | 否 | 健身目标 |
| targetWeight | Double | 否 | 目标体重（kg） |
| role | Integer | 否 | 角色：0-用户, 1-管理员 |
| status | Integer | 否 | 状态：1-正常, 2-禁用 |

**响应 data**: `Integer` — 新用户 ID

---

### 1.6 更新用户 `[管理员]`
```
POST /user/update
```

**请求参数 (JSON Body)**: 同 [1.5 创建用户](#15-创建用户-管理员)，但 **必须传 userId**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Integer | 是 | 要更新的用户 ID |
| ... | | | 其余字段同 1.5 |

**响应 data**: `Boolean` — true 表示成功

---

### 1.7 更新个人信息 `[需登录]`
```
POST /user/update/my
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| phone | String | 否 | 手机号 |
| gender | Integer | 否 | 性别：0-未知, 1-男, 2-女 |
| age | Integer | 否 | 年龄 |
| height | Double | 否 | 身高（cm） |
| weight | Double | 否 | 当前体重（kg） |
| fitGoal | String | 否 | 健身目标 |
| targetWeight | Double | 否 | 目标体重（kg） |

> 注意：此接口不支持修改 username、password、role、status

**响应 data**: `Boolean` — true 表示成功

---

### 1.8 删除用户 `[管理员]`
```
POST /user/delete
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 要删除的用户 ID |

**响应 data**: `Boolean` — true 表示成功

---

### 1.9 根据 ID 获取用户 `[管理员]`
```
GET /user/get?id={id}
```

**请求参数 (Query)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 用户 ID |

**响应 data**: `User`（完整实体对象）

---

### 1.10 根据 ID 获取用户（脱敏） `[需登录]`
```
GET /user/get/vo?id={id}
```

**请求参数 (Query)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 用户 ID |

**响应 data**: `UserVO`（结构同 1.2）

---

### 1.11 分页获取用户列表 `[管理员]`
```
POST /user/list/page
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| current | int | 否 | 当前页码，默认 1 |
| pageSize | int | 否 | 每页条数，默认 10 |
| sortField | String | 否 | 排序字段 |
| sortOrder | String | 否 | 排序方向：asc / desc |
| userId | Integer | 否 | 按 ID 筛选 |
| username | String | 否 | 按用户名模糊筛选 |
| phone | String | 否 | 按手机号筛选 |
| gender | Integer | 否 | 按性别筛选 |
| age | Integer | 否 | 按年龄筛选 |
| role | Integer | 否 | 按角色筛选 |
| status | Integer | 否 | 按状态筛选 |
| fitGoal | String | 否 | 按健身目标筛选 |

**响应 data**: `Page<User>`（含 total、records 等分页字段）

---

### 1.12 分页获取用户列表（脱敏） `[需登录]`
```
POST /user/list/page/vo
```

**请求参数**: 同 [1.11](#111-分页获取用户列表-管理员)，pageSize 最大 20

**响应 data**: `Page<UserVO>`

---

## 二、打卡模块 `/checkin`

### 2.1 添加打卡 `[需登录]`
```
POST /checkin/add
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| water | Integer | 是 | 饮水量（杯数） |
| sleep | Double | 是 | 睡眠时长（小时） |
| exercise | Integer | 是 | 运动时长（分钟） |

**请求示例**
```json
{
  "water": 8,
  "sleep": 7.5,
  "exercise": 30
}
```

**响应 data**: `Integer` — 打卡记录 ID

> 注意：同一用户每天只能打卡一次，重复打卡返回"今日已打卡"错误

---

### 2.2 删除打卡 `[需登录]`
```
POST /checkin/delete
```

**请求参数 (JSON Body)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 打卡记录 ID |

**响应 data**: `Boolean` — true 表示成功

> 普通用户只能删除自己的打卡，管理员可删除所有人的

---

### 2.3 根据 ID 获取打卡
```
GET /checkin/get?id={id}
```

**请求参数 (Query)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Integer | 是 | 打卡记录 ID |

**响应 data**: `CheckInVO`

**响应示例**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "userId": 1,
    "userName": "zhangsan",
    "checkInDate": "2026-05-07 00:00:00",
    "water": 8,
    "sleep": 7.5,
    "exercise": 30,
    "createdAt": "2026-05-07 08:30:00"
  },
  "message": ""
}
```

---

### 2.4 获取今日打卡 `[需登录]`
```
GET /checkin/today
```

**请求参数**: 无（自动获取当前登录用户）

**响应 data**: `DailyCheckIn`

```json
{
  "code": 0,
  "data": {
    "id": 1,
    "userId": 1,
    "checkInDate": "2026-05-07 00:00:00",
    "water": 8,
    "sleep": 7.5,
    "exercise": 30,
    "createdAt": "2026-05-07 08:30:00"
  },
  "message": ""
}
```

> 今日未打卡时 data 为 null

---

### 2.5 分页获取打卡列表
```
GET /checkin/list/page?current=1&pageSize=10&userId=1&startDate=2026-05-01&endDate=2026-05-07
```

**请求参数 (Query)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| current | int | 否 | 当前页码，默认 1 |
| pageSize | int | 否 | 每页条数，默认 10 |
| userId | Integer | 否 | 按用户 ID 筛选（非管理员只能查看自己） |
| startDate | Date | 否 | 开始日期（yyyy-MM-dd） |
| endDate | Date | 否 | 结束日期（yyyy-MM-dd） |

**响应 data**: `IPage<CheckInVO>`（含 total、records 等分页字段）

---

### 2.6 获取日期范围内的打卡列表 `[需登录]`
```
GET /checkin/list/range?startDate=2026-05-01&endDate=2026-05-07
```

**请求参数 (Query)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | Date | 是 | 开始日期（yyyy-MM-dd） |
| endDate | Date | 是 | 结束日期（yyyy-MM-dd） |

**响应 data**: `List<CheckInVO>` — 当前登录用户的打卡列表

---

### 2.7 获取当月打卡数量 `[需登录]`
```
GET /checkin/count/month?startDate=2026-05-01&endDate=2026-05-31
```

**请求参数 (Query)**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | Date | 是 | 统计开始日期 |
| endDate | Date | 是 | 统计结束日期 |

**响应 data**: `Integer` — 打卡天数

---

## 三、附录：数据字典

### UserVO 字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Integer | 用户 ID |
| username | String | 用户名 |
| phone | String | 手机号 |
| gender | Integer | 0-未知, 1-男, 2-女 |
| age | Integer | 年龄 |
| height | Double | 身高（cm） |
| weight | Double | 当前体重（kg） |
| fitGoal | String | 健身目标 |
| targetWeight | Double | 目标体重（kg） |
| role | Integer | 0-普通用户, 1-管理员 |
| status | Integer | 1-正常, 2-禁用 |
| createTime | Date | 注册时间 |
| updateTime | Date | 更新时间 |

### CheckInVO 字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录 ID |
| userId | Integer | 用户 ID |
| userName | String | 用户名 |
| checkInDate | Date | 打卡日期 |
| water | Integer | 饮水量（杯） |
| sleep | Double | 睡眠时长（小时） |
| exercise | Integer | 运动时长（分钟） |
| createdAt | Date | 创建时间 |

### 分页响应格式 `Page<T>`
```json
{
  "code": 0,
  "data": {
    "records": [...],   // 数据列表
    "total": 50,        // 总条数
    "current": 1,       // 当前页
    "size": 10,         // 每页条数
    "pages": 5          // 总页数
  },
  "message": ""
}
```
