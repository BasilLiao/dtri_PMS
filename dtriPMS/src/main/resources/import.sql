--system_config
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value,sys_header)VALUES (1, 1, 'FTP設定', 'IP', '10.0.0.1',true);
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (2, 1, 'FTP設定', 'PORT', '21');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (3, 1, 'FTP設定', 'PATH', '/123/456/789');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (4, 2, 'E_MAIL代理', '帳號', '123MES@gmail.com');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (5, 2, 'E_MAIL代理', '密碼', '123MES');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (6, 2, 'E_MAIL代理', '協定', 'POST');
SELECT setval('public.system_config_seq', 6, true);
DROP sequence IF EXISTS SYSTEM_CONFIG_G_SEQ CASCADE;
create sequence SYSTEM_CONFIG_G_SEQ start with 3 increment by 1;

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
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (11, 3, '產品製程', '0001001101', 1201, '製令-料件SN設定', 'production_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (12, 3, '產品製程', '0001001101', 1202, '製令-產品SN規則', 'production_sn.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (13, 3, '產品製程', '0001001101', 1203, '製令-關聯SN物料', 'production_body.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (14, 3, '產品製程', '0001001101', 1204, '製令-製程管理', 'production_header.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (15, 3, '產品製程', '0001001101', 1205, '製令-規格紀錄', 'production_records.basil');
--工作站
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (16, 4, '工作站', '0001001101', 1301, '工作-過站WP設定', 'workstation_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (17, 4, '工作站', '0001001101', 1302, '工作-項目SN管理', 'workstation_item.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (18, 4, '工作站', '0001001101', 1303, '工作-站台WK管理', 'workstation.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (19, 4, '工作站', '0001001101', 1304, '工作-流程管理', 'workstation_program.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (20, 4, '工作站', '0001001101', 1305, '作業-SN補單', 'workstation_snadd.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (21, 4, '工作站', '0001001101', 1306, '作業-工作站', 'workstation_work.basil');
--維修區
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (22, 5, '產品維修', '0001001101', 1401, '維修-錯誤代碼', 'maintain_code.basil');

SELECT setval('public.system_permission_seq', 22, true);
DROP sequence IF EXISTS SYSTEM_PERMISSION_G_SEQ CASCADE;
create sequence SYSTEM_PERMISSION_G_SEQ start with 6 increment by 1;

--system_group(sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
----admin
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (1,1, '系統管理者', '0000000000', 1,0,true);
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
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (15,1, '系統管理者', '1111111111', 15,1205);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (16,1, '系統管理者', '1111111111', 16,1301);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (17,1, '系統管理者', '1111111111', 17,1302);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (18,1, '系統管理者', '1111111111', 18,1303);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (19,1, '系統管理者', '1111111111', 19,1304);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (20,1, '系統管理者', '1111111111', 20,1305);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (21,1, '系統管理者', '1111111111', 21,1306);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (22,1, '系統管理者', '1111111111', 22,1401);

----user
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (23,2, '一般使用者', '0000000000', 1,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (24,2, '一般使用者', '0001000001', 8,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (25,2, '一般使用者', '0001000001', 9,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (26,2, '一般使用者', '0001000001', 10,1103);
SELECT setval('public.system_group_seq', 26, true);
DROP sequence IF EXISTS SYSTEM_GROUP_G_SEQ CASCADE;
create sequence SYSTEM_GROUP_G_SEQ start with 3 increment by 1;

--system_user
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (1,'admin','Admin_en', 'admin@dtr.com', 'Admin', '$2a$10$2tt8kwMSweSbTY/Jx1T9HuWcmrHzy50fZuOJWm/XORjJEOtpIoUdy', '超級管理者',1 );
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (2,'user','User_en', 'user@dtr.com', 'User', '$2a$10$2tt8kwMSweSbTY/Jx1T9HuWcmrHzy50fZuOJWm/XORjJEOtpIoUdy', '一般使用者',2);
SELECT setval('public.system_user_seq', 2, true);

----production_records
INSERT INTO production_records(pr_id, pr_bom_id, pr_c_from,pr_c_name,pr_s_sn, pr_e_sn, pr_order_id, pr_p_model,pr_p_quantity,pr_p_ok_quantity,pr_b_item, pr_s_item) VALUES ('A44654-A654',  '91-363-G100001', '生產注意事項','MAYA(Isreal)', 'AAAB12111A001','AAAB12111A100','訂單編號(OP-2021042001)', 'DT363GL',3,1, '{"CPU":i600,"RAM":"4G"}','{"M/B 版本":"R5.6.P","ECN":"D6B"}');
INSERT INTO production_records(pr_id, pr_bom_id, pr_c_from,pr_c_name,pr_s_sn, pr_e_sn, pr_order_id, pr_p_model,pr_p_quantity,pr_p_ok_quantity,pr_b_item, pr_s_item) VALUES ('A511-210204004',  '92-363-G100001', '生產注意事項','MAYA(Isreal)', 'AAAB12111A101','AAAB12111A200','訂單編號(OP-2021042002)', 'DT363GL',7,0, '{"CPU":i700,"RAM":"4G"}','{"M/B 版本":"R5.7.P","ECN":"D6B"}');
INSERT INTO production_records(pr_id, pr_bom_id, pr_c_from,pr_c_name,pr_s_sn, pr_e_sn, pr_order_id, pr_p_model,pr_p_quantity,pr_p_ok_quantity,pr_b_item, pr_s_item) VALUES ('A513-123456799',  '93-363-G100001', '生產注意事項','MAYA(Isreal)', 'AAAB12111A201','AAAB12111A250','訂單編號(OP-2021042002)', 'DT363GL',7,0, '{"CPU":i800,"RAM":"4G"}','{"M/B 版本":"R5.8.P","ECN":"D6B"}');

--production_header
INSERT INTO production_header(ph_id,ph_pb_g_id,ph_type, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (1,1,'A511', 'A44654-A654', '', 1, true, 0);
INSERT INTO production_header(ph_id,ph_pb_g_id,ph_type, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (2,2,'A511', 'A511-210204004', '', 2, true, 0);
INSERT INTO production_header(ph_id,ph_pb_g_id,ph_type, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (3,2,'A511', 'A513-123456799', '', 1, true, 0);
SELECT setval('public.production_header_seq', 3, true);
DROP sequence IF EXISTS PRODUCTION_HEADER_G_SEQ CASCADE;
create sequence PRODUCTION_HEADER_G_SEQ start with 4 increment by 1;

----production_body
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_value01, pb_value02 ,pb_value03 ,pb_value04 ,pb_value05 ,pb_value06 ,pb_value07,pb_value08,pb_value09,pb_sn, sys_header,pb_w_name01,pb_w_name02,pb_w_name03,pb_w_name04,pb_w_name05,pb_w_name06,pb_w_name07,pb_w_name08) VALUES ( 0,0, 0,'MB(UUID)' ,'MAC_ID(1)' ,'MAC_ID(2)' ,'WiFi_MAC' ,'(4G)IMEI','Battery(1)','Battery(2)','Battery(3)','SSD','', true,'PCB_processing','PCB_burnin','PCB_function_test','Assembly','Burnin','T1','T2','Package');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 1,1, 0, '06QP32110Z391' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號11');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 2,1, 0, '06QP32110Z392' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號12');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 3,1, 0, '06QP32110Z393' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號13');

INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 4,2, 0, 'A13W1CC140300' , false, '{A站:N,B站:Y}','主版號21');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 5,2, 0, 'A13W1CC140301' , false, '{A站:N,B站:Y}','主版號22');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01,pb_l_text) VALUES ( 6,2, 0, 'A13W1CC140302' , false, '{A站:N,B站:Y}','主版號23','999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 7,2, 0, 'A13W1CC140303' , false, '{A站:N,B站:Y}','主版號24');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 8,2, 0, 'A13W1CC140304' , false, '{A站:N,B站:Y}','主版號25');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 9,2, 0, 'A13W1CC140305' , false, '{A站:N,B站:Y}','主版號26');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 10,2, 0, 'A13W1CC140306' , false, '{A站:N,B站:Y}','主版號27');

SELECT setval('public.production_body_seq', 10, true);
DROP sequence IF EXISTS PRODUCTION_BODY_G_SEQ CASCADE;
create sequence PRODUCTION_BODY_G_SEQ start with 3 increment by 1;

--workstation_item
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (0, true,'Group' , 'Group');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (1, false,'pb_value01' , 'SN_MB(UUID)');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (2, false,'pb_value02' , 'SN_MAC_ID(1)');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (3, false,'pb_value03' , 'SN_MAC_ID(2)');
SELECT setval('public.workstation_item_seq', 3, true);

--workstation
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (0, true, 'Group', 0, 'Group','','', 0, 0);

INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (1, true, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 0);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (2, false, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 1);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (3, false, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 2);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (4, false, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 3);

INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (5, true, 'BC099', 2, 'PCB_function_test','pb_w_name03','系統管理者', 1, 0);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (6, false, 'BC099', 2, 'PCB_function_test','pb_w_name03','系統管理者', 1, 1);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (7, false, 'BC099', 2, 'PCB_function_test','pb_w_name03','系統管理者', 1, 2);
SELECT setval('public.workstation_seq', 7, true);
DROP sequence IF EXISTS WORKSTATION_G_SEQ CASCADE;
create sequence WORKSTATION_G_SEQ start with 3 increment by 1;

--workstation_program
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (1,1,0,'WP999', 'PCB+TEST站程序', 0,true);
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (2,1,1,'WP999', 'PCB+TEST站程序', 1,false);
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (3,1,2,'WP999', 'PCB+TEST站程序', 2,false);

INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (4,2,0,'WP888', 'TEST站程序', 0,true);
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name , wp_name, sys_sort,sys_header) VALUES (5,2,1,'WP888', 'TEST站程序', 1,false);
SELECT setval('public.workstation_program_seq', 5, true);
DROP sequence IF EXISTS WORKSTATION_PROGRAM_G_SEQ CASCADE;
create sequence WORKSTATION_PROGRAM_G_SEQ start with 3 increment by 1;

--production_sn
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (1, 1, '機種別', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (2, 1, '機種別', '136BU', 'AAA',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (3, 2, '生產廠別', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (4, 2, '生產廠別', 'Beijing', 'B',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (5, 3, '保固期限', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (6, 3, '保固期限', '1_year', '1',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (7, 4, '生產年周', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (8, 4, '生產年周', '[YYWW]', '2118',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (9, 5, 'Panel廠商(面板)', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (10, 5, 'Panel廠商(面板)', 'AGL', 'A',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (11, 6, '流水號', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (12, 6, '流水號', '[000]', '346',false);
SELECT setval('public.production_sn_seq', 12, true);
DROP sequence IF EXISTS PRODUCTION_SN_G_SEQ  CASCADE;
create sequence PRODUCTION_SN_G_SEQ start with 7 increment by 1;

--維修code
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (1, 1, '電池', '', 'EC',true);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (2, 1, '電池', '冒煙', 'EC001',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (3, 1, '電池', '浸水', 'EC002',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (4, 1, '電池', '長香菇', 'EC003',false);

INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (5, 2, '螢幕', '', 'EA',true);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (6, 2, '螢幕', '不亮', 'EA001',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (7, 2, '螢幕', '很亮', 'EA002',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (8, 2, '螢幕', '一下亮一下不亮', 'EA003',false);
SELECT setval('public.maintain_code_seq', 8, true);
DROP sequence IF EXISTS MAINTAIN_CODE_G_SEQ  CASCADE;
create sequence MAINTAIN_CODE_G_SEQ start with 3 increment by 1;


