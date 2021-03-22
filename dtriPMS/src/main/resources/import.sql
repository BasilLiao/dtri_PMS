--system_permission
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (1, 1, '系統管理', '0001001111', 1001, '系統-參數設定', 'system_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (2, 1, '系統管理', '0001001111', 1002, '功能組-權限管理', 'system_permission.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (3, 1, '系統管理', '0001001111', 1003, '使用者-帳號管理', 'system_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (4, 1, '系統管理', '0001001111', 1004, '使用者-群組管理', 'system_group.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (5, 1, '系統管理', '0001001111', 1005, '使用者-待命人員', 'standby_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (6, 1, '系統管理', '0001001111', 1006, '布告欄-設置管理', 'system_none.basil');

INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (7, 2, '個人功能', '0001000001', 1101, '首頁', 'index.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (8, 2, '個人功能', '0001001101', 1102, '個人-資料內容', 'own_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (9, 2, '個人功能', '0001001101', 1103, '個人-自訂設定', 'own_config.basil');

INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (10, 3, '工作站功能', '0001001101', 1201, '工作站設定-規則管理', 'workstation_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (11, 3, '工作站功能', '0001001101', 1202, '工作站內容-項目管理', 'workstation_project.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (12, 3, '工作站功能', '0001001101', 1203, '工作站程序-流程管理', 'workstation_propgram.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (13, 3, '工作站功能', '0001001101', 1204, '工作站補單-SN補單', 'workstation_snadd.basil');

--system_group(sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
----admin
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 1);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 2);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 3);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 4);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 5);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 6);

INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 7);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 8);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 9);

INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 10);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 11);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 12);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 13);
----user
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (2, '一般使用者', '0001000001', 7);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (2, '一般使用者', '0001000001', 8);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (2, '一般使用者', '0001000001', 9);

--system_user
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (1,'admin','Admin_en', 'admin@dtr.com', 'Admin', '123456', '超級管理者',1 );
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (2,'user','User_en', 'user@dtr.com', 'User', '123456', '一般使用者',2);


--workstation_prohect


