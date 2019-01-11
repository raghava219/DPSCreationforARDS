SELECT OEOH.order_number as "Order_Number",
OEOH.org_id as "BU_ID",
ott.name as "Order_Type",
--"GCM_DP" as "SourceSystem",
ca.account_number as "Base_Customer_Account_Number",
ca.orig_system_reference as "Base_Cust_Orig_System_order",
ca.account_name as "Base_Customer_Name",
ca.attribute19 as "Base_Customer_Name_EN",
org_part.organization_name_phonetic as "Base_Customer_Name_Katakana",
L.ADDRESS1  as "Base_Address1",
L.ADDRESS2 as "Base_Address2",
L.ADDRESS3 as "Base_Address3",
L.ADDRESS4 as "Base_Address4",
L.ATTRIBUTE8 as "Base_Address1_EN",
L.ATTRIBUTE9 as "Base_Address2_EN",
L.ATTRIBUTE10 as "Base_Address3_EN",
L.ATTRIBUTE11 as "Base_Address4_EN",
L.ATTRIBUTE16 as "Base_Address1_Katakana",
L.ATTRIBUTE17 as "Base_Address2_Katakana",
L.ATTRIBUTE18 as "Base_Address3_Katakana",
L.ATTRIBUTE19 as "Base_Address4_Katakana",
L.CITY as "Base_City",
L.ATTRIBUTE15 as "Base_CityEN", 
L.POSTAL_CODE as "Base_Postal_Code",
--L.STATE as "Base_State",
( SELECT
FV_STATE_TL.DESCRIPTION 
FROM
APPS.FND_FLEX_VALUES FV_STATE,
APPS.FND_FLEX_VALUES_TL FV_STATE_TL,
APPS.FND_FLEX_VALUE_SETS FVS_STATE
WHERE
FVS_STATE.FLEX_VALUE_SET_NAME = 'DELL_AP_STATE'
AND FVS_STATE.FLEX_VALUE_SET_ID = FV_STATE.FLEX_VALUE_SET_ID
AND FV_STATE.FLEX_VALUE_ID = FV_STATE_TL.FLEX_VALUE_ID
AND FV_STATE.FLEX_VALUE = L.STATE
AND FV_STATE_TL.LANGUAGE = USERENV ( 'LANG' )
AND FV_STATE.PARENT_FLEX_VALUE_LOW = ORG.ATTRIBUTE2 ) as "Base_State_Description",
( SELECT
FV_STATE.ATTRIBUTE2
FROM
APPS.FND_FLEX_VALUES FV_STATE,
APPS.FND_FLEX_VALUE_SETS FVS_STATE
WHERE
FVS_STATE.FLEX_VALUE_SET_NAME = 'DELL_AP_STATE'
AND FVS_STATE.FLEX_VALUE_SET_ID = FV_STATE.FLEX_VALUE_SET_ID
AND FV_STATE.FLEX_VALUE = L.STATE
AND FV_STATE.PARENT_FLEX_VALUE_LOW = ORG.ATTRIBUTE2 )  as "Base_State_Description_EN", 
ps.addressee as "Base Branch", 
l.address_lines_phonetic as "Base_Branch_Katakana",
L.COUNTRY as "Base_Country",
'Different Query' as "Base_Contact_Title",
'Different Query' as "Base_Contact_Name",
'Different Query' as "Base_Contact_Name_EN",
'Different Query' as "Base_Contact_Name_Katakana",
l.attribute13 as "Base_Gedis_Address_ID",
'Different Query' as "Base_e-mail_address",
'Different Query' as "Base_Contact_Primary_Phone",
'Different Query' as "Base_Contact_Primary_Phone_Ext",
ca.sales_channel_code as "Base_Customer_L8_Channel_Code",
flv.attribute3 as  "Base_Customer_L6_Channel_Code",
flv.description as "Base_Cust_L8_SC_Description",
org.attribute9 as "Global_BUID",
org_part.category_code as "Base_Customer_Category_Code",
'Different Query' as "Base_Cust_Retail/Reseller_Flag"
FROM 
APPS.OE_ORDER_HEADERS_ALL OEOH,
APPS.HZ_CUST_ACCOUNTS CA,
APPS.HZ_CUST_ACCT_SITES_ALL HCAS,
APPS.HZ_PARTY_SITES PS,
APPS.HZ_LOCATIONS L, 
APPS.FND_TERRITORIES_TL TERR,
APPS.HZ_PARTIES ORG_PART,
APPS.HZ_CUSTOMER_PROFILES PRF,
APPS.HR_ALL_ORGANIZATION_UNITS ORG,
APPS.HZ_CUST_PROFILE_CLASSES HCPC,
apps.fnd_lookup_values flv,
apps.oe_transaction_types_tl ott
WHERE
PS.PARTY_ID = CA.PARTY_ID
AND PS.IDENTIFYING_ADDRESS_FLAG = 'Y'
AND PS.PARTY_SITE_ID = HCAS.PARTY_SITE_ID
AND PS.LOCATION_ID = L.LOCATION_ID
AND TERR.TERRITORY_CODE = L.COUNTRY
AND PRF.CUST_ACCOUNT_ID = CA.CUST_ACCOUNT_ID
AND PRF.SITE_USE_ID IS NULL
AND ORG_PART.PARTY_ID = CA.PARTY_ID
AND HCAS.ORG_ID = ORG.ORGANIZATION_ID
AND HCPC.PROFILE_CLASS_ID = PRF.PROFILE_CLASS_ID
AND CA.CUST_ACCOUNT_ID = OEOH.SOLD_TO_ORG_ID
AND flv.lookup_code = ca.sales_channel_code
AND oeoh.order_type_id = ott.transaction_type_id
AND Rownum < 2
AND oeoh.order_number = 'param_value'