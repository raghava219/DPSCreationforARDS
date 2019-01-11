select sum(ola.unit_selling_price) as Total_Price
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