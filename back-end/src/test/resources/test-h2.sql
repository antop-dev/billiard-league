delete
from tbl_mtc;
delete
from tbl_plyr;
delete
from tbl_cnts;
delete
from tbl_mmbr;
delete
from tbl_kko_lgn;
alter table tbl_mmbr
    alter column mmbr_id bigint auto_increment (5);
alter table tbl_plyr
    alter column plyr_id bigint auto_increment (4);
alter table tbl_cnts
    alter column cnts_id bigint auto_increment (3);
/* 카카오 로그인 목록 */
insert into tbl_kko_lgn (lgn_id, lst_cnct_dt, nck_nm, prfl_img_url, prfl_thmb_img_url)
values (1, now(), '안정용', 'https://picsum.photos/640', 'https://picsum.photos/110');
insert into tbl_kko_lgn (lgn_id, lst_cnct_dt, nck_nm, prfl_img_url, prfl_thmb_img_url)
values (2, now(), '김형익', 'https://picsum.photos/640', 'https://picsum.photos/110');
insert into tbl_kko_lgn (lgn_id, lst_cnct_dt, nck_nm, prfl_img_url, prfl_thmb_img_url)
values (3, now(), '김정민', 'https://picsum.photos/640', 'https://picsum.photos/110');
insert into tbl_kko_lgn (lgn_id, lst_cnct_dt, nck_nm, prfl_img_url, prfl_thmb_img_url)
values (4, now(), '홍길동', 'https://picsum.photos/640', 'https://picsum.photos/110');
/* 회원 목록 */
insert into tbl_mmbr (mmbr_id, mmbr_nck_nm, mmbr_hndc, rgst_dt, kko_lgn_id, mgnr_yn)
values (1, '안탑', 22, now(), 1, 'Y');
insert into tbl_mmbr (mmbr_id, mmbr_nck_nm, mmbr_hndc, rgst_dt, mdfy_dt, kko_lgn_id)
VALUES (2, '띠용', 20, now(), now(), 2);
insert into tbl_mmbr (mmbr_id, mmbr_nck_nm, mmbr_hndc, rgst_dt, kko_lgn_id)
values (3, '잼미니', 25, NOW(), 3);
/** 2021 대회 - 진행중 (CNTS_ID = 1) */
insert into tbl_cnts (cnts_id, cnts_nm, cnts_dscr, strt_date, strt_time, end_date, end_time, prgr_stt, max_prtc_prsn,
                      rgst_dt)
values (1, '2021 리그전', '2021.01.01~', parsedatetime('20210101', 'yyyyMMdd'), PARSEDATETIME('000000', 'HHmmss'),
        parsedatetime('20211230', 'yyyyMMdd'), PARSEDATETIME('235959', 'HHmmss'), '2', 32,
        parsedatetime('20191112151145', 'yyyyMMddHHmmss'));

insert into tbl_plyr (plyr_id, cnts_id, mmbr_id, plyr_no, prtc_hndc, plyr_rnkn, plyr_scr)
values (1, 1, 1, 1, 22, 1, 150);
insert into tbl_plyr (plyr_id, cnts_id, mmbr_id, plyr_no, prtc_hndc, plyr_rnkn, plyr_scr)
values (2, 1, 2, 2, 24, 2, 40);
insert into tbl_plyr (plyr_id, cnts_id, mmbr_id, plyr_no, prtc_hndc, plyr_rnkn, plyr_scr)
values (3, 1, 3, 3, 26, 3, 10);
/** 2022 대회 - 모집중 (CNTS_ID = 3) */
insert into tbl_cnts (cnts_id, cnts_nm, cnts_dscr, strt_date, strt_time, end_date, end_time, prgr_stt, max_prtc_prsn,
                      rgst_dt)
values (2, '2022 리그전', '2021.05.01~', parsedatetime('20210101', 'yyyyMMdd'), parsedatetime('000000', 'HHmmss'),
        parsedatetime('20221230', 'yyyyMMdd'), parsedatetime('235959', 'HHmmss'), '0', 128,
        parsedatetime('20200411001145', 'yyyyMMddHHmmss'));
