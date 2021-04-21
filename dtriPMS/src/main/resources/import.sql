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
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control,sys_header)VALUES (1, 0, '無', '0000000000', 0, 'Title_Group', '',true);
--系統管理
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sys_status, sp_name, sp_control)VALUES (2, 1, '系統管理', '0001001111', 1001, 2, '系統-參數設定', 'system_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sys_status, sp_name, sp_control)VALUES (3, 1, '系統管理', '0001001111', 1002, 2, '功能-單元管理', 'system_permission.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (4, 1, '系統管理', '0001001111', 1003, '群組-權限管理', 'system_group.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (5, 1, '系統管理', '0001001111', 1004, '帳號-帳號管理', 'system_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (6, 1, '系統管理', '0001001111', 1005, '人員-待命狀態', 'system_standby.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (7, 1, '系統管理', '0001001111', 1006, '布告-設置管理', 'system_bulletin.basil');
--個人功能
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (8, 2, '個人設定', '0001000001', 1101, '首頁', 'index.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (9, 2, '個人設定', '0001001101', 1102, '個人-資料內容', 'own_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (10, 2, '個人設定', '0001001101', 1103,'個人-自訂設定', 'own_config.basil');
--產品製程
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (11, 4, '產品製程', '0001001101', 1201, '製令-製程管理', 'production_header.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (12, 4, '產品製程', '0001001101', 1202, '製令-關聯物料', 'production_body.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (13, 4, '產品製程', '0001001101', 1203, '製令-規格紀錄', 'production_records.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (14, 4, '產品製程', '0001001101', 1204, '製程-製令紀錄', 'manufacturing_records.basil');
--工作站
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (15, 3, '工作站', '0001001101', 1301, '工作-規則管理', 'workstation_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (16, 3, '工作站', '0001001101', 1302, '工作-項目管理', 'workstation_project.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (17, 3, '工作站', '0001001101', 1303, '工作-流程管理', 'workstation_propgram.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (18, 3, '工作站', '0001001101', 1304, '作業-SN補單據', 'workstation_snadd.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (19, 3, '工作站', '0001001101', 1305, '作業-製程工作站', 'workstation_work.basil');


SELECT setval('public.system_permission_seq', 19, true);

--system_group(sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
----admin
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (1,1, '系統管理者_Group', '0000000000', 1,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (2,1, '系統管理者', '1111111111', 2,1001);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (3,1, '系統管理者', '1111111111', 3,1002);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (4,1, '系統管理者', '0001111111', 4,1003);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (5,1, '系統管理者', '1111111111', 5,1004);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (6,1, '系統管理者', '1111111111', 6,1005);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (7,1, '系統管理者', '1111111111', 7,1006);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (8,1, '系統管理者', '1111111111', 8,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (9,1, '系統管理者', '1111111111', 9,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (10,1, '系統管理者', '1111111111', 10,1103);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (11,1, '系統管理者', '1111111111', 11,1201);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (12,1, '系統管理者', '1111111111', 12,1202);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (13,1, '系統管理者', '1111111111', 13,1203);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (14,1, '系統管理者', '1111111111', 14,1204);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (15,1, '系統管理者', '1111111111', 15,1204);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (16,1, '系統管理者', '1111111111', 16,1301);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (17,1, '系統管理者', '1111111111', 17,1302);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (18,1, '系統管理者', '1111111111', 18,1303);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (19,1, '系統管理者', '1111111111', 19,1304);
----user
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (20,2, '一般使用者_Group', '0000000000', 1,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (21,2, '一般使用者', '0001000001', 8,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (22,2, '一般使用者', '0001000001', 9,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (23,2, '一般使用者', '0001000001', 10,1103);
SELECT setval('public.system_group_seq', 23, true);

--system_user
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (1,'admin','Admin_en', 'admin@dtr.com', 'Admin', '$2a$10$2tt8kwMSweSbTY/Jx1T9HuWcmrHzy50fZuOJWm/XORjJEOtpIoUdy', '超級管理者',1 );
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (2,'user','User_en', 'user@dtr.com', 'User', '$2a$10$2tt8kwMSweSbTY/Jx1T9HuWcmrHzy50fZuOJWm/XORjJEOtpIoUdy', '一般使用者',2);
SELECT setval('public.system_user_seq', 2, true);

--production_header
INSERT INTO production_header(ph_id, ph_model, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (0, 'Title_Group', '', '', 0, false, 0);
INSERT INTO production_header(ph_id, ph_model, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (1, '測試_產品型號1', 'A511-123456789', '50/100', 1, true, 0);
INSERT INTO production_header(ph_id, ph_model, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (2, '測試_產品型號2', 'A512-123456789', '100/250', 2, true, 0);
SELECT setval('public.production_header_seq', 2, true);

--production_body
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_value01, pb_value02 ,pb_value03 ,pb_value04 ,pb_value05 ,pb_value06 ,pb_value07,pb_value08,pb_value09,pb_sn, sys_header) VALUES ( 0,0, 0,'SN_MB(UUID)' ,'SN_MAC_ID(1)' ,'SN_MAC_ID(2)' ,'SN_WiFi_MAC' ,'SN_(4G)IMEI','SN_Battery(1)','SN_Battery(2)','SN_Battery(3)','SN_SSD','', true);
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule) VALUES ( 1,1, 0, 'SN_Group' , true,'');
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 2,1, 0, '測試_出貨序號_SN101' , false, '{A站:{A1項目:N,A2項目:Y}}','主版號11');
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 3,1, 0, '測試_出貨序號_SN102' , false, '{A站:{A1項目:N,A2項目:Y}}','主版號12');
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 4,1, 0, '測試_出貨序號_SN103' , false, '{A站:{A1項目:N,A2項目:Y}}','主版號13');

INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule) VALUES ( 5,2, 0, 'SN_Group' , true,'');
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 6,2, 0, '測試_出貨序號_SN201' , false, '{A站:{A1項目:N,A2項目:Y}}','主版號21');
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 7,2, 0, '測試_出貨序號_SN202' , false, '{A站:{A1項目:N,A2項目:Y}}','主版號22');
INSERT INTO production_body(pb_id,pb_ph_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01,pb_l_text) VALUES ( 8,2, 0, '測試_出貨序號_SN203' , false, '{A站:{A1項目:N,A2項目:Y}}','主版號23','999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999');
SELECT setval('public.production_body_seq', 8, true);

--production_records



--workstation_prohect
