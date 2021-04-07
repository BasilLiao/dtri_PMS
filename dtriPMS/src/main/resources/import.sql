--system_config
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value,sys_header)VALUES (1, 1, 'FTP設定', 'IP', '10.0.0.1',true);
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (2, 1, 'FTP設定', 'PORT', '21');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (3, 1, 'FTP設定', 'PATH', '/123/456/789');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (4, 2, 'E_MAIL代理', '帳號', '123MES@gmail.com');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (5, 2, 'E_MAIL代理', '密碼', '123MES');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (6, 2, 'E_MAIL代理', '協定', 'POST');
SELECT setval('public.system_config_seq', 6, true);
--system_permission
--無權限
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control,sys_header)VALUES (0, 0, '無', '0000000000', 0, '無', '',true);
--系統管理
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (1, 1, '系統管理', '0001001111', 1001, '系統-參數設定', 'system_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (2, 1, '系統管理', '0001001111', 1002, '功能組-權限管理', 'system_permission.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (3, 1, '系統管理', '0001001111', 1003, '使用者-群組管理', 'system_group.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (4, 1, '系統管理', '0001001111', 1004, '使用者-帳號管理', 'system_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (5, 1, '系統管理', '0001001111', 1005, '使用者-待命人員', 'system_standby.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (6, 1, '系統管理', '0001001111', 1006, '布告欄-設置管理', 'system_bulletin.basil');
--個人功能
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (7, 2, '個人功能', '0001000001', 1101, '首頁', 'index.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (8, 2, '個人功能', '0001001101', 1102, '個人-資料內容', 'own_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (9, 2, '個人功能', '0001001101', 1103, '個人-自訂設定', 'own_config.basil');
--工作站
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (10, 3, '工作站功能', '0001001101', 1201, '工作站設定-規則管理', 'workstation_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (11, 3, '工作站功能', '0001001101', 1202, '工作站內容-項目管理', 'workstation_project.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (12, 3, '工作站功能', '0001001101', 1203, '工作站程序-流程管理', 'workstation_propgram.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (13, 3, '工作站功能', '0001001101', 1204, '工作站補單-SN補單', 'workstation_snadd.basil');
SELECT setval('public.system_permission_seq', 13, true);

--system_group(sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
----admin
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (1,1, '系統管理者', '0000000000', 0,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (2,1, '系統管理者', '1111111111', 1,1001);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (3,1, '系統管理者', '1111111111', 2,1002);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (4,1, '系統管理者', '0001111111', 3,1003);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (5,1, '系統管理者', '1111111111', 4,1004);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (6,1, '系統管理者', '1111111111', 5,1005);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (7,1, '系統管理者', '1111111111', 6,1006);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (8,1, '系統管理者', '0001000001', 7,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (9,1, '系統管理者', '0001000001', 8,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (10,1, '系統管理者', '0001000001', 9,1103);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (11,1, '系統管理者', '0001000001', 10,1201);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (12,1, '系統管理者', '0001000001', 11,1202);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (13,1, '系統管理者', '0001000001', 12,1203);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (14,1, '系統管理者', '0001000001', 13,1204);
----user
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (15,2, '一般使用者', '0000000000', 0,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (16,2, '一般使用者', '0001000001', 7,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (17,2, '一般使用者', '0001000001', 8,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (18,2, '一般使用者', '0001000001', 9,1103);
SELECT setval('public.system_group_seq', 18, true);

--system_user
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (1,'admin','Admin_en', 'admin@dtr.com', 'Admin', '123456', '超級管理者',1 );
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (2,'user','User_en', 'user@dtr.com', 'User', '123456', '一般使用者',2);
SELECT setval('public.system_user_seq', 2, true);

--workstation_prohect


