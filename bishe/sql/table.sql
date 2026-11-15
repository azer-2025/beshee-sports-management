-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id           bigint(20)      not null auto_increment    comment '部门id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(50)     default ''                 comment '祖级列表',
  dept_name         varchar(30)     default ''                 comment '部门名称',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(20)     default null               comment '负责人',
  phone             varchar(11)     default null               comment '联系电话',
  email             varchar(50)     default null               comment '邮箱',
  status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  dept_id           bigint(20)      default null               comment '部门ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     not null                   comment '用户昵称',
  user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
  email             varchar(50)     default ''                 comment '用户邮箱',
  phonenumber       varchar(11)     default ''                 comment '手机号码',
  sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    default ''                 comment '头像地址',
  password          varchar(100)    default ''                 comment '密码',
  status            char(1)         default '0'                comment '账号状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    default ''                 comment '最后登录IP',
  login_date        datetime                                   comment '最后登录时间',
  pwd_update_date   datetime                                   comment '密码最后更新时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
  post_id       bigint(20)      not null auto_increment    comment '岗位ID',
  post_code     varchar(64)     not null                   comment '岗位编码',
  post_name     varchar(50)     not null                   comment '岗位名称',
  post_sort     int(4)          not null                   comment '显示顺序',
  status        char(1)         not null                   comment '状态（0正常 1停用）',
  create_by     varchar(64)     default ''                 comment '创建者',
  create_time   datetime                                   comment '创建时间',
  update_by     varchar(64)     default ''			       comment '更新者',
  update_time   datetime                                   comment '更新时间',
  remark        varchar(500)    default null               comment '备注',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id              bigint(20)      not null auto_increment    comment '角色ID',
  role_name            varchar(30)     not null                   comment '角色名称',
  role_key             varchar(100)    not null                   comment '角色权限字符串',
  role_sort            int(4)          not null                   comment '显示顺序',
  data_scope           char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  menu_check_strictly  tinyint(1)      default 1                  comment '菜单树选择项是否关联显示',
  dept_check_strictly  tinyint(1)      default 1                  comment '部门树选择项是否关联显示',
  status               char(1)         not null                   comment '角色状态（0正常 1停用）',
  del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time          datetime                                   comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                   comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  path              varchar(200)    default ''                 comment '路由地址',
  component         varchar(255)    default null               comment '组件路径',
  query             varchar(255)    default null               comment '路由参数',
  route_name        varchar(50)     default ''                 comment '路由名称',
  is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
  is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
  status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', sysdate(), '', null, '系统工具目录');
-- 二级菜单

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert into sys_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/dept/index',        '', '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          'admin', sysdate(), '', null, '部门管理菜单');
# insert into sys_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/post/index',        '', '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          'admin', sysdate(), '', null, '岗位管理菜单');

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

# insert into sys_menu values('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         'admin', sysdate(), '', null, '表单构建菜单');
# insert into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          'admin', sysdate(), '', null, '代码生成菜单');

-- Data rows omitted from the public source snapshot.

-- 三级菜单

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 用户管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 角色管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 菜单管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 部门管理按钮
# insert into sys_menu values('1016', '部门查询', '103', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',          '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1017', '部门新增', '103', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',            '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1018', '部门修改', '103', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',           '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1019', '部门删除', '103', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',         '#', 'admin', sysdate(), '', null, '');
# -- 岗位管理按钮
# insert into sys_menu values('1020', '岗位查询', '104', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',          '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1021', '岗位新增', '104', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',            '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1022', '岗位修改', '104', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',           '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1023', '岗位删除', '104', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',         '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1024', '岗位导出', '104', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',         '#', 'admin', sysdate(), '', null, '');
-- 字典管理按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 参数设置按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 通知公告按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 操作日志按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 登录日志按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 在线用户按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 定时任务按钮

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- 代码生成按钮
# insert into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 'admin', sysdate(), '', null, '');
# insert into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
  role_id   bigint(20) not null comment '角色ID',
  dept_id   bigint(20) not null comment '部门ID',
  primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
  user_id   bigint(20) not null comment '用户ID',
  post_id   bigint(20) not null comment '岗位ID',
  primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id           bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            varchar(200)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         varchar(50)     default ''                 comment '操作人员',
  dept_name         varchar(50)     default ''                 comment '部门名称',
  oper_url          varchar(255)    default ''                 comment '请求URL',
  oper_ip           varchar(128)    default ''                 comment '主机地址',
  oper_location     varchar(255)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  cost_time         bigint(20)      default 0                  comment '消耗时间',
  primary key (oper_id),
  key idx_sys_oper_log_bt (business_type),
  key idx_sys_oper_log_s  (status),
  key idx_sys_oper_log_ot (oper_time)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code        bigint(20)      not null auto_increment    comment '字典编码',
  dict_sort        int(4)          default 0                  comment '字典排序',
  dict_label       varchar(100)    default ''                 comment '字典标签',
  dict_value       varchar(100)    default ''                 comment '字典键值',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
  list_class       varchar(100)    default null               comment '表格回显样式',
  is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(500)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  user_name      varchar(50)    default ''                comment '用户账号',
  ipaddr         varchar(128)   default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id),
  key idx_sys_logininfor_s  (status),
  key idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id         int(4)          not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
  notice_content    longblob        default null               comment '公告内容',
  status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(255)    default null               comment '备注',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.



-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id          bigint(20)      not null auto_increment    comment '编号',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  sub_table_name    varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  tpl_web_type      varchar(30)     default ''                 comment '前端模板类型（element-ui模版 element-plus模版）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options           varchar(1000)                              comment '其它生成选项',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          bigint(20)                                 comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

-- ----------------------------
-- 20、体育器材分类表
-- ----------------------------
drop table if exists sp_equipment_category;
create table sp_equipment_category (
  category_id       bigint(20)      not null auto_increment    comment '分类ID',
  category_name     varchar(50)     not null                   comment '分类名称',
  class_type        char(1)         not null                   comment '器材大类（1球类 2器械类 3场地辅助类）',
  category_code     varchar(50)     default null               comment '分类编码',
  order_num         int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (category_id),
  unique key uk_sp_category_code (category_code)
) engine=innodb auto_increment=100 comment = '体育器材分类表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 21、体育器材台账表
-- ----------------------------
drop table if exists sp_equipment_asset;
create table sp_equipment_asset (
  asset_id           bigint(20)      not null auto_increment    comment '器材ID',
  category_id        bigint(20)      not null                   comment '分类ID',
  asset_name         varchar(100)    not null                   comment '器材名称',
  asset_code         varchar(64)     not null                   comment '器材编码',
  barcode            varchar(100)    default null               comment '条码',
  brand              varchar(50)     default null               comment '品牌',
  purchase_date      date                                       comment '采购日期',
  purchase_price     decimal(10,2)   default 0.00               comment '采购价',
  depreciation_rate  decimal(5,2)    default 10.00              comment '年折旧率（%）',
  service_life_year  int(4)          default 5                  comment '使用年限（年）',
  asset_status       char(1)         default '0'                comment '状态（0空闲 1借出 2维修中 3已报废 4已下架）',
  audit_status       char(1)         default '1'                comment '审核状态（0待审核 1通过 2驳回）',
  location           varchar(100)    default ''                 comment '存放位置',
  stock_qty          int(11)         default 1                  comment '总库存',
  available_qty      int(11)         default 1                  comment '可用库存',
  cover_image        varchar(255)    default null               comment '器材图片',
  del_flag           char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''                 comment '创建者',
  create_time        datetime                                    comment '创建时间',
  update_by          varchar(64)     default ''                 comment '更新者',
  update_time        datetime                                    comment '更新时间',
  remark             varchar(500)    default null               comment '备注',
  primary key (asset_id),
  unique key uk_sp_asset_code (asset_code),
  key idx_sp_asset_category (category_id),
  key idx_sp_asset_status (asset_status)
) engine=innodb auto_increment=1000 comment = '体育器材台账表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 22、器材预约借还记录表
-- ----------------------------
drop table if exists sp_equipment_borrow;
create table sp_equipment_borrow (
  borrow_id            bigint(20)      not null auto_increment    comment '借还ID',
  asset_id             bigint(20)      not null                   comment '器材ID',
  user_id              bigint(20)      not null                   comment '申请人ID',
  quantity             int(11)         default 1                  comment '申请数量',
  reserve_start_time   datetime                                    comment '预约开始时间',
  reserve_end_time     datetime                                    comment '预约结束时间',
  borrow_time          datetime                                    comment '借出时间',
  expected_return_time datetime                                    comment '预计归还时间',
  actual_return_time   datetime                                    comment '实际归还时间',
  status               char(1)         default '0'                comment '状态（0待审批 1审批通过 2审批驳回 3借出中 4已归还 5逾期）',
  reject_reason        varchar(255)    default null               comment '驳回原因',
  approval_by          varchar(64)     default ''                 comment '审批人',
  approval_time        datetime                                    comment '审批时间',
  overdue_days         int(11)         default 0                  comment '逾期天数',
  overdue_reason       varchar(255)    default null               comment '逾期原因回填',
  review_quality_score tinyint(4)      default null               comment 'quality score(1-5)',
  review_package_score tinyint(4)      default null               comment 'package score(1-5)',
  review_service_score tinyint(4)      default null               comment 'service score(1-5)',
  review_content       varchar(500)    default null               comment 'review content',
  review_time          datetime                                    comment 'review time',
  review_by            varchar(64)     default ''                 comment 'review by',
  del_flag             char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time          datetime                                    comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                    comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (borrow_id),
  key idx_sp_borrow_asset (asset_id),
  key idx_sp_borrow_user (user_id),
  key idx_sp_borrow_status (status)
) engine=innodb auto_increment=10000 comment = '器材预约借还记录表';

-- ----------------------------
-- 23、器材报修表
-- ----------------------------
drop table if exists sp_equipment_repair;
create table sp_equipment_repair (
  repair_id            bigint(20)      not null auto_increment    comment '报修ID',
  asset_id             bigint(20)      not null                   comment '器材ID',
  user_id              bigint(20)      not null                   comment '报修人ID',
  fault_desc           varchar(500)    not null                   comment '故障描述',
  fault_image          varchar(500)    default null               comment '故障图片',
  fault_reason         char(1)         default '0'                comment '损耗原因（0自然损耗 1使用不当 2外部损坏 3其他）',
  urgency              char(1)         default '0'                comment '紧急程度（0普通 1紧急）',
  status               char(1)         default '0'                comment '状态（0待受理 1已受理 2维修中 3已修复 4已驳回）',
  process_note         varchar(500)    default null               comment '处理反馈',
  repair_by            varchar(64)     default ''                 comment '维修处理人',
  repair_time          datetime                                    comment '受理时间',
  finish_time          datetime                                    comment '完成时间',
  del_flag             char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time          datetime                                    comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                    comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (repair_id),
  key idx_sp_repair_asset (asset_id),
  key idx_sp_repair_user (user_id),
  key idx_sp_repair_status (status)
) engine=innodb auto_increment=10000 comment = '器材报修表';

-- ----------------------------
-- 23.1、借还/报修样例数据
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- 23.2、Trend sample for last 12 months

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


update sp_equipment_asset
set available_qty = 4,
    update_by = 'admin',
    update_time = sysdate(),
    remark = concat(ifnull(remark, ''), ' SAMPLE_STOCK_LOW')
where asset_code = 'EQ-BALL-001'
  and stock_qty >= 4;

-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 24、器材模块字典类型
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 25、器材模块菜单与权限
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- =========================================================
-- 体育器材分类管理模块增量脚本（可重复执行）
-- 适用：在已存在 RuoYi 基础库的前提下执行
-- =========================================================

-- ----------------------------
-- 1) 业务表（若不存在则创建）
-- ----------------------------
create table if not exists sp_equipment_category (
                                                     category_id       bigint(20)      not null auto_increment    comment '分类ID',
                                                     category_name     varchar(50)     not null                   comment '分类名称',
                                                     class_type        char(1)         not null                   comment '器材大类（1球类 2器械类 3场地辅助类）',
                                                     category_code     varchar(50)     default null               comment '分类编码',
                                                     order_num         int(4)          default 0                  comment '显示顺序',
                                                     status            char(1)         default '0'                comment '状态（0正常 1停用）',
                                                     del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
                                                     create_by         varchar(64)     default ''                 comment '创建者',
                                                     create_time       datetime                                   comment '创建时间',
                                                     update_by         varchar(64)     default ''                 comment '更新者',
                                                     update_time       datetime                                   comment '更新时间',
                                                     remark            varchar(500)    default null               comment '备注',
                                                     primary key (category_id),
                                                     unique key uk_sp_category_code (category_code)
) engine=innodb comment = '体育器材分类表';

create table if not exists sp_equipment_asset (
                                                  asset_id           bigint(20)      not null auto_increment    comment '器材ID',
                                                  category_id        bigint(20)      not null                   comment '分类ID',
                                                  asset_name         varchar(100)    not null                   comment '器材名称',
                                                  asset_code         varchar(64)     not null                   comment '器材编码',
                                                  barcode            varchar(100)    default null               comment '条码',
                                                  brand              varchar(50)     default null               comment '品牌',
                                                  purchase_date      date                                       comment '采购日期',
                                                  purchase_price     decimal(10,2)   default 0.00               comment '采购价',
                                                  depreciation_rate  decimal(5,2)    default 10.00              comment '年折旧率（%）',
                                                  service_life_year  int(4)          default 5                  comment '使用年限（年）',
                                                  asset_status       char(1)         default '0'                comment '状态（0空闲 1借出 2维修中 3已报废 4已下架）',
                                                  audit_status       char(1)         default '1'                comment '审核状态（0待审核 1通过 2驳回）',
                                                  location           varchar(100)    default ''                 comment '存放位置',
                                                  stock_qty          int(11)         default 1                  comment '总库存',
                                                  available_qty      int(11)         default 1                  comment '可用库存',
                                                  cover_image        varchar(255)    default null               comment '器材图片',
                                                  del_flag           char(1)         default '0'                comment '删除标志（0存在 2删除）',
                                                  create_by          varchar(64)     default ''                 comment '创建者',
                                                  create_time        datetime                                    comment '创建时间',
                                                  update_by          varchar(64)     default ''                 comment '更新者',
                                                  update_time        datetime                                    comment '更新时间',
                                                  remark             varchar(500)    default null               comment '备注',
                                                  primary key (asset_id),
                                                  unique key uk_sp_asset_code (asset_code),
                                                  key idx_sp_asset_category (category_id),
                                                  key idx_sp_asset_status (asset_status)
) engine=innodb comment = '体育器材台账表';

create table if not exists sp_equipment_borrow (
                                                   borrow_id            bigint(20)      not null auto_increment    comment '借还ID',
                                                   asset_id             bigint(20)      not null                   comment '器材ID',
                                                   user_id              bigint(20)      not null                   comment '申请人ID',
                                                   quantity             int(11)         default 1                  comment '申请数量',
                                                   reserve_start_time   datetime                                    comment '预约开始时间',
                                                   reserve_end_time     datetime                                    comment '预约结束时间',
                                                   borrow_time          datetime                                    comment '借出时间',
                                                   expected_return_time datetime                                    comment '预计归还时间',
                                                   actual_return_time   datetime                                    comment '实际归还时间',
                                                   status               char(1)         default '0'                comment '状态（0待审批 1审批通过 2审批驳回 3借出中 4已归还 5逾期）',
                                                   reject_reason        varchar(255)    default null               comment '驳回原因',
                                                   approval_by          varchar(64)     default ''                 comment '审批人',
                                                   approval_time        datetime                                    comment '审批时间',
                                                   overdue_days         int(11)         default 0                  comment '逾期天数',
                                                   overdue_reason       varchar(255)    default null               comment '逾期原因回填',
                                                   review_quality_score tinyint(4)      default null               comment 'quality score(1-5)',
                                                   review_package_score tinyint(4)      default null               comment 'package score(1-5)',
                                                   review_service_score tinyint(4)      default null               comment 'service score(1-5)',
                                                   review_content       varchar(500)    default null               comment 'review content',
                                                   review_time          datetime                                    comment 'review time',
                                                   review_by            varchar(64)     default ''                 comment 'review by',
                                                   del_flag             char(1)         default '0'                comment '删除标志（0存在 2删除）',
                                                   create_by            varchar(64)     default ''                 comment '创建者',
                                                   create_time          datetime                                    comment '创建时间',
                                                   update_by            varchar(64)     default ''                 comment '更新者',
                                                   update_time          datetime                                    comment '更新时间',
                                                   remark               varchar(500)    default null               comment '备注',
                                                   primary key (borrow_id),
                                                   key idx_sp_borrow_asset (asset_id),
                                                   key idx_sp_borrow_user (user_id),
                                                   key idx_sp_borrow_status (status)
) engine=innodb comment = '器材预约借还记录表';

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'overdue_reason'
);
set @sp_borrow_alter_sql := if(
        @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column overdue_reason varchar(255) default null comment ''逾期原因回填'' after overdue_days',
        'select 1'
                            );
prepare stmt_sp_borrow from @sp_borrow_alter_sql;
execute stmt_sp_borrow;
deallocate prepare stmt_sp_borrow;


set @sp_col_review_quality_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_quality_score'
);
set @sp_borrow_review_quality_sql := if(
        @sp_col_review_quality_exists = 0,
        'alter table sp_equipment_borrow add column review_quality_score tinyint(4) default null comment ''quality score(1-5)'' after overdue_reason',
        'select 1'
                                     );
prepare stmt_sp_borrow_review_quality from @sp_borrow_review_quality_sql;
execute stmt_sp_borrow_review_quality;
deallocate prepare stmt_sp_borrow_review_quality;

set @sp_col_review_package_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_package_score'
);
set @sp_borrow_review_package_sql := if(
        @sp_col_review_package_exists = 0,
        'alter table sp_equipment_borrow add column review_package_score tinyint(4) default null comment ''package score(1-5)'' after review_quality_score',
        'select 1'
                                     );
prepare stmt_sp_borrow_review_package from @sp_borrow_review_package_sql;
execute stmt_sp_borrow_review_package;
deallocate prepare stmt_sp_borrow_review_package;

set @sp_col_review_service_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_service_score'
);
set @sp_borrow_review_service_sql := if(
        @sp_col_review_service_exists = 0,
        'alter table sp_equipment_borrow add column review_service_score tinyint(4) default null comment ''service score(1-5)'' after review_package_score',
        'select 1'
                                     );
prepare stmt_sp_borrow_review_service from @sp_borrow_review_service_sql;
execute stmt_sp_borrow_review_service;
deallocate prepare stmt_sp_borrow_review_service;

set @sp_col_review_content_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_content'
);
set @sp_borrow_review_content_sql := if(
        @sp_col_review_content_exists = 0,
        'alter table sp_equipment_borrow add column review_content varchar(500) default null comment ''review content'' after review_service_score',
        'select 1'
                                     );
prepare stmt_sp_borrow_review_content from @sp_borrow_review_content_sql;
execute stmt_sp_borrow_review_content;
deallocate prepare stmt_sp_borrow_review_content;

set @sp_col_review_time_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_time'
);
set @sp_borrow_review_time_sql := if(
        @sp_col_review_time_exists = 0,
        'alter table sp_equipment_borrow add column review_time datetime comment ''review time'' after review_content',
        'select 1'
                                  );
prepare stmt_sp_borrow_review_time from @sp_borrow_review_time_sql;
execute stmt_sp_borrow_review_time;
deallocate prepare stmt_sp_borrow_review_time;

set @sp_col_review_by_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_by'
);
set @sp_borrow_review_by_sql := if(
        @sp_col_review_by_exists = 0,
        'alter table sp_equipment_borrow add column review_by varchar(64) default '''' comment ''review by'' after review_time',
        'select 1'
                                );
prepare stmt_sp_borrow_review_by from @sp_borrow_review_by_sql;
execute stmt_sp_borrow_review_by;
deallocate prepare stmt_sp_borrow_review_by;

create table if not exists sp_equipment_repair (
                                                   repair_id            bigint(20)      not null auto_increment    comment '报修ID',
                                                   asset_id             bigint(20)      not null                   comment '器材ID',
                                                   user_id              bigint(20)      not null                   comment '报修人ID',
                                                   fault_desc           varchar(500)    not null                   comment '故障描述',
                                                   fault_image          varchar(500)    default null               comment '故障图片',
                                                   fault_reason         char(1)         default '0'                comment '损耗原因（0自然损耗 1使用不当 2外部损坏 3其他）',
                                                   urgency              char(1)         default '0'                comment '紧急程度（0普通 1紧急）',
                                                   status               char(1)         default '0'                comment '状态（0待受理 1已受理 2维修中 3已修复 4已驳回）',
                                                   process_note         varchar(500)    default null               comment '处理反馈',
                                                   repair_by            varchar(64)     default ''                 comment '维修处理人',
                                                   repair_time          datetime                                    comment '受理时间',
                                                   finish_time          datetime                                    comment '完成时间',
                                                   del_flag             char(1)         default '0'                comment '删除标志（0存在 2删除）',
                                                   create_by            varchar(64)     default ''                 comment '创建者',
                                                   create_time          datetime                                    comment '创建时间',
                                                   update_by            varchar(64)     default ''                 comment '更新者',
                                                   update_time          datetime                                    comment '更新时间',
                                                   remark               varchar(500)    default null               comment '备注',
                                                   primary key (repair_id),
                                                   key idx_sp_repair_asset (asset_id),
                                                   key idx_sp_repair_user (user_id),
                                                   key idx_sp_repair_status (status)
) engine=innodb comment = '器材报修表';

-- ----------------------------
-- 2) 业务初始化数据（分类/台账）
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- 借还样例数据（用于审批中心/统计图展示）

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- 报修样例数据（用于报修管理/损耗原因分析）

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- 近12个月借阅样例（用于月度走势）

-- Data rows omitted from the public source snapshot.


-- 本月高借阅样例（配合低库存触发“建议采购”）

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


update sp_equipment_asset
set available_qty = 4,
    update_by = 'admin',
    update_time = sysdate(),
    remark = concat(ifnull(remark, ''), ' SAMPLE_STOCK_LOW')
where asset_code = 'EQ-BALL-001'
  and stock_qty >= 4
  and (remark is null or remark not like '%SAMPLE_STOCK_LOW%');

-- 高频报修样例（触发“建议报废”）

-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 3) 字典类型（若不存在则插入）
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 4) 字典数据（按 dict_type + dict_value 幂等插入）
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 5) 菜单与权限（若不存在则插入）
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- ----------------------------
-- 6) 角色、用户与RBAC绑定（若不存在则插入）
-- ----------------------------

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Sports profile extension incremental script
-- Compatible with MySQL 5.7/8.0

create table if not exists sp_user_profile_ext (
                                                   user_id            bigint(20)      not null               comment '用户ID',
                                                   student_no         varchar(30)     default ''             comment '学号',
                                                   coach_no           varchar(30)     default ''             comment '教练编号',
                                                   campus             varchar(64)     default ''             comment '校区',
                                                   college            varchar(64)     default ''             comment '学院',
                                                   major_name         varchar(64)     default ''             comment '专业',
                                                   class_name         varchar(64)     default ''             comment '班级',
                                                   id_no              varchar(32)     default ''             comment '身份证号',
                                                   emergency_contact  varchar(30)     default ''             comment '紧急联系人',
                                                   emergency_phone    varchar(20)     default ''             comment '紧急联系电话',
                                                   bio                varchar(255)    default ''             comment '个人简介',
                                                   remark             varchar(500)    default ''             comment '备注',
                                                   create_by          varchar(64)     default ''             comment '创建者',
                                                   create_time        datetime                                comment '创建时间',
                                                   update_by          varchar(64)     default ''             comment '更新者',
                                                   update_time        datetime                                comment '更新时间',
                                                   primary key (user_id)
) engine=innodb comment = '用户扩展档案表';

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Patch: add missing columns for sp_equipment_borrow (MySQL 5.7/8.0 compatible)
-- Usage: run this script in the target database.

set @sp_table_exists := (
    select count(1)
    from information_schema.tables
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
);

set @sp_sql := if(
        @sp_table_exists = 1,
        'select 1',
        'select ''skip: table sp_equipment_borrow not found'' as msg'
               );
prepare stmt_sp_table_check from @sp_sql;
execute stmt_sp_table_check;
deallocate prepare stmt_sp_table_check;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'overdue_reason'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column overdue_reason varchar(255) default null comment ''overdue reason'' after overdue_days',
        'select 1'
               );
prepare stmt_sp_add_overdue_reason from @sp_sql;
execute stmt_sp_add_overdue_reason;
deallocate prepare stmt_sp_add_overdue_reason;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_quality_score'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column review_quality_score tinyint(4) default null comment ''quality score(1-5)'' after overdue_reason',
        'select 1'
               );
prepare stmt_sp_add_review_quality_score from @sp_sql;
execute stmt_sp_add_review_quality_score;
deallocate prepare stmt_sp_add_review_quality_score;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_package_score'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column review_package_score tinyint(4) default null comment ''package score(1-5)'' after review_quality_score',
        'select 1'
               );
prepare stmt_sp_add_review_package_score from @sp_sql;
execute stmt_sp_add_review_package_score;
deallocate prepare stmt_sp_add_review_package_score;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_service_score'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column review_service_score tinyint(4) default null comment ''service score(1-5)'' after review_package_score',
        'select 1'
               );
prepare stmt_sp_add_review_service_score from @sp_sql;
execute stmt_sp_add_review_service_score;
deallocate prepare stmt_sp_add_review_service_score;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_content'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column review_content varchar(500) default null comment ''review content'' after review_service_score',
        'select 1'
               );
prepare stmt_sp_add_review_content from @sp_sql;
execute stmt_sp_add_review_content;
deallocate prepare stmt_sp_add_review_content;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_time'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column review_time datetime comment ''review time'' after review_content',
        'select 1'
               );
prepare stmt_sp_add_review_time from @sp_sql;
execute stmt_sp_add_review_time;
deallocate prepare stmt_sp_add_review_time;

set @sp_col_exists := (
    select count(1)
    from information_schema.columns
    where table_schema = database()
      and table_name = 'sp_equipment_borrow'
      and column_name = 'review_by'
);
set @sp_sql := if(
        @sp_table_exists = 1 and @sp_col_exists = 0,
        'alter table sp_equipment_borrow add column review_by varchar(64) default '''' comment ''review by'' after review_time',
        'select 1'
               );
prepare stmt_sp_add_review_by from @sp_sql;
execute stmt_sp_add_review_by;
deallocate prepare stmt_sp_add_review_by;

-- verify
select
    column_name,
    column_type
from information_schema.columns
where table_schema = database()
  and table_name = 'sp_equipment_borrow'
  and column_name in (
                      'overdue_reason',
                      'review_quality_score',
                      'review_package_score',
                      'review_service_score',
                      'review_content',
                      'review_time',
                      'review_by'
    )
order by ordinal_position;

 -- 前台个人档案字典选项 (学院/专业)
     -- 1. 插入字典类型

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


    -- 2. 插入学院类型数据

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


    -- 3. 插入专业类型数据

-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.


-- Data rows omitted from the public source snapshot.

-- --------------------------------------------
-- Sports asset cover image initialization (2026-02-25)
-- 1) Image files: uploadPath/upload/sports-assets/
-- 2) DB path format: /profile/... (frontend can render directly)
-- --------------------------------------------

update sp_equipment_asset
set cover_image = '/profile/upload/sports-assets/basketball_spalding.png',
    update_by = 'admin',
    update_time = sysdate()
where asset_code = 'EQ-BALL-001';

update sp_equipment_asset
set cover_image = '/profile/upload/sports-assets/soccer_nike_size5.png',
    update_by = 'admin',
    update_time = sysdate()
where asset_code = 'EQ-BALL-002';

update sp_equipment_asset
set cover_image = '/profile/upload/sports-assets/treadmill_commercial.png',
    update_by = 'admin',
    update_time = sysdate()
where asset_code = 'EQ-MCH-001';

update sp_equipment_asset
set cover_image = '/profile/upload/sports-assets/strength_machine_combo.png',
    update_by = 'admin',
    update_time = sysdate()
where asset_code = 'EQ-MCH-002';

update sp_equipment_asset
set cover_image = '/profile/upload/sports-assets/training_cone_set.png',
    update_by = 'admin',
    update_time = sysdate()
where asset_code = 'EQ-AUX-001';
