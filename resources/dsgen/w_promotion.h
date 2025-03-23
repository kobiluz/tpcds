/* 
 * Legal Notice 
 * 
 * This document and associated source code (the "Work") is a part of a 
 * benchmark specification maintained by the TPC. 
 * 
 * The TPC reserves all right, title, and interest to the Work as provided 
 * under U.S. and international laws, including without limitation all patent 
 * and trademark rights therein. 
 * 
 * No Warranty 
 * 
 * 1.1 TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, THE INFORMATION 
 *     CONTAINED HEREIN IS PROVIDED "AS IS" AND WITH ALL FAULTS, AND THE 
 *     AUTHORS AND DEVELOPERS OF THE WORK HEREBY DISCLAIM ALL OTHER 
 *     WARRANTIES AND CONDITIONS, EITHER EXPRESS, IMPLIED OR STATUTORY, 
 *     INCLUDING, BUT NOT LIMITED TO, ANY (IF ANY) IMPLIED WARRANTIES, 
 *     DUTIES OR CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A PARTICULAR 
 *     PURPOSE, OF ACCURACY OR COMPLETENESS OF RESPONSES, OF RESULTS, OF 
 *     WORKMANLIKE EFFORT, OF LACK OF VIRUSES, AND OF LACK OF NEGLIGENCE. 
 *     ALSO, THERE IS NO WARRANTY OR CONDITION OF TITLE, QUIET ENJOYMENT, 
 *     QUIET POSSESSION, CORRESPONDENCE TO DESCRIPTION OR NON-INFRINGEMENT 
 *     WITH REGARD TO THE WORK. 
 * 1.2 IN NO EVENT WILL ANY AUTHOR OR DEVELOPER OF THE WORK BE LIABLE TO 
 *     ANY OTHER PARTY FOR ANY DAMAGES, INCLUDING BUT NOT LIMITED TO THE 
 *     COST OF PROCURING SUBSTITUTE GOODS OR SERVICES, LOST PROFITS, LOSS 
 *     OF USE, LOSS OF DATA, OR ANY INCIDENTAL, CONSEQUENTIAL, DIRECT, 
 *     INDIRECT, OR SPECIAL DAMAGES WHETHER UNDER CONTRACT, TORT, WARRANTY,
 *     OR OTHERWISE, ARISING IN ANY WAY OUT OF THIS OR ANY OTHER AGREEMENT 
 *     RELATING TO THE WORK, WHETHER OR NOT SUCH AUTHOR OR DEVELOPER HAD 
 *     ADVANCE NOTICE OF THE POSSIBILITY OF SUCH DAMAGES. 
 * 
 * Contributors:
 * Gradient Systems
 */ 
#ifndef W_PROMOTION_H
#define W_PROMOTION_H
#include "constants.h"
/*
 * PROMOTION table structure 
 */
struct __attribute__((packed)) W_PROMOTION_TBL {
	ds_key_t	p_promo_sk;
	//char		p_promo_id[RS_BKEY + 1];
    char		*p_promo_id;
	ds_key_t	p_start_date_id;
	ds_key_t	p_end_date_id;
	ds_key_t	p_item_sk;
	//char		p_promo_name[RS_P_PROMO_NAME + 1];
    char		*p_promo_name;
    //char		p_channel_details[RS_P_CHANNEL_DETAILS + 1];
    char		*p_channel_details;
    char		*p_purpose;
	int			p_response_target;
	int			p_channel_dmail;
	int			p_channel_email;
	int			p_channel_catalog;
	int			p_channel_tv;
	int			p_channel_radio;
	int			p_channel_press;
	int			p_channel_event;
	int			p_channel_demo;
	int			p_discount_active;
    decimal_t	p_cost;
};

int mk_w_promotion(void *pDest, ds_key_t kIndex);
int pr_w_promotion(void *pSrc);
int ld_w_promotion(void *pSrc);
#endif

