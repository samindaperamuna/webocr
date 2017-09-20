# webocr
### Web OCR using Tesseract and Google Vision APIs.
This project is aimed at developing a web based OCR solution to upload and categorize images. The current code includes a web based invoice receipt scanner which scans the image using either Google Vision or Tesserat. The mapping is done using an XML based template which can be customized according to the invoice format you're scanning.

#### Current Version
> - Uses Tesseract only.
> - Scans the invoice image and fill the invoice form.
> - Text is processed and map according to a predefined template.
> - Each different type of invoice requires a different template.

#### Template

The default template is as follows.

	<invoice xmlns="org.webocr.template" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="webocr template.xsd">
		<line cols="1" field_name="company_name" />

		<paragraph>
			<line cols="1" field_name="address1" />
			<line cols="1" field_name="address2" />
			<line cols="1" field_name="address3" />
		</paragraph>

		<line cols="2">
			<field name="invoice_type" />
			<field />
		</line>

		<line cols="5">
			<field />
			<field />
			<field name="date" />
			<field name="time" />
			<field name="invoice_number" />
		</line>

		<iterator>
			<line cols="5">
				<field name="item_id" />
				<field name="item_qty" />
				<field />
				<field name="item_price" />
				<field name="total_price" />
			</line>
			<line field_name="item_desc" />
		</iterator>
	</invoice>
