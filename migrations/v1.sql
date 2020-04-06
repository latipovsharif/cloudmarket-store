alter table sold_details add column barcode text;
update sold_details set barcode = (select barcode from products where products.id = sold_details.product_id);
