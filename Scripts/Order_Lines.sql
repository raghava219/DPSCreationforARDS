select ola.line_number as "Line_Number",
ola.attribute1 as "User_Entered_Tag_Number",
ola.attribute8 as "Start_Date_for_APOS",
ola.attribute2 as "End_Date_for_APOS",
ola.attribute14 as "Model_Code",
--xosp.supplier_part as "Mod/Item_Number",
ola.ordered_item as "SKU_Number",
MSI.attribute1 as "Base_Flag", 
ola.unit_selling_price as "Selling_Price",
xitm.DESCRIPTION as "Sku_Description",
--xosp.DETAIL_SEQ as "Detail_Sequence_Number",
msi.ATTRIBUTE8 as "Sku_type",
ola.attribute9 as "CFI_SI_Number",
ola.unit_list_price as "List_Price",
ola.ordered_quantity as "Ord_Qty",
to_char((from_tz(to_timestamp(to_char(msi.last_update_date, 'YYYY-MM-DD HH:MI:SS PM'), 'YYYY-MM-DD HH:MI:SS PM') ,'America/New_York') at time zone 'UTC'),'YYYY-MM-DD"T"HH24:MI:SS"Z"') as "SKU_EOLDATE",
(CASE 
  WHEN msi.ATTRIBUTE8= 'INSTALL' THEN 'Y'
  WHEN msi.ATTRIBUTE8= 'APOSWARR' THEN 'Y'
  WHEN msi.ATTRIBUTE8= 'WARR' THEN 'Y'
  ELSE 'N'
END) as "Service_indicator"
from 
ont.oe_order_headers_all oha,
ont.oe_order_lines_all ola,
xxeom.xxeom_inv_item_ml_desc xitm,
apps.mtl_system_items msi
where 
oha.ORG_ID = msi.ORGANIZATION_ID
and oha.header_id = ola.header_id
and msi.INVENTORY_ITEM_ID = ola.INVENTORY_ITEM_ID   
and xitm.INVENTORY_ITEM_ID = ola.INVENTORY_ITEM_ID
and xitm.LANG_CODE = 'EN'
and ola.unit_selling_price > 0
and oha.ORDER_NUMBER = 'param_value'
and ola.ordered_item in('sku_order_item')
and msi.segment1 = ola.ordered_item
and msi.organization_id = ola.org_id
order by ola.ordered_item asc