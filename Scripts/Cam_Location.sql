SELECT oha.order_number, CS.cam_location_id ,CS.site_use_id, category_code, customer_number,payment_term_id
FROM apps.XXG_CUSTOMER_DATA_V2 CS, 
ont.oe_order_headers_all oha 
WHERE CS.site_use_id = oha.invoice_to_org_id
and oha.order_number = 'param_value'